
 //Clase que engloba las distintas clases que componen los metodos de analisis del 
//lenguaje fuente de nuestro compilador

package traductor.aLexico;


 // Representa las distintas caracteristicas de nuestra clase token (categoria lexica) de los
 // con las que se componen la estructura lexica del lenguaje fuente

public class Token {
		
	
	 //Codigo de la categoria lexica a la que pertenece el token
	 
	private EnumToken categoria;
	

	

	  //Lexema que compone el token.
	 
	private String lexema;

	
	
	 //Constructor principal, inicializa los atributos

	public Token(EnumToken c, String l) {
		categoria = c;
		lexema = l;
	}
	

	 //Constructor por defecto
	
	public Token() {
		categoria = null;		
		lexema = null;
	}

	//Setters y getters
	
	public void setCategoria(EnumToken cat){
		categoria = cat;
	}

	
	public EnumToken getCategoria(){
		return categoria;
	}
	
	public void setLexema(String lex){
		lexema = lex;
	}
	
	public String getLexema(){
		return lexema;
	}
}
