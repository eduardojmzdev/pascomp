package mVirtual;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

import excepciones.MVException;
import mVirtual.instrucciones.Instruccion;
import main.Utils;

public class MaquinaVirtualImpl extends MaquinaVirtual{
	/**
	 * Pila de la maquina virtual.
	 */
	private Stack<String> pila;	
	/**
	 * Tabla Hash: contiene la memoria de datos.
	 */
	private Hashtable<Integer,String> memoriaDatos;	
	/**
	 * Contador de Programa: número de instrucción que se está ejecutando.
	 */
	private int contadorPrograma;	
	/**
	 * Conjunto de instrucciones que ejecuta la máquina virtual.
	 */
	private CodigoObjeto memoriaInstrucciones;	
	
	

	public String ejecutaIni(String[] args) throws Exception {

		String arg = "";
		String nombreFich = "";
		boolean traza = false;
		System.out.println("Bienvenido al entorno de Ejecución.");
		for (int i = 0; i < args.length; i++) {
			arg = args[i];
			if (arg.equalsIgnoreCase("-b"))
				traza = true;
			else
				nombreFich = args[i];
		}
		cargarFichero(nombreFich);
		resetear();	

		if (!traza)
			return ejecutar();
		else
			return ejecutarPaso();
	}


	/**
	 * Carga el fichero de la máquina virtual, comprueba a su vez si se ha cargado correctamente
	 * @throws Exception Lanzador de las posibles excepciones
	 */
	private void cargarFichero(String nombreFich) throws Exception {
		try {
			if (Utils.compruebaExtensionSalida(nombreFich)){
				Scanner scan = new Scanner(new File(nombreFich));
				System.out.println("Comienza la lectura del codigo");
				memoriaInstrucciones= new CodigoObjeto();
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
			throw new Exception("No se encontro el fichero de codigo");
		}

	}

	/**
	 * Procesa una cadena y la trata mediante el protocolo adecuado
	 * @param cadena a tratar
	 */
	private void procesaCadena(String cadena) {
		if (cadena.contains("(")){ //instruccion con argumentos
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
			memoriaInstrucciones.añadirInstruccion(comando,subCadena);
		}else{ //instruccion sin argumentos
			memoriaInstrucciones.añadirInstruccion(cadena);	
		}

	}


	/**
	 * Ejecuta la lista de instrucciones contenidas en la memoria de instrucciones.
	 */

	@Override
	public String ejecutar() throws MVException {
		try {
			while(contadorPrograma < memoriaInstrucciones.getCodigo().size())
			{
				Instruccion instr = memoriaInstrucciones.getCodigo().get(contadorPrograma);
				instr.Ejecutar();
				contadorPrograma++;	
			}	
			return printMemoria() + "\n" + printPila();

		} catch (MVException e) {
			e.setNumLinea(contadorPrograma+1);
			throw e;
		}
	}

	@Override
	public String ejecutarPaso() throws MVException{
		try {
			if (contadorPrograma < memoriaInstrucciones.getCodigo().size()){
				Instruccion aux = memoriaInstrucciones.getCodigo().get(contadorPrograma);
				aux.Ejecutar();
				contadorPrograma++;
				return contadorPrograma + ".  " 
					+memoriaInstrucciones.getCodigo().get(contadorPrograma-1) 
					+ "\n\n"+printMemoria() + "\n" + printPila();			
			}else
				return "PROGRAMA TERMINADO.";
		
		} catch (MVException e) {
			e.setNumLinea(contadorPrograma+1);
			throw e;
		}

	}

	/**
	 * Crea una nueva memoria de datos y pila, además inicializa el contador de programa a cero.
	 */
	public void resetear(){
		pila = new Stack<String>();
		memoriaDatos = new Hashtable<Integer,String>();
		memoriaDatos.put(0,"CP");
		memoriaDatos.put(1,"7");
		memoriaDatos.put(2,"7");
		memoriaDatos.put(3,"7");
		memoriaDatos.put(4,"7");
		memoriaDatos.put(5,"7");
		memoriaDatos.put(6,"7");
		contadorPrograma = 0;
	}

	/**
	 * Devuelve la pila actual de la MV
	 */
	public Stack<String> getPila() {
		return pila;
	}
	/**
	 * Devuelve el PC de la Maquina Virtual
	 * @return PC de la Maquina Virtual
	 */
	public int getContadorPrograma() {
		return contadorPrograma;
	}
	/**
	 * Devuelve la memoria de instrucciones de la Maquina Virtual
	 * @return memoria de instrucciones de la Maquina Virtual
	 */
	public CodigoObjeto getMemoriaInstrucciones() {
		return memoriaInstrucciones;
	}
	
	public void setMemoriaInstrucciones(CodigoObjeto memoriaInstrucciones) {
		this.memoriaInstrucciones = memoriaInstrucciones;
	}

	public void setContadorPrograma(int contadorPrograma) {
		this.contadorPrograma = contadorPrograma;
	}

	@Override
	public Hashtable<Integer, String> getMemoriaDatos() {
		return this.memoriaDatos;
	}

	public String printMemoria(){
		Set<Integer> keys = memoriaDatos.keySet();
		Iterator<Integer> keysIt = keys.iterator();
		String salida = "--MEMORIA DE DATOS--\n";	

		Integer pos;
		
		while (keysIt.hasNext()){
			pos = keysIt.next();
			salida += "Posición: "+ pos + "  contiene: " + memoriaDatos.get(pos) + "\n";
		}
		return salida;	
	}

	public String printPila(){
		Iterator<String> pilaIt= pila.iterator();
		String salida = "";	
		while (pilaIt.hasNext()){
			String datoPila = pilaIt.next();
			salida = datoPila + "\n" + salida;
		}
		if(salida.equals("")) salida = "vacia";
		return "--PILA--\n" + salida;

	}

}

