
package aSintactico;

import aSintactico.tipos.*;

import java.util.LinkedList;

/**
 * Implementa la tabla de simbolos para almacenar objetos Entry
 * 
 * @since jdk 1.5
 * @see aSintactico.AbstractSimbolTable
 * @see aSintactico.Entry
 */
public final class EntryTable extends AbstractSimbolTable<Entry>{
    
    /** Constructor */
    public EntryTable() {
        super();
        
    }

    /**
     * Implementacion del metodo init
     * Agrega los tipos, funciones y constantes predefinidas del lenguaje
     */
    public void init() {
        if(pila.size()>0) {
            pila.clear();
            if(tablaActual!=null) tablaActual.clear();
        }
        
        //crea una tabla con nivel lexico -1
        pushTable();
        nivelLexico = -1;
        
        //Tipos predefinidos
        Tipo tinteger = TipoFactory.crearTipoEntero();
        Tipo tboolean = TipoFactory.crearTipoBooleano();
        agregarTipo("integer",tinteger);
        agregarTipo("boolean",tboolean);
        
      //Constantes predefinidas 
        Entry e = new Entry("true",Entry.CONSTANTE); e.asignable = false;
        e.tipo = tboolean;
        e.valor = Boolean.TRUE;
        e.valorStr = "1";
        agregarConstante(e);
         e = new Entry("false",Entry.CONSTANTE);
        e.asignable = false;
        e.tipo = tboolean;
        e.valor = Boolean.FALSE;
        e.valorStr = "0";
        agregarConstante(e);
        
    }
    
    /**
     * Inserta una entrada que corresponde al nombre del programa
     * @param id nombre del programa
     */
    public void agregarPrograma(String id){
        Entry e = new Entry(id, Entry.PROGRAMA);
        e.asignable = false;
        inserta(id,e);
    }
    
    
    /**
     * Inserta un tipo en la tabla de simbolos
     * @param id nombre del tipo
     * @param tipo tipo
     */
    public void agregarTipo(String id, Tipo tipo){
        Entry e = new Entry(id,Entry.TIPO);
        e.tipo = tipo;
        e.asignable=false;
        inserta(id,e);
    }
    
    
    
    /**
     * agrega una lista de variables en a la TS en n.l actual con su
     * correspondiente desplazamiento no negativo dependiendo de las
     * llamadas anteriores a este metodo
     * @param listaID lista de identificadores de variables
     * @param tipo tipo de las variables
     */
    public void agregarVariables(LinkedList<String> listaID, Tipo tipo){
        int size = listaID.size();
        Entry e;
        String id;
        for(int i=0;i<size; i++){
            id = listaID.get(i);
            e = new Entry(id, Entry.VARIABLE);
            e.nivelLexico = getNivelLexico();
            e.desplazamiento = getOffset();
            e.tipo = tipo;
            e.asignable=true;
            //agrega la declaracion de la variable
            inserta(id,e);
            incOffset(tipo.getSize());            
        }
    }
    
    /** * Inserta una entrada que corresponde a una constante * @param id nombre de la constante * @param e entrada */ 
    public void agregarConstante(Entry e){ 
     e.asignable=false;
     inserta(e.nombre,e);
     } 
    /**
     * Busca una entrada de tipo     
     * @param lex 
     * @return la entrada para el tipo o null si no existe     
     */
    public Entry buscarTipo(String lex){
        Entry entry = buscar(lex);        
        return (entry!=null && entry.esTipo())?entry:null;        
    }
}
