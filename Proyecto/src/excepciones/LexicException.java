package excepciones;

import excepciones.CompiladorException;

/**
 * Representa un error Lexico
 * 
 *
 */
public final class LexicException extends CompiladorException {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		private int numLinea;
	/**
	 * constructor
	 * @param cod codigo del error generado
	 * @param numLinea representa el numero de la line en al que se generó el error
	 */

	
    public LexicException(String mensaje, int numLinea) {
        super(mensaje);
		this.numLinea = numLinea;
    }    
    
    @Override
	public String getMensajeError(){
    	return "Error Lexico en línea " + numLinea + ": " + super.getMensajeError();
    	
    }
		
}
