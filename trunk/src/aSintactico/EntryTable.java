
package aSintactico;

import aSintactico.tipos.*;

import java.util.Hashtable;
import java.util.LinkedList;

/**
 * Implementa la tabla de simbolos para almacenar objetos Entry
 * 
  *@author Agustin Ramone
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
        Entry e = new Entry("true",Entry.CONSTANTE);
        e.asignable = false;
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
        
        e = new Entry("maxint",Entry.CONSTANTE);
        e.asignable = false;
        e.tipo = tinteger;
        e.valor = Integer.MAX_VALUE;        
        e.valorStr = e.valor.toString();
        agregarConstante(e);
        
       //PROCEDIMIENTOS Y FUNCIONES PREDEFINIDAS
        LinkedList<Entry> l = new LinkedList<Entry>();
        Entry ep = agregarProcedimiento("write",l,null);
        ep.predefinido = true;
        
        ep = agregarProcedimiento("writeln",l,null);
        ep.predefinido = true;
        
        ep = agregarProcedimiento("read",l,null);
        ep.predefinido = true;
        
        ep = agregarProcedimiento("readln",l,null);
        ep.predefinido = true;
        
        Entry ef = agregarFuncion("succ",tinteger,null,l);
        ef.predefinido = true;
        
        ef = agregarFuncion("pred",tinteger,null,l);
        ef.predefinido = true;
        
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
     * Inserta una entrada que corresponde a una constante
     * @param id nombre de la constante
     * @param e entrada
     */
    public void agregarConstante(Entry e){
        e.asignable=false;
        inserta(e.nombre,e);
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
     * Agrega una declaración de procedimientos en el n.l actual y retorna la
     * entrada generada
     * @param id nombre del procedimiento
     * @param listaPar lista de parametros
     * @param etiqueta etiqueta de procedimiento
     * @return entrada creada
     */
    public Entry agregarProcedimiento(String id, LinkedList<Entry> listaPar, String etiqueta){
        Entry e = new Entry(id,Entry.PROCEDIMIENTO);
        e.etiqueta = etiqueta;
        e.listaParametros = listaPar;
        e.asignable=false;
        int tamPar=0;
        int size=listaPar.size();
        Entry entryPar;
                                
        for(int i=0;i<size;i++){ //calcula el espacio para los parámetros formales            
            entryPar= listaPar.get(i);            
            if(entryPar.porValor)
                tamPar += entryPar.tipo.getSize();
            else
                tamPar++;//las referencias ocupan 1 celda
            
        }
        e.sizeParametros = tamPar;
        
        inserta(id, e);
        return e;
    }
    
    
    /**
     * Inserta una lista de parametros en la tabla actual
     * Se asume que los parametros ya tienen su desplazamiento y nivel léxico
     * calculados
     * @param listaPar parametros formales
     */
    public void agregarParFormales(LinkedList<Entry> listaPar){
        int size=listaPar.size();
        Entry e;
        for(int i=0; i<size ; i++){
            e = listaPar.get(i);
            inserta(e.nombre, e);
        }
    }
    
    
    /**
     * 
     * Agrega una declaración de función en el n.l actual y retorna la entrada
     * agregada.OBS:no se calcula el nivel lexico ni el desplzameinto, ni tampoco
     * se setea asignable
     * @param id   nombre de funcion 
     * @param tipo tipo de la funcion
     * @param eti  etiqueta de la funcion
     * @param listaPar lista de parametros formales
     * @return entrada creada
     */
    public Entry agregarFuncion(String id, Tipo tipo, String eti, LinkedList<Entry> listaPar){
        Entry e;
        e = new Entry(id,Entry.FUNCION);
        e.tipo = tipo;
        e.etiqueta=eti;
        e.listaParametros = listaPar;
        int tamPar = 0;
        int size = listaPar.size();
        Entry entryPar;
        for(int i=0;i<size;i++){
            //calcula el espacio para los parámetros formales
            entryPar = listaPar.get(i);
            if(entryPar.porValor)
                tamPar += entryPar.tipo.getSize();
            else
                tamPar++; //las referencias ocupan 1 celda
        }
        e.sizeParametros = tamPar;
        inserta(id,e);
        return e;        
    }
    
    
    /**
     * Agrega a los parametros formales sus desplazamientos  negativos y
     * su nivel lexico como el actual ,retornando el desplazamiento base
     * del primer parametro de la lista ,e.d el que teine desplazamiento
     * negativo mas grande
     * @param listaPar parametros formales
     * @return desplazamiento del primer parametro
     */
    public int agregarDesplazamientos(LinkedList <Entry>listaPar){
        int base=-2; //tiene en cuenta el puntero de retorn y el valor del
        //display salvado por ENPR
        Entry e;
        int tam;
        int size = listaPar.size();
        for(int i=size-1; i>=0 ; i--){
            e = listaPar.get(i);
            if(e.porValor)
                tam = e.tipo.getSize();
            else //por referencia
                tam = 1;
            e.nivelLexico = getNivelLexico();
            base -= tam;
            e.desplazamiento = base;
        }
        return base;
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
    
    /**
     * Busca una entrada de constante
     * @param lex identificador de constante
     * @return la entrada para la constante o null si no existe     
     */
    public Entry buscarConstante(String lex){
        Entry entry = buscar(lex);
        return (entry!=null && entry.esConstante())? entry: null;        
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
