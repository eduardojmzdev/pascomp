package mVirtual.instrucciones.bool;


import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.ExcepcionEnEjecucion;
import mVirtual.instrucciones.Instruccion;

/**
 * 
 * Esta clase implementa la ejecución en la pila de la instrucción and de la máquina virtual
 *
 */
public class InstruccionAnd implements Instruccion {

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
	/* (non-Javadoc)
	 * @see maquinaVirtual.repertorio.Instruccion#Ejecutar(java.util.Stack, java.util.Hashtable)
	 */
	public void Ejecutar()
			throws ExcepcionEnEjecucion {
		// TODO Auto-generated method stub
		if (MaquinaVirtual.obtenerInstancia().getPila().size() >= 2) {
			String bString = MaquinaVirtual.obtenerInstancia().getPila().pop();
			boolean a, b;
			b = (bString.equals("TRUE"));
			String aString = MaquinaVirtual.obtenerInstancia().getPila().pop();
			a = (aString.equals("TRUE"));
			boolean c = a && b;
			if (c) {
				MaquinaVirtual.obtenerInstancia().getPila().push(new String("TRUE"));
			} else {
				MaquinaVirtual.obtenerInstancia().getPila().push(new String("FALSE"));

			}
		} else
			throw new ExcepcionEnEjecucion(
					"No se han encontrado operandos validos");
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