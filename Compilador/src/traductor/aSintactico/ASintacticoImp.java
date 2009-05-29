package traductor.aSintactico;

import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import excepciones.SintacticException;
import traductor.tablaSimbolos.Modo;
import traductor.tablaSimbolos.Parametro;
import traductor.tablaSimbolos.TElemento;
import traductor.tablaSimbolos.TablaSimbolos;
import traductor.tablaSimbolos.tipos.PropTipos;
import traductor.tablaSimbolos.tipos.PropTiposArray;
import traductor.tablaSimbolos.tipos.PropTiposNombrado;
import traductor.tablaSimbolos.tipos.PropTiposPointer;
import traductor.tablaSimbolos.tipos.PropTiposPro;
import traductor.tablaSimbolos.tipos.TTipo;
import traductor.aLexico.ALexico;
import traductor.aLexico.EnumToken;
import traductor.aLexico.Token;

/**
 * Implementacion del Analizador Sintactico
 *
 */
public class ASintacticoImp extends ASintactico {

	private Token token;
	private int direccion;
	private int contadorInstrucciones = 0;
	private int nivel;
	private boolean hayError;
	private PrintWriter ficheroSalida;
	private ArrayList<String> codigoTexto = new ArrayList<String>();
	private int tamaño;
	private String nombreAmbito;

	public boolean getErrores() {
		return hayError;
	}

	/* Analiza el fichero que se pasa como parametro
	 * @see traductor.aSintactico.ASintactico#analizar(java.io.FileReader)
	 */
	public void analizar(FileReader ficheroFuente) throws Exception {
		direccion = 0;
		hayError = false;
		ALexico.getInstance().setFichero(ficheroFuente);
		reconocePrograma();
	}

	/**
	 * Vuelca el contenido de la variable "codigoTexto" donde hemos almacenado
	 * progresivamente el codigo p
	 * 
	 */
	private void volcarFicheroCodigo() {
		for (int i = 0; i < codigoTexto.size(); i++) {
			this.ficheroSalida.println(codigoTexto.get(i));
		}
		ficheroSalida.close();

	}

	/**
	 * Reconoce la primera linea de programa "PROGRAM"
	 * @throws Exception
	 */
	private void reconocePrograma() throws Exception {
		tamaño = 0;
		reconoceCabecera();
		reconoceRestoPrograma();
		token = ALexico.getInstance().obtenerToken();
		compruebaTokens(token.getCategoria(), EnumToken.PUNTO);
		token = ALexico.getInstance().obtenerToken();
		if (token != null && comparaTokens(token.getCategoria(), EnumToken.PUNTO)) {
			throw new SintacticException(ALexico.getInstance().getLinea() + "'.' No esperado.", ALexico.getInstance().getLinea());
		}
		volcarFicheroCodigo();
	}

	
	/**
	 * Reconoce la cabecera del programa
	 * @throws Exception
	 */
	private void reconoceCabecera() throws Exception {

		token = ALexico.getInstance().obtenerToken();
		if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.PROGRAM))) {
			reconoceIdentificadorCabecera();
		}
		token = ALexico.getInstance().obtenerToken();
		if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.PYCOMA))) {
			token = ALexico.getInstance().obtenerToken();
		}
	}


	private void reconoceRestoPrograma() throws Exception {
		int tmp0 = 0;

		if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.TIPO))) {
			reconoceZonaTipos();
		}
		if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.TVAR))) {
			reconoceDeclaraciones();
		}
		if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.PROC))) {
			String codigo = "cargaCP;.";
			emitirCodigo(codigo);
			codigo = "ir-a();.";
			emitirCodigo(codigo);
			tmp0 = contadorInstrucciones;
			direccion = 1;
			// ////////////////////Ojo con el dos
			reconoceProcedimiento();
			epilogo(nivel + 1);
			codigo = "irIndice;.";
			emitirCodigo(codigo);
			while (!hayErrorLexico(token) && (!comparaTokens(token.getCategoria(), EnumToken.INICIO))) {
				if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.PROC))) {
					direccion = 1;
					reconoceProcedimiento();
					epilogo(nivel + 1);
					codigo = "irIndice;.";
					emitirCodigo(codigo);
				}
			}
			parchea(tmp0, contadorInstrucciones);
		}
		if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.INICIO))) {
			reconoceCuerpo();
		}
	}

	/**
	 * Reconoce un procedimiento
	 * @throws Exception
	 */
	private void reconoceProcedimiento() throws Exception {
		token = ALexico.getInstance().obtenerToken();
		if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.ID))) {
			String nombreProc = token.getLexema();
			token = ALexico.getInstance().obtenerToken();
			if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.PYCOMA))) {
				TablaSimbolos tabla = TablaSimbolos.getInstance();
				if (!existeID(nombreProc)) {
					prologo(nivel, tamaño);
					reconocePuntoYComa();
					nombreAmbito = nombreProc;
					PropTiposPro p = new PropTiposPro(new ArrayList<Parametro>());
					tabla.añadeProcedimiento(nombreProc, nivel, p, contadorInstrucciones - 13);
					nivel++;
					tabla.crearNivel(nivel);
					tabla.añadeProcedimiento(nombreProc, nivel, p, contadorInstrucciones - 13);
					reconoceRestoPrograma();
					token = ALexico.getInstance().obtenerToken();
					reconocePuntoYComa();
					tabla.liberarNivel(nivel);
					nivel--;
				}
			} else if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.PA))) {
				TablaSimbolos tabla = TablaSimbolos.getInstance();
				if (!existeID(nombreProc)) {
					nivel++;
					nombreAmbito = nombreProc;
					tabla.crearNivel(nivel);
					PropTiposPro p = new PropTiposPro(reconoceParametrosProcedimiento());
					int tamaux = 0;
					for (int i = 0; i < p.getParametros().size(); i++) {
						tamaux += p.getParametros().get(i).getTipo().getTam();
					}
					prologo(nivel, tamaux);
					tabla.añadeProcedimiento(nombreProc, nivel - 1, p, contadorInstrucciones - 13);
					tabla.añadeProcedimiento(nombreProc, nivel, p, contadorInstrucciones - 13);
					reconoceRestoPrograma();
					token = ALexico.getInstance().obtenerToken();
					reconocePuntoYComa();
					tabla.liberarNivel(nivel);
					nivel--;
				}
			}
		}

	}

	/**
	 * Reconoce los parametros de un procedimiento
	 * @return
	 * @throws Exception
	 */
	private ArrayList<Parametro> reconoceParametrosProcedimiento() throws Exception {
		token = ALexico.getInstance().obtenerToken();
		ArrayList<Parametro> lparametros = new ArrayList<Parametro>();
		boolean fin = false;
		while (!fin) {
			Modo porVariable = Modo.VALOR;
			ArrayList<String> aL = new ArrayList<String>();
			if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.TVAR))) {
				porVariable = Modo.VARIABLE;
				token = ALexico.getInstance().obtenerToken();
			}
			if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.ID))) {
				aL.add(token.getLexema());
			}
			token = ALexico.getInstance().obtenerToken();
			while (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.PYCOMA)) && !hayError) {
				token = ALexico.getInstance().obtenerToken();
				if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.ID))) {
					aL.add(token.getLexema());// resto
				}
				token = ALexico.getInstance().obtenerToken();
			}
			if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.DOSPUNTOS))) {
				token = ALexico.getInstance().obtenerToken();// obtenemos el
				// tipo

				if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.TIPBOOL))) {
					for (int i = 0; i < aL.size(); i++) {
						if (!existeID((String) aL.get(i))) {
							TablaSimbolos tabla = TablaSimbolos.getInstance();
							PropTipos p = tabla.dameTipo("boolean", nivel);
							tabla.añadeVariable((String) aL.get(i), direccion, p, nivel);
							Parametro par = new Parametro(porVariable, p, (String) aL.get(i));
							lparametros.add(par);
							direccion++;
						}
					}
				} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.TIPENT))) {
					for (int i = 0; i < aL.size(); i++) {
						if (!existeID((String) aL.get(i))) {
							TablaSimbolos tabla = TablaSimbolos.getInstance();
							PropTipos p = tabla.dameTipo("integer", nivel);
							tabla.añadeVariable((String) aL.get(i), direccion, p, nivel);
							Parametro par = new Parametro(porVariable, p, (String) aL.get(i));
							lparametros.add(par);
							direccion++;
						}
					}
				} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.ID))) {
					for (int i = 0; i < aL.size(); i++) {
						if (!existeID((String) aL.get(i))) {
							TablaSimbolos tabla = TablaSimbolos.getInstance();
							if (tabla.existeTipo(token.getLexema(), nivel)) {
								PropTipos p = tabla.dameTipo(token.getLexema(), nivel);
								tabla.añadeVariable((String) aL.get(i), direccion, p, nivel);
								Parametro par = new Parametro(porVariable, p, (String) aL.get(i));
								lparametros.add(par);
								direccion++;
								// Emitir el codigo
							}
						}
					}
				} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.TARRAY))) {
					int tam = reconoceDeclaracionArray();
					for (int i = 0; i < aL.size(); i++) {
						if (!existeID((String) aL.get(i))) {
							TablaSimbolos tabla = TablaSimbolos.getInstance();
							if (tabla.existeTipo(token.getLexema(), nivel)) {
								PropTipos p = tabla.dameTipo(token.getLexema(), nivel);
								tabla.añadeVariable((String) aL.get(i), direccion, p, nivel);
								Parametro par = new Parametro(porVariable, p, (String) aL.get(i));
								lparametros.add(par);
								direccion = direccion + tam * tabla.dameTipo(token.getLexema(), nivel).getTam();
								// Emitir el codigo
							}
						}
					}
				} else if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.TPUNTERO))) {
					token = ALexico.getInstance().obtenerToken();
					if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.ID))) {
						for (int i = 0; i < aL.size(); i++) {
							if (!existeID((String) aL.get(i))) {
								TablaSimbolos tabla = TablaSimbolos.getInstance();
								if (tabla.existeTipo(token.getLexema(), nivel)) {
									PropTipos p = tabla.dameTipo(token.getLexema(), nivel);
									tabla.añadeVariable((String) aL.get(i), direccion, p, nivel);
									Parametro par = new Parametro(porVariable, p, (String) aL.get(i));
									lparametros.add(par);
									direccion++;
									// Emitir el codigo
								}
							}
						}
					}
				}

			}
			token = ALexico.getInstance().obtenerToken();
			if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.PC))) {
				fin = true;
			} else {
				compruebaTokens(token.getCategoria(), EnumToken.PYCOMA);
				token = ALexico.getInstance().obtenerToken();
			}
		}
		token = ALexico.getInstance().obtenerToken();
		reconocePuntoYComa();
		return lparametros;
	}

	
	/**
	 * Reconoce la zona de declaracion de tipos
	 * @throws Exception
	 */
	private void reconoceZonaTipos() throws Exception {
		token = ALexico.getInstance().obtenerToken();
		reconoceTipo();
		reconoceRestoTipos();
	}

	private void reconoceRestoTipos() throws Exception {
		while (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.ID)) && !hayError) {
			reconoceTipo();
		}
	}

	private void reconoceTipo() throws Exception {
		if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.ID))) {
			String nombre = token.getLexema();
			TablaSimbolos Ts = TablaSimbolos.getInstance();
			if (!Ts.existeTipoNivel(nombre, nivel)) {
				token = ALexico.getInstance().obtenerToken();
				if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.IGUAL))) {
					token = ALexico.getInstance().obtenerToken();

					if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.TIPENT))) {
						PropTipos p = Ts.dameTipo(token.getLexema(), nivel);
						Ts.añadeTipo(nombre, p, nivel);
					} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.TIPBOOL))) {
						PropTipos p = Ts.dameTipo(token.getLexema(), nivel);
						Ts.añadeTipo(nombre, p, nivel);
					} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.ID))) {
						if (existeTipo(token.getLexema())) {
							PropTipos p = Ts.dameTipo(token.getLexema(), nivel);
							Ts.añadeTipo(nombre, p, nivel);
						}
					} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.TARRAY))) {
						token = ALexico.getInstance().obtenerToken();
						if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.CA))) {
							token = ALexico.getInstance().obtenerToken();
							int tam = reconoceIntervalo();
							token = ALexico.getInstance().obtenerToken();
							if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.CC))) {
								token = ALexico.getInstance().obtenerToken();
								if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.OF))) {
									token = ALexico.getInstance().obtenerToken();
									TablaSimbolos tabla = TablaSimbolos.getInstance();
									if (tabla.existeTipo(token.getLexema(), nivel)) {
										PropTiposArray p = new PropTiposArray(tam, Ts.dameTipo(token.getLexema(), nivel));
										Ts.añadeTipo(nombre, p, nivel);

									}
								}
							}
						}
					} else if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.TPUNTERO))) {
						token = ALexico.getInstance().obtenerToken();
						TablaSimbolos tabla = TablaSimbolos.getInstance();
						if (tabla.existeTipo(token.getLexema(), nivel)) {
							PropTiposPointer p = new PropTiposPointer(Ts.dameTipo(token.getLexema(), nivel));
							Ts.añadeTipo(nombre, p, nivel);

						}
					}
					token = ALexico.getInstance().obtenerToken();
					reconocePuntoYComa();
				}
			} else {
				throw new SintacticException("Identificador de tipo: " + nombre + " repetido ", ALexico.getInstance().getLinea());
			}
		}
	}

	/**
	 * Reconoce el intervalo de un tipo para comprobar si parte de 0 y si es positivo
	 * @return
	 * @throws Exception
	 */
	private int reconoceIntervalo() throws Exception {
		int num = Integer.parseInt(token.getLexema());
		if (num != 0)
			throw new SintacticException("Intervalo no válido ", ALexico.getInstance().getLinea());
		else {
			token = ALexico.getInstance().obtenerToken();
			if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.PUNTO))) {
				token = ALexico.getInstance().obtenerToken();
				if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.PUNTO))) {
					token = ALexico.getInstance().obtenerToken();
					if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.DIGITO))) {
						int num2 = Integer.parseInt(token.getLexema());
						if (num2 < 0)
							throw new SintacticException("El intervalo debe ser positivo ", ALexico.getInstance().getLinea());
						return num2 + 1;
					}
				}
			}
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	private void reconoceConstantes() throws Exception {
		if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.ID))) {
			String nombre = token.getLexema();
			token = ALexico.getInstance().obtenerToken();
			if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.IGUAL))) {
				token = ALexico.getInstance().obtenerToken();
				if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.TRUE))) {
					if (!existeIDNivel(nombre)) {
						TablaSimbolos Ts = TablaSimbolos.getInstance();
						PropTipos p = Ts.dameTipo("boolean", nivel);
						Ts.añadeConstante(nombre, direccion, p, nivel);
						String codigoEmitido = "apila(TRUE)" + ";" + "desapilaDireccion(" + (direccion - 1) + ");.";
						emitirCodigo(codigoEmitido);
						direccion++;
					}
				} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.FALSE))) {
					if (!existeIDNivel(nombre)) {

						TablaSimbolos Ts = TablaSimbolos.getInstance();
						PropTipos p = Ts.dameTipo("boolean", nivel);
						Ts.añadeConstante(nombre, direccion, p, nivel);
						String codigoEmitido = "apila(FALSE)" + ";" + "desapilaDireccion(" + (direccion - 1) + ");.";
						emitirCodigo(codigoEmitido);
						direccion++;
					}
				} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.DIGITO))) {
					if (!existeIDNivel(nombre)) {
						TablaSimbolos Ts = TablaSimbolos.getInstance();
						PropTipos p = Ts.dameTipo("integer", nivel);
						Ts.añadeConstante(nombre, direccion, p, nivel);
						String codigoEmitido = "apila(" + token.getLexema() + ")" + ";" + "desapilaDireccion(" + (direccion - 1) + ");.";
						emitirCodigo(codigoEmitido);
						direccion++;
					}
				} else if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.CA))) {
					if (!existeIDNivel(nombre)) {
						TablaSimbolos Ts = TablaSimbolos.getInstance();
						token = ALexico.getInstance().obtenerToken();
						String tipo = "";
						if (comparaTokens(token.getCategoria(), EnumToken.DIGITO))
							tipo = "integer";
						else if (comparaTokens(token.getCategoria(), EnumToken.TRUE) || comparaTokens(token.getCategoria(), EnumToken.FALSE))
							tipo = "boolean";
						else if (compruebaTokens(token.getCategoria(), EnumToken.ID)) {
							tipo = tipoConstante(token.getLexema());
						}
						ArrayList arrayConstante = obtenerArrayConstante(tipo);
						PropTiposArray p = new PropTiposArray(arrayConstante.size(), Ts.dameTipo(tipo, nivel));
						Ts.añadeConstante(nombre, direccion, p, nivel);
						direccion = direccion + arrayConstante.size();
					}
				}
				token = ALexico.getInstance().obtenerToken();
				reconocePuntoYComa();
			}
		}
	}

	@SuppressWarnings("unchecked")
	// Aqui habria que hacer el emite codigo
	private ArrayList obtenerArrayConstante(String tipo) throws Exception {
		ArrayList<String> aConst = new ArrayList<String>();
		if (tipo.equals("integer")) {
			aConst.add(token.getLexema());
			token = ALexico.getInstance().obtenerToken();
			while (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.COMA))) {
				token = ALexico.getInstance().obtenerToken();
				if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.DIGITO))) {
					aConst.add(token.getLexema());
					token = ALexico.getInstance().obtenerToken();
				} else if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.ID))
						&& (tipoConstante(token.getLexema()).equals("integer"))) {
					aConst.add(token.getLexema());
					token = ALexico.getInstance().obtenerToken();
				}
			}
			compruebaTokens(token.getCategoria(), EnumToken.CC);
			return aConst;
		}
		if (tipo.equals("boolean")) {
			aConst.add(token.getLexema());
			token = ALexico.getInstance().obtenerToken();
			while (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.COMA))) {
				token = ALexico.getInstance().obtenerToken();
				if (!hayErrorLexico(token)
						&& (comparaTokens(token.getCategoria(), EnumToken.TRUE) || comparaTokens(token.getCategoria(), EnumToken.FALSE))) {
					aConst.add(token.getLexema());
					token = ALexico.getInstance().obtenerToken();
				} else if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.ID))
						&& (tipoConstante(token.getLexema()).equals("boolean"))) {
					aConst.add(token.getLexema());
					token = ALexico.getInstance().obtenerToken();
				}
			}
			compruebaTokens(token.getCategoria(), EnumToken.CC);
			return aConst;
		}
		return null;
	}

	private String tipoConstante(String nombre) throws Exception {
		TablaSimbolos Ts = TablaSimbolos.getInstance();
		if (Ts.dameTipo(nombre, nivel).getNombreTipo() == TTipo.BOOLEAN)
			return "boolean";
		else if (Ts.dameTipo(nombre, nivel).getNombreTipo() == TTipo.INTEGER)
			return "integer";
		else
			throw new SintacticException("Valor introducido no válido ", ALexico.getInstance().getLinea());
	}

	private void emitirCodigo(String codigoEmitido) {
		String subCadena = "";
		int puntero = 0;
		while (codigoEmitido.charAt(puntero) != '.') {
			while (codigoEmitido.charAt(puntero) != ';') {
				subCadena += codigoEmitido.charAt(puntero);
				puntero++;
			}
			codigoTexto.add(subCadena);
			subCadena = "";
			puntero++;
			contadorInstrucciones++;
		}
	}

	/**
	 * Reconocimiento de declaraciones de tipos y variables
	 * @throws Exception
	 */
	private void reconoceDeclaraciones() throws Exception {
		token = ALexico.getInstance().obtenerToken();
		reconoceDeclaracion();
		reconoceRestoDeclaracion();
	}

	private void reconoceDeclaracion() throws Exception {
		ArrayList<String> aL = new ArrayList<String>();
		if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.ID))) {
			aL.add(token.getLexema());
		}
		token = ALexico.getInstance().obtenerToken();
		while (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.COMA)) && !hayError) {
			token = ALexico.getInstance().obtenerToken();
			if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.ID))) {
				aL.add(token.getLexema());// resto
			}
			token = ALexico.getInstance().obtenerToken();
		}
		if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.DOSPUNTOS))) {
			token = ALexico.getInstance().obtenerToken();// obtenemos el tipo

			if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.TIPBOOL))) {
				for (int i = 0; i < aL.size(); i++) {
					if (!existeIDNivel((String) aL.get(i))) {
						TablaSimbolos tabla = TablaSimbolos.getInstance();
						PropTipos p = tabla.dameTipo("boolean", nivel);
						tabla.añadeVariable((String) aL.get(i), direccion, p, nivel);
						String codigoEmitido = "apilaDireccion(" + (1 + nivel) + ");.";
						emitirCodigo(codigoEmitido);
						codigoEmitido = "apila(" + (direccion - 1) + ");.";
						emitirCodigo(codigoEmitido);
						codigoEmitido = "suma;.";
						emitirCodigo(codigoEmitido);
						codigoEmitido = "apila(null);.";
						emitirCodigo(codigoEmitido);
						codigoEmitido = "desapilaIndice;.";
						emitirCodigo(codigoEmitido);
						direccion++;
					}
				}
			} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.TIPENT))) {
				for (int i = 0; i < aL.size(); i++) {
					if (!existeIDNivel((String) aL.get(i))) {
						TablaSimbolos tabla = TablaSimbolos.getInstance();
						PropTipos p = tabla.dameTipo("integer", nivel);
						tabla.añadeVariable((String) aL.get(i), direccion, p, nivel);
						String codigoEmitido = "apilaDireccion(" + (1 + nivel) + ");.";
						emitirCodigo(codigoEmitido);
						codigoEmitido = "apila(" + (direccion - 1) + ");.";
						emitirCodigo(codigoEmitido);
						codigoEmitido = "suma;.";
						emitirCodigo(codigoEmitido);
						codigoEmitido = "apila(null);.";
						emitirCodigo(codigoEmitido);
						codigoEmitido = "desapilaIndice;.";
						emitirCodigo(codigoEmitido);
						direccion++;
					}
				}
			} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.ID))) {
				for (int i = 0; i < aL.size(); i++) {
					if (!existeIDNivel((String) aL.get(i))) {
						TablaSimbolos tabla = TablaSimbolos.getInstance();
						if (tabla.existeTipo(token.getLexema(), nivel)) {
							String nombreTipo = token.getLexema();
							PropTipos p = tabla.dameTipo(nombreTipo, nivel);
							tabla.añadeVariable((String) aL.get(i), direccion, p, nivel);
							if (compruebaTiposNoExcepcion(p, tabla.dameTipo("boolean", nivel))) {
								// Array prototipado en booleanos
								String codigoEmitido = "apilaDireccion(" + (1 + nivel) + ");.";
								emitirCodigo(codigoEmitido);
								codigoEmitido = "apila(" + (direccion - 1) + ");.";
								emitirCodigo(codigoEmitido);
								codigoEmitido = "suma;.";
								emitirCodigo(codigoEmitido);
								codigoEmitido = "apila(null);.";
								emitirCodigo(codigoEmitido);
								codigoEmitido = "desapilaIndice;.";
								emitirCodigo(codigoEmitido);
							} else if (compruebaTiposNoExcepcion(p, tabla.dameTipo("integer", nivel))) {
								// Array prototipado en enteros
								String codigoEmitido = "apilaDireccion(" + (1 + nivel) + ");.";
								emitirCodigo(codigoEmitido);
								codigoEmitido = "apila(" + (direccion - 1) + ");.";
								emitirCodigo(codigoEmitido);
								codigoEmitido = "suma;.";
								emitirCodigo(codigoEmitido);
								codigoEmitido = "apila(null);.";
								emitirCodigo(codigoEmitido);
								codigoEmitido = "desapilaIndice;.";
								emitirCodigo(codigoEmitido);
							} else {
								tabla.existeTipo(nombreTipo, nivel);
								compruebaTiposNoExcepcion(p, tabla.dameTipo("integer", nivel));
								String codigoEmitido = "apilaDireccion(" + (1 + nivel) + ");.";
								emitirCodigo(codigoEmitido);
								codigoEmitido = "apila(" + (direccion - 1) + ");.";
								emitirCodigo(codigoEmitido);
								codigoEmitido = "suma;.";
								emitirCodigo(codigoEmitido);
								codigoEmitido = "apila(null);.";
								emitirCodigo(codigoEmitido);
								codigoEmitido = "desapilaIndice;.";
								emitirCodigo(codigoEmitido);
							}
							direccion++;
						} else {
							throw new SintacticException("Tipo '" + token.getLexema() + "' no declarado.", ALexico.getInstance().getLinea());
						}
					}
				}
			} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.TARRAY))) {
				int tam = reconoceDeclaracionArray();
				for (int i = 0; i < aL.size(); i++) {
					if (!existeIDNivel((String) aL.get(i))) {
						TablaSimbolos tabla = TablaSimbolos.getInstance();
						if (tabla.existeTipo(token.getLexema(), nivel)) {
							PropTiposArray p = new PropTiposArray(tam, tabla.dameTipo(token.getLexema(), nivel));
							tabla.añadeVariable((String) aL.get(i), direccion, p, nivel);
							int tope = direccion + tam * tabla.dameTipo(token.getLexema(), nivel).getTam();
							for (int j = tope - tam * tabla.dameTipo(token.getLexema(), nivel).getTam(); j < tope; j++) {
								// Emitir la dupla de codigo correspondiente al
								// tipado basico del array
								if (compruebaTiposNoExcepcion(p.getRef(), tabla.dameTipo("boolean", nivel))) {
									// Array prototipado en booleanos
									String codigoEmitido = "apilaDireccion(" + (nivel + 1) + ");.";
									emitirCodigo(codigoEmitido);
									codigoEmitido = "apila(" + (j - (tope - tam * tabla.dameTipo(token.getLexema(), nivel).getTam())) + ");.";
									emitirCodigo(codigoEmitido);
									codigoEmitido = "suma;.";
									emitirCodigo(codigoEmitido);
									codigoEmitido = "apila(" + (direccion - 1) + ");.";
									emitirCodigo(codigoEmitido);
									codigoEmitido = "suma;.";
									emitirCodigo(codigoEmitido);
									codigoEmitido = "apila(null);.";
									emitirCodigo(codigoEmitido);
									codigoEmitido = "desapilaIndice;.";
									emitirCodigo(codigoEmitido);
									direccion++;
								} else if (compruebaTiposNoExcepcion(p.getRef(), tabla.dameTipo("integer", nivel))) {
									// Array prototipado en enteros
									String codigoEmitido = "apilaDireccion(" + (nivel + 1) + ");.";
									emitirCodigo(codigoEmitido);
									codigoEmitido = "apila(" + (j - (tope - tam * tabla.dameTipo(token.getLexema(), nivel).getTam())) + ");.";
									emitirCodigo(codigoEmitido);
									codigoEmitido = "suma;.";
									emitirCodigo(codigoEmitido);
									codigoEmitido = "apila(" + (direccion - 1) + ");.";
									emitirCodigo(codigoEmitido);
									codigoEmitido = "suma;.";
									emitirCodigo(codigoEmitido);
									codigoEmitido = "apila(null);.";
									emitirCodigo(codigoEmitido);
									codigoEmitido = "desapilaIndice;.";
									emitirCodigo(codigoEmitido);
									direccion++;
								} else {
									throw new SintacticException("Tipo no aceptado. ", ALexico.getInstance().getLinea());
								}
							}
							// Emitir el codigo
						}
					}

				}

			} else if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.TPUNTERO))) {
				token = ALexico.getInstance().obtenerToken();
				if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.TIPENT))) {
					for (int i = 0; i < aL.size(); i++) {
						if (!existeIDNivel((String) aL.get(i))) {
							TablaSimbolos tabla = TablaSimbolos.getInstance();
							PropTiposPointer p = new PropTiposPointer(tabla.dameTipo("integer", nivel));
							tabla.añadeVariable((String) aL.get(i), direccion, p, nivel);
							String codigoEmitido = "apilaDireccion(" + (1 + nivel) + ");.";
							emitirCodigo(codigoEmitido);
							codigoEmitido = "apila(" + (direccion - 1) + ");.";
							emitirCodigo(codigoEmitido);
							codigoEmitido = "suma;.";
							emitirCodigo(codigoEmitido);
							codigoEmitido = "apila(null);.";
							emitirCodigo(codigoEmitido);
							codigoEmitido = "desapilaIndice;.";
							emitirCodigo(codigoEmitido);
							direccion++;

						}
					}
				} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.TIPBOOL))) {
					for (int i = 0; i < aL.size(); i++) {
						if (!existeIDNivel((String) aL.get(i))) {
							TablaSimbolos tabla = TablaSimbolos.getInstance();
							PropTiposPointer p = new PropTiposPointer(tabla.dameTipo("boolean", nivel));
							tabla.añadeVariable((String) aL.get(i), direccion, p, nivel);
							String codigoEmitido = "apilaDireccion(" + (1 + nivel) + ");.";
							emitirCodigo(codigoEmitido);
							codigoEmitido = "apila(" + (direccion - 1) + ");.";
							emitirCodigo(codigoEmitido);
							codigoEmitido = "suma;.";
							emitirCodigo(codigoEmitido);
							codigoEmitido = "apila(null);.";
							emitirCodigo(codigoEmitido);
							codigoEmitido = "desapilaIndice;.";
							emitirCodigo(codigoEmitido);
							direccion++;
						}
					}

				}

				else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.ID))) {
					for (int i = 0; i < aL.size(); i++) {
						if (!existeIDNivel((String) aL.get(i))) {
							TablaSimbolos tabla = TablaSimbolos.getInstance();
							if (tabla.existeTipo(token.getLexema(), nivel)) {
								PropTiposPointer p = new PropTiposPointer(tabla.dameTipo(token.getLexema(), nivel));
								tabla.añadeVariable((String) aL.get(i), direccion, p, nivel);
								direccion++;
								// Emitir el codigo
							}
						}
					}
				} else if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.TARRAY))) {

					int tam = reconoceDeclaracionArray();
					for (int i = 0; i < aL.size(); i++) {
						if (!existeIDNivel((String) aL.get(i))) {
							TablaSimbolos tabla = TablaSimbolos.getInstance();
							if (tabla.existeTipo(token.getLexema(), nivel)) {
								PropTiposArray p = new PropTiposArray(tam, tabla.dameTipo(token.getLexema(), nivel));
								PropTiposPointer punt = new PropTiposPointer(p);
								tabla.añadeVariable((String) aL.get(i), direccion, punt, nivel);
								direccion = direccion + tam * tabla.dameTipo(token.getLexema(), nivel).getTam();
								for (int j = direccion - tam * tabla.dameTipo(token.getLexema(), nivel).getTam(); j < direccion; j++) {
									// Emitir la dupla de codigo correspondiente
									// al tipado basico del array
									if (compruebaTiposNoExcepcion(p.getRef(), tabla.dameTipo("boolean", nivel))) {
										// Array prototipado en booleanos
										String codigoEmitido = "apila(null)" + ";" + "desapilaDireccion(" + (j - 1) + ");.";
										emitirCodigo(codigoEmitido);
									} else if (compruebaTiposNoExcepcion(p.getRef(), tabla.dameTipo("integer", nivel))) {
										// Array prototipado en enteros
										String codigoEmitido = "apila(null)" + ";" + "desapilaDireccion(" + (j - 1) + ");.";
										emitirCodigo(codigoEmitido);
									} else {
										throw new SintacticException("No se permiten arrays multidimensionales ni arrays de punteros", ALexico
												.getInstance().getLinea());
									}
								}
							}
						}

					}
				}
			}

		}
		token = ALexico.getInstance().obtenerToken();
		reconocePuntoYComa();
	}

	/**
	 * Reconoce la declaracion de un array
	 * @return
	 * @throws Exception
	 */
	private int reconoceDeclaracionArray() throws Exception {
		int tam = 0;
		token = ALexico.getInstance().obtenerToken();
		if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.CA))) {
			token = ALexico.getInstance().obtenerToken();
			tam = reconoceIntervalo();
			token = ALexico.getInstance().obtenerToken();
			if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.CC))) {
				token = ALexico.getInstance().obtenerToken();
				if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.OF))) {
					token = ALexico.getInstance().obtenerToken();
				}
			}
		}
		return tam;
	}

	private void reconoceRestoDeclaracion() throws Exception {
		while (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.ID)) && !hayError) {
			reconoceDeclaracion();
		}
	}

	private void reconoceCuerpo() throws Exception {
		token = ALexico.getInstance().obtenerToken();
		if (!hayErrorLexico(token) && !comparaTokens(token.getCategoria(), EnumToken.FIN)) {
			reconoceInstrucciones();
		}

	}

	private void reconoceInstrucciones() throws Exception {
		reconoceInstruccion();
		reconoceRestoInstrucciones();
	}

	/**
	 * Reconoce una instruccion
	 * @throws Exception
	 */
	private void reconoceInstruccion() throws Exception {
		try {
			TablaSimbolos t = TablaSimbolos.getInstance();
			if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.ID))) {
				String asignada = token.getLexema();
				token = ALexico.getInstance().obtenerToken();
				if (comparaTokens(token.getCategoria(), EnumToken.ASIG)) {
					// es una variable normal, se queda como en la primera
					// version,

					if ((t.obtenerInfo(asignada, nivel).dameElemento() == TElemento.VAR)) {
						PropTipos p = t.obtenerInfo(asignada, nivel).getProp();
						if (p.getNombreTipo() == TTipo.PUNTERO) {
							p = new PropTipos(TTipo.VACIO, 0);
						}
						if (IDDeclarado(asignada) && compruebaTipos(p, reconoceAsignacion())) {
							String codigoEmitido = "apila(" + (t.obtenerInfo(asignada, nivel).dameDir() - 1) + ");.";
							emitirCodigo(codigoEmitido);
							codigoEmitido = "apila(" + (1 + (t.dameNivel(asignada, nivel))) + ");.";
							emitirCodigo(codigoEmitido);
							codigoEmitido = "apilaIndice;.";
							emitirCodigo(codigoEmitido);
							codigoEmitido = "suma;.";
							emitirCodigo(codigoEmitido);
							if (nivel > 0) {
								PropTiposPro aux = ((PropTiposPro) t.dameTipo(nombreAmbito, nivel));
								String aux2 = "";
								for (int i = 0; i < aux.getParametros().size(); i++) {
									aux2 = aux.getParametros().get(i).getNombre();
									if ((aux2.equals(asignada)) && (aux.getParametros().get(i).getModo() == Modo.VARIABLE)) {
										String codigo = "apilaIndice;.";
										emitirCodigo(codigo);
									}
								}
							}
							codigoEmitido = "flip;.";
							emitirCodigo(codigoEmitido);
							codigoEmitido = "desapilaIndice;.";
							emitirCodigo(codigoEmitido);
							reconocePuntoYComa();
						}
					}

				} else if (comparaTokens(token.getCategoria(), EnumToken.CA)) {
					// es un array, el nombre del array esta en asignada
					token = ALexico.getInstance().obtenerToken();

					// Comprobamos si la posicion del array esta dentro del
					// rango
					int tam = t.dameTipo(asignada, nivel).getTam();
					if (!(token.getCategoria() != EnumToken.ID) && !(token.getCategoria() == EnumToken.DISTINTO)) {
						throw new SintacticException("No se permiten expresiones en arrays", ALexico.getInstance().getLinea());
					}
					try {
						int posicionArray = Integer.parseInt(token.getLexema());
						if (posicionArray > tam - 1 || posicionArray < 0) {
							throw new SintacticException("Array fuera de rango", ALexico.getInstance().getLinea());
						}
					} catch (NumberFormatException nfe) {
						// throw new
						// SintacticException("Posición de array inválida.",
						// ALexico.getInstance().getLinea());
					}

					if (compruebaTipos(reconoceExpresion(), t.dameTipo("integer", nivel))) {
						if (compruebaTokens(token.getCategoria(), EnumToken.CC)) {
							// Obtener direccion del array
							String codigoEmitir = "apila(" + (t.obtenerInfo(asignada, nivel).dameDir() - 1) + ");.";
							emitirCodigo(codigoEmitir);
							codigoEmitir = "apilaDireccion(" + (1 + (t.dameNivel(asignada, nivel))) + ");.";
							emitirCodigo(codigoEmitir);
							codigoEmitir = "suma;.";
							emitirCodigo(codigoEmitir);
							codigoEmitir = "suma;.";
							emitirCodigo(codigoEmitir);
							token = ALexico.getInstance().obtenerToken();
							if (comparaTokens(token.getCategoria(), EnumToken.ASIG)) {

								if (IDDeclarado(asignada) && (t.obtenerInfo(asignada, nivel).dameElemento() == TElemento.VAR)) {
									PropTiposArray p = (PropTiposArray) t.obtenerInfo(asignada, nivel).getProp();
									if (compruebaTipos(p.getRef(), reconoceAsignacion())) {
										String codigo = "desapilaIndice;.";
										emitirCodigo(codigo);
										reconocePuntoYComa();
									}
								}
							}
						}
					}
				} else if (comparaTokens(token.getCategoria(), EnumToken.TPUNTERO)) {
					// el nombre del puntero está en asignada
					if (IDDeclarado(asignada) && (t.obtenerInfo(asignada, nivel).dameElemento() == TElemento.VAR)) {
						token = ALexico.getInstance().obtenerToken();
						if (!hayErrorLexico(token) && comparaTokens(token.getCategoria(), EnumToken.CA)) {
							token = ALexico.getInstance().obtenerToken();
							if (compruebaTipos(reconoceExpresion(), t.dameTipo("integer", nivel))) {
								if (!hayErrorLexico(token) && compruebaTokens(token.getCategoria(), EnumToken.CC)) {
									token = ALexico.getInstance().obtenerToken();
									if (!hayErrorLexico(token) && compruebaTokens(token.getCategoria(), EnumToken.ASIG)) {
										PropTipos p = ((PropTiposArray) (((PropTiposPointer) t.dameTipo(asignada, nivel)).getRef())).getRef();
										if (compruebaTipos(p, reconoceAsignacion())) {
											reconocePuntoYComa();
											// emitir codigo
										}
									}
								}
							}
						} else if (!hayErrorLexico(token) && comparaTokens(token.getCategoria(), EnumToken.ASIG)) {

							PropTiposPointer p = (PropTiposPointer) t.obtenerInfo(asignada, nivel).getProp();
							String codigo = "apilaDireccion(" + (1 + (t.dameNivel(asignada, nivel))) + ");.";
							emitirCodigo(codigo);
							codigo = "apila(" + (TablaSimbolos.getInstance().obtenerInfo(asignada, nivel).dameDir() - 1) + ");.";
							emitirCodigo(codigo);
							codigo = "suma;.";
							emitirCodigo(codigo);
							codigo = "apilaIndice;.";// ////???????
							emitirCodigo(codigo);
							if (compruebaTipos(p.getRef(), reconoceAsignacion())) {
								reconocePuntoYComa();
								codigo = "desapilaIndice;.";
								emitirCodigo(codigo);
							}
						}
					}
				} else if (comparaTokens(token.getCategoria(), EnumToken.PA)) {
					// llamada a un procedimiento con parentesis
					int i = 0;
					if (!t.existeProcedimiento(asignada, nivel)) {
						throw new SintacticException("Procedimiento '" + asignada + "' no declarado ", ALexico.getInstance().getLinea());
					}
					int nParam = ((PropTiposPro) t.dameTipo(asignada, nivel)).getParametros().size();
					tamaño = 0;
					token = ALexico.getInstance().obtenerToken();
					apilaRet((9 * nParam) + contadorInstrucciones + 11);
					String excodigo = "apilaDireccion(0);.";
					emitirCodigo(excodigo);
					excodigo = "apila(2);.";
					emitirCodigo(excodigo);
					excodigo = "suma;.";
					emitirCodigo(excodigo);
					excodigo = "apila(Hueco);.";
					emitirCodigo(excodigo);
					excodigo = "desapilaIndice;.";
					emitirCodigo(excodigo);
					while (!comparaTokens(token.getCategoria(), EnumToken.PC)) {
						if (i >= nParam) {
							throw new SintacticException("Número de parámetros incorrecto ", ALexico.getInstance().getLinea());
						}
						if (!hayErrorLexico(token) && comparaTokens(token.getCategoria(), EnumToken.ID)) {
							String nombreParam = token.getLexema();
							// Tenemos variable entera.
							// Determinamos el modo
							tamaño += t.dameTipo(nombreParam, nivel).getTam();
							if (((PropTiposPro) t.dameTipo(asignada, nivel)).getParametros().get(i).getModo() == Modo.VARIABLE) {
								// Apilar direccion de comienzo variable
								String codigo = "apilaDireccion(" + (1 + t.dameNivel(nombreParam, nivel)) + ");.";
								emitirCodigo(codigo);
								codigo = "apila(" + (t.obtenerInfo(nombreParam, nivel).dameDir() - 1) + ");.";
								emitirCodigo(codigo);
								codigo = "suma;.";
								emitirCodigo(codigo);
								codigo = "apilaDireccion(0);.";
								emitirCodigo(codigo);
								codigo = "apila(" + (2 + tamaño) + ");.";
								emitirCodigo(codigo);
								codigo = "suma;.";
								emitirCodigo(codigo);
								codigo = "flip;.";
								emitirCodigo(codigo);
								codigo = "desapilaIndice;.";
								emitirCodigo(codigo);
								codigo = "burbuja;.";
								emitirCodigo(codigo);
							} else// Porvalor
							{
								String codigo = "apilaDireccion(" + (1 + t.dameNivel(nombreParam, nivel)) + ");.";
								emitirCodigo(codigo);
								codigo = "apila(" + (t.obtenerInfo(nombreParam, nivel).dameDir() - 1) + ");.";
								emitirCodigo(codigo);
								codigo = "suma;.";
								emitirCodigo(codigo);
								codigo = "apilaIndice;.";
								emitirCodigo(codigo);
								codigo = "apilaDireccion(0);.";
								emitirCodigo(codigo);
								codigo = "apila(" + (2 + tamaño) + ");.";
								emitirCodigo(codigo);
								codigo = "suma;.";
								emitirCodigo(codigo);
								codigo = "flip;.";
								emitirCodigo(codigo);
								codigo = "desapilaIndice;.";
								emitirCodigo(codigo);
							}
							token = ALexico.getInstance().obtenerToken();
							if (!hayErrorLexico(token) && comparaTokens(token.getCategoria(), EnumToken.CA)) {
								token = ALexico.getInstance().obtenerToken();
								if (compruebaTipos(reconoceExpresion(), t.dameTipo("integer", nivel))) {
									if (!hayErrorLexico(token) && compruebaTokens(token.getCategoria(), EnumToken.CC)) {
										PropTipos param = ((PropTiposPro) t.dameTipo(asignada, nivel)).getParametros().get(i).getTipo();
										PropTipos parametroEsperado = ((PropTiposArray) t.dameTipo(nombreParam, nivel)).getRef();
										if (compruebaTipos(param, parametroEsperado)) {
											if (((PropTiposPro) t.dameTipo(asignada, nivel)).getParametros().get(i).getModo() == Modo.VARIABLE) {
												if (t.ambitoConstante(nombreParam, nivel)) {
													throw new SintacticException("Constante pasada por variable ", ALexico.getInstance().getLinea());
												}
											}
											token = ALexico.getInstance().obtenerToken();
											if (!hayErrorLexico(token) && comparaTokens(token.getCategoria(), EnumToken.COMA)) {
												token = ALexico.getInstance().obtenerToken();
											}
											i++;

										}
									}
								}
							} else if (!hayErrorLexico(token) && comparaTokens(token.getCategoria(), EnumToken.TPUNTERO)) {
								token = ALexico.getInstance().obtenerToken();
								if (!hayErrorLexico(token) && comparaTokens(token.getCategoria(), EnumToken.CA)) {
									token = ALexico.getInstance().obtenerToken();
									if (compruebaTipos(reconoceExpresion(), t.dameTipo("integer", nivel))) {
										if (!hayErrorLexico(token) && compruebaTokens(token.getCategoria(), EnumToken.CC)) {
											PropTipos param = ((PropTiposPro) t.dameTipo(asignada, nivel)).getParametros().get(i).getTipo();
											PropTipos parametroEsperado = ((PropTiposArray) ((PropTiposPointer) t.dameTipo(nombreParam, nivel))
													.getRef()).getRef();
											if (compruebaTipos(param, parametroEsperado)) {
												if (((PropTiposPro) t.dameTipo(asignada, nivel)).getParametros().get(i).getModo() == Modo.VARIABLE) {
													if (t.ambitoConstante(nombreParam, nivel)) {
														throw new SintacticException("Constante pasada por variable en línea ", ALexico.getInstance()
																.getLinea());
													}
												}
												token = ALexico.getInstance().obtenerToken();
												if (!hayErrorLexico(token) && comparaTokens(token.getCategoria(), EnumToken.COMA)) {
													token = ALexico.getInstance().obtenerToken();
												}
												i++;

											}
										}
									}
								} else if (!hayErrorLexico(token)
										&& (comparaTokens(token.getCategoria(), EnumToken.COMA) || comparaTokens(token.getCategoria(), EnumToken.PC))) {
									PropTipos param = ((PropTiposPro) t.dameTipo(asignada, nivel)).getParametros().get(i).getTipo();
									PropTipos parametroEsperado = ((PropTiposPointer) t.dameTipo(nombreParam, nivel)).getRef();
									if (compruebaTipos(param, parametroEsperado)) {
										if (((PropTiposPro) t.dameTipo(asignada, nivel)).getParametros().get(i).getModo() == Modo.VARIABLE) {
											if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.COMA))) {
												token = ALexico.getInstance().obtenerToken();
											}
										} else {
											if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.COMA))) {
												token = ALexico.getInstance().obtenerToken();
											}
										}
										i++;
									}
								} else {
									throw new SintacticException("Token no esperado ", ALexico.getInstance().getLinea());
								}
							} else if (!hayErrorLexico(token)
									&& (comparaTokens(token.getCategoria(), EnumToken.PC) || comparaTokens(token.getCategoria(), EnumToken.COMA))) {

								PropTipos param = ((PropTiposPro) t.dameTipo(asignada, nivel)).getParametros().get(i).getTipo();
								PropTipos parametroEsperado = t.dameTipo(nombreParam, nivel);
								if (compruebaTipos(param, parametroEsperado)) {
									if (((PropTiposPro) t.dameTipo(asignada, nivel)).getParametros().get(i).getModo() == Modo.VARIABLE) {
										if (t.ambitoConstante(nombreParam, nivel)) {
											throw new SintacticException("Constante pasada por variable  ", ALexico.getInstance().getLinea());
										}
									}
									if (!hayErrorLexico(token) && comparaTokens(token.getCategoria(), EnumToken.COMA)) {
										token = ALexico.getInstance().obtenerToken();
									}
									i++;
								}
							} else {
								throw new SintacticException("Parametro no valido ", ALexico.getInstance().getLinea());

							}
						} else if (!hayErrorLexico(token) && comparaTokens(token.getCategoria(), EnumToken.TRUE)) {
							PropTipos param = ((PropTiposPro) t.dameTipo(asignada, nivel)).getParametros().get(i).getTipo();
							if (compruebaTipos(param, t.dameTipo("boolean", nivel))) {
								if (((PropTiposPro) t.dameTipo(asignada, nivel)).getParametros().get(i).getModo() == Modo.VARIABLE) {
									throw new SintacticException("Constante pasada por variable en línea ", ALexico.getInstance().getLinea());
								} else {
									token = ALexico.getInstance().obtenerToken();
									if (!hayErrorLexico(token) && comparaTokens(token.getCategoria(), EnumToken.COMA)) {
										token = ALexico.getInstance().obtenerToken();
									}
									i++;
								}
							}
						} else if (!hayErrorLexico(token) && comparaTokens(token.getCategoria(), EnumToken.FALSE)) {
							PropTipos param = ((PropTiposPro) t.dameTipo(asignada, nivel)).getParametros().get(i).getTipo();
							if (compruebaTipos(param, t.dameTipo("boolean", nivel))) {
								if (((PropTiposPro) t.dameTipo(asignada, nivel)).getParametros().get(i).getModo() == Modo.VARIABLE) {
									throw new SintacticException("Constante pasada por variable", ALexico.getInstance().getLinea());
								} else {
									token = ALexico.getInstance().obtenerToken();
									if (!hayErrorLexico(token) && comparaTokens(token.getCategoria(), EnumToken.COMA)) {
										token = ALexico.getInstance().obtenerToken();
									}
									i++;
								}
							}
						} else if (!hayErrorLexico(token) && compruebaTokens(token.getCategoria(), EnumToken.DIGITO)) {
							PropTipos param = ((PropTiposPro) t.dameTipo(asignada, nivel)).getParametros().get(i).getTipo();
							if (compruebaTipos(param, t.dameTipo("integer", nivel))) {
								if (((PropTiposPro) t.dameTipo(asignada, nivel)).getParametros().get(i).getModo() == Modo.VARIABLE) {
									throw new SintacticException("Constante pasada por variable ", ALexico.getInstance().getLinea());
								} else {
									token = ALexico.getInstance().obtenerToken();
									if (!hayErrorLexico(token) && comparaTokens(token.getCategoria(), EnumToken.COMA)) {
										token = ALexico.getInstance().obtenerToken();
									}
									i++;
								}
							}
						}

					}

					// Aqui nosotros emitimos la llamada
					String codigo = "ir-a(" + t.obtenerInfo(asignada, nivel).dameDir() + ");.";
					emitirCodigo(codigo);
					// fin del bucle de reconocimiento de parámetros
					if (i != nParam) {
						throw new SintacticException("Número de parámetros incorrecto ", ALexico.getInstance().getLinea());
					}
					token = ALexico.getInstance().obtenerToken();
					if (comparaTokens(token.getCategoria(), EnumToken.PYCOMA)) {
						reconocePuntoYComa();
					}
				} else if (IDDeclarado(asignada) && comparaTokens(token.getCategoria(), EnumToken.PYCOMA)) {
					reconocePuntoYComa();
					apilaRet(contadorInstrucciones + 6);
					String codigo = "ir-a(" + TablaSimbolos.getInstance().obtenerInfo(asignada, nivel).dameDir() + ");.";
					emitirCodigo(codigo);
				} else
					throw new SintacticException("Instrucción no valida", ALexico.getInstance().getLinea());

			} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.ESCRIBIR))) {
				reconoceEscritura();
				String codigoEmitido = "write;.";
				emitirCodigo(codigoEmitido);
			} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.LEER))) {
				reconoceLectura();
				String codigoEmitido = "read;.";
				emitirCodigo(codigoEmitido);
				codigoEmitido = "desapilaIndice;.";
				emitirCodigo(codigoEmitido);
			} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.SI))) {
				token = ALexico.getInstance().obtenerToken();
				if (compruebaTipos(reconoceExpresion(), t.dameTipo("boolean", nivel))) {
					String codigoEmitido = "ir-f();.";
					emitirCodigo(codigoEmitido);
					int etq1 = contadorInstrucciones;
					// salvar el ccpp para luego parchear
					if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.ENTONCES))) {
						reconoceCuerpoBucle();
						parchea(etq1, contadorInstrucciones + 1);
					}
					if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.SINO))) {
						codigoEmitido = "ir-a();.";
						emitirCodigo(codigoEmitido);
						int etq2 = contadorInstrucciones;
						// salvar ccpp para luego parchear
						reconoceCuerpoBucle();
						parchea(etq2, contadorInstrucciones);
						// reconocePuntoYComa();

					}
					// else{reconocePuntoYComa();}
				}
			} else if (!hayErrorLexico(token) && comparaTokens(token.getCategoria(), EnumToken.NUEVO)) {
				token = ALexico.getInstance().obtenerToken();
				if (!hayErrorLexico(token) && compruebaTokens(token.getCategoria(), EnumToken.PA)) {
					token = ALexico.getInstance().obtenerToken();
					if (!hayErrorLexico(token) && compruebaTokens(token.getCategoria(), EnumToken.ID)) {
						String nombreVar = token.getLexema();
						if (t.dameTipo(nombreVar, nivel).getNombreTipo() == TTipo.PUNTERO) {
							token = ALexico.getInstance().obtenerToken();
							if (!hayErrorLexico(token) && compruebaTokens(token.getCategoria(), EnumToken.PC)) {
								int contador = 0;
								PropTiposPointer aux;
								boolean sigue = true;
								aux = (PropTiposPointer) TablaSimbolos.getInstance().dameTipo(nombreVar, nivel);
								while (sigue) {
									sigue = aux.getRef().getNombreTipo() == TTipo.PUNTERO;
									contador++;
									sigue = sigue && contador < 6;
								}
								if (contador == 6) {
									throw new SintacticException("Tipos mutuamente recursivos no soportados ", ALexico.getInstance().getLinea());
								} else {
									contador = aux.getRef().getTam();
								}
								String codigo = "new(" + contador + ");.";
								emitirCodigo(codigo);
								// Guardar su dire
								codigo = "apilaDireccion(" + (1 + nivel) + ");.";
								emitirCodigo(codigo);
								codigo = "apila(" + (TablaSimbolos.getInstance().obtenerInfo(nombreVar, nivel).dameDir() - 1) + ");.";
								emitirCodigo(codigo);
								codigo = "suma;.";
								emitirCodigo(codigo);
								codigo = "flip;.";
								emitirCodigo(codigo);
								codigo = "desapilaIndice;.";
								emitirCodigo(codigo);
								token = ALexico.getInstance().obtenerToken();
								reconocePuntoYComa();
							}
						}
					}
				}
			}

			else if (!hayErrorLexico(token) && comparaTokens(token.getCategoria(), EnumToken.LIBERAR)) {
				token = ALexico.getInstance().obtenerToken();
				if (!hayErrorLexico(token) && compruebaTokens(token.getCategoria(), EnumToken.PA)) {
					token = ALexico.getInstance().obtenerToken();
					if (!hayErrorLexico(token) && compruebaTokens(token.getCategoria(), EnumToken.ID)) {
						String nombreVar = token.getLexema();
						if (t.dameTipo(nombreVar, nivel).getNombreTipo() == TTipo.PUNTERO) {
							token = ALexico.getInstance().obtenerToken();
							if (!hayErrorLexico(token) && compruebaTokens(token.getCategoria(), EnumToken.PC)) {
								// ///
								String codigo = "apilaDireccion(" + (1 + nivel) + ");.";
								emitirCodigo(codigo);
								codigo = "apila(" + (TablaSimbolos.getInstance().obtenerInfo(nombreVar, nivel).dameDir() - 1) + ");.";
								emitirCodigo(codigo);
								codigo = "suma;.";
								emitirCodigo(codigo);
								codigo = "copia;.";
								emitirCodigo(codigo);
								codigo = "apilaIndice;.";
								emitirCodigo(codigo);
								codigo = "del(" + TablaSimbolos.getInstance().dameTipo(nombreVar, nivel).getTam() + ");.";
								emitirCodigo(codigo);
								codigo = "apila(null);.";
								emitirCodigo(codigo);
								codigo = "desapilaIndice;.";
								emitirCodigo(codigo);
								token = ALexico.getInstance().obtenerToken();
								reconocePuntoYComa();
							}
						}
					}
				}

			} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.MIENTRAS))) {
				token = ALexico.getInstance().obtenerToken();
				// salvar el ccpp para luego parchear
				int etq0 = contadorInstrucciones;
				if (compruebaTipos(reconoceExpresion(), t.dameTipo("boolean", nivel))) {
					String codigoEmitido = "ir-f();.";
					int etq1 = contadorInstrucciones;
					emitirCodigo(codigoEmitido);

					if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.DO))) {
						reconoceCuerpoBucle();

						codigoEmitido = "ir-a(" + (etq0) + ");.";
						emitirCodigo(codigoEmitido);
						parchea(etq1 + 1, contadorInstrucciones);
					}
					// parchea ir-f
				}
			} else
				throw new SintacticException("Comienzo de instrucción no valido", ALexico.getInstance().getLinea());
		} catch (NullPointerException npe) {
			throw new SintacticException("Variable no declarada.", ALexico.getInstance().getLinea());
		}
	}

	private void parchea(int etq1, int contadorInstrucciones) {
		String instruccion = codigoTexto.get(etq1 - 1);
		// Se trata de una instruccion ir-f
		if (instruccion.contains("f")) {
			instruccion = "ir-f(" + contadorInstrucciones + ")";
			codigoTexto.remove(etq1 - 1);
			codigoTexto.add(etq1 - 1, instruccion);
		}
		// y aqui se trata ir-a
		else {
			instruccion = "ir-a(" + contadorInstrucciones + ")";
			codigoTexto.remove(etq1 - 1);
			codigoTexto.add(etq1 - 1, instruccion);
		}
	}

	/**
	 * Reconoce el cuerpo de un bucle
	 * @throws Exception
	 */
	private void reconoceCuerpoBucle() throws Exception {

		token = ALexico.getInstance().obtenerToken();
		if (!hayErrorLexico(token) && comparaTokens(token.getCategoria(), EnumToken.INICIO)) {
			if (!hayErrorLexico(token) && !comparaTokens(token.getCategoria(), EnumToken.FIN)) {
				token = ALexico.getInstance().obtenerToken();
				reconoceInstrucciones();
				token = ALexico.getInstance().obtenerToken();
				if (!hayErrorLexico(token) && comparaTokens(token.getCategoria(), EnumToken.PYCOMA)) {
					reconocePuntoYComa();
				}
			}
		} else {
			reconoceInstruccion();
		}

	}

	private void reconoceRestoInstrucciones() throws Exception {
		while (!hayErrorLexico(token) && (!comparaTokens(token.getCategoria(), EnumToken.FIN))) {
			reconoceInstruccion();
		}
	}

	/**
	 * Reconoce un ; 
	 * @throws Exception
	 */
	private void reconocePuntoYComa() throws Exception {
		if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.PYCOMA))) {
			while (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.PYCOMA))) {
				token = ALexico.getInstance().obtenerToken();
			}
		}
	}

	/**
	 * Reconoce la instruccion "read"
	 * @throws Exception
	 */
	private void reconoceLectura() throws Exception {
		token = ALexico.getInstance().obtenerToken();
		TablaSimbolos t = TablaSimbolos.getInstance();
		if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.PA))) {
			token = ALexico.getInstance().obtenerToken();
			if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.ID))) {
				String asignada = token.getLexema();
				token = ALexico.getInstance().obtenerToken();
				if (comparaTokens(token.getCategoria(), EnumToken.PC)) {
					// es una variable normal, se queda como en la primera
					// version,
					if (IDDeclarado(asignada) && (t.obtenerInfo(asignada, nivel).dameElemento() == TElemento.VAR)) {
						if (compruebaTiposNoExcepcion(t.dameTipo("integer", nivel), t.dameTipo(asignada, nivel))
								|| compruebaTiposNoExcepcion(t.dameTipo("boolean", nivel), t.dameTipo(asignada, nivel))) {
							// modificaciones para el read
							String codigo = "apilaDireccion(" + (1 + (t.dameNivel(asignada, nivel))) + ");.";
							emitirCodigo(codigo);
							// fin mod
						} else
							throw new SintacticException("Esperado tipo Integer o Boolean", ALexico.getInstance().getLinea());
					}
				}
			}

			if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.PC))) {
				token = ALexico.getInstance().obtenerToken();
				reconocePuntoYComa();
			}
		}
	}

	/**
	 * Reconoce la instruccion write
	 * @throws Exception
	 */
	private void reconoceEscritura() throws Exception {
		token = ALexico.getInstance().obtenerToken();
		if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.PA))) {
			token = ALexico.getInstance().obtenerToken();
			reconoceExpresion();
			if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.PC))) {
				token = ALexico.getInstance().obtenerToken();
				reconocePuntoYComa();
				/*
				 * String codigo="write;."; emitirCodigo(codigo);
				 */
			}
		}
	}

	/**
	 * Reconoce una expresion
	 * @return
	 * @throws Exception
	 */
	private PropTipos reconoceExpresion() throws Exception {
		PropTipos tp = null;
		PropTipos tp2 = null;
		tp = reconoceExpresionSuma();
		tp2 = reconoceRestoExpresion(tp);
		return tp2;
	}

	@SuppressWarnings("unused")
	private PropTipos reconoceRestoExpresion(PropTipos t) throws Exception {
		TablaSimbolos tabla = TablaSimbolos.getInstance();
		PropTipos tp = null;
		PropTipos tp2 = null;

		if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.IGUAL))) {
			token = ALexico.getInstance().obtenerToken();
			tp = reconoceExpresionSuma();

			if (compruebaTipos(t, tp)) {
				String codigoEmitido = "igual;.";
				emitirCodigo(codigoEmitido);
				tp2 = reconoceRestoExpresion(t);
				return tabla.dameTipo("boolean", nivel);
			}
		} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.MENOR))) {
			token = ALexico.getInstance().obtenerToken();
			tp = reconoceExpresionSuma();
			if (compruebaTipos(t, tp)) {
				String codigoEmitido = "menor;.";
				emitirCodigo(codigoEmitido);
				tp2 = reconoceRestoExpresion(t);
				return tabla.dameTipo("boolean", nivel);
			}
		} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.MAYOR))) {
			token = ALexico.getInstance().obtenerToken();
			tp = reconoceExpresionSuma();
			if (compruebaTipos(t, tp)) {
				String codigoEmitido = "mayor;.";
				emitirCodigo(codigoEmitido);
				tp2 = reconoceRestoExpresion(t);
				return tabla.dameTipo("boolean", nivel);
			}
		} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.MENORIGUAL))) {
			token = ALexico.getInstance().obtenerToken();
			tp = reconoceExpresionSuma();
			if (compruebaTipos(t, tp)) {
				String codigoEmitido = "menorIgual;.";
				emitirCodigo(codigoEmitido);
				tp2 = reconoceRestoExpresion(t);
				return tabla.dameTipo("boolean", nivel);
			}
		} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.MAYORIGUAL))) {
			token = ALexico.getInstance().obtenerToken();
			tp = reconoceExpresionSuma();
			if (compruebaTipos(t, tp)) {
				String codigoEmitido = "mayorIgual;.";
				emitirCodigo(codigoEmitido);
				tp2 = reconoceRestoExpresion(t);
				return tabla.dameTipo("boolean", nivel);
			}
		} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.DISTINTO))) {
			token = ALexico.getInstance().obtenerToken();
			tp = reconoceExpresionSuma();
			if (compruebaTipos(t, tp)) {
				String codigoEmitido = "distinto;.";
				emitirCodigo(codigoEmitido);
				tp2 = reconoceRestoExpresion(t);
				return tabla.dameTipo("boolean", nivel);
			}
		}
		return t;
	}

	/**
	 * Reconoce una expresion suma
	 * @return
	 * @throws Exception
	 */
	private PropTipos reconoceExpresionSuma() throws Exception {
		PropTipos tp = null;
		PropTipos tp2 = null;

		tp = reconoceExpresionProducto();
		tp2 = reconoceRestoExpresionSum(tp);
		return tp2;
	}


	@SuppressWarnings("unused")
	private PropTipos reconoceRestoExpresionSum(PropTipos t) throws Exception {
		PropTipos tp = null;
		PropTipos tp2 = null;
		TablaSimbolos tabla = TablaSimbolos.getInstance();
		if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.SUMA))) {
			token = ALexico.getInstance().obtenerToken();
			tp = reconoceExpresionProducto();

			if (compruebaTipos(t, tp)) {
				String codigoEmitido = "suma;.";
				emitirCodigo(codigoEmitido);
				tp2 = reconoceRestoExpresionSum(tp);
				return tabla.dameTipo("integer", nivel);
			}
		} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.RESTA))) {
			token = ALexico.getInstance().obtenerToken();
			tp = reconoceExpresionProducto();
			if (compruebaTipos(t, tp)) {
				String codigoEmitido = "resta;.";
				emitirCodigo(codigoEmitido);
				tp2 = reconoceRestoExpresionSum(tp);
				return tabla.dameTipo("integer", nivel);
			}

		} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.OR))) {
			token = ALexico.getInstance().obtenerToken();
			tp = reconoceExpresionProducto();
			if (compruebaTipos(t, tp)) {
				String codigoEmitido = "or;.";
				emitirCodigo(codigoEmitido);
				tp2 = reconoceRestoExpresionSum(tp);
				return tabla.dameTipo("boolean", nivel);
			}
		}
		return t;
	}

	/**
	 * Reconoce expresion producto
	 * @return
	 * @throws Exception
	 */
	private PropTipos reconoceExpresionProducto() throws Exception {
		PropTipos tp = null;
		PropTipos tp2 = null;
		tp = reconoceExpresionNeg();
		tp2 = reconoceRestoExpresionProducto(tp);

		return tp2;
	}

	@SuppressWarnings("unused")
	private PropTipos reconoceRestoExpresionProducto(PropTipos t) throws Exception {
		PropTipos tp = null;
		PropTipos tp2 = null;
		TablaSimbolos tabla = TablaSimbolos.getInstance();
		if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.MUL))) {
			token = ALexico.getInstance().obtenerToken();
			tp = reconoceExpresionNeg();
			if (compruebaTipos(t, tp)) {
				String codigoEmitido = "producto;.";
				emitirCodigo(codigoEmitido);
				tp2 = reconoceRestoExpresionProducto(tp);
				return tabla.dameTipo("integer", nivel);
			}

		} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.DIV))) {
			token = ALexico.getInstance().obtenerToken();
			tp = reconoceExpresionNeg();
			if (compruebaTipos(t, tp)) {
				String codigoEmitido = "division;.";
				emitirCodigo(codigoEmitido);
				tp2 = reconoceRestoExpresionProducto(tp);
				return tabla.dameTipo("integer", nivel);
			}
		} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.AND))) {
			token = ALexico.getInstance().obtenerToken();
			tp = reconoceExpresionNeg();
			if (compruebaTipos(t, tp)) {
				String codigoEmitido = "and;.";
				emitirCodigo(codigoEmitido);
				tp2 = reconoceRestoExpresionProducto(tp);
				return tabla.dameTipo("boolean", nivel);
			}
		} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.MODULO))) {
			token = ALexico.getInstance().obtenerToken();
			tp = reconoceExpresionNeg();
			if (compruebaTipos(t, tp)) {
				String codigoEmitido = "moduloEntero;.";
				emitirCodigo(codigoEmitido);
				tp2 = reconoceRestoExpresionProducto(tp);
				return tabla.dameTipo("integer", nivel);
			}
		}

		return t;
	}

	/**
	 * Reconoce la expresion unaria de negacion
	 * @return
	 * @throws Exception
	 */
	private PropTipos reconoceExpresionNeg() throws Exception {
		PropTipos tp;
		TablaSimbolos tabla = TablaSimbolos.getInstance();
		if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.RESTA))) {
			token = ALexico.getInstance().obtenerToken();
			tp = reconoceExpresionProducto();
			String codigoEmitido = "restaUnario;.";
			emitirCodigo(codigoEmitido);
			return tp;
		} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.SUMA))) {
			token = ALexico.getInstance().obtenerToken();
			tp = reconoceExpresionProducto();
			String codigoEmitido = "sumaUnario;.";
			emitirCodigo(codigoEmitido);
			return tp;
		} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.NOT))) {
			token = ALexico.getInstance().obtenerToken();
			tp = reconoceExpresion();
			if (compruebaTipos(tp, tabla.dameTipo("boolean", nivel))) {
				String codigoEmitido = "negacion;.";
				emitirCodigo(codigoEmitido);
				return tp;
			}
		} else if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.PA))) {
			token = ALexico.getInstance().obtenerToken();
			tp = reconoceExpresion();
			compruebaTokens(token.getCategoria(), EnumToken.PC);
			token = ALexico.getInstance().obtenerToken();
			return tp;
		} else if (!hayErrorLexico(token) && comparaTokens(token.getCategoria(), EnumToken.DIGITO)) {
			String codigoEmitido = "apila(" + token.getLexema() + ");.";
			emitirCodigo(codigoEmitido);
			token = ALexico.getInstance().obtenerToken();
			return tabla.dameTipo("integer", nivel);
		} else if (!hayErrorLexico(token) && comparaTokens(token.getCategoria(), EnumToken.NIL)) {
			// emitir codigo
			token = ALexico.getInstance().obtenerToken();
			return new PropTipos(TTipo.VACIO, 0);
		} else if (!hayErrorLexico(token) && comparaTokens(token.getCategoria(), EnumToken.TRUE)) {
			String codigoEmitido = "apila(TRUE);.";
			emitirCodigo(codigoEmitido);
			token = ALexico.getInstance().obtenerToken();
			return tabla.dameTipo("boolean", nivel);
		} else if (!hayErrorLexico(token) && comparaTokens(token.getCategoria(), EnumToken.FALSE)) {
			String codigoEmitido = "apila(FALSE);.";
			emitirCodigo(codigoEmitido);
			token = ALexico.getInstance().obtenerToken();
			return tabla.dameTipo("boolean", nivel);
		}

		if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.ID))) {
			String asignado = token.getLexema();
			if (tabla.existeConstante(asignado, nivel) || tabla.existeVariable(asignado, nivel)) {

				token = ALexico.getInstance().obtenerToken();

				// al avanzar, el token se queda apuntando a lo siguiente al
				// nombre, que será, o un puntero, o un array, o el parentesis
				// de cierre
				if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.CA))) {

					token = ALexico.getInstance().obtenerToken();
					if (compruebaTipos(reconoceExpresion(), tabla.dameTipo("integer", nivel))) {
						if (compruebaTokens(token.getCategoria(), EnumToken.CC)) {
							String codigo = "apilaDireccion(" + (1 + (tabla.dameNivel(asignado, nivel))) + ");.";
							emitirCodigo(codigo);
							codigo = "apila(" + (tabla.obtenerInfo(asignado, nivel).dameDir() - 1) + ");.";
							emitirCodigo(codigo);
							codigo = "suma;.";
							emitirCodigo(codigo);
							codigo = "suma;.";
							emitirCodigo(codigo);
							codigo = "apilaIndice;.";
							emitirCodigo(codigo);
							token = ALexico.getInstance().obtenerToken();
							return ((PropTiposArray) tabla.dameTipo(asignado, nivel)).getRef();
						}
					}
				} else if (!hayErrorLexico(token) && comparaTokens(token.getCategoria(), EnumToken.TPUNTERO)) {
					// es un puntero
					token = ALexico.getInstance().obtenerToken();
					if (!hayErrorLexico(token) && (comparaTokens(token.getCategoria(), EnumToken.CA))) {
						token = ALexico.getInstance().obtenerToken();
						if (compruebaTipos(reconoceExpresion(), tabla.dameTipo("integer", nivel))) {
							if (compruebaTokens(token.getCategoria(), EnumToken.CC)) {
								// emitir codigo
								token = ALexico.getInstance().obtenerToken();
								return ((PropTiposArray) ((PropTiposPointer) tabla.dameTipo(asignado, nivel)).getRef()).getRef();
							}
						}
					} else {
						String codigo = "apilaDireccion(" + (1 + (tabla.dameNivel(asignado, nivel))) + ");.";
						emitirCodigo(codigo);
						codigo = "apila(" + (tabla.obtenerInfo(asignado, nivel).dameDir() - 1) + ");.";
						emitirCodigo(codigo);
						codigo = "suma;.";
						emitirCodigo(codigo);
						codigo = "apilaIndice;.";
						emitirCodigo(codigo);
						codigo = "apilaIndice;.";
						emitirCodigo(codigo);
						return ((PropTiposPointer) tabla.dameTipo(asignado, nivel)).getRef();
					}
				}
			} else
				throw new SintacticException("El id \"" + token.getLexema() + "\" no existe", ALexico.getInstance().getLinea());

			// Escritura
			String codigo = "apilaDireccion(" + (1 + (tabla.dameNivel(asignado, nivel))) + ");.";
			emitirCodigo(codigo);
			codigo = "apila(" + (tabla.obtenerInfo(asignado, nivel).dameDir() - 1) + ");.";
			emitirCodigo(codigo);
			codigo = "suma;.";
			emitirCodigo(codigo);
			codigo = "apilaIndice;.";
			emitirCodigo(codigo);
			if (nivel > 0) {
				PropTiposPro aux = ((PropTiposPro) tabla.dameTipo(nombreAmbito, nivel));
				String aux2 = "";
				for (int i = 0; i < aux.getParametros().size(); i++) {
					aux2 = aux.getParametros().get(i).getNombre();
					if ((aux2.equals(asignado)) && (aux.getParametros().get(i).getModo() == Modo.VARIABLE)) {
						codigo = "apilaIndice;.";
						emitirCodigo(codigo);
					}
				}
			}
			return tabla.dameTipo(asignado, nivel);
		} else if (!hayErrorLexico(token) && comparaTokens(token.getCategoria(), EnumToken.SUMA)) {
			token = ALexico.getInstance().obtenerToken();
			if (compruebaTokens(token.getCategoria(), EnumToken.DIGITO)) {
				String codigoEmitido = "apila(" + "-" + token.getLexema() + ");.";
				emitirCodigo(codigoEmitido);
				token = ALexico.getInstance().obtenerToken();
				return tabla.dameTipo("integer", nivel);
			}
		} else if (!hayErrorLexico(token) && compruebaTokens(token.getCategoria(), EnumToken.RESTA)) {
			token = ALexico.getInstance().obtenerToken();
			if (compruebaTokens(token.getCategoria(), EnumToken.DIGITO)) {
				String codigoEmitido = "apila(" + "-" + token.getLexema() + ");.";
				emitirCodigo(codigoEmitido);
				token = ALexico.getInstance().obtenerToken();
				return tabla.dameTipo("integer", nivel);
			}
		}

		return null;
	}

	/**
	 * Reconoce una asignacion
	 * @return
	 * @throws Exception
	 */
	private PropTipos reconoceAsignacion() throws Exception {

		PropTipos devolver = null;
		if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.ASIG))) {
			token = ALexico.getInstance().obtenerToken();
			devolver = reconoceExpresion();
		}
		return devolver;
	}

	/**
	 * Reconoce un identificador de la cabecera
	 * @throws Exception
	 */
	private void reconoceIdentificadorCabecera() throws Exception {
		token = ALexico.getInstance().obtenerToken();
		if (!hayErrorLexico(token) && (compruebaTokens(token.getCategoria(), EnumToken.ID))) {
			if (!existeID(token.getLexema())) {
				// Ver que se hace aqui con el indentiicador del programa
				// ya que ahora no tenemos el TError
				nombreAmbito = token.getLexema();
				TablaSimbolos ts = TablaSimbolos.getInstance();
				ts.añadeIdenProgram(token.getLexema(), 0, null);// A la espera
				// del
				// proptiposProc
				direccion++;
			} else {
				hayError = true;
			}
		}
	}
	/**
	 * Comprueba si el token recibido es el esperado segun el estado actual del analizador
	 * @param tokenRecibido
	 * @param tokenEsperado
	 * @return
	 */
	private boolean compruebaTokens(EnumToken tokenRecibido, EnumToken tokenEsperado) throws Exception {
		if (tokenRecibido != tokenEsperado) {
			String mensajeError = "";
			String cadenaToken = cadenaToken(tokenEsperado);
			mensajeError = mensajeError + "Se esperaba '" + cadenaToken + "'";
			hayError = true;
			throw new SintacticException(mensajeError, ALexico.getInstance().getLinea());
		}
		return true;
	}

	/**
	 * Comprueba si el token recibido es el esperado segun el estado actual del analizador
	 * @param tokenRecibido
	 * @param tokenEsperado
	 * @return
	 */
	private boolean comparaTokens(EnumToken tokenRecibido, EnumToken tokenEsperado) {
		return (tokenRecibido == tokenEsperado);
	}

	/**
	 * Relacion entre las cadenas internas usadas y los tokens
	 * @param tokenEsperado
	 * @return
	 */
	private String cadenaToken(EnumToken tokenEsperado) {
		switch (tokenEsperado) {
			case PROGRAM :
				return "PROGRAM";
			case INICIO :
				return "BEGIN";
			case FIN :
				return "END";
			case TVAR :
				return "VAR";
			case TIPENT :
				return "TIPO";
			case TIPBOOL :
				return "TIPO";
			case LEER :
				return "READ";
			case ESCRIBIR :
				return "WRITE";
			case TRUE :
				return "TRUE";
			case FALSE :
				return "FALSE";
			case NOT :
				return "NOT";
			case PA :
				return "(";
			case PC :
				return ")";
			case PUNTO :
				return ".";
			case COMA :
				return ";";
			case PYCOMA :
				return ";";
			case DOSPUNTOS :
				return ":";
			case ASIG :
				return ":=";
			case SUMA :
				return "+";
			case RESTA :
				return "-";
			case OR :
				return "OR";
			case MUL :
				return "*";
			case DIV :
				return "/";
			case AND :
				return "AND";
			case MODULO :
				return "MOD";
			case IGUAL :
				return "=";
			case DISTINTO :
				return "<>";
			case MAYOR :
				return ">";
			case MENOR :
				return "<";
			case MAYORIGUAL :
				return ">=";
			case MENORIGUAL :
				return "<=";
			case ID :
				return "Identificador";
			case DIGITO :
				return "Valor";
			case DO :
				return "DO";
			case ENTONCES :
				return "THEN";
			case PROC :
				return "PROCEDURE o BEGIN";
			default :
				return "";
		}
	}

	/**
	 * Busca un identificador en la tabla de simbolos en el nivel actual
	 * @param t Nombre de identificador
	 * @return true si existe o false si no existe
	 * @throws Exception
	 */
	private boolean existeIDNivel(String t) throws Exception {
		TablaSimbolos ts = TablaSimbolos.getInstance();
		if (ts.existeTipoNivel(t, nivel) || ts.existeVariableNivel(t, nivel) || ts.existeConstanteNivel(t, nivel)
				|| ts.existeProcedimientoNivel(t, nivel)) {
			String mensajeError = "Identificador '" + t + "' repetido";
			throw new SintacticException(mensajeError, ALexico.getInstance().getLinea());
		}
		return false;
	}

	/**
	 * Busca un identificador en la tabla de simbolos a nivel global
	 * @param t Nombre de identificador
	 * @return true si existe o false si no existe
	 * @throws Exception
	 */
	private boolean existeID(String t) throws Exception {
		TablaSimbolos ts = TablaSimbolos.getInstance();
		if (ts.existeTipo(t, nivel) || ts.existeVariable(t, nivel) || ts.existeConstante(t, nivel) || ts.existeProcedimiento(t, nivel)) {
			String mensajeError = "Identificador '" + t + "' repetido";
			throw new SintacticException(mensajeError, ALexico.getInstance().getLinea());
		}
		return false;
	}

	private boolean IDDeclarado(String t) throws Exception {

		TablaSimbolos ts = TablaSimbolos.getInstance();
		if (!ts.existeVariable(t, nivel)) {
			String mensajeError = "El identificador '" + t + "' no ha sido declarado";
			throw new SintacticException(mensajeError, ALexico.getInstance().getLinea());
		}
		return true;
	}

	/**
	 * Busca si existe un tipo
	 * @param nombre Nombre del tipo
	 * @return true si existe, false caso contrario
	 * @throws Exception
	 */
	private boolean existeTipo(String nombre) throws Exception {
		TablaSimbolos ts = TablaSimbolos.getInstance();
		if (!ts.existeTipo(nombre, nivel)) {
			String mensajeError = "El tipo '" + nombre + "' no ha sido declarado";
			throw new SintacticException(mensajeError, ALexico.getInstance().getLinea());
		}
		return true;
	}

	private boolean hayErrorLexico(Token t) {
		if (t == null) {
			hayError = true;
			return true;
		}
		return false;
	}

	private boolean compatibleTipos2(PropTipos e1, PropTipos e2, ArrayList<ParejaTipos> visitadas) throws Exception {
		// Creamos la pareja de los tipos para insertarla o no luego
		ParejaTipos par = new ParejaTipos(e1, e2);

		// Si esa pareja ya se ha visitado... devolvemos true
		if (visitadas.contains(par)) {
			return true;
		}

		// sino, la añadimos
		else
			visitadas.add(par);

		// Si los dos tipos de la pareja son tipos básicos devolvemos cierto

		// si son tipos basicos...devuelve true
		if ((e1.getNombreTipo() == TTipo.INTEGER && e2.getNombreTipo() == TTipo.INTEGER)
		||	(e1.getNombreTipo() == TTipo.BOOLEAN && e2.getNombreTipo() == TTipo.BOOLEAN)
		||	(e1.getNombreTipo() == TTipo.VACIO && e2.getNombreTipo() == TTipo.VACIO)
		) {
			return true;
		}

		// Si no son tipos simples, hay que ir a por sus referencias

		// Si el tipo referido es el primero, e1...
		else if (e1.getNombreTipo() == TTipo.CONSTRUIDO)
			return compatibleTipos2(((PropTiposNombrado) e1).getRef(), e2, visitadas);

		// Si el tipo referido es el segundo, e2...
		else if (e2.getNombreTipo() == TTipo.CONSTRUIDO)
			return compatibleTipos2(e1, ((PropTiposNombrado) e2).getRef(), visitadas);

		// Si los dos tipos son arrays ...
		else if (((e1.getNombreTipo() == TTipo.ARRAY && e2.getNombreTipo() == TTipo.ARRAY))
		&&
		// y tienen el mismo numero de elementos
				((PropTiposArray) e1).getTam() == ((PropTiposArray) e2).getTam())
			return compatibleTipos2(((PropTiposArray) e1).getRef(), ((PropTiposArray) e2).getRef(), visitadas);

		// Si son punteros los dos....
		else if ((e1.getNombreTipo() == TTipo.PUNTERO && e2.getNombreTipo() == TTipo.PUNTERO))

			return compatibleTipos2(((PropTiposPointer) e1).getRef(), ((PropTiposPointer) e2).getRef(), visitadas);

		else
			return false;

	}


	/**
	 * Comprueba si dos tipos son compatibles
	 * @param e1 Tipo 1
	 * @param e2 Tipo 2
	 * @return True si son compatibles, false caso contrario
	 * @throws Exception
	 */
	private boolean compruebaTipos(PropTipos e1, PropTipos e2) throws Exception {
		boolean compatible = false;
		ArrayList<ParejaTipos> visitadas = new ArrayList<ParejaTipos>();
		compatible = compatibleTipos2(e1, e2, visitadas);
		if (compatible)
			return true;
		else {
			throw new SintacticException("Tipos no compatibles", ALexico.getInstance().getLinea());
		}
	}

	/**
	 * Comprueba si dos tipos son compatibles pero sin lanzar una excepcion
	 * @param e1 Tipo 1
	 * @param e2 Tipo 2
	 * @return True si son compatibles, false caso contrario
	 * @throws Exception
	 */
	private boolean compruebaTiposNoExcepcion(PropTipos e1, PropTipos e2) {
		boolean compatible = false;
		ArrayList<ParejaTipos> visitadas = new ArrayList<ParejaTipos>();
		try {
			compatible = compatibleTipos2(e1, e2, visitadas);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (compatible)
			return true;
		else {
			return false;
		}
	}

	private void prologo(int nivel, int tamLocales) {
		String codigo = "apilaDireccion(0);.";
		emitirCodigo(codigo);
		codigo = "apila(2);.";
		emitirCodigo(codigo);
		codigo = "suma;.";
		emitirCodigo(codigo);
		codigo = "apilaDireccion(" + (nivel + 1) + ");.";
		emitirCodigo(codigo);
		codigo = "desapilaIndice;.";
		emitirCodigo(codigo);
		codigo = "apilaDireccion(0);.";
		emitirCodigo(codigo);
		codigo = "apila(3);.";
		emitirCodigo(codigo);
		codigo = "suma;.";
		emitirCodigo(codigo);
		codigo = "desapilaDireccion(" + (nivel + 1) + ");.";
		emitirCodigo(codigo);
		codigo = "apilaDireccion(0);.";
		emitirCodigo(codigo);
		codigo = "apila(" + (2 + tamLocales) + ");.";
		emitirCodigo(codigo);
		codigo = "suma;.";
		emitirCodigo(codigo);
		codigo = "desapilaDireccion(0);.";
		emitirCodigo(codigo);
	}

	private void epilogo(int nivel) {
		String codigo = "apilaDireccion(" + (nivel + 1) + ");.";
		emitirCodigo(codigo);
		codigo = "apila(2);.";
		emitirCodigo(codigo);
		codigo = "resta;.";
		emitirCodigo(codigo);
		codigo = "apilaIndice;.";
		emitirCodigo(codigo);
		codigo = "apilaDireccion(" + (nivel + 1) + ");.";
		emitirCodigo(codigo);
		codigo = "apila(3);.";
		emitirCodigo(codigo);
		codigo = "resta;.";
		emitirCodigo(codigo);
		codigo = "copia;.";
		emitirCodigo(codigo);
		codigo = "desapilaDireccion(0);.";
		emitirCodigo(codigo);
		codigo = "apila(2);.";
		emitirCodigo(codigo);
		codigo = "suma;.";
		emitirCodigo(codigo);
		codigo = "apilaIndice;.";
		emitirCodigo(codigo);
		codigo = "desapilaDireccion(" + (nivel + 1) + ");.";
		emitirCodigo(codigo);
	}

	private void apilaRet(int ret) {
		String codigo = "apilaDireccion(0);.";
		emitirCodigo(codigo);
		codigo = "apila(1);.";
		emitirCodigo(codigo);
		codigo = "suma;.";
		emitirCodigo(codigo);
		codigo = "apila(" + ret + ");.";
		emitirCodigo(codigo);
		codigo = "desapilaIndice;.";
		emitirCodigo(codigo);
	}

	@Override
	public void setCodigo(PrintWriter codigo) {
		this.ficheroSalida = codigo;

	}
}