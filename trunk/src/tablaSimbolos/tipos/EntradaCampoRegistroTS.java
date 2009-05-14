/**
 * Paquete que recoge todas las clases pertenecientes a la tabla de símbolos.
 */
package tablaSimbolos.tipos;

/**
 * Clase que implementa, en forma de árbol binario, el contenido de los registros.
 */
public class EntradaCampoRegistroTS {

	/**
	 * nombre del campo.
	 */
	private String nombreCampo;

	/**
	 * Información con el tipo del campo.
	 */
	private EntradaTS tipoCampo;

	/**
	 * Desplazamiento del campo dentro del registro.
	 */
	private int offset;

	/**
	 * El hijo izquierdo de este campo dentro del registro.
	 */
	private EntradaCampoRegistroTS hijoIzq;

	/**
	 * El hijo derecho de este campo dentro del registro.
	 */
	private EntradaCampoRegistroTS hijoDer;

	/**
	 * Constructora con parámetros.
	 * 
	 * @param nombreCampo
	 *            Nombre del campo.
	 * @param tipoCampo
	 *            Información del tipo del campo.
	 */
	public EntradaCampoRegistroTS(String nombreCampo, EntradaTS tipoCampo) {
		super();
		this.nombreCampo = nombreCampo;
		this.tipoCampo = tipoCampo;
		try {
			this.offset += tipoCampo.getTamanno();
		} catch (NullPointerException e) {
			this.offset = 1;
		}
	}

	/**
	 * Constructora por defecto.
	 */
	public EntradaCampoRegistroTS() {
	}
	
	/**
	 * Constructora con parámetros.
	 * 
	 * @param nombreCampo
	 *            Nombre del campo.
	 * @param tipoCampo
	 *            Información del tipo del campo.
	 * @param offset
	 *            Desplazamiento dentro del registro.
	 */
	public EntradaCampoRegistroTS(String nombreCampo, EntradaTS tipoCampo, int offset) {
		super();
		this.nombreCampo = nombreCampo;
		this.tipoCampo = tipoCampo;
		this.offset = offset;

	}

	/**
	 * Accesor para el atributo nombreCampo.
	 * 
	 * @return El nombre del campo.
	 */
	public String getNombreCampo() {
		return nombreCampo;
	}

	/**
	 * Mutador para el atributo nombreCampo.
	 * 
	 * @param nombreCampo
	 *            Nombre del campo.
	 */
	public void setNombreCampo(String nombreCampo) {
		this.nombreCampo = nombreCampo;
	}

	/**
	 * Accesor para el atributo tipoCampo.
	 * 
	 * @return La información asociada al tipo del campo.
	 */
	public EntradaTS getTipoCampo() {
		return tipoCampo;
	}

	/**
	 * Mutador para el atributo tipoCampo.
	 * 
	 * @param tipoCampo
	 *            Información asociada al tipo del campo.
	 */
	public void setTipoCampo(EntradaTS tipoCampo) {
		this.tipoCampo = tipoCampo;
	}

	/**
	 * Accesor para el atributo offset.
	 * 
	 * @return el desplazamiento.
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * Método que se encarga de calcular el desplazamiento máximo del árbol.
	 * @return El desplazamiento máximo del árbol.
	 */
	public int getMaxOffset() {
		if (hijoIzq == null && hijoDer == null) {
			return offset;
		} else {
			int altoIz = -1;
			if (hijoIzq != null)
				altoIz = hijoIzq.altura();
			int altoDe = -1;
			if (hijoDer != null)
				altoDe = hijoDer.altura();
			if (altoIz > altoDe) {
				return hijoIzq.getMaxOffset();
			} else {
				return hijoDer.getMaxOffset();
			}
		}
	}

	/**
	 * Establece el desplazamiento (tamaño) del árbol binario.
	 * 
	 * @param offset
	 *            Entero con el desplazamiento.
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * Devuelve en el hijo izquierdo del arbol binario.
	 * 
	 * @return Subarbol izquierdo del padre.
	 */
	public EntradaCampoRegistroTS getHijoIzq() {
		return hijoIzq;
	}

	/**
	 * Escribe en el hijo izquierdo del árbol binario.
	 * 
	 * @param hijoIzq
	 *            Subárbol que insertamos a la izquierda del padre.
	 */
	public void setHijoIzq(EntradaCampoRegistroTS hijoIzq) {
		this.hijoIzq = hijoIzq;
	}

	/**
	 * Devuelve en el hijo derecho del árbol binario.
	 * 
	 * @return Subárbol derecho del padre.
	 */
	public EntradaCampoRegistroTS getHijoDer() {
		return hijoDer;
	}

	/**
	 * Escribe en el hijo derecho del arbol binario.
	 * 
	 * @param hijoDer
	 *            Subarbol que insertamos a la derecha del padre
	 */
	public void setHijoDer(EntradaCampoRegistroTS hijoDer) {
		this.hijoDer = hijoDer;
	}

	/**
	 * Método que se encarga de buscar por el registro el
	 * @param lexema Nombre del campo que queremos encontrar.
	 * @param nodo El padre del nodo actual.
	 * @return Si el nodo está devuelve el nodo buscado, sino devuelve
	 */
	public EntradaCampoRegistroTS buscar(String lexema, EntradaCampoRegistroTS nodo) {
		if (!nombreCampo.equals(lexema)) {
			if ((hijoIzq != null) && (nodo.getNombreCampo() == null)) {
				nodo = hijoIzq.buscar(lexema, nodo);
				if ((hijoDer != null) && (nodo.getNombreCampo() == null)) {
					nodo = hijoDer.buscar(lexema, nodo);
				} else if (nodo == null)
					nodo = null;
			}
		} else {
			return this;
		}
		return nodo;
	}

	/**
	 * Método que se encarga de calcular evaluar el offset del árbol binario.
	 * @return el offset completo del árbol.
	 */
	public int evaluar_offset() {
		int offset = 0;
		offset = this.inorden(offset);
		return offset;
	}

	/**
	 * recorrido en inorden del árbol y suma los offset de todos los campos.
	 * @return la suma de todos los offsets.
	 */
	public int inorden(int offset) {
		// hijoIzquierdo - Nodo - hijoDerecho
		if (hijoIzq != null) {
			offset += hijoIzq.inorden(offset + tipoCampo.getTamanno());
		}
		offset += this.offset;
		if (hijoDer != null) {
			offset += hijoDer.inorden(offset + tipoCampo.getTamanno());
		}
		return offset;
	}

	/**
	 * Método que crea un nuevo TNodoCampo a partir de uno dado.
	 * @param nodo Nodo que queremos duplicar.
	 * @return EL nodo duplicado.
	 */
	public EntradaCampoRegistroTS copia(EntradaCampoRegistroTS nodo) {
		EntradaCampoRegistroTS nodoA = new EntradaCampoRegistroTS();
		nodoA.setHijoDer(nodo.getHijoDer());
		nodoA.setHijoIzq(nodo.getHijoIzq());
		nodoA.setNombreCampo(nodo.getNombreCampo());
		nodoA.setOffset(nodo.getOffset());
		nodoA.setTipoCampo(nodo.getTipoCampo());
		return nodoA;
	}

	/**
	 * Método que se encarga de añadir un nuevo nodo al árbol binario.
	 * @param lexema Nombre del campo.
	 * @param tipo Información del campo.
	 * @param tipoAnt Padre del nodo actual.
	 * @param desplazamiento desplazamiento dentro del árbol.
	 */
	public void add(String lexema, EntradaTS tipo, EntradaTS tipoAnt, int desplazamiento) {
		if (hijoIzq == null && hijoDer == null) {
			// hay que insertar en un árbol vacio empezamos por el izquierdo
			hijoIzq = new EntradaCampoRegistroTS(lexema, tipo, desplazamiento
					+ tipoAnt.getTamanno());
		} else {
			/* ahora insertaremos en el que menor altura tenga */
			int altoIz = -1;
			if (hijoIzq != null)
				altoIz = hijoIzq.altura();
			int altoDe = -1;
			if (hijoDer != null)
				altoDe = hijoDer.altura();
			if (altoIz <= altoDe) {
				if (altoIz != -1)
					// recursión
					hijoIzq.add(lexema, tipo, tipoAnt, desplazamiento
							+ this.offset);
				else {
					hijoIzq = new EntradaCampoRegistroTS(lexema, tipo, desplazamiento
							+ tipoAnt.getTamanno());
				}
			} else {
				if (altoDe != -1)
					// recursión
					hijoDer.add(lexema, tipo, tipoAnt, desplazamiento
							+ this.offset);
				else {
					hijoDer = new EntradaCampoRegistroTS(lexema, tipo, desplazamiento
							+ tipoAnt.getTamanno());
				}
			}
		}
	}

	/**
	 * Función que determina la altura de un árbol binario. 
	 * 
	 * @return la altura del árbol.
	 */
	public int altura() {
		int alto = -1;
		int hijoIz = 0, hijoDe = 0;
		if (hijoDer == null && hijoIzq == null)
			return 1;
		else {
			if (hijoIzq != null)
				hijoIz = hijoIzq.altura() + 1;
			if (hijoDer != null)
				hijoDe = hijoDer.altura() + 1;
			if (hijoIz <= hijoDe) {
				alto = hijoDe;
			} else {
				alto = hijoIz;
			}
		}
		return alto;
	}

	/**
	 * Método que se encarga de convertir a String el nombre del campo.
	 * @return cadena de caracteres con la información.
	 */
	public String toString() {
		String cad = "";
		cad += " Campo: " + nombreCampo;
		return cad;
	}
}
