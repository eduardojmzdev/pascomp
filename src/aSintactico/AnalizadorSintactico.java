
package aSintactico;

import Utils.BufferedFileWriter;
import aLexico.AnalizadorLexico;
import aLexico.Token;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import exceptions.*;
import aSintactico.tipos.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Properties;
import main.Testeable;

/**
 *
 * <P>Analizador Sintactico para el lenguaje Mini-Pascal. Este lenguaje es un
 * subconjunto del lenguaje Pascal Standar. (ver manual de usuario)
 *<pre>
 * <br><b>Modo uso:</b>
 *      AnalizadorSintactico as = new AnalizadorSintactico(new AnalizadorLexico("program.pas"));
 *      as.start();
 * </pre>
 *<p>Esta clase implementa la interface <code>Testeable</code> para poder ser
 *testeada con uno o varios archivos simultaneamente.
 *<br><b>Ejemplo:</b>
 *<pre>
 *      //testea el archivo arch.pas
 *      Testeador t= new Testeador("arch.pas",new AnalizadorSintactico());
 *       t.start();
 *
 *      //testea los archivos dentro del directorio actual (y subdirectorios)
 *      Testeador t= new Testeador(".",new AnalizadorSintactico());
 *       t.start();
 *
 *</pre>
  * @author Agustin Ramone
 *
 * @see aLexico.AnalizadorLexico
 * @see aSintactico.TablaSimbolos
 * @since jdk 1.5
 */
public final class AnalizadorSintactico implements Testeable{
    
    /**
     * Token devuelto por el analizador lexico luego de una
     * llamada al metodo <code>nextToken()</code>
     */
    private Token token = null;
    
    /**
     * Analizador Lexico del lenguaje fuente
     */
    private AnalizadorLexico aLexico = null;
    
    /**
     * Archivo Mepa generado
     */
    private BufferedFileWriter fileMepa = null;
    
    /**
     * Tabla de Simbolos
     */
    private EntryTable TS = null;
    
    /**
     * Cuando vale true permite saltearse la proxima lectura de un token.
     * Es decir, nextToken no cambia el <code>token</code> actual
     */
    private boolean restaurarToken = false;
    
    /** permite generar un nombre de etiqueta diferente cada vez
     *  que se solicita.
     */
    private int numEtiqueta = 1;

    /** 
     * Se mantiene en false a menos que se produzca una excepcion
     * o un error de compilacion
     */
    private boolean HUBO_ERROR = false;
    
    /** Determina si se genera codigo itermedio o no*/
    private boolean genCodigo = true;
    
    /** Permite mostrar mensajes para debugging*/
    private boolean debug = false;
    
    /** Conjunto de extensiones de archivo validas para analizar */
    private HashSet<String> extensionesValidas = null;
    
    /**
     * Constructor sin parametros
     *  Carga los parametros del archivo de configuracion
     */
    public AnalizadorSintactico() {
        FileInputStream f;
        try {
            f = new FileInputStream("config.conf");
            Properties p = System.getProperties();
            p.load(f);
            System.setProperties(p);
            f.close();            
        } catch (FileNotFoundException ex) { 
            System.out.println(ex.getMessage());
        } catch(IOException ex){ 
            System.out.println(ex.getMessage());
        }
        
        String prop = System.getProperty("gen_codigo");
        if(prop!=null){            
            genCodigo = prop.equalsIgnoreCase("on");
        }
        
        prop = System.getProperty("debug");        
        if(prop!=null){                        
            debug = prop.equalsIgnoreCase("on");            
        }
        
        prop = System.getProperty("file_extension");
        if(prop!=null){            
            if(prop.lastIndexOf(",")>0){
                String cad[] = prop.split(",");
                extensionesValidas = new java.util.HashSet<String>(cad.length);
                for(String i:cad) 
                    extensionesValidas.add(i.toLowerCase());                 
                
            }else{
                extensionesValidas = new java.util.HashSet<String>(1);
                extensionesValidas.add(prop.toLowerCase());                
            }
        }else{
            extensionesValidas = new java.util.HashSet<String>(1);
            extensionesValidas.add(".pas");
        }                
    }
    
    /**
     * Permite construir un analizador sintactico
     * @param aLexico analizador lexico del codigo fuente
     */
    public AnalizadorSintactico(AnalizadorLexico aLexico){
        this();
        this.aLexico = aLexico;
    }
    
    /**
     * Permite construir un analizador sintactico a partir
     * del nombre del archivo fuente
     * 
     * @param strFile nombre del archivo de codigo fuente
     * @throws java.io.IOException 
     */
    public AnalizadorSintactico(String strFile) throws IOException{        
        this();
        setSourceFile(strFile);
    }
    
    /**
     * Permite cambiar el archivo fuente a analizar
     * 
     * @param strFile nombre de archivo a analizar
     * @throws java.io.IOException 
     */
    public void setSourceFile(String strFile) throws IOException{
        setAnalizadorLexico(new AnalizadorLexico(strFile));
        if(fileMepa!=null){
            fileMepa.close();
            fileMepa = null;
        }
        if(genCodigo)
            fileMepa = new BufferedFileWriter(strFile+".mep");        
        
        numEtiqueta = 1;
        restaurarToken = false;
        token = null;
        HUBO_ERROR= false;
    }
    
    /**
     * 
     * @return 
     */
    public boolean testOK(){
        return HUBO_ERROR;
    }
    
    /** 
     * retorna el objeto File del archivo mepa generado 
     * @return archivo
     */
    public File getOutputFile() {
        if(fileMepa!=null) return fileMepa.getFile();
        return null;
    } 
    /**
     * Invoca al analizador lexico para que nos devuelva
     * el siguiente token.
     * @throws java.io.IOException 
     * @throws exceptions.LexicException 
     */
    private void nextToken() throws Exception{
        if  (!restaurarToken)
            token = aLexico.nextToken();
        else
            restaurarToken= false;
    }
    
    /**
     * Permite setear un analizador lexico
     *
     * @param aLexico el analizador lexico a setear
     * @throws java.io.IOException  si hubo algun error de E/S
     */
    public void setAnalizadorLexico(AnalizadorLexico aLexico) throws IOException{
        if(this.aLexico!=null){
            this.aLexico.cerrarLector();
            this.aLexico = null;
        }
        this.aLexico = aLexico;
    }
    
    /**
     * Implementa el metodo finish de la interface Testeable
     * Se invoca al terminar la ejecucion, cierra los archivos abiertos
     */
    public void finish(){        
        try {
            if(aLexico!=null){
                aLexico.cerrarLector();
                aLexico = null;
            }
            if(fileMepa!=null){
                fileMepa.close();
                if(HUBO_ERROR && !debug){                                        
                    //Ocurrio un error.. elimino el archivo mepa 
                    java.io.File file = fileMepa.getFile();
                    if(file.exists()) file.delete();
                }                
            }  
            
        } catch (IOException ex) { }
    }
    
    /**
     * Comprueba si el archivo tiene una extension valida
     * @param strFile 
     * @return 
     */
    public boolean validaExtencion(String strFile) {
        int i = strFile.lastIndexOf(".");        
        if(i>0){
            String ext = strFile.substring(i);            
            return extensionesValidas.contains(ext);
        }
        return false;
    }
    
    /**
     * Comienza el analisis sintactico
     * Impementacion del metodo <code>run</code> de la interface <code>Testeable</code>
     * @throws java.lang.Exception 
     * si ocurre error lexico (LexicException), sintactico (SintacticException) o semantico (SemanticException)IOException
     */
    public void run() throws Exception{        
        if(debug) System.out.println("Compilando...");
        try{
            if(TS == null)   TS = new EntryTable();
            
            TS.init();
            nextToken();
            if(token.cod != Token.INICIO) throw new SintacticException(0,token.numLin);
            
            nextToken();
            if(token.cod!=Token.id) throw new SintacticException(1,token.numLin);
            
            /* accion semantica */
            TS.crearNivelLexico();
            TS.agregarPrograma(token.lex);
            genMepa("INPP", true);
            String e = genEtiqueta();
            genMepa("DSVS "+e,false);            
            /* fin accion semantica*/
            
            nextToken();
            if(token.cod!=Token.PYCOMA) throw new SintacticException(2,token.numLin);
            
            bloque(e,true,0,0);
            
            nextToken();
            if(token.cod!=Token.PUNTO) throw new SintacticException(3,token.numLin);
            
            /* accion semantica*/
            genMepa("PARA",true);            
             /* fin accion semantica*/
            
            nextToken();
            if(token.cod!=Token.EOF) //throw new SintacticException(4,token.numLin);
                System.out.println("Warning["+token.numLin+"]: se ignora el cod que sigue al punto final del programa\n");
            
            //Analisis exitoso
            if(debug)   System.out.println("Analisis Sintactico OK!");
        } catch(Exception ex){
            HUBO_ERROR = true;
            throw ex;
        }
        
    }
    
    /**
     * @throws java.lang.Exception si ocurre error lexico (LexicException) o sintactico (SintacticException)
     */
    private void bloque(String etiqueta, boolean esProgram,int nivelLexico, int sizeParamFor)throws Exception{
        nextToken();
        if(token.cod==Token.VAR|| token.cod==Token.SEP){
            restaurarToken=true;

            int tamVar = seccionDefinicionDeVariables();
            
            /* accion semantica*/
            if (esProgram) genMepa(etiqueta + "NADA",false);
            else genMepa(etiqueta + " ENPR",false,TS.getNivelLexico());            
            
            if (tamVar > 0) genMepa("RMEM",true,tamVar);            
            /* fin accion semantica*/
            
            sentenciaCompuesta();
            
            /* accion semantica*/
            if (tamVar > 0) genMepa("LMEM",true,tamVar);
            if(!esProgram) genMepa("RTPR",true,nivelLexico,sizeParamFor);
             /* fin accion semantica*/
            
        } else
            throw new SintacticException(5,token.numLin);
    }
    
    
    /**
     *
     * @throws java.lang.Exception
     */
    private Tipo tipo()throws Exception{
        nextToken();
        if(token.cod==Token.id){
        	Entry entradaTipo = TS.buscarTipo(token.lex);
        	if(entradaTipo!=null) return entradaTipo.tipo;
        	restaurarToken=true;
        	return tipoSimple();
        } else
            throw new SintacticException(9,token.numLin);
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private TipoSimple tipoSimple()throws Exception{
        nextToken();
        //token.cod == Token.id           
            return restoTipoSimple(token.lex);
        
    }
    
    
    /**
     * 
     * @throws java.lang.Exception 
     */
    private TipoSimple restoTipoSimple(String id)throws Exception{
        nextToken();
        Entry entrada = TS.buscarTipo(id);
        if(entrada==null) throw new SemanticException(5,token.numLin,id);
        if(!entrada.tipo.esSimple()) throw new SemanticException(5,token.numLin,id);
        restaurarToken=true;
        return (TipoSimple)entrada.tipo;

    }
    
    /**
     * @throws java.lang.Exception si ocurre error lexico (LexicException) o sintactico (SintacticException)
     */
    private int seccionDefinicionDeVariables()  throws Exception{
        nextToken();
        if(token.cod!=Token.VAR){
            restaurarToken=true;
            return 0;
        }
        int tamVar1 = declaracionDeVariables();
        nextToken();
        if(token.cod!=Token.PYCOMA) throw new SintacticException(2,token.numLin);
        int tamVar2 = restoDefinicionDeVariables();
        return tamVar1 + tamVar2;
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private int declaracionDeVariables()throws Exception{
        LinkedList<String> listaId = listaIdentificadoresDeVariables();
        nextToken();
        if(token.cod!=Token.DOSPUNTOS) throw new SintacticException(15,token.numLin);
        Tipo tipo = tipo();        
        
        TS.agregarVariables(listaId,tipo);
        return listaId.size() * tipo.getSize();
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private int restoDefinicionDeVariables()throws Exception{
        nextToken();
        if(token.cod == Token.id){
            restaurarToken =true;
            int tamVar1 = declaracionDeVariables();
            nextToken();
            if(token.cod!=Token.PYCOMA) throw new SintacticException(2,token.numLin);
            int tamVar2 = restoDefinicionDeVariables();
            return tamVar1 + tamVar2;
        } else if(token.cod==Token.SEP ){
            restaurarToken = true;
            return 0;    //lambda
        } else
            throw new SintacticException(16,token.numLin);
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private LinkedList<String> listaIdentificadoresDeVariables()throws Exception{
        nextToken();
        if(token.cod!= Token.id) throw new SintacticException(1,token.numLin);
        
        if (TS.estaDeclarado(token.lex)) throw new SemanticException(0,token.numLin,token.lex);
        LinkedList<String> l = new LinkedList<String>();
        l.addFirst(token.lex);
        return restoListaIdentificadoresDeVariables(l);
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private LinkedList<String> restoListaIdentificadoresDeVariables(LinkedList<String> listIn)throws Exception{
        nextToken();
        if(token.cod== Token.COMA) {
            nextToken();
            if(token.cod!= Token.id) throw new SintacticException(1,token.numLin);
            if (TS.estaDeclarado(token.lex) || listIn.contains(token.lex)) throw new SemanticException(0,token.numLin,token.lex);
            listIn.addFirst(token.lex);
            return restoListaIdentificadoresDeVariables(listIn);
        } else{
            restaurarToken=true;
            return listIn;
        }
        
    }
    
    
    /**
     * @throws java.lang.Exception si ocurre error lexico (LexicException) o sintactico (SintacticException)
     */
    private void sentenciaCompuesta()  throws Exception{
        nextToken();
        if(token.cod!=Token.SEP) throw new SintacticException(24,token.numLin);
        sentencia();
        restoSentencia();
        nextToken();
        if(token.cod!=Token.FIN) throw new SintacticException(25,token.numLin);
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private void sentencia()throws Exception{
        nextToken();
        restaurarToken=true;
        
        if(token.cod==Token.id){
            sentenciaSimple();
        } else if(token.cod==Token.SEP){
            sentenciaCompuesta();
        } 
        
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private void restoSentencia()throws Exception{
        nextToken();
        if(token.cod==Token.PYCOMA){
            sentencia();
            restoSentencia();
        } else
            restaurarToken=true;
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private void sentenciaSimple()throws Exception{
        nextToken();
        //no chequeo, ya viene un id
        Entry e = TS.buscar(token.lex);    
        
        if (e == null) throw new SemanticException (7, token.numLin,token.lex) ;
        restoSentenciaSimple(e);
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private void restoSentenciaSimple(Entry e)throws Exception{
        int lineaid = token.numLin; 
        nextToken();        
        //:= exp
        if(token.cod==Token.ASIG){
            if (!e.asignable) throw new SemanticException(34,token.numLin,e.nombre);
            Tipo tipoExp = expresion(true);            
            if(!e.tipo.equivalenteCon(tipoExp)) throw new SemanticException(8,token.numLin);
            
            if(e.esVariable() ){
                if(e.tipo.esSimple()){
                   genMepa("ALVL", true,e.nivelLexico,e.desplazamiento);
                }
            }         
        }
        //[exp1] := exp2
    }
    
    
    /**
     * <expresion> ::= <expresionSimple> <restoExpresion>     
     */
    private Tipo expresion(boolean porValor)throws Exception{
        Tipo texp = expresionSimple(porValor);
        Tipo trestoexp = restoExpresion(porValor,texp);
        return trestoexp;
    }
    
    /**
     * <expresionSimple> ::= 
     *      <termino> <restoExpresionSimple> |     
     *      <signo> <termino> <restoExpresionSimple>
     */
    private Tipo expresionSimple(boolean porValor)throws Exception{
        nextToken();
        if(token.cod!=Token.RESTA && token.cod!=Token.SUMA){
            restaurarToken=true;
            Tipo tterm = termino(porValor);
            return restoExpresionSimple(porValor,tterm);
        }
        else {
            String signo = token.lex;
            Tipo tterm = termino(true);
            if(!(tterm.esEntero())) 
                throw new SemanticException(18,token.numLin);
            if(signo.equals("-")) genMepa("UMEN",true);
            return restoExpresionSimple(true,tterm);
        }
       
        
    }
    /**
     * <restoTermino> ::= <op-multipilcador> <factor> <restoTermino> | <vacio>    
     */
    
    private Tipo restoTermino(boolean porValor, Tipo t)throws Exception{
        nextToken();
        if(token.cod==Token.MUL || token.cod==Token.DIV || token.cod==Token.AND){
            String operador = token.lex;
            if (!porValor) throw new SemanticException(16,token.numLin);
            Tipo tfactor = factor(true);
            if (!tfactor.equivalenteCon(t)) throw new SemanticException(20,token.numLin);
            if(operador.equals("*")) {
                if(tfactor.esBoolean()) 
                        throw new SemanticException(22,token.numLin);
                genMepa("MULT",true);
            }
            else  if(operador.equals("div")) {
                if(tfactor.esBoolean()) 
                        throw new SemanticException(22,token.numLin);
                genMepa("DIVC",true);           
                genMepa("DIVI",true);
            }
            else  if(operador.equals("and")) {
                if(!tfactor.esBoolean()) 
                    throw new SemanticException(23,token.numLin); 
                genMepa("CONJ",true);
            }
            return restoTermino(true,tfactor);
        } else{
            restaurarToken=true;
            return t;
        }
        
    }
    
    /**
     * <termino> :: <factor> <restoTermino>     
     */
    private Tipo termino(boolean porValor)throws Exception{        
        Tipo tfactor = factor(porValor);
        return restoTermino(porValor,tfactor);
    }
    
    /**
     * <restoExpresionSimple> ::= vacio | <op-sumador> <termino> <restoExpresionSimple>     
     */
    private Tipo restoExpresionSimple(boolean porValor, Tipo t)throws Exception{
        nextToken();
        if(token.cod==Token.RESTA || token.cod==Token.SUMA || token.cod==Token.OR){
            String op = token.lex;
            if(!porValor) throw new SemanticException(19,token.numLin);
            Tipo tterm = termino(true);
            
            if (!tterm.equivalenteCon(t)) throw new SemanticException(20,token.numLin);
            if(op.equals("+")) {
                if(tterm.esBoolean()) 
                        throw new SemanticException(21,token.numLin);
                genMepa("SUMA",true);
            }
            else if(op.equals("-")) {
                if(tterm.esBoolean()) 
                        throw new SemanticException(21,token.numLin);
                genMepa("SUST",true);
            }
            else  if(op.equals("or")) {
                if(!tterm.esBoolean()) 
                    throw new SemanticException(23,token.numLin); 
                genMepa("DISJ",true);
            }
            return restoExpresionSimple(true,tterm);
        } else{
            restaurarToken=true;
            return t;
        }
    }
    
    /**
     * <restoExpresion> ::= <op-relacional> <expresionSimple> | <vacio>     
     */
    private Tipo restoExpresion(boolean porValor, Tipo t)throws Exception{
        nextToken();
        
        if(token.cod==Token.OPREL  || token.cod==Token.IGUAL){
           String op = token.lex;
           if(!porValor ) throw new SemanticException(17,token.numLin);
           Tipo texp = expresionSimple(true);
           if(!texp.equivalenteCon(t)) throw new SemanticException(15,token.numLin);
           if(op.equals("<")) genMepa("CMME",true);
           else if(op.equals("<=")) genMepa("CMNI",true);
           else if(op.equals(">")) genMepa("CMMA",true);
           else if(op.equals(">=")) genMepa("CMYI",true);
           else if(op.equals("!=")) genMepa("CMDG",true);
           else if(op.equals("=")) genMepa("CMIG",true);
           
           return TipoFactory.crearTipoBooleano();
        } else{
            restaurarToken=true;
            return t;
        }       
    }
    
    /**
     * 
     * @throws java.lang.Exception
     */
    private Tipo factor(boolean porValor)throws Exception{
        nextToken();
        if(token.cod==Token.PA){
            if(!porValor)  throw new SemanticException(16,token.numLin);  
            Tipo texp = expresion(true);
            nextToken();
            if(token.cod!=Token.PC) throw new SintacticException(18,token.numLin);
            return texp;
        } 
        else  if(token.cod==Token.NOT){
            if(!porValor)  throw new SemanticException(19,token.numLin);  
            Tipo tfactor =  factor(true);
            if(!tfactor.esBoolean()) throw new SemanticException(11,token.numLin);
            genMepa("NEGA",true);
            return tfactor;
        }
        else  if(token.cod==Token.digito){
            if(!porValor)  throw new SemanticException(19,token.numLin);
            int num = convertirAEntero(token.lex,token.numLin);
            genMepa("APCT",true,num);
            return TipoFactory.crearTipoEntero();
        }
        else  if(token.cod==Token.id){
            Entry e = TS.buscar(token.lex);
            if(e == null)  throw new SemanticException(7,token.numLin,token.lex);
            return restoFactor(porValor,e);
        } else
            throw new SintacticException(32,token.numLin);
        
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private Tipo restoFactor(boolean porValor, Entry e)throws Exception{
        nextToken();
       //id 
        restaurarToken=true;
        if(e.esPrograma() || e.esTipo())
        	throw new SemanticException(36,token.numLin);
        if(e.tipo.esSimple()){
        	genMepa("APVL",true,e.nivelLexico,e.desplazamiento);
        	return e.tipo;
        }else{//copia del arreglo
        	int n = ((TipoArreglo)e.tipo).getNumComponentes();
        	genMepa("PUAR",true,e.nivelLexico,e.desplazamiento,n);                        
       		return e.tipo;
       	}
    }
       
        
    

    
      /**
     * Genera la proxima etiqueta
     * @return 
     */
    private String genEtiqueta(){        
        String l = "L"+numEtiqueta + "     ";
        String e = l.substring(0,5);
        numEtiqueta++;
        return e;
    }
    
    
    /**
     * Escribe la instruccion mepa en el archivo
     * @param inst instruccion
     * @param conEti indica si viene con etiqueta o no
     * @param par parametros de la instruccion
     * @throws java.lang.Exception 
     */
    private void genMepa(String inst, boolean conEti, int... par)throws Exception{
        if(!genCodigo) return;
        
        String linea = inst;
        if(par.length == 1)
            linea+= " "+par[0];
        else if(par.length == 2)
            linea+= " "+par[0] + " , "+par[1];
        else if(par.length == 3)
            linea+= " "+par[0] + " , "+par[1] + " , "+par[2];
        
        fileMepa.write(((conEti)?"     ":"")+linea +"\n");
        
    }
     
    /** */
    private String negar(String val) {
        if (val.charAt(0)=='-') return val.substring(1);
        else return "-" + val;
    }

    /**
     * Genera el cod para las funcinoes y procedimientos predefinidos
     * con parametros.
     * @param id nombre de la funcion/procedimiento
     * @param t tipo del parametro
     */
    private void genPredefinida(String id, Tipo t) throws Exception{
        if (!(t.esEntero()))
            throw new SemanticException(35,token.numLin);
        
        if ( id.equalsIgnoreCase("write")) genMepa("IMPR",true);
        
        else if( id.equalsIgnoreCase("read")) {
            genMepa("LEER",true);
            genMepa("ALDR",true);
        } 
     }
    
    /** 
     * @param num
     * @param numLin
     * @return 
     */
    private Integer convertirAEntero(String num, int numLin)throws Exception{
        Integer numI;
        try{
            numI=new Integer(Integer.parseInt(num));
        }catch(NumberFormatException e){
            throw new SemanticException(39,numLin);
        }
        return numI;
    }
}
