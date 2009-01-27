package excepciones;

import excepciones.BasicException;

/**
 * Representa a un error en la maquina virtual MEPA
 *
 */
public class MepaException extends BasicException{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** MAPEO DE CODIGOS A ERRORES */
    static{
        errorMap.put(new Integer(0),"Valor fuera de rango permitido!");
        errorMap.put(new Integer(1),"Division por cero!");
        errorMap.put(new Integer(2),"Numero mal formado!");
        errorMap.put(new Integer(3),"Caracter no perteneciente al alfabeto!");
        errorMap.put(new Integer(4),"Nombre de etiqueta invalido!");
        errorMap.put(new Integer(5),"Nombre de instruccion no valida!");
        errorMap.put(new Integer(6),"Instruccion desconocida!");
        errorMap.put(new Integer(7),"El programa debe empezar con INPP");
        errorMap.put(new Integer(8),"Despues de PARA no pueden haber sentencias!");
        errorMap.put(new Integer(9),"Existen etiquetas sin declarar!");
        errorMap.put(new Integer(10),"El programa debe terminar con PARA");
        errorMap.put(new Integer(11),"La instruccion INPP solo puede ir al comienzo");
        errorMap.put(new Integer(12),"Argumentos invalidos!");
        errorMap.put(new Integer(13),"Entre dos etiquetas debe ir instruccion!");
        errorMap.put(new Integer(14),"Comienzo de instruccion invalida");
        errorMap.put(new Integer(15),"se espera un nro no negativo como primer argumento");
        errorMap.put(new Integer(16),"se espera una etiqueta como primer argumento.");
        errorMap.put(new Integer(17),"se espera una coma entre primer y 2do argumento");
        errorMap.put(new Integer(18),"se espera un nro no negativo como 2do argumento");
        errorMap.put(new Integer(19),"se espera una coma entre 2do y 3er arg");
        errorMap.put(new Integer(20),"se espera un nro no negativo como 3er argumento");
        errorMap.put(new Integer(21),"se espera un numero positivo  o negativo");
        errorMap.put(new Integer(22),"se espera un numero luego de un menos");
        errorMap.put(new Integer(23),"Numero no representable");
        errorMap.put(new Integer(24),"La etiqueta ya esta declarada!");
        errorMap.put(new Integer(25),"Valor invalido ingresado por teclado.");
        errorMap.put(new Integer(26),"Magnitud no representable ingresada por teclado.");
	errorMap.put(new Integer(27),"Desborde de Pila");
        errorMap.put(new Integer(28),"Recursos insuficientes para ejecutar el programa");
    }
    
    /** 
     * Constructor
     * @param codigo codigo de error
     */
    public MepaException(int codigo) {
        super("[MEPA] Error: "+ errorMap.get(new Integer(codigo)));
        this.codigo = codigo;
    }
    
    /**
     * Constructor
     * @param codigo codigo de error
     * @aram numLinea numero de linea donde ocurrio el error
     */
    public MepaException(int codigo, int numLinea) {
        super("[MEPA] Error en linea " + numLinea + ": " + errorMap.get(new Integer(codigo)));
        this.codigo = codigo;
    }
    
    /**
     * Constructor
     * @param codigo codigo de error
     * @param txt mensaje adicional
     * @aram numLinea numero de linea donde ocurrio el error
     */
    public MepaException(int codigo,String txt, int numLinea) {
        super("[MEPA] Error en linea "+numLinea+": '"+txt+"' "+errorMap.get(new Integer(codigo)));
        this.codigo = codigo;
    }
}
