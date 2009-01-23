package aSintactico.tipos;

/**
 * Clase TipoFactory
 * Provee metodos para la creacion eficiente de tipos
 *
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
        else if(cod == Tipo.SUBRANGO){
            return new TipoSubrango();
        }
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
    
    /**
     * Crea un tipo subrango
     * @param limInf limite inferior
     * @param limSup limite superior
     * @return un tipo subrango
     */
    public static TipoSubrango crearTipoSubrango(int limInf, int limSup){
        return new TipoSubrango(limInf,limSup);
    }
    
    /**
     * Crea un tipo array
     *
     * @param tipoD tipo de la expresion de subindice
     * @param tipoR tipo de las componentes
     * @return un tipo array
     */
    public static TipoArreglo crearTipoArreglo(TipoSimple tipoD, TipoSimple tipoR){
        return new TipoArreglo(tipoD,tipoR);
    }
}
