package maquinaVirtual.instrucciones.pila;


import maquinaVirtual.instrucciones.Instruccion;
import excepciones.MVException;

public class Burbuja implements Instruccion {

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
	@Override
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
