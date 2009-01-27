
package mepa;
/**
 * TokenMepa
 * Representa a un componente lexico del lenguaje
 
 */
public final class TokenMepa{
    /** Codigo que identifica univocamente a un token */
    public int codigo;
    
    /** Lexema */
    public String lexema;
    
    /** Numero de linea donde ocurre la aparicion del token */
    public int numLinea;
    
    /** HashMap de lexemas */
    private static java.util.HashMap<Integer,String>
            LEXEMAS = new java.util.HashMap<Integer,String>(54);
    
    /** CODIGOS DE TOKENS */
    public static final int EOF = -1;
    public static final int APCT =  0;
//aritmeticas
    public static final int SUMA =  1;
    public static final int SUST =  2;
    public static final int MULT =  3;
    public static final int DIVI =  4;
    public static final int MODU =  5;
    public static final int UMEN =  6;
//logicas
    public static final int CONJ =  7;
    public static final int DISJ =  8;
    public static final int NEGA =  9;
    public static final int CMME = 10;
    public static final int CMMA = 11;
    public static final int CMIG = 12;
    public static final int CMDG = 13;
    public static final int CMNI = 14;
    public static final int CMYI = 15;
//para case (duplicar tope)
    public static final int DUPT = 16;
//saltos
    public static final int DSVS = 17;
    public static final int DSVF = 18;
    
//entrada - salida
    public static final int LEER = 19;
    public static final int IMPR = 20;
    public static final int LELN = 21;
    public static final int IMLN = 22;
    
//variables
    public static final int APVL = 23;
    public static final int ALVL = 24;
    
//programas y procedimientos
    public static final int INPP = 25;
    public static final int ENPR = 26;
    public static final int LLPR = 27;
    public static final int RMEM = 28;
    public static final int LMEM = 29;
    public static final int RTPR = 30;
    
//pasaje de param por ref
    public static final int APDR = 31;
    public static final int APVI = 32;
    public static final int ALVI = 33;
    
//Arreglos
    public static final int APAR = 34;
    public static final int ALAR = 35;
    public static final int PUAR = 36;
    public static final int POAR = 37;
//Arreglos por ref
    public static final int APAI = 38;
    public static final int ALAI = 39;
    public static final int PUAI = 40;
    public static final int POAI = 41;
    public static final int APDC = 42;
//Arreglos: control de indice inferior y superior
    public static final int CONT = 43;
    
//Otras
    public static final int DIVC = 44;
    public static final int NADA = 45;
    public static final int PARA = 46;

    //Instrucciones nuevas:
    public static final int DSVV = 47;
    public static final int ALDR = 48;
    public static final int IMLV = 49;
    
    public static final int NUMERO = 50;
    public static final int MENOS = 51;
    public static final int ETIQ = 52;
    public static final int COMA = 53;
    
    /** Vector de instrucciones Mepa*/
    private static final String instrucciones []={
        "APCT","SUMA","SUST","MULT","DIVI","MODU","UMEN","CONJ","DISJ","NEGA","CMME","CMMA","CMIG","CMDG"
                ,"CMNI","CMYI","DUPT","DSVS","DSVF","LEER","IMPR","LELN","IMLN","APVL","ALVL","INPP","ENPR","LLPR"
                ,"RMEM","LMEM","RTPR","APDR","APVI","ALVI","APAR","ALAR","PUAR","POAR","APAI","ALAI","PUAI","POAI"
                ,"APDC","CONT","DIVC","NADA","PARA","DSVV","ALDR","IMLV"};
    
    /** Agrego los lexemas al HashMap */
    static{
        LEXEMAS.put(APCT,"APCT");
        LEXEMAS.put(SUMA,"SUMA");
        LEXEMAS.put(SUST,"SUST");
        LEXEMAS.put(MULT,"MULT");
        LEXEMAS.put(DIVI,"DIVI");
        LEXEMAS.put(MODU,"MODU");
        LEXEMAS.put(UMEN,"UMEN");
        LEXEMAS.put(CONJ,"CONJ");
        LEXEMAS.put(DISJ,"DISJ");
        LEXEMAS.put(NEGA,"NEGA");
        LEXEMAS.put(CMME,"CMME");
        LEXEMAS.put(CMMA,"CMMA");
        LEXEMAS.put(CMIG,"CMIG");
        LEXEMAS.put(CMDG,"CMDG");
        LEXEMAS.put(CMNI,"CMNI");
        LEXEMAS.put(CMYI,"CMYI");
        LEXEMAS.put(DUPT,"DUPT");
        LEXEMAS.put(DSVS,"DSVS");
        LEXEMAS.put(DSVF,"DSVF");
        LEXEMAS.put(LEER,"LEER");
        LEXEMAS.put(IMPR,"IMPR");
        LEXEMAS.put(LELN,"LELN");
        LEXEMAS.put(IMLN,"IMLN");
        LEXEMAS.put(APVL,"APVL");
        LEXEMAS.put(ALVL,"ALVL");
        LEXEMAS.put(INPP,"INPP");
        LEXEMAS.put(ENPR,"ENPR");
        LEXEMAS.put(LLPR,"LLPR");
        LEXEMAS.put(RMEM,"RMEM");
        LEXEMAS.put(LMEM,"LMEM");
        LEXEMAS.put(RTPR,"RTPR");
        LEXEMAS.put(APDR,"APDR");
        LEXEMAS.put(APVI,"APVI");
        LEXEMAS.put(ALVI,"ALVI");
        LEXEMAS.put(APAR,"APAR");
        LEXEMAS.put(ALAR,"ALAR");
        LEXEMAS.put(PUAR,"PUAR");
        LEXEMAS.put(POAR,"POAR");
        LEXEMAS.put(APAI,"APAI");
        LEXEMAS.put(ALAI,"ALAI");
        LEXEMAS.put(PUAI,"PUAI");
        LEXEMAS.put(POAI,"POAI");
        LEXEMAS.put(APDC,"APDC");
        LEXEMAS.put(CONT,"CONT");
        LEXEMAS.put(DIVC,"DIVC");
        LEXEMAS.put(NADA,"NADA");
        LEXEMAS.put(PARA,"PARA");
        LEXEMAS.put(EOF,"EOF");
        LEXEMAS.put(MENOS,"-");
        LEXEMAS.put(ETIQ,"ETIQ");
        LEXEMAS.put(COMA,",");
        LEXEMAS.put(DSVV,"DSVV");
        LEXEMAS.put(ALDR,"ALDR");
        LEXEMAS.put(IMLV,"IMLV");
    }
    
    /**
     * Constructor
     * @param cod codigo de token
     * @param nline numero de linea donde aparece
     */
    public TokenMepa(int cod, int nline){
        this.codigo = cod;
        if(LEXEMAS.get(cod)!=null){
            this.lexema = LEXEMAS.get(cod);
        }else
            this.lexema = "";
        this.numLinea = nline;
    }
    
    /**
     * Constructor
     * @param cod codigo del token
     * @param lex lexema del token
     * @param nline numero de linea donde aparece
     */
    public TokenMepa(int cod,String lex,int nline){
        this.codigo = cod;
        this.lexema = lex;
        this.numLinea = nline;
    }
    
    
    /** 
     * Dado un nombre de instruccion retorna su codigo asociado
     * @return  el codigo de la instruccion
     */
    public static Integer getCodigoInstruccion(String inst){
        for(int i = 0 ; i<instrucciones.length;i++){
            if(instrucciones[i].equals(inst)) return new Integer(i);
        }
        return null;
    }
    
    /**
     * 
     * @return true si el token corresponde a una instruccion
     */
    public boolean esInstruccion(){
        return getCodigoInstruccion(lexema.toUpperCase())!=null;
    }
    
    /** 
     * @return la representacion del token como string
     */
    public String toString(){
        String n="000"+numLinea;
        n = n.substring(n.length()-3);
        return "[linea "+n+", "+lexema+", "+LEXEMAS.get(codigo)+"]";
    }
    
}
