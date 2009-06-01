package excepciones;



/**
 * Representa un error en la maquina virtual 
 *
 */
public class MVException extends BasicException{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int numLinea;

	/** MAPEO DE CODIGOS A ERRORES */
    static{
    	errorMap.put(new Integer(0),"se espera un entero");
        errorMap.put(new Integer(1),"Division por cero!");
        errorMap.put(new Integer(2),"se espera un booleano");
        errorMap.put(new Integer(3),"pila vacia, no hay operandos");
        errorMap.put(new Integer(4),"valor null (puede que una variable no haya sido inicializada)");
        errorMap.put(new Integer(5),"se espera un entero o booleano");
        
    }
    
    /** 
     * Constructor
     * @param codigo codigo de error
     */
    public MVException(int codigo) {
        super("[MV] Error: "+ errorMap.get(new Integer(codigo)));
        this.codigo = codigo;
    }

	public int getNumLinea() {
		return numLinea;
	}

	public void setNumLinea(int numLinea) {
		this.numLinea = numLinea;
	}
    
	public String getError(){
		return errorMap.get(new Integer(codigo));
	}

}
