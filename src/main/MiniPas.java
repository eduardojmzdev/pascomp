
package main;

import aSintactico.AnalizadorSintactico;
import java.io.IOException;

import mVirtual.MaquinaVirtual;

/**
 * COMPILADOR 
 * 
 * @see aSintactico.AnalizadorSintactico
 * @see mepa.Mepa
 * @since 1.5
 */
public final class MiniPas implements Testeable{
    /** Analizador sintactico */
    private AnalizadorSintactico as = null;
    
    /** Maquina virtual */
    private MaquinaVirtual MV = null;
    
    /** Archivo a analizar */
    private String strFile;
   
    /**
     * Constructor 
     */
    public MiniPas(){
        as = new AnalizadorSintactico();
    }
    
    /**
     * Compila y ejecuta
     *
     * @throws java.lang.Exception si ocurre error en la compilacion o en la ejecucion
     */
    public void run() throws Exception {
        /** ETAPA DE COMPILACION Y GEN DE CODIGO */
        try{            
            as.setSourceFile(strFile);
            as.run();
            as.finish();
        } 
        catch(Exception ex){
            as.finish();
            throw ex;
        }
        
        /** ETAPA DE EJECUCION */
        try{
            //OBTENGO EL ARCHIVO .mep GENERADO
            java.io.File f = as.getOutputFile();
            
            //PUEDE SER NULL SI COMPILO SIN GENERACION DE CODIGO
            if(f==null) return;
                        
            //CREO LA MEPA Y EJECUTO EL ARCHIVO            
            MV = new MaquinaVirtual(f.getAbsolutePath());
            MV.run();
            //MV.finish();
            
        }
        catch(Exception ex){
            //MV.finish();
            //throw ex;
            ex.printStackTrace();
        }
    }
    
    /**
     * Setea el archivo a compilar
     * @param strFile nombre de archivo 
     * @throws java.io.IOException En este caso no ocurre excepcion
     */
    public void setSourceFile(String strFile) throws IOException {
        this.strFile = strFile;        
    }
    
    /** */
    public void finish() {
        //nada para hacer
    }
    
    /**
     * Verifica que el archivo tenga la extension correcta
     * @param strFile nombre de archivo a compilar
     * @return  true si el archivo soporta la extension correcta
     * @see config.conf
     */
    public boolean validaExtension(String strFile) {
        return as.validaExtension(strFile);
    }
    
}