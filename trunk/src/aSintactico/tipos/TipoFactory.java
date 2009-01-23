package aSintactico.tipos;

/**
 * Clase TipoFactory
 * Provee metodos para la creacion eficiente de tipos
 *
 * @author Agustin Ramone
 * @author Mayra Echandi
 */
public final class TipoFactory {
    /** Tipo integer*/
    private static final TipoSimple tipoInteger = new TipoSimple(Tipo.INTEGER);
    
    /** Tipo boolean*/
    private static final TipoSimple tipoBoolean = new TipoSimple(Tipo.BOOLEAN);
    
    /**
     * Permite construir un tipo simlpe
     * @param cod clase de tipo
     * @return un tipo simple
     */
    public static TipoSimple crearTipoSimple(int cod){
        if(cod == Tipo.BOOLEAN)
            return tipoBoolean;
        else if(cod == Tipo.INTEGER)
            return  tipoInteger;
        else 
            return null;    
    }    
    
    /**
     * Crea un tipo integer
     * @return un tipo integer
     */
    public static TipoSimple crearTipoEntero(){
        return tipoInteger;        
    }
    
    /**
     * crea un tipo boolean
     * @return un tipo boolean
     */
    public static TipoSimple crearTipoBooleano(){
        return tipoBoolean;        
    }
}
