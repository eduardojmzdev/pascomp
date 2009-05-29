package main;

import excepciones.CompiladorException;

/**
 * COMPILADOR PLG 08/09
 * 
 * PLG UCM GRUPO 8
 * 
 * @version 1.0
 */
public class Main {

	/**
	 * Metodo de ejecucion de la aplicacion
	 * 
	 * @param args
	 *            Argumentos de entrada. 0 argumentos arranca el interfaz
	 *            grafico, 1 argumento la maquina virtual y 2 argumentos el
	 *            traductor.
	 */
	public static void main(String[] args) {
		if (args.length > 2 || args.length == 1) {
			System.out.println("Con dos argumentos se ejecuta el traductor. Ej. <ficheroEntrada> <ficheroSalida>");
			System.out.println("\nPara usar el modo gráfico no introducir argumentos");
		} else if (args.length == 0) {
			// Se ejecuta el interfaz gráfico

			Ventana ventana = new Ventana();
			ventana.setTitle("PLG");
			ventana.setSize(700, 500);
			ventana.setVisible(true);
		} else if (args.length == 2) {
			// Se ejecuta el traductor

			try {
				String ficheroEntrada = args[0];
				String ficheroSalida = args[1];

				System.out.println("Iniciando compilador...");
				Traductor compilador = new Traductor(ficheroEntrada, ficheroSalida);

				System.out.println("Compilando fichero " + ficheroEntrada + "...");
				compilador.ejecutar();

				System.out.println("Se ha compilado correctamente, escribiendo el fichero " + ficheroSalida + "...");
				System.out.println("Finalizado correctamente.");

			} catch (CompiladorException e) {
				System.out.println("Se produjeron errores al compilar:");
				System.out.println(e.getMensajeError());
			} catch (Exception e) {
				System.out.println("Se produjeron errores desconocidos al compilar:");
				e.printStackTrace();
			}

		}
	}
}
