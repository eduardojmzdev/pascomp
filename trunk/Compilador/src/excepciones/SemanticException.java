package excepciones;

import excepciones.BasicException;

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
        errorMap.put(new Integer(0),"Identificador ya declarado");
        errorMap.put(new Integer(1),"Se espera un tipo simple");        
        errorMap.put(new Integer(2),"Identificador de constante no valido");
        errorMap.put(new Integer(3),"No se puede aplicar + o - a una constante no entera");
        errorMap.put(new Integer(4),"El tipo integer no es un tipo de indice valido");
        errorMap.put(new Integer(5),"Identificador de tipo no valido");
        errorMap.put(new Integer(6),"Identificador de parametro formal repetido");
        errorMap.put(new Integer(7),"Identificador desconocido");
        errorMap.put(new Integer(8),"Incopatibilidad de tipos en asignacion");
        errorMap.put(new Integer(9),"No se permiten valores booleanos en subrangos");
        errorMap.put(new Integer(10),"Se espera un arreglo");
        errorMap.put(new Integer(11),"Se expera una expresion de tipo boolean");
        errorMap.put(new Integer(12),"Se expera una expresion de tipo simple");
        errorMap.put(new Integer(13),"Constante repetida");        
        errorMap.put(new Integer(14),"los operadores relacionales solo se aplican a expresiones de tipos simples");
        errorMap.put(new Integer(15),"expresión relacional con tipos incompatibles");
        errorMap.put(new Integer(16),"se espera el nombre de una variable o componente de un arreglo como parámetro actual");
        errorMap.put(new Integer(17),"se espera el nombre de una variable o componente de arreglo");
        errorMap.put(new Integer(18),"los  operadores unarios  + o – solo se aplica a expresiones enteras");
        errorMap.put(new Integer(19),"no se permite pasar por referencia un valor numérico o booleano");
        errorMap.put(new Integer(20),"Tipos incompatibles en expresion");
        errorMap.put(new Integer(21),"no se puede sumar ni restar valores de tipo arreglo o booleano");
        errorMap.put(new Integer(22),"no se puede multiplicar o dividir  valores de tipo arreglo o booleano");
        errorMap.put(new Integer(23),"se esperan operando de tipo boolean");                
        errorMap.put(new Integer(24),"no se puede pasar una  numero o funcion por referencia");
        //errorMap.put(new Integer(25),"el operador not solo se aplica a expresiones booleanas");
        errorMap.put(new Integer(26),"El identificador no corresponde a una funcion");
        errorMap.put(new Integer(27),"no se puede pasar una función por referencia");
        errorMap.put(new Integer(28),"faltan parametros actuales");
        errorMap.put(new Integer(29),"incompatibilidad de tipos entre parámetro formal y actual");             
        errorMap.put(new Integer(30),"se espera un parámetro de tipo simple");        
        errorMap.put(new Integer(31),"el numero de parámetros actuales no corresponde con los formales");        
        errorMap.put(new Integer(33),"se espera un identificado de procedimiento o := ausente");
        errorMap.put(new Integer(34),"el identificador no correspone a una entidad asignable");
        errorMap.put(new Integer(35),"se esperan operando de tipo entero o subrango");                
        errorMap.put(new Integer(36),"se esperan un identificador de Variable,  Constante, Funcion o Parametro");                
        errorMap.put(new Integer(37),"valor de tipo incompatible con la expresion en la sentencia case");             
        errorMap.put(new Integer(38),"subrango de valores invalido (el limite inferior debe ser menor al limite superior)");                   
        errorMap.put(new Integer(39),"Magnitud no representable");
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
