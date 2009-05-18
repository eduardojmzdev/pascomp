package mVirtual.instrucciones.saltos;


import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.ExcepcionEnEjecucion;
import mVirtual.instrucciones.Instruccion;

public class InstruccionIrA implements Instruccion {

	private String datos;

	public void Ejecutar()
			throws ExcepcionEnEjecucion {
		MaquinaVirtual.obtenerInstancia().setContadorPrograma(Integer.parseInt(datos)-1);

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
