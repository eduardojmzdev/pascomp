package aLexico;

import Utils.BufferedFileReader;
import Utils.Reader;
import excepciones.LexicException;

import java.io.IOException;
import java.util.HashSet;
import java.util.HashMap;
import main.Testeable;

/**
 * <P>Analizador Lexico para el lenguaje. Este lenguaje es un
 * subconjunto del lenguaje Pascal. 
 * El metodo nextToken es el principal y devuelve un <code>Token</code>
 * del lenguaje.
 * <br>Modo uso:
 * <pre>
 *     AnalizadorLexico alex = new AnalizadorLexico("program.pas");
 *     Token t = alex.nextToken();
 *     while(t.codigo != Token.EOF){
 *         System.out.println(t.toString());
 *     }
 *     alex.cerrarLector();
 * </pre>
 * <p>Esta clase implementa la interface <code>Testeable</code> para poder ser
 * testeada con uno o varios archivos simultaneamente.
 * <br><b>Ejemplo:</b>
 * <pre>
 *      //testea el archivo arch.pas
 *      Testeador t= new Testeador("arch.pas",new AnalizadorLexico());
 *       t.start();
 *
 *      //testea los archivos dentro del directorio actual (y subdirectorios)
 *     Testeador t= new Testeador(".",new AnalizadorLexico());
 *      t.start();
 *
 *</pre>
 * @see aLexico.Token
 * @see aLexico.BufferedFileReader
 * @see excepciones.LexicException
 * @see main.Testeable
 * @since jdk 1.6
 */
public final class AnalizadorLexico implements Testeable{
    
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
     * Tabla de palabras reservadas y codigo de tokens.
     * Cada entrada es de la forma:
     *   key     value
     * [lexema, codigo]
     */
    private static final HashMap<String,Integer> palabrasReservadas = new HashMap<String,Integer>(25);
    static{
        palabrasReservadas.put("and",new Integer(Token.AND));
        palabrasReservadas.put("array",new Integer(Token.ARRAY));
        palabrasReservadas.put(":=",new Integer(Token.ASIGNACION));
        palabrasReservadas.put("begin",new Integer(Token.BEGIN));
        palabrasReservadas.put("case",new Integer(Token.CASE));
        palabrasReservadas.put(",",new Integer(Token.COMA));
        palabrasReservadas.put("const",new Integer(Token.CONST));
        palabrasReservadas.put("[",new Integer(Token.COR_ABRE));
        palabrasReservadas.put("]",new Integer(Token.COR_CIERRA));
        palabrasReservadas.put("do",new Integer(Token.DO));
        palabrasReservadas.put("..",new Integer(Token.DOS_PUNTOS));
        palabrasReservadas.put("else",new Integer(Token.ELSE));
        palabrasReservadas.put("end",new Integer(Token.END));
        palabrasReservadas.put("function",new Integer(Token.FUNCTION));
        palabrasReservadas.put("if",new Integer(Token.IF));
        palabrasReservadas.put("not",new Integer(Token.NOT));
        palabrasReservadas.put("of",new Integer(Token.OF));
        palabrasReservadas.put("or",new Integer(Token.OR));
        palabrasReservadas.put("procedure",new Integer(Token.PROCEDURE));
        palabrasReservadas.put("program",new Integer(Token.PROGRAM));
        palabrasReservadas.put("div",new Integer(Token.OP_DIV));
        palabrasReservadas.put("then",new Integer(Token.THEN));
        palabrasReservadas.put("type",new Integer(Token.TYPE));
        palabrasReservadas.put("var",new Integer(Token.VAR));
        palabrasReservadas.put("while",new Integer(Token.WHILE));
    }
    
    
    /** ESTADOS PARA EL RECONOCIMIENTO DE COMENTARIOS
     *  DE LA FORMA (* ... *)
     *  */
    
    /** Estado inicial	 */
    private static final int INICIAL 		  		  = 0;
    
    /** Se leyo el primer parentesis que abre el comentario */
    private static final int ESTADO_PAR_ABRE  		  = 1;
    
    /** Se leyo el primer asterisco que abre el comentario */
    private static final int ESTADO_PRIMER_ASTERISCO  = 2;
    
    /** Se leyo un segundo asterisco */
    private static final int ESTADO_SEGUNDO_ASTERISCO = 3;
    
    /**
     * Constructor:
     *
     */
    public AnalizadorLexico(){
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
    public AnalizadorLexico(Reader reader) throws IOException {
        this();
        this.reader = reader;
        leerCaracter();
    }
    
    /**
     * Constructor:
     * Inicializa el bufferedFileReader con el archivo fuente del programa
     * y lee el primer caracter.
     *
     * @param strFile  nombre del archivo con el codigo fuente
     * @throws IOException si hubo excepcion al abrir el archivo
     */
    public AnalizadorLexico(String strFile) throws IOException {
        this();
        this.reader = new BufferedFileReader(strFile);
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
     *@param strFile nombre de archivo a analizar
     *@see main.Testeable
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
     * Cierra el lector de codigo fuente
     * @throws IOException si hubo error al cerrarlo
     */
    public void cerrarLector() throws IOException {
        reader.close();
    }
    
    
    /**
     * Metodo principal de la clase. Su funcion es retornar el siguiente token.
     * @return el token encontrado o EOF si se encuentra el final de archivo.
     * @throws IOException si hubo error en la lectura
     * @throws LexicException si hubo algun error lexico
     */
    public Token nextToken() throws IOException, LexicException{
        
        while(true){
            //guardo la ultima linea
            int lastLine = numLinea;
            Character ch = ultimoCharLeido;
            
            if(ch == Reader.EOF){
                return new Token(Token.EOF,"",lastLine);
            }
            //RECONOCIMIENTO DE CARACTERES SEPARADORES ' ','\t','\n','\r'
            else if (esSeparador(ch)) {
                do{
                    ch = leerCaracter();
                }
                while(ch!=Reader.EOF && esSeparador(ch));
                //continue;
            }
            //RECONOCIMIENTO DE COMENTARIOS DEL ESTILO { ... }
            else if(ch.charValue() == '{'){
                do{
                    ch = leerCaracter();
                }
                while(ch!=Reader.EOF && ch.charValue()!='}');
                if(ch!=Reader.EOF) leerCaracter();
                else{
                    throw new LexicException(2,numLinea);
                }
                //continue;
            }
            //LEYO UN PARENTESIS QUE ABRE, PUEDE SER COMENTARIO O NO.
            else if(ch.charValue() == '('){
                Token token = leerComentarioPar();
                //SI NO ES NULL ENTONCES ES UN PARENTESIS
                if(token!=null) return token;
                continue;
            }
            
            //RECONOCIMIENTO DE CARACTERES SIMPLES
            else if (ch.charValue()=='+') {
                //avanza al proximo carater antes de retornar
                leerCaracter();
                return new Token(Token.OP_SUMA ,"",lastLine);
            } else if (ch.charValue()=='-') {
                leerCaracter();
                return new Token(Token.OP_RESTA ,"",lastLine);
            } else if (ch.charValue()=='*') {
                leerCaracter();
                return new Token(Token.OP_MULT,"", lastLine);
            }else if (ch.charValue()=='=') {
                leerCaracter();
                return new Token(Token.IGUAL,"", lastLine);
            }
            //LEYO UN . PERO PUEDE TAMBIEN SER ..
            else if (ch.charValue()=='.') {
                return leerPuntoPunto();
            } else if (ch.charValue()==';') {
                leerCaracter();
                return new Token(Token.PUNTO_Y_COMA,"", lastLine);
            } else if (ch.charValue()==',') {
                leerCaracter();
                return new Token(Token.COMA,"", lastLine);
            } else if (ch.charValue()==')') {
                leerCaracter();
                return new Token(Token.PAR_CIERRA,"", lastLine);
            } else if (ch.charValue()=='[') {
                leerCaracter();
                return new Token(Token.COR_ABRE,"", lastLine);
            } else if (ch.charValue()==']') {
                leerCaracter();
                return new Token(Token.COR_CIERRA,"", lastLine);
            } else if (ch.charValue()=='>') {
                return leerMayorIgual();
            } else if (ch.charValue()=='<') {
                return  leerMenorIgualDist();
            } else if (ch.charValue()==':') {
                return  leerDosPuntosOAsignacion();
            } else if (ch.charValue()=='}') {
                throw new LexicException(3, lastLine);
            }
            //RECONOCIMIENTO DE IDENTIFICADORES Y PALABRAS RESERVADAS
            else  if (esLetra(ch)){
                return getTokenID();
            }
            //RECONOCIMIENTO DE NUMEROS
            else if (esDigito(ch)){
                return getTokenNumero();
            } else {
                //caracter no perteneciente al alfabeto
                throw new LexicException(4, ch.toString(),lastLine);
            }
        }
    }
    
    /**
     * Permite reoconocer parentesis del estilo "(* .. *)" o un '(' en caso
     * que el proximo caracter sea distinto que '*'.
     * <br>Ejemplo:
     * <pre>
     * si la entrada contiene (* ... *)    retorna null
     * si la entrada contiene (otro*... *) retorna el token PAR_ABRE
     * si la entrada contiene (*... * )  *) retorna el token null
     * </pre>
     * @return el <code>Token</code> Token.PAR_ABRE si el proximo caracter no es '*'. null en otro caso
     * @throws IOException si ocurrio algun error de I/O
     * @throws LexicException si se encontro un simbolo no valido para el lenguaje
     */
    private Token leerComentarioPar()throws IOException, LexicException{
        Character proxiChar; //proximo caracter a procesar
        int estado = INICIAL;
        int lineaComent = numLinea; //lnea de cominezo de comentrario
        int lineaPar = numLinea;//linea del ( si se reconoce
        proxiChar = ultimoCharLeido;
        while (true){
            switch(estado) {
                case INICIAL:{
                    if (proxiChar== Reader.EOF) {
                        return null;
                    } else if (proxiChar.charValue()=='(') {
                        lineaComent = numLinea;
                        lineaPar = numLinea;//salva la linea de (
                        estado = ESTADO_PAR_ABRE;
                        proxiChar = leerCaracter();
                    } else if (esSeparador(proxiChar)) {
                        proxiChar = leerCaracter();
                    } else
                        return null;
                    
                }break;
                
                case ESTADO_PAR_ABRE:{
                    if(proxiChar == Reader.EOF) {
                        return new Token(Token.PAR_ABRE,"",lineaPar);
                    } else if (proxiChar.charValue()=='*')  {
                        proxiChar = leerCaracter();
                        estado = ESTADO_PRIMER_ASTERISCO;
                    } else  {
                        return new Token(Token.PAR_ABRE,"",lineaPar);
                    }
                    break;
                }
                
                case ESTADO_PRIMER_ASTERISCO:{
                    if (proxiChar == Reader.EOF) {
                        throw new LexicException(1, lineaComent);
                    } else if (proxiChar.charValue()=='*') {
                        proxiChar = leerCaracter();
                        estado = ESTADO_SEGUNDO_ASTERISCO;
                    } else{
                        proxiChar = leerCaracter();
                    }
                    break;
                }
                
                case ESTADO_SEGUNDO_ASTERISCO: {
                    //	ya se proceso (*'cualqueier cosa'*
                    if(proxiChar == Reader.EOF){
                        throw new LexicException(1, lineaComent);
                    } else if (proxiChar.charValue()==')') {
                        proxiChar = leerCaracter();
                        estado = INICIAL;
                    } else if (proxiChar.charValue()=='*')  {
                        proxiChar = leerCaracter();
                    } else {
                        proxiChar = leerCaracter();
                        estado = ESTADO_PRIMER_ASTERISCO;
                    }
                    break;
                }
            }//final de switch
        }//Final del while
        
    }
    
    
    /**
     * Reconoce la asignacion(:=)  y los dos puntos (:).
     * Asume que el ultimo caracter leido fue ':'
     * @return el Token ASIGNACION si el proximo caracter es ':', en otro caso
     * 		   retorna el token DOS_PUNTOS.
     * @throws IOException si ocurrio error de I/O
     */
    private Token leerDosPuntosOAsignacion()throws IOException{
        // asume que ultimoCharLeido :
        int lineaUlt = numLinea; //salva la linea actual
        
        Character ch = leerCaracter();
        if ((ch==Reader.EOF) || (ch.charValue()!= '=')) {
            return new Token(Token.DOS_PUNTOS,"",lineaUlt);
        }
        //el caracter actual es =
        leerCaracter(); //avanza al proximo carater antes de retornar
        return new Token(Token.ASIGNACION,"",lineaUlt);
    }
    
    /**
     * Reconoce el punto '.' o los dos puntos '..'
     * @return el token PUNTO_PUNTO si el proximo caracter es '.'; en otro
     *         caso retorna el token PUNTO
     * @throws IOException si ocurrio error de I/O
     */
    private Token leerPuntoPunto()throws IOException{
        int lineaUlt = numLinea;
        
        Character proxi = leerCaracter();
        if ((proxi==Reader.EOF) || (proxi.charValue()!= '.')) {
            return new Token(Token.PUNTO,"",lineaUlt);}
        
        leerCaracter(); //avanza al proximo carater antes de retornar
        return new Token(Token.PUNTO_PUNTO,"",lineaUlt);
    }
    
    
    /**
     * reconoce el mayor (>) o el mayor igual (>=)
     * @return el token OP_RELACIONAL con el lexema '>=' si el proximo caracter es '='
     *         , en otro caso es '>'
     * @throws IOException si ocurrio error de I/O
     */
    private Token leerMayorIgual()throws IOException{
        // asume que ultimoCharLeido >
        int lineaUlt = numLinea; //salva la linea actual
        
        Character proxi=leerCaracter();
        if ((proxi==Reader.EOF) || (proxi.charValue()!= '=')) {
            return new Token(Token.OP_RELACIONAL,">",lineaUlt);}
        //el caracter actual es =
        leerCaracter(); //avanza al proximo carater antes de retouna
        return new Token(Token.OP_RELACIONAL,">=",lineaUlt);
    }
    
    
    /**
     * Reconoce el menor (<), el menor igual (<=) o el distinto (<>)
     * Asume que se leyo el caracter '<'
     * @return el token OP_RELACIONAL <pre>
     * 			con el lexema '<' si el siguiente caracter no es ni '=' ni '>', o
     *          con el lexema '<=' si el siguiente caracter es '=', o
     *          con el lexema '<>' si el siguiente caracter es '>' </pre>
     * @throws IOException si ocurrio error de I/O
     */
    private Token leerMenorIgualDist()throws IOException{
        // asume que ultimoCharLeido <
        int lineaUlt = numLinea; //salva la linea actual
        
        Character proxi = leerCaracter();
        if ((proxi==Reader.EOF) || (proxi.charValue()!= '='  && proxi.charValue()!='>')) {
            return new Token(Token.OP_RELACIONAL,"<",lineaUlt);}
        
        leerCaracter();   //avanza al proximo carater antes de retornar
        
        if (proxi.charValue()=='='){ //debe ser <=            
            return new Token(Token.OP_RELACIONAL,"<=",lineaUlt);
        }
        //el caracter leido proxi debe ser >
        return new Token(Token.OP_RELACIONAL,"<>",lineaUlt);
    }
    
    
    /**
     * Reconoce palabras reservadas e identificadres
     * @return el token ID
     * @throws IOException si ocurrio error de I/O
     */
    private Token getTokenID() throws IOException {
        Character c;
        StringBuffer buff = new StringBuffer();
        c=ultimoCharLeido; //asume que es una letra
        int ultimaLinea = numLinea; //salva la linea actual
        
        do{
            buff.append(c.charValue());
            c = leerCaracter();
        }while(esDigito(c)||esLetra(c));
        String lex =  buff.toString();
        String lexema = lex.toLowerCase();
        
        Integer cod = palabrasReservadas.get(lexema);
        if(cod!=null){
            return new Token(cod.intValue(),lex,ultimaLinea);
        } else
            return new Token(Token.ID, lex, ultimaLinea);
    }
    
    
    /**
     * Reconoce Numeros
     * @return el token NUMERO
     * @throws IOException si ocurre error de I/O
     * @throws LexicException si el identificador esta mal formado
     */
    private Token getTokenNumero() throws IOException, LexicException{
        Character c;
        StringBuffer buff = new StringBuffer();
        c = ultimoCharLeido; //asume que es un digito
        int lineaUlt = numLinea;//salva la liena actual
        
        do {
            buff.append(c.charValue());
            c = leerCaracter();
        }
        while (esDigito(c));
        
        if (esLetra(c)){
            throw new LexicException(0,buff.toString()+c.toString(), lineaUlt);
        }
        return new Token(Token.NUMERO,buff.toString(),lineaUlt);
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
     */
    public void run() throws Exception {
        //Obtengo los tokens
        Token t= null;
        leerCaracter();
        for( t = nextToken(); t.codigo != Token.EOF; t = nextToken()){
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
    
    public boolean validaExtencion(String strFile) {
        return strFile.endsWith(".pas");
    }
}