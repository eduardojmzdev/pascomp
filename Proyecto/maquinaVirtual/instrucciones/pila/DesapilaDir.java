package maquinaVirtual.instrucciones.pila;


import java.util.EmptyStackException;

import maquinaVirtual.MaquinaVirtual;
import maquinaVirtual.instrucciones.Instruccion;
import excepciones.MVException;

public class DesapilaDir implements Instruccion {

	private String param;
	
	/**
	 * Ejecuta la instruccion
	 * @throws MVException. Si hay un error en la ejecucion
	 */
	public void ejecutar() throws MVException {
		try{
			MaquinaVirtual.obtenerInstancia().getMemoriaDatos().put(Integer.parseInt(param), MaquinaVirtual.obtenerInstancia().getPila().pop());
		
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
