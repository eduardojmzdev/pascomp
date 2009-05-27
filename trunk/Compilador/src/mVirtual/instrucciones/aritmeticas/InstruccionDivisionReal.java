package mVirtual.instrucciones.aritmeticas;


import java.util.EmptyStackException;

import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.Instruccion;
import mVirtual.excepciones.MVException;

/**
 * 
 * Esta clase implementa la ejecución en la pila de la instrucción división real de la máquina virtual
 *
 */
public class InstruccionDivisionReal implements Instruccion {

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
			if(b==0) 
				throw new MVException(1);
			String aString = MaquinaVirtual.obtenerInstancia().getPila().pop();
			if(aString.equals("null"))
				throw new MVException(31);
			
			a = Integer.parseInt(aString);
			int c = a / b;
			MaquinaVirtual.obtenerInstancia().getPila().push(new String(String.valueOf(c)));
		
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
