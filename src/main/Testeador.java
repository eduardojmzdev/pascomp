package main;

import java.io.File;

/**
 * Permite realizar el testing de uno o mas archivos automaticamente. Debe
 * especificarse la clase <code>Testeable</code> como argumento. Para testear
 * varios archivos se debe pasar como parametro el path o ruta del directorio
 * que los contiene. Si no se especifican argumentos, se buscan archivos .pas a
 * partir del directorio actual. <br>
 * <b>Ejemplo:</b>
 * 
 * <pre>
 *      java main.Testeador aLexico.AnalizadorLexico  test/Lexico/Validos/
 *      java main.Testeador aSintactico.AnalizadorSintactico  test/Sintactico/validos/arch.pas
 *      java main.Testeador aSintactico.AnalizadorSitnactico
 *      java main.Testeador mepa.ALMepa
 *      java main.Testeador mepa.ASMepa
 *      java main.Testeador mepa.Mepa
 *</pre>
 * 
 * @see aLexico.AnalizadorLexico
 * @see aSintactico.AnalizadorSintactico
 */
public final class Testeador {

	/**
	 * Main
	 * 
	 * @param args
	 *            "clase Testeable" ["path o archivo a testear"]
	 */
	public static void main(String args[]) {
		if (args.length == 1 || args.length == 2) {
			Testeable test = null;
			try {
				// Carga la clase testeable
				Class c = Class.forName(args[0]);
				// Construye el objeto testeable
				Object ob = c.getConstructor().newInstance();

				if (ob instanceof Testeable)
					test = (Testeable) ob;
				else {
					System.out
							.printf(
									"La clase '%s' no implementa la interface Testeable!",
									args[0]);
					return;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			if (test == null)
				return;

			Testeador t = new Testeador((args.length == 2) ? args[1] : ".",
					test);
			t.start();
		} else {
			System.out
					.println("Modo de uso: java main.Testeador <clase Testeable> [<dir> | <file>]"
							+ "\nParametros:"
							+ "\n<clase Testeable>: clase que implemente la interface Testeable (ej: aSintactico.AnalizadorSintactico)"
							+ "\n<dir>: permite testear todos los archivos .pas del directorio"
							+ "\n<file>: permite testear un archivo .pas"
							+ "\nSin parametros testea todos los archivos *.pas del directorio actual"
							+ "\nEjemplos:"
							+ "\njava main.Testeador aLexico.AnalizadorLexico   busca archivos .pas o .txt desde el directorio actual"
							+ "\njava main.Testeador aLexico.AnalizadorLexico  somefile.pas    testea el archivo somefile.pas"
							+ "\njava main.Testeador aSintactico.AnalizadorSintactico test/Sintactico/validos    testea los archivos a partir del path dado"
							+ "\njava main.Testeador mepa.Mepa test/Mepa    testea los archivos del directorio test/Mepa");
		}
	}

	/** Archivo|directorio de entrada */
	private File file = null;

	/** Objeto testeable */
	private Testeable test = null;

	/**
	 * Construye el testeador
	 * 
	 * @param file
	 *            el nombre de archivo o directorio con los archivos fuentes.
	 * @param test
	 *            el objeto a testear
	 */
	public Testeador(String file, Testeable test) {
		this.file = new File(file);
		this.test = test;
	}

	public Testeable getTest() {
		return test;
	}

	/**
	 * Inicia el testeador
	 */
	public void start() {
		if (!file.exists()) {
			System.out
					.println("No existe ningun archivo o directorio con el nombre "
							+ file.getName());
			return;
		}

		if (!file.canRead()) {
			System.out.println("No se puede leer el archivo " + file.getName());
			return;
		}

		if (file.isFile()) {
			testear(file.getAbsolutePath());
		} else if (file.isDirectory()) {
			testearRecursivo(file);
		}

	}

	/**
	 * Realiza el test del objeto testeable con el archivo <code>file</code>
	 * dado como parametro
	 * 
	 * @param file
	 *            el archivo .pas o .txt a testear
	 */
	private void testear(String file) {
		System.out
				.println("\n-------------------------------------------------");
		System.out.println("Testing for " + file);

		if (!test.validaExtension(file)) {
			System.out.println("El archivo no tiene la extension adecuada");
			return;
		}

		try {
			test.setSourceFile(file);
			long t1 = System.nanoTime();
			test.run();
			long t2 = System.nanoTime();
			test.finish();
			System.out.printf("End Testing. Time: %f mseg %n",
					(double) (t2 - t1) / 1000000);

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			test.finish();
		}
	}

	/**
	 * Testea todos los archivos validos del directorio y subdirectorios de
	 * <code>dir</code>
	 * 
	 * @param dir
	 *            el nombre del directorio con los archivos a testear
	 */
	private void testearRecursivo(File dir) {
		File[] list = dir.listFiles();
		for (File arch : list) {

			if (!arch.canRead()) {
				System.out.println("No se puedo leer " + file.getName());
				continue;
			}
			if (arch.isFile()) {
				testear(arch.getAbsolutePath());
			} else {
				testearRecursivo(arch);
			}
		}
	}

}
