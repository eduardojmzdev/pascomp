package comunicacion;

import comunicacion.transfers.Transfer;
import excepciones.MVException;

/**
 * Clase implementador del controlador (Singleton)
 * 
 *
 */
public class ControladorImp extends Controlador {

	/**
	 * El modelo a controlar 
	 */
	private Modelo modelo;

	
	/**
	 *La vista a controlar 
	 */
	private Vista vista;
	
	/* (non-Javadoc)
	 * @see comunicacion.Controlador#captarTransferencia(comunicacion.transfers.Transfer)
	 */
	public void captarTransferencia(Transfer trans) throws Exception {
		if (trans == null)
			throw new Exception("Problemas de comunicacion en el sistema");
		else{
			if (trans.getRuta()&&!trans.getTraza())
				modelo.ejecutar(trans);
			else
				modelo.ejecutaPasoAPaso(trans);
		}
	}
	/**
	 * Programa principal para hacer las asociaciones del controlador
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Controlador control = Controlador.obtenerInstancia();
			Modelo modelo = new ModeloImp();
			Vista vista = new Consola();
			control.asociarModelo(modelo);
			control.asociarVista(vista);
			vista.ejecutar(args);
		} catch (MVException e) {
			System.out.println("[MV] Error el linea " + e.getNumLinea() + ": " + e.getError());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/* (non-Javadoc)
	 * @see comunicacion.Controlador#asociarModelo(comunicacion.Modelo)
	 */
	public void asociarModelo(Modelo modelo) {
		this.modelo = modelo;
	}
	/* (non-Javadoc)
	 * @see comunicacion.Controlador#asociarVista(comunicacion.Vista)
	 */
	public void asociarVista(Vista vista) {
		this.vista = vista;
	}

	/* (non-Javadoc)
	 * @see comunicacion.Controlador#actualizarVistas(comunicacion.transfers.Transfer)
	 */
	public void actualizarVistas(Transfer trans) {
		vista.actualizar(trans);
	}
}
