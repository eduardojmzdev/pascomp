
package excepciones;


/**
 * Clase basica de las excepciones del compilador
 *
 */
public class CompiladorException extends Exception{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
   
    /** Creates a new instance of BasicException */
    public CompiladorException(String txt) {
        super(txt);
    }
    
    /**
     * Obtiene el mensaje de error de la excepcion
     * @return el codigo de error
     */
    public String getMensajeError(){
        return getMessage();
    }
}
