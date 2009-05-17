package comunicacion.transfers;

import java.util.Vector;

/**
 * Clase implementadora del transfer usado en el sistema
 * 
 *
 */
public class TransferImp implements Transfer {
	/**
	 * Contenedor de los diversos mensajes de texto que se deben mostrar 
	 */
	private Vector<String> texto;
	
	/**
	 * Ejecutar la maquina en modo traza 
	 */
	private boolean modoTraza;

	/**
	 * Cierto si hay que cargar el fichero
	 */
	private boolean ruta;
	
	/**
	 * Cierto si hay una comunicacion desde la maquina virtual debido al programa de usuario 
	 */
	private boolean comunicacion;
	
	/**
	 * Informa de un mensaje solo compuesto por el nombre de una instruccion 
	 */
	private boolean instruccion;
	
	/**
	 * Contstructora por defecto 
	 */
	public TransferImp(){
		this.modoTraza=false;
		this.texto=new Vector<String>();
	}
	
	/* (non-Javadoc)
	 * @see comunicacion.Transfer#getTexto()
	 */
	public Vector<String> getTexto(){
		return texto;
		}
	
	/* (non-Javadoc)
	 * @see comunicacion.Transfer#getTraza()
	 */
	public boolean getTraza(){
		return modoTraza;
		}
		
	/* (non-Javadoc)
	 * @see comunicacion.Transfer#setTexto(java.lang.String, int)
	 */
	public void setTexto(String texto,int linea){
		 this.texto.add(linea,texto);
		}
		
	/* (non-Javadoc)
	 * @see comunicacion.Transfer#setModoTraza(boolean)
	 */
	public void setModoTraza(boolean modoTraza){
		this.modoTraza=modoTraza;
		}

	/* (non-Javadoc)
	 * @see comunicacion.transfers.Transfer#getRuta()
	 */
	public boolean getRuta() {
		return ruta;
	}

	/* (non-Javadoc)
	 * @see comunicacion.transfers.Transfer#setRuta(boolean)
	 */
	public void setRuta(boolean ruta) {
		this.ruta=ruta;
		
	}

	/* (non-Javadoc)
	 * @see comunicacion.transfers.Transfer#getComunicacionInterna()
	 */
	public boolean getComunicacionInterna() {
		return comunicacion;
	}

	/* (non-Javadoc)
	 * @see comunicacion.transfers.Transfer#setComunicacionInterna(boolean)
	 */
	public void setComunicacionInterna(boolean comunicacion) {
		this.comunicacion=comunicacion;
		
	}

	/* (non-Javadoc)
	 * @see comunicacion.transfers.Transfer#getSoloInstruccion()
	 */
	public boolean getSoloInstruccion() {
		return instruccion;
	}

	/* (non-Javadoc)
	 * @see comunicacion.transfers.Transfer#setSoloInstruccion(boolean)
	 */
	public void setSoloInstruccion(boolean instruccion) {
		this.instruccion=instruccion;		
	}
	}
