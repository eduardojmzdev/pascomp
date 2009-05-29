package mVirtual.instrucciones.pila;


import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.Instruccion;
import excepciones.MVException;

public class InstruccionApila implements Instruccion {

	private String param;
	
	/**
	 * Ejecuta la instruccion
	 * @throws MVException. Si hay un error en la ejecucion
	 */
	public void ejecutar() throws MVException {
		MaquinaVirtual.obtenerInstancia().getPila().push(param);
		
	}

	/**
	 * @return String: representa la instruccion
	 */
	public String toString() {
		return this.getClass().getSimpleName()+"("+param+")";
	}
	/**
	 * Setter
	 * @param String: param
	 */
	public void setParam(String param) {
		this.param = param;
	}


}
