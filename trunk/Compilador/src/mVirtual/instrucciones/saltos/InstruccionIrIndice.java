package mVirtual.instrucciones.saltos;


import java.util.EmptyStackException;

import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.Instruccion;
import excepciones.MVException;

public class InstruccionIrIndice implements Instruccion {

	private String datos;

	public void Ejecutar() throws MVException {
		try {
			String cima = MaquinaVirtual.obtenerInstancia().getPila().pop();
			if(cima.equals("null"))
				throw new MVException(4);
			MaquinaVirtual.obtenerInstancia().setContadorPrograma(Integer.parseInt(cima)-1);
		}catch (EmptyStackException e) {
			throw new MVException(3);

		} catch (NumberFormatException e) {
			throw new MVException(0);
		}	
	}

	public String getDatos() {
		return datos;
	}

	public void setDatos(String datos) {
	this.datos=datos;

	}
	
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
