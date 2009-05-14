package aSintactico;

import java.util.LinkedList;

import aSintactico.tipos.Tipo;

/**
 * Representa una entrada en la tabla de simbolos Mantiene la informacion para
 * las distintas entidades del lenguaje:
 * constantes,variables,parametros,funciones,etc
 * 
 * @since jdk 1.6
 * @see aSintactico.EntryTable
 */
public final class Entry {
	public static final int DEFAULT_ENTRY = -1;
	public static final int CONSTANTE = 0;
	public static final int PROGRAMA = 1;
	public static final int VARIABLE = 2;
	public static final int TIPO = 3;
    public static final int PARAMETRO = 4;
    public static final int PROCEDIMIENTO = 5;

	/** clase de entrada */
	public int clase = DEFAULT_ENTRY;

	/** nombre de la entidad */
	public String nombre = null;

	/** tipo de datos */
	public Tipo tipo = null;

	/** valor de la entidad */
	public Object valor = null;

	public String valorStr = null;

	/** nivel lexico */
	public int nivelLexico;

	/** direccion */
	public int desplazamiento;

	/**
	 * Vale false solo para procedimientos o cualquier otra entidad
	 * que no se le pueden asignar valores
	 */
	public boolean asignable = true;
        
    /** Para ver si una variable tiene asignado un valor*/
	public boolean tieneValor = false;

	// ATRIBUTOS PARA LOS PROCEDIMIENTOS
	
	/** etiqueta del procedimiento */
	public String etiqueta = null;
	
    /** parametros para procedimientos */
    public LinkedList<Entry> listaParametros = null;
    
    /** tamaño de parametros de los procedimientos */
    public int sizeParametros;
    
    /** Indica si la entidad viene por valor o referencia */
    public boolean porValor = true;
    	
	/** Constructor */
	public Entry() {
	}

	/**
	 * Constructor
	 * 
	 * @param claseEntrada
	 */
	public Entry(int clase) {
		this.clase = clase;
	}

	/**
	 * 
	 * @param name
	 * @param claseEntrada
	 */
	public Entry(String name, int clase) {
		this.nombre = name;
		this.clase = clase;
	}

	/**
	 * 
	 * @param name
	 * @param claseEntrada
	 * @param tipo
	 */
	public Entry(String name, int clase, Tipo tipo) {
		this.nombre = name;
		this.clase = clase;
		this.tipo = tipo;
	}

	/**
	 * Metodo auxiliar para detectar el identificador "PROGRAMA"
	 * 
	 * @return true si la entidad es el programa
	 */
	public boolean esPrograma() {
		return clase == PROGRAMA;
	}

	/**
	 * Metodo auxiliar para detectar si es una variable
	 * 
	 * @return true si la entidad es una variable
	 */
	public boolean esVariable() {
		return clase == VARIABLE;
	}

	/**
	 * Metodo auxiliar para detectar si es un tipo
	 * 
	 * @return true si la entidad es un Tipo
	 */
	public boolean esTipo() {
		return clase == TIPO;
	}

	/**
	 * Metodo auxiliar para comprobar constantes
	 * 
	 * @return true si la entidad es una constante
	 */
	public boolean esConstante() {
		return clase == CONSTANTE;
	}
	
	/*
    * @return true si la entidad es un procedimiento
    */
   public boolean esProcedimiento(){
       return clase == PROCEDIMIENTO;
   }
   /**
    * 
    * @return true si  la entidad es un parametro
    */
   public boolean esParametro(){
       return clase == PARAMETRO;
   }

}
