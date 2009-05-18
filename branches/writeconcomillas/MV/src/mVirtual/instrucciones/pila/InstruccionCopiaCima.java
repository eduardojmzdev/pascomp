package mVirtual.instrucciones.pila;


import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.ExcepcionEnEjecucion;
import mVirtual.instrucciones.Instruccion;
/**
 * 
 * Esta clase implementa la ejecución en la pila de la instrucción desapila  de la máquina virtual
 *
 */
public class InstruccionCopiaCima implements Instruccion {

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
	/* (non-Javadoc)
	 * @see maquinaVirtual.repertorio.Instruccion#Ejecutar(java.util.Stack, java.util.Hashtable)
	 */
	public void Ejecutar() throws ExcepcionEnEjecucion {
		try{
			String temp=MaquinaVirtual.obtenerInstancia().getPila().pop();
			MaquinaVirtual.obtenerInstancia().getPila().push(temp);
			MaquinaVirtual.obtenerInstancia().getPila().push(temp);
		}catch (Exception e){
			throw new ExcepcionEnEjecucion("La pila de ejecucion esta vacia");
		} 

	}

	/* (non-Javadoc)
	 * @see maquinaVirtual.repertorio.Instruccion#getDatos()
	 */
	public String getDatos() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDatos(String datos) {
		// TODO Auto-generated method stub
		
	}

}
