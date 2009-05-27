package mVirtual.instrucciones.memoria;


import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.Instruccion;
import mVirtual.excepciones.MVException;
/**
 * 
 * Esta clase implementa la ejecuci�n en la pila de la instrucci�n desapila  de la m�quina virtual
 *
 */
public class InstruccionCargaCP implements Instruccion {

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
	/* (non-Javadoc)
	 * @see maquinaVirtual.repertorio.Instruccion#Ejecutar(java.util.Stack, java.util.Hashtable)
	 */
	public void Ejecutar() throws MVException {
		
		MaquinaVirtual.obtenerInstancia().getMemoriaDatos().put(0,MaquinaVirtual.obtenerInstancia().getMemoriaDatos().size()-1+"");

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
