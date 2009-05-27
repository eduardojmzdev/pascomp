package main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

import traductor.Utils;
import traductor.excepciones.CompiladorException;
import traductor.memoria.TMemoria;
import traductor.tablaSimbolos.TablaSimbolos;

import traductor.aLexico.ALexico;
import traductor.aSintactico.ASintactico;
import traductor.aSintactico.ASintacticoImp;

public class Traductor {
	String ficheroEntrada;
	String ficheroSalida;

	public Traductor(String ficheroEntrada, String ficheroSalida) {
		this.ficheroEntrada = ficheroEntrada;
		this.ficheroSalida = ficheroSalida;
	}

	public Traductor() {

	}

	public void setEntrada(String s) {
		ficheroEntrada = s;
	}

	public void setSalida(String s) {
		ficheroSalida = s;
	}

	public String getEntrada() {
		return ficheroEntrada;
	}

	public String getSalida() {
		return ficheroSalida;
	}

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

	public static void reset() {
		ALexico.reset();
		ASintactico.reset();
		TMemoria.reset();
		TablaSimbolos.reset();
	}

	public static void finalizar() {
		reset();
	}

}
