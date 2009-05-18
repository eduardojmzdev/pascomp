package comunicacion.transfers;

public interface Transfer {

	public abstract String getTexto();

	public abstract boolean getError();
	
	public abstract void setTexto(String texto);

	public abstract void setError(boolean error);
	
}