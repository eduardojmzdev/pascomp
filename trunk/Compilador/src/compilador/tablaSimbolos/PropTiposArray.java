package compilador.tablaSimbolos;

import java.util.ArrayList;
public class PropTiposArray extends PropTipos {
	

	private int tam;
	
	private PropTipos ref; //tipo al que hacemos referencia (tipo base del array)
	
	public PropTiposArray(int tam, PropTipos ref) {
		super(TTipo.ARRAY, 1);//habria que poner el tamaño bien
		this.tam=tam;
		this.ref=ref;
			
	}
	
	public int getTam() {
		return tam;
	}
	public void setTam(int t){
		
		tam=t;
	}
	
	public PropTipos getRef() {
		return ref;
	}
	public void setRef(PropTipos ref) {
		this.ref = ref;
	}

}
