package mVirtual.instrucciones.saltos;


import java.util.EmptyStackException;

import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.Instruccion;
import excepciones.MVException;

public class InstruccionIrIndice implements Instruccion {

	/**
	 * Ejecuta la instruccion
	 * @throws MVException. Si hay un error en la ejecucion
	 */
	public void ejecutar() throws MVException {
		try {
			String cima = MaquinaVirtual.obtenerInstancia().getPila().pop();
			if(cima.equals("null"))
				throw new MVException(4);
			MaquinaVirtual.obtenerInstancia().setContadorPrograma(Integer.parseInt(cima)-1);
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
