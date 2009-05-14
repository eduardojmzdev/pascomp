/**
 * Paquete que recoge todas las clases pertenecientes a la tabla de símbolos.
 */
package tablaSimbolos;

//LIBRERÍAS:

import java.util.Hashtable;

/**
 * Importamos los recursos para los tipos definidos.
 */
import tablaSimbolos.tipos.EntradaTS;
import tablaSimbolos.tipos.EntradaProcTS;
import utils.*;

/**
 * Implementa la estructura y métodos de una tabla de símbolos para variables y constantes 
 * definidas en el lenguaje.
 */
public class TablaSimbolos {

	/**
	 * Tabla Hash cuya clave son los identificadores (lexemas del token) y
	 * sus valores son objetos con la información acerca del token.
	 */
	private Hashtable<String,InfoTS> tablaHash;

	/**
	 * Constructora por defecto de la clase.
	 * Inicializa el atributo tablaHash con una nueva Hashtable de clave String y 
	 * valor InfoTS.
	 */
	public TablaSimbolos(){
		tablaHash = new Hashtable<String,InfoTS>();
		
		// Insertamos la información de los tipos básicos.
		EntradaTS nodoENT = new EntradaTS(TTipo.ENT, 1);
		InfoTS ent = new InfoTS(TClase.TBASICO, nodoENT, null, 0, 0);
		tablaHash.put("ENT", ent);
		EntradaTS nodoLOG = new EntradaTS(TTipo.LOG, 1);
		InfoTS log = new InfoTS(TClase.TBASICO, nodoLOG, null, 0, 0);
		tablaHash.put("LOG", log);
	}
	
	/**
	 * Inserta una nueva variable en la tabla Hash siempre
	 * que no exista previamente con la misma clave.
	 * @param id Nombre del identificador.
	 * @param tipo Tipo del identificador: ENT o LOG.
	 * @param dir Dirección de memoria asignada al identificador.
	 * @param nivel Nivel del identificador.
	 */
	public void añadeVariable(String id, EntradaTS tipo, int dir,int nivel){
		if(!existeID(id)){
			TClase c = TClase.VAR;
			InfoTS aux = new InfoTS(c,tipo,null,dir,nivel);
			tablaHash.put(id, aux);
		}
	}
	
	/**
	 * Inserta una nueva constante en la tabla Hash siempre que no exista previamente 
	 * con la misma clave.
	 * @param id Nombre del identificador.
	 * @param tipo Tipo del identificador: ENT o LOG.
	 * @param valor Valor del identificador: CIERTO, FALSO, o número entero.
	 * @param dir Dirección de memoria asignada al identificador.
	 * @param nivel Nivel del identificador.
	 */
	public void añadeConstante(String id, EntradaTS tipo, Object valor, int dir,int nivel){
		if(!existeID(id)){
			TClase c = TClase.CONS;
			InfoTS aux = new InfoTS(c,tipo,valor,dir,nivel);
			tablaHash.put(id, aux);
		}
	}
	
	/**
	 * Método que devuelve la información asociada al identificador indicado por parámetro.
	 * @param id Nombre del identificador.
	 * @return Objeto de tipo InfoTS con toda la información del identificador.
	 */
	public InfoTS obtenerID(String id){
		return tablaHash.get(id);
	}
	
	/**
	 * Método que indica si una clave contiene el identificador
	 * pasado como parámetro.
	 * @param id Nombre del identificador.
	 * @return Booleano que indica si la clave se encuentra en la tabla.
	 */
	public boolean existeID(String id){
		return tablaHash.containsKey(id);
	}
	
	/**
	 * Método para la obtención de toda la información de los identificadores contenidos
	 * en la tabla de símbolos.
	 * @param memoria Memoria de datos en la que están contenidos los valores de las 
	 * variables.
	 * @return Cadena de caracteres con la información.
	 */
	public String toString(Hashtable<Integer,String> memoria){
		StringBuffer str = new StringBuffer();
		for(String key: tablaHash.keySet()) {
			InfoTS informacion = tablaHash.get(key);
			if (informacion.getClase()==TClase.VAR || informacion.getClase()==TClase.CONS)
			str.append("Identificador: "+key+" - "+informacion.toString(memoria)+"\n");
		}
		return str.toString();
	}
	
	/**
	 * Inserta un nuevo tipo definido en la tabla Hash siempre que no exista previamente 
	 * con la misma clave.
	 * @param id Nombre del identificador.
	 * @param tipo Tipo del identificador.
	 * @param dir Dirección de memoria asignada al identificador.
	 * @param nivel Nivel del identificador.
	 */
	public void addTipoConstruido(String id,EntradaTS tipo,int dir,int nivel){
		if(!existeID(id)){
			TClase c = TClase.TCONSTRUIDO;
			InfoTS aux = new InfoTS(c,tipo,null,dir,nivel);
			tablaHash.put(id, aux);
		}
	}
	
	/**
	 * Inserta un nuevo procedimiento definido en la tabla Hash siempre que no exista previamente 
	 * con la misma clave.
	 * @param id Nombre del procedimiento.
	 * @param tipo Información del procedimiento.
	 * @param etq Dirección de memoria asignada al procedimiento.
	 * @param nivel Nivel del procedimiento.
	 */
	public void addProcedimiento(String id,EntradaProcTS tipo,int etq,int nivel){
		if(!existeID(id)){
			TClase c = TClase.TCONSTRUIDO;
			InfoTS aux = new InfoTS(c,tipo,null,etq,nivel);
			tablaHash.put(id, aux);
		}
	}
}
