package traductor.tablaSimbolos.tipos;

/**
 * Clase para implementar los tipos de datos
 *
 */
public class PropTipos {
	protected TTipo nombreTipo;
	protected int tam;
	
	public TTipo getNombreTipo() {
		return nombreTipo;
	}
	public void setNombreTipo(TTipo nombreTipo) {
		this.nombreTipo = nombreTipo;
	}
	public int getTam() {
		return tam;
	}
	public void setTam(int tam) {
		this.tam = tam;
	}
	
	public PropTipos(TTipo nombreTipo, int tam) {
		this.nombreTipo = nombreTipo;
		this.tam = tam;
	
	}
	
}
