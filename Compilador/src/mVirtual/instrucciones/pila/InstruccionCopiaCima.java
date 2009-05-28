package mVirtual.instrucciones.pila;


import java.util.EmptyStackException;

import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.Instruccion;
import excepciones.MVException;
/**
 * 
 * Esta clase implementa la ejecución en la pila de la instrucción CopiaCima  de la máquina virtual
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
	public void Ejecutar() throws MVException {
		try{
			String temp=MaquinaVirtual.obtenerInstancia().getPila().pop();
			MaquinaVirtual.obtenerInstancia().getPila().push(temp);
			MaquinaVirtual.obtenerInstancia().getPila().push(temp);
		
		}catch (EmptyStackException e) {
			throw new MVException(30);

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
