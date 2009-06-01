package maquinaVirtual.instrucciones.aritmeticas;


import java.util.EmptyStackException;

import maquinaVirtual.MaquinaVirtual;
import maquinaVirtual.instrucciones.Instruccion;
import excepciones.MVException;

public class Suma implements Instruccion {

	/**
	 * Ejecuta la instruccion
	 * @throws MVException. Si hay un error en la ejecucion
	 */
	public void ejecutar() throws MVException {

		try {
			int a, b;
			String bString = MaquinaVirtual.obtenerInstancia().getPila().pop();
			if(bString.equals("null"))
				throw new MVException(4);
			
			b = Integer.parseInt(bString);
			String aString = MaquinaVirtual.obtenerInstancia().getPila().pop();
			if(aString.equals("null"))
				throw new MVException(4);
			
			a = Integer.parseInt(aString);
			int c = a + b;
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
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
	/**
	 * vacio
	 */
	public void setParam(String param) {}

}
