/**
 * 
 */
package compilador.aSintactico;
import java.io.FileReader;
import java.io.PrintWriter;

/**
 * 
 */
public abstract class ASintactico {
	
	/**
	 * 
	 */
	private static ASintactico instancia;
	
	/**
	 * 
	 * @return
	 */
	public static ASintactico getInstance(){
		if (instancia == null){
			instancia = new ASintacticoImp();
		}
		return instancia;
	}
	public abstract void analizar(FileReader ficheroFuente) throws Exception;
	
	public abstract void setCodigo(PrintWriter codigo);
}
