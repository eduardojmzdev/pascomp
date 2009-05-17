package mVirtual;

import java.util.Hashtable;
import java.util.Stack;

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
	 */
	public abstract void ejecutar();

	/**
	 * Ejecuta un paso de la máquina virtual
	 * 
	 */
	public abstract boolean ejecutarPaso();

	/**
	 * Crea una nueva memoria de datos y pila, y pone el contador de programa a cero.
	 */
	public abstract void resetear();

	//prueba de la máquina virtual
	/*public static void main(String[] args){
	 CodigoObjeto co= new CodigoObjeto();
	 co.añadirInstruccion("Apila","1");
	 co.añadirInstruccion("Apila","2");
	 co.añadirInstruccion("Apila","3");
	 co.añadirInstruccion("Apila","4");
	 co.añadirInstruccion("Apila","5");
	 co.añadirInstruccion("Apila","6");
	 co.añadirInstruccion("Desapila");
	 co.añadirInstruccion("Desapila");
	 co.añadirInstruccion("Suma");
	 co.añadirInstruccion("Suma");
	 co.añadirInstruccion("DesapilaDireccion","1");
	 co.añadirInstruccion("Apila","7");
	 co.añadirInstruccion("Resta");
	 MaquinaVirtual mv= new MaquinaVirtual(co);
	 mv.ejecutar();
	 }*/
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

}