package mVirtual.comunicacion;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Stack;

import mVirtual.CodigoObjeto;
import mVirtual.MaquinaVirtual;

import mVirtual.comunicacion.transfers.Transfer;
import mVirtual.excepciones.MVException;

/**
 * Clase implemtadora del modelo
 * 
 *
 */
public class ModeloImp implements Modelo {
	
	/**
	 * El codigo a ejecutar por la maquina
	 */
	private CodigoObjeto co;
	
	/**
	 * Carga el fichero de la máquina virtual comprobando si se ha cargado correctamente
	 * @param trans Contenedor del fichero
	 * @throws Exception Lanzador de las posibles excepciones
	 */
	private void cargarFichero(Transfer trans) throws Exception {
		try {
			if (new Filtro().accept(trans.getTexto().get(0))){
			Scanner scan = new Scanner(new File(trans.getTexto().get(0)));
			System.out.println("Comienza el parseo del fichero de codigo");
			co= new CodigoObjeto();
			String cadena;
			while (scan.hasNext()){
				cadena=scan.nextLine();
				procesaCadena(cadena);
			}
			System.out.println("Programa cargado en maquina virtual.");
			scan.close();}
		else{
			throw new Exception();
		}
		} catch (FileNotFoundException e) {
			throw new Exception();
		}
		
	}
/**
 * Procesa una cadena y la trata adecuadamente
 * @param cadena a tratar
 */
	private void procesaCadena(String cadena) {
		if (cadena.contains("(")){//Se trata de una instruccion con argumentos
		String subCadena="";
		String comando="";
		int indice=0;
		while(cadena.charAt(indice)!='('){
			comando+=cadena.charAt(indice);
			indice++;
		}
		indice++;
		while(cadena.charAt(indice)!=')'){
			subCadena+=cadena.charAt(indice);
			indice++;
		}
		co.añadirInstruccion(comando,subCadena);
		}
		else{//Se trata de una instruccion sin argumentos
		co.añadirInstruccion(cadena);	
		}
		
	}

	/* (non-Javadoc)
	 * @see comunicacion.Modelo#ejecutaPasoAPaso(comunicacion.transfers.Transfer)
	 */
	public void ejecutaPasoAPaso(Transfer trans) {
		try{
		if(trans.getRuta()){
			cargarFichero(trans);
			MaquinaVirtual.obtenerInstancia().setMemoriaInstrucciones(co);
			MaquinaVirtual.obtenerInstancia().resetear();
		}
			if (trans.getSoloInstruccion()){
			trans.getTexto().removeAllElements();
			trans.setSoloInstruccion(true);
			if(MaquinaVirtual.obtenerInstancia().getContadorPrograma()<MaquinaVirtual.obtenerInstancia().getMemoriaInstrucciones().getCodigo().size())
				trans.setTexto(""+MaquinaVirtual.obtenerInstancia().getMemoriaInstrucciones().getCodigo().get(MaquinaVirtual.obtenerInstancia().getContadorPrograma()), 0);
			else
				trans.setTexto("", 0);
			Controlador.obtenerInstancia().actualizarVistas(trans);
			}
			else if (MaquinaVirtual.obtenerInstancia().ejecutarPaso()){//Se ha ejecutado una instruccion, sacar informacion.
				trans.getTexto().removeAllElements();
				trans.setSoloInstruccion(false);
				Stack<String> pila=MaquinaVirtual.obtenerInstancia().getPila();
				int indice=0;
				for (int i=pila.size();i>0;i--){
					trans.setTexto(pila.get(i-1),indice);
					indice++;
					}
				trans.setTexto(";", indice);
				indice++;
				Hashtable<Integer,String> memoriaDatos=MaquinaVirtual.obtenerInstancia().getMemoriaDatos();
				for (int i=0;i<memoriaDatos.size();i++){
					trans.setTexto(i+"",indice);
					indice++;
					trans.setTexto(memoriaDatos.get(i),indice);
					indice++;
					}
				trans.setTexto(";", indice);
				Controlador.obtenerInstancia().actualizarVistas(trans);
			}
		} catch (Exception e) {
			System.out.println("No se encontro el fichero de codigo");
			e.printStackTrace();
		}
		}		
	/* (non-Javadoc)
	 * @see comunicacion.Modelo#ejecutar(comunicacion.transfers.Transfer)
	 */
	public void ejecutar(Transfer trans) throws Exception {
		try{
			if(trans.getRuta()){
			cargarFichero(trans);
			System.out.println("Comienza la ejecucion.");
			MaquinaVirtual.obtenerInstancia().setMemoriaInstrucciones(co);
			MaquinaVirtual.obtenerInstancia().resetear();
			MaquinaVirtual.obtenerInstancia().ejecutar();
		}
		} catch (MVException e) {
			throw e; 
		} catch (Exception e) {
			//e.printStackTrace();
			throw new Exception("No se encontro el fichero de codigo");
		}
		
	}

}
