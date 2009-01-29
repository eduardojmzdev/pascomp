package excepciones;

/**
 * Representa a un error en la maquina virtual
 * 
 */
public class MaquinaVirtualException extends BasicException {

	/**
	 * Identificador de version
	 */
	private static final long serialVersionUID = 1L;

	/** MAPEO DE CODIGOS A ERRORES */
    static{
        errorMap.put(new Integer(0),"Division por cero!");
        errorMap.put(new Integer(1),"Valor invalido ingresado por teclado.");
        errorMap.put(new Integer(2),"Magnitud no representable ingresada por teclado.");
    }

	/**
	 * Constructor
	 * 
	 * @param codigo
	 *            codigo de error
	 */
	public MaquinaVirtualException(int codigo) {
		super("[MEPA] Error: " + errorMap.get(new Integer(codigo)));
		this.codigo = codigo;
	}

}
