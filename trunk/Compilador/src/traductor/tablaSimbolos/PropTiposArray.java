package traductor.tablaSimbolos;

import java.util.ArrayList;
public class PropTiposArray extends PropTipos {
	

	private int tam;
	
	private PropTipos ref; //tipo al que hacemos referencia (tipo base del array)
	
	public PropTiposArray(int tam, PropTipos ref) {
	    	//el temaño de memoria para reservar en el array es
	    	//tamaño elemenots* numero de elementos
	    	//como son arrays de enteros y booleanos siempre va a ser tamaño 1
		super(TTipo.ARRAY, tam * ref.getTam());
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
