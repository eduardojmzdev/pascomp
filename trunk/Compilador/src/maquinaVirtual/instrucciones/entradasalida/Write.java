package maquinaVirtual.instrucciones.entradasalida;


import maquinaVirtual.MaquinaVirtual;
import maquinaVirtual.instrucciones.Instruccion;

import excepciones.MVException;

public class Write implements Instruccion {

	/**
	 * Ejecuta la instruccion
	 * @throws MVException. Si hay un error en la ejecucion
	 */
	public void ejecutar() throws MVException {
		System.out.println("Salida usuario:  " + MaquinaVirtual.obtenerInstancia().getPila().pop());
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
