package mVirtual.instrucciones.pila;


import mVirtual.instrucciones.Instruccion;
import excepciones.MVException;

public class InstruccionBurbuja implements Instruccion {

	private String param;
	
	/**
	 * Ejecuta la instruccion
	 * @throws MVException. Si hay un error en la ejecucion
	 */
	public void ejecutar() throws MVException {
		//try{
		//	MaquinaVirtual.obtenerInstancia().getPila().pop();
		//}catch (Exception e){
		//	throw new ExcepcionEnEjecucion("Error de acceso a memoria");
		//} 
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
