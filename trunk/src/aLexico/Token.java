package aLexico;


/**
 * Token.java
 * Permite representar la informacion de un simbolo valido del programa. 
 * Los tokens son creados por el <code>AnalizadorLexico</code> a medida que este lee 
 * el archivo fuente del programa. Los tokens generados seran consumidos posteriormente por 
 * el <code>AnalizadorSintactico</code> para validar la sintaxis del programa.
 *
 * @see aLexico.AnalizadorLexico
 * @version 1.0
 */
public final class Token{
	
	/** texto del token */
	public String lexema;
	
	/** codigo del token*/
	public int codigo;
	
	/** Numero de linea del codigo donde aparece el token */
	public int numLinea;
	
		
	/** Lexemas 
	 * Nota: el primer null corresponde a identificadores, el segundo null corresponde a numeros y el 3er null corresponde al operador igual
	 */
	public static final String[] LEXEMAS ={"",null,null, //0-2
		";",".","..",":",",","(",")","[","]","program","const","type","var","array",   //3-16
		"begin","end","procedure","function","if","then","else","case",   //17-24
		"of","while","do",":=",null,"+","-","*","div","not","and","or","="};   //25-37
	
	/** Vector de nombres de los codigos del token */
	public static final String[] TOKENS = {"EOF","ID","NUMERO",
		"PUNTO_Y_COMA","PUNTO","PUNTO_PUNTO","DOS_PUNTOS","COMA","PAR_ABRE","PAR_CIERRA","COR_ABRE","COR_CIERRA","PROGRAM",
		"CONST","TYPE","VAR","ARRAY","BEGIN","END","PROCEDURE","FUNCTION","IF","THEN","ELSE","CASE","OF","WHILE","DO",
		"ASIGNACION","OP_RELACIONAL","OP_SUMA","OP_RESTA","OP_MULT","OP_DIV","NOT","AND","OR","IGUAL"};   
		
	public static final int EOF		= 0;
	
	/** Identificadores */
	public static final int ID		= 1;
	public static final int NUMERO	= 2;
	
	/** Signos de puntuacion */	
	public static final int PUNTO_Y_COMA = 3;
	public static final int PUNTO 		 = 4;
	public static final int PUNTO_PUNTO  = 5;      
	public static final int DOS_PUNTOS   = 6; 
	public static final int COMA 		 = 7;         
	
	/** Parentesis */
	public static final int PAR_ABRE   = 8;
	public static final int PAR_CIERRA = 9;
	public static final int COR_ABRE   = 10;
	public static final int COR_CIERRA = 11;
	
	/** Palabras reservadas */
	public static final int PROGRAM   = 12;
	public static final int CONST     = 13;
	public static final int TYPE	  = 14;
	public static final int VAR		  = 15;
	public static final int ARRAY	  = 16;
	public static final int BEGIN	  = 17;
	public static final int END		  = 18;
	public static final int PROCEDURE = 19;
	public static final int FUNCTION  = 20;
	public static final int IF		  = 21;
	public static final int THEN	  = 22;
	public static final int ELSE	  = 23;
	public static final int CASE	  = 24;
	public static final int OF		  = 25;
	public static final int WHILE	  = 26;
	public static final int DO		  = 27;
	
	public static final int ASIGNACION = 28; 
	
	public static final int OP_RELACIONAL = 29; 
	
//	OPERADORES ARITMETICOS
	public static final int OP_SUMA	  = 30; 
	public static final int OP_RESTA  = 31; 
	public static final int OP_MULT   = 32; 
	public static final int OP_DIV    = 33; 
	
//	OPERADORES LOGICOS
	public static final int NOT = 34;
	public static final int AND = 35;
	public static final int OR  = 36;

	public static final int IGUAL = 37;
	
	
	/** 
	 * Constructor 
	 * @param codigo numero que identifica al token
	 * @param lexema cadena de caracteres que representa al token
	 * @param numLinea numero de linea del codigo fuente donde aparece el token
	 */
	 public Token(int codigo, String lexema, int numLinea){
		this.codigo   = codigo;
		this.numLinea = numLinea;
		
                if(LEXEMAS[codigo]==null){
                    if(codigo==Token.ID)
                        this.lexema = lexema.toLowerCase();
                    else
                        this.lexema = lexema;
                }
                else
                    this.lexema = LEXEMAS[codigo];
                
                
	}
	
	/** Obtiene el lexema asociado al token
	 * @return String  la cadena de caracteres que representa al lexema del token
	 */
	public String getLexema(){
		return lexema;
	}
	
	/** 
	 * Convierte el token a String
	 * @return el string que representa el token
	 */
	@Override
	public String toString(){
		String n = Integer.toString(numLinea);
		String nl = ("00" + numLinea).substring(n.length()-1);
		
		return "[LINE="+nl+", "+TOKENS[codigo]+", "+getLexema()+"]";
	}
	
}