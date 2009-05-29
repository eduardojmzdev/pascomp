package maquinaVirtual;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import excepciones.MVException;
import main.Utils;
import maquinaVirtual.instrucciones.Instruccion;

public class MaquinaVirtualImpl extends MaquinaVirtual{
	
	private Stack<String> pila;	
	private Hashtable<Integer,String> memoriaDatos;	
	private int contadorPrograma;	
	private MemIntrucciones memoriaInstrucciones;	
	
	/**
	 * Comienza la ejecución según los argumentos recibidos
	 * @param args: nombre de archivo [y opción paso a paso]
	 * @return String: estado de la memoria y la pila
	 * @throws Exception 
	 */
	public String ejecutaIni(String[] args) throws Exception {

		String nombreFich = "";
		boolean pasos = false;
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.equalsIgnoreCase("-b"))
				pasos = true;
			else
				nombreFich = args[i];
		}
		cargarFichero(nombreFich);
		resetear();	

		if (!pasos)
			return ejecutaTodas();
		else
			return ejecutaPaso();
	}


	/**
	 * Carga el fichero de entrada
	 * @throws Exception Si no se ha cargado correctamente el fichero
	 */
	private void cargarFichero(String nombreFich) throws Exception {
		try {
			if (Utils.compruebaExtensionSalida(nombreFich)){
				Scanner scan = new Scanner(new File(nombreFich));
				memoriaInstrucciones= new MemIntrucciones();
				while (scan.hasNext()){
					String linea = scan.nextLine();
					procesaLinea(linea);
				}
				scan.close();}
			else{
				throw new Exception("Extension no valida");
			}
		} catch (FileNotFoundException e) {
			throw new Exception("No se encontro el fichero de codigo");
		}

	}

	/**
	 * Procesa una linea y la trata mediante el protocolo adecuado
	 * @param linea a tratar
	 */
	private void procesaLinea(String linea) {
		if (linea.contains("(")){ //instruccion con parametro
			String comando="";
			int i=0;
			while(linea.charAt(i)!='('){
				comando+=linea.charAt(i);
				i++;
			}
			i++;
			
			String param="";
			while(linea.charAt(i)!=')'){
				param+=linea.charAt(i);
				i++;
			}
			memoriaInstrucciones.añadeInstruccion(comando,param);
		}else{ //instruccion sin parametro
			memoriaInstrucciones.añadeInstruccion(linea);	
		}

	}


	/**
	 * Ejecuta toda la secuencia de instrucciones 
	 * @throws MVException si alguna instruccion lanza una excepcion
	 * @return String: estado de la memoria y de la pila
	 */
	@Override
	public String ejecutaTodas() throws MVException {
		try {
			Vector<Instruccion> codigoObjeto = memoriaInstrucciones.getCodigo();
			while(contadorPrograma < codigoObjeto.size()){
				Instruccion instr = codigoObjeto.get(contadorPrograma);
				instr.ejecutar();
				contadorPrograma++;	
			}	
			return printMemoria() + "\n" + printPila();

		} catch (MVException e) {
			e.setNumLinea(contadorPrograma+1);
			throw e;
		}
	}

	/**
	 * Ejecuta la siguiente instruccion 
	 * @throws MVException si la instruccion lanza una excepcion
	 * @return String: estado de la memoria y de la pila, o 
	 * "PROGRAMA TERMINADO." si ha terminado la ejecución
	 */
	@Override
	public String ejecutaPaso() throws MVException{
		try {
			Vector<Instruccion> codigo = memoriaInstrucciones.getCodigo();
			if (contadorPrograma < codigo.size()){
				Instruccion instr = codigo.get(contadorPrograma);
				instr.ejecutar();
				contadorPrograma++;
				return contadorPrograma + ".  " 
					+ codigo.get(contadorPrograma-1) 
					+ "\n\n" + printMemoria() + "\n" + printPila();			
			}else
				return "PROGRAMA TERMINADO.";
		
		} catch (MVException e) {
			e.setNumLinea(contadorPrograma+1);
			throw e;
		}

	}

	/**
	 * Crea una nueva memoria de datos y pila, además inicializa 
	 * el contador de programa a cero.
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
	 * Getter
	 * @return Stack<String>: pila
	 */
	public Stack<String> getPila() {
		return pila;
	}
	
	/**
	 * Getter
	 * @return int: contador de programa 
	 */
	public int getContadorPrograma() {
		return contadorPrograma;
	}
	/**
	 * Devuelve la memoria de instrucciones de la Maquina Virtual
	 * @return memoria de instrucciones de la Maquina Virtual
	 */
	public MemIntrucciones getMemoriaInstrucciones() {
		return memoriaInstrucciones;
	}
	
	/**
	 * Setter
	 * @param CodigoObjeto: memoriaInstrucciones
	 */
	public void setMemoriaInstrucciones(MemIntrucciones memoriaInstrucciones) {
		this.memoriaInstrucciones = memoriaInstrucciones;
	}

	/**
	 * Setter
	 * @param int: contadorPrograma
	 */
	public void setContadorPrograma(int contadorPrograma) {
		this.contadorPrograma = contadorPrograma;
	}

	/**
	 * Getter
	 * @return CodigoObjeto: memoria de instrucciones
	 */
	@Override
	public Hashtable<Integer, String> getMemoriaDatos() {
		return this.memoriaDatos;
	}

	/**
	 * 
	 * @return String: estado de la memoria
	 */
	public String printMemoria(){
		Set<Integer> keys = memoriaDatos.keySet();
		Iterator<Integer> keysIt = keys.iterator();
		String salida = "--MEMORIA DE DATOS--\n";	

		Integer pos;
		
		while (keysIt.hasNext()){
			pos = keysIt.next();
			salida += "Posición: "+ pos + "  contiene: " 
				+ memoriaDatos.get(pos) + "\n";
		}
		return salida;	
	}

	/**
	 * 
	 * @return String: estado de la pila
	 */
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

