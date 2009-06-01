package traductor.aSintactico;

import java.io.FileReader;
import java.io.PrintWriter;

/**
 * Clase abstracta del Analizador Sintactico
 * 
 * Usa el patron Singleton
 */
public abstract class ASintactico {

	private static ASintactico instancia;

	/**
	 * Devuelve una instancia del analizador
	 * 
	 * @return
	 */
	public static ASintactico getInstance() {
		if (instancia == null) {
			instancia = new ASintacticoImp();
		}
		return instancia;
	}

	public abstract void analizar(FileReader ficheroFuente) throws Exception;

	public abstract void setCodigo(PrintWriter codigo);

	public static void reset() {
		instancia = null;
	}
}
