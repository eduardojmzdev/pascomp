package traductor.tablaSimbolos;

import traductor.tablaSimbolos.tipos.PropTipos;
import traductor.tablaSimbolos.tipos.PropTiposPro;
import traductor.tablaSimbolos.tipos.TInfo;



/**
 * Clase de la Tabla de Simbolos
 * 
 * Implementa el patron Singleton
 *
 */
public abstract class TablaSimbolos {
	
	private static TablaSimbolos instancia;
	
	public abstract boolean existeTipo(String id,int nivel);
	public abstract void añadeVariable(String id,int dir,PropTipos p,int nivel)throws Exception;
	public abstract TInfo obtenerInfo (String nombre,int nivel)throws Exception;	
	public abstract boolean existeVariable(String id,int nivel);
	public abstract void añadeTipo(String id,PropTipos p,int nivel)throws Exception;	
	public abstract PropTipos dameTipo(String id,int nivel)throws Exception;;
	public abstract void añadeIdenProgram(String id,int nivel,PropTiposPro p)throws Exception;
	public abstract void liberarNivel(int nivel);
	public abstract void crearNivel(int nivel);
	public abstract boolean existeVariableNivel(String id,int nivel);
	public abstract boolean existeConstante(String id, int nivel);
	public abstract boolean existeConstanteNivel(String id,int nivel);
	public abstract void añadeConstante(String id,int dir,PropTipos p,int nivel)throws Exception;
	public abstract void añadeProcedimiento(String id, int nivel,PropTiposPro p,int dir) throws Exception;
	public abstract PropTipos dameTipoNivel(String id, int nivel);
	public abstract boolean existeProcedimiento(String id,int nivel);
	public abstract boolean existeTipoNivel(String id, int nivel);
	public abstract boolean existeProcedimientoNivel(String id, int nivel);
	public abstract boolean ambitoConstante(String id,int nivel) throws Exception;
	public abstract int dameNivel(String id, int nivel);
	public static TablaSimbolos getInstance(){
		
		
		if (instancia==null){
			instancia=new TablaSimbolosImp();			
		}
		return instancia;
		
		
	}	
	public static void reset(){
		instancia = null;
	}
	
}
