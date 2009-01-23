package Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;

/**
 * BufferedFileReader.java
 * Permite bufferear la lectura de caracteres de un archivo
 * 
 */
public final class BufferedFileReader extends BufferedReader implements Reader{
	
	/** Constructor
	 *  
	 * @param file el nombre del archivo
	 */
	public BufferedFileReader(String file)throws IOException{
		super(new FileReader(file));
	}
	
	/**
	 * Lee un caracter del archivo
	 * @return el proximo caracter del archivo o null si 
	 * 		   encontró el fin de archivo
	 */
	public Character readCharacter()throws IOException{        
        int ch = read();
        return (ch == -1)? EOF : new Character((char)ch);
    }
	
	   
	/** 
	 * Cierra el archivo
	*/ 
	public void close(){
		try {
			super.close();
		} catch (IOException e) {			
		}
    }
}