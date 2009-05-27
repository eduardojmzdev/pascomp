
package traductor.excepciones;

import java.util.HashMap;

/**
 * Clase base de las excepciones del compilador
 *
 */
public class BasicException extends Exception{
    

	private static final long serialVersionUID = 1L;
    
    /** Creates a new instance of BasicException */
    public BasicException(String txt) {
        super(txt);
    }
    

}
