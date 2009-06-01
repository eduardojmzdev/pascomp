package traductor.memoria;

/**
 * Clase para la Memoria del analizador
 * 
 * Implementa el patron Singleton
 *
 */
public abstract class TMemoria {
	private static TMemoria instancia;
	public abstract Object damePosicion(int pos);
	public abstract void almacenaElemento(int pos,Object elem);
	public abstract int dameTam();
	
	/**
	 * Devuelve una instancia de la memoria
	 * @return
	 */
	public static TMemoria getInstance(){
		if (instancia==null)
			instancia=new TMemoriaImp();			
		return instancia;									
	}
	
	/**
	 * Resetea la memoria
	 */
	public static void reset(){
		instancia = null;
	}
}
