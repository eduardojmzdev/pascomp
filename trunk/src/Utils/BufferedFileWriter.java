package Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Permite bufferear la escritura en archivo
 *
 */
public class BufferedFileWriter extends BufferedWriter {
    
    /** Nombre de archivo */
    private String strFile = null;
    
    /** 
     * Constructor 
     * @param file nombre de archivo
     */
    public BufferedFileWriter(String file)throws IOException {
        super(new FileWriter(file));
        strFile = file;
    }
    
    /**
     * Obtiene el objeto File del archivo 
     * @return File el archivo
     */
    public File getFile(){
        return new File(strFile);
    }
}
