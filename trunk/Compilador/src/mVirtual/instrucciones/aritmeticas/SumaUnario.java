package mVirtual.instrucciones.aritmeticas;


import java.util.EmptyStackException;

import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.Instruccion;
import excepciones.MVException;

public class SumaUnario implements Instruccion {

	
	/**
	 * Ejecuta la instruccion
	 * @throws MVException. Si hay un error en la ejecucion
	 */
	public void ejecutar() throws MVException {

		try {
			int a;
			String aString = MaquinaVirtual.obtenerInstancia().getPila().pop();
			if(aString.equals("null"))
				throw new MVException(4);
			
			a = Integer.parseInt(aString);
			int c = a;
			MaquinaVirtual.obtenerInstancia().getPila().push(new String(String.valueOf(c)));

		}catch (EmptyStackException e) {
			throw new MVException(3);

		} catch (NumberFormatException e) {
			throw new MVException(0);

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
