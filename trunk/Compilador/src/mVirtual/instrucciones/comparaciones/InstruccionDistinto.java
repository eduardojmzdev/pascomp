package mVirtual.instrucciones.comparaciones;


import java.util.EmptyStackException;

import mVirtual.MaquinaVirtual;
import mVirtual.instrucciones.Instruccion;
import excepciones.MVException;

/**
 * 
 * Esta clase implementa la ejecución en la pila de la instrucción distinto de la máquina virtual
 *
 */public class InstruccionDistinto implements Instruccion {

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
	/* (non-Javadoc)
	 * @see maquinaVirtual.repertorio.Instruccion#Ejecutar(java.util.Stack, java.util.Hashtable)
	 */
	public void Ejecutar() throws MVException {
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

	/* (non-Javadoc)
	 * @see maquinaVirtual.repertorio.Instruccion#getDatos()
	 */
	public String getDatos() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see maquinaVirtual.repertorio.Instruccion#setDatos(java.lang.String)
	 */
	public void setDatos(String datos) {
		// TODO Auto-generated method stub
		
	}

}
