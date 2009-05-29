package mVirtual.instrucciones.pila;


import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.Instruccion;
import excepciones.MVException;

public class ApilaDir implements Instruccion {

	private String param;
	
	/**
	 * Ejecuta la instruccion
	 * @throws MVException. Si hay un error en la ejecucion
	 */
	public void ejecutar() throws MVException {
		try{
			MaquinaVirtual.obtenerInstancia().getPila().push(MaquinaVirtual.obtenerInstancia().getMemoriaDatos().get(Integer.parseInt(param)));
		
		} catch (NumberFormatException e) {
			throw new MVException(0);
		}
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
