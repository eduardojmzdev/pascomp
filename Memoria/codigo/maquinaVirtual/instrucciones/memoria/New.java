package maquinaVirtual.instrucciones.memoria;


import maquinaVirtual.MaquinaVirtual;
import maquinaVirtual.instrucciones.Instruccion;
import excepciones.MVException;

public class New implements Instruccion {

	private String param;
	
	/**
	 * Ejecuta la instruccion
	 * @throws MVException. Si hay un error en la ejecucion
	 */
	public void ejecutar() throws MVException {
		try{
			int tope=Integer.parseInt(param);
			int ocupacion=MaquinaVirtual.obtenerInstancia().getMemoriaDatos().size();
			for (int i=ocupacion;i<ocupacion+tope;i++){
				MaquinaVirtual.obtenerInstancia().getMemoriaDatos().put(i,"null");
			}
			MaquinaVirtual.obtenerInstancia().getPila().push((ocupacion)+"");
		
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
