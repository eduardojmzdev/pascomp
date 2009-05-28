package mVirtual.instrucciones.pila;


import mVirtual.instrucciones.Instruccion;
import excepciones.MVException;

/**
 * 
 * Esta clase implementa la ejecución en la pila de la instrucción Burbuja de la máquina virtual
 *
 */
public class InstruccionBurbuja implements Instruccion {

	/**
	 * Datos que se deben procesar
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
		//try{
		//	MaquinaVirtual.obtenerInstancia().getPila().pop();
		//}catch (Exception e){
		//	throw new ExcepcionEnEjecucion("Error de acceso a memoria");
		//} 
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
