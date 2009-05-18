package comunicacion.transfers;

public class FactoriaTransfersImp extends FactoriaTransfers {
	
	public Transfer generarTransfer(){
			return new TransferImp();
	};
}
