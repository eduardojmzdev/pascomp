package mVirtual.instrucciones;

import mVirtual.instrucciones.aritmeticas.InstruccionDivision;
import mVirtual.instrucciones.aritmeticas.InstruccionDivisionReal;
import mVirtual.instrucciones.aritmeticas.InstruccionModulo;
import mVirtual.instrucciones.aritmeticas.InstruccionMultiplicacion;
import mVirtual.instrucciones.aritmeticas.InstruccionResta;
import mVirtual.instrucciones.aritmeticas.InstruccionSuma;
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
import mVirtual.instrucciones.pila.InstruccionApila;
import mVirtual.instrucciones.pila.InstruccionApilaDireccion;
import mVirtual.instrucciones.pila.InstruccionApilaIndice;
import mVirtual.instrucciones.pila.InstruccionBurbuja;
import mVirtual.instrucciones.pila.InstruccionCargaCP;
import mVirtual.instrucciones.pila.InstruccionCopiaCima;
import mVirtual.instrucciones.pila.InstruccionDesapila;
import mVirtual.instrucciones.pila.InstruccionDesapilaDireccion;
import mVirtual.instrucciones.pila.InstruccionDesapilaIndice;
import mVirtual.instrucciones.pila.InstruccionDispose;
import mVirtual.instrucciones.pila.InstruccionFlip;
import mVirtual.instrucciones.saltos.InstruccionIrA;
import mVirtual.instrucciones.saltos.InstruccionIrF;
import mVirtual.instrucciones.saltos.InstruccionIrIndice;
import mVirtual.repertorio.instrucciones.*;
/**
 * La factoria de comandos propiamente dicha (Singleton) 
 *
 */
public class FactoriaComandosImp extends FactoriaComandos {
	
	/* (non-Javadoc)
	 * @see maquinaVirtual.repertorio.FactoriaComandos#generarComando(java.lang.String)
	 */
	public Instruccion generarComando(String comando){
		if (comando.equals("and"))
			return new InstruccionAnd();
		if (comando.equals("apila"))
			return new InstruccionApila();
		if (comando.equals("apilaDireccion"))
			return new InstruccionApilaDireccion();
		if (comando.equals("desapila"))
			return new InstruccionDesapila();
		if (comando.equals("desapilaDireccion"))
			return new InstruccionDesapilaDireccion();
		if (comando.equals("distinto"))
			return new InstruccionOr();
		if (comando.equals("divisionEntera"))
			return new InstruccionDivision();
		if (comando.equals("division"))
			return new InstruccionDivisionReal();
		if (comando.equals("igual"))
			return new InstruccionIgual();
		if (comando.equals("mayor"))
			return new InstruccionMayor();
		if (comando.equals("mayorIgual"))
			return new InstruccionMayorIgual();
		if (comando.equals("menor"))
			return new InstruccionMenor();
		if (comando.equals("menorIgual"))
			return new InstruccionMenorIgual();
		if (comando.equals("moduloEntero"))
			return new InstruccionModulo();
		if (comando.equals("producto"))
			return new InstruccionMultiplicacion();
		if (comando.equals("negacion"))
			return new InstruccionNot();
		if (comando.equals("or"))
			return new InstruccionOr();
		if (comando.equals("read"))
			return new InstruccionRead();
		if (comando.equals("resta"))
			return new InstruccionResta();
		if (comando.equals("suma"))
			return new InstruccionSuma();
		if (comando.equals("write"))
			return new InstruccionWrite();
		if (comando.equals("ir-a"))
			return new InstruccionIrA();
		if (comando.equals("ir-f"))
			return new InstruccionIrF();
		if (comando.equals("apilaIndice"))
			return new InstruccionApilaIndice();
		if (comando.equals("desapilaIndice"))
			return new InstruccionDesapilaIndice();
		if (comando.equals("new"))
			return new InstruccionNew();
		if (comando.equals("del"))
			return new InstruccionDispose();
		if (comando.equals("copia"))
			return new InstruccionCopiaCima();
		if (comando.equals("cargaCP"))
			return new InstruccionCargaCP();
		if (comando.equals("irIndice"))
			return new InstruccionIrIndice();
		if (comando.equals("flip"))
			return new InstruccionFlip();
		if (comando.equals("burbuja"))
			return new InstruccionBurbuja();
		return null;
	};
}
