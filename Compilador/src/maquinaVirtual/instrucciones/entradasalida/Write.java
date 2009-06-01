package maquinaVirtual.instrucciones.entradasalida;


import java.util.EmptyStackException;

import main.Ventana;
import maquinaVirtual.MaquinaVirtual;
import maquinaVirtual.instrucciones.Instruccion;

import excepciones.MVException;

public class Write implements Instruccion {

	/**
	 * Ejecuta la instruccion
	 * @throws MVException. Si hay un error en la ejecucion
	 */
	public void ejecutar() throws MVException {
		try{
			Ventana.sal += "\nSalida de usuario:   " + MaquinaVirtual.obtenerInstancia().getPila().pop();
		}catch (EmptyStackException e) {
			throw new MVException(3);

		}
	}

	/**
	 * @return String: representa la instruccion
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
	/**
	 * vacio
	 */
	public void setParam(String param) {}

}
