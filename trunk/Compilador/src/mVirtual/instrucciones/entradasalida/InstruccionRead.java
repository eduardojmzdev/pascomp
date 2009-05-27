package mVirtual.instrucciones.entradasalida;


import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.Instruccion;

import mVirtual.comunicacion.Controlador;
import mVirtual.comunicacion.transfers.FactoriaTransfers;
import mVirtual.comunicacion.transfers.Transfer;

import mVirtual.excepciones.MVException;
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
		Transfer t =FactoriaTransfers.obtenerInstancia().generarTransfer();
		t.setComunicacionInterna(true);
		Controlador.obtenerInstancia().actualizarVistas(t);
		MaquinaVirtual.obtenerInstancia().getPila().push(t.getTexto().get(0));
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
