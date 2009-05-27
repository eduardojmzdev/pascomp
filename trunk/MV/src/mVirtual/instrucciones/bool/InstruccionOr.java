package mVirtual.instrucciones.bool;


import java.util.EmptyStackException;

import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.Instruccion;
import excepciones.MVException;

/**
 * 
 * Esta clase implementa la ejecución en la pila de la instrucción or de la máquina virtual
 *
 */
public class InstruccionOr implements Instruccion {

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
			boolean a, b;

			String bString = MaquinaVirtual.obtenerInstancia().getPila().pop();
			if (bString.equals("TRUE"))
				b = true;
			else if (bString.equals("FALSE"))
				b = false;
			else
				throw new MVException(31);

			String aString = MaquinaVirtual.obtenerInstancia().getPila().pop();
			if (aString.equals("TRUE"))
				a = true;
			else if (aString.equals("FALSE"))
				a = false;
			else
				throw new MVException(31);

			boolean c = a || b;
			if (c) {
				MaquinaVirtual.obtenerInstancia().getPila().push(new String("TRUE"));
			} else {
				MaquinaVirtual.obtenerInstancia().getPila().push(new String("FALSE"));
			}
		
		}catch (EmptyStackException e) {
			throw new MVException(30);

		} catch (MVException e) {
			throw e;
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
