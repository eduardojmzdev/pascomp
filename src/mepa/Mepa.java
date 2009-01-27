
package mepa;

import excepciones.MepaException;

import java.io.IOException;
import java.util.ArrayList;
import main.Testeable;

/**
 * Mepa.java
 * Maquina Virtual del lenguaje  
 * 
 * @since jdk1.6
 */

public class Mepa implements Testeable{
    /** Analizador sintactico */
    private ASMepa asint = null;
            
    /** Tamaño maximo de la Pila */
    private static final int MAX_PILA = 65536;
    
    /** Tamaño maximo del Display */
    private static final int MAX_DISPLAY = 10;
    
    /** Tope de pila */
    private int s;
    
    /** Contador de programa */
    private int i;
    
    /** Display */
    private int[] D = null;
    
    /** Pila */
    private int[] M = null;
    
    /** Instrucciones */
    private Instruccion[] P;
    
    /** Para mensajes de debug */
    public static boolean debug = false;
       
    
    /**
     * Constructor
     * Carga los parametros de configuracion
     */
    public Mepa(){
        String prop = System.getProperty("debug");        
        if(prop!=null){                        
            debug = prop.equalsIgnoreCase("on");                        
        }
    }
    
    /**
     * Constructor
     * 
     * @param file nombre de archivo a ejecutar
     * @throws java.io.IOException 
     */
    public Mepa(String file) throws IOException{
        setSourceFile(file);
    }
    
    /**
     * Implementacion de la interface Testeable
     * @param file 
     * @throws java.io.IOException 
     */
    public void setSourceFile(String file)throws IOException{
        /*if(asint!=null){
            asint.finish();
            asint = null;
        }  */      
        asint = new ASMepa(file);
        D = new int[MAX_DISPLAY];
        M = new int[MAX_PILA];
    }
    
    /** Implementacion de la interface Testeable */
    public void finish(){
        if(asint!=null) asint.finish();        
        asint = null;
    }
    
    /** 
     * Valida la extencion de un archivo
     * @param strFile nombre de archivo a verificar
     * @return true si el archivo tiene la extension correcta
     */
    public boolean validaExtencion(String strFile) {
        return strFile.endsWith(".mep") ;
    }
    
       
    /**
     * Metodo principal
     * Toma una instruccion y la ejecuta
     * 
     * @throws java.lang.Exception si ocurre algun error
     */
    public void run() throws Exception{
        //Analiza sintacticamente y carga las instrucciones
        try{
            asint.run();
            ArrayList<Instruccion> aux = asint.getInstrucciones();
            P = new Instruccion[aux.size()+1];
            
            for(int i = 0;i<aux.size();i++) P[i+1] = aux.get(i);
                                   
            if(debug) System.out.println("[MEPA] Ejecutando...");
            Instruccion inst=null;
            i = 1;
            do{
                inst = P[i++];
                ejecutar(inst);
            }
            while(inst.opc != TokenMepa.PARA);
            
        }catch(java.lang.OutOfMemoryError ex){
            throw new MepaException(28);
        }
    }
    
    /**
     * Ejecuta una instruccion
     *      
     * @param instruccion instruccion a ejecutar
     * @throws java.lang.Exception si hubo error
     */
    private void ejecutar(Instruccion instruccion)throws Exception{
        
        try{
            switch(instruccion.opc){
                //Aritmeticas
                case TokenMepa.APCT:
                    s++; M[s] = instruccion.op1;
                    break;
                case TokenMepa.SUMA:
                    M[s-1] += M[s]; s--;
                    break;
                case TokenMepa.SUST:
                    M[s-1] -= M[s]; s--;
                    break;
                case TokenMepa.MULT:
                    M[s-1] *= M[s]; s--;
                    break;
                case TokenMepa.DIVI:
                    M[s-1] /= M[s]; s--;
                    break;
                case TokenMepa.MODU:
                    M[s-1] = M[s-1] % M[s]; s--;
                    break;
                case TokenMepa.UMEN:
                    M[s] = (0-M[s]);
                    break;
                    //logicas
                case TokenMepa.CONJ:
                    M[s-1] = ((M[s-1]==1 && M[s]==1) ? 1 : 0); s--;
                    break;
                case TokenMepa.DISJ:
                    M[s-1] = ((M[s-1]==1 || M[s]==1) ? 1 : 0); s--;
                    break;
                case TokenMepa.NEGA:
                    M[s] = 1-M[s];
                    break;
                case TokenMepa.CMME:
                    M[s-1] = (M[s-1] < M[s]) ? 1 : 0; s--;
                    break;
                case TokenMepa.CMMA:
                    M[s-1] = (M[s-1] > M[s]) ? 1 : 0; s--;
                    break;
                case TokenMepa.CMIG:
                    M[s-1] = (M[s-1] == M[s]) ? 1 : 0; s--;
                    break;
                case TokenMepa.CMDG:
                    M[s-1] = (M[s-1] != M[s]) ? 1 : 0; s--;
                    break;
                case TokenMepa.CMNI:
                    M[s-1] = (M[s-1] <= M[s]) ? 1 : 0; s--;
                    break;
                case TokenMepa.CMYI:
                    M[s-1] = (M[s-1] >= M[s]) ? 1 : 0; s--;
                    break;
//para sent case (duplicar tope)
                case TokenMepa.DUPT:
                    s++; M[s] = M[s-1];
                    break;
//saltos
                case TokenMepa.DSVS:
                    i = instruccion.op1;
                    break;
                case TokenMepa.DSVF:
                    if (M[s] == 0)
                        i = instruccion.op1;
                    s--;
                    break;
//entrada - salida
                case TokenMepa.LEER:
                    leer(instruccion);
                    break;
                case TokenMepa.IMPR:
                    System.out.print(M[s]); s--;
                    break;
                case TokenMepa.LELN:
                    leer(instruccion);
                    break;
                case TokenMepa.IMLN:
                    System.out.println(M[s]); s--;
                    break;                                    
//variables
                case TokenMepa.APVL:
                    //Aca falla cuando op2 es negativo
                    s++; M[s] = M[D[instruccion.op1] + instruccion.op2];
                    break;
                case TokenMepa.ALVL:
                    M[D[instruccion.op1] + instruccion.op2] = M[s]; s--;
                    break;
//programas y procedimientos
                case TokenMepa.INPP:
                    s = -1; D[0] = 0;
                    break;
                case TokenMepa.ENPR:
                    s++;
                    M[s] = D[instruccion.op1] ;
                    D[instruccion.op1] = s+1;
                    break;
                case TokenMepa.LLPR:
                    s++;
                    M[s] = i;
                    i = instruccion.op1;
                    break;
                case TokenMepa.RMEM:
                    s+= instruccion.op1;
                    break;
                case TokenMepa.LMEM:
                    s-= instruccion.op1;
                    break;
                case TokenMepa.RTPR:
                    D[instruccion.op1] = M[s];
                    i = M[s-1];
                    s = s - (instruccion.op2 + 2);
                    break;
//pasaje de param por ref
                case TokenMepa.APDR:
                    s++; M[s] = D[instruccion.op1] + instruccion.op2;
                    break;
                case TokenMepa.APVI:
                    s++; M[s] = M[M[D[instruccion.op1] + instruccion.op2]];
                    break;
                case TokenMepa.ALVI:
                    M[M[D[instruccion.op1] + instruccion.op2]] = M[s]; 	s--;
                    break;
//Arreglos
                case TokenMepa.APAR:
                    M[s] = M[D[instruccion.op1] + instruccion.op2 + M[s]];
                    break;
                case TokenMepa.ALAR:
                    M[D[instruccion.op1] + instruccion.op2 + M[s-1]] = M[s];
                    s-=2;
                    break;
                case TokenMepa.PUAR:
                    for(int k = 1; k<=instruccion.op3; k++){
                        s++; M[s] = M[D[instruccion.op1]+ instruccion.op2 + k - 1];
                    }
                    break;
                case TokenMepa.POAR:
                    for(int k = instruccion.op3; k>=1; k--){
                        M[D[instruccion.op1] + instruccion.op2 + k - 1] = M[s];
                        s--;
                    }
                    break;
//Arreglos por ref
                case TokenMepa.APAI:
                    M[s] = M[M[D[instruccion.op1]+instruccion.op2]+M[s]];
                    break;
                case TokenMepa.ALAI:
                    M[M[D[instruccion.op1]+instruccion.op2]+M[s-1]] = M[s];
                    s-=2;
                    break;
                case TokenMepa.PUAI:
                    for(int comp = 0; comp<instruccion.op3; comp++){
                        s++;
                        M[s]= M[M[D[instruccion.op1]+instruccion.op2]+ comp];
                    }
                    break;
                case TokenMepa.POAI:
                    for(int comp =instruccion.op3-1; comp>=0;comp--){
                        M[M[D[instruccion.op1]+instruccion.op2]+ comp] = M[s];
                        s--;
                    }
                    break;
                case TokenMepa.APDC:
                    M[s] = D[instruccion.op1]+ instruccion.op2+ M[s];
                    break;
//Arreglos: control de indice inferior y superior
                case TokenMepa.CONT:
                    if(M[s]<instruccion.op1 || M[s]>instruccion.op2)
                        throw new MepaException(0,"->"+instruccion.nombre+" "+instruccion.op1+","+instruccion.op2+" valor en pila ="+M[s],instruccion.numLinea);
                    break;
//Division por cero
                case TokenMepa.DIVC:
                    if(M[s] == 0) throw new MepaException(1,"->"+instruccion.nombre,instruccion.numLinea);
                    break;
                    
                case TokenMepa.NADA:
                    break;
                    
                case TokenMepa.PARA:                    
                    break;
                    
                case TokenMepa.ALDR:                     
                    M[M[s-1]] = M[s]; s-=2;
                    break;
                case TokenMepa.DSVV: 
                    if(M[s]==1)  i = instruccion.op1; s--;
                    break;
                case TokenMepa.IMLV:
                    System.out.println("");
                    break;    
            }//end switch
            
        }catch (IndexOutOfBoundsException e){
            throw new MepaException(27,"->"+instruccion.nombre,instruccion.numLinea);
        }
    }//end ejecutar
    
    /**     
     * se usa para leer 
     */
    private int ultCarLeido =' ';
    
    /**
     * Lee un valor del teclado
     * @param ins 
     * @throws java.lang.Exception 
     */
    private void leer(Instruccion ins)throws Exception{               
        int valLeido = 0;
        int dig = ultCarLeido;
        boolean nega = false;
        //valor leido en representacion long
        long valLongLeido = 0;
        //valor aboluto mayor permitido,
        long valLongPer = Integer.MAX_VALUE;
        
        //MAXINT o MAXINT +1 si es negativo
        while(dig=='\r'||dig=='\n'||dig=='\t'||dig==' ' || dig=='+'){
            dig = System.in.read();
            ultCarLeido = dig;
        }
        
        if(dig == '-'){
            nega = true;
            valLongPer++; //MAXIXT +1;
            dig = System.in.read();
            ultCarLeido = dig;
        }
        
        if(dig>57 || dig<48)//ERROR -Si despues de leer el menos no viene un digito
            throw new MepaException(25, ins.numLinea);
        
        while(dig>=48 && dig<=57){
            valLongLeido=(valLongLeido*10)+(dig-48);
            valLeido=(valLeido*10)+(dig-48);
            if(valLongLeido>valLongPer) //ERROR overflow
                throw new MepaException(26,ins.numLinea);
            dig = System.in.read();
            ultCarLeido = dig;
        }
        
        if(ins.opc == TokenMepa.LELN){
            while(ultCarLeido !='\n'){//descara todo hasta final de linea
                ultCarLeido=System.in.read();
            }
        }
        
        if (nega) 	valLeido= - valLeido;
        s++;
        M[s]=valLeido;
    }
}
