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
	 * Getter
	 * @return Hashtable :memoria de datos.
	 */
	public abstract Hashtable<Integer, String> getMemoriaDatos();

	/**
	 * Ejecuta la secuencia de instrucciones
	 * @throws MVException 
	 */
	public abstract String ejecutaTodas() throws MVException;

	/**
	 * Ejecuta la siguiente instruccion
	 * @throws MVException 
	 * 
	 */
	public abstract String ejecutaPaso() throws MVException;

	/**
	 * Crea una nueva memoria de datos y pila, 
	 * actualizando el contador de programa a 0
	 */
	public abstract void resetear();

	/**
	 * Getter
	 * @return Stack<String> : pila.
	 */
	public abstract Stack<String> getPila();

	/**
	 * Gettet
	 * @return int: contador de programa 
	 */
	public abstract int getContadorPrograma();

	/**
	 * Getter
	 * @return CodigoObjeto: memoria de instrucciones
	 */
	public abstract CodigoObjeto getMemoriaInstrucciones();
	
	/**
	 * Setter
	 * @param int: contadorPrograma
	 */
	public abstract void setContadorPrograma(int contadorPrograma);
	
	/**
	 * Setter
	 * @param CodigoObjeto: memoriaInstrucciones
	 */
	public abstract void setMemoriaInstrucciones(CodigoObjeto memoriaInstrucciones);

	/**
	 * libera la instancia de la Maquina virtual
	 */
	public static void reset(){
		instancia = null;
	}
}