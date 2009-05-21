
package main;



/**
 * COMPILADOR PLG 08/09
 * 
 * PLG UCM
 * GRUPO 8
 * @version 1.0   	  
 */
public class Main {
    private String ficheroEntrada, ficheroSalida;
    public static void main(String[] args) {        
        
        if(args.length !=2){
            System.out.println("Modo de Uso: <ficheroEntrada> <ficheroSalida>");
        } else{
    		try {
    			Compilador compilador = new Compilador(args[0], args[1]);
    			compilador.ejecutar();
    			System.out.println("Finalizado");
    			
    		} catch (Exception e) {
    		e.printStackTrace();
    		}
        }
        
    }
    
}
