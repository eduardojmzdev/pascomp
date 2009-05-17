package comunicacion;

import comunicacion.transfers.Transfer;

public abstract class Controlador {
	
		public static Controlador controlador;
		
		public static Controlador obtenerInstancia(){
			if (controlador==null) 
				controlador= new ControladorImp();
			return controlador;
		}

		public void captarTransferencia(Transfer trans) throws Exception {
		}
		
		public void actualizarVistas(Transfer trans){};
		
		public void asociarVista(Vista vista){};
		
		public void asociarModelo(Modelo modelo){};
}
