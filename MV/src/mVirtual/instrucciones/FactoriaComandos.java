package mVirtual.instrucciones;


/**
 * Clase abstracta que modeliza una factoria de comandos
 * 
 *
 */
public abstract class FactoriaComandos {
	
	/**
	 * La factoria
	 */
	private static FactoriaComandos instancia;
	
	/**
	 * Modelizador de objectos singleton
	 * @return La unica instancia de clase
	 */
	public static FactoriaComandos obtenerInstancia(){
		if (instancia==null){
			instancia= new FactoriaComandosImp();
			return instancia;
			}
		else
			return instancia;
	}
	
	/**
	 * Genera una instruccion del codigo máquina acorde a la peticion recibida
	 * @param comando El comando a generar
	 * @return El comando generado
	 */
	public Instruccion generarComando(String comando){
		return null;
	};
}
