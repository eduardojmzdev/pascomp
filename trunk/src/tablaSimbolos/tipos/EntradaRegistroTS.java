/**
 * Paquete que recoge todas las clases pertenecientes a la tabla de símbolos.
 */
package tablaSimbolos.tipos;

import utils.TTipo;

/**
 * Clase que implemnta el nodo registro. 
 */
public class EntradaRegistroTS extends EntradaTS {

	/**
	 * Contenido del nodo registro.
	 */
	private EntradaCampoRegistroTS campos;
	
	/**
	 * Cosntructora con parámetros.
	 * @param registro Tipo registro.
	 * @param raiz Raíz del árbol binario.
	 */
	public EntradaRegistroTS(TTipo registro,EntradaCampoRegistroTS raiz) {
		super(registro,raiz.getMaxOffset()+1);
		campos = raiz;
	}
	
	/**
	 * Método que busca en el registro el campo que se quiere buscar.
	 * @param lexema Nombre del campo a buscar.
	 * @return Información de ese campo.
	 */
	public EntradaCampoRegistroTS buscar(String lexema){
		EntradaCampoRegistroTS nodo = new EntradaCampoRegistroTS();
		nodo = campos.buscar(lexema, nodo);
		return nodo;
	}
	
	/**
	 * Accesor para el atributo campos.
	 * @return Los campos del registro.
	 */
	public EntradaCampoRegistroTS getCampos() {
		return campos;
	}

	/**
	 * Mutador para el atributo campos.
	 * @param campos Los campos del registro.
	 */
	public void setCampos(EntradaCampoRegistroTS campos) {
		this.campos = campos;
	}

	/**
	 * Método que se encarga de calcular el desplazamiento
	 * total del registro.
	 */
	public void evaluarOffsets(){
		int offset = 0;
		offset = campos.evaluar_offset();
		super.setTamanno(offset);
		
	}
}
