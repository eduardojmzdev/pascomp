package mepa;

import Utils.BufferedFileReader;
import Utils.Reader;
import excepciones.MepaException;

import java.io.IOException;
import java.util.HashSet;
import main.Testeable;

/**
 *
 *
 * @see aLexico.TokenMepa
 * @see aLexico.BufferedFileReader
 * @see excepciones.MepaException
 * @see main.Testeable
 * @since jdk 1.6
 */
public final class ALMepa implements Testeable{
    
    /**
     * Numero de linea del ultimo caracter
     */
    private int numLinea;
    
    /**
     * Mantiene el ultimo caracter leido
     */
    private Character ultimoCharLeido;
    
    /**
     * Objeto que proporciona la operacion de lectura de caracteres
     * del codigo fuente
     */
    private Reader reader = null;
    
    
    /**
     * Constructor:
     *
     */
    public ALMepa(){
        numLinea = 1; //inicializa la linea de lectua actual
    }
    
    /**
     * Constructor:
     * Permite construir un Analizador lexico con
     * un Lector de codigo fuente dado.
     *
     * @param reader Lector de codigo fuente que provee los caracteres
     * @throws IOException si hubo erro al leer un caracter
     */
    public ALMepa(Reader reader) throws IOException {
        this();
        setReader(reader);
    }
    
    /**
     * Constructor:
     * Inicializa el bufferedFileReader con el archivo fuente del programa
     * y lee el primer caracter.
     *
     * @param strFile  nombre del archivo con el codigo fuente
     * @throws IOException si hubo excepcion al abrir el archivo
     */
    public ALMepa(String strFile) throws IOException {
        this();       
        setSourceFile(strFile);       
        leerCaracter();
    }
    
    
    /**
     * Setea el lector de codigo fuente para el analizador lexico
     * @param reader Lector de codigo
     * @throws IOException si hubo error al leer el primer caracter
     */
    public void setReader(Reader reader) throws IOException{
        this.reader = reader;
        numLinea = 1; //inicializa la linea de lectua actual
        ultimoCharLeido = null;
        leerCaracter();
    }
    
    /**
     * Permite cambiar el archivo fuente a analizar
     * 
     * @see main.Testeable
     * @param strFile nombre de archivo a analizar
     * @throws java.io.IOException 
     */
    public void setSourceFile(String strFile) throws IOException{
        if(reader!=null) {
            reader.close();
            reader = null;
        }
       
        this.reader = new BufferedFileReader(strFile);       
        numLinea = 1; //inicializa la linea de lectua actual
        ultimoCharLeido = null;
    }
    
    /**
     * 
     * @return 
     */
    public boolean testOK(){
        return true;
    }
    
    /**
     * Cierra el lector de codigo fuente
     * @throws IOException si hubo error al cerrarlo
     */
    public void cerrarLector() throws IOException {
        reader.close();
    }
    
    
    /**
     * Metodo principal de la clase. Su funcion es retornar el siguiente token.
     * 
     * @return el token encontrado o EOF si se encuentra el final de archivo.
     * @throws java.lang.Exception 
     */
    public TokenMepa nextToken() throws Exception{
        
        //guardo la ultima linea
        int lastLine = numLinea;
        Character ch = ultimoCharLeido;
        
        //RECONOCIMIENTO DE CARACTERES SEPARADORES ' ','\t','\n','\r'
        if (esSeparador(ch)) {
            do{
                ch = leerCaracter();
            }
            while(esSeparador(ch));
            lastLine = numLinea;
        }
        
        if(ch == Reader.EOF){
            return new TokenMepa(TokenMepa.EOF,lastLine);
        }
        
        if (ch.charValue() =='-'){
            leerCaracter();
            return new TokenMepa(TokenMepa.MENOS,lastLine);
        }
        
        if (ch.charValue()==',') {
            leerCaracter();
            return new TokenMepa(TokenMepa.COMA, lastLine);
        }
        
        //RECONOCIMIENTO DE NUMEROS
        if (esDigito(ch)){
            return getTokenNumero();
        }
        
        //RECONOCIMIENTO DE POSIBLE ETIQUETA
        if (ch.charValue()=='L' || ch.charValue()=='l'){
            return getTokenEtiqueta();
        }
        
        //RECONOCIMIENTO DE INSTRUCCIONES
        if (esLetra(ch)){
            return getTokenInstruccion();
        } else
            throw new MepaException(3,lastLine);
        
    }
    
    
    /**
     * 
     * 
     * @throws Exception si ocurrio un error
     * @return 
     */
    private TokenMepa getTokenEtiqueta() throws Exception {
        StringBuffer buff = new StringBuffer();
        Character ch = ultimoCharLeido; //asume que es una letra
        Character chAux;
        int lastLine = numLinea; //salva la linea actual
        //Ya se leyo l o L
        chAux = leerCaracter();
        
        if(esDigito(chAux)){
            do{
                buff.append(chAux.charValue());
                chAux = leerCaracter();
            }
            while(esDigito(chAux));
            
            String lex =  buff.toString();
            if(esLetra(chAux)){
                throw new MepaException(4,ch+lex+chAux,lastLine);
            }
            
            return new TokenMepa(TokenMepa.ETIQ, "L"+lex, lastLine);
        } else{
            //PUEDE SER INSTRUCCION
            while(esLetra(chAux)){
                buff.append(chAux.charValue());
                chAux=leerCaracter();
            }
            String lex = ch+ buff.toString();//agrega la L inicial
            if(esDigito(chAux))
                throw new MepaException(5, lex + chAux,lastLine);
            
            lex = lex.toUpperCase();
            Integer cod = TokenMepa.getCodigoInstruccion(lex);
            if(cod == null) throw new MepaException(6,lex,lastLine);
            
            return new TokenMepa(cod.intValue(),lex,lastLine);
        }
    }
    
    /**
     * 
     * @throws java.lang.Exception 
     * @return 
     */
    private TokenMepa getTokenInstruccion() throws Exception{
        Character ch = ultimoCharLeido;//asume que es una letra
        int lastLine = numLinea;
        StringBuffer buff = new StringBuffer();
        String lex;
        do{
            buff.append(ch.charValue());
            ch = leerCaracter();
        }
        while(esLetra(ch));
        lex = buff.toString();
        if(esDigito(ch))//ERROR
            throw new MepaException(5,lex+ch,lastLine);
        
        lex = lex.toUpperCase();
        
        //Me fijo si la instruccion pertenece al set de instrucciones
        Integer cod = TokenMepa.getCodigoInstruccion(lex);
        if(cod == null) throw new MepaException(6,lex,lastLine);
        
        return new TokenMepa(cod.intValue(),lex,lastLine);
    }
    
    /**
     * Reconoce Numeros
     * 
     * @return el token NUMERO
     * @throws java.lang.Exception 
     */
    private TokenMepa getTokenNumero() throws Exception{
        StringBuffer buff = new StringBuffer();
        Character ch = ultimoCharLeido;
        int lastLine = numLinea;
        
        do{
            buff.append(ch.charValue());
            ch = leerCaracter();
        }
        while(esDigito(ch));
        String lex = buff.toString();
        if(esLetra(ch))
            throw new MepaException(2,lex+ ch,numLinea);
        
        return new TokenMepa(TokenMepa.NUMERO,lex,lastLine);
    }
    
    
    /**
     * Lee un caracter
     *
     * @return el ultimo caracter leido
     * @throws IOException si ocurre error de I/O
     */
    private Character leerCaracter()throws IOException {
        if(reader==null) throw new IOException("No creaste ningun Reader para la clase!");
        
        ultimoCharLeido = reader.readCharacter();
        if(esFinalDeLinea(ultimoCharLeido)) {
            numLinea++; //incrementa la linea
        }
        return ultimoCharLeido;
    }
    
    /**
     * Conjunto de digitos
     */
    private static final HashSet<Character> digitos = new HashSet<Character>(10);
    static{
        for(char d = '0'; d<='9';d++) digitos.add(new Character(d));
    }
    
    /**
     * Conjunto de letras del alfabeto
     */
    private static final char[] listLetras={'a','b', 'c', 'd' ,'e', 'f', 'g' ,'h' ,'i', 'j' ,'k', 'l', 'm','n', 'o', 'p', 'q', 'r', 's','t','u','v','w','x','y','z',
    'A','B','C','D','E','F','G','H','I','J','K','L', 'M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z' };
    private static final HashSet<Character> letras  = new HashSet<Character>(listLetras.length);
    static{
        for(char ch:listLetras) letras.add(new Character(ch));
    }
    
    /**
     * Conjunto de caracteres separadores.
     * Un caracter es separador si es ' ' (space),'\n' (new line),'\t' (tab) o
     * '\r' (cr)
     */
    private static final HashSet<Character> separadores = new HashSet<Character>(4);
    static{
        separadores.add(new Character(' '));
        separadores.add(new Character('\n'));
        separadores.add(new Character('\t'));
        separadores.add(new Character('\r'));
    }
    
    
    /**
     * Determina si un caracter dado esta en el conjunto de las letras
     * @param ch el caracter
     * @return true si el caracter ch esta en el conjunto, false en otro caso
     */
    private boolean esLetra(Character ch){
        return letras.contains(ch);
    }
    
    
    /**
     * Determina si el caracter c esta en el conjunto de los digitos
     * @param ch el caracter
     * @return true si el caracter dado esta en el conjunto, false si no
     */
    private boolean esDigito(Character ch){
        return digitos.contains(ch);
    }
    
    /**
     * determina si el caracter c esta en el conjunto de separadores
     * @param ch el caracter
     * @return true si el caracter dado esta en el conjunto, false si no
     */
    private boolean esSeparador(Character ch){
        return separadores.contains(ch);
    }
    
    /**
     * determina si el caracter ch es fin de linea
     * @param ch el caracter
     * @return true si el caracter dado es fin de linea
     */
    private  boolean esFinalDeLinea(Character ch){
        return ((ch!=null) && (ch.charValue()=='\n'));
    }
    
    /**
     * Implementa el metodo run de la interface Testeable
     * @throws java.lang.Exception 
     */
    public void run() throws Exception {
        //Obtengo los tokens
        TokenMepa t= null;
        leerCaracter();
        for( t = nextToken(); t.codigo != TokenMepa.EOF; t = nextToken()){
            System.out.println(t);
        }
        //imprimo eof
        System.out.println(t);
        cerrarLector();
    }
    
    /**
     * Implementa el metodo finish de la interface Testeable
     */
    public void finish(){
        try {
            cerrarLector();
        } catch (IOException ex) {
        }
    }
        
    /**
     * Comprueba si el archivo tiene la extension correcta
     * @param strFile el nombre de archivo a verificar
     * @return true si tiene la extension correcta
     */
    public boolean validaExtension(String strFile) {
        return strFile.endsWith(".mep");
    }
    
}

