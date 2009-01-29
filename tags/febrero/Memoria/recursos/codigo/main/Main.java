package main;

/**
 * COMPILADOR PLG 08/09
 * 
 * PLG UCM GRUPO 8
 * 
 * Clase principal del compilador
 * 
 * @version 1.0
 */
public class Main {

	/**
	 * Metodo principal de ejecucion del programa
	 * 
	 * @param args
	 *            Argumentos de entrada de linea de comandos: <program name>
	 *            <in_file>
	 */
	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println("Modo de Uso: <program name> <in_file>");
		} else {
			Testeador test = new Testeador(args[0], new Compilador());
			test.start();
		}

	}

}
