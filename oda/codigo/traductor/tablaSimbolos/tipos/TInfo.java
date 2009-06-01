package traductor.tablaSimbolos.tipos;

import traductor.tablaSimbolos.TElemento;

/**
 * Informacion de los tipos
 *
 */
public class TInfo {
	private TElemento clase; 
	private int direccion;
	private int nivel;			//indica el nivel en los procedimientos

	private PropTipos prop;

	public PropTipos getProp() {
		return prop;
	}
	
	public void setProp(PropTipos prop) {
		this.prop = prop;
	}
	public TInfo(){}	
	
	public TInfo(TElemento el, int dir,PropTipos p){
		clase=el;
		direccion=dir;				
		prop=p;		
	}
	
	public TElemento dameElemento(){return clase;}
	
	public int dameDir(){return direccion;}
	
	//Getters y setters
	
	public TElemento getClase() {
		return clase;
	}

	public void setClase(TElemento clase) {
		this.clase = clase;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}
}
