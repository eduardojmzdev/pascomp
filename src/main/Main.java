
package main;

import aSintactico.AnalizadorSintactico;
import java.io.IOException;
import mepa.Mepa;

/**
 * COMPILADOR MINIPASCAL
 * 
 * Compiladores e Interpretes - Universidad Nacional del Sur - Bahia Blanca
 *
 * @version 1.0  ("Compiladores e Interpretes"- Universidad Nacional del Sur) 	  
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
