package maquinaVirtual.instrucciones;

import maquinaVirtual.instrucciones.booleanas.*;
import maquinaVirtual.instrucciones.aritmeticas.*;
import maquinaVirtual.instrucciones.comparaciones.*;
import maquinaVirtual.instrucciones.entradasalida.*;
import maquinaVirtual.instrucciones.memoria.*;
import maquinaVirtual.instrucciones.pila.*;
import maquinaVirtual.instrucciones.saltos.*;

public class GeneradorInstrImp extends GeneradorInstr {
	
	/**
	 * Genera una instruccion 
	 * @param nombre String: nombre de la instruccion a generar
	 * @return Instruccion
	 */
	public Instruccion generaInstr(String nombre){
		if (nombre.equals("and"))
			return new And();
		if (nombre.equals("apila"))
			return new Apila();
		if (nombre.equals("apilaDireccion"))
			return new ApilaDir();
		if (nombre.equals("desapilaDireccion"))
			return new DesapilaDir();
		if (nombre.equals("distinto"))
			return new Distinto();
		if (nombre.equals("division"))
			return new Division();
		if (nombre.equals("igual"))
			return new Igual();
		if (nombre.equals("mayor"))
			return new Mayor();
		if (nombre.equals("mayorIgual"))
			return new MayorIgual();
		if (nombre.equals("menor"))
			return new Menor();
		if (nombre.equals("menorIgual"))
			return new MenorIgual();
		if (nombre.equals("moduloEntero"))
			return new Modulo();
		if (nombre.equals("producto"))
			return new Mult();
		if (nombre.equals("negacion"))
			return new Not();
		if (nombre.equals("or"))
			return new Or();
		if (nombre.equals("read"))
			return new Read();
		if (nombre.equals("resta"))
			return new Resta();
		if (nombre.equals("restaUnario"))
			return new RestaUnario();
		if (nombre.equals("suma"))
			return new Suma();
		if (nombre.equals("sumaUnario"))
			return new SumaUnario();
		if (nombre.equals("write"))
			return new Write();
		if (nombre.equals("ir-a"))
			return new IrA();
		if (nombre.equals("ir-f"))
			return new IrF();
		if (nombre.equals("apilaIndice"))
			return new ApilaIndice();
		if (nombre.equals("desapilaIndice"))
			return new DesapilaIndice();
		if (nombre.equals("new"))
			return new New();
		if (nombre.equals("del"))
			return new Dispose();
		if (nombre.equals("copia"))
			return new CopiaCima();
		if (nombre.equals("cargaCP"))
			return new CargaCP();
		if (nombre.equals("irIndice"))
			return new IrIndice();
		if (nombre.equals("flip"))
			return new Flip();
		if (nombre.equals("burbuja"))
			return new Burbuja();
		return null;
	};
}
