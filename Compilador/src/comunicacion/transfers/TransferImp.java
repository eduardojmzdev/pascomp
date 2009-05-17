package comunicacion.transfers;

public class TransferImp implements Transfer {
	
	//La cadena de caracteres a tratar
	private String texto;
		
	private boolean error;

	public boolean getError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}
		
	
	}
