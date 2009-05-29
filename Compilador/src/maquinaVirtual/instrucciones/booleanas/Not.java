package maquinaVirtual.instrucciones.booleanas;


import java.util.EmptyStackException;

import maquinaVirtual.MaquinaVirtual;
import maquinaVirtual.instrucciones.Instruccion;
import excepciones.MVException;

public class Not implements Instruccion {

	/**
	 * Ejecuta la instruccion
	 * @throws MVException. Si hay un error en la ejecucion
	 */
	public void ejecutar() throws MVException {
		try {
			boolean a;
			String aString = MaquinaVirtual.obtenerInstancia().getPila().pop();
			if (aString.equals("TRUE"))
				a = true;
			else if (aString.equals("FALSE"))
				a = false;
			else if(aString.equals("null"))
				throw new MVException(4);
			else
				throw new MVException(2);

			if (a) {
				MaquinaVirtual.obtenerInstancia().getPila().push(new String("TRUE"));
			} else {
				MaquinaVirtual.obtenerInstancia().getPila().push(new String("FALSE"));
			}
		
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
