package mVirtual.instrucciones.saltos;


import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.ExcepcionEnEjecucion;
import mVirtual.instrucciones.Instruccion;

public class InstruccionIrIndice implements Instruccion {

	private String datos;

	public void Ejecutar()
			throws ExcepcionEnEjecucion {
		MaquinaVirtual.obtenerInstancia().setContadorPrograma(Integer.parseInt(MaquinaVirtual.obtenerInstancia().getPila().pop())-1);

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
