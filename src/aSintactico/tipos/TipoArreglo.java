package aSintactico.tipos;

import aSintactico.Entry;
import java.util.ArrayList;

/**
 * Representa al tipo array
 *
  * @see aSintactico.tipos.TipoFactory
 */
public final class TipoArreglo extends Tipo{
    
    /** Tipo de la expresion de subindice */
    public TipoSimple tipoDominio = null;
    
    /** tipo de las componentes */
    public TipoSimple tipoRango = null;
    
    /** Numero de componentes del arreglo */
    private int numComponents;
    
    /**
     * Constructor
     * @param tipoDom 
     * @param tipoRango 
     */
    public TipoArreglo(TipoSimple tipoDom, TipoSimple tipoRango) {
        super(Tipo.ARRAY);
        this.tipoDominio = tipoDom;
        this.tipoRango = tipoRango;
        if(tipoDom.esBoolean()) this.numComponents = 2;
        else this.numComponents = ((TipoSubrango)tipoDominio).getRango();  
    }
                           
    /**
     * Comprueba compatibilidad de tipos
     * 
     * @return true si los tipos son compatibles
     * @param tipo 
     */
    public boolean equivalenteCon(Tipo tipo) {  
        if(tipo.esArreglo()){
            TipoArreglo t = (TipoArreglo)tipo;
            if(tipoRango.equivalenteCon(t.tipoRango)&& numComponents == t.numComponents){                
                return tipoDominio.equivalenteCon(t.tipoDominio);
            }
        }
        return false;
    }

    /**
     * Calcula el tamaño del arreglo
     * @return tamaño del arreglo
     */
    public int getSize() {
        return numComponents * tipoRango.getSize();        
    }
    
    /**
     * @return numero de componentes del arreglo
     */
    public int getNumComponentes(){
        return this.numComponents;
    }
    
    /**
     * 
     * @return 
     */
    public String toString() {
        return "arreglo";
    }

    /**
     * 
     * @return 
     */
    public boolean esSimple() {
        return false;
    }

    /**
     * 
     * @return 
     */
    public boolean esEntero() {
        return false;
    }

    /**
     * 
     * @return 
     */
    public boolean esBoolean() {
        return false;
    }

    /**
     * 
     * @return 
     */
    public boolean esSubrango() {
        return false;
    }

    /**
     * 
     * @return 
     */
    public boolean esArreglo() {
        return true;
    }
}
