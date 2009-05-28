package mVirtual.instrucciones.saltos;


import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.Instruccion;
import excepciones.MVException;

public class InstruccionIrA implements Instruccion {

	private String datos;

	public void Ejecutar()throws MVException {
		try {
			MaquinaVirtual.obtenerInstancia().setContadorPrograma(Integer.parseInt(datos)-1);
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
