package mVirtual.instrucciones.memoria;


import java.util.EmptyStackException;

import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.Instruccion;
import mVirtual.excepciones.MVException;

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
	public void Ejecutar() throws MVException {
		try{
			String cima = MaquinaVirtual.obtenerInstancia().getPila().pop();
			if(cima.equals("null"))
				throw new MVException(31);
			int pos = Integer.parseInt(cima);
			
			for(int i=pos; i<pos + Integer.parseInt(datos); i++){
				MaquinaVirtual.obtenerInstancia().getMemoriaDatos().remove(i);	
			}
		}catch (EmptyStackException e) {
			throw new MVException(30);

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
