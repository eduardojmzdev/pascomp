package tablaSimbolos;

import java.util.ArrayList;


public class PropTiposPro extends PropTipos{

	private ArrayList<Parametro> parametros;
	
	public PropTiposPro(ArrayList<Parametro> parametros) {
		super(TTipo.PROCEDIMIENTO, 1);
		this.parametros= parametros;
	}

	public ArrayList<Parametro> getParametros() {
		return parametros;
	}
}
