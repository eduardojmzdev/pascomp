package mVirtual;

import java.util.Hashtable;
import java.util.Stack;

import mVirtual.excepciones.MVException;

public abstract class MaquinaVirtual {
	private static MaquinaVirtual instancia;
	
	public static MaquinaVirtual obtenerInstancia(){
		if (instancia==null)
			instancia=new MaquinaVirtualImp();
		return instancia;
	}
	/**
	 * Método accesor del atributo memoriaDatos.
	 * @return Hashtable con la memoria de datos (resultados).
	 */
	public abstract Hashtable<Integer, String> getMemoriaDatos();

	/**
	 * Ejecuta el listado de instrucciones que se encuentran en la memoria de instrucciones.
	 * @throws MVException 
	 */
	public abstract void ejecutar() throws MVException;

	/**
	 * Ejecuta un paso de la máquina virtual
	 * @throws MVException 
	 * 
	 */
	public abstract boolean ejecutarPaso() throws MVException;

	/**
	 * Crea una nueva memoria de datos y pila, y pone el contador de programa a cero.
	 */
	public abstract void resetear();

	/**
	 * Devuelve la pila actual de la MV
	 */
	public abstract Stack<String> getPila();

	/**
	 * Devuelve el contador de programa de la MV
	 * @return contador del programa de la MV
	 */
	public abstract int getContadorPrograma();

	/**
	 * Devuelve la memoria de instrucciones de la MV
	 * @return memoria de instrucciones de la MV
	 */
	public abstract CodigoObjeto getMemoriaInstrucciones();
	public abstract void setContadorPrograma(int contadorPrograma);
	public abstract void setMemoriaInstrucciones(CodigoObjeto memoriaInstrucciones);

	public static void reset(){
		instancia = null;
	}
}