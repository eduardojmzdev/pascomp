package aSintactico.tipos;

/**
 * Clase abstracta para representar los tipos del lenguaje
  * @see aSintactico.tipos.TipoFactory
 */
public abstract class Tipo {
    
    private static final int VOID = -1;
    
    public static final int INTEGER = 0;
    
    public static final int BOOLEAN = 1;
    
    public static final int SUBRANGO = 2;
    
    public static final int ARRAY   = 3;
    
    protected int clase = VOID;
    
    /**
     * Constructor
     *
     */
    public Tipo(int clase){
        this.clase = clase;
    }
    
    /**
     * Chequea si el tipo es simple 
     * @return true si el tipo es simple
     */
    public abstract boolean esSimple();
    
    /**
     * Chequea si el tipo es entero
     * @return true si el tipo es entero
     */
    public abstract boolean esEntero();
        
    /**
     * Chequea si el tipo es boolean
     * @return true si el tipo es boolean
     */
    public abstract boolean esBoolean();
    
    /**
     * Chequea si el tipo es subrango
     * @return true si el tipo es subrango
     */
    public abstract boolean esSubrango();
    
    /**
     * Chequea si el tipo es array
     * @return true si el tipo es arreglo
     */
    public  abstract boolean esArreglo();
            
    /**
     * Chequea si el tipo actual es compatible con el tipo t
     * @param t tipo a comparar
     * @return true si son tipos compatibles
     */
    public abstract boolean equivalenteCon(Tipo t);    
    
    /**
     * Calcula el tamaño del tipo
     * @return el tamaño del tipo en loc. de memoria
     */
    public abstract int getSize();
    
    /**
     * @return  la representacion del tipo como string
     */
    @Override
	public abstract String toString();
    
}
