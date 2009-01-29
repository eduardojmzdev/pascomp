package mVirtual;

import excepciones.MaquinaVirtualException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Stack;

public class MaquinaVirtual {

    /**     
     * se usa para leer 
     */
    private int ultCarLeido = ' ';
    
    /** Pila */
    private Stack<String> pila;

    /**
     * Tabla Hash que contiene la memoria de datos.
     */
    private Hashtable<Integer, String> memoriaDatos;


    //Nombre del fichero que almacena el código objeto
    private String codigoObjeto;

    // Constructor
    public MaquinaVirtual(String archivo) throws Exception {
        setFichero(archivo);
        inicializar();
    }

    //Crea una nueva memoria de datos y pila, y pone el contador de programa a cero.
    public void inicializar() {
        pila = new Stack<String>();
        memoriaDatos = new Hashtable<Integer, String>();
    }

    public void setFichero(String archivo) throws IOException {
        codigoObjeto = archivo;
    }

    public ArrayList obtenerInstrucciones() {
        ArrayList temp = new ArrayList();
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            archivo = new File(codigoObjeto);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            while ((linea = br.readLine()) != null) {
                temp.add(eliminaBlancos(linea));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta
            // una excepcion.
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return temp;
    }

    /**
     * Metodo principal
     * Toma una instruccion y la ejecuta
     * 
     * @throws java.lang.Exception si ocurre algun error
     */
    public void run() throws Exception {
        //Carga las instrucciones
        try {
            ArrayList<String> aux = obtenerInstrucciones();

            String inst = null;
            int i = 0;
            do {
                inst = aux.get(i);
                ejecutar(inst);
                if (pila.size()>0){
                    for (int z=pila.size()-1;z>=0;z--)
                        System.out.println(pila.elementAt(z));
                    System.out.println("-------------------------");
                }
                    
                i++;
            } while (!(eliminaBlancos(inst)).equals("stop") || i < aux.size());

        } catch (java.lang.OutOfMemoryError ex) {
            throw new Exception(ex);
        }
    }

    private String eliminaBlancos(String instruccion){
        char letra=instruccion.charAt(0);
        int i=0;
        while (letra==' '){
            i++;
            letra=instruccion.charAt(i);
        }
        String temp = instruccion.substring(i);
        return temp;
    }

    private String argumento(String instruccion) {
        
        char letra=instruccion.charAt(0);
        int i=0;
        while (letra==' '){
            i++;
            letra=instruccion.charAt(i);
        }
        String temp = instruccion.substring(i);
        String[] array = temp.split(" ");
        temp = array[1];

        return temp;
    }

    /**
     * Ejecuta una instruccion
     *      
     * @param instruccion instruccion a ejecutar
     * @throws java.lang.Exception si hubo error
     */
    private void ejecutar(String instruccion) throws Exception {
   
            //Apila un valor en la cima de la pila.
            //Puede ser un entero o un booleano.
            if (instruccion.contains("apila") && !instruccion.contains("desapila_dir") && !instruccion.contains("apila_dir")) {

                pila.push(argumento(instruccion));

            //Apila una dirección de memoria en la cima de la pila.
            } else if (!instruccion.contains("desapila_dir") && instruccion.contains("apila_dir")) {

                pila.push(memoriaDatos.get(Integer.valueOf(argumento(instruccion))));

            } else if (instruccion.contains("desapila_dir")) {
          
                memoriaDatos.put(Integer.valueOf(argumento(instruccion)), pila.pop());

            } else if (instruccion.equals("resta")) {

                int op1 = Integer.parseInt(pila.pop());
                int op2 = Integer.parseInt(pila.pop());
                int resultado = (op2 - op1);
                pila.push(String.valueOf(resultado));

            } else if (instruccion.equals("suma")) {

                int op1 = Integer.parseInt(pila.pop());
                int op2 = Integer.parseInt(pila.pop());
                int resultado = (op2 + op1);
                pila.push(String.valueOf(resultado));

            } else if (instruccion.equals("or")) {

                boolean op1, op2, resultado;
                String temp;

                temp = pila.pop();
                if (temp.equals("true")) {
                    op1 = true;
                } else {
                    op1 = false;
                }

                temp = pila.pop();
                if (temp.equals("true")) {
                    op2 = true;
                } else {
                    op2 = false;
                }

                resultado = op1 || op2;
                if (resultado) {
                    pila.push(new String("true"));
                } else {
                    pila.push(new String("false"));
                }

            } else if (instruccion.equals("multiplica")) {

                int op1 = Integer.parseInt(pila.pop());
                int op2 = Integer.parseInt(pila.pop());
                int resultado = (op2 * op1);
                pila.push(String.valueOf(resultado));

            } else if (instruccion.equals("divide")) {

                int op1 = Integer.parseInt(pila.pop());
                int op2 = Integer.parseInt(pila.pop());
                int resultado;
                // Lanzamos un error si es una división por cero
                if (op1 == 0) {
                    throw new MaquinaVirtualException(0);
                } else {
                    resultado = (op2 / op1);
                }
                pila.push(String.valueOf(resultado));

            } else if (instruccion.equals("and")) {
                boolean op1, op2, resultado;
                String temp;

                temp = pila.pop();
                if (temp.equals("true")) {
                    op1 = true;
                } else {
                    op1 = false;
                }

                temp = pila.pop();
                if (temp.equals("true")) {
                    op2 = true;
                } else {
                    op2 = false;
                }

                resultado = op1 && op2;
                if (resultado) {
                    pila.push(new String("true"));
                } else {
                    pila.push(new String("false"));
                }
            } else if (instruccion.equals("not")) {

                boolean op1, resultado;
                String temp;

                temp = pila.pop();
                if (temp.equals("true")) {
                    op1 = true;
                } else {
                    op1 = false;
                }

                resultado = !op1;
                if (resultado) {
                    pila.push(new String("true"));
                } else {
                    pila.push(new String("false"));
                }
            } else if (instruccion.equals("positivo")) {

                int resultado;
                int num = Integer.valueOf(pila.pop());
                if (num >= 0) {
                    resultado = num;
                } else {
                    resultado = -num;
                }
                pila.push(String.valueOf(resultado));

            } else if (instruccion.equals("negativo")) {

                int resultado;
                int num = Integer.valueOf(pila.pop());
                if (num <= 0) {
                    resultado = num;
                } else {
                    resultado = -num;
                }
                pila.push(String.valueOf(resultado));

            } else if (instruccion.equals("mayor")) {

                String temp;
                temp = pila.pop();
                int a, b;

                b = Integer.parseInt(temp);
                a = Integer.parseInt(temp);

                if (a > b) {
                    pila.push(new String("true"));
                } else {
                    pila.push(new String("false"));
                }

            } else if (instruccion.equals("menor")) {

                String temp;
                temp = pila.pop();
                int a, b;

                b = Integer.parseInt(temp);
                a = Integer.parseInt(temp);

                if (a < b) {
                    pila.push(new String("true"));
                } else {
                    pila.push(new String("false"));
                }

            } else if (instruccion.equals("mayor_igual")) {

                String temp;
                temp = pila.pop();
                int a, b;

                b = Integer.parseInt(temp);
                a = Integer.parseInt(temp);

                if (a >= b) {
                    pila.push(new String("true"));
                } else {
                    pila.push(new String("false"));
                }

            } else if (instruccion.equals("menor_igual")) {

                String temp;
                temp = pila.pop();
                int a, b;

                b = Integer.parseInt(temp);
                a = Integer.parseInt(temp);

                if (a <= b) {
                    pila.push(new String("true"));
                } else {
                    pila.push(new String("false"));
                }

            } else if (instruccion.equals("igual")) {
                String temp;
                temp = pila.pop();
                if (temp.equals("true") || temp.equals("false")) {
                    String temp1 = pila.pop();
                    if (temp1.equals("true") || temp1.equals("false")) {
                        if (temp.equals(temp1)) {
                            pila.push(new String("true"));
                        } else {
                            pila.push(new String("false"));
                        }
                    }
                } else {
                    int a, b;

                    b = Integer.parseInt(temp);
                    a = Integer.parseInt(pila.pop());

                    if (a == b) {
                        pila.push(new String("true"));
                    } else {
                        pila.push(new String("false"));
                    }
                }
            } else if (instruccion.equals("distinto")) {
                String temp;
                temp = pila.pop();
                if (temp.equals("true") || temp.equals("false")) {
                    String temp1 = pila.pop();
                    if (temp1.equals("true") || temp1.equals("false")) {
                        if (!temp.equals(temp1)) {
                            pila.push(new String("true"));
                        } else {
                            pila.push(new String("false"));
                        }
                    }
                } else {
                    int a, b;

                    b = Integer.parseInt(temp);
                    a = Integer.parseInt(pila.pop());

                    if (a != b) {
                        pila.push(new String("true"));
                    } else {
                        pila.push(new String("false"));
                    }
                }

            } else if (instruccion.equals("lee")) {

                int valLeido = 0;
                int dig = ultCarLeido;
                boolean nega = false;
                //valor leido en representacion long
                long valLongLeido = 0;
                //valor aboluto mayor permitido,
                long valLongPer = Integer.MAX_VALUE;

                //MAXINT o MAXINT +1 si es negativo
                while (dig == '\r' || dig == '\n' || dig == '\t' || dig == ' ' || dig == '+') {
                    dig = System.in.read();
                    ultCarLeido = dig;
                }

                if (dig == '-') {
                    nega = true;
                    valLongPer++; //MAXIXT +1;
                    dig = System.in.read();
                    ultCarLeido = dig;
                }

                if (dig > 57 || dig < 48)//ERROR -Si despues de leer el menos no viene un digito
                {
                    throw new MaquinaVirtualException(1);
                }

                while (dig >= 48 && dig <= 57) {
                    valLongLeido = (valLongLeido * 10) + (dig - 48);
                    valLeido = (valLeido * 10) + (dig - 48);
                    if (valLongLeido > valLongPer) //ERROR overflow
                    {
                        throw new MaquinaVirtualException(2);
                    }
                    dig = System.in.read();
                    ultCarLeido = dig;
                }


                if (nega) {
                    valLeido = -valLeido;
                }
                pila.push(String.valueOf(valLeido));
     
            } else if (instruccion.equals("escribe")) {
                System.out.println(pila.pop());
            }


        
    }
}
