
package main;


/**
 * COMPILADOR PLG 08/09
 * 
 * PLG UCM
 * GRUPO 8
 * @version 1.0   	  
 */
public class Main {
       
    public static void main(String[] args) {        
        
        if(args.length ==0){
            System.out.println("Modo de Uso: <program name> <in_file>");
        } else{
            Testeador test = new Testeador(args[0],new MiniPas());
            test.start();            
        }
        
    }
    
}
