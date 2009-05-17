package comunicacion;

import comunicacion.transfers.Transfer;
/**
 * Comportamiento que deberan implementar los diversos modelos que
 * quieran usar esta aplicacion
 * 
 *
 */
public interface Modelo {

	/**
	 * Ordena ejecutar la MV
	 * @param trans Transfer con los datos necesarios
	 */void ejecutar(Transfer trans);
	
	/**
	 * Ejecuta paso a paso el código para la MV
	 * @param trans Transfer con los datos necesarios
	 */
	void ejecutaPasoAPaso(Transfer trans);

}
