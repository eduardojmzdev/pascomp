package mVirtual.instrucciones.memoria;


import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.Instruccion;
import excepciones.MVException;

public class InstruccionCargaCP implements Instruccion {

	/**
	 * Ejecuta la instruccion
	 * @throws MVException. Si hay un error en la ejecucion
	 */
	public void ejecutar() throws MVException {
		
		MaquinaVirtual.obtenerInstancia().getMemoriaDatos().put(0,MaquinaVirtual.obtenerInstancia().getMemoriaDatos().size()-1+"");
	}

	/**
	 * @return String: representa la instruccion
	 */
	public String toString() {
		return this.getClass().getSimpleName();
	}
	/**
	 * vacio
	 */
	public void setParam(String param) {}

}
