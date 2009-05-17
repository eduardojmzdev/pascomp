package comunicacion;

import comunicacion.transfers.FactoriaTransfers;
import comunicacion.transfers.Transfer;

public class Consola implements Vista {

	String[] argumentos;

	public Consola() {

	}

	public Consola(String args[]) throws Exception {
		argumentos = args;
		crearTransfer();
	}

	// Crea el transfer con lo que tiene y lo envia
	public void crearTransfer() throws Exception {

		Transfer trans = FactoriaTransfers.obtenerInstancia().generarTransfer();
		String ruta = "";
		System.out.println("Bienvenido al compilador");
		for (int i = 0; i < argumentos.length; i++) {
			ruta += argumentos[i]+";";
		}
		trans.setTexto(ruta);
		Controlador.obtenerInstancia().captarTransferencia(trans);
	}

	public void actualizar(Transfer t) {
		if (t.getError()){
			System.out.println("　　SE HA PRODUCIDO UN ERROR!!!!");
			System.out.println("El error detectado a sido en:");
			System.out.println(t.getTexto());
		}
		else{
			System.out.println("　　COMPILACION CORRECTA!!!!");
			System.out.println("Se ha generado el siguiente fichero:");
			System.out.println(t.getTexto());
		}
	}

	static public void main(String args[]) {
		Consola c;
		try {
			c = new Consola(args);
			c.crearTransfer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void ejecutar(String[] args) throws Exception {
		argumentos = args;
		crearTransfer();
	}

}
