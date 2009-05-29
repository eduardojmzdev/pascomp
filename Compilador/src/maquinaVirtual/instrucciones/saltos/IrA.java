package maquinaVirtual.instrucciones.saltos;


import maquinaVirtual.MaquinaVirtual;
import maquinaVirtual.instrucciones.Instruccion;
import excepciones.MVException;

public class IrA implements Instruccion {

	private String param;

	/**
	 * Ejecuta la instruccion
	 * @throws MVException. Si hay un error en la ejecucion
	 */
	public void ejecutar()throws MVException {
		try {
			MaquinaVirtual.obtenerInstancia().setContadorPrograma(Integer.parseInt(param)-1);
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
