package mVirtual.comunicacion.transfers;


/**
 * Modelizacion de un Factoria Abstracta Singleton creadora de transfers
 * 
 *
 */
public abstract class FactoriaTransfers {
	
	private static FactoriaTransfers instancia;
	/**
	 * Devuelve la instancia de la factoría Transfer actual
	 * @return instancia de factoría
	 */
	public static FactoriaTransfers obtenerInstancia(){
		if (instancia==null){
			instancia= new FactoriaTransfersImp();
			return instancia;
			}
		else
			return instancia;
	}
	/**
	 * Devuelve una instancia de un transfer, bajo demanda
	 * @return transfer por defecto
	 */
	public Transfer generarTransfer(){
		return null;
	};
	
	public static void reset(){
		instancia = null;
	}
}
