
package aSintactico;

import Utils.BufferedFileWriter;
import aLexico.AnalizadorLexico;
import aLexico.Token;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import excepciones.*;
import exceptions.SintacticException;
import aSintactico.tipos.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Properties;
import main.Testeable;

/**
 *
 * <P>Analizador Sintactico para el lenguaje. Este lenguaje es un
 * subconjunto del lenguaje Pascal.
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
 *
 * @see aLexico.AnalizadorLexico
 * @see aSintactico.TablaSimbolos
 * @since jdk 1.6
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
    private TablaEntrada TS = null;
    
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
            f = new FileInputStream("minipas.conf");
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
     * @throws excepciones.LexicException 
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
            if(TS == null)   TS = new TablaEntrada();
            
            TS.init();
            nextToken();
            if(token.codigo != Token.PROGRAM) throw new SintacticException(0,token.numLinea);
            
            nextToken();
            if(token.codigo!=Token.ID) throw new SintacticException(1,token.numLinea);
            
            /* accion semantica */
            TS.crearNivelLexico();
            TS.agregarPrograma(token.lexema);
            genMepa("INPP", true);
            String e = genEtiqueta();
            genMepa("DSVS "+e,false);            
            /* fin accion semantica*/
            
            nextToken();
            if(token.codigo!=Token.PUNTO_Y_COMA) throw new SintacticException(2,token.numLinea);
            
            bloque(e,true,0,0);
            
            nextToken();
            if(token.codigo!=Token.PUNTO) throw new SintacticException(3,token.numLinea);
            
            /* accion semantica*/
            genMepa("PARA",true);            
             /* fin accion semantica*/
            
            nextToken();
            if(token.codigo!=Token.EOF) //throw new SintacticException(4,token.numLinea);
                System.out.println("Warning["+token.numLinea+"]: se ignora el codigo que sigue al punto final del programa\n");
            
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
        if(token.codigo ==Token.CONST || token.codigo==Token.TYPE || token.codigo==Token.VAR||
                token.codigo==Token.PROCEDURE || token.codigo==Token.FUNCTION || token.codigo==Token.BEGIN){
            restaurarToken=true;
            seccionDefinicionDeConstantes();
            seccionDefinicionDeTipos();
            int tamVar = seccionDefinicionDeVariables();
            seccionDeclaracionProcYFunciones();
            
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
            throw new SintacticException(5,token.numLinea);
    }
    
    /**
     * @throws java.lang.Exception si ocurre error lexico (LexicException) o sintactico (SintacticException)
     */
    private void seccionDefinicionDeConstantes() throws Exception{
        nextToken();
        if(token.codigo!=Token.CONST){
            restaurarToken=true;
            return;
        }
        definicionDeConstante();
        nextToken();
        if(token.codigo !=Token.PUNTO_Y_COMA) throw new SintacticException(2,token.numLinea);
        restoDefinicionDeConstante();
        
    }
    
    /**
     * 
     * @throws java.lang.Exception 
     */
    private void definicionDeConstante()throws Exception{
        nextToken();
        if(token.codigo!= Token.ID) throw new SintacticException(1,token.numLinea);
         /* accion semantica*/
        if (TS.estaDeclarado(token.lexema)) throw new SemanticException(0,token.numLinea,token.lexema);
        String id = token.lexema;
        
        nextToken();
        if(token.codigo!= Token.IGUAL) throw new SintacticException(4,token.numLinea);
       
        Entrada entry = constante();
        entry.nombre = id;
        TS.agregarConstante(entry);
        
    }
    
    /**
     * 
     * @throws java.lang.Exception 
     */
    private void restoDefinicionDeConstante()throws Exception{
        nextToken();
        
        if(token.codigo == Token.ID){
            restaurarToken = true;
            definicionDeConstante();
            nextToken();
            if (token.codigo!=Token.PUNTO_Y_COMA)
                throw new SintacticException(2,token.numLinea);
            restoDefinicionDeConstante();
        } else if(token.codigo==Token.TYPE || token.codigo==Token.VAR ||
                token.codigo==Token.PROCEDURE || token.codigo==Token.FUNCTION
                || token.codigo==Token.BEGIN){
            restaurarToken = true;
            return; //labmda
        } else
            throw new SintacticException(8,token.numLinea);
    }
    
    /**
     * 
     * @throws java.lang.Exception 
     */
    private Entrada constante() throws Exception{
         nextToken();
        if(token.codigo == Token.OP_SUMA )  return idConstante("+");
        else  if(token.codigo == Token.OP_RESTA) return idConstante("-");
        else{
            restaurarToken=true;
            return idConstante("");
        }
        
    }
    
    /**
     * idConstante-> Numero| identificador
     * @throws java.lang.Exception 
     */
    private Entrada idConstante( String signo)throws Exception{
        nextToken();
        //semantico: analizar si ID esta declarado
        if(token.codigo==Token.ID){
            Entrada e = TS.buscarConstante(token.lexema);
            if (e == null)  
                throw new SemanticException (2,token.numLinea,token.lexema);
                                    
            Entrada entry = new Entrada(e.nombre,Entrada.CONSTANTE);
            entry.asignable = false;
            String valStr = e.valorStr;
            
            if (signo.length()>0) {
                if(!e.tipo.esEntero() && !e.tipo.esSubrango()) 
                    throw new SemanticException(3,token.numLinea);                
                if(signo.equals("-"))
                   valStr = negar(valStr);                                                                        
            }
                
            entry.valor = convertirAEntero(valStr,token.numLinea);            
            entry.valorStr = valStr;                           
            entry.tipo = e.tipo;            
            return entry;
        }            
        else if (token.codigo ==Token.NUMERO) {
            Entrada entry = new Entrada( Entrada.CONSTANTE);
            entry.asignable = false;
            entry.tipo = TipoFactory.crearTipoEntero();  
            String valStr = token.lexema; 
            
            int val = convertirAEntero(valStr,token.numLinea).intValue();
            
            if (signo.length()>0) {                                                
                if(signo.equals("-")){                   
                   valStr = "-"+ valStr; 
                   val = val * -1;                                      
                }
            }                
                        
            entry.valor = new Integer(val);            
            entry.valorStr = valStr;                                                   
            return entry;
        }
        else throw new SintacticException(7,token.numLinea);
    }
    
    /**
     * @throws java.lang.Exception si ocurre error lexico (LexicException) o sintactico (SintacticException)
     */
    private void seccionDefinicionDeTipos()  throws Exception{
    
        nextToken();
        if(token.codigo!=Token.TYPE){
            restaurarToken=true;
            return;
        }
        definicionDeTipo();
        nextToken();
        if (token.codigo!=Token.PUNTO_Y_COMA)
            throw new SintacticException(2,token.numLinea);
        restoDefinicionDeTipo();
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private void definicionDeTipo()  throws Exception{
        nextToken();
        if(token.codigo!= Token.ID) throw new SintacticException(1,token.numLinea);
        /*accion semantica*/
        if ( TS.estaDeclarado(token.lexema)) 
                throw new SemanticException(0,token.numLinea,token.lexema);              
        String id=token.lexema;
        nextToken();
        if(token.codigo!= Token.IGUAL) throw new SintacticException(4,token.numLinea);
        Tipo t = tipo();
        /*accion semantica*/
        
        TS.agregarTipo(id,t);
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private void restoDefinicionDeTipo()  throws Exception{
        nextToken();
        if(token.codigo==Token.ID){
            restaurarToken=true;
            definicionDeTipo();
            nextToken();
            if (token.codigo!=Token.PUNTO_Y_COMA)   throw new SintacticException(2,token.numLinea);
            restoDefinicionDeTipo();
        } else if (token.codigo==Token.VAR || token.codigo==Token.BEGIN ||
                token.codigo==Token.PROCEDURE || token.codigo==Token.FUNCTION){
            restaurarToken=true;
            return;
        } else
            throw new SintacticException(6,token.numLinea);
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private Tipo tipo()throws Exception{
        nextToken();
        if(token.codigo==Token.ARRAY){
            nextToken();
            if(token.codigo!=Token.COR_ABRE) throw new SintacticException(11,token.numLinea);
            TipoSimple ti = tipoSimple();
            nextToken();
            if(token.codigo!=Token.COR_CIERRA) throw new SintacticException(12,token.numLinea);
            nextToken();
            if(token.codigo!=Token.OF) throw new SintacticException(13,token.numLinea);
            TipoSimple tr = tipoSimple();
            if ( ti.esEntero() ) 
                throw new SemanticException(4, token.numLinea);
            return TipoFactory.crearTipoArreglo(ti,tr);
        }
        
        else if(token.codigo==Token.ID || token.codigo==Token.NUMERO || token.codigo==Token.OP_SUMA || token.codigo==Token.OP_RESTA){
            
            if(token.codigo==Token.ID){
                Entrada entradaTipo = TS.buscarTipo(token.lexema);
                if(entradaTipo!=null) return entradaTipo.tipo;
            }
            restaurarToken=true;
            return tipoSimple();
        } else
            throw new SintacticException(9,token.numLinea);
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private TipoSimple tipoSimple()throws Exception{
        nextToken();
        if(token.codigo == Token.ID){            
            return restoTipoSimple(token.lexema);
        } 
        else if(token.codigo==Token.NUMERO){
            int li = convertirAEntero(token.lexema,token.numLinea).intValue();
            nextToken();
            if(token.codigo!=Token.PUNTO_PUNTO)  throw new SintacticException(14,token.numLinea);
            Entrada entrada = constante();
            if(entrada.tipo.esBoolean()) throw new SemanticException(9,token.numLinea,entrada.nombre);
            
            int ls = ((Integer)entrada.valor).intValue();
            
            /* aca se controla que el li no sea mayor al ls */            
            if(ls<li) throw new SemanticException(38,token.numLinea);
            
            return TipoFactory.crearTipoSubrango(li,ls);
            
        } 
        else if(token.codigo==Token.OP_SUMA )  {
            return restoSubrango("+");
        } 
        else if(token.codigo==Token.OP_RESTA )  {
            return restoSubrango("-");
        }else
            throw new SintacticException(10,token.numLinea);
    }
    
    /**
     * 
     * @throws java.lang.Exception 
     */
    private TipoSubrango restoSubrango(String signo)throws Exception{
        nextToken();
        if(token.codigo!=Token.ID && token.codigo!=Token.NUMERO) throw new SintacticException(28,token.numLinea);
        Integer val;
        if(token.codigo == Token.ID){
            Entrada entradaLi = TS.buscarConstante (token.lexema);
            if (entradaLi == null) throw new SemanticException(2,token.numLinea,token.lexema);
            
            if(entradaLi.tipo.esBoolean()) throw new SemanticException(9,token.numLinea,entradaLi.nombre);
            
            val = (Integer)entradaLi.valor;
        }else
            val = convertirAEntero(token.lexema,token.numLinea);
        
        if (signo.equals("-")) val = val * -1;
        
        nextToken();
        if(token.codigo!=Token.PUNTO_PUNTO) throw new SintacticException(14,token.numLinea);
        Entrada entradaLs = constante();
        
        if(entradaLs.tipo.esBoolean()) throw new SemanticException(9,token.numLinea,entradaLs.nombre);
        
        int li = val.intValue();
        int ls = ((Integer)entradaLs.valor).intValue();
        
        /* aca se controla que el li no sea mayor al ls */            
        if(ls<li) throw new SemanticException(38,token.numLinea);
        
        return TipoFactory.crearTipoSubrango(li,ls);
    }
    
    /**
     * 
     * @throws java.lang.Exception 
     */
    private TipoSimple restoTipoSimple(String id)throws Exception{
        int ultLinea = token.numLinea;
        nextToken();
        if(token.codigo==Token.PUNTO_PUNTO) {            
            Entrada entradaLS = constante();
            Entrada entradaLI = TS.buscarConstante(id);
            
            if(entradaLI == null) throw new SemanticException(2,token.numLinea,id);
            else 
                if(entradaLI.tipo.esBoolean()) throw new SemanticException(9,ultLinea,id);
            
            if(entradaLS.tipo.esBoolean()) throw new SemanticException(9,token.numLinea,entradaLS.nombre);
            
            int li = ((Integer)entradaLI.valor).intValue();
            int ls = ((Integer)entradaLS.valor).intValue();
            /* aca se controla que el li no sea mayor al ls */            
            if(ls<li) throw new SemanticException(38,token.numLinea);
            
            return TipoFactory.crearTipoSubrango(li,ls);
            
        } else{
            Entrada entrada = TS.buscarTipo(id);
            if(entrada==null) throw new SemanticException(5,token.numLinea,id);
            if(!entrada.tipo.esSimple()) throw new SemanticException(5,token.numLinea,id);
            restaurarToken=true;
            return (TipoSimple)entrada.tipo;
        }
    }
    
    /**
     * @throws java.lang.Exception si ocurre error lexico (LexicException) o sintactico (SintacticException)
     */
    private int seccionDefinicionDeVariables()  throws Exception{
        nextToken();
        if(token.codigo!=Token.VAR){
            restaurarToken=true;
            return 0;
        }
        int tamVar1 = declaracionDeVariables();
        nextToken();
        if(token.codigo!=Token.PUNTO_Y_COMA) throw new SintacticException(2,token.numLinea);
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
        if(token.codigo!=Token.DOS_PUNTOS) throw new SintacticException(15,token.numLinea);
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
        if(token.codigo == Token.ID){
            restaurarToken =true;
            int tamVar1 = declaracionDeVariables();
            nextToken();
            if(token.codigo!=Token.PUNTO_Y_COMA) throw new SintacticException(2,token.numLinea);
            int tamVar2 = restoDefinicionDeVariables();
            return tamVar1 + tamVar2;
        } else if(token.codigo==Token.PROCEDURE || token.codigo==Token.BEGIN ||
                token.codigo==Token.FUNCTION){
            restaurarToken = true;
            return 0;    //lambda
        } else
            throw new SintacticException(16,token.numLinea);
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private LinkedList<String> listaIdentificadoresDeVariables()throws Exception{
        nextToken();
        if(token.codigo!= Token.ID) throw new SintacticException(1,token.numLinea);
        
        if (TS.estaDeclarado(token.lexema)) throw new SemanticException(0,token.numLinea,token.lexema);
        LinkedList<String> l = new LinkedList<String>();
        l.addFirst(token.lexema);
        return restoListaIdentificadoresDeVariables(l);
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private LinkedList<String> restoListaIdentificadoresDeVariables(LinkedList<String> listIn)throws Exception{
        nextToken();
        if(token.codigo== Token.COMA) {
            nextToken();
            if(token.codigo!= Token.ID) throw new SintacticException(1,token.numLinea);
            if (TS.estaDeclarado(token.lexema) || listIn.contains(token.lexema)) throw new SemanticException(0,token.numLinea,token.lexema);
            listIn.addFirst(token.lexema);
            return restoListaIdentificadoresDeVariables(listIn);
        } else{
            restaurarToken=true;
            return listIn;
        }
        
    }
    
    /**
     * @throws java.lang.Exception si ocurre error lexico (LexicException) o sintactico (SintacticException)
     */
    private void seccionDeclaracionProcYFunciones() throws Exception {
        nextToken();
        if(token.codigo!=Token.PROCEDURE && token.codigo!=Token.FUNCTION){
            restaurarToken=true;
            return;
        }
        
        restaurarToken=true;
        if(token.codigo== Token.PROCEDURE){
            declaracionDeProcedimiento();
           
        } else{
            declaracionDeFuncion();           
        }
        nextToken();
        if(token.codigo!=Token.PUNTO_Y_COMA) throw new SintacticException(2,token.numLinea);
        seccionDeclaracionProcYFunciones();
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private void declaracionDeProcedimiento() throws Exception {
        Entrada entradaProc = encabezadoDeProcedimiento();        
        bloque(entradaProc.etiqueta,false,entradaProc.nivelLexico,entradaProc.sizeParametros);
        TS.eliminarNivelLexico();
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private Entrada encabezadoDeProcedimiento() throws Exception {
        nextToken();
        
        nextToken();
        if(token.codigo!= Token.ID) throw new SintacticException(1,token.numLinea);
        
        if(TS.estaDeclarado(token.lexema)) throw new SemanticException(0,token.numLinea,token.lexema);
        String id = token.lexema;        
        LinkedList<Entrada> listaPar = restoEncabezadoDeProcedimiento();                
        String eti = genEtiqueta();           
        Entrada entradaProc = TS.agregarProcedimiento(id,listaPar,eti);                
        TS.crearNivelLexico();
        entradaProc.nivelLexico = TS.getNivelLexico();
        TS.agregarDesplazamientos(listaPar);
        TS.agregarParFormales(listaPar);
        
        return entradaProc;
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private LinkedList<Entrada> restoEncabezadoDeProcedimiento() throws Exception {
        nextToken();
        if(token.codigo==Token.PUNTO_Y_COMA){
            return new LinkedList<Entrada>();
        } 
        else if(token.codigo==Token.PAR_ABRE){
            LinkedList<Entrada> lista =  listaParametrosFormales();
            nextToken();
            if(token.codigo!=Token.PAR_CIERRA) throw new SintacticException(18,token.numLinea);
            nextToken();
            if(token.codigo!=Token.PUNTO_Y_COMA) throw new SintacticException(2,token.numLinea);
            return lista;
        } 
        else
            throw new SintacticException(19,token.numLinea);
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private LinkedList<Entrada> listaParametrosFormales()throws Exception{
        HashSet<String> conjId = new HashSet<String>();
        LinkedList<Entrada> listaPar = grupoDeParametros(new LinkedList<Entrada>(), conjId);
        return restoListaParametrosFormales(listaPar,conjId);
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private LinkedList<Entrada> grupoDeParametros(LinkedList<Entrada> listaPar , HashSet<String> conjId)throws Exception{
        nextToken();
        //por valor
        if(token.codigo == Token.ID){
            restaurarToken=true;
            LinkedList<String> listaId = listaIdentificadorParametroFormales(conjId);
            nextToken();
            if(token.codigo!=Token.DOS_PUNTOS) throw new SintacticException(15,token.numLinea);
            nextToken();
            if(token.codigo!=Token.ID) throw new SintacticException(9,token.numLinea);
            Entrada etipo = TS.buscarTipo(token.lexema);
            if (etipo == null) throw new SemanticException(5,token.numLinea,token.lexema);
            Entrada e;
            String id;
            while(listaId.size()>0){
                id = listaId.removeFirst();
                e = new Entrada(id,Entrada.PARAMETRO);                
                e.tipo = etipo.tipo;
                e.porValor = true;
                e.asignable = true;
                listaPar.addLast(e);
            }
            return listaPar;
        }
        //por referencia
        else if(token.codigo == Token.VAR){
            LinkedList<String> listaId = listaIdentificadorParametroFormales(conjId);
            nextToken();
            if(token.codigo!=Token.DOS_PUNTOS) throw new SintacticException(15,token.numLinea);
            nextToken();
            if(token.codigo!=Token.ID) throw new SintacticException(9,token.numLinea);
            Entrada etipo = TS.buscarTipo(token.lexema);
            if (etipo == null) throw new SemanticException(5,token.numLinea,token.lexema);
            Entrada e;
            String id;
            while(listaId.size()>0){
                id = listaId.removeFirst();
                e = new Entrada(id,Entrada.PARAMETRO);
                e.tipo = etipo.tipo;
                e.porValor = false;
                e.asignable = true;
                listaPar.addLast(e);
            }
            return listaPar;
        } else
            throw new SintacticException(20,token.numLinea);
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private LinkedList<Entrada>  restoListaParametrosFormales(LinkedList<Entrada> listaPar, HashSet<String> conjId)throws Exception{
        nextToken();
        if(token.codigo==Token.PUNTO_Y_COMA){
            LinkedList<Entrada> listaP = grupoDeParametros(listaPar,conjId);
            return restoListaParametrosFormales(listaP,conjId);
        } else {
            restaurarToken=true;
            return listaPar;
        }
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private LinkedList<String> listaIdentificadorParametroFormales(HashSet<String> conjId) throws Exception{
        nextToken();
        if(token.codigo!= Token.ID) throw new SintacticException(1,token.numLinea);
        if(conjId.contains(token.lexema)) throw new SemanticException(6,token.numLinea);
        conjId.add(token.lexema);
        String id = token.lexema;
        LinkedList<String> listaId= restoListaIdentificadorParametroFormales(conjId);
        listaId.addFirst(id);
        return listaId;
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private LinkedList<String> restoListaIdentificadorParametroFormales(HashSet<String> conjId) throws Exception{
        nextToken();
        
        if(token.codigo== Token.COMA){
            nextToken();
            if(token.codigo!= Token.ID) throw new SintacticException(1,token.numLinea);
            String id = token.lexema;
            if (conjId.contains(id)) throw new SemanticException(6,token.numLinea);
            conjId.add(id);
            LinkedList<String> listaId = restoListaIdentificadorParametroFormales(conjId);
            listaId.addFirst(id);
            return listaId;
        } else{
            restaurarToken=true;
            return new LinkedList<String>();
        }
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private void declaracionDeFuncion() throws Exception {
        Entrada entradaFun = encabezadoDeFuncion();
        bloque(entradaFun.etiqueta,false,entradaFun.nivelLexico,entradaFun.sizeParametros);
        entradaFun.asignable = false;
        TS.eliminarNivelLexico();
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private Entrada encabezadoDeFuncion() throws Exception {
        nextToken();//ya se leyo function        
        nextToken();
        if(token.codigo!= Token.ID) throw new SintacticException(21,token.numLinea);
        
        if (TS.estaDeclarado(token.lexema)) throw new SemanticException(0, token.numLinea,token.lexema);
        return restoEncabezadoDeFuncion(token.lexema);
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private Entrada restoEncabezadoDeFuncion(String id) throws Exception {
        nextToken();
        //Funcion sin parametros
        if(token.codigo==Token.DOS_PUNTOS){
            nextToken();
            if(token.codigo!= Token.ID) throw new SintacticException(22,token.numLinea);
            Entrada etipo = TS.buscarTipo(token.lexema);
            if (etipo==null) throw new SemanticException(5,token.numLinea,token.lexema);
            if(!etipo.tipo.esSimple()) throw new SemanticException (1, token.numLinea);
            LinkedList<Entrada> listaPar = new LinkedList<Entrada>();
            String eti = genEtiqueta();            
            Entrada efun = TS.agregarFuncion(id, etipo.tipo,eti,listaPar);
            TS.crearNivelLexico();
            efun.nivelLexico = TS.getNivelLexico();
            efun.desplazamiento = -3;
            nextToken();
            if(token.codigo!= Token.PUNTO_Y_COMA) throw new SintacticException(2,token.numLinea);
            return efun;
        } 
        //Funcion con parametros
        else if(token.codigo==Token.PAR_ABRE){
            LinkedList<Entrada> listaPar = listaParametrosFormales();
            nextToken();
            if(token.codigo!= Token.PAR_CIERRA) throw new SintacticException(18,token.numLinea);
            nextToken();
            if(token.codigo!= Token.DOS_PUNTOS) throw new SintacticException(15,token.numLinea);
            nextToken();
            if(token.codigo!= Token.ID) throw new SintacticException(22,token.numLinea);
            Entrada etipo = TS.buscarTipo(token.lexema);
            if (etipo==null) throw new SemanticException(5,token.numLinea,token.lexema);
            if(!etipo.tipo.esSimple()) throw new SemanticException (1, token.numLinea);
            String eti = genEtiqueta();
            Entrada efun = TS.agregarFuncion(id, etipo.tipo,eti,listaPar);
            TS.crearNivelLexico();
            int desplazamiento = TS.agregarDesplazamientos(listaPar);
            efun.nivelLexico = TS.getNivelLexico();
            efun.desplazamiento = desplazamiento -1;
            TS.agregarParFormales(listaPar);                    
            nextToken();
            if(token.codigo!= Token.PUNTO_Y_COMA) throw new SintacticException(2,token.numLinea);
            return efun;
        } 
        else
            throw new SintacticException(23,token.numLinea);
    }
    
    /**
     * @throws java.lang.Exception si ocurre error lexico (LexicException) o sintactico (SintacticException)
     */
    private void sentenciaCompuesta()  throws Exception{
        nextToken();
        if(token.codigo!=Token.BEGIN) throw new SintacticException(24,token.numLinea);
        sentencia();
        restoSentencia();
        nextToken();
        if(token.codigo!=Token.END) throw new SintacticException(25,token.numLinea);
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private void sentencia()throws Exception{
        nextToken();
        restaurarToken=true;
        
        if(token.codigo==Token.ID){
            sentenciaSimple();
        } else if(token.codigo==Token.BEGIN){
            sentenciaCompuesta();
        } else if(token.codigo==Token.IF){
            sentenciaIF();
        } else if(token.codigo==Token.CASE){
            sentenciaCASE();
        } else if(token.codigo==Token.WHILE){
            sentenciaWHILE();
        }
        
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private void restoSentencia()throws Exception{
        nextToken();
        if(token.codigo==Token.PUNTO_Y_COMA){
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
        //no chequeo, ya viene un ID
        Entrada e = TS.buscar(token.lexema);    
        
        if (e == null) throw new SemanticException (7, token.numLinea,token.lexema) ;
        restoSentenciaSimple(e);
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private void restoSentenciaSimple(Entrada e)throws Exception{ 
        nextToken();        
        //:= exp
        if(token.codigo==Token.ASIGNACION){
            if (!e.asignable) throw new SemanticException(34,token.numLinea,e.nombre);
            Tipo tipoExp = expresion(true);            
            if(!e.tipo.equivalenteCon(tipoExp)) throw new SemanticException(8,token.numLinea);
            
            if(e.esVariable() || e.esFuncion() || (e.esParametro() && e.porValor)){
                if(e.tipo.esSimple()){
                    if (e.tipo.esSubrango())
                        genMepa("CONT" ,true, ((TipoSubrango)e.tipo).getValInferior() , ((TipoSubrango)e.tipo).getValSuperior());
                    genMepa("ALVL", true,e.nivelLexico,e.desplazamiento);
                } 
                else{//copia el arreglo apilado a la memoria de la variable
                    genMepa("POAR",true,e.nivelLexico,e.desplazamiento,((TipoArray)e.tipo).getNumComponentes());                    
                }
            }else{ //asignación de un valor a un param formal pasado por referencia
                if(e.tipo.esSimple()){
                    if(e.tipo.esSubrango())
                        genMepa("CONT",true,((TipoSubrango)e.tipo).getValInferior(),((TipoSubrango)e.tipo).getValSuperior());
                    genMepa("ALVI",true,e.nivelLexico,e.desplazamiento);
                } 
                else{//param formal, arreglo  por referencia
                    genMepa("POAI",true,e.nivelLexico,e.desplazamiento,((TipoArray)e.tipo).getNumComponentes());
                }
            }         
        }
        //[exp1] := exp2
        else if(token.codigo==Token.COR_ABRE){
            if(!e.asignable) throw new SemanticException(34,token.numLinea);
            
            if(!e.tipo.esArreglo()) throw new SemanticException(10,token.numLinea);
            
            TipoArray tipo = (TipoArray)e.tipo;            
            Tipo tipoExp = expresion(true);
            
            if (!tipo.tipoDominio.equivalenteCon(tipoExp))
                throw new SemanticException(12,token.numLinea);
            
            if (tipo.tipoDominio.esSubrango()){
                genMepa( "CONT" ,true, ((TipoSubrango)tipo.tipoDominio).getValInferior()
                                ,((TipoSubrango)tipo.tipoDominio).getValSuperior());
                genMepa("APCT",true, ((TipoSubrango)tipo.tipoDominio).getValInferior());
                genMepa("SUST",true);     
            }else //puede ser boolean
                genMepa( "CONT" ,true, 0,1);            
           
            nextToken();
            if(token.codigo!=Token.COR_CIERRA) throw new SintacticException(12,token.numLinea);
            nextToken();
            if(token.codigo!=Token.ASIGNACION) throw new SintacticException(26,token.numLinea);
                        
            Tipo tipoExp2 = expresion(true);
            if (!tipo.tipoRango.equivalenteCon(tipoExp2))
                throw new SemanticException(12,token.numLinea);
            
            if (tipo.tipoRango.esSubrango())
                genMepa( "CONT",true , ((TipoSubrango)tipo.tipoRango).getValInferior()
                    , ((TipoSubrango)tipo.tipoRango).getValSuperior());
            
            if (!e.porValor)
                genMepa("ALAI",true, e.nivelLexico,e.desplazamiento);
            else
                genMepa("ALAR",true, e.nivelLexico,e.desplazamiento);

        }// id(params) 
        else if(token.codigo== Token.PAR_ABRE){
            if(!e.esProcedimiento()) throw new SemanticException(33,token.numLinea);
            if(e.listaParametros.size()==0 && !e.predefinido) throw new SemanticException(31,token.numLinea);
            
            listaParametrosActuales(e);
            nextToken();
            if(token.codigo!=Token.PAR_CIERRA) throw new SintacticException(18,token.numLinea);
            if(!e.predefinido) 
                genMepa("LLPR "+e.etiqueta,true);
            
        } 
        else{ //procedimiento sin parametros           
           restaurarToken=true;
           if(!e.esProcedimiento()) throw new SemanticException(33,token.numLinea);
           if(e.listaParametros.size()>0)throw new SemanticException(31,token.numLinea);
           
           if(!e.predefinido)
               genMepa("LLPR " + e.etiqueta,true);
           else{
               if(e.nombre.equals("writeln"))  genMepa("IMLV",true) ;               
           }
        }
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private void sentenciaIF()throws Exception{
        nextToken();
        int linea = token.numLinea;
        //ya asume que es IF
        Tipo texp = expresion(true);
        if(!texp.esBoolean()) throw new SemanticException(11, linea);
        nextToken();
        if(token.codigo!=Token.THEN) throw new SintacticException(27,token.numLinea);
        String eti = genEtiqueta();
        genMepa("DSVF "+ eti,true);
        sentencia();
        restoSentenciaIF(eti);
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private void restoSentenciaIF(String etiqueta)throws Exception{
        nextToken();
        if(token.codigo==Token.ELSE){
            String eti =  genEtiqueta();
            genMepa("DSVS "+ eti,true);
            genMepa(etiqueta + "NADA",false) ;
            sentencia();
            genMepa(eti +"NADA",false);
        } else{
            restaurarToken=true;
            genMepa(etiqueta + "NADA",false) ;
        }
    }
    
    
    
    /**
     * <sentenciaCASE> ::= case <expresion> of <listaElementosCASE> end
     * 
     */
    private void sentenciaCASE()throws Exception{
        nextToken();
        //asume que vino CASE
        Tipo texp = expresion(true);
        if (! texp.esSimple()) throw new SemanticException(12, token.numLinea);
        nextToken();
        if(token.codigo!=Token.OF)throw new SintacticException(13,token.numLinea);
        String etiFinCase = genEtiqueta();
        
        HashSet conjId = new HashSet();
        listaElementosCASE(texp,etiFinCase,conjId);
        nextToken();
        if(token.codigo!=Token.END)throw new SintacticException(25,token.numLinea);
        genMepa(etiFinCase+" LMEM 1",false);
    }
    
    /**
     * <listaElementosCASE> ::= <etiquetaListaCASE> <restoListaElementosCASE>
     * 
     */
    private HashSet listaElementosCASE(Tipo t,String etiFinCase, HashSet conjId)throws Exception{
        HashSet conjIdSint = etiquetaListaCASE(t,etiFinCase,conjId);
        return restoListaElementosCASE(t,etiFinCase, conjIdSint);
    }
    
    /**     
     * <etiquetaListaCASE>::= <consatnte><restoEtiquetas>: <sentencia> | <vacio>
     *     
     */
    private HashSet etiquetaListaCASE(Tipo t,String etiFinCase,HashSet conjId)throws Exception{
        nextToken();
        restaurarToken = true; 
        if (token.codigo!=Token.ID && token.codigo!=Token.NUMERO &&token.codigo!=Token.OP_SUMA && token.codigo!=Token.OP_RESTA  ){
            return conjId;
        }        
        String etiFinSent = genEtiqueta();
        Entrada econ = constante(); 
        if(!econ.tipo.equivalenteCon(t)) throw new SemanticException(37, token.numLinea); 
        
        if(conjId.contains(econ.valor)) throw new SemanticException(13,token.numLinea);
        else 
            conjId.add(econ.valor);
        
        genMepa("DUPT",true);
	genMepa("APCT "+econ.valorStr,true);	
	genMepa("CMIG",true);
	String etiSent = genEtiqueta();
	genMepa("DSVV " + etiSent,true);
        
        HashSet conjIdSint = restoEtiquetas(t,etiFinCase,etiSent,conjId);   
        
        nextToken();
        if (token.codigo!=Token.DOS_PUNTOS) throw new SintacticException(15, token.numLinea );
        genMepa("DSVS " + etiFinSent,true);
	genMepa(etiSent +"NADA",false);	
        
        sentencia();
        
        genMepa("DSVS " + etiFinCase,true);
	genMepa(etiFinSent +"NADA",false);	
        return conjIdSint;
    }
    
    /**
     * <restoListaElementosCASE>::= ; <listaElementosCASE> | <vacio>
     * @throws java.lang.Exception 
     */
    private HashSet restoListaElementosCASE(Tipo t,String etiFinCase, HashSet conjId)throws Exception{
        nextToken();
        if(token.codigo==Token.PUNTO_Y_COMA){
            return listaElementosCASE(t,etiFinCase,conjId);
        } else{
            restaurarToken = true;
            return conjId;
        }
    }
   
    /**
     * <restoEtiquetas> ::= , <constante> <restoEtiquetas> | <vacio>
     * 
     */
    private HashSet restoEtiquetas(Tipo t, String etiFinCase, String etiSent,HashSet conjId)throws Exception{
        nextToken();
        if(token.codigo==Token.COMA){
            Entrada econ = constante();
            if(!econ.tipo.equivalenteCon(t))throw new SemanticException(37, token.numLinea); 
            
            if(conjId.contains(econ.valor)) throw new SemanticException(13,token.numLinea);
            else 
                conjId.add(econ.valor);
            
            genMepa("DUPT",true);
            genMepa("APCT "+econ.valorStr,true);	
            genMepa("CMIG",true);
            genMepa("DSVV "+etiSent,true);
            return restoEtiquetas(t,etiFinCase,etiSent,conjId);        
        }
        else
            restaurarToken=true;        
            return conjId;
    }
    
    /**
     * <sentenciaWHILE> ::= while <expresion> do <sentencia>
     * 
     */
    private void sentenciaWHILE()throws Exception{
        nextToken();
        if(token.codigo==Token.WHILE){
            String eti = genEtiqueta();
            int lineaErr = token.numLinea;
            genMepa(eti+"NADA",false);
            
            Tipo texp = expresion(true);
            
            if(!texp.esBoolean()) throw new SemanticException(11,lineaErr);
            nextToken();
            if(token.codigo !=Token.DO) throw new SintacticException(30,token.numLinea);
            String etiFin = genEtiqueta();
            genMepa("DSVF "+etiFin,true);
            
            sentencia();
            
            genMepa("DSVS "+eti,true);
            genMepa(etiFin+"NADA",false);
        } else
            throw new SintacticException(31,token.numLinea);
        
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
        if(token.codigo!=Token.OP_RESTA && token.codigo!=Token.OP_SUMA){
            restaurarToken=true;
            Tipo tterm = termino(porValor);
            return restoExpresionSimple(porValor,tterm);
        }
        else {
            String signo = token.lexema;
            if(!porValor) throw new SemanticException(16,token.numLinea);
            Tipo tterm = termino(true);
            if(!(tterm.esEntero()|| tterm.esSubrango())) 
                throw new SemanticException(18,token.numLinea);
            if(signo.equals("-")) genMepa("UMEN",true);
            return restoExpresionSimple(true,tterm);
        }
       
        
    }
    /**
     * <restoTermino> ::= <op-multipilcador> <factor> <restoTermino> | <vacio>    
     */
    
    private Tipo restoTermino(boolean porValor, Tipo t)throws Exception{
        nextToken();
        if(token.codigo==Token.OP_MULT || token.codigo==Token.OP_DIV || token.codigo==Token.AND){
            String operador = token.lexema;
            if (!porValor) throw new SemanticException(16,token.numLinea);
            Tipo tfactor = factor(true);
            if (!tfactor.equivalenteCon(t)) throw new SemanticException(20,token.numLinea);
            if(operador.equals("*")) {
                if(tfactor.esBoolean() || tfactor.esArreglo()) 
                        throw new SemanticException(22,token.numLinea);
                genMepa("MULT",true);
            }
            else  if(operador.equals("div")) {
                if(tfactor.esBoolean() || tfactor.esArreglo()) 
                        throw new SemanticException(22,token.numLinea);
                genMepa("DIVC",true);           
                genMepa("DIVI",true);
            }
            else  if(operador.equals("and")) {
                if(!tfactor.esBoolean()) 
                    throw new SemanticException(23,token.numLinea); 
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
        if(token.codigo==Token.OP_RESTA || token.codigo==Token.OP_SUMA || token.codigo==Token.OR){
            String op = token.lexema;
            if(!porValor) throw new SemanticException(19,token.numLinea);
            Tipo tterm = termino(true);
            
            if (!tterm.equivalenteCon(t)) throw new SemanticException(20,token.numLinea);
            if(op.equals("+")) {
                if(tterm.esBoolean() || tterm.esArreglo()) 
                        throw new SemanticException(21,token.numLinea);
                genMepa("SUMA",true);
            }
            else if(op.equals("-")) {
                if(tterm.esBoolean() || tterm.esArreglo()) 
                        throw new SemanticException(21,token.numLinea);
                genMepa("SUST",true);
            }
            else  if(op.equals("or")) {
                if(!tterm.esBoolean()) 
                    throw new SemanticException(23,token.numLinea); 
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
        
        if(token.codigo==Token.OP_RELACIONAL  || token.codigo==Token.IGUAL){
           String op = token.lexema;
           if(!porValor ) throw new SemanticException(17,token.numLinea);
           if (t.esArreglo())throw new SemanticException(14,token.numLinea);
           Tipo texp = expresionSimple(true);
                                
           if (texp.esArreglo())throw new SemanticException(14,token.numLinea);
           if(!texp.equivalenteCon(t)) throw new SemanticException(15,token.numLinea);
                      
           if(op.equals("<")) genMepa("CMME",true);
           else if(op.equals("<=")) genMepa("CMNI",true);
           else if(op.equals(">")) genMepa("CMMA",true);
           else if(op.equals(">=")) genMepa("CMYI",true);
           else if(op.equals("<>")) genMepa("CMDG",true);
           else if(op.equals("=")) genMepa("CMIG",true);
           
           return TipoFactory.crearTipoBooleano();
        } else{
            restaurarToken=true;
            return t;
        }       
    }
    
    /**
     * <listaParametrosActuales> ::= <vacio> | <expresion> <restoListaParametrosActuales>     
     */
    private void listaParametrosActuales(Entrada e)throws Exception{
        Entrada ePar=null;
        if(!e.predefinido) 
            ePar = e.listaParametros.get(0);
        Tipo texp;
        if(e.predefinido){
            if(e.nombre.equals("read") || e.nombre.equals("readln"))
               texp = expresion(false);
            else texp = expresion(true);
        }else 
            texp = expresion(ePar.porValor);
        
        
        if (!e.predefinido && !ePar.tipo.equivalenteCon(texp))  
            throw new SemanticException(29,token.numLinea);
        
        if(e.predefinido) {
            if(!texp.esSimple()) throw new SemanticException(30,token.numLinea);
            genPredefinida(e.nombre, texp);
        }
        restoListaParametrosActuales(e,1);
    }
    
   
    
    /**
     *
     * @throws java.lang.Exception
     */
    private void restoListaParametrosActuales(Entrada e, int indice)throws Exception{
        nextToken();           
        if(token.codigo==Token.COMA){
            if(indice >= e.listaParametros.size() && !e.predefinido) throw new SemanticException(31,token.numLinea); 
            Entrada ePar=null;
            if(!e.predefinido) 
                ePar= e.listaParametros.get(indice);  
            
            Tipo texp;
            if(e.predefinido){
                if(e.nombre.equals("read") || e.nombre.equals("readln"))
                    texp = expresion(false);
                else 
                    texp = expresion(true);
            } else 
                texp = expresion(ePar.porValor);
            
            if(!e.predefinido && !ePar.tipo.equivalenteCon(texp)) throw new SemanticException(29,token.numLinea); 
            if(e.predefinido) {
                if(!texp.esSimple()) throw new SemanticException(30,token.numLinea);
                genPredefinida(e.nombre, texp);
            }
            restoListaParametrosActuales(e,indice+1);
        } else{
            restaurarToken=true;
            if(indice < e.listaParametros.size()&& !e.predefinido) throw new SemanticException(31,token.numLinea);            
        }
    }
    
    /**
     * 
     * @throws java.lang.Exception
     */
    private Tipo factor(boolean porValor)throws Exception{
        nextToken();
        if(token.codigo==Token.PAR_ABRE){
            if(!porValor)  throw new SemanticException(16,token.numLinea);  
            Tipo texp = expresion(true);
            nextToken();
            if(token.codigo!=Token.PAR_CIERRA) throw new SintacticException(18,token.numLinea);
            return texp;
        } 
        else  if(token.codigo==Token.NOT){
            if(!porValor)  throw new SemanticException(19,token.numLinea);  
            Tipo tfactor =  factor(true);
            if(!tfactor.esBoolean()) throw new SemanticException(11,token.numLinea);
            genMepa("NEGA",true);
            return tfactor;
        }
        else  if(token.codigo==Token.NUMERO){
            if(!porValor)  throw new SemanticException(19,token.numLinea);
            int num = convertirAEntero(token.lexema,token.numLinea);
            genMepa("APCT",true,num);
            return TipoFactory.crearTipoEntero();
        }
        else  if(token.codigo==Token.ID){
            Entrada e = TS.buscar(token.lexema);
            if(e == null)  throw new SemanticException(7,token.numLinea,token.lexema);
            return restoFactor(porValor,e);
        } else
            throw new SintacticException(32,token.numLinea);
        
    }
    
    /**
     *
     * @throws java.lang.Exception
     */
    private Tipo restoFactor(boolean porValor, Entrada e)throws Exception{
        nextToken();
        //id[exp]
        if(token.codigo==Token.COR_ABRE){
            if (!e.tipo.esArreglo())  throw new SemanticException(10,token.numLinea);
           
            Tipo texp = expresion(true);
            
            TipoArray tipo = (TipoArray)e.tipo;
            
            if(! tipo.tipoDominio.equivalenteCon(texp))
                throw new SemanticException(12,token.numLinea);
           
            if(tipo.tipoDominio.esBoolean()) genMepa("CONT",true,0,1);
            else{
                TipoSubrango t = (TipoSubrango)tipo.tipoDominio;
                genMepa("CONT",true,t.getValInferior(),t.getValSuperior());
                genMepa("APCT",true,t.getValInferior());
                genMepa("SUST",true);
            }
            nextToken();
            if(token.codigo != Token.COR_CIERRA) throw new SintacticException(12,token.numLinea);
            
            if(porValor) {//debe dejar el valor
                //variable o parametro array por valor
                if(e.porValor) genMepa("APAR",true,e.nivelLexico,e.desplazamiento);
                //parametro array por referencia
                else genMepa("APAI",true,e.nivelLexico,e.desplazamiento);
            }
            else {//debe dejar la referencia
                
                //CORRECCION!
                //variable o parametro array por valor
                //if(e.porValor)  genMepa("APDR",true,e.nivelLexico,e.desplazamiento);
                                
                //parametro array por referencia
               // else  
                    genMepa("APDC",true, e.nivelLexico,e.desplazamiento);                
            }
            return tipo.tipoRango;
        } 
        //id(params)
        else if(token.codigo==Token.PAR_ABRE){
            if(!e.esFuncion())throw new SemanticException(26,token.numLinea);
            if(!porValor) throw new SemanticException(27,token.numLinea);
            if(!e.predefinido) genMepa("RMEM 1",true);
            listaParametrosActuales(e);
            nextToken();
            if(token.codigo!=Token.PAR_CIERRA)   throw new SintacticException(18,token.numLinea);
            if(!e.predefinido) genMepa("LLPR " + e.etiqueta, true);
            return e.tipo;
        } 
        //id 
        else{
            restaurarToken=true;
            if(e.esPrograma() || e.esProcedimiento() || e.esTipo())
                throw new SemanticException(36,token.numLinea);
            if(porValor){
                if(e.esConstante()){
                    genMepa("APCT  "+ e.valorStr,true);
                    return e.tipo;
                }
                else if(e.esVariable() ){
                    if(e.tipo.esSimple()){
                        genMepa("APVL",true,e.nivelLexico,e.desplazamiento);
                        return e.tipo;
                    }else{//copia del arreglo
                        int n = ((TipoArray)e.tipo).getNumComponentes();
                        genMepa("PUAR",true,e.nivelLexico,e.desplazamiento,n);                        
                        return e.tipo;
                    }
                } 
                else if (e.esParametro()){
                    if(e.porValor){
                        if(e.tipo.esSimple()){
                            genMepa("APVL",true,e.nivelLexico,e.desplazamiento);
                            return e.tipo;
                        }else{//copia del arreglo
                            int n = ((TipoArray)e.tipo).getNumComponentes();
                            genMepa("PUAR",true, e.nivelLexico,e.desplazamiento,n);                            
                            return e.tipo;
                        }
                    }
                    else{
                        if(e.tipo.esSimple()){
                            genMepa("APVI",true,e.nivelLexico,e.desplazamiento);
                            return e.tipo;
                        }
                        else{//copia del arreglo
                            int n = ((TipoArray)e.tipo).getNumComponentes();
                            genMepa("PUAI",true, e.nivelLexico,e.desplazamiento,n);                            
                            return e.tipo;
                        }
                    }
                }
                else{//funcion
                    if(e.listaParametros.size()!=0) throw new SemanticException(28,token.numLinea);
                    genMepa("RMEM 1",true);
                    genMepa("LLPR "+e.etiqueta, true);
                    return e.tipo;
                }
            }
            else {// por referencia
                if(e.esConstante() || e.esFuncion())
                    throw new SemanticException(24,token.numLinea);
                else if(e.esVariable() || (e.esParametro() && e.porValor)){
                    if(e.tipo.esSimple()){
                        genMepa("APDR",true,e.nivelLexico,e.desplazamiento);
                        return e.tipo;
                    }
                    else{//copia del arreglo
                        genMepa("APCT",true,0);
                        genMepa("APDC",true, e.nivelLexico,e.desplazamiento);                        
                        return e.tipo;
                    }
                }
                else {// parametro formal por referencia
                    genMepa("APVL",true,e.nivelLexico,e.desplazamiento);
                    return e.tipo;
                }
            }
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
     * Genera el codigo para las funcinoes y procedimientos predefinidos
     * con parametros.
     * @param id nombre de la funcion/procedimiento
     * @param t tipo del parametro
     */
    private void genPredefinida(String id, Tipo t) throws Exception{
        if (!(t.esEntero() || t.esSubrango()))
            throw new SemanticException(35,token.numLinea);
        
        if ( id.equalsIgnoreCase("write")) genMepa("IMPR",true);
        
        else if( id.equalsIgnoreCase("writeln"))  genMepa("IMLN",true);
        
        else if( id.equalsIgnoreCase("read")) {
            genMepa("LEER",true);
            if (t.esSubrango())
                genMepa("CONT", true,((TipoSubrango)t).getValInferior(),((TipoSubrango)t).getValSuperior());
            genMepa("ALDR",true);
        } 
        
        else if(id.equalsIgnoreCase("readln")){
            genMepa("LELN",true);
            if (t. esSubrango())
                genMepa("CONT", true,((TipoSubrango)t).getValInferior(),((TipoSubrango)t).getValSuperior());
            genMepa("ALDR",true);
        } 
        
        else if(id.equalsIgnoreCase("succ")) {
            if (t.esSubrango())
                genMepa("CONT",true, ((TipoSubrango)t).getValInferior(),((TipoSubrango)t).getValSuperior());
            else
                genMepa("CONT",true, Integer.MIN_VALUE,Integer.MAX_VALUE-1);
            genMepa("APCT 1",true );
            genMepa("SUMA",true);
            
        } 
        
        else if(id.equalsIgnoreCase("pred")) {
             if (t. esSubrango())
                genMepa("CONT",true, ((TipoSubrango)t).getValInferior(),((TipoSubrango)t).getValSuperior());
            else
                genMepa("CONT",true, Integer.MIN_VALUE+1,Integer.MAX_VALUE );
            genMepa("APCT 1",true );
            genMepa("SUST",true);
           
        }
    }
    
    /** 
     * @param num
     * @param numLinea
     * @return 
     */
    private Integer convertirAEntero(String num, int numLinea)throws Exception{
        Integer numI;
        try{
            numI=new Integer(Integer.parseInt(num));
        }catch(NumberFormatException e){
            throw new SemanticException(39,numLinea);
        }
        return numI;
    }
}
