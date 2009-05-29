package mVirtual.instrucciones.memoria;


import java.util.EmptyStackException;

import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.Instruccion;
import excepciones.MVException;

public class Dispose implements Instruccion {

	private String param;
	
	/**
	 * Ejecuta la instruccion
	 * @throws MVException. Si hay un error en la ejecucion
	 */
	public void ejecutar() throws MVException {
		try{
			String cima = MaquinaVirtual.obtenerInstancia().getPila().pop();
			if(cima.equals("null"))
				throw new MVException(4);
			int pos = Integer.parseInt(cima);
			
			for(int i=pos; i<pos + Integer.parseInt(param); i++){
				MaquinaVirtual.obtenerInstancia().getMemoriaDatos().remove(i);	
			}
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
