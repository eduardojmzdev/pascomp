package main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

import excepciones.CompiladorException;

import aSintactico.ASintacticoImp;

public class Compilador {
    String ficheroEntrada;
    String ficheroSalida;

    public Compilador(String ficheroEntrada, String ficheroSalida) {
	this.ficheroEntrada = ficheroEntrada;
	this.ficheroSalida = ficheroSalida;
    }

    public void ejecutar() throws Exception {
	if (Utils.compruebaExtension(ficheroEntrada) && !ficheroSalida.equals("")) {
	    FileReader fuente;
	    try {
		fuente = new FileReader(ficheroEntrada);
	    } catch (FileNotFoundException e) {		
		throw new CompiladorException("No se ha encontrado el fichero fuente: " + ficheroEntrada);
	    }
	    ASintacticoImp.getInstance().setCodigo(new PrintWriter(ficheroSalida + Utils.extensionSalida));
	    ASintacticoImp.getInstance().analizar(fuente);
	} else {
	    throw new CompiladorException("Error en la extension del fichero, debe ser \".src\"");
	}
    }

}
