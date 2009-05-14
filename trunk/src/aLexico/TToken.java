/**
 * Paquete que engloba las distintas clases que componen el módulo de análisis
 * léxico del lenguaje fuente del compilador.
 */
package aLexico;

/**
 * Importamos los recursos para los tipos definidos.
 */
import utils.*;

/**
 * Implementa la información requerida para cada token (categoría léxica) de los
 * que se compone la estructura léxica del lenguaje fuente.
 */
public class TToken {
	
	// Tokens
	public static final int TKPROGRAMA = 256; //PROGRAMA
	public static final int TKCONS = 257; //CONS
	public static final int TKVAR = 258; //VAR
	public static final int TKINICIO = 259; //INICIO
	public static final int TKFIN = 260; //FIN
	public static final int TKENT = 261; //ENT
	public static final int TKLOG = 262; //LOG
	public static final int TKNUM = 263; //Números (enteros)
	public static final int TKCIERTO = 264; //CIERTO
	public static final int TKFALSO = 265; //FALSO
	public static final int TKIDEN = 266; //Identificadores ([a-z] ([a-z] | [A-Z] | [0-9])*)
	public static final int TKCOMA = 267; //,
	public static final int TKPYC = 268; //;
	public static final int TKPABRE = 269; //(
	public static final int TKPCIERRA = 270; //)
	public static final int TKASIG = 271; //:=
	public static final int TKSUMA = 272; //+
	public static final int TKRESTA = 273; //-
	public static final int TKOR = 274; //||
	public static final int TKPROD = 275; //*
	public static final int TKDIV = 276; // / 
	public static final int TKMOD = 277; //MOD
	public static final int TKAND = 278; //&
	public static final int TKIGUAL = 279; // ==
	public static final int TKDESIGUAL = 280; //!=
	public static final int TKMENOR = 281; //<	
	public static final int TKMENORIGUAL = 282; //<=,
	public static final int TKMAYOR = 283; //>
	public static final int TKMAYORIGUAL = 284; //>=
	public static final int TKNEG = 285; //!
	public static final int TKCOMENT = 286; //#
	public static final int TKSI = 287; //SI
	public static final int TKENTONCES = 288; //ENTONCES
	public static final int TKSINO = 289; //SINO
	public static final int TKMIENTRAS = 290; //MIENTRAS
	public static final int TKHACER = 291; //HACER
	public static final int TKTIPO = 292; //TIPO
	public static final int TKREG = 293; // REGISTRO
	public static final int TKPROC = 294; //PROC
	public static final int TKNUEVO = 295; //NUEVO
	public static final int TKGORRO = 296; //^ (Puntero)
	public static final int TKPUNTO = 297; //. (Punto)
	public static final int TKPROCEDIMIENTO = 298; //PROCEDIMIENTO

	/**
	 * Codigo de la categoría léxica a la que pertenece el token.
	 */
	private int categoria;
	
	/**
	 * Valor del token.
	 */
	private Object valor;
	
	/**
	 * Lexema que compone el token.
	 */
	private String lexema;
	
	/**
	 * Tipo al que pertenece el token.
	 */
	private TTipo tipo;
	
	/**
	 * Constructor principal, inicializa los componentes a utilizar
     * en el objeto según los valores indicados.
     * @param c Codigo de la categoría léxica.
     * @param v Valor del token.
     * @param l Lexema del token.
     * @param t Tipo del token.
	 */
	public TToken(int c, Object v, String l, TTipo t) {
		categoria = c;
		valor = v;
		lexema = l;
		tipo = t;
	}
	
	/**
	 * Constructor por defecto, inicializa los componentes a utilizar
     * en el objeto.
	 */
	public TToken() {
		categoria = 0;
		valor = null;
		lexema = null;
		tipo = null;
	}

	/**
	 * Mutador para el atributo categoría.
	 * @param cat Codigo de la categoría léxica.
	 */
	public void setCategoria(int cat){
		categoria = cat;
	}

	/**
	 * Accesor para el atributo categoría.
	 * @return Valor del atributo categoria.
	 */
	public int getCategoria(){
		return categoria;
	}
	
	/**
	 * Mutador para el atributo valor.
	 * @param val Valor del token.
	 */
	public void setValor(Object val){
		valor = val;
	}
	
	/**
	 * Accesor para el atributo valor.
	 * @return Valor del token.
	 */
	public Object getValor(){
		return valor;
	}
	
	/**
	 * Mutador para el atributo lexema.
	 * @param lex Lexema del token.
	 */
	public void setLexema(String lex){
		lexema = lex;
	}
	
	/**
	 * Accesor para el atributo lexema.
	 * @return Lexema del token.
	 */
	public String getLexema(){
		return lexema;
	}
	
	/**
	 * Mutador para el atributo tipo.
	 * @param tip Tipo del token.
	 */
	public void setTipo(TTipo tip){
		tipo = tip;
	}
		
	/**
	 * Accesor para el atributo tipo.
	 * @return Tipo del token.
	 */
	public TTipo getTipo(){
		return tipo;
	}
}
