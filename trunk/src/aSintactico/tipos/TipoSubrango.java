package aSintactico.tipos;

/**
 * Clase TipoSubrango
 *
  * @see aSintactico.tipos.TipoFactory
 */
public final class TipoSubrango extends TipoSimple {
    
    /** Limite inferior */
    private int li;
    
    /** Limite superior*/
    private int ls;
    
    /** Constructor */
    public TipoSubrango() {
        super(Tipo.SUBRANGO);
    }
            
    /**
     * Constructor     
     * @param li limite inferior
     * @param ls limite superior
     */
    public TipoSubrango(int li, int ls) {
        super(Tipo.SUBRANGO);
        this.li = li;
        this.ls = ls;
    }
    
    /**
     * 
     * @return el valor inferior del subrango
     */
    public int getValInferior(){
        return li;
    }
    
    /**
     * 
     * @return el valor superior del subrango
     */
    public int getValSuperior(){
        return ls;
    }
    
    /**
     * 
     * @return cantidad de valores soportado
     */
    public int getRango(){
        return ls -li +1;
    }
    
    /**
     * Chequea compatibilidad de tipos
     * @param t tipo a comparar
     * @return true si el tipo es compatible con el tipo t
     */
    @Override
	public boolean equivalenteCon(Tipo t) {
        if(t.esSubrango() || t.esEntero()){
            return true;
        }
        return false;
    }
    
    /**
     */
    @Override
	public String toString(){
        return "subrango";                
    }
    
    /***/
    @Override
	public boolean esSimple() {
        return true;
    }
    /***/
    @Override
	public boolean esEntero() {
        return false;
    }
    
    /***/
    @Override
	public boolean esBoolean() {
        return false;
    }
    /***/
    @Override
	public boolean esSubrango() {
        return true;
    }
    
    /***/
    @Override
	public boolean esArreglo() {
        return false;
    }
}
