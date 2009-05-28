package mVirtual.instrucciones;


/**
 * Clase abstracta que genera comandos
 * 
 *
 */
public abstract class FactoriaComandos {
	
	/**
	 * Factoria
	 */
	private static FactoriaComandos instancia;
	
	/**
	 * Modelizador de objectos singleton
	 * @return La instancia de clase
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
	 * Genera una instruccion del codigo máquina 
	 * @param comando Comando a generar
	 * @return Comando generado
	 */
	public Instruccion generarComando(String comando){
		return null;
	};
	
	public static void reset(){
		instancia = null;
	}
}
