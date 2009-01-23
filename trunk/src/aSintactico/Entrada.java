package aSintactico;

import aSintactico.tipos.Tipo;
import java.util.LinkedList;

/**
 * Representa una entrada en la tabla de simbolos
 * Mantiene la informacion para las distintas entidades 
 * del lenguaje: constantes,variables,parametros,funciones,etc
 *
 * @since jdk 1.6
 * @see aSintactico.TablaEntrada
 */
public final class Entrada {
    
    public static final int DEFAULT_ENTRY   = -1;
    public static final int CONSTANTE       = 0;
    public static final int PROGRAMA        = 1;
    public static final int VARIABLE        = 2;
    public static final int TIPO            = 3;
    public static final int PARAMETRO       = 4;
    public static final int FUNCION         = 5;
    public static final int PROCEDIMIENTO   = 6;
    
    /** clase de entrada */
    public int clase = DEFAULT_ENTRY;
    
    /** nombre de la entidad */
    public String nombre = null;
    
    /** tipo de datos*/
    public Tipo tipo = null;
    
    /** valor de la entidad*/
    public Object valor = null;
    
    public String valorStr = null;
    
    /** nivel lexico */
    public int nivelLexico;
    
    /** direccion  */
    public int desplazamiento;
    
    /** etiqueta del proc o funcion */
    public String etiqueta = null;
    
    /** 
     * Vale false solo para constantes, procedimientos 
     * o cualquier otra entidad que no se le pueden asignar valores
     */
    public boolean asignable = true;
    
    /** parametros (para proc y func)*/
    public LinkedList<Entrada> listaParametros = null;
    
    /** tamaño de parametros (para proc y func)*/
    public int sizeParametros;
    
    /** Indica si la entidad viene por valor o referencia */
    public boolean porValor = true;
    
    /** Indica si es una entidad predefinida */
    public boolean predefinido = false;
    
    /** Constructor */
    public Entrada() { }
        
    /**
     * Constructor
     * @param claseEntrada 
     */
    public Entrada(int clase) {
        this.clase = clase;
    }
    
    /**
     * 
     * @param name 
     * @param claseEntrada 
     */
    public Entrada(String name, int clase){
        this.nombre = name;
        this.clase = clase;
    }
    
    /**
     * 
     * @param name 
     * @param claseEntrada 
     * @param tipo 
     */
    public Entrada(String name, int clase, Tipo tipo){
        this.nombre = name;
        this.clase = clase;
        this.tipo = tipo;
    }
    
    /**
     * 
     * @return true si  la entidad es el programa
     */
    public boolean esPrograma(){
        return clase == PROGRAMA;
    }
    
    /**
     * 
     * @return true si la entidad es una funcion
     */
    public boolean esFuncion(){
        return clase == FUNCION;
    }
    
    /**
     * 
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
    
    /**
     * 
     * @return true si  la entidad es una constante
     */
    public boolean esConstante(){
        return clase == CONSTANTE;
    }
    
    /**
     * 
     * @return true si  la entidad es un Tipo
     */
    public boolean esTipo(){
        return clase == TIPO;
    }
    
    /**
     * 
     * @return true si  la entidad es una variable
     */
    public boolean esVariable(){
        return clase == VARIABLE;
    }
}
