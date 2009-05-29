package traductor.tablaSimbolos.tipos;

public class PropTiposNombrado extends PropTipos{

	private PropTipos ref;
	
	public PropTiposNombrado(PropTipos ref) {
		super(TTipo.CONSTRUIDO, ref.getTam());
		this.ref=ref;		
	}

	public PropTipos getRef() {
		return ref;
	}

	public void setRef(PropTipos ref) {
		this.ref = ref;
	}	
	
}
