/**
 * Paquete que engloba las distintas clases que componen el módulo de análisis
 * léxico del lenguaje fuente del compilador.
 */
package aLexico;

/**
 * Importamos los recursos para los tipos definidos.
 */
import utils.*;
import utils.error.Error;
import utils.error.TError;

/**
 * Librería para el uso de ficheros de texto para lectura.
 */
import java.io.FileReader;

/**
 * Librería de excepciones ante el uso de ficheros.
 */
import java.io.IOException;

/**
 * Implementa la maquina de estados para el analisis y obtención de los tokens 
 * del codigo presente en un fichero, según el lenguaje fuente del compilador.
 */
public class AnalizadorLexico  {
	
	/**
	 * Error existente en el código.
	 */
	private Error errorLexico = null;
	
	/**
	 * Cadena en procesamiento.
	 */
	private String lexema;
	
	/**
	 * Último caracter leido.
	 */
	private char caracter;
	
	/**
	 * Número de línea del fichero donde se encuentra el puntero de lectura.
	 */
	private int linea;

	/**
	 * Número de estado en el que está el autómata.
	 */
	private int estado;
	
	/**
	 * Último token reconocido.
	 */
	private TToken token;
	
	/**
	 * Fichero del que se lee el código fuente a analizar.
	 */
	private FileReader ficheroFuente;

	/**
	 * Constructor por defecto, inicializa los componentes a utilizar
     * en el objeto.
	 */
	public AnalizadorLexico(){
		lexema = ""; 
		caracter = ' ';
		linea = 1; 
		estado = 0;
		token = new TToken();
	}
	
	/**
	 * Constructor principal, inicializa los componentes a utilizar
     * en el objeto, indicando el fichero fuente a analizar.
     * @param f Fichero con el código fuente a analizar.
	 */
	public AnalizadorLexico(FileReader f){
		ficheroFuente = f;
		lexema = ""; 
		caracter = ' ';
		linea = 1; 
		estado = 0;
		token = new TToken();
	}
	
	/**
	 * Accesor para el atributo errorLexico.
	 * @return Error presente en el código.
	 */
	public Error getError(){
			return errorLexico;
	}
	
	/**
	 * Accesor para el atributo linea.
	 * @return Valor del atributo linea.
	 */
	public int getLinea(){
		return linea;
	}
	
	/**
	 * Accesor para el atributo ficheroFuente.
	 * @return Fichero con el código fuente para el análisis.
	 */
	public FileReader getFichero(){
		return ficheroFuente;
	}
	
	/**
	 * Mutador para el atributo ficheroFuente.
	 * @param f Fichero con el código fuente para el análisis.
	 * @throws IOException 
	 */
	public void setFichero(FileReader f){
		lexema = ""; 
		caracter = ' ';
		linea = 1; 
		estado = 0;
		ficheroFuente = f;
	}
	
	/**
	 * Función para la obtención de los tokens presentes en el fichero con el 
	 * código fuente. Ante la presencia de errores léxicos, se comunicarán y
	 * se finalizará la ejecución.
	 * @return Siguiente Token reconocido desde la última posición del
	 * puntero de lectura del fichero. Se devuelve null en caso de que
	 * se llegue a fin de fichero.
	 */
	public TToken obtenerToken(){
		
		lexema = "";
		try{
			while(true){ // Repetimos hasta obtener token o final de fichero				
				switch(estado){
				case 0: // Estado inicial
					switch(caracter){
					case '#': lexema +=caracter;
							  estado = 1;
							  break;
					case '0': lexema += caracter;
							  estado = 2; 
							  caracter = (char)ficheroFuente.read();
							  break;
					case '1':	//Números naturales.
					case '2':
					case '3':
					case '4':
					case '5':
					case '6':
					case '7':
					case '8':
					case '9':lexema +=caracter; estado = 3; caracter = (char)ficheroFuente.read();
							break;
					case 'a':
					case 'b':	//Letras minúsculas
					case 'c':	//para el comienzo de los identificadores.
					case 'd':
					case 'e':
					case 'f':
					case 'g':
					case 'h':
					case 'i':
					case 'j':
					case 'k':
					case 'l':
					case 'm':
					case 'n':
					case 'ñ':
					case 'o':
					case 'p':
					case 'q':
					case 'r':
					case 's':
					case 't':
					case 'u':
					case 'v':
					case 'w':
					case 'x':
					case 'y':
					case 'z':
						lexema +=caracter; estado = 4;
						caracter = (char)ficheroFuente.read();
						break;
					case ',':
						lexema +=caracter; estado = 5;
						caracter = (char)ficheroFuente.read();
						break;
					case ';':
						lexema +=caracter; estado = 6;
						caracter = (char)ficheroFuente.read();
						break;
					case '(':
						lexema +=caracter; estado = 7;
						caracter = (char)ficheroFuente.read();
						break;
					case ')':
						lexema +=caracter; estado = 8;
						caracter = (char)ficheroFuente.read();
						break;
					case ':':
						lexema +=caracter; estado = 9;
						caracter = (char)ficheroFuente.read();
						break;
					case '=':
						lexema +=caracter; estado = 11;
						caracter = (char)ficheroFuente.read();
						break;
					case '!':
						lexema +=caracter; estado = 13;
						caracter = (char)ficheroFuente.read();
						break;
					case '<':case'>':
						lexema +=caracter; estado = 14;
						caracter = (char)ficheroFuente.read();
						break;
					case '+':case '-':
						lexema +=caracter; estado = 16;
						caracter = (char)ficheroFuente.read();
						break;
					case '|':
						lexema +=caracter; estado = 17;
						caracter = (char)ficheroFuente.read();
						break;
					case '*':case'/':case'&':
						lexema +=caracter; estado = 18;
						caracter = (char)ficheroFuente.read();
						break;
					case 'M':
						lexema +=caracter; estado = 19;
						caracter = (char)ficheroFuente.read();
						break;
					case 'E':
						lexema +=caracter; estado = 21;
						caracter = (char)ficheroFuente.read();
						break;
					case 'L':
						lexema +=caracter; estado = 24;
						caracter = (char)ficheroFuente.read();
						break;
					case 'C':
						lexema +=caracter; estado = 26;
						caracter = (char)ficheroFuente.read();
						break;
					case 'F':
						lexema +=caracter; estado = 32;
						caracter = (char)ficheroFuente.read();
						break;
					case 'I':
						lexema +=caracter; estado = 39;
						caracter = (char)ficheroFuente.read();
						break;
					case 'V':
						lexema +=caracter; estado = 44;
						caracter = (char)ficheroFuente.read();
						break;
					case 'P':
						lexema +=caracter; estado = 46;
						caracter = (char)ficheroFuente.read();
						break;
					case 'T':
						lexema +=caracter; estado = 74;
						caracter = (char)ficheroFuente.read();
						break;
					case 'R':
						lexema +=caracter; estado = 80;
						caracter = (char)ficheroFuente.read();
						break;
					case 'N':
						lexema +=caracter; estado = 82;
						caracter = (char)ficheroFuente.read();
						break;
				
					case '\n':
						estado = 0;
						linea++;
						caracter = (char)ficheroFuente.read();
						break;
						
					case ' ':
						estado = 0;
						caracter = (char)ficheroFuente.read();
						break;
						
					case '\r':
						estado = 0;
						caracter = (char)ficheroFuente.read();
						break;
						
					case '\t':
						estado = 0;
						caracter = (char)ficheroFuente.read();
						break;
						
					case 'S':
						lexema +=caracter; estado = 64;
						caracter = (char)ficheroFuente.read();
						break;
					
					case 'H':
						lexema +=caracter; estado = 53;
						caracter = (char)ficheroFuente.read();
						break;
						
					case '^':
						lexema +=caracter; estado = 72;
						caracter = (char)ficheroFuente.read();
						break;
						
					case '.':
						lexema +=caracter; estado = 73;
						caracter = (char)ficheroFuente.read();						
						break;
									
					default:
						// Caracter no perteneciente a la gramática
						if(ficheroFuente.ready()){
							errorLexico = new Error(TError.LEXICO, "Línea "+linea+":"+" '"+caracter+"'"+".");
							return null;
						}else{ // Fin de fichero: devolver token vacío
							TToken t = new TToken();
							return t;
						}
					}
					break;
					
				// Resto de estados
				case 1:
					// Salta una línea completa
					while (caracter != '\n'){
						caracter = (char)ficheroFuente.read();
					}
					estado = 0;
					lexema = "";
					linea++;
					caracter = (char)ficheroFuente.read();
					break;
					
				case 2:
					// Control de errores
					if (!((caracter >='a' && caracter <='z') 	|| (caracter >='A' && 
						  caracter <='Z') || (caracter >='0' && caracter <='9'))){
						token.setCategoria(TToken.TKNUM);
						token.setLexema(null);
						token.setValor(Integer.parseInt(lexema));
						token.setTipo(TTipo.ENT);
						estado = 0;
						return token;
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+":"+" '"+lexema+caracter+"'"+".");
						return null;
					}
					
				case 3:
					if (caracter >= '0' && caracter <='9'){
						lexema += caracter;
						caracter = (char)ficheroFuente.read();
						break;
					}
					else{
						// Control de errores
						if (!((caracter >='a' && caracter <='z') || (caracter >='A' && caracter <='Z'))){
							token.setCategoria(TToken.TKNUM);
							token.setValor(Integer.parseInt(lexema));
							token.setTipo(TTipo.ENT);
							token.setLexema(null);
							estado = 0;
							return token;
						}
						else{
							errorLexico = new Error(TError.LEXICO, "Línea "+linea+":"+" '"+lexema+caracter+"'"+".");
							return null;
						}
					}
											
				case 4:
					if (((caracter >='a' && caracter <='z') || (caracter >='A' && 
							  caracter <='Z') || (caracter >='0' && caracter <='9'))){
						lexema+=caracter;
						caracter = (char)ficheroFuente.read();
					}
					else{
						token.setCategoria(TToken.TKIDEN);
						token.setLexema(lexema);
						token.setValor(null);
						token.setTipo(null);
						estado = 0;
						return token;
					}
					break;
	
				case 5:
					token.setCategoria(TToken.TKCOMA);
					token.setLexema(lexema);
					token.setValor(null);
					token.setTipo(null);
					estado = 0;
					return token;

				case 6:
					token.setCategoria(TToken.TKPYC);
					token.setLexema(lexema);
					token.setValor(null);
					token.setTipo(null);
					estado = 0;
					return token;
					
				case 7:
					token.setCategoria(TToken.TKPABRE);
					token.setLexema(lexema);
					token.setValor(null);
					token.setTipo(null);
					estado = 0;
					return token;
					
				case 8:
					token.setCategoria(TToken.TKPCIERRA);
					token.setLexema(lexema);
					token.setValor(null);
					token.setTipo(null);
					estado = 0;
					return token;
					
				case 9:
					estado = 10;
					break;
					
				case 10:
					if(caracter == '='){
						lexema+=caracter;
						token.setCategoria(TToken.TKASIG);
						token.setLexema(lexema);
						token.setValor(null);
						token.setTipo(null);
						estado = 0;
						caracter = (char)ficheroFuente.read();
						return token;
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Se esperaba '='.");
						return null;
					}
							
				case 11:
					estado = 12;
					break;
					
				case 12:
					if (caracter == '='){
						lexema+=caracter;
						token.setCategoria(TToken.TKIGUAL);
						token.setLexema(lexema);
						token.setValor(null);
						token.setTipo(null);
						estado = 0;
						caracter = (char)ficheroFuente.read();
						return token;
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Se esperaba '='.");
						return null;
					}
										
				case 13:
					if (caracter == '='){
						lexema+=caracter;
						token.setCategoria(TToken.TKDESIGUAL);
						token.setLexema(lexema);
						token.setValor(null);
						token.setTipo(null);
						estado = 0;
						caracter = (char)ficheroFuente.read();
						return token;
					}
					else{
						token.setCategoria(TToken.TKNEG);
						token.setLexema(lexema);
						token.setValor(null);
						token.setTipo(null);
						estado = 0;
						return token;
					}
					
				case 14:
					if (caracter == '='){
						lexema+=caracter;
						int cat = 0;
						if (lexema.equals("<="))
							cat = TToken.TKMENORIGUAL;
						else
							cat = TToken.TKMAYORIGUAL;
						token.setCategoria(cat);
						token.setValor(null);
						token.setTipo(null);
						token.setLexema(lexema);
						estado = 0;
						caracter = (char)ficheroFuente.read();
						return token;
					}
					else {
						int cat = 0;
						if (lexema.equals("<"))
							cat = TToken.TKMENOR;
						else
							cat = TToken.TKMAYOR;
						token.setCategoria(cat);
						token.setValor(null);
						token.setTipo(null);
						token.setLexema(lexema);
						estado = 0;
						return token;
					}
					
				case 16:
					int cat = 0;
					if (lexema.equals("+"))
						cat = TToken.TKSUMA;
					if (lexema.equals("-"))
						cat = TToken.TKRESTA;
					if (lexema.equals("||"))
						cat = TToken.TKOR;
						
					token.setCategoria(cat);
					token.setLexema(lexema);
					token.setValor(null);
					token.setTipo(null);
					estado = 0;
					return token;

				case 17:
					if (caracter == '|'){
						lexema += caracter;
						estado = 16;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Se esperaba '|'.");
						return null;
					}
					break;
					
				case 18:
					int ct = 0;
					if (lexema.equals("*"))
						ct = TToken.TKPROD;
					if (lexema.equals("/"))
						ct = TToken.TKDIV;
					if (lexema.equals("&"))
						ct = TToken.TKAND;				
					if (lexema.equals("MOD"))
						ct = TToken.TKMOD;
					token.setCategoria(ct);
					//cambiado por el sintáctico
					token.setLexema(lexema);
					token.setValor(null);
					token.setTipo(null);
					estado = 0;
					return token;
					
				case 19:
					if (caracter == 'O'){
						lexema += caracter;
						estado = 20;
						caracter = (char)ficheroFuente.read();
					}
					else if(caracter == 'I'){
						lexema += caracter;
						estado = 58;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}					
					break;
					
				case 20:
					if (caracter == 'D'){
						lexema += caracter;
						caracter = (char)ficheroFuente.read();
						if (!((caracter >='a' && caracter <='z') || (caracter >='A' && 
								caracter <='Z'))){
								estado = 18;
						}
						else{
							errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
							return null;
						}	
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;
					
				case 21:
					if (caracter == 'N'){
						lexema += caracter;
						estado = 22;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;
					
				case 22:
					if (caracter == 'T'){
						lexema += caracter;
						estado = 67;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;

				case 24:
					if (caracter == 'O'){
						lexema += caracter;
						estado = 25;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;				
					}
					break;
					
				case 25:
					if (caracter == 'G'){
						lexema += caracter;
						estado = 37;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;
					
				case 26:
					if (caracter == 'I'){
						lexema += caracter;
						estado = 27;
						caracter = (char)ficheroFuente.read();
					}
					else if (caracter == 'O'){
						lexema += caracter;
						estado = 35;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;
					
				case 27:
					if (caracter == 'E'){
						lexema += caracter;
						estado = 28;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;
					
				case 28:
					if (caracter == 'R'){
						lexema += caracter;
						estado = 29;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;
					
				case 29:
					if (caracter == 'T'){
						lexema += caracter;
						estado = 30;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;
					
				case 30:
					if (caracter == 'O'){
						lexema += caracter;
						estado = 31;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;
					
				case 31:
					int c;
					if (lexema.equals("CIERTO")){
						c = TToken.TKCIERTO;
					} else{
						c = TToken.TKFALSO;
					}
					token.setCategoria(c);
					token.setValor(lexema);
					token.setTipo(TTipo.LOG);
					token.setLexema(null);
					estado = 0;
					return token;
					
				case 32:
					if (caracter == 'A'){
						lexema += caracter;
						estado = 33;
						caracter = (char)ficheroFuente.read();
					}
					else if (caracter =='I'){
						lexema += caracter;
						estado = 38;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;
					
				case 33:
					if (caracter == 'L'){
						lexema += caracter;
						estado = 34;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;
					
				case 34:
					if (caracter == 'S'){
						lexema += caracter;
						estado =30;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;
					
				case 35:
					if (caracter == 'N'){
						lexema += caracter;
						estado = 36;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;
					
				case 36:
					if (caracter == 'S'){
						lexema += caracter;
						estado = 37;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;
					
				case 37:
					token.setTipo(null);
					if (!((caracter >='a' && caracter <='z') || (caracter >='A' && 
							caracter <='Z') || (caracter >='0' && caracter <='9'))){
						int cate = 0;
						if (lexema.equals("LOG")){
							cate = TToken.TKLOG;
							token.setTipo(TTipo.LOG);
						}
						if (lexema.equals("CONS"))
							cate = TToken.TKCONS;
						if (lexema.equals("FIN"))
							cate = TToken.TKFIN;
						if (lexema.equals("INICIO"))
							cate = TToken.TKINICIO;
						if (lexema.equals("VAR"))
							cate = TToken.TKVAR;
						if (lexema.equals("PROGRAMA"))
							cate = TToken.TKPROGRAMA;
						if (lexema.equals("SINO"))
							cate = TToken.TKSINO;
						if (lexema.equals("ENTONCES"))
							cate = TToken.TKENTONCES;
						if (lexema.equals("MIENTRAS"))
							cate = TToken.TKMIENTRAS;
						if (lexema.equals("HACER"))
							cate = TToken.TKHACER;
						if (lexema.equals("TIPO"))
							cate = TToken.TKTIPO;
						if (lexema.equals("PROCEDIMIENTO"))
							cate = TToken.TKPROCEDIMIENTO;
						if (lexema.equals("PROC"))
							cate = TToken.TKPROC;
						if (lexema.equals("REG"))
							cate = TToken.TKREG;
						if (lexema.equals("NUEVO"))
							cate = TToken.TKNUEVO;
						
						token.setCategoria(cate);
						token.setLexema(null);
						token.setValor(null);
						estado = 0;
						return token;
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+":"+" '"+lexema+caracter+"'"+".");
						return null;
					}
					
				case 38:
					if (caracter == 'N'){
						lexema += caracter;
						estado = 37;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");			
						return null;
					}
					break;
					
				case 39:
					if (caracter == 'N'){
						lexema += caracter;
						estado = 40;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");				
						return null;
					}
					break;			
					
				case 40:
					if (caracter == 'I'){
						lexema += caracter;
						estado = 41;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");					
						return null;
					}
					break;	
					
				case 41:
					if (caracter == 'C'){
						lexema += caracter;
						estado = 42;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");			
						return null;
					}
					break;	
					
				case 42:
					if (caracter == 'I'){
						lexema += caracter;
						estado = 43;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");					
						return null;
					}
					break;	
					
				case 43:
					if (caracter == 'O'){
						lexema += caracter;
						estado = 37;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");		
						return null;
					}
					break;		
					
				case 44:
					if (caracter == 'A'){
						lexema += caracter;
						estado = 45;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");	
						return null;
					}
					break;	
					
				case 45:
					if (caracter == 'R'){
						lexema += caracter;
						estado = 37;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");		
						return null;
					}
					break;	
					
				case 46:
					if (caracter == 'R'){
						lexema += caracter;
						estado = 47;
						caracter = (char)ficheroFuente.read();
					}
					
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");			
						return null;
					}
					break;	
										
				case 47:
					if (caracter == 'O'){
						lexema += caracter;
						estado = 48;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");	
						return null;
					}
					break;		
					
				case 48:
					if (caracter == 'G'){
						lexema += caracter;
						estado = 49;
						caracter = (char)ficheroFuente.read();
					}
					else if (caracter == 'C'){
						lexema += caracter;
						estado = 76;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");			
						return null;
					}
					break;			
					
				case 49:
					if (caracter == 'R'){
						lexema += caracter;
						estado = 50;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");			
						return null;
					}
					break;	
					
				case 50:
					if (caracter == 'A'){
						lexema += caracter;
						estado = 51;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");		
						return null;
					}
					break;		
					
				case 51:	
					if (caracter == 'M'){
						lexema += caracter;
						estado = 52;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");			
						return null;
					}
					break;	
					
				case 52:
					if (caracter == 'A'){
						lexema += caracter;
						estado = 37;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;
					
				case 53:
					if (caracter == 'A'){
						lexema += caracter;
						estado = 54;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;	
					
				case 54:
					if (caracter == 'C'){
						lexema += caracter;
						estado = 55;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;	
					
				case 55:
					if (caracter == 'E'){
						lexema += caracter;
						estado = 56;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;	
					
				case 56:
					if (caracter == 'R'){
						lexema += caracter;
						estado = 37;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;	
				
				case 58:
					if (caracter == 'E'){
						lexema += caracter;
						estado = 59;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;
					
				case 59:
					if (caracter == 'N'){
						lexema += caracter;
						estado = 60;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;	
					
				case 60:
					if (caracter == 'T'){
						lexema += caracter;
						estado = 61;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;	
					
				case 61:
					if (caracter == 'R'){
						lexema += caracter;
						estado = 62;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;	
				
				case 62:
					if (caracter == 'A'){
						lexema += caracter;
						estado = 71;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;	
					
				case 64:
					if (caracter == 'I'){
						lexema += caracter;
						estado = 65;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;
					
				case 65:
					if (caracter == 'N'){
						lexema += caracter;
						estado = 66;
						caracter = (char)ficheroFuente.read();
					}
					else{
						if (!((caracter >='a' && caracter <='z') || (caracter >='A' && 
								caracter <='Z') || (caracter >='0' && caracter <='9'))){
							
							token.setCategoria(TToken.TKSI);
							token.setLexema(lexema);
							token.setValor(null);
							token.setTipo(null);
							estado = 0;
							return token;
							
						}else{
							errorLexico = new Error(TError.LEXICO, "Línea "+linea+":"+" '"+lexema+caracter+"'"+".");
							return null;
						}
					}
					break;
					
				case 66:
					if (caracter =='O'){
						lexema += caracter;
						estado = 37;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;
				
				case 67:
					if (caracter == 'O'){
						lexema += caracter;
						estado = 68;
						caracter = (char)ficheroFuente.read();
					}
					else{ 
						if (!((caracter >='a' && caracter <='z') || (caracter >='A' && 
								caracter <='Z') || (caracter >='0' && caracter <='9'))){
							
								token.setCategoria(TToken.TKENT);
								token.setLexema(lexema);
								token.setValor(null);
								token.setTipo(TTipo.ENT);
								estado = 0;
								return token;
						}else{
							errorLexico = new Error(TError.LEXICO, "Línea "+linea+":"+" '"+lexema+caracter+"'"+".");
							return null;
						}
					}
					break;
					
				case 68:
					if (caracter == 'N'){
						lexema += caracter;
						estado = 69;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;
					
				case 69:
					if (caracter == 'C'){
						lexema += caracter;
						estado = 70;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;	
					
				case 70:
					if (caracter == 'E'){
						lexema += caracter;
						estado = 71;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;	
					
				case 71:
					if (caracter == 'S'){
						lexema += caracter;
						estado = 37;
						caracter = (char)ficheroFuente.read();
					}
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");
						return null;
					}
					break;
					
					
				case 72:
					token.setCategoria(TToken.TKGORRO);
					token.setLexema(lexema);
					token.setValor(null);
					token.setTipo(null);
					estado = 0;
					return token;
					
				case 73:				
					token.setCategoria(TToken.TKPUNTO);
					token.setLexema(lexema);
					token.setValor(null);
					token.setTipo(null);
					estado = 0;
					return token;
					
				case 74:
					if (caracter == 'I'){
						lexema += caracter;
						estado = 75;
						caracter = (char)ficheroFuente.read();
					}
					
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");			
						return null;
					}
					break;
							
				case 75:
					if (caracter == 'P'){
						lexema += caracter;
						estado = 66;
						caracter = (char)ficheroFuente.read();
					}
					
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");			
						return null;
					}
					break;
							
					
				
				case 76:
					if (caracter == 'E'){
						lexema += caracter;
						estado = 77;
						caracter = (char)ficheroFuente.read();
					}
					else if (!((caracter >='a' && caracter <='z') || (caracter >='A' && 
								caracter <='Z') || (caracter >='0' && caracter <='9'))){
							
							token.setCategoria(TToken.TKPROC);
							token.setLexema(lexema);
							token.setValor(null);
							token.setTipo(null);
							estado = 0;
							return token;
						}
					
					
					
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");			
						return null;
					}
					break;
					
				case 77:
					if (caracter == 'D'){
						lexema += caracter;
						estado = 78;
						caracter = (char)ficheroFuente.read();
					}
					
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");			
						return null;
					}
					break;
					
				case 78:
					if (caracter == 'I'){
						lexema += caracter;
						estado = 79;
						caracter = (char)ficheroFuente.read();
					}
					
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");			
						return null;
					}
					break;
				case 79:
					if (caracter == 'M'){
						lexema += caracter;
						estado = 85;
						caracter = (char)ficheroFuente.read();
					}
					
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");			
						return null;
					}
					break;
				case 80:
					if (caracter == 'E'){
						lexema += caracter;
						estado = 81;
						caracter = (char)ficheroFuente.read();
					}
					
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");			
						return null;
					}
					break;
				case 81:
					if (caracter == 'G'){
						lexema += caracter;
						estado = 37;
						caracter = (char)ficheroFuente.read();
					}
					
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");			
						return null;
					}
					break;
					
				case 82:
					if (caracter == 'U'){
						lexema += caracter;
						estado = 83;
						caracter = (char)ficheroFuente.read();
					}
					
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");			
						return null;
					}
					break;
					
				case 83:
					if (caracter == 'E'){
						lexema += caracter;
						estado = 84;
						caracter = (char)ficheroFuente.read();
					}
					
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");			
						return null;
					}
					break;
							
				case 84:
					if (caracter == 'V'){
						lexema += caracter;
						estado = 66;
						caracter = (char)ficheroFuente.read();
					}
					
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");			
						return null;
					}
					break;
				case 85:
					if (caracter == 'I'){
						lexema += caracter;
						estado = 86;
						caracter = (char)ficheroFuente.read();
					}
					
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");			
						return null;
					}
					break;
				case 86:
					if (caracter == 'E'){
						lexema += caracter;
						estado = 87;
						caracter = (char)ficheroFuente.read();
					}
					
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");			
						return null;
					}
					break;
				case 87:
					if (caracter == 'N'){
						lexema += caracter;
						estado = 88;
						caracter = (char)ficheroFuente.read();
					}
					
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");			
						return null;
					}
					break;
				case 88:
					if (caracter == 'T'){
						lexema += caracter;
						estado = 66;
						caracter = (char)ficheroFuente.read();
					}
					
					else{
						errorLexico = new Error(TError.LEXICO, "Línea "+linea+": "+"Un identificador debe comenzar en minúsculas.");			
						return null;
					}
					break;
								
					
				}
				
			}//while
		}//try
		catch(IOException e){
			//Tratar excepción
		}
	
		return null;
	}
}
