package mepa;

import excepciones.MepaException;

import java.io.IOException;
import java.util.Hashtable;
import java.util.ArrayList;
import main.Testeable;

/**
 * Realiza el analisis sintactico de las sentencias de un programa MEPa.
 * A la vez, realiza la carga de las instrucciones y resuelve las direcciones
 * para las etiquetas del programa
 *
 * @since jdk 1.6
 * @see mepa.ALMepa
 * @see mepa.Instruccion
 */
public class ASMepa implements Testeable{
    /** Analizador Lexico */
    private ALMepa aLexico;
    
    /** Ultimo token leido */
    private TokenMepa token;
    
    /** Permite restaurar la lectura de un token */
    private boolean restaurarToken = false;
    
    /** Tabla que mantiene la asociacion de etiquetas y direcciones <string,int> o la
     * asociacion entre etiquetas aun no declaradas y un vector de instrucciones
     * que la referencian <String, list>.
     */
    private Hashtable<String,Object> etiquetas = new Hashtable<String,Object>();
    
    /** Tabla que mantiene las etiquetas aun no declaradas
     *  Permite chequear, luego del analisis sintactico, si no quedan etiquetas
     *  que fueron referenciadas pero no declaradas
     */
    private Hashtable<String,Integer> etiquetasNoDeclaradas = new Hashtable<String,Integer>();
    
    /**
     * Mantiene las instrucciones que va cargando a medida que progresa el analisis sintactico
     */
    private ArrayList<Instruccion> instrucciones = new ArrayList<Instruccion>();
    
    /** Permite mostrar mensajes en modo debug */
    private  boolean debug = false;
    
    /**  Constructor vacio */
    public ASMepa() {
        String prop = System.getProperty("debug");        
        if(prop!=null){                                    
            debug = prop.equalsIgnoreCase("on");                                    
        }
    }
    
    /**
     * Construye le analizador sintactico con el analizador lexico
     * @param alexico analizador lexico
     */
    public ASMepa(ALMepa alexico) {
        this();
        this.aLexico = alexico;
    }
    
    /**
     * Constructor
     * @param strFile archivo a analizar
     * @throws java.lang.Exception si ocurre algun error
     */
    public ASMepa(String strFile)  throws IOException{
        this();
        setAnalizadorLexico(new ALMepa(strFile));
    }
    
    /**
     * Invoca al analizador lexico para que nos devuelva
     * el siguiente token.
     */
    private void nextToken() throws Exception{
        if  (!restaurarToken){
            token = aLexico.nextToken();            
        } else
            restaurarToken= false;
    }
    
    /**
     * Permite setear un analizador lexico
     *
     * @param aLexico el analizador lexico a setear
     * @throws java.io.IOException  si hubo algun error de E/S
     */
    public void setAnalizadorLexico(ALMepa aLexico) throws IOException{
        if(this.aLexico!=null){            
            this.aLexico.cerrarLector();
            this.aLexico = null;
        }
        
        this.aLexico = aLexico;
    }
    
    /**
     * Setea el archivo para testear
     * @param strFile nombre de archivo a testear
     * @throws java.io.IOException si ocurre algun error
     */
    public void setSourceFile(String strFile)throws IOException{
        setAnalizadorLexico(new ALMepa(strFile));
    }
    
    /**
     * Implementa el metodo finish de la interface Testeable
     */
    public void finish(){
        try {
            aLexico.cerrarLector();
        } catch (IOException ex) { }
    }
    
    /**
     * Comprueba si el archivo tiene la extension adecuada
     * @param strFile nombre de archivo a verificar
     * @return true si es una extension valida
     */
    public boolean validaExtension(String strFile) {
        return strFile.endsWith(".mep");
    }
    
    /**
     * ProgramaMepa::= INPP <listaSentencias> EOF
     *
     * @throws java.lang.Exception si ocurre un error
     */
    public void run() throws Exception{
        if(debug) System.out.println("[MEPA] Cargando instrucciones...");
        nextToken();
        //el programa debe empezar con INPP
        if(token.codigo != TokenMepa.INPP) throw new MepaException(7,token.numLinea);
        
        instrucciones.add(new Instruccion(token.codigo,token.numLinea,token.lexema));
        listaSentencias();
        nextToken();
        
        if(token.codigo!=TokenMepa.EOF) throw new MepaException(8,token.numLinea);
        
        //Chequeo que todas las etiquetas referenciadas estan declaradas
        if(!etiquetasNoDeclaradas.isEmpty()) throw new MepaException(9);
        
        if(debug) System.out.println("[MEPA] Analisis y carga....OK");
    }
    
    /**
     * <listaSentencias>::= <etiqueta> <restoListaSentencia> |
     * <instruccion> <listaSentencias>	|
     * PARA
     */
    private void listaSentencias() throws Exception{
        nextToken();
        if(token.codigo == TokenMepa.ETIQ){
            ingresarEtiqueta();
            restoListaSentencia();
        } else if(token.esInstruccion() && token.codigo!=TokenMepa.PARA){
            restaurarToken = true;
            instruccion();
            listaSentencias();
        } else if(token.codigo == TokenMepa.PARA){
            instrucciones.add(new Instruccion(token.codigo,token.numLinea,token.lexema));
        } else{
            //ERROR
            int codErr =-1;
            if(token.codigo == TokenMepa.EOF)
                //todo prog. debe terminar con PARA
                codErr = 10;
            else if(token.codigo==TokenMepa.INPP){
                //la instruccion INPP solo puede ir al comiezo
                codErr = 11;
            }else{
                //argumentos invalidos
                codErr = 12;
            }
            throw new MepaException(codErr,token.numLinea);
        }
    }
    
    /**
     * <restoListaSentencia>::= <instruccion><listaSentencias> | PARA
     */
    private void restoListaSentencia() throws Exception{
        nextToken();
        if(token.esInstruccion()){
            restaurarToken = true;
            instruccion();
            listaSentencias();
        } else if(token.codigo == TokenMepa.PARA){
            instrucciones.add(new Instruccion(token.codigo,token.numLinea,token.lexema));
        } else{
            //ERROR
            int codError;
            if(token.codigo==TokenMepa.EOF){
                codError = 10;
            }else if (token.codigo==TokenMepa.ETIQ){
                codError = 13;
            }else if (token.codigo==TokenMepa.INPP){
                codError = 11;
            }else{//token="," a "-" o a Num
                codError = 14;
            }
            throw new MepaException(codError,token.numLinea);
        }
    }
    
    /**
     * Carga una instruccion
     */
    private void instruccion()throws Exception{
        nextToken();
        switch(token.codigo){
//Aritmeticas
            case TokenMepa.APCT:
                //APCT k
                TokenMepa t = token;
                int op1 = numero();
                instrucciones.add(new Instruccion(t.codigo,t.numLinea,t.lexema,op1));
                break;
            case TokenMepa.SUMA:
            case TokenMepa.SUST:
            case TokenMepa.MULT:
            case TokenMepa.DIVI:
            case TokenMepa.MODU:
            case TokenMepa.UMEN:
//logicas
            case TokenMepa.CONJ:
            case TokenMepa.DISJ:
            case TokenMepa.NEGA:
            case TokenMepa.CMME:
            case TokenMepa.CMMA:
            case TokenMepa.CMIG:
            case TokenMepa.CMDG:
            case TokenMepa.CMNI:
            case TokenMepa.CMYI:
            
//para sent case (duplicar tope)
            case TokenMepa.DUPT:
                instrucciones.add(new Instruccion(token.codigo,token.numLinea,token.lexema));
                break;
//saltos
            case TokenMepa.DSVS:
                //DSVS eti
                leerEtiqueta();
                break;
            case TokenMepa.DSVF:
                //DSVF eti
                leerEtiqueta();
                break;
           case TokenMepa.DSVV:
                //DSVV eti
                leerEtiqueta();
                break;
              
//entrada - salida
            case TokenMepa.LEER:                
                instrucciones.add(new Instruccion(token.codigo,token.numLinea,token.lexema));
                break;
            case TokenMepa.IMPR:
            case TokenMepa.LELN:
            case TokenMepa.IMLN:            
                instrucciones.add(new Instruccion(token.codigo,token.numLinea,token.lexema));
                break;
//variables
            case TokenMepa.APVL:
                //APVL m , n
                leer2Parametros(false,true);
                break;
            case TokenMepa.ALVL:
                //ALVL m , n
                leer2Parametros(false,true);
                break;
//programas y procedimientos
            case TokenMepa.INPP:
                instrucciones.add(new Instruccion(token.codigo,token.numLinea,token.lexema));
                break;
            case TokenMepa.ENPR:
                //ENPR k
                leer1Parametro();
                break;
            case TokenMepa.LLPR:
                //LLPR eti
                leerEtiqueta();
                break;
                
//RESERVAR Y LIBERAR MEMORIA
            case TokenMepa.RMEM:
                //RMEM m
                leer1Parametro();
                break;
            case TokenMepa.LMEM:
                //LMEM m
                leer1Parametro();
                break;
                
            case TokenMepa.RTPR:
                //RTPR k, n
                leer2Parametros(false,false);
                break;
                
//pasaje de param por ref
            case TokenMepa.APDR:
                //APDR m , n
            case TokenMepa.APVI:
                //APVI m, n
            case TokenMepa.ALVI:
                //ALVI m , n
//Arreglos
            case TokenMepa.APAR:
                //APAR m,n
            case TokenMepa.ALAR:
                //ALAR m,n
                leer2Parametros(false,true);
                break;
            case TokenMepa.PUAR:
                //PUAR m, n, l
                //leer3Parametros();              break;
            case TokenMepa.POAR:
                //POAR m,n,l
                leer3Parametros();
                break;
//Arreglos por ref
            case TokenMepa.APAI:
                //APAI m,n
                leer2Parametros(false,true);
                break;
            case TokenMepa.ALAI:
                //ALAI m,n
                leer2Parametros(false,true);
                break;
            case TokenMepa.PUAI:
                //PUAI m,n,l
            case TokenMepa.POAI:
                //POAI m,n,l
                leer3Parametros();
                break;
            case TokenMepa.APDC:
                //APDC m,n
                leer2Parametros(false,true);
                break;
//Arreglos: control de indice inferior y superior
            case TokenMepa.CONT:
                //CONT li,ls
                leer2Parametros(true,true);
                break;

            case TokenMepa.DIVC:
            case TokenMepa.NADA:
            case TokenMepa.PARA:            
            case TokenMepa.ALDR: 
            case TokenMepa.IMLV:
                instrucciones.add(new Instruccion(token.codigo,token.numLinea,token.lexema));
                break;
            default: 
                throw new Exception("instruccion no reconocida!, nombre="+token.lexema+" codigo="+token.codigo);
        }
        
    }
    
    /** Asume que ya hay un token leido */
    private void leerEtiqueta() throws Exception{
        TokenMepa t = token;
        nextToken();
        if(token.codigo!=TokenMepa.ETIQ) throw new MepaException(16,t.numLinea);
        Instruccion inst = new Instruccion(t.codigo,t.numLinea,t.lexema,token.lexema);
        resolverEtiqueta(inst,token.numLinea);
        instrucciones.add(inst);
    }
    
    /** Analiza instrucciones de un solo parametro */
    private void leer1Parametro() throws Exception{
        TokenMepa t = token;
        nextToken();
        if(token.codigo!=TokenMepa.NUMERO) throw new MepaException(15,t.numLinea);
        int op1 = convertirAEntero(token.lexema);
        instrucciones.add(new Instruccion(t.codigo,t.numLinea,t.lexema,op1));
    }
    
    /** 
     * Analiza instrucciones de dos parametros 
     * @param b1 si es true el primer parametro puede ser un numero negativo
     * @param b2 si es true el segundo parametro puede ser un numero negativo
     */
    private void leer2Parametros(boolean b1, boolean b2) throws Exception{
        TokenMepa t = token;
        int op1 =0;
        if(!b1){
            nextToken();
            if(token.codigo!=TokenMepa.NUMERO) throw new MepaException(15,t.numLinea);
            op1 = convertirAEntero(token.lexema);
        } else
            op1 = numero();
        
        nextToken();
        if(token.codigo!=TokenMepa.COMA) throw new MepaException(17,token.numLinea);
        
        int op2=0;
        if(!b2){
            nextToken();
            if(token.codigo!=TokenMepa.NUMERO) throw new MepaException(18,token.numLinea);
            op2 = convertirAEntero(token.lexema);
        } else
            op2= numero();
        instrucciones.add(new Instruccion(t.codigo,t.numLinea,t.lexema,op1,op2));
    }
    
    /** Analiza instrucciones de tres parametros */
    private void leer3Parametros() throws Exception{
        TokenMepa t = token;
        nextToken();
        if(token.codigo!=TokenMepa.NUMERO) throw new MepaException(15,t.numLinea);
        int op1 = convertirAEntero(token.lexema);
        nextToken();
        if(token.codigo!=TokenMepa.COMA) throw new MepaException(17,token.numLinea);
        /*nextToken();
        if(token.codigo!=TokenMepa.NUMERO) throw new MepaException(18,token.numLinea);
        int op2 = convertirAEntero(token.lexema);
         */
        int op2 = numero();
        nextToken();
        if(token.codigo!=TokenMepa.COMA) throw new MepaException(19,token.numLinea);
        nextToken();
        if(token.codigo!=TokenMepa.NUMERO) throw new MepaException(20,token.numLinea);
        int op3 = convertirAEntero(token.lexema);
        instrucciones.add(new Instruccion(t.codigo,t.numLinea,t.lexema,op1,op2,op3));
    }
    
    /**
     * <numero> ::= NUMERO | - NUMERO
     * atributo sintetizado int
     */
    private int numero()throws Exception{
        nextToken();
        if(token.codigo==TokenMepa.NUMERO){//Num
            return convertirAEntero(token.lexema);//pos. error por overflow
            // en conversiona  Int
        } else{//-NUMERO
            if(token.codigo != TokenMepa.MENOS)//ERROR
                throw new MepaException(21,token.numLinea);
            nextToken();
            
            if(token.codigo != TokenMepa.NUMERO)//ERROR
                throw new MepaException(22,token.numLinea);
            
            //Posible error por overflow
            return convertirAEntero("-"+token.lexema);
        }
    }
    
    /**
     * Convierte un string a entero capturando posibles
     * errores de numeros mal formados
     * @return el numero convertido
     */
    private int  convertirAEntero(String num)throws Exception{
        int res = 0;
        try{
            res = Integer.parseInt(num);
        } catch(NumberFormatException ex){
            //overflow
            throw new MepaException(23);
        }
        return res;
    }
    
    /**
     * Ingresa la declaracion de una eiqueta. Dispara error si la etiqueta ya fue declarada
     * previamente.
     * Modifica la instrucciones anteriores que la referencian (si hay alguna) y la elimina
     * de la tabla de etiquetas no referenciadas
     */
    private void ingresarEtiqueta()throws Exception{
        Object ob = etiquetas.get(token.lexema);
        int pos = token.numLinea;//instrucciones.size()-1;
        if(ob == null){
            etiquetas.put(token.lexema,new Integer(pos));
        } else if(ob instanceof ArrayList){
            ArrayList <Instruccion> list = (ArrayList<Instruccion>)ob;
            
            for(int i = 0; i<list.size(); i++)
                list.get(i).op1 = pos;
            
            etiquetas.put(token.lexema, new Integer(pos));
        } else{
            //etiqueta ya declarada
            throw new MepaException(24,token.numLinea);
        }
        etiquetasNoDeclaradas.remove(token.lexema);
    }
    
    /**
     * @param inst la instruccion que hace ref a la etiqueta
     * @param line nro de linea de la instruccion
     */
    private void resolverEtiqueta(Instruccion inst, int line){
        Object ob = etiquetas.get(inst.etiqueta);
        if(ob == null){
            inst.op1 = -1;
            ArrayList<Instruccion> l = new ArrayList<Instruccion>();
            l.add(inst);
            etiquetas.put(inst.etiqueta,l);
            etiquetasNoDeclaradas.put(inst.etiqueta,new Integer(line));
        } else if(ob instanceof ArrayList){
            ArrayList<Instruccion> list = (ArrayList<Instruccion>)ob;
            inst.op1 = -1;
            list.add(inst);
        }else{
            inst.op1 = ((Integer)ob).intValue();
        }
    }
    
    /**
     * Lo invoca la MEPa para procesar las instrucciones
     * @return las instrucciones cargadas
     */
    public ArrayList<Instruccion> getInstrucciones(){
        return instrucciones;
    }
}
