/**
 * Paquete que engloba las distintas clases que componen el módulo de la
 * máquina virtual del compilador.
 */
package mVirtual;

/**
 * Instrucción del lenguaje que está compuesta por su nombre
 * y por el valor que toma. Puede que la instrucción no tome
 * ningún valor.
 */
public class Instruccion {
		
	/**
	 * Nombre de la instrucción.
	 */
	private	String nombre;	
	
	/**
	 * Valor de la instrucción.
	 */
	private String valor;	
	
	/**
	 * Valor para los procedimientos.
	 */
	private String valor2;
		
	/**
	 * Constructora por defecto de la instrucción.
	 */
	 public Instruccion (){
		
	 }
	 
	/**
	 * Contructora de la instrucción que inicializa
	 * el nombre y el valor.
	 * @param nombre Nombre de la instrucción.
	 * @param valor  Valor que toma la instrucción.
	 */
	public Instruccion(String nombre, String valor) {
		this.nombre = nombre;
		this.valor = valor;
	}
	
	/**
	 * Constructora de la instrucción, que sólo inicializa
	 * el nombre.
	 * @param nombre Nombre de la instrucción.
	 */
	public Instruccion(String nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * Contructora de la instrucción que inicializa
	 * el nombre, el valor y el valor para los procedimientos.
	 * @param nombre Nombre de la instrucción.
	 * @param valor1  Valor que toma la instrucción.
	 * @param valor2 valor para los procedimientos
	 */
	public Instruccion(String nombre, String valor1, String valor2){
		this.nombre = nombre;
		this.valor = valor1;
		this.valor2 = valor2;
	}

	/**
	 * Accesor para el atributo nombre de la instrucción.
	 * @return Nombre de la instrucción.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Mutador para el nombre de la instrucción.
	 * @param nombre Nombre de la instrucción.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Accesor para el atributo valor de la instrucción.
	 * @return Valor de la instrucción.
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * Le asigna un valor a la instrucción.
	 * @param valor Valor de la instrucción.
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	/**
	 * Método que devuelve el nombre y el valor de
	 * una instrucción o sólo el nombre en caso de 
	 * está no tenga ningún valor.
	 * @return String con el nombre y el valor de instrucción.
	 */
	public String toString(){		
		if(valor !=null){
			return nombre + "(" + valor + ");";
		}
		return nombre + ";";
	}

	/**
	 * Accesor para el atributo valor de los procedimientos.
	 * @return valor2 Valor para el procedimiento.
	 */
	public String getValor2() {
		return valor2;
	}
	
	/**
	 * Mutador para el atributovalor de los procedimientos.
	 * @param valor2 Valor para el procedimiento.
	 */
	public void setValor2(String valor2) {
		this.valor2 = valor2;
	}
}
