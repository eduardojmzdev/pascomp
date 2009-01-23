package aSintactico.tipos;

/**
 * Representa un tipo simple del lenguaje. Puede ser integer, boolean y subrango
  * @see aSintactico.tipos.TipoFactory
 */
public class TipoSimple extends Tipo{
            
   
    /**
     * Constructor 
     * @param clase clase de tipo 
     */
    public TipoSimple(int clase) {
        super(clase);
    }
        
    /**
     * Verifica si el tipo es compatible con otro
     * @param t tipo a chequear
     * @return true si son equivalentes, false en caso contrario
     */
    @Override
	public boolean equivalenteCon(Tipo t) {
        if(clase == t.clase)
            return true;
        else if((clase == INTEGER && t.clase == SUBRANGO) || (clase == SUBRANGO && t.clase == INTEGER))
            return true;
        else
            return false;
    }

    /**
     * Obtiene el tamaño del tipo en locaciones de memoria
     * @return un tamaño de 1 posicion
     */
    @Override
	public int getSize() {
        return 1;
    }

    /**
     * Obtiene la representacion del tipo como string
     * @return el string que representa el tipo
     */
    @Override
	public String toString(){
        if(clase == INTEGER) return "integer";
        if(clase == BOOLEAN) return "boolean";
        if(clase == SUBRANGO)return "subrango";        
        return "";
    }
    
    /**
     * 
     * @return 
     */
    @Override
	public boolean esSimple() {
        return true;
    }

    /**
     * 
     * @return 
     */
    @Override
	public boolean esEntero() {
        return clase == INTEGER;
    }

    /**
     * 
     * @return 
     */
    @Override
	public boolean esBoolean() {
        return clase == BOOLEAN;
    }

    /**
     * 
     * @return 
     */
    @Override
	public boolean esSubrango() {
        return clase == SUBRANGO;
    }

    /**
     * 
     * @return 
     */
    @Override
	public boolean esArreglo() {
        return false;
    }
}
