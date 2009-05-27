package traductor.tablaSimbolos;

public class Parametro {

	private Modo modo;
	private String nombre;
	private PropTipos tipo;
	
	public Parametro(Modo modo, PropTipos tipo,String nombre) {
		this.modo = modo;
		this.tipo = tipo;
		this.nombre=nombre;
	}
	public Modo getModo() {
		return modo;
	}
	public void setModo(Modo modo) {
		this.modo = modo;
	}
	public PropTipos getTipo() {
		return tipo;
	}
	public void setTipo(PropTipos tipo) {
		this.tipo = tipo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
