package main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

import excepciones.CompiladorException;
import traductor.memoria.TMemoria;
import traductor.tablaSimbolos.TablaSimbolos;

import traductor.aLexico.ALexico;
import traductor.aSintactico.ASintactico;
import traductor.aSintactico.ASintacticoImp;

/**
 * Clase principal del Traductor
 *
 */
public class Traductor {
	String ficheroEntrada;
	String ficheroSalida;

	/**
	 * Crea un nuevo traductor
	 * @param ficheroEntrada Ruta del fichero de entrada
	 * @param ficheroSalida Ruta del fichero de salida
	 */
	public Traductor(String ficheroEntrada, String ficheroSalida) {
		this.ficheroEntrada = ficheroEntrada;
		this.ficheroSalida = ficheroSalida;
	}

	/**
	 * Constructor por defecto. Si se usa hay que usar los setters del fichero de entrada y salida
	 */
	public Traductor() {
	}

	/**
	 * Especifica el nombre del fichero de entrada
	 * @param s Nombre del fichero de entrada
	 */
	public void setEntrada(String s) {
		ficheroEntrada = s;
	}

	/**
	 * Especifica el nombre del fichero de salida
	 * @param s Nombre del fichero de salida
	 */
	public void setSalida(String s) {
		ficheroSalida = s;
	}

	/**
	 * Devuelve el nombre del fichero de entrada
	 * @return String con el fichero de entrada
	 */
	public String getEntrada() {
		return ficheroEntrada;
	}

	/**
	 * Devuelve el nombre del fichero de salida
	 * @return String con el fichero de salida
	 */
	public String getSalida() {
		return ficheroSalida;
	}

	/**
	 * Ejecuta el analizador una vez especificados el fichero de entrada y salida.
	 * Traduce el fichero de entrada y crea el codigo p en el fichero salida.
	 * 
	 * @throws Exception
	 */
	public void ejecutar() throws Exception {
		if (Utils.compruebaExtension(ficheroEntrada) && !ficheroSalida.equals("")) {
			FileReader fuente;
			try {
				fuente = new FileReader(ficheroEntrada);
			} catch (FileNotFoundException e) {
				throw new CompiladorException("No se ha encontrado el fichero fuente: " + System.getProperty("user.dir") + "\\" +  ficheroEntrada);
			} 
			ASintacticoImp.getInstance().setCodigo(new PrintWriter(ficheroSalida + "." + Utils.extensionSalida));
			ASintacticoImp.getInstance().analizar(fuente);
			
			fuente.close();
		} else {
			throw new CompiladorException("Error en la extension del fichero, debe ser \".src\"");
		}
	}

	/**
	 * Resetea todos los miembros estaticos de ciertas clases para poder ejecutar varias veces el compilador
	 * sin necesidad de terminar la aplicacion.
	 */
	public static void reset() {
		ALexico.reset();
		ASintactico.reset();
		TMemoria.reset();
		TablaSimbolos.reset();
	}

	/**
	 * Finaliza el proceso de traduccion
	 * 
	 */
	public static void finalizar() {
		reset();
	}

}
