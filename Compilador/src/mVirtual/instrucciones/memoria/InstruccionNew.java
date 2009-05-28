package mVirtual.instrucciones.memoria;


import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.Instruccion;
import excepciones.MVException;

/**
 * 
 * Esta clase implementa la ejecución en la pila de la instrucción New de la máquina virtual
 *
 */
public class InstruccionNew implements Instruccion {

	/**
	 * Los datos con los cuales debe trabajar
	 */
	private String datos;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.getClass().getSimpleName()+"("+datos+")";
	}
	
	/* (non-Javadoc)
	 * @see maquinaVirtual.repertorio.Instruccion#Ejecutar(java.util.Stack, java.util.Hashtable)
	 */
	public void Ejecutar() throws MVException {
		try{
			int tope=Integer.parseInt(datos);
			int ocupacion=MaquinaVirtual.obtenerInstancia().getMemoriaDatos().size();
			for (int i=ocupacion;i<ocupacion+tope;i++){
				MaquinaVirtual.obtenerInstancia().getMemoriaDatos().put(i,"null");
			}
			MaquinaVirtual.obtenerInstancia().getPila().push((ocupacion)+"");
		
		} catch (NumberFormatException e) {
			throw new MVException(0);

		}
	}

	/* (non-Javadoc)
	 * @see maquinaVirtual.repertorio.Instruccion#getDatos()
	 */
	public String getDatos() {
		return datos;
	}

	/* (non-Javadoc)
	 * @see maquinaVirtual.repertorio.Instruccion#setDatos(java.lang.String)
	 */
	public void setDatos(String datos) {
		this.datos=datos;		
	}

}
