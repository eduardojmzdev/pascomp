package comunicacion.transfers;

//Clase singleton que modeliza una factoria de comandos
public abstract class FactoriaTransfers {
	
	private static FactoriaTransfers instancia;
	
	public static FactoriaTransfers obtenerInstancia(){
		if (instancia==null){
			instancia= new FactoriaTransfersImp();
			return instancia;
			}
		else
			return instancia;
	}
	
	public Transfer generarTransfer(){
		return null;
	};
}
