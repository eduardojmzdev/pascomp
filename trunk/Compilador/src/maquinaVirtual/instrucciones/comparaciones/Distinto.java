package maquinaVirtual.instrucciones.comparaciones;


import java.util.EmptyStackException;

import maquinaVirtual.MaquinaVirtual;
import maquinaVirtual.instrucciones.Instruccion;
import excepciones.MVException;

public class Distinto implements Instruccion {

	/**
	 * Ejecuta la instruccion
	 * @throws MVException. Si hay un error en la ejecucion
	 */
	public void ejecutar() throws MVException {
		try {
			boolean opBooleanos =false;
			boolean aBool = false, bBool=false;
			int aInt=0, bInt=0;
			
			String bString = MaquinaVirtual.obtenerInstancia().getPila().pop();
			if(bString.equals("null")){
				throw new MVException(4);
			
			//comparacion entre booleanos
			}else if (bString.equals("TRUE")){
				opBooleanos = true;
				bBool = true;
			
			//comparacion entre booleanos
			}else if (bString.equals("FALSE")){
				opBooleanos = true;
				bBool  = false;
			
			//comparacion entre enteros
			}else{
				try{
					bInt = Integer.parseInt(bString);	
				} catch (NumberFormatException e) {
					throw new MVException(5);

				}
			}
			
			String aString = MaquinaVirtual.obtenerInstancia().getPila().pop();
			
			if(aString.equals("null")){
				throw new MVException(4);
				
			//comparacion entre booleanos
			}else if(opBooleanos){
				if (aString.equals("TRUE") ){
					aBool = true;
				}else if (aString.equals("FALSE")){
					aBool  = false;
				}else{
					throw new MVException(2);
				}
			}else{//comparacion entre enteros
				aInt = Integer.parseInt(aString);	
			}
			
			boolean c;
			if(opBooleanos)
				c = aBool != bBool;
			else
				c = aInt != bInt;
			if (c)
				MaquinaVirtual.obtenerInstancia().getPila().push(new String("TRUE"));
			else
				MaquinaVirtual.obtenerInstancia().getPila().push(new String("FALSE"));

		}catch (EmptyStackException e) {
			throw new MVException(3);

		} catch (NumberFormatException e) {
			throw new MVException(0);

		}

	}

	/**
	 * @return String: representa la instruccion
	 */
	public String toString() {
		return this.getClass().getSimpleName();
	}
	/**
	 * vacio
	 */
	public void setParam(String param) {}
}
