package mVirtual.instrucciones.entradasalida;


import java.util.Scanner;

import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.Instruccion;

import excepciones.MVException;
/**
 * 
 * Esta clase implementa la ejecución en la pila de la instrucción read de la máquina virtual
 *
 */
public class InstruccionRead implements Instruccion {

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
		System.out.println("Esperando escritura de usuario por consola:\n");
		Scanner lector = new Scanner(System.in);
		String cadenaLeida="";
		try {
			cadenaLeida=lector.next();
		} catch (Exception e) {

		}
		MaquinaVirtual.obtenerInstancia().getPila().push(cadenaLeida);
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
