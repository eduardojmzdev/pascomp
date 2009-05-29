package traductor.tablaSimbolos;

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
}
