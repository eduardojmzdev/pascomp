package mVirtual.instrucciones;

import mVirtual.instrucciones.aritmeticas.InstruccionDivisionReal;
import mVirtual.instrucciones.aritmeticas.InstruccionModulo;
import mVirtual.instrucciones.aritmeticas.InstruccionMultiplicacion;
import mVirtual.instrucciones.aritmeticas.InstruccionResta;
import mVirtual.instrucciones.aritmeticas.InstruccionRestaUnario;
import mVirtual.instrucciones.aritmeticas.InstruccionSuma;
import mVirtual.instrucciones.aritmeticas.InstruccionSumaUnario;
import mVirtual.instrucciones.bool.InstruccionAnd;
import mVirtual.instrucciones.bool.InstruccionNot;
import mVirtual.instrucciones.bool.InstruccionOr;
import mVirtual.instrucciones.comparaciones.InstruccionIgual;
import mVirtual.instrucciones.comparaciones.InstruccionMayor;
import mVirtual.instrucciones.comparaciones.InstruccionMayorIgual;
import mVirtual.instrucciones.comparaciones.InstruccionMenor;
import mVirtual.instrucciones.comparaciones.InstruccionMenorIgual;
import mVirtual.instrucciones.entradasalida.InstruccionRead;
import mVirtual.instrucciones.entradasalida.InstruccionWrite;
import mVirtual.instrucciones.memoria.InstruccionCargaCP;
import mVirtual.instrucciones.memoria.InstruccionDispose;
import mVirtual.instrucciones.memoria.InstruccionNew;
import mVirtual.instrucciones.pila.InstruccionApila;
import mVirtual.instrucciones.pila.InstruccionApilaDireccion;
import mVirtual.instrucciones.pila.InstruccionApilaIndice;
import mVirtual.instrucciones.pila.InstruccionBurbuja;
import mVirtual.instrucciones.pila.InstruccionCopiaCima;
import mVirtual.instrucciones.pila.InstruccionDesapilaDireccion;
import mVirtual.instrucciones.pila.InstruccionDesapilaIndice;
import mVirtual.instrucciones.pila.InstruccionFlip;
import mVirtual.instrucciones.saltos.InstruccionIrA;
import mVirtual.instrucciones.saltos.InstruccionIrF;
import mVirtual.instrucciones.saltos.InstruccionIrIndice;

public class GeneradorInstrImp extends GeneradorInstr {
	
	/**
	 * Genera una instruccion 
	 * @param nombre String: nombre de la instruccion a generar
	 * @return Instruccion
	 */
	public Instruccion generaInstr(String nombre){
		if (nombre.equals("and"))
			return new InstruccionAnd();
		if (nombre.equals("apila"))
			return new InstruccionApila();
		if (nombre.equals("apilaDireccion"))
			return new InstruccionApilaDireccion();
		if (nombre.equals("desapilaDireccion"))
			return new InstruccionDesapilaDireccion();
		if (nombre.equals("distinto"))
			return new InstruccionOr();
		if (nombre.equals("division"))
			return new InstruccionDivisionReal();
		if (nombre.equals("igual"))
			return new InstruccionIgual();
		if (nombre.equals("mayor"))
			return new InstruccionMayor();
		if (nombre.equals("mayorIgual"))
			return new InstruccionMayorIgual();
		if (nombre.equals("menor"))
			return new InstruccionMenor();
		if (nombre.equals("menorIgual"))
			return new InstruccionMenorIgual();
		if (nombre.equals("moduloEntero"))
			return new InstruccionModulo();
		if (nombre.equals("producto"))
			return new InstruccionMultiplicacion();
		if (nombre.equals("negacion"))
			return new InstruccionNot();
		if (nombre.equals("or"))
			return new InstruccionOr();
		if (nombre.equals("read"))
			return new InstruccionRead();
		if (nombre.equals("resta"))
			return new InstruccionResta();
		if (nombre.equals("restaUnario"))
			return new InstruccionRestaUnario();
		if (nombre.equals("suma"))
			return new InstruccionSuma();
		if (nombre.equals("sumaUnario"))
			return new InstruccionSumaUnario();
		if (nombre.equals("write"))
			return new InstruccionWrite();
		if (nombre.equals("ir-a"))
			return new InstruccionIrA();
		if (nombre.equals("ir-f"))
			return new InstruccionIrF();
		if (nombre.equals("apilaIndice"))
			return new InstruccionApilaIndice();
		if (nombre.equals("desapilaIndice"))
			return new InstruccionDesapilaIndice();
		if (nombre.equals("new"))
			return new InstruccionNew();
		if (nombre.equals("del"))
			return new InstruccionDispose();
		if (nombre.equals("copia"))
			return new InstruccionCopiaCima();
		if (nombre.equals("cargaCP"))
			return new InstruccionCargaCP();
		if (nombre.equals("irIndice"))
			return new InstruccionIrIndice();
		if (nombre.equals("flip"))
			return new InstruccionFlip();
		if (nombre.equals("burbuja"))
			return new InstruccionBurbuja();
		return null;
	};
}
