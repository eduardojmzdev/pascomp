package mVirtual.instrucciones.pila;


import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.ExcepcionEnEjecucion;
import mVirtual.instrucciones.Instruccion;

/**
 * 
 * Esta clase implementa la ejecución en la pila de la instrucción desapiladir de la máquina virtual
 *
 */
public class InstruccionDesapilaDireccion implements Instruccion {

	/**
	 * Los datos con los que debe trabajar la instruccion
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
	public void Ejecutar() throws ExcepcionEnEjecucion {
		try{
			MaquinaVirtual.obtenerInstancia().getMemoriaDatos().put(Integer.parseInt(datos), MaquinaVirtual.obtenerInstancia().getPila().pop());
			}catch (Exception e){
				throw new ExcepcionEnEjecucion("Error de acceso a memoria (Direccion invalida)");
			} 

	}

	/* (non-Javadoc)
	 * @see maquinaVirtual.repertorio.Instruccion#getDatos()
	 */
	public String getDatos() {
		return datos;
	}

	public void setDatos(String datos) {
		this.datos=datos;
	}

}
