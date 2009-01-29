package excepciones;

import excepciones.BasicException;

/**
 * Representa a un error en el Analisis Sintactico
 *
  *@see aSintactico.AnalizadorSintactico
 */
public final class SintacticException extends BasicException{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 
     * ESPECIFICACION DE INFORME DE ERRORES
     * Formato: [CODIGO, MENSAJE]
     */
    static{
        errorMap.put(new Integer(0),"Se espera 'program'");
        errorMap.put(new Integer(1),"Se espera un identificador");
        errorMap.put(new Integer(2),"Se espera un punto y coma ';'");
        errorMap.put(new Integer(3),"Se espera un punto '.'");
        errorMap.put(new Integer(4),"Se espera =");
        errorMap.put(new Integer(5), "Se espera alguna de las sig. palabras reservadas: const,type,var,procedure,function o begin");
        errorMap.put(new Integer(6), "Se espera un identificador de tipo o  alguna de las siguientes palabras reservadas: var,procedure,function o begin");
        errorMap.put(new Integer(7), "se espera un numero o identificador de constante");
        errorMap.put(new Integer(8), "Se espera un identificador de constante o  alguna de las siguientes palabras reservadas: type,var,procedure,function o begin");
        errorMap.put(new Integer(9),"identificador de tipo invalido");
        errorMap.put(new Integer(10), "Se espera un identificador o constructor  de tipo Simple valido");
        errorMap.put(new Integer(11),"Se espera un [");
        errorMap.put(new Integer(12),"Se espera un ]");
        errorMap.put(new Integer(13),"Se espera un of");
        errorMap.put(new Integer(14),"Se espera  '..'");
        errorMap.put(new Integer(15),"Se espera  un ':'");
        errorMap.put(new Integer(16), "Se espera un identificador de variable o  alguna de las siguientes palabras reservadas: procedure,function o begin");
        errorMap.put(new Integer(17), "Se espera texto o un identificador valido");
        errorMap.put(new Integer(18),"Se espera  un  ')'");
        errorMap.put(new Integer(19), "Se espera ';' o falta la lista de parametros formales.");
        errorMap.put(new Integer(20), "Se espera 'var' o un identificador");
        errorMap.put(new Integer(21),"Identificador de funcion invalido");
        errorMap.put(new Integer(22), "Identificador de tipo de retorno invalido");
        errorMap.put(new Integer(23), "Se espera ':', '(' o la lista de parametros formales .");
        errorMap.put(new Integer(24),"Se espera un begin");
        errorMap.put(new Integer(25),"Se espera un end");
        errorMap.put(new Integer(26),"Se espera := ");
        errorMap.put(new Integer(27),"Se espera un then");
        errorMap.put(new Integer(28),"Se espera una constante no signada");
        errorMap.put(new Integer(29),"Se espera [, ( o :=  ");        
        errorMap.put(new Integer(30), "Se espera una  expresion ");
    }
    
    /**
     *
     */   
    public SintacticException(int cod, int numLinea) {
        super("Error Sintactico en linea " + numLinea + ": " + errorMap.get(new Integer(cod)));
		codigo = cod;
    }        
    
}
