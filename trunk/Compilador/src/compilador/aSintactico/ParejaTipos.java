package compilador.aSintactico;
import compilador.tablaSimbolos.PropTipos;
import compilador.tablaSimbolos.TInfo;
public class ParejaTipos {
	
	private PropTipos e1; 
	private PropTipos e2;
	private int visitas;
	
	
	
	public ParejaTipos(PropTipos e1, PropTipos e2) {
		super();
		this.e1 = e1;
		this.e2 = e2;
		this.visitas = 0;
	}
	public PropTipos getE1() {
		return e1;
	}
	public void setE1(PropTipos e1) {
		this.e1 = e1;
	}
	public PropTipos getE2() {
		return e2;
	}
	public void setE2(PropTipos e2) {
		this.e2 = e2;
	}
	public int getVisitas() {
		return visitas;
	}
	public void setVisitas(int visitas) {
		this.visitas = visitas;
	}
}
