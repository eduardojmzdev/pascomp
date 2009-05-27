package main;

import traductor.Ventana;
import traductor.excepciones.CompiladorException;

/**
 * COMPILADOR PLG 08/09
 * 
 * PLG UCM GRUPO 8
 * 
 * @version 1.0
 */
public class Main {
	public static void main(String[] args) {
		if (args.length > 2) {
			System.out.println("Con un argumento se ejecuta la máquina virtual. Ej. <ficheroEntrada>");
			System.out.println("Con dos argumentos se ejecuta el traductor. Ej. <ficheroEntrada> <ficheroSalida>");
			System.out.println("\nPara usar el modo gráfico no introducir argumentos");
		} else if (args.length == 0) {
			// Se ejecuta el interfaz gráfico

			Ventana ventana = new Ventana();
			ventana.setTitle("PLG");
			ventana.setSize(700, 500);
			ventana.setVisible(true);
		} else if (args.length == 1) {
			// Se ejecuta la máquina virtual

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
