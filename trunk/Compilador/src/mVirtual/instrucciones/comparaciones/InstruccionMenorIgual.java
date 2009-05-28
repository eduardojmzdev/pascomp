package mVirtual.instrucciones.comparaciones;


import java.util.EmptyStackException;

import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.Instruccion;
import excepciones.MVException;

/**
 * 
 * Esta clase implementa la ejecución en la pila de la instrucción menor o igual de la máquina virtual
 *
 */
public class InstruccionMenorIgual implements Instruccion {

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
		try {
			int a, b;
			String bString = MaquinaVirtual.obtenerInstancia().getPila().pop();
			if(bString.equals("null"))
				throw new MVException(31);
			b = Integer.parseInt(bString);
			
			String aString = MaquinaVirtual.obtenerInstancia().getPila().pop();
			if(aString.equals("null"))
				throw new MVException(31);
			a = Integer.parseInt(aString);
			boolean c = a <= b;
			if (c)
				MaquinaVirtual.obtenerInstancia().getPila().push(new String("TRUE"));
			else
				MaquinaVirtual.obtenerInstancia().getPila().push(new String("FALSE"));
		
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
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see maquinaVirtual.repertorio.Instruccion#setDatos(java.lang.String)
	 */
	public void setDatos(String datos) {
		// TODO Auto-generated method stub
		
	}

}
