package Utils;

import java.io.IOException;

/**
 * Permite crear Objetos que leen caracteres desde diferentes fuentes, por ejemplo
 * un archivo, un String en memoria, un cinta, cd-room, etc..
 * Un ejemplo de Reader es la clase <code>BufferedFileReader</code>
 * usada por el <code>AnalizadorLexico</code>
 * 
 * @version 1.0 30/08/2006 ("Compiladores e Interpretes"- Universidad Nacional del Sur)
 * @see aLexico.BufferedFileReader 	  
 */
public interface Reader {
	/**
	 * Denota el final de la fuente de caracteres
	 */
	public static final Character EOF = null;	
	
	/**
	 * @return el caracter leido
	 * @throws IOException si ocurre error de E/S
	 */
	public Character readCharacter()throws IOException;	
	
	/**
	 * cierra la fuente de caracteres 
	 */
	public void close();
}
