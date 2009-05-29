package mVirtual.instrucciones.entradasalida;


import java.util.Scanner;

import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.Instruccion;

import excepciones.MVException;

public class Read implements Instruccion {

	/**
	 * Ejecuta la instruccion
	 * @throws MVException. Si hay un error en la ejecucion
	 */
	public void ejecutar() throws MVException {
		System.out.println("Esperando escritura de usuario por consola:\n");
		Scanner lector = new Scanner(System.in);
		String cadenaLeida="";
		try {
			cadenaLeida=lector.next();
		} catch (Exception e) {

		}
		MaquinaVirtual.obtenerInstancia().getPila().push(cadenaLeida);
	}

	/**
	 * @return String: representa la instruccion
	 */
	public String toString() {
		return this.getClass().getSimpleName();
	}
	/**
	 * vacio
	 */
	public void setParam(String param) {}

}
