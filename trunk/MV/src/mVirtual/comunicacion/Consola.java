package mVirtual.comunicacion;

import java.io.InputStreamReader;
import java.util.Scanner;

import mVirtual.comunicacion.transfers.FactoriaTransfers;
import mVirtual.comunicacion.transfers.Transfer;
/**
 * Implementación de la vista de usuario
 *
 */
public class Consola implements Vista {

	String[] argumentos;
	/**
	 * 
	 * Constructora por defecto de la clase consola
	 * 
	 *
	 */
	public Consola() {

	}
	/**
	 * 
	 * Constructora de consola con un array de strings como argumentos
	 *
	 *
	 */
	public Consola(String args[]) throws Exception {
		argumentos = args;
		crearTransfer();
	}

	/**
	 * 
	 * Crea un transfer y lo envía
	 * 
	 *
	 */
	public void crearTransfer() throws Exception {

		Transfer trans = FactoriaTransfers.obtenerInstancia().generarTransfer();
		String arg = "";
		String ruta = "";
		boolean traza = false;
		System.out.println("Bienvenido al entorno de Ejecución.");
		for (int i = 0; i < argumentos.length; i++) {
			arg = argumentos[i];
			if (arg.equalsIgnoreCase("-b"))
				traza = true;
			else
				ruta = argumentos[i];
		}
		trans.setModoTraza(traza);
		trans.setTexto(ruta, 0);
		trans.setRuta(true);
		trans.setSoloInstruccion(true);
		Controlador.obtenerInstancia().captarTransferencia(trans);
	}
	/**
	 * 
	 * Actualiza el transfer con el pasado por parámetro
	 * 
	 *
	 */
	public void actualizar(Transfer t) {
		if (t.getComunicacionInterna()) {
			if (t.getTexto().size()!=0) {//Es una escritura del programa
				System.out
						.println("Escritura realizada por programa de usuario");
				System.out.println(t.getTexto().get(0));
			}
			else{
				System.out
				.println("Lectura solicitada por programa de usuario");
				Scanner lector = new Scanner(System.in);
				String cadenaLeida="";
				try {
					cadenaLeida=lector.next();
					} catch (Exception e) {
					
				}
				t.setTexto(cadenaLeida,0);
			}
		} else {
			if (t.getSoloInstruccion()) {
				if (t.getTexto().get(0).equals("")) {
					System.out
							.println("No quedan mas instrucciones por ejecutar,Trazado Concluido.");
				} else {
					System.out
							.println("La siguiente instruccion a ejecutar es del tipo:");
					System.out.println(t.getTexto().get(0));
					if (seguirEjecutando()) {
						Transfer trans = FactoriaTransfers.obtenerInstancia()
								.generarTransfer();
						trans.setModoTraza(true);
						trans.setSoloInstruccion(false);
							try {
								Controlador.obtenerInstancia().captarTransferencia(
										trans);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
					} else {
						System.out.println("Ejecucion detenida por el usuario");
				}
				}
			} else {
				int i = 0;
				if (t.getTexto().size() != 0) {
					String Instruccion = t.getTexto().get(i);
					System.out.println("Estado de la Pila");
					while ((i < t.getTexto().size())
							&& (!Instruccion.equalsIgnoreCase(";"))) {

						System.out.print("Posicion " + (i + 1) + ":   ");
						System.out.println(Instruccion);
						i++;
						Instruccion = t.getTexto().get(i);
					}
					i++;
					System.out.println("");
					System.out.println("");

					Instruccion = t.getTexto().get(i);
					System.out.println("Estado de la memoria");

					while ((i < t.getTexto().size())
							&& (!Instruccion.equalsIgnoreCase(";"))) {
						System.out.print("Posicion de Memoria "
								+ t.getTexto().get(i) + ":   ");
						i++;
						Instruccion = t.getTexto().get(i);
						System.out.println(Instruccion);
						i++;
						Instruccion = t.getTexto().get(i);
					}
					System.out.println("");
					System.out.println("");
					System.out.println("Fin de paso de ejecución.");
					t.setSoloInstruccion(true);
					try {
						Controlador.obtenerInstancia().captarTransferencia(
								t);
					} catch (Exception e) {
						e.printStackTrace();
					}
					}
				}
			}
	}
	/**
	 * 
	 * Método que indica si se quiere seguir ejecutando
	 * 
	 *
	 */
	private boolean seguirEjecutando() {
		int leido = 0;
		char opcion = 'a';// inicializo el caracter a algo no valido
		System.out
				.println("Desea seguir trazando el programa? Si es asi introduzca una r");
		InputStreamReader lector = new InputStreamReader(System.in);
		try {
			leido = lector.read();
			opcion = (char) leido;
		} catch (Exception e) {
			return false;
		}

		if (opcion == 'r')
			return true;

		return false;

	}
/**
 * Programa principal
 * @param args Argumentos del programa principal
 */
	static public void main(String args[]) {
		Consola c;
		try {
			c = new Consola(args);
			c.crearTransfer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Ejecuta con los argumentos pasados por parámetro
	 */
	public void ejecutar(String[] args) throws Exception {
		argumentos = args;
		crearTransfer();
	}

}
