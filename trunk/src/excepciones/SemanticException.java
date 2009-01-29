package excepciones;

/**
 * Representa a un error en la semantica del lenguaje
 *
  * @see excepciones.BasicException
 */
public class SemanticException extends BasicException{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static{
        errorMap.put(new Integer(0), "Identificador ya declarado");                                      
        errorMap.put(new Integer(1), "Identificador de tipo no valido");        
        errorMap.put(new Integer(2), "Identificador desconocido");
        errorMap.put(new Integer(3), "Incopatibilidad de tipos en asignacion");                
        errorMap.put(new Integer(4), "Se expera una expresion de tipo boolean");
        errorMap.put(new Integer(5), "los  operadores unarios  + o – solo se aplica a expresiones enteras");        
        errorMap.put(new Integer(6), "Tipos incompatibles en expresion");
        errorMap.put(new Integer(7), "no se puede sumar ni restar valores de tipo arreglo o booleano");
        errorMap.put(new Integer(8), "no se puede multiplicar o dividir  valores de tipo booleano");
        errorMap.put(new Integer(9), "se esperan operando de tipo boolean");                
        errorMap.put(new Integer(10), "el identificador no es asignable");
        errorMap.put(new Integer(11), "se esperan un identificador de Variable,  true o false");                
        errorMap.put(new Integer(12), "Magnitud no representable");
        errorMap.put(new Integer(13), "Variable no inicializada");
    }
    
    /** Constructor */
    public SemanticException(int cod, int numLinea, String param) {        
        super("Error Semantico en linea " + numLinea + ": " + errorMap.get(new Integer(cod)) +  " '"+param+"'");
	codigo = cod;
    }            
    
    /** Constructor */
    public SemanticException(int cod, int numLinea) {        
        super("Error Semantico en linea " + numLinea + ": " + errorMap.get(new Integer(cod)) );
	codigo = cod;
    }            
}
