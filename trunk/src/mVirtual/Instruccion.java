
package mVirtual;
/**
 * Clase Instruccion
 * Representa a una instruccion MEPA
 * @see mepa.Mepa
 */
public final class Instruccion{
    /** Codigo de operacion (TokenMepa) */
    public int opc;
    
    /** Numero de linea */
    public int numLinea;
    
    /** Nombre de la instruccion */
    public String nombre;
    
    /** Primer operando */
    public int op1=-1;
    
    /** Segundo operando */
    public int op2;
    
    /** Tercer operando */
    public int op3=-1;
    
    /** Etiqueta */
    public String etiqueta;
    
    /**
     * Constructor
     * Crea ina instruccion sin operandos
     * @param opc codigo de instruccion
     * @param line numero de linea
     * @param nombre el nombre de la instruccion
     */
    public Instruccion(int opc, int line, String nombre){
        this.opc = opc;
        this.numLinea = line;
        this.nombre = nombre;
    }
    
    /**
     * Constructor
     * Crea una instruccion con un operando
     * @param opc codigo de instruccion
     * @param line numero de linea
     * @param nombre nombre de la instruccion
     * @param op1 operando
     */
    public Instruccion(int opc, int line, String nombre, int op1){
        this.opc = opc;
        this.numLinea = line;
        this.op1 = op1;
        this.nombre = nombre;
    }
    
    /**
     * Constructor
     * Crea una instruccion con dos operandos
     * @param opc codigo de instruccion
     * @param line numero de linea
     * @param nombre nombre de la instruccion
     * @param op1 primer operando
     * @param op2 segundo operando
     */
    public Instruccion(int opc,int line, String nombre, int op1, int op2){
        this.opc = opc;
        this.numLinea = line;
        this.op1 = op1;
        this.op2 = op2;
        this.nombre = nombre;
    }
    
    /**
     * Constructor
     * Crea una instruccion con tres operandos
     * @param opc codigo de instruccion
     * @param line numero de linea
     * @param nombre nombre de la instruccion
     * @param op1 primer operando
     * @param op2 segundo operando
     * @param op3 tercer operando
     */
    public Instruccion(int opc, int line, String nombre, int op1, int op2, int op3){
        this.opc = opc;
        this.numLinea = line;
        this.nombre = nombre;
        this.op1 = op1;
        this.op2 = op2;
        this.op3 = op3;
    }
    
    /**
     * Constructor
     * Crea una instruccion de salto
     * @param opc codigo de instruccion
     * @param line numero de linea
     * @param nombre nombre de la instruccion
     * @param etiqueta etiqueta
     */
    public Instruccion(int opc, int line, String nombre, String etiqueta){
        this.opc = opc;
        this.numLinea = line;
        this.nombre = nombre;
        this.etiqueta = etiqueta;
    }
    
    /** */
    @Override
	public String toString(){
        String n = "000" + numLinea;
        n = n.substring(n.length()-3);
        String txt = "["+n+"]"+nombre;
        if(etiqueta!=null && etiqueta.length()>0)
            txt += " "+etiqueta+"("+op1+")";
        else
            if(op1>=0){
            txt += " "+op1;
            txt += ", "+op2;
            if(op3>=0) txt += ", "+op3;
            }
        return txt;
    }
}

