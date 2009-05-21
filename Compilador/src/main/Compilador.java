package main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

import aSintactico.ASintacticoImp;


public class Compilador {
	String ficheroEntrada;
	String ficheroSalida;
	
	public Compilador(String ficheroEntrada, String ficheroSalida) {
		this.ficheroEntrada = ficheroEntrada;
		this.ficheroSalida = ficheroSalida;
	}
	
	public void ejecutar() {
		FileReader fuente;
		try {
			if (Utils.compruebaExtension(ficheroEntrada) && !ficheroSalida.equals("")) {

				fuente = new FileReader(ficheroEntrada);

				ASintacticoImp.getInstance().setCodigo(new PrintWriter(ficheroSalida + ".mv"));
				ASintacticoImp.getInstance().analizar(fuente);
				System.out.println("Se ha compilado correctamente, escribiendo el fichero "
								+ ficheroSalida + "...");
			} else {
				System.out.println("Error en la extensiond el fichero");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
