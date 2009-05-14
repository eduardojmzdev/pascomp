/**
 * Paquete que engloba las distintas clases que componen el módulo de análisis
 * sintáctico del lenguaje fuente del compilador.
 */
package aSintactico;

/**
 * Importamos los recursos para los tipos definidos.
 */
import java.util.ArrayList;

import tablaSimbolos.*;
import tablaSimbolos.tipos.EntradaTS;
import tablaSimbolos.tipos.EntradaPunteroTS;
import utils.*;

/**
 * Implementa las restricciones contextuales que podemos encontrarnos en el código
 * presente en un fichero dado nuestro lenguaje fuente.
 */
public class RContextuales {
	
	/**
	 * Tabla de símbolos asociada al código.
	 */
	private ArrayList<TablaSimbolos> ts;
	
	/**
	 * Constructor por defecto de las restricciones contextuales. 
	 * Genera una tabla de símbolos vacía.
	 */
	public RContextuales(){
		ts = new ArrayList<TablaSimbolos>();
	}
	
	/**
	 * Constructor por parámetros de las restricciones contextuales.
	 * Toma la tabla de símbolos de los parámetros.
	 * @param tabla Tabla de símbolos que asociamos a las restricciones contextuales.
	 */
	public RContextuales(ArrayList<TablaSimbolos> tabla){
		ts = tabla;
	}

	/**
	 * Accesor de la tabla de símbolos.
	 * @return La tabla de símbolos.
	 */
	public ArrayList<TablaSimbolos> getTablasSimbolos(){
		return ts;
	}

	/**
	 * Mutador de la tabla de símbolos
	 * @param tabla Tabla de símbolos que queremos cambiar.
	 */
	public void setTablasSimbolos(ArrayList<TablaSimbolos> tabla){
		ts = tabla;
	}

	/**
	 * Determina si una constante ha sido previamente declarada o no en nuestro código.
	 * @param lex Lexema asociado a la constante que queremos analizar.
	 * @param nivel Nivel del identificador.
	 * @return Devuevle "true" en caso de que ya esté definida la constante. 
	 * 		   Devuelve "false" siempre y cuando la constante no haya sido declarada.
	 */
	public boolean restriccionCon(String lex, int nivel){
		return ts.get(nivel).existeID(lex);
	}
	
	/**
	 * Determina si un identificador de variable ha sido previamente declarado o no en nuestro código.
	 * @param lex Lexema asociado al identificador de variable que queremos analizar.
	 * @param nivel Nivel del identificador.
	 * @return Devuevle "true" en caso de que ya esté definida la constante. 
	 * 		   Devuelve "false" siempre y cuando la constante no haya sido declarada. 
	 */
	public boolean restriccionIds(String lex, int nivel){
		return ts.get(nivel).existeID(lex);
	}
		
	/**
	 * Determina si el lexema de un token que hemos reconocido como identificador no ha sido definido previamente 
	 * o en realidad es una constante.
	 * @param lex Lexema asociado al identificador que queremos analizar.
	 * @param nivel Nivel del identificador.
	 * @return Devuevle "true" en caso de que ya esté definida y no sea una constante. 
	 * 		   Devuelve "false" en cualquier otro caso. 
	 */
	public boolean restriccionIn(String lex, int nivel){
		InfoTS info = buscaTS(lex,nivel);
		if (info == null){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Determina si el lexema de un token que hemos reconocido representa un identificador que
	 * hemos declarado previamente.
	 * @param lex Lexema asociado al identificador que queremos analizar. 
	 * @param nivel Nivel del identificador.
	 * @return Devuelve "true" en caso de que exista previamente en el código.
	 * 		   Devuelve "false" en cualquier otro caso.
	 */
	public boolean restriccionIden(String lex, int nivel){
		InfoTS info = buscaTS(lex,nivel);
		if (info == null){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * Determina si el lexema de un puntero ya existe, si es de la clase variable y de tipo puntero
	 * @param lex Lexema asociado al puntero que queremos analizar.
	 * @param nivel Nivel del puntero.
	 * @return Devuelve "true" en caso de que lexema del puntero ya exista previamente en el código, 
	 * que sea de la clase variable y de tipo puntero.
	 * 		   Devuelve "false" en cualquier otro caso.
	 */
	public boolean restriccionPuntero(String lex, int nivel){	
		InfoTS info = buscaTS(lex,nivel);
		if ((info != null) && (info.getClase()==TClase.VAR) && (info.getTipo().getNombreTipo()==TTipo.PUNTERO)){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * Determina si en una operacion de igualdad, los dos operandos son de tipos compatibles.
	 * @param t1 Tipo del primer parametro de la igualdad.
	 * @param t2 Tipo del segundo parametro de la igualdad.
	 * @return Devuelve "true" en caso de que se trate de operandos compatibles.
	 * 		   Devuelve "false" en caso de que exista alguna incompatibilidad de operandos.
	 */
	public boolean restriccionTipo( TTipo t1, TTipo t2){ 
		return (t1 != t2);
	}
	
	/**
	 * Determina si en una operacion de orden, los dos operandos son de tipo entero.
	 * @param t1 Tipo del primer parametro de la igualdad.
	 * @param t2 Tipo del segundo parametro de la igualdad.
	 * @return Devuelve "true" en caso de que se trate de operandos enteros..
	 * 		   Devuelve "false" en caso de que exista alguna incompatibilidad de operandos.
	 */
	public boolean restriccionExpOrd(TTipo t1,TTipo t2){
		return ((t1 != TTipo.ENT)||(t2 != TTipo.ENT));
	}
	
	/**
	 * Determina si todos los tipos de una asignacion a un identificador son 
	 * compatibles entre si.
	 * @param t1 Tipo del primer parametro de la operación.
	 * @param t2 Tipo del segundo parametro de la operación.
	 * @param t3 Tipo del tercer parametro de la operación.
	 * @return Devuelve "true" en caso de que todos los tipos que tienen lugar en la operacion son iguales.
	 * 		   Devuelve "false" en caso de que algun tipo sea distinto que el resto.
	 */
	public boolean restriccionExpSumMult(TTipo t1,TTipo t2,TTipo t3){
		return ((t1 != t2)||(t2 != t3)||(t1 != t3));
	}
	
	/**
	 * Determina si el lexema de un token que hemos reconocido en nuestro código 
	 * ha sido definido previamente o no.
	 * @param lex Lexema asociado al identificador que queremos analizar. 
	 * @param nivel Nivel del identificador.
	 * @return Devuelve "true" en caso de que ese identificador no haya sido declarado.
	 * 		   Devuelve "false" en caso de que ese identificador este presente en nuestro codigo.
	 */
	public boolean restriccionExpFact(String lex, int nivel){
		InfoTS info = buscaTS(lex,nivel);
		if (info == null){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Determina si se cumplen las resctricciones para un puntero. Que son
	 * que sea de la clase variable y de tipo puntero.
	 * @param a Información utilizada para comprobar las restricciones.
	 * @return Devuelve "true" en caso de que se cumplan.
	 * 		   Devuelve "false" en caso contrario.
	 */
	public boolean restriccionDesPuntero(InfoTS a){
		if ((a.getClase() == TClase.VAR) && (a.getTipo().getNombreTipo() == TTipo.PUNTERO)){
			return true;
		}
		return false;
	}
	
	/**
	 * Determina si son Tipos son equivalentes
	 * @param a Información del primer Tipo.
	 * @param b Información del segundo Tipo.
	 * @return Devuelve "true" en caso de que sean equivalentes.
	 * 		   Devuelve "false" en caso contrario.
	 */
	public boolean equivalente(EntradaTS a, EntradaTS b){
		if(a.getNombreTipo() == b.getNombreTipo()){
			return true;
		}
		else{
			if(a.getNombreTipo()== TTipo.PUNTERO){
				if (((EntradaPunteroTS)a).getTipo().getNombreTipo() == b.getNombreTipo()){
					return true;
				}
			}
			if(b.getNombreTipo()== TTipo.PUNTERO){
				if (((EntradaPunteroTS)b).getTipo().getNombreTipo() == a.getNombreTipo()){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Método que se encarga de buscar en la Tabla de símbolos la información asociada
	 * a un identificador en el nivel indicado y en todos a los que tiene acceso.
	 * que se encuentra en la Tabla de símbolos.
	 * @param id Identificador.
	 * @param nivel Nivel del identificador.
	 * @return Información asociada al identificador.
	 */
	public InfoTS buscaTS(String id, int nivel) {
		if (nivel == 0){
			if (ts.get(0).existeID(id)) {
				return ts.get(0).obtenerID(id);
			}
			return null;
		}else{
			if (ts.get(nivel).existeID(id)) {
				return ts.get(nivel).obtenerID(id);
			}else{
				return buscaTS(id, nivel-1);
			}
		}

	}
}