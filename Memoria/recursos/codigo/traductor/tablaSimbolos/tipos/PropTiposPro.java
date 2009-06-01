package traductor.tablaSimbolos.tipos;

import java.util.ArrayList;

import traductor.tablaSimbolos.Parametro;


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
