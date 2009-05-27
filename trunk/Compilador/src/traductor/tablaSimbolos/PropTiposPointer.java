package traductor.tablaSimbolos;

public class PropTiposPointer extends PropTipos {
	
	PropTipos ref; //tipo al que hace referencia el puntero	
	
	public PropTiposPointer(PropTipos ref) {
		super(TTipo.PUNTERO, 1);///el tamaño de los punteros en nuestro sistema es 1
		this.ref=ref;
	}

	public PropTipos getRef() {
		return ref;
	}

	public void setRef(PropTipos ref) {
		this.ref = ref;
	}	
	
}
