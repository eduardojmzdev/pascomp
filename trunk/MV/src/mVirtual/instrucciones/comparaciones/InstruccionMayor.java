package mVirtual.instrucciones.comparaciones;


import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.ExcepcionEnEjecucion;
import mVirtual.instrucciones.Instruccion;

/**
 * 
 * Esta clase implementa la ejecución en la pila de la instrucción mayor de la máquina virtual
 *
 */
public class InstruccionMayor implements Instruccion {

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
		// TODO Auto-generated method stub
		if (MaquinaVirtual.obtenerInstancia().getPila().size() >= 2) {
			try {
				String bString = MaquinaVirtual.obtenerInstancia().getPila().pop();
				int a, b;
				b = Integer.parseInt(bString);
				String aString = MaquinaVirtual.obtenerInstancia().getPila().pop();
				a = Integer.parseInt(aString);
				boolean c = a > b;
				if (c)
					MaquinaVirtual.obtenerInstancia().getPila().push(new String("TRUE"));
				else
					MaquinaVirtual.obtenerInstancia().getPila().push(new String("FALSE"));
			} catch (Exception e) {
				throw new ExcepcionEnEjecucion("Tipos erroneos");
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
