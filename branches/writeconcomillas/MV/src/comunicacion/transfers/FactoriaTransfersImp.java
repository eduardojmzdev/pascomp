package comunicacion.transfers;
/**
 * Implementa los métodos de la clase abstracta FactoríaTransfer
 *
 */
public class FactoriaTransfersImp extends FactoriaTransfers {
	/* (non-Javadoc)
	 * @see comunicacion.transfers.FactoriaTransfers#generarTransfer()
	 */
	public Transfer generarTransfer(){
			return new TransferImp();
	};
}
