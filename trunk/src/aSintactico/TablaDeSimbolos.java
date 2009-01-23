package aSintactico;

import java.util.LinkedList;
import java.util.Hashtable;

/**
 * Es una plantilla generica para tablas de simbolos de lenguajes de programacion
 *
 * @since jdk 1.6
 */
public abstract class TablaDeSimbolos <T>{
    
    /**
     * Mantiene las tablas de simbolos de los distintos
     * ambientes de referenciamiento. Por ejemplo:
     * Al entrar a un procedimiento P se inserta una nueva
     * tabla para P donde mantiene sus datos locales.
     * Si un identificador no esta en la tabla actual (ej: variable
     * global), entonces se busca en las restantes tablas.
     */
    protected LinkedList <Hashtable<String,T>>pila = null;
    
    /**
     * Tabla de simbolos del ambiente de referenciamiento
     * actual.
     */
    protected Hashtable <String,T>tablaActual=null;
    
    /** Mantiene el nivel lexico */
    protected int nivelLexico = -1;
    
    /** Apuntador a la proxima entrada de la tabla actual */
    protected int offset = 0;
    
    /**
     * Crea la tabla de simbolos
     */
    public TablaDeSimbolos() {
        //construye la pila
        pila= new LinkedList <Hashtable<String,T>>();        
    }
    
    /**
     * Obtiene el nivel lexico actual
     *@return el nivel lexico actual
     */
    public int getNivelLexico(){
        return nivelLexico;
    }
    
    /**
     * 
     * @return el desplazamiento actual
     */
    public int getOffset(){
        return offset;
    }
    
    /**
     * Incrementa el desplazamiento actual con el valor  n 
     * @param n incremento
     */
    public void incOffset(int n){
        offset += n;
    }
    
    /**
     * Inicializa la tabla de simbolos global con los tipos , funciones
     * y constantes predefinidas que son propias del lenguaje.
     * Las subclases deben implementarlo
     */
    public abstract void init();
    
    /**
     * Busca una entrada para un identificador de la tabla actual
     * @param id el nombre a buscar
     * @return true si existe una entrada para el identificador
     */
    public boolean estaDeclarado(String id){
        return tablaActual.containsKey(id);
    }
    
    /**
     * Busca un elemento recorriendo toda la pila hasta encontrarlo o no.
     * @param lexema nombre de la entidad a buscar
     * @return la entrada de tipo T o null si no encuentra nada
     */
    public T buscar(String lexema){
        int size = pila.size();
        Hashtable<String,T> tabla;
        T entry = null;
        for (int i=0; i<size ; i++){
            tabla = pila.get(i);
            entry = tabla.get(lexema);
            if (entry != null)	break;
        }
        return entry;
    }
    
    /**
     * Inserta una entrada identificada por name
     * @param name nombre de la entidad 
     * @param entry entrada de la tabla de simbolos
     */
    public void inserta(String name, T entry){
        tablaActual.put(name, entry);
    }
    
    /**
     * Inserta en el tope de la pila una nueva tabla correspondiente
     * a un nuevo ambiente de referenciamiento.
     */
    public void crearNivelLexico(){        
        pushTable();
        nivelLexico++;
        offset = 0;        
    }
    
    /**
     * apila una nueva tabla sin modificar el nivel lexico
     */
    protected void pushTable(){
        tablaActual = new Hashtable<String,T>();
        pila.addFirst(tablaActual);        
    }
    
    /**
     * Elimina la tabla del tope de la pila y restaura el nivel
     * actual.
     */
    public void eliminarNivelLexico(){
        pila.removeFirst();
        tablaActual = pila.getFirst();
        nivelLexico--;
    }
}
