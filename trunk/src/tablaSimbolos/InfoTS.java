/**
 * Paquete que recoge todas las clases pertenecientes a la tabla de símbolos.
 */
package tablaSimbolos;

/**
 * Importamos los recursos para los tipos definidos.
 */
import tablaSimbolos.tipos.EntradaTS;
import tablaSimbolos.tipos.EntradaCampoRegistroTS;
import tablaSimbolos.tipos.EntradaRegistroTS;
import utils.*;

import java.util.Hashtable;

/**
 * Clase que almacena la información de cada identificador declarado que se
 * almacenará en la tabla.
 */
public class InfoTS {
	
	/**
	 * Indica si es variable o constante.
	 */
	private TClase clase;   
	
	/**
	 * Tipo del identificador.
	 */
	private	EntradaTS tipo; 	
	
	/**
	 * Valor del identificador: CIERTO, FALSO, o número entero.
	 */
	private	Object valor;	
	
	/**
	 * Dirección de memoria asignada al identificador.
	 * O Etiqueta para los procedimientos.
	 */
	private int dirOEtq;  
	
	/**
	 * Nivel del identificador.
	 */
	private int nivel;  
	
	/**
	 * Constructora por defecto de la clase.
	 */
	public InfoTS() {
	}
	
	/**
	 * Constructora de la clase InfoTS. Inicializa todos los atributos de 
	 * la clase con los parámetros introducidos.
	 * @param clase Clase del identificador (Variable o constante).
	 * @param tipo2 Tipo del identificador.
	 * @param valor Valor del identificador.
	 * @param dirOEtq Dirección de memoria asignada al identificador.
	 * O Etiqueta para los procedimientos.
	 * @param nivel Nivel del identificador.
	 */
	public InfoTS(TClase clase, EntradaTS tipo2, Object valor, int dirOEtq,int nivel) {
		this.clase = clase;
		this.tipo = tipo2;
		this.valor = valor;
		this.dirOEtq= dirOEtq;
		this.nivel = nivel;
	}
	
	/**
	 * Accesor para el atributo clase.
	 * @return Clase del identificador (Variable o constante).
	 */
	public TClase getClase() {
		return clase;
	}
	
	/**
	 * Mutador para el atributo clase.
	 * @param clase Clase del identificador (Variable o constante).
	 */
	public void setClase(TClase clase) {
		this.clase = clase;
	}
	
	/**
	 * Accesor para el atributo dirOEtq.
	 * @return Dirección de memoria asignada al identificador.
	 * O Etiqueta para los procedimientos.
	 */
	public int getDirOEtq() {
		return dirOEtq;
	}
	
	/**
	 * Mutador para el atributo dirOEtq.
	 * @param dirOEtq Dirección de memoria asignada al identificador.
	 * O Etiqueta para los procedimientos.
	 */
	public void setDirOEtq(int dirOEtq) {
		this.dirOEtq = dirOEtq;
	}
	
	/**
	 * Accesor para el atributo tipo.
	 * @return Tipo del identificador.
	 */
	public EntradaTS getTipo() {
		return tipo;
	}
	
	/**
	 * Mutador para el atributo tipo.
	 * @param tipo Tipo del identificador.
	 */
	public void setTipo(EntradaTS tipo) {
		this.tipo = tipo;
	}
	
	/**
	 * Accesor para el atributo valor.
	 * @return Valor del identificador.
	 */
	public Object getValor() {
		return valor;
	}
	
	/**
	 * Mutador para el atributo valor.
	 * @param valor Valor del identificador.
	 */
	public void setValor(Object valor) {
		this.valor = valor;
	}	
	
	/**
	 * Accesor para el atributo nivel.
	 * @return el nivel del identificador.
	 */
	public int getNivel() {
		return nivel;
	}

	/**
	 * Mutador para el atributo valor.
	 * @param nivel Nivel del identificador.
	 */
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	/**
	 * Método para la obtención de toda la información del identificador.
	 * @return Cadena de caracteres con la información.
	 */
	public String toString(Hashtable<Integer,String> memoria){
		if(clase == TClase.VAR){
			if (tipo.getNombreTipo() == TTipo.REGISTRO){
				String x = ("Clase: "+clase.toString()+" - Tipo: "+tipo.toString()+" - Dirección: "+dirOEtq+".");
				EntradaTS arbol = tipo;
				return x+recorreCampos(((EntradaRegistroTS)arbol).getCampos(),memoria,0);
			}
			return("Clase: "+clase.toString()+" - Tipo: "+tipo.toString()+" - Valor: "+memoria.get(dirOEtq)+" - Dirección: "+dirOEtq+".");
		}
		if (clase == TClase.CONS)
			return("Clase: "+clase.toString()+" - Tipo: "+tipo.toString()+" - Valor: "+valor.toString()+" - Dirección: "+dirOEtq+".");

		return "";
	}

	/**
	 * Método para la obtención de toda la información un registro.
	 * @param a Información del primer elemento del registro.
	 * @param memoria Memoria de datos.
	 * @param nivel Nivel del registro.
	 * @return Cadena de caracteres con la información.
	 */
	private String recorreCampos(EntradaCampoRegistroTS a,Hashtable<Integer,String> memoria,int nivel) {
		
		String x="\n";
		for(int i=0;i<nivel;i++)
			x=x+"\t";
				
		x +=imprime(a, memoria);
		
		if(a.getTipoCampo().getNombreTipo() == TTipo.REGISTRO){
			
			EntradaTS arbol = a.getTipoCampo();
			
			x = x+recorreCampos(((EntradaRegistroTS)arbol).getCampos(),memoria,nivel+1);	
		}

		if (a.getHijoIzq() != null)
			x = x+recorreCampos(a.getHijoIzq(), memoria,nivel);
		if (a.getHijoDer() != null)
			x = x+recorreCampos(a.getHijoDer(), memoria,nivel);

		return x;
	}
	
	/**
	 * Funcion que mete en un String los valores de los campos de un registro.
	 * @param a Nodo del registro
	 * @param memoria Memoria de Datos.
	 * @return String con los datos del registro.
	 */
	private String imprime(EntradaCampoRegistroTS a, Hashtable<Integer, String> memoria) {

		int dirCampo = dirOEtq + a.getOffset();
		return ("\t" + a.toString() + " - Tipo: " + a.getTipoCampo().toString()
				+ " - Valor: " + memoria.get(dirCampo) + " - Dirección: "
				+ dirCampo + ".");
	}

}
