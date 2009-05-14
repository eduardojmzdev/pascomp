/**
 * Paquete que engloba las distintas clases que componen el módulo de la
 * máquina virtual del compilador.
 */
package mVirtual;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Stack;

import utils.*;
import utils.error.Error;
import utils.error.TError;

/**
 * Clase que implementa la máquina virtual. Recibirá un conjunto de
 * instrucciones y deberá ejecutarlas una a una guardando los resultados
 * intermedios y finales.
 */
public class MaquinaVirtual {

	/**
	 * Pila de la máquina virtual.
	 */
	private Stack<String> pila;

	/**
	 * Tamaño máximo de la memoria.
	 */
	private final int maxMemoria = 1000;

	/**
	 * Apuntador a la primera posición libre de la memoria dinámica.
	 */
	private int H = 500;

	/**
	 * Tabla Hash que contiene la memoria de datos.
	 */
	private Hashtable<Integer, String> memoriaDatos;

	/**
	 * Contador de Programa. Número de instrucción que se está ejecutando.
	 */
	private int contadorPrograma;

	/**
	 * Conjunto de instrucciones que ejecutará la máquina virtual.
	 */
	private CodigoObjeto memoriaInstrucciones;

	/**
	 * Objeto Error para controlar los errores en tiempo de ejecución.
	 */
	private Error error;

	/**
	 * Posición de la cima de los registros de activación.
	 */
	private int c;

	/**
	 * Posición de la base del registro de activación activo.
	 */
	private int b;

	/**
	 * Constructora de la clase MaquinaVirtual.
	 * 
	 * @param instrucciones
	 *            Conjunto de intrucciones que ejecutará la máquina virtual.
	 */
	public MaquinaVirtual(CodigoObjeto instrucciones) {
		memoriaInstrucciones = new CodigoObjeto(instrucciones);
		resetear();
	}

	/**
	 * Método accesor del atributo memoriaDatos.
	 * 
	 * @return Hashtable con la memoria de datos (resultados).
	 */
	public Hashtable<Integer, String> getMemoriaDatos() {
		return memoriaDatos;
	}

	/**
	 * Método accesor del atributo error.
	 * 
	 * @return Objeto con la información acerca de un error en tiempo de
	 *         ejecución.
	 */
	public Error getError() {
		return error;
	}

	/**
	 * Ejecuta el listado de instrucciones que se encuentran en la memoria de
	 * instrucciones.
	 * 
	 * @return La información, esto es, el resultado referente a la ejecución.
	 */
	public TPasoApaso ejecutar() {
		while (contadorPrograma < memoriaInstrucciones.getCodigo().size()) {
			Instruccion aux = memoriaInstrucciones.getCodigo().get(
					contadorPrograma);
			contadorPrograma++;
			ejecutarInstruccion(aux);
		}
		TPasoApaso t = dameEstado();
		return t;
	}

	/**
	 * Ejecuta una instrucción del listado de instrucciones que se encuentran en
	 * la memoria de instrucciones.
	 * 
	 * @return La información, esto es, el resultado referente a la ejecución de
	 *         la instrucciión correspondiente.
	 */
	public TPasoApaso ejecutarPaso() {
		if (contadorPrograma < memoriaInstrucciones.getCodigo().size()) {
			Instruccion aux = memoriaInstrucciones.getCodigo().get(
					contadorPrograma);
			contadorPrograma++;
			ejecutarInstruccion(aux);
		}
		TPasoApaso t = dameEstado();
		return t;
	}

	/**
	 * Crea una nueva memoria de datos y pila, y pone el contador de programa a
	 * cero.
	 */
	public void resetear() {
		pila = new Stack<String>();
		memoriaDatos = new Hashtable<Integer, String>();
		contadorPrograma = 0;
		H = 500;
		error = null;
		b = 0;
		c = 0;
	}

	/**
	 * Ejecuta una instruccion del listado, identificandola mediante su nombre,
	 * mandandola ejecutar mendiante una de las funciones privadas de las
	 * operaciones.
	 * 
	 * @param ins
	 *            Instrucción a ejecutar actualmente.
	 */
	private void ejecutarInstruccion(Instruccion ins) {
		String nombre = ins.getNombre();
		if (nombre.equals("apila"))
			apila(ins.getValor());
		else if (nombre.equals("apilaDir"))
			apilaDir(Integer.valueOf(ins.getValor()));
		else if (nombre.equals("desapilaDir"))
			desapilaDir(Integer.valueOf(ins.getValor()));
		else if (nombre.equals("igualdad"))
			igualdad();
		else if (nombre.equals("desigualdad"))
			desigualdad();
		else if (nombre.equals("negacion"))
			negacion();
		else if (nombre.equals("mayor"))
			mayor();
		else if (nombre.equals("mayorIgual"))
			mayorIgual();
		else if (nombre.equals("menor"))
			menor();
		else if (nombre.equals("menorIgual"))
			menorIgual();
		else if (nombre.equals("suma"))
			suma();
		else if (nombre.equals("resta"))
			resta();
		else if (nombre.equals("or"))
			or();
		else if (nombre.equals("multiplica"))
			multiplica();
		else if (nombre.equals("divide"))
			divide();
		else if (nombre.equals("mod"))
			mod();
		else if (nombre.equals("and"))
			and();
		else if (nombre.equals("ir_a"))
			ir_a(Integer.valueOf(ins.getValor()));
		else if (nombre.equals("ir_f"))
			ir_f(Integer.valueOf(ins.getValor()));
		else if (nombre.equals("apilaInd"))
			apilaInd();
		else if (nombre.equals("desapilaInd"))
			desapilaInd();
		else if (nombre.equals("apilaH"))
			apilaH();
		else if (nombre.equals("incrementaH"))
			incrementaH(Integer.valueOf(ins.getValor()));
		else if (nombre.equals("llamada"))
			llamada();
		else if (nombre.equals("incrementaC"))
			incrementarC(Integer.valueOf(ins.getValor()));
		else if (nombre.equals("retorno"))
			retorno();
	}

	/**
	 * Apila dentro de la pila de la máquina virtual.
	 * 
	 * @param o
	 *            Objeto a apilar. Puede ser entero, CIERTO O FALSO.
	 */
	private void apila(String o) {
		pila.push(o);
	}

	/**
	 * Desapila dos objetos, String o enteros, y apila el resultado de su
	 * comparación por igualdad.
	 */
	private void igualdad() {
		String bObject = pila.pop();
		String aObject = pila.pop();
		boolean c = true;
		try {
			int numB = Integer.parseInt(bObject);
			int numA = Integer.parseInt(aObject);
			c = (numB == numA);
		} catch (NumberFormatException nFE) {
			boolean boolA, boolB;
			boolB = (bObject.equals("CIERTO"));
			boolA = (aObject.equals("CIERTO"));
			c = (boolA == boolB);
		}
		if (c) {
			pila.push(new String("CIERTO"));
		} else {
			pila.push(new String("FALSO"));
		}
	}

	/**
	 * Desapila dos Strings o enteros y apila CIERTO O FALSO dependiendo de si
	 * son distintos o no respectivamente.
	 */
	private void desigualdad() {
		String bObject = pila.pop();
		String aObject = pila.pop();
		boolean c = true;
		try {
			int numB = Integer.parseInt(bObject);
			int numA = Integer.parseInt(aObject);
			c = (numB != numA);
		} catch (NumberFormatException nFE) {
			boolean boolA, boolB;
			boolB = (bObject.equals("CIERTO"));
			boolA = (aObject.equals("CIERTO"));
			c = (boolA != boolB);
		}
		if (c) {
			pila.push(new String("CIERTO"));
		} else {
			pila.push(new String("FALSO"));
		}
	}

	/**
	 * Cambia el valor del booleano que se encuentra en la cima de la pila.
	 */
	private void negacion() {
		String aString = pila.pop();
		boolean a = (aString.equals("CIERTO"));
		if (!a) {
			pila.push(new String("CIERTO"));
		} else {
			pila.push(new String("FALSO"));
		}
	}

	/**
	 * Apila CIERTO si el 2º elemento desapilado (entero) es mayor que el 1º.
	 * Apila FALSO en caso contrario.
	 */
	private void mayor() {
		int b = Integer.parseInt(pila.pop());
		int a = Integer.parseInt(pila.pop());
		if (a > b) {
			pila.push(new String("CIERTO"));
		} else {
			pila.push(new String("FALSO"));
		}
	}

	/**
	 * Apila CIERTO si el 2º elemento desapilado (entero) es mayor o igual que
	 * el 1º. Apila FALSO en caso contrario.
	 */
	private void mayorIgual() {
		int b = Integer.parseInt(pila.pop());
		int a = Integer.parseInt(pila.pop());
		if (a >= b) {
			pila.push(new String("CIERTO"));
		} else {
			pila.push(new String("FALSO"));
		}
	}

	/**
	 * Apila CIERTO si el 2º elemento desapilado (entero) es menor que el 1º.
	 * Apila FALSO en caso contrario.
	 */
	private void menor() {
		int b = Integer.parseInt(pila.pop());
		int a = Integer.parseInt(pila.pop());
		if (a < b) {
			pila.push(new String("CIERTO"));
		} else {
			pila.push(new String("FALSO"));
		}
	}

	/**
	 * Apila CIERTO si el 2º elemento desapilado (entero) es menor o igual que
	 * el 1º. Apila FALSO en caso contrario.
	 */
	private void menorIgual() {
		int b = Integer.parseInt(pila.pop());
		int a = Integer.parseInt(pila.pop());
		if (a <= b) {
			pila.push(new String("CIERTO"));
		} else {
			pila.push(new String("FALSO"));
		}
	}

	/**
	 * Desapila dos enteros de la pila y apila el resultado de sumarlos.
	 */
	private void suma() {
		int b = Integer.parseInt(pila.pop());
		int a = Integer.parseInt(pila.pop());
		int c = (a + b);
		pila.push(String.valueOf(c));
	}

	/**
	 * Desapila dos enteros de la pila y apila el resultado de restarlos.
	 */
	private void resta() {
		int b = Integer.parseInt(pila.pop());
		int a = Integer.parseInt(pila.pop());
		int c = (a - b);
		pila.push(String.valueOf(c));
	}

	/**
	 * Desapila dos booleanos de la pila y apila el resultado de la operación OR
	 * lógica entre ambos.
	 */
	private void or() {
		String bString = pila.pop();
		boolean a, b;
		b = (bString.equals("CIERTO"));
		String aString = pila.pop();
		a = (aString.equals("CIERTO"));
		boolean c = a || b;
		if (c) {
			pila.push(new String("CIERTO"));
		} else {
			pila.push(new String("FALSO"));
		}
	}

	/**
	 * Desapila dos enteros de la cima de la pila y apila el resultado de
	 * multiplicarlos.
	 */
	private void multiplica() {
		int b = Integer.parseInt(pila.pop());
		int a = Integer.parseInt(pila.pop());
		int c = (a * b);
		pila.push(String.valueOf(c));
	}

	/**
	 * Desapila dos enteros de la cima de la pila y apila el resultado de
	 * dividirlos.
	 */
	private void divide() {
		int b = Integer.parseInt(pila.pop());
		int a = Integer.parseInt(pila.pop());
		int c;
		// División por cero
		if (b == 0) {
			c = 0;
			error = new Error(TError.EJECUCION, "División por 0.");
		} else {
			c = (a / b);
		}
		pila.push(String.valueOf(c));
	}

	/**
	 * Desapila dos enteros de la cima de la pila y apila el resultado de hacer
	 * la operación módulo entre ellos.
	 */
	private void mod() {
		int b = Integer.parseInt(pila.pop());
		int a = Integer.parseInt(pila.pop());
		int c;
		// Módulo cero
		if (b == 0) {
			c = 0;
			error = new Error(TError.EJECUCION, "Módulo 0.");
		} else {
			c = (a % b);
		}

		pila.push(String.valueOf(c));
	}

	/**
	 * Desapila dos booleanos de la pila y apila el resultado de la operación
	 * AND lógica entre ambos.
	 */
	private void and() {
		String bString = pila.pop();
		boolean a, b;
		b = (bString.equals("CIERTO"));
		String aString = pila.pop();
		a = (aString.equals("CIERTO"));
		boolean c = a && b;
		if (c) {
			pila.push(new String("CIERTO"));
		} else {
			pila.push(new String("FALSO"));
		}
	}

	/**
	 * Realiza un salto incondicional a la direccion indicada.
	 * 
	 * @param dir
	 *            valor del nuevo contador de programa.
	 */
	private void ir_a(Integer dir) {
		contadorPrograma = dir;
	}

	/**
	 * Realiza un salto condicional si falso a la direccion indicada.
	 * 
	 * @param dir
	 *            valor del nuevo contador de programa.
	 */
	private void ir_f(Integer dir) {
		String bString = pila.pop();
		boolean a = bString.equals("FALSO");
		if (a) {
			contadorPrograma = dir;
		}
	}

	/**
	 * Apila el valor de H (la dirección de memoria del primer hueco libre de la
	 * memoria dinámica).
	 */
	private void apilaH() {
		String bString = String.valueOf(H);
		pila.push(bString);
	}

	/**
	 * Incrementa el valor del registro H en X unidades.
	 * 
	 * @param X
	 *            Unidades de incremento del apuntador a la pila de memoria
	 *            dinámica.
	 */
	private void incrementaH(int x) {
		if ((H + x) >= maxMemoria) {
			error = new Error(TError.EJECUCION,
					"Memoria dinámica insuficiente.");
		} else {
			H += x;
		}
	}

	/**
	 * Método que se usar para llamar a un procedimiento.
	 */
	private void llamada() {
		int etq = Integer.parseInt(pila.pop());
		int np = Integer.parseInt(pila.pop());
		// instruccion siguiente
		memoriaDatos.put(c + 2, Integer.toString(contadorPrograma));
		contadorPrograma = etq;
		// enlace dinamico
		memoriaDatos.put(c + 1, Integer.toString(b));
		String base = String.valueOf(b);
		while (np > 0) {
			base = memoriaDatos.get(Integer.parseInt(base));
			np -= 1;
		}
		// enlace estatico
		memoriaDatos.put(c, base);
		b = c;
	}

	/**
	 * Incrementamos C en un valor igual al número de posiciones de memoria
	 * necesarias para guardar la información de control ( 3 en el caso del
	 * modelo de enlaces).
	 */
	private void incrementarC(Integer numPos) {
		c += numPos.intValue();
	}

	/**
	 * Restaura el estado de la máquina cuando finaliza una función. Copia B en
	 * C. Copia en EnlaceDin en B. Copia la InSig en el CP.
	 */
	private void retorno() {
		c = b;
		b = Integer.parseInt(memoriaDatos.get(c + 1));
		contadorPrograma = Integer.parseInt(memoriaDatos.get(c + 2));
	}

	/**
	 * Apila una dirección de memoria.
	 * 
	 * @param dir
	 *            Dirección de memoria a apilar. Ha de ser entero positivo.
	 */
	private void apilaDir(Integer dir) {
		int nv = Integer.parseInt(pila.pop());
		dir = Integer.valueOf(dir);
		String base = String.valueOf(b);
		while (nv > 0) {
			base = memoriaDatos.get(Integer.parseInt(base));
			nv -= 1;
		}
		pila.push(memoriaDatos.get(Integer.parseInt(base)) + dir);
	}

	/**
	 * Apila lo que halla en la memoria de datos en la dirección cuyo valor
	 * sacamos al desapilar la pila (cima).
	 */
	private void apilaInd() {
		int nv = Integer.parseInt(pila.pop());
		int dir = Integer.parseInt(pila.pop());
		String base = String.valueOf(b);
		while (nv > 0) {
			base = memoriaDatos.get(Integer.parseInt(base));
			nv -= 1;
		}
		pila.push(memoriaDatos.get(Integer.parseInt(base) + dir));

	}

	/**
	 * Desapila una dirección de memoria.
	 * 
	 * @param dir
	 *            Dirección de memoria a desapilar. Ha de ser entero positivo.
	 */
	private void desapilaDir(Integer dir) {
		int nv = Integer.parseInt(pila.pop());
		dir = Integer.valueOf(dir);
		String base = String.valueOf(b);
		while (nv > 0) {
			base = memoriaDatos.get(Integer.parseInt(base));
			nv -= 1;
		}
		memoriaDatos.put(Integer.parseInt(base) + dir, pila.pop());
	}

	/**
	 * Guardamos en la memoria, en la dirección que indique la cima de la pila,
	 * justo lo que hay debajo de la cima.
	 */
	private void desapilaInd() {
		String aux = pila.pop();
		int nv = Integer.parseInt(pila.pop());
		int dir = Integer.parseInt(pila.pop());
		String base = String.valueOf(b);
		while (nv > 0) {
			base = memoriaDatos.get(Integer.parseInt(base));
			nv -= 1;
		}
		memoriaDatos.put(Integer.parseInt(base) + dir, aux);
	}

	/**
	 * Devuelve el estado del contador del programa, la memoria de datos, la
	 * pila para actualizar la vista.
	 * 
	 * @return El objeto TResultado contiene la información necesaria para
	 *         actualizar la vista
	 */
	private TPasoApaso dameEstado() {
		// Inicializo las variables del objeto TResultado
		boolean fin = contadorPrograma >= memoriaInstrucciones.getCodigo()
				.size();
		ArrayList<String> mem = new ArrayList<String>();
		ArrayList<String> pilaAux = new ArrayList<String>();

		// Se hallan todos los elementos de la pila
		Enumeration<String> es = pila.elements();
		while (es.hasMoreElements()) {
			pilaAux.add(es.nextElement());
		}

		// Se extraen todos los elementos de la memoria de datos
		Iterator<Integer> ks = memoriaDatos.keySet().iterator();
		int size = 0;
		int p = 0;
		while (ks.hasNext()) {
			int v = ks.next();
			if (v > size)
				size = v;
		}
		boolean cAparece = false;
		while (p <= size) {
			String cadena = memoriaDatos.get(p);
			if (cadena == null)
				cadena = "null";
			if (p == b && p == c) {
				mem.add(cadena + " <- b,c");
				cAparece = true;
			} else if (p == b)
				mem.add(cadena + " <- b");
			else if (p == c) {
				mem.add(cadena + " <- c");
				cAparece = true;
			} else
				mem.add(cadena);
			p++;
		}
		if (!cAparece) {
			for (int j = size + 1; j < c; j++) {
				mem.add(j, "null");
			}
			mem.add(c, "null <- c");
		}
		// Se crea el objeto y se devuelve.
		return new TPasoApaso(fin, contadorPrograma, pilaAux, mem);
	}
}
