package mVirtual.instrucciones;


public abstract class GeneradorInstr {
	
	
	private static GeneradorInstr instancia;
	
	/**
	 * Crea una instancia de GeneradorInstrImp o devuelve 
	 * una ya existente
	 * @return MaquinaVirtual: instancia
	 */
	public static GeneradorInstr obtenerInstancia(){
		if (instancia==null){
			instancia= new GeneradorInstrImp();
			return instancia;
			}
		else
			return instancia;
	}
	
	/**
	 * Genera una instruccion 
	 * @param nombre String: nombre de la instruccion a generar
	 * @return Instruccion
	 */
	public Instruccion generaInstr(String nombre){
		return null;
	}
	
	
	/**
	 * libera la instancia de la clase
	 */
	public static void reset(){
		instancia = null;
	}
}
