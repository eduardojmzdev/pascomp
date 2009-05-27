/**
 * Paquete que engloba las distintas clases que componen el módulo de análisis
 * léxico del lenguaje fuente del compilador.
 */
package traductor.aLexico;

/**
 * Implementa la información requerida para cada token (categoría léxica) de los
 * que se compone la estructura léxica del lenguaje fuente.
 */
public class Token {
		
	/**
	 * Codigo de la categoría léxica a la que pertenece el token.
	 */
	private EnumToken categoria;
	

	
	/**
	 * Lexema que compone el token.
	 */
	private String lexema;

	
	/**
	 * Constructor principal, inicializa los componentes a utilizar
     * en el objeto según los valores indicados.
     * @param c Codigo de la categoría léxica.
     * @param v Valor del token.
     * @param l Lexema del token.
	 */
	public Token(EnumToken c, String l) {
		categoria = c;
		lexema = l;
	}
	
	/**
	 * Constructor por defecto, inicializa los componentes a utilizar
     * en el objeto.
	 */
	public Token() {
		categoria = null;		
		lexema = null;
	}

	/**
	 * Mutador para el atributo categoría.
	 * @param cat Codigo de la categoría léxica.
	 */
	public void setCategoria(EnumToken cat){
		categoria = cat;
	}

	/**
	 * Accesor para el atributo categoría.
	 * @return Valor del atributo categoria.
	 */
	public EnumToken getCategoria(){
		return categoria;
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
}
