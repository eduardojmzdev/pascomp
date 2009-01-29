package Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * BufferedFileReader.java Permite hacer un buffer la lectura de caracteres de
 * un archivo
 * 
 */
public final class BufferedFileReader extends BufferedReader implements Reader {

	/**
	 * Constructor
	 * 
	 * @param file
	 *            el nombre del archivo
	 */
	public BufferedFileReader(String file) throws IOException {
		super(new FileReader(file));
	}

	/**
	 * Cierra el archivo
	 */
	@Override
	public void close() {
		try {
			super.close();
		} catch (IOException e) {
		}
	}

	/**
	 * Lee un caracter del archivo
	 * 
	 * @return el proximo caracter del archivo o null si encontró el fin de
	 *         archivo
	 */
	public Character readCharacter() throws IOException {
		int ch = read();
		return (ch == -1) ? EOF : new Character((char) ch);
	}
}