package comunicacion;

import comunicacion.transfers.Transfer;


public interface Vista {

	public void ejecutar(String[] args) throws Exception;	
	
	public void actualizar (Transfer t);
}
