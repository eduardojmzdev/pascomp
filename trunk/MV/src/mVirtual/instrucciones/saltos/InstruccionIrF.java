package mVirtual.instrucciones.saltos;


import java.util.EmptyStackException;

import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.Instruccion;
import mVirtual.excepciones.MVException;

public class InstruccionIrF implements Instruccion {

	private String datos;

	public void Ejecutar() throws MVException {
		try {
			if (MaquinaVirtual.obtenerInstancia().getPila().pop().equals("FALSE"))
				MaquinaVirtual.obtenerInstancia().setContadorPrograma(Integer.parseInt(datos)-1);

		}catch (EmptyStackException e) {
			throw new MVException(30);

		} catch (NumberFormatException e) {
			throw new MVException(21);
		}
	}

	public String getDatos() {
		return datos;
	}

	public void setDatos(String datos) {
	this.datos=datos;

	}
	
	public String toString() {
		return this.getClass().getSimpleName()+"("+datos+")";
	}

}
