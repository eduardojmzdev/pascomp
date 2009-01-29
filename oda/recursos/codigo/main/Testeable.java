package main;

import java.io.IOException;

/**
 * Permite crear objetos testeables. Las clases que implementen esta interface
 * deben redefinir el metodo <code>run</code> con el codigo propio del test.
 * Por ejemplo, la clases AnalizadorLexico y AnalizadorSintactico implementan
 * esta interface para poder ser testeadas por el testeador
 * 
 * @see Testeador
 */
public interface Testeable {

	/** Comienza el test */
	public void run() throws Exception;

	/** Setea el archivo a analizar */
	public void setSourceFile(String strFile) throws IOException;

	/** Finaliza el test (ej: cerrar archivos abiertos) */
	public void finish();

	/** Comprueba si la extension del archivo es la correcta para el test */
	public boolean validaExtension(String strFile);

}
