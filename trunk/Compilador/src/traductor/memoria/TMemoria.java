package traductor.memoria;

public abstract class TMemoria {
	private static TMemoria instancia;
	public abstract Object damePosicion(int pos);
	public abstract void almacenaElemento(int pos,Object elem);
	public abstract int dameTam();
	public static TMemoria getInstance(){
		if (instancia==null)
			instancia=new TMemoriaImp();			
		return instancia;									
	}
	public static void reset(){
		instancia = null;
	}
}
