package main;

/**
 * COMPILADOR PLG 08/09
 * 
 * PLG UCM GRUPO 8
 * 
 * @version 1.0
 */
public class Main {
    public static void main(String[] args) {
	
	    Compilador compi= new Compilador();
    	Ventana ventana = new Ventana(compi);
		ventana.setTitle("PLG");
		ventana.setSize(700, 500);
		ventana.setVisible(true);
	
    }

}
