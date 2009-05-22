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
    public static void main(String[] args) {

	if (args.length != 2) {
	    System.out.println("Modo de Uso: <ficheroEntrada> <ficheroSalida>");
	} else {
	    try {
		String ficheroEntrada = args[0];
		String ficheroSalida = args[1];

		System.out.println("Iniciando compilador...");
		Compilador compilador = new Compilador(ficheroEntrada, ficheroSalida);

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
