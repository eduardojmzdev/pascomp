package mVirtual.instrucciones.pila;


import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.ExcepcionEnEjecucion;
import mVirtual.instrucciones.Instruccion;

/**
 * 
 * Esta clase implementa la ejecución en la pila de la instrucción apiladir de la máquina virtual
 *
 */
public class InstruccionDispose implements Instruccion {

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
	public void Ejecutar() throws ExcepcionEnEjecucion {
		try{
			int pos = Integer.parseInt(MaquinaVirtual.obtenerInstancia().getPila().pop());
			for(int i=pos; i<pos + Integer.parseInt(datos); i++){
				MaquinaVirtual.obtenerInstancia().getMemoriaDatos().remove(i);	
			}
		}catch (Exception e){
			throw new ExcepcionEnEjecucion("Error de acceso a memoria");
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
