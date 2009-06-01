package maquinaVirtual.instrucciones.entradasalida;


import javax.swing.JOptionPane;

import main.Ventana;
import maquinaVirtual.MaquinaVirtual;
import maquinaVirtual.instrucciones.Instruccion;

import excepciones.MVException;

public class Read implements Instruccion {

	/**
	 * Ejecuta la instruccion
	 * @throws MVException. Si hay un error en la ejecucion
	 */
	public void ejecutar() throws MVException {

		String cadenaLeida =JOptionPane.showInputDialog("Escritura de usuario:");
		Ventana.sal += "\nEntrada de usuario:   " + cadenaLeida;
		MaquinaVirtual.obtenerInstancia().getPila().push(cadenaLeida);
	}

	/**
	 * @return String: representa la instruccion
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
	/**
	 * vacio
	 */
	public void setParam(String param) {}

}
