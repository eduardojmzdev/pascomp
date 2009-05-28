package mVirtual;

import java.util.Hashtable;
import java.util.Stack;

import excepciones.MVException;

public abstract class MaquinaVirtual {
	private static MaquinaVirtual instancia;
	
	public static MaquinaVirtual obtenerInstancia(){
		if (instancia==null)
			instancia=new MaquinaVirtualImpl();//MaquinaVirtualImp();
		return instancia;
	}
	/**
	 * Getter memoria datos
	 * @return Hashtable con la memoria de datos (resultados).
	 */
	public abstract Hashtable<Integer, String> getMemoriaDatos();

	/**
	 * Ejecuta el listado de instrucciones
	 * @throws MVException 
	 */
	public abstract String ejecutar() throws MVException;

	/**
	 * Ejecuta un paso de la maquina virtual
	 * @throws MVException 
	 * 
	 */
	public abstract String ejecutarPaso() throws MVException;

	/**
	 * Crea una nueva memoria de datos y pila, actualizando el contador de programa a 0
	 */
	public abstract void resetear();

	/**
	 * Devuelve la pila latente a la maquina virtual
	 */
	public abstract Stack<String> getPila();

	/**
	 * Devuelve el contador de programa de la maquina virtual
	 * @return contador del programa de la maquina virtual
	 */
	public abstract int getContadorPrograma();

	/**
	 * Devuelve la memoria de instrucciones de la maquina virtual
	 * @return memoria de instrucciones de la maquina virtual
	 */
	public abstract CodigoObjeto getMemoriaInstrucciones();
	public abstract void setContadorPrograma(int contadorPrograma);
	public abstract void setMemoriaInstrucciones(CodigoObjeto memoriaInstrucciones);

	public static void reset(){
		instancia = null;
	}
}