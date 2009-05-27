package mVirtual.comunicacion;

import mVirtual.comunicacion.transfers.Transfer;
/**
 * 
 * Modeliza el controlador en el esquema modelo-vista-controlador
 *
 */
public abstract class Controlador {
	
		public static Controlador controlador;
		/**
		 * Obtiene la instancia del controlador
		 * @return instancia La instancia
		 */
		public static Controlador obtenerInstancia(){
			if (controlador==null) 
				controlador= new ControladorImp();
			return controlador;
		}
		/**
		 * Capta la transferencia para el controlador
		 * @param trans Los datos usados por la transferencia
		 * @throws Exception Posible error de comunicacino
		 */
		public void captarTransferencia(Transfer trans) throws Exception {
		}
		/**
		 * Actualiza una vista con el transfer pasado por parámetro
		 * @param trans Transfer para actualizar la vista
		 */
		public void actualizarVistas(Transfer trans){};
		/**
		 * Asocia una vista para el controlador
		 * @param vista La vista a asociar
		 */
		public void asociarVista(Vista vista){};
		/**
		 * Asocia un modelo para el controlador
		 * @param modelo El modelo a asociar
		 */
		public void asociarModelo(Modelo modelo){};
}
