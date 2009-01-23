package aLexico;

import Utils.BufferedFileReader;
import Utils.Reader;
import exceptions.LexicException;
import java.io.IOException;
import java.util.HashSet;
import java.util.HashMap;

public final class AnalizadorLexico {

    private int numLin;
    private Character LastCharRead;
    private Reader reader = null;
    /**
     * Tabla de palabras reservadas y codigo de tokens.
     * Cada entrada es de la forma:
     *   key     value
     * [lexema, codigo]
     */
    private static final HashMap<String, Integer> palabrasReservadas = new HashMap<String, Integer>(25);
    

    static {
        palabrasReservadas.put("and", new Integer(Token.AND));
        palabrasReservadas.put("begin", new Integer(Token.SEP));
        palabrasReservadas.put("end", new Integer(Token.FIN));
        palabrasReservadas.put("not", new Integer(Token.NOT));
        palabrasReservadas.put("or", new Integer(Token.OR));
        palabrasReservadas.put("program", new Integer(Token.INICIO));
        palabrasReservadas.put("integer", new Integer(Token.tipent));
        palabrasReservadas.put("boolean", new Integer(Token.tipbool));
        palabrasReservadas.put("true", new Integer(Token.cierto));
        palabrasReservadas.put("false", new Integer(Token.falso));
        palabrasReservadas.put("var", new Integer(Token.VAR));            
    }

    public AnalizadorLexico() {
        numLin = 1;

    }

    public AnalizadorLexico(Reader reader) throws IOException {
        this();
        this.reader = reader;
        leerCaracter();
    }

    public AnalizadorLexico(String strFile) throws IOException {
        this();
        this.reader = new BufferedFileReader(strFile);
        leerCaracter();
    }

    public void setReader(Reader reader) throws IOException {
        this.reader = reader;
        numLin = 1;
        LastCharRead = null;
        leerCaracter();
    }

    public void setSourceFile(String ruta) throws IOException {
        if (reader != null) {
            reader.close();
            reader = null;
        }
        this.reader = new BufferedFileReader(ruta);
        numLin = 1;
        LastCharRead = null;
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
    public Token nextToken() throws IOException, LexicException {

        while (true) {
            //guardo la ultima linea
            int lastLine = numLin;
            Character ch = LastCharRead;

            if (ch == Reader.EOF) {
                return new Token(Token.EOF, "", lastLine);
            } //RECONOCIMIENTO DE CARACTERES SEPARADORES ' ','\t','\n','\r'
            else if (esSeparador(ch)) {
                do {
                    ch = leerCaracter();
                } while (ch != Reader.EOF && esSeparador(ch));

            } else if (ch.charValue() == '(') {
                leerCaracter();
                return new Token(Token.PA, "", lastLine);
            } //RECONOCIMIENTO DE CARACTERES SIMPLES
            else if (ch.charValue() == '+') {
                //avanza al proximo carater antes de retornar
                leerCaracter();
                return new Token(Token.SUMA, "", lastLine);
            } else if (ch.charValue() == '-') {
                leerCaracter();
                return new Token(Token.RESTA, "", lastLine);
            } else if (ch.charValue() == '*') {
                leerCaracter();
                return new Token(Token.MUL, "", lastLine);
            } else if (ch.charValue() == '=') {
                leerCaracter();
                return new Token(Token.IGUAL, "", lastLine);
            } else if (ch.charValue() == '.') {
                leerCaracter();
                return new Token(Token.PUNTO, "", lastLine);
            } else if (ch.charValue() == ';') {
                leerCaracter();
                return new Token(Token.PYCOMA, "", lastLine);
            } else if (ch.charValue() == ',') {
                leerCaracter();
                return new Token(Token.COMA, "", lastLine);
            } else if (ch.charValue() == ')') {
                leerCaracter();
                return new Token(Token.PC, "", lastLine);
            } else if (ch.charValue() == '>') {
                return leerMayorIgual();
            } else if (ch.charValue() == '<') {
                return leerMenorIgual();
            } else if (ch.charValue() == ':') {
                return leerDosPuntosAsig();
            } //RECONOCIMIENTO DE IDENTIFICADORES Y PALABRAS RESERVADAS
            else if (esLetra(ch)) {
                return getTokenID();
            } //RECONOCIMIENTO DE NUMEROS
            else if (esDigito(ch)) {
                return getTokenNumero();
            } else {
                //caracter no perteneciente al alfabeto
                throw new LexicException(4, ch.toString(), lastLine);
            }
        }
    }

    private Token leerDosPuntosAsig() throws IOException {

        Character ch = leerCaracter();
        if ((ch == Reader.EOF) || (ch.charValue() != '=')) {
            return new Token(Token.DOSPUNTOS, "", numLin);
        }
        leerCaracter();
        return new Token(Token.ASIG, "", numLin);
    }

    private Token leerMayorIgual() throws IOException {
        Character proxi = leerCaracter();
        if ((proxi == Reader.EOF) || (proxi.charValue() != '=')) {
            return new Token(Token.OPREL, ">", numLin);
        }        
        leerCaracter(); 
        return new Token(Token.OPREL, ">=", numLin);
    }

    private Token leerMenorIgual() throws IOException {
        Character proxi = leerCaracter();
        if ((proxi == Reader.EOF) || (proxi.charValue() != '=')) {
            return new Token(Token.OPREL, "<", numLin);
        }
        leerCaracter();
            return new Token(Token.OPREL, "<=", numLin);        
                
    }

    private Token getTokenID() throws IOException {
        Character c;
        StringBuffer buff = new StringBuffer();
        c = LastCharRead; //asume que es una letra
        int ultimaLinea = numLin; //salva la linea actual
        do {
            buff.append(c.charValue());
            c = leerCaracter();
        } while (esDigito(c) || esLetra(c));
        String lex = buff.toString();
        String lexema = lex.toLowerCase();
        Integer cod = palabrasReservadas.get(lexema);
        if (cod != null) {
            return new Token(cod.intValue(), lex, ultimaLinea);
        } else {
            return new Token(Token.id, lex, ultimaLinea);
        }
    }

    private Token getTokenNumero() throws IOException, LexicException {
        Character c;
        StringBuffer buff = new StringBuffer();
        c = LastCharRead;
        int lineaUlt = numLin;//salva la liena actual
        do {
            buff.append(c.charValue());
            c = leerCaracter();
        } while (esDigito(c));
        if (esLetra(c)) {
            throw new LexicException(0, buff.toString() + c.toString(), lineaUlt);
        }
        return new Token(Token.digito, buff.toString(), lineaUlt);
    }

    private Character leerCaracter() throws IOException {
        if (reader == null) {
            throw new IOException("No creaste ningun Reader para la clase!");
        }
        LastCharRead = reader.readCharacter();
        if (esFinalDeLinea(LastCharRead)) {
            numLin++; //incrementa la linea

        }
        return LastCharRead;
    }
    /**
     * Conjunto de digitos
     */
    private static final HashSet<Character> digitos = new HashSet<Character>(10);
    

    static {
        for (char d = '0'; d <= '9'; d++) {
            digitos.add(new Character(d));
        }
    }

    private static final char[] listLetras = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };
    
    private static final HashSet<Character> letras = new HashSet<Character>(listLetras.length);
    

    static {
        for (char ch : listLetras) {
            letras.add(new Character(ch));
        }
    }
    
    private static final HashSet<Character> separadores = new HashSet<Character>(4);
    

    static {
        separadores.add(new Character(' '));
        separadores.add(new Character('\n'));
        separadores.add(new Character('\t'));
        separadores.add(new Character('\r'));
    }

    private boolean esLetra(Character ch) {
        return letras.contains(ch);
    }

    private boolean esDigito(Character ch) {
        return digitos.contains(ch);
    }

    private boolean esSeparador(Character ch) {
        return separadores.contains(ch);
    }

    private boolean esFinalDeLinea(Character ch) {
        return ((ch != null) && (ch.charValue() == '\n'));
    }

    public void run() throws Exception {
        //Obtengo los tokens
        Token t = null;
        leerCaracter();
        for (t = nextToken(); t.cod != Token.EOF; t = nextToken()) {
            System.out.println(t);
        }
        //imprimo eof
        System.out.println(t);
        cerrarLector();
    }

    /**
     * Implementa el metodo finish de la interface Testeable
     */
    public void finish() {
        try {
            cerrarLector();
        } catch (IOException ex) {
        }
    }

    public boolean validaExtencion(String strFile) {
        return strFile.endsWith(".pas");
    }
}