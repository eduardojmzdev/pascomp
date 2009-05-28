package mVirtual.instrucciones.pila;


import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.Instruccion;
import excepciones.MVException;

/**
 * 
 * Esta clase implementa la ejecución en la pila de la instrucción apiladir de la máquina virtual
 *
 */
public class InstruccionApilaDireccion implements Instruccion {

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
			MaquinaVirtual.obtenerInstancia().getPila().push(MaquinaVirtual.obtenerInstancia().getMemoriaDatos().get(Integer.parseInt(datos)));
		
		} catch (NumberFormatException e) {
			throw new MVException(21);
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
