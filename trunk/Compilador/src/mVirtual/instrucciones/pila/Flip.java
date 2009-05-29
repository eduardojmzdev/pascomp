package mVirtual.instrucciones.pila;


import java.util.EmptyStackException;

import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.Instruccion;
import excepciones.MVException;

public class Flip implements Instruccion {

	/**
	 * Ejecuta la instruccion
	 * @throws MVException. Si hay un error en la ejecucion
	 */
	public void ejecutar() throws MVException {
		try{
			String temp=MaquinaVirtual.obtenerInstancia().getPila().pop();
			String temp2=MaquinaVirtual.obtenerInstancia().getPila().pop();
			MaquinaVirtual.obtenerInstancia().getPila().push(temp);
			MaquinaVirtual.obtenerInstancia().getPila().push(temp2);
		
		}catch (EmptyStackException e) {
			throw new MVException(3);
		}

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
