package mVirtual;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import mVirtual.instrucciones.FactoriaComandos;
import mVirtual.instrucciones.Instruccion;

/** 
 * Clase que implementa el código objeto compuesto una secuencia 
 * de instrucciones
 * 
 */
public class CodigoObjeto {

	//ATRIBUTOS:
	/** Va a contener las instrucciones */
	Vector<Instruccion> codigo;	

	//MÉTODOS:
	/** Constructora */
	public CodigoObjeto(){
		codigo = new Vector<Instruccion>();
	}

	/** Constructora de la clase, que crea un nuevo Codigo Objeto con uno
	 * ya existente
	 * @param cO código objeto existente.
	 */
	public CodigoObjeto(CodigoObjeto cO){
		codigo = new Vector<Instruccion>();
		codigo.addAll(cO.getCodigo());
	}

	/** Getter
	 * @return Devuelve el atributo codigo
	 */
	public Vector<Instruccion> getCodigo(){
		return codigo;
	}
	
	/** Crea una instruccion
	 * @param nombre String con el nombre de la instruccion.
	 * @param valor String con el valor que toma la instruccion.
	 */
	public void añadirInstruccion(String nombre, String valor){
		Instruccion i = FactoriaComandos.obtenerInstancia().generarComando(nombre);
		i.setDatos(valor);
		codigo.add(i);
	}
		
	/** Añade instruccion sin datos (valor) definidos
	 * @param nombre String con el nombre de la instruccion.
	 */
	public void añadirInstruccion(String nombre){
		Instruccion i = FactoriaComandos.obtenerInstancia().generarComando(nombre);
		codigo.add(i);
	}
	
	/** Escribe en el fichero el vector de instrucciones
	 * @param nombreFichero String que contiene el nombre del fichero donde se van a 
	 * 			almacenar las instrucciones.
	 */
	public void volcarEnFichero(String nombreFichero){
		//Abrimos el fichero y su correspondiente FileWriter
		File fich = new File(nombreFichero);
		try{
			FileWriter out = new FileWriter(fich);
			
			//Recorremos elemento a elemento nuestro vector y lo vamos poniendo
			//en el fichero de salida.
			Instruccion elem;
			for(int i = 0; i<codigo.size(); i++){
				elem = codigo.elementAt(i);
				out.write(elem.toString()+" \r\n");
			}
			out.close();
		}
		catch(IOException e){
			//Tratar excepción
			
		}
	}
	/**
	 * Recorre el vector de instrucciones, para la posterior escritura en el fichero de salida
	 */
	public String toString(){
		String s="";
		
		Instruccion elem;
		for(int i = 0; i<codigo.size(); i++){
			elem = codigo.elementAt(i);
			s+=(elem.toString()+" \r\n");
		}
		return s;
	}
}