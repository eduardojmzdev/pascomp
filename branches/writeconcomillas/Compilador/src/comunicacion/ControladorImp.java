package comunicacion;

import comunicacion.transfers.Transfer;

public class ControladorImp extends Controlador {

	// El modelo a controlar
	private Modelo modelo;

	// La vista a controlar
	private Vista vista;

	public void captarTransferencia(Transfer trans) throws Exception {
		if (trans==null){
			throw new Exception("Problemas de comunicacion en el sistema");
		}
		else{
			modelo.ejecutar(trans);
		}
	}

	public static void main(String[] args) {
		try {
			Controlador control = Controlador.obtenerInstancia();
			Modelo modelo = new Compilador();
			Vista vista = new Consola();
			control.asociarModelo(modelo);
			control.asociarVista(vista);
			vista.ejecutar(args);
		} catch (Exception e) {
		e.printStackTrace();
		}
	}

	public void asociarModelo(Modelo modelo) {
		this.modelo = modelo;
	}

	public void asociarVista(Vista vista) {
		this.vista = vista;
	}

	@Override
	public void actualizarVistas(Transfer trans) {
		vista.actualizar(trans);
	}
}
