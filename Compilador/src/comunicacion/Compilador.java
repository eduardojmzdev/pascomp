package comunicacion;

import java.io.FileReader;
import java.io.PrintWriter;

import aSintactico.ASintacticoImp;


import comunicacion.transfers.Transfer;

public class Compilador implements Modelo {
	
	public void ejecutar(Transfer trans) {
		String rutaFichero="";
		String ficheroSalida="";
		int puntero=0;
		while (trans.getTexto().charAt(puntero)!=';'){
			rutaFichero+=trans.getTexto().charAt(puntero);
			puntero++;
		}
		puntero++;
		String error="";
		while (trans.getTexto().charAt(puntero)!=';'){
			ficheroSalida+=trans.getTexto().charAt(puntero);
			puntero++;
		}
		Filtro filtro=new Filtro();
		FileReader fuente;
		try{
			if(filtro.accept(rutaFichero)&&!ficheroSalida.equals("")){
				try{
					fuente = new FileReader(rutaFichero);
					}
				catch (Exception e){
					error="Tiempo de ejecucion: Problemas con el fichero.";
					throw new Exception();  
				}
				ASintacticoImp.getInstance().setCodigo(new PrintWriter(ficheroSalida+".da"));
				ASintacticoImp.getInstance().analizar(fuente);
				error=ficheroSalida+".da\nTODO VA BIEN";
				trans.setError(false);
				trans.setTexto(error);
				Controlador.obtenerInstancia().actualizarVistas(trans);
				}
			else{
			//Fichero no aceptado
		    error="Tiempo de ejecucion: compruebe que la extension sea: .dam o que haya introducido un fichero y escriba el nombre del fichero de salida";
		    throw new Exception();
			}
		} catch (Exception e) {
		trans.setError(true);
		if (error.equals(""))
			error=e.getMessage();
		trans.setTexto(error);
		Controlador.obtenerInstancia().actualizarVistas(trans);	
		}
		
	}

}
