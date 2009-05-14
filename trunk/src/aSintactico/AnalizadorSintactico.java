/**
 * Paquete que engloba las distintas clases que componen el módulo de análisis
 * sintáctico del lenguaje fuente del compilador.
 */
package aSintactico;

import java.io.FileReader;
import java.util.ArrayList;

import aLexico.*;

import mVirtual.CodigoObjeto;
import tablaSimbolos.*;
import tablaSimbolos.tipos.EntradaTS;
import tablaSimbolos.tipos.EntradaCampoRegistroTS;
import tablaSimbolos.tipos.EntradaProcTS;
import tablaSimbolos.tipos.EntradaPunteroTS;
import tablaSimbolos.tipos.EntradaRegistroTS;
import utils.*;
import utils.error.Error;
import utils.error.TError;

/**
 * Implementación del analizador sintáctico, que se encarga de generar el código
 * objeto a partir del código fuente y detectar y tratar los distintos errores
 * de compilación.
 */
public class AnalizadorSintactico  {

	/**
	 * Analizador léxico.
	 */
	private AnalizadorLexico aLexico = new AnalizadorLexico();

	/**
	 * Token actual.
	 */
	private TToken token;

	/**
	 * Código objeto resultante.
	 */
	private CodigoObjeto codObj;

	/**
	 * Detector de presencia de error.
	 */
	private boolean err = false;

	/**
	 * Información del error (Léxico, sintáctico, o semántico).
	 */
	private Error error = null;

	/**
	 * Módulo de restricciones contextuales.
	 */
	private RContextuales restricciones;

	/**
	 * Etiqueta.
	 */
	private int etq = 0;

	/**
	 * Nivel de anidamiento.
	 */
	private int nivel = 0;

	/**
	 * Dirección actual para cada nivel.
	 */
	private ArrayList<Integer> dirs;

	/**
	 * Tablas de símbolos para cada nivel.
	 */
	private ArrayList<TablaSimbolos> tablasDeSimbolos;

	/**
	 * Obtiene la tabla de símbolos principal del analizador.
	 * 
	 * @return Tabla de símbolos del programa principal.
	 */
	public TablaSimbolos getTablaSimbolos() {
		return tablasDeSimbolos.get(0);
	}

	/**
	 * Obtiene el código objeto del analizador.
	 * 
	 * @return Código objeto del analizador.
	 */
	public CodigoObjeto getCodigoObjeto() {
		return codObj;
	}

	/**
	 * Informa si hay errores (semánticos, sintácticos o léxicos) en el código
	 * que se está analizado.
	 * 
	 * @return Devuelve cierto en caso de que existan errores.
	 */
	public boolean getErrores() {
		return err;
	}

	/**
	 * Devuelve la informacion de un error (semánticos, sintácticos o léxicos)
	 * presente en el codigo.
	 * 
	 * @return Información acerca de un error presente en el código.
	 */
	public Error getError() {
		return error;
	}

	/**
	 * Crea un nuevo analizador léxico a partir del fichero fuente, y procede a
	 * realizar el análisis sintáctico del codigo fuente según la definición
	 * sintáctica.
	 * 
	 * @param ficheroFuente
	 *            Fichero con el código fuente.
	 */
	public void analizar(FileReader ficheroFuente) {
		// etiqueta
		etq = 0;
		// nivel
		nivel = 0;
		// Direccion
		Integer dir = new Integer(3);
		dirs = new ArrayList<Integer>();
		dirs.add(nivel, dir);
		// Detector de presencia de error
		err = false;
		// Información del error (Léxico, sintáctico, o semántico)
		error = null;
		// Le asignamos el fichero fuente
		((AnalizadorLexico) aLexico).setFichero(ficheroFuente);
		// Creamos una tabla de símbolos.
		tablasDeSimbolos = new ArrayList<TablaSimbolos>();
		TablaSimbolos tablaSim = new TablaSimbolos();
		tablasDeSimbolos.add(nivel, tablaSim);
		// Creamos un nuevo código objeto.
		codObj = new CodigoObjeto();
		// Creamos un módulo de restricciones contextuales.
		restricciones = new RContextuales();
		// Le asignamos la tabla de símbolos creada.
		restricciones.setTablasSimbolos(tablasDeSimbolos);
		// Comenzamos reconocimiento.
		reconoceProg();
	}

	/**
	 * Método que se encarga de reconocer la producción general de estructura de
	 * un programa: Prog := PROGRAMA Decs INICIO Ins FIN.
	 */
	private void reconoceProg() {
		etq = 0;
		token = aLexico.obtenerToken();
		if (!comprobarErrorLexico(token)) {
			if (reconoceTToken(token.getCategoria(), TToken.TKPROGRAMA)) {
				// añadimos la instrucción que se encargará de comenzar la
				// ejecución del código
				// por el programa principal
				codObj.añadirInstruccion("ir_a", Integer.toString(0));
				etq++;
				reconoceDecs();
				if (!err) {
					if (reconoceTToken(token.getCategoria(), TToken.TKINICIO)) {
						token = aLexico.obtenerToken();
						if (!comprobarErrorLexico(token)) {
							// parcheamos la instrucción 0 ir_a con la dirección
							// donde comienza el Programa principal
							codObj.parchea(0, etq);
							// tenemos que hacer que C apunte a la cima de la
							// pila.
							codObj.añadirInstruccion("incrementaC", Integer
									.toString(dirs.get(nivel)));
							etq++;
							reconoceIns();
							if (!err) {
								reconoceTToken(token.getCategoria(),
										TToken.TKFIN);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Método que se encarga de reconocer la producción: Decs := TIPO Tips RDecs |
	 * RDecs
	 */
	private void reconoceDecs() {
		if (!err) {
			token = ((AnalizadorLexico) aLexico).obtenerToken();
			if (!comprobarErrorLexico(token)) {
				if (token.getCategoria() == TToken.TKTIPO) {
					// TIPO Tips RTips
					token = aLexico.obtenerToken();
					if (!comprobarErrorLexico(token)) {
						reconoceTips();
						if (!err)
							reconoceRDecs();
					}
				} else {
					reconoceRDecs();
				}
			}
		}
	}

	/**
	 * Método que se encarga de reconocer la producción: RDecs := CONS Cons
	 * RRDecs | RRDecs
	 */
	private void reconoceRDecs() {
		if (!err) {
			if (!comprobarErrorLexico(token)) {
				// Evaluamos presencia de CONS.
				if (token.getCategoria() == (TToken.TKCONS)) {
					// Reconocemos el tipo.
					token = aLexico.obtenerToken();
					if (!comprobarErrorLexico(token)) {
						EntradaTS tipo = reconoceTipo();
						if (!err) {
							reconoceCons(tipo);
							if (!err)
								reconoceRRDecs();
						}
					}
				} else
					reconoceRRDecs();
			}
		}
	}

	/**
	 * Método que se encarga de reconocer la producción: RRDecs := VAR Vars
	 * RRRDecs
	 */
	private void reconoceRRDecs() {
		if (!err) {
			if (reconoceTToken(token.getCategoria(), TToken.TKVAR)) {
				// Reconocemos el tipo.
				token = aLexico.obtenerToken();
				if (!comprobarErrorLexico(token)) {
					EntradaTS tipo = reconoceTipo();
					if (!err) {
						reconoceVars(tipo);
						if (!err)
							reconoceRRRDecs();
					}
				}
			}
		}
	}

	/**
	 * Método que se encarga de reconocer la producción: RRRDecs :=
	 * PROCEDIMIENTO Procs | lambda
	 */
	private void reconoceRRRDecs() {
		if (!err) {
			if (token.getCategoria() == TToken.TKPROCEDIMIENTO) {
				token = aLexico.obtenerToken();
				if (!comprobarErrorLexico(token)) {
					// aumentamos en uno el nivel de declaración de
					// procedimientos
					nivel++;
					reconoceProcs();
					nivel--;
				}
			}
		}
	}

	/**
	 * Método que se encarga de reconocer la producción: Tips := Tip RTips
	 */
	private void reconoceTips() {
		if (!err) {
			reconoceTip();
			if (!err)
				reconoceRTips();
		}
	}

	/**
	 * Método que se encarga de reconocer la producción: RTips := Tips |lambda
	 */
	private void reconoceRTips() {
		if (!err) {
			token = ((AnalizadorLexico) aLexico).obtenerToken();
			if (!comprobarErrorLexico(token)) {
				// Queremos ver si reconoce un tipo o VAR o cons o lambda
				if ((token.getCategoria() == TToken.TKENT)
						|| (token.getCategoria() == TToken.TKLOG)
						|| (token.getCategoria() == TToken.TKIDEN)
						|| (token.getCategoria() == TToken.TKGORRO)
						|| (token.getCategoria() == TToken.TKREG)) {
					reconoceTips();
				}
			}
		}
	}

	/**
	 * Método que se encarga de reconocer la producción: Tip:= Tipo IDEN ;
	 */
	private void reconoceTip() {
		if (!err) {
			EntradaTS tipo = reconoceTipo();
			if (!err) {
				token = ((AnalizadorLexico) aLexico).obtenerToken();
				if (!comprobarErrorLexico(token)) {
					if (reconoceTToken(token.getCategoria(), TToken.TKIDEN)) {
						if (!restricciones.restriccionIden(token.getLexema(),
								nivel)) {
							tablasDeSimbolos.get(nivel).addTipoConstruido(
									token.getLexema(), tipo, dirs.get(nivel),
									nivel);
							dirs.add(nivel, dirs.get(nivel) + 1);
							token = ((AnalizadorLexico) aLexico).obtenerToken();
							if (!comprobarErrorLexico(token)) {
								if (!reconoceTToken(token.getCategoria(),
										TToken.TKPYC)) {
									err = true;
									error = new Error(TError.SINTACTICO,
											"Línea "
													+ ((AnalizadorLexico) aLexico)
															.getLinea() + ": "
													+ "Se esperaba ;");
								}
							}
						} else {
							err = true;
							String iden = token.getLexema();
							error = new Error(TError.SEMANTICO, "Línea "
									+ ((AnalizadorLexico) aLexico).getLinea() + ": "
									+ "Identificador " + iden + " existente.");
						}
					}
				}
			}
		}
	}

	/**
	 * Método que se encarga de reconocer la producción: Cons := Con RCons.
	 */
	private void reconoceCons(EntradaTS tipo) {
		if (!err) {
			reconoceCon(tipo);
			if (!err) {
				reconoceRCons();
			}
		}
	}

	/**
	 * Método que se encarga de reconocer la producción: Con := Tipo IDEN := Val ;
	 */
	private void reconoceCon(EntradaTS tipo) {
		if (!err) {
			token = aLexico.obtenerToken();
			if (!comprobarErrorLexico(token)) {
				if (reconoceTToken(token.getCategoria(), TToken.TKIDEN)) {
					String lex = token.getLexema();
					token = aLexico.obtenerToken();
					if (!comprobarErrorLexico(token)) {
						if (reconoceTToken(token.getCategoria(), TToken.TKASIG)) {
							// Reconocemos el valor (lo devolvemos).
							Object valor = reconoceVal();
							if (!err) {
								// Hallamos el tipo del valor.
								EntradaTS tipoValor;
								if (valor instanceof Integer)
									tipoValor = restricciones.buscaTS("ENT",
											nivel).getTipo();
								else
									tipoValor = restricciones.buscaTS("LOG",
											nivel).getTipo();
								// Comprobamos su presencia en la TS.
								if (!restricciones.restriccionCon(lex, nivel)) {
									// Comprobamos los tipos.
									if (restricciones.equivalente(tipo,
											tipoValor)) {
										// Añadimos la nueva constante a la
										// tabla de símbolos
										// e incrementamos la dirección.
										tablasDeSimbolos.get(nivel)
												.añadeConstante(lex, tipo,
														valor, dirs.get(nivel),
														nivel);
										dirs.add(nivel, dirs.get(nivel) + 1);
									} else {
										error = new Error(
												TError.SEMANTICO,
												"Línea "
														+ ((AnalizadorLexico) aLexico)
																.getLinea()
														+ ": "
														+ "Tipos incompatibles.");
										err = true;
									}
								} else {
									error = new Error(TError.SEMANTICO,
											"Línea "
													+ ((AnalizadorLexico) aLexico)
															.getLinea() + ": "
													+ "Identificador '" + lex
													+ "' existente.");
									err = true;
								}
								if (!err) {
									// Reconocer el ;
									token = aLexico.obtenerToken();
									if (!comprobarErrorLexico(token)) {
										reconoceTToken(token.getCategoria(),
												TToken.TKPYC);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Método que se encarga de reconocer la producción: Tipo := ENT | LOG |IDEN |
	 * ^Tipo | REG LisCampos FIN
	 * 
	 * @return Información sintetizada de la producción reconocida.
	 */
	private EntradaTS reconoceTipo() {
		if (!err) {
			if (!comprobarErrorLexico(token)) {
				if (token.getCategoria() != TToken.TKREG) {
					if (token.getCategoria() != TToken.TKENT) {
						if (token.getCategoria() != TToken.TKLOG) {
							if (token.getCategoria() != TToken.TKGORRO) {
								if (token.getCategoria() == TToken.TKIDEN) {
									InfoTS infoIden = restricciones.buscaTS(
											token.getLexema(), nivel);
									if (infoIden != null) {
										if (infoIden.getClase() != TClase.TCONSTRUIDO) {
											err = true;
											error = new Error(
													TError.SEMANTICO,
													"Línea "
															+ ((AnalizadorLexico) aLexico)
																	.getLinea()
															+ ": "
															+ "Identificador "
															+ token.getLexema()
															+ " no es un tipo construido.");
										} else {
											return infoIden.getTipo();
										}
									} else {
										err = true;
										error = new Error(
												TError.SEMANTICO,
												"Línea "
														+ ((AnalizadorLexico) aLexico)
																.getLinea()
														+ ": "
														+ "Identificador "
														+ token.getLexema()
														+ " no existente.");
									}
								} else {
									err = true;
									error = new Error(
											TError.SINTACTICO,
											"Línea "
													+ ((AnalizadorLexico) aLexico)
															.getLinea()
													+ ": "
													+ "Se esperaba 'ENT', 'LOG', '^', 'REG' o Identificador.");
								}
							} else {
								// reconocemos un puntero
								token = aLexico.obtenerToken();
								if (!comprobarErrorLexico(token)) {
									EntradaTS tipo1 = reconoceTipo();
									if (!err) {
										EntradaTS tipo = new EntradaPunteroTS(
												TTipo.PUNTERO, 1, tipo1);
										return tipo;
									}
								}
							}
						} else {
							return restricciones.buscaTS("LOG", nivel)
									.getTipo();
						}
					} else {
						return restricciones.buscaTS("ENT", nivel).getTipo();
					}
				} else {
					if (!err) {
						EntradaCampoRegistroTS tipoLisCampos = reconoceLisCampos();
						// reconocemos el token FIN y sino lanza el error
						if (!err) {
							reconoceTToken(token.getCategoria(), TToken.TKFIN);
							if (!comprobarErrorLexico(token)) {
								EntradaTS tipo = new EntradaRegistroTS(TTipo.REGISTRO,
										tipoLisCampos);
								return tipo;
							}
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Método que se encarga de reconocer la producción: LisCampos := Tipo IDEN
	 * RCampos
	 * 
	 * @return Información sintetizada de un campo de un registro.
	 */
	private EntradaCampoRegistroTS reconoceLisCampos() {
		EntradaCampoRegistroTS n;
		if (!err) {
			token = aLexico.obtenerToken();
			if (!comprobarErrorLexico(token)) {
				EntradaTS tipo = reconoceTipo();
				if (!err) {
					token = aLexico.obtenerToken();
					if (!comprobarErrorLexico(token)) {
						if (reconoceTToken(token.getCategoria(), TToken.TKIDEN)) {
							n = new EntradaCampoRegistroTS(token.getLexema(), tipo, 0);
							reconoceRCampos(n);
							if (!err) {
								return n;
							}
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Método que se encarga de reconocer la producción: RCampos := ; Tipo IDEN
	 * RCampos | lambda
	 * 
	 * @param nInformación
	 *            de un nodo del registro.
	 */
	private void reconoceRCampos(EntradaCampoRegistroTS n) {
		if (!err) {
			token = aLexico.obtenerToken();
			if (!comprobarErrorLexico(token)) {
				if (token.getCategoria() == TToken.TKPYC) {
					token = aLexico.obtenerToken();
					if (!comprobarErrorLexico(token)) {
						EntradaTS tipo = reconoceTipo();
						if (!err) {
							token = aLexico.obtenerToken();
							if (!comprobarErrorLexico(token)) {
								if (reconoceTToken(token.getCategoria(),
										TToken.TKIDEN)) {
									n.add(token.getLexema(), tipo, n
											.getTipoCampo(), n.getMaxOffset());
									reconoceRCampos(n);
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Método que se encarga de reconocer la producción: Val := - NUM | NUM
	 * |CIERTO |FALSO.
	 * 
	 * @return Valor reconocido.
	 */
	private Object reconoceVal() {
		if (!err) {
			TToken tk = aLexico.obtenerToken();
			if (!comprobarErrorLexico(tk)) {
				// Comprobamos si es un -
				if (tk.getCategoria() == TToken.TKRESTA) {
					token = aLexico.obtenerToken();
					if (!comprobarErrorLexico(token)) {
						// Reconocemos NUM.
						if (reconoceTToken(token.getCategoria(), TToken.TKNUM)) {
							Integer i = (Integer) token.getValor();
							// Construimos el número negativo.
							Integer in = Integer.valueOf("-" + i);
							return in;
						}
					}
					// Comprobamos si es un NUM o un valor LOG
				} else {
					if (tk.getCategoria() == TToken.TKNUM
							|| tk.getCategoria() == TToken.TKCIERTO
							|| tk.getCategoria() == TToken.TKFALSO) {
						return tk.getValor();
					} else {
						err = true;
						error = new Error(TError.SINTACTICO, "Línea "
								+ ((AnalizadorLexico) aLexico).getLinea() + ": "
								+ "Se esperaba un valor válido.");
					}
				}
			}
		}
		return null;
	}

	/**
	 * Método que se encarga de reconocer la producción: RCons := Cons | lambda.
	 */
	private void reconoceRCons() {
		if (!err) {
			token = aLexico.obtenerToken();
			if (!comprobarErrorLexico(token)) {
				// Vemos si lo siguiente es VAR (transición lambda),
				// si no es Cons (esperamos un tipo)
				if ((token.getCategoria() != TToken.TKVAR)) {
					// Reconocemos el tipo.
					EntradaTS tipo = null;
					if (token.getCategoria() != TToken.TKENT) {
						if (token.getCategoria() == TToken.TKLOG) {
							tipo = restricciones.buscaTS("LOG", nivel)
									.getTipo();
						} else {
							err = true;
							error = new Error(TError.SINTACTICO, "Línea "
									+ ((AnalizadorLexico) aLexico).getLinea() + ": "
									+ "Se esperaba 'ENT' o 'LOG'.");
						}
					} else {
						tipo = restricciones.buscaTS("ENT", nivel).getTipo();
					}

					if (!err) {
						reconoceCons(tipo);
					}
				}
			}
		}
	}

	/**
	 * Método que se encarga de reconocer la producción: Vars := Var RVars.
	 * 
	 * @param tipo
	 *            Información heredada.
	 */
	private void reconoceVars(EntradaTS tipo) {
		if (!err) {
			reconoceVar(tipo);
			if (!err) {
				reconoceRVars();
			}
		}
	}

	/**
	 * Método que se encarga de reconocer la producción: Var := Tipo Ids ;
	 * 
	 * @param tipo
	 *            Información heredada.
	 */
	private void reconoceVar(EntradaTS tipo) {
		// Procedemos a Ids
		if (!err) {
			reconoceIds(tipo);
			if (!err) {
				// Reconocemos el ;
				reconoceTToken(token.getCategoria(), TToken.TKPYC);
			}
		}
	}

	/**
	 * Método que se encarga de reconocer la producción: Ids := IDEN RIds.
	 * 
	 * @param tipo
	 *            Información heredada.
	 */
	private void reconoceIds(EntradaTS tipo) {
		if (!err) {
			token = aLexico.obtenerToken();
			if (!comprobarErrorLexico(token)) {
				// Reconocemos un identificador.
				if (reconoceTToken(token.getCategoria(), TToken.TKIDEN)) {
					String iden = token.getLexema();
					// Comprobamos si se ha declarado el identificador.
					if (!restricciones.restriccionIds(iden, nivel)) {
						tablasDeSimbolos.get(nivel).añadeVariable(iden, tipo,
								dirs.get(nivel), nivel);
						if (tipo.getNombreTipo() == TTipo.REGISTRO) {
							dirs
									.add(nivel, dirs.get(nivel)
											+ tipo.getTamanno());
						} else {
							dirs.add(nivel, dirs.get(nivel) + 1);
						}
						if (!err) {
							reconoceRIds(tipo);
						}
					} else {
						err = true;
						error = new Error(TError.SEMANTICO, "Línea "
								+ ((AnalizadorLexico) aLexico).getLinea() + ": "
								+ "Identificador " + iden + " existente.");
					}
				}
			}
		}
	}

	/**
	 * Método que se encarga de reconocer la producción: Ids := RIds := , Ids
	 * |lambda.
	 * 
	 * @param tipo
	 *            Información heredada.
	 */
	private void reconoceRIds(EntradaTS tipo) {
		if (!err) {
			token = aLexico.obtenerToken();
			if (!comprobarErrorLexico(token)) {
				if (token.getCategoria() == TToken.TKCOMA) {
					reconoceIds(tipo);
				}
			}
		}
	}

	/**
	 * Método que se encarga de reconocer la producción: RVars := Vars | lambda.
	 */
	private void reconoceRVars() {
		// Si hay más variables, vendría un tipo, si no nada (lambda).
		if (!err) {
			token = aLexico.obtenerToken();
			if (!comprobarErrorLexico(token)) {
				EntradaTS tipo = null;
				if (token.getCategoria() == TToken.TKLOG) {
					tipo = restricciones.buscaTS("LOG", nivel).getTipo();
					reconoceVars(tipo);
				} else if (token.getCategoria() == TToken.TKENT) {
					tipo = restricciones.buscaTS("ENT", nivel).getTipo();
					reconoceVars(tipo);
				} else if (token.getCategoria() == TToken.TKIDEN) {
					if (restricciones.buscaTS(token.getLexema(), nivel) == null) {
						err = true;
						error = new Error(TError.SEMANTICO, "Línea "
								+ ((AnalizadorLexico) aLexico).getLinea() + ": "
								+ "Identificador " + token.getLexema()
								+ " no existente.");
					} else {
						tipo = restricciones.buscaTS(token.getLexema(), nivel)
								.getTipo();
						reconoceVars(tipo);
					}
				} else if (token.getCategoria() == TToken.TKGORRO) {
					token = aLexico.obtenerToken();
					if (!comprobarErrorLexico(token)) {
						EntradaTS tipo1 = reconoceTipo();
						if (!err) {
							tipo = new EntradaPunteroTS(TTipo.PUNTERO, 1, tipo1);
							reconoceVars(tipo);
						}
					}
				}
			}
		}
	}

	/**
	 * Método que se encarga de reconocer la producción: Procs := Proc RProc
	 */
	private void reconoceProcs() {
		if (!err) {
			reconoceProc();
			if (!err) {
				token = aLexico.obtenerToken();
				if (!comprobarErrorLexico(token)) {
					reconoceRProc();
				}
			}
		}
	}

	/**
	 * Método que se encarga de reconocer la producción: RProc := Procs | lambda
	 */
	private void reconoceRProc() {
		if (!err) {
			if (token.getCategoria() == TToken.TKPROC)
				reconoceProcs();
		}
	}

	/**
	 * Método que se encarga de reconocer la producción: Proc := PROC IDEN ( ) Decs INICIO Ins FIN ;
	 */
	private void reconoceProc() {
		if (!err) {
			if (reconoceTToken(TToken.TKPROC, token.getCategoria())) {
				token = aLexico.obtenerToken();
				if (!comprobarErrorLexico(token)) {
					// reconoce IDEN
					if (reconoceTToken(TToken.TKIDEN, token.getCategoria())) {
						String IDEN = token.getLexema();
						token = aLexico.obtenerToken();
						if (!comprobarErrorLexico(token))
							// reconoce Parentesis abierto.
							if (reconoceTToken(TToken.TKPABRE, token
									.getCategoria())) {
								token = aLexico.obtenerToken();
								if (!comprobarErrorLexico(token))
									// reconoce Paréntesis cerrado.
									if (reconoceTToken(TToken.TKPCIERRA, token
											.getCategoria())) {
										EntradaProcTS tipo = new EntradaProcTS(0);
										TablaSimbolos ts = new TablaSimbolos();
										// añadimos la tabla de símbolos del
										// procedimiento a nuestro array de
										// tablas de símbolos
										tablasDeSimbolos.add(nivel, ts);
										Integer dir = new Integer(3);
										dirs.add(nivel, dir);
										tablasDeSimbolos.get(nivel - 1)
												.addProcedimiento(IDEN, tipo,
														etq, nivel - 1);
										// si queremos ejecutar el procedimiento
										// p1 y en las declaraciones de este,
										// hay otro procedimiento p2, tenemos
										// que redireccionar al comienzo de p1
										int etqAux = etq;
										codObj.añadirInstruccion("ir_a", "0");
										etq++;
										reconoceDecs();
										// aqui parcheamos la instrucción ir_a
										// al comienzo de p1
										codObj.parchea(etqAux, etq);
										if (!err) {
											if (reconoceTToken(TToken.TKINICIO,
													token.getCategoria())) {
												token = aLexico.obtenerToken();
												if (!comprobarErrorLexico(token)) {
													codObj
															.añadirInstruccion(
																	"incrementaC",
																	Integer
																			.toString(dirs
																					.get(nivel)));
													etq++;
													reconoceIns();
													codObj
															.añadirInstruccion("retorno");
													etq++;
													if (!err) {
														if (reconoceTToken(
																TToken.TKFIN,
																token
																		.getCategoria())) {
															token = aLexico
																	.obtenerToken();
															if (!comprobarErrorLexico(token)) {
																// reconocemos
																// el ;
																reconoceTToken(
																		TToken.TKPYC,
																		token
																				.getCategoria());
															}
														}
													}
												}
											}
										}
									}
							}
					}
				}
			}
		}
	}

	/**
	 * Método que se encarga de reconocer la producción: Ins := In RIns.
	 */
	private void reconoceIns() {
		if (!err)
			reconoceIn();
		if (!err)
			reconoceRIns();
	}

	/**
	 * Método que se encarga de reconocer la producción: In := IDEN := Exp ;
	 * (Cambios para la segunda parte: In := InAsig | InIf | InWhile | InPunt |InProc)
	 */
	private void reconoceIn() {
		if (!err) {
			if ((token.getCategoria() == TToken.TKSI))
				reconoceInIf();
			else if ((token.getCategoria() == TToken.TKMIENTRAS))
				reconoceInWhile();
			else if (token.getCategoria() == TToken.TKNUEVO)
				reconoceInPunt();
			else if ((token.getCategoria() == TToken.TKIDEN)
					&& (restricciones.buscaTS(token.getLexema(), nivel) != null)
					&& (restricciones.buscaTS(token.getLexema(), nivel)
							.getTipo().getNombreTipo() == TTipo.PROC)) {
				reconoceInProc();
			} else
				reconoceInAsig();
		}
	}

	/**
	 * Método que se encarga de reconocer la producción: In := InProc
	 */
	private void reconoceInProc() {
		if (!err) {
			if (!comprobarErrorLexico(token)) {
				InfoTS info = restricciones.buscaTS(token.getLexema(), nivel);
				codObj.añadirInstruccion("apila", Integer.toString(nivel
						- info.getNivel()));
				codObj.añadirInstruccion("apila", Integer.toString(info
						.getDirOEtq()));
				codObj.añadirInstruccion("llamada");
				// Actualizar etiqueta
				etq = etq + 3;
				token = aLexico.obtenerToken();
				if (reconoceTToken(token.getCategoria(), TToken.TKPABRE)) {
					token = aLexico.obtenerToken();
					if (!comprobarErrorLexico(token)) {
						if (reconoceTToken(token.getCategoria(),
								TToken.TKPCIERRA)) {
							token = aLexico.obtenerToken();
							if (!comprobarErrorLexico(token)) {
								if (reconoceTToken(token.getCategoria(),
										TToken.TKPYC)) {
									token = aLexico.obtenerToken();
									comprobarErrorLexico(token);
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Método que se encarga de reconocer la producción: 
	 * InPunt := (IDEN); 
	 */
	private void reconoceInPunt() {
		if (!err) {
			token = aLexico.obtenerToken();
			if (!comprobarErrorLexico(token)) {
				if (reconoceTToken(token.getCategoria(), TToken.TKPABRE)) {
					token = aLexico.obtenerToken();
					if (!comprobarErrorLexico(token)) {
						if (reconoceTToken(token.getCategoria(), TToken.TKIDEN)) {
							if (!restricciones.restriccionPuntero(token
									.getLexema(), nivel)) {
								InfoTS info = restricciones.buscaTS(token
										.getLexema(), nivel);
								String dir = String.valueOf(info.getDirOEtq());
								codObj.añadirInstruccion("apila", dir);
								codObj.añadirInstruccion("apila", String
										.valueOf(nivel - info.getNivel()));
								codObj.añadirInstruccion("apilaH");
								codObj.añadirInstruccion("desapilaInd");
								String tam = String.valueOf(info.getTipo()
										.getTamanno());
								codObj.añadirInstruccion("incrementaH", tam);
								etq = etq + 5;
								token = aLexico.obtenerToken();
								comprobarErrorLexico(token);
							} else {
								err = true;
								error = new Error(
										TError.SEMANTICO,
										"Línea "
												+ ((AnalizadorLexico) aLexico)
														.getLinea()
												+ ": "
												+ "Variable no existente o no es puntero.");
							}
							if (!err) {
								if (reconoceTToken(token.getCategoria(),
										TToken.TKPCIERRA)) {
									token = aLexico.obtenerToken();
									if (!comprobarErrorLexico(token)) {
										if (reconoceTToken(
												token.getCategoria(),
												TToken.TKPYC)) {
											token = aLexico.obtenerToken();
											comprobarErrorLexico(token);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Método que se encarga de reconocer la producción: InAsig := Descriptor :=
	 * Exp ;
	 */
	private void reconoceInAsig() {
		if (!err) {
			InfoTS info = reconoceDescriptor();
			if (!err) {
				if (info.getClase() != TClase.CONS) {
					// Reconocemos :=
					if (!comprobarErrorLexico(token)) {
						if (reconoceTToken(token.getCategoria(), TToken.TKASIG)) {
							// Exp, reconociendo su tipo
							token = aLexico.obtenerToken();
							if (!comprobarErrorLexico(token)) {
								EntradaTS expTipo = reconoceExp();
								if (!err) {
									// Comprobamos tipos
									if (restricciones.equivalente(expTipo, info
											.getTipo())) {
										// Reconocemos el ;
										if (reconoceTToken(
												token.getCategoria(),
												TToken.TKPYC)) {
											// Generamos el código objeto de la
											// instruccion
											codObj
													.añadirInstruccion("desapilaInd");
											// actualizamos el valor de etiqueta
											etq = etq + 1;
											// pasamos al siguiente token
											token = aLexico.obtenerToken();
											comprobarErrorLexico(token);
										}
									} else {
										err = true;
										error = new Error(
												TError.SEMANTICO,
												"Línea "
														+ ((AnalizadorLexico) aLexico)
																.getLinea()
														+ ": "
														+ "Tipos incompatibles.");
									}
								}
							}
						}
					}
				} else {
					err = true;
					error = new Error(TError.SEMANTICO, "Línea "
							+ ((AnalizadorLexico) aLexico).getLinea() + ": "
							+ "Identificador constante.");
				}
			}
		}
	}

	/**
	 * Método que se encarga de reconocer la producción: InIf := SI ( Exp )
	 * ENTONCES INICIO Ins FIN SINO INICIO Ins FIN | SI ( Exp ) ENTONCES INICIO
	 * Ins FIN
	 */
	private void reconoceInIf() {
		if (!err) {
			token = aLexico.obtenerToken();
			if (!comprobarErrorLexico(token)) {
				if (reconoceTToken(token.getCategoria(), TToken.TKPABRE)) {
					token = aLexico.obtenerToken();
					if (!comprobarErrorLexico(token)) {
						EntradaTS tipoExp = reconoceExp();
						if (!err) {
							EntradaTS aux = restricciones.buscaTS("LOG", nivel)
									.getTipo();
							if (restricciones.equivalente(tipoExp, aux)) {
								// reconocemos el final del parentesis
								if (reconoceTToken(token.getCategoria(),
										TToken.TKPCIERRA)) {
									token = aLexico.obtenerToken();
									if (!comprobarErrorLexico(token)) {
										if (reconoceTToken(
												token.getCategoria(),
												TToken.TKENTONCES)) {
											token = aLexico.obtenerToken();
											if (!comprobarErrorLexico(token)) {
												if (reconoceTToken(token
														.getCategoria(),
														TToken.TKINICIO)) {
													token = aLexico
															.obtenerToken();
													if (!comprobarErrorLexico(token)) {
														int etqExph = etq;
														codObj
																.añadirInstruccion(
																		"ir_f",
																		String
																				.valueOf(etqExph));
														etq = etq + 1;
														reconoceIns();
														if (!err) {
															if (reconoceTToken(
																	token
																			.getCategoria(),
																	TToken.TKFIN)) {
																etq = etq + 1;
																codObj
																		.añadirInstruccion(
																				"ir_a",
																				String
																						.valueOf(etq));
																codObj
																		.parchea(
																				etqExph,
																				etq);
																// Ahora podemos
																// reconocer
																// SINO o lambda
																token = aLexico
																		.obtenerToken();
																if (!comprobarErrorLexico(token)) {
																	reconoceInSINO();
																}
															}
														}
													}
												}
											}
										}
									}
								}
							} else {
								err = true;
								error = new Error(TError.SEMANTICO, "Línea "
										+ ((AnalizadorLexico) aLexico).getLinea()
										+ ": " + "Tipos incompatibles.");
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Método que se encarga de reconocer la producción: RInIf := SINO INICIO Ins FIN | lambda
	 */
	private void reconoceInSINO() {
		if (!err) {
			int etqExph = etq;
			if (token.getCategoria() == TToken.TKSINO) {
				token = aLexico.obtenerToken();
				if (!comprobarErrorLexico(token)) {
					if (reconoceTToken(token.getCategoria(), TToken.TKINICIO)) {
						// comprobar que no me paso de token
						token = aLexico.obtenerToken();
						if (!comprobarErrorLexico(token)) {
							reconoceIns();
							if (!err) {
								codObj.parchea(etqExph - 1, etq);
								if (reconoceTToken(token.getCategoria(),
										TToken.TKFIN)) {
									token = aLexico.obtenerToken();
									comprobarErrorLexico(token);
								}
							}
						}
					}
				}
			} else {
				// reconocemos lambda
				codObj.parchea(etq - 1, etq);
			}
		}
	}

	/**
	 * Método que se encarga de reconocer la producción: InWhile := MIENTRAS (
	 * Exp ) HACER INICIO Ins FIN
	 */
	private void reconoceInWhile() {
		if (!err) {
			int etqh = etq;
			token = aLexico.obtenerToken();
			if (!comprobarErrorLexico(token)) {
				if (reconoceTToken(token.getCategoria(), TToken.TKPABRE)) {
					token = aLexico.obtenerToken();
					if (!comprobarErrorLexico(token)) {
						EntradaTS tipoExp = reconoceExp();
						EntradaTS aux = restricciones.buscaTS("LOG", nivel)
								.getTipo();
						if (restricciones.equivalente(tipoExp, aux)) {
							if (reconoceTToken(token.getCategoria(),
									TToken.TKPCIERRA)) {
								token = aLexico.obtenerToken();
								if (!comprobarErrorLexico(token)) {
									if (reconoceTToken(token.getCategoria(),
											TToken.TKHACER)) {
										token = aLexico.obtenerToken();
										if (!comprobarErrorLexico(token)) {
											if (reconoceTToken(token
													.getCategoria(),
													TToken.TKINICIO)) {
												token = aLexico.obtenerToken();
												if (!comprobarErrorLexico(token)) {
													int etqExp = etq;
													etq = etq + 1;
													codObj
															.añadirInstruccion(
																	"ir_f",
																	String
																			.valueOf(etq));
													reconoceIns();
													if (!err) {
														if (reconoceTToken(
																token
																		.getCategoria(),
																TToken.TKFIN)) {
															codObj
																	.añadirInstruccion(
																			"ir_a",
																			String
																					.valueOf(etq));
															codObj.parchea(
																	etqExp,
																	etq + 1);
															codObj.parchea(etq,
																	etqh);
															etq = etq + 1;
															token = aLexico
																	.obtenerToken();
															comprobarErrorLexico(token);
														}
													}
												}
											}
										}
									}
								}
							}
						} else {
							err = true;
							error = new Error(TError.SEMANTICO, "Línea "
									+ ((AnalizadorLexico) aLexico).getLinea() + ": "
									+ "Tipos incompatibles.");
						}
					}
				}
			}
		}
	}

	/**
	 * Método que se encarga de reconocer la producción: Exp := ExpIg RExp.
	 * 
	 * @return Información sintetizada de la expresión.
	 */
	private EntradaTS reconoceExp() {
		if (!err) {
			EntradaTS expTipo = null, expIgTipo = null;
			if (!err)
				expIgTipo = reconoceExpIg();
			if (!err)
				expTipo = reconoceRExp(expIgTipo);
			return expTipo;
		}
		return null;
	}

	/**
	 * Método que se encarga de reconocer la producción: ExpIg := ExpSum RExpIg.
	 * 
	 * @return Información sintetizada de la expresión.
	 */
	private EntradaTS reconoceExpIg() {
		if (!err) {
			EntradaTS expSumtipo = null, expIgtipo = null;
			if (!err)
				expSumtipo = reconoceExpSum();
			if (!err)
				expIgtipo = reconoceRExpIg(expSumtipo);
			return expIgtipo;
		}
		return null;
	}

	/**
	 * Método que se encarga de reconocer la producción: ExpSum := ExpMult
	 * RExpSum.
	 * 
	 * @return Información sintetizada de la expresión.
	 */
	private EntradaTS reconoceExpSum() {
		if (!err) {
			EntradaTS expSumtipo = null, rExpSumtipo = null;
			if (!err)
				rExpSumtipo = reconoceExpMult();
			if (!err)
				expSumtipo = reconoceRExpSum(rExpSumtipo);
			return expSumtipo;
		}
		return null;
	}

	/**
	 * Método que se encarga de reconocer la producción: ExpMult := ExpUn
	 * RExpMul.
	 * 
	 * @return Información sintetizada de la expresión.
	 */
	private EntradaTS reconoceExpMult() {
		if (!err) {
			EntradaTS expMultTipo = null, rExpMultTipo = null;
			if (!err)
				rExpMultTipo = reconoceExpUn();
			if (!err)
				expMultTipo = reconoceRExpMult(rExpMultTipo);
			return expMultTipo;
		}
		return null;
	}

	/**
	 * Método que se encarga de reconocer la producción: ExpUn := Fact | !Fact.
	 * 
	 * @return Información sintetizada de la expresión.
	 */
	private EntradaTS reconoceExpUn() {
		if (!err) {
			EntradaTS expUnTipo = null;
			if (!comprobarErrorLexico(token)) {
				// Reconocemos !
				if (token.getCategoria() == TToken.TKNEG) {
					if (!err) {
						token = aLexico.obtenerToken();
						if (!comprobarErrorLexico(token)) {
							// Reconocemos Fact
							expUnTipo = reconoceFact();
						}
					}
					if (!err) {
						EntradaTS aux = restricciones.buscaTS("LOG", nivel)
								.getTipo();
						if (restricciones.equivalente(expUnTipo, aux)) {
							codObj.añadirInstruccion("negacion");
							etq = etq + 1;
						} else {
							err = true;
							error = new Error(
									TError.SEMANTICO,
									"Línea "
											+ ((AnalizadorLexico) aLexico).getLinea()
											+ ": "
											+ "Operador '!' solo aplicable a valores lógicos.");
						}
					}
				} else {
					// Reconocemos Fact
					if (!err)
						expUnTipo = reconoceFact();
				}
				return expUnTipo;
			}
		}
		return null;
	}

	/**
	 * Método que se encarga de reconocer la producción: Fact := ( Exp )| IDEN |-
	 * NUM | NUM | CIERTO | FALSO.
	 * 
	 * @return Información sintetizada de la expresión.
	 */
	private EntradaTS reconoceFact() {
		EntradaTS factTipo = null;
		if (!err) {
			// Reconocemos ( Exp )
			if (token.getCategoria() == TToken.TKPABRE) {
				token = aLexico.obtenerToken();
				if (!comprobarErrorLexico(token)) {
					// Reconocemos Exp
					factTipo = reconoceExp();
					if (!err) {
						// Reconocemos )
						reconoceTToken(token.getCategoria(), TToken.TKPCIERRA);
						token = aLexico.obtenerToken();
						comprobarErrorLexico(token);
					}
				}
			}
			// Reconocemos -NUM
			else if (token.getCategoria() == TToken.TKRESTA) {
				// Reconocemos NUM
				token = aLexico.obtenerToken();
				if (!comprobarErrorLexico(token)) {
					if (reconoceTToken(token.getCategoria(), TToken.TKNUM)) {
						Integer i = (Integer) token.getValor();
						// Construimos el número negativo.
						Integer in = Integer.valueOf("-" + i);
						codObj.añadirInstruccion("apila", String.valueOf(in));
						factTipo = restricciones.buscaTS("ENT", nivel)
								.getTipo();
						etq = etq + 1;
						token = aLexico.obtenerToken();
						comprobarErrorLexico(token);
					}
				}
			}
			// Reconocemos NUM
			else if (token.getCategoria() == TToken.TKNUM) {
				codObj.añadirInstruccion("apila", String.valueOf(token
						.getValor()));
				factTipo = restricciones.buscaTS("ENT", nivel).getTipo();
				etq = etq + 1;
				token = aLexico.obtenerToken();
				comprobarErrorLexico(token);
			}
			// Reconocemos CIERTO
			else if (token.getCategoria() == TToken.TKCIERTO) {
				factTipo = restricciones.buscaTS("LOG", nivel).getTipo();
				codObj.añadirInstruccion("apila", "CIERTO");
				etq = etq + 1;
				token = aLexico.obtenerToken();
				comprobarErrorLexico(token);
			}
			// Reconocemos FALSO
			else if (token.getCategoria() == TToken.TKFALSO) {
				factTipo = restricciones.buscaTS("LOG", nivel).getTipo();
				codObj.añadirInstruccion("apila", "FALSO");
				etq = etq + 1;
				token = aLexico.obtenerToken();
				comprobarErrorLexico(token);
			} else {
				InfoTS info = reconoceDescriptor();
				// Comprobamos si existe el identificador
				if (!err) {
					if (info.getClase() != TClase.CONS) {
						codObj.añadirInstruccion("apilaInd");
						etq = etq + 1;
					}
					// Devolvemos el tipo del identificador.
					factTipo = info.getTipo();
				}
			}

		}
		return factTipo;
	}

	/**
	 * Método que se encarga de reconocer la producción: RExpMult := OpMult
	 * ExpUn RExpMult | lambda.
	 * 
	 * @param tipoH Información heredada de la expresión.
	 * @return Tipo Información sintetizada de la expresión.
	 */
	private EntradaTS reconoceRExpMult(EntradaTS tipoH) {
		EntradaTS rExpMultTipo = tipoH, expUnTipo = null;
		EntradaTS r1ExpMultTipo = null;
		if (!err) {
			if (!comprobarErrorLexico(token)) {
				/*
				 * OpMult := * | / | MOD | &
				 */
				String operacion = opMult(token.getCategoria());
				if (token.getCategoria() == TToken.TKDIV
						|| token.getCategoria() == TToken.TKMOD
						|| token.getCategoria() == TToken.TKPROD) {
					r1ExpMultTipo = restricciones.buscaTS("ENT", nivel)
							.getTipo();
					;
				} else if (token.getCategoria() == TToken.TKAND) {
					r1ExpMultTipo = restricciones.buscaTS("LOG", nivel)
							.getTipo();
				} else {
					// nada, reconocemos lambda
					// el tipo devuelto es el heredado...
					rExpMultTipo = tipoH;
				}
				// si es null es porque reconocemos lambda
				if (r1ExpMultTipo != null) {
					token = aLexico.obtenerToken();
					if (!comprobarErrorLexico(token)) {
						expUnTipo = reconoceExpUn();
						if (!err) {
							// Comprobamos restriccion de tipo.
							if (restricciones.equivalente(tipoH, r1ExpMultTipo)
									&& restricciones.equivalente(tipoH,
											expUnTipo)) {
								// Generamos la instruccion.
								codObj.añadirInstruccion(operacion);
								etq = etq + 1;
								rExpMultTipo = reconoceRExpMult(r1ExpMultTipo);
							} else {
								err = true;
								error = new Error(TError.SEMANTICO, "Línea "
										+ ((AnalizadorLexico) aLexico).getLinea()
										+ ": " + "Tipos incompatibles.");
							}
						}
					}
				}
			}
		}
		return rExpMultTipo;
	}

	/**
	 * Método que se encarga de reconocer la producción: OpMult := * | / | MOD | &.
	 * 
	 * @param categoria Categoría del token leido.
	 * @return Código objeto de la operación.
	 */
	private String opMult(int categoria) {
		String operacion = null;
		if (!err) {
			if (categoria == TToken.TKDIV)
				operacion = "divide";
			if (categoria == TToken.TKMOD)
				operacion = "mod";
			if (categoria == TToken.TKPROD)
				operacion = "multiplica";
			if (categoria == TToken.TKAND)
				operacion = "and";
		}
		return operacion;
	}

	/**
	 * Método que se encarga de reconocer la producción: RExpSum := OpSum
	 * ExpMult RExpSum| lambda.
	 * 
	 * @param tipoH Información heredada  la expresión.
	 * @return Tipo Información sintetizada de la expresión.
	 */
	private EntradaTS reconoceRExpSum(EntradaTS tipoH) {
		EntradaTS rExpSumTipo = tipoH;
		EntradaTS opTipo = null;
		EntradaTS expMultTipo = null;
		String operacion = null;
		if (!err) {
			/*
			 * OpSum := + | - | ||
			 */
			operacion = opSum(token.getCategoria());
			if (token.getCategoria() == TToken.TKSUMA
					|| token.getCategoria() == TToken.TKRESTA) {
				opTipo = restricciones.buscaTS("ENT", nivel).getTipo();
			} else if (token.getCategoria() == TToken.TKOR) {
				opTipo = restricciones.buscaTS("LOG", nivel).getTipo();
			}
			if (opTipo != null) {
				token = aLexico.obtenerToken();
				if (!comprobarErrorLexico(token)) {
					// si es null reconocemos lambda
					expMultTipo = reconoceExpMult();
					if (!err) {
						if (restricciones.equivalente(tipoH, opTipo)
								&& restricciones
										.equivalente(tipoH, expMultTipo)) {
							// si no ha habido error antes
							if (!err) {
								// generamos el codigo de la instruccion
								codObj.añadirInstruccion(operacion);
								// actualizamos el valor de etq
								etq = etq + 1;
								rExpSumTipo = reconoceRExpSum(opTipo);
							}
						} else {
							err = true;
							error = new Error(TError.SEMANTICO, "Línea "
									+ ((AnalizadorLexico) aLexico).getLinea() + ": "
									+ "Tipos incompatibles.");
						}
					}
				}
			}
		}
		return rExpSumTipo;
	}

	/**
	 * Método que se encarga de reconocer la producción: OpSum := + | - | ||.
	 * 
	 * @param categoria Categoría del token leido.
	 * @return Código objeto de la operación.
	 */
	private String opSum(int categoria) {
		/*
		 * OpSum := + | - | ||
		 */
		String operacion = null;
		if (!err) {
			if (categoria == TToken.TKSUMA)
				operacion = "suma";
			if (categoria == TToken.TKRESTA)
				operacion = "resta";
			if (categoria == TToken.TKOR)
				operacion = "or";
		}
		return operacion;
	}

	/**
	 * Método que se encarga de reconocer la producción: RExpIg := OpIg ExpSum |
	 * lambda.
	 * 
	 * @param tipoH Información heredada de la expresión.
	 * @return Tipo Información sintetizada de la expresión.
	 */
	private EntradaTS reconoceRExpIg(EntradaTS tipoH) {
		EntradaTS rExpIgTipo = tipoH;
		EntradaTS expSumTipo = null;
		String operacion = opIg(token.getCategoria());
		if (!err) {
			if (token.getCategoria() == TToken.TKDESIGUAL
					|| token.getCategoria() == TToken.TKIGUAL) {
				token = aLexico.obtenerToken();
				if (!comprobarErrorLexico(token)) {
					expSumTipo = reconoceExpSum();
					if (!err) {
						if (restricciones.equivalente(tipoH, expSumTipo)) {
							rExpIgTipo = restricciones.buscaTS("LOG", nivel)
									.getTipo();
							codObj.añadirInstruccion(operacion);
							etq = etq + 1;
						} else {
							error = new Error(TError.SEMANTICO, "Línea "
									+ ((AnalizadorLexico) aLexico).getLinea() + ": "
									+ "Tipos incompatibles.");
							err = true;
						}
					}
				}
			}
		}
		return rExpIgTipo;
	}

	/**
	 * Método que se encarga de reconocer la producción: OpIg := == | !=.
	 * 
	 * @param categoria Categoría del token leido.
	 * @return Código objeto de la operacion.
	 */
	private String opIg(int categoria) {
		String operacion = null;
		if (!err) {
			if (categoria == TToken.TKIGUAL)
				operacion = "igualdad";
			if (categoria == TToken.TKDESIGUAL)
				operacion = "desigualdad";
		}
		return operacion;
	}

	/**
	 * Método que se encarga de reconocer la producción: RExp := OpOrd ExpIg |
	 * lambda.
	 * 
	 * @param tipoH Información heredada de la expresión.
	 * @return Tipo Información sintetizada de la expresión.
	 */
	private EntradaTS reconoceRExp(EntradaTS tipoH) {
		if (!err) {
			EntradaTS tipo = tipoH;
			int categoria = token.getCategoria();
			String operacion = opOrd(categoria);
			// si la operacion es null es porque reconocemos otra cosa
			if (operacion != null) {
				token = aLexico.obtenerToken();
				if (!comprobarErrorLexico(token)) {
					EntradaTS aux = restricciones.buscaTS("ENT", nivel).getTipo();
					if (restricciones.equivalente(tipoH, aux)) {
						tipo = restricciones.buscaTS("LOG", nivel).getTipo();
						EntradaTS tipoExpIg = reconoceExpIg();
						if (!err) {
							if (restricciones.equivalente(tipoExpIg, aux)) {
								// Añadimos la instruccion.
								codObj.añadirInstruccion(operacion);
								etq = etq + 1;
							} else {
								// error de TIPO
								err = true;
								error = new Error(TError.SEMANTICO, "Línea "
										+ ((AnalizadorLexico) aLexico).getLinea()
										+ ": " + "Tipos incompatibles.");
							}
						}
					} else {
						// error de TIPO
						err = true;
						error = new Error(TError.SEMANTICO, "Línea "
								+ ((AnalizadorLexico) aLexico).getLinea() + ": "
								+ "Tipos incompatibles.");
					}
				}
			}
			return tipo;
		}
		return null;
	}

	/**
	 * Método que se encarga de reconocer la producción: OpIg := > | >= | < |
	 * <=.
	 * 
	 * @param categoria Categoría del token leido.
	 * @return Código objeto de la operación.
	 */
	private String opOrd(int categoria) {
		/*
		 * OpOrd := > | >= | < | <=
		 */
		String operacion = null;
		if (!err) {
			if (categoria == TToken.TKMAYOR)
				operacion = "mayor";
			if (categoria == TToken.TKMAYORIGUAL)
				operacion = "mayorIgual";
			if (categoria == TToken.TKMENOR)
				operacion = "menor";
			if (categoria == TToken.TKMENORIGUAL)
				operacion = "menorIgual";
		}
		return operacion;
	}

	/**
	 * Método que se encarga de reconocer la producción: RIns := Ins | lambda.
	 */
	private void reconoceRIns() {
		if (!err) {
			if (!comprobarErrorLexico(token)) {
				if (token.getCategoria() == TToken.TKIDEN
						|| token.getCategoria() == TToken.TKGORRO
						|| token.getCategoria() == TToken.TKSI
						|| token.getCategoria() == TToken.TKMIENTRAS
						|| token.getCategoria() == TToken.TKNUEVO) {
					reconoceIns();
				}
			}
		}
	}

	/**
	 * Método que se encarga de reconocer la producción: 
	 * Descriptor := IDEN RDescriptor | ^Descriptor RDescriptor
	 */
	private InfoTS reconoceDescriptor() {
		if (!err) {
			int n = 0;
			boolean generar = false;
			if (!comprobarErrorLexico(token)) {
				if (token.getCategoria() == TToken.TKIDEN) {
					// Obtenemos el nombre de la variable
					String nameVar = token.getLexema();
					// Comprobamos errores en tabla de símbolos
					if (!restricciones.restriccionIn(nameVar, nivel)) {
						InfoTS info = restricciones.buscaTS(nameVar, nivel);
						if (!err) {
							if (info != null) {
								if (info.getClase() == TClase.CONS) {
									codObj.añadirInstruccion("apila", info
											.getValor().toString());
									etq = etq + 1;
								} else if (info.getClase() == TClase.VAR) {
									codObj.añadirInstruccion("apila", String
											.valueOf(info.getDirOEtq()));
									if (info.getTipo().getNombreTipo() != TTipo.REGISTRO) {
										codObj.añadirInstruccion("apila",
												String.valueOf(nivel
														- info.getNivel()));
										etq = etq + 1;
									} else {
										n = info.getNivel();
										generar = true;
									}
									etq = etq + 1;
								} else {
									err = true;
									error = new Error(
											TError.SEMANTICO,
											"Línea "
													+ ((AnalizadorLexico) aLexico)
															.getLinea()
													+ ": "
													+ "Tipo construido, uso incorrecto.");
								}
								token = aLexico.obtenerToken();
								if (!comprobarErrorLexico(token)) {

									info = reconoceRDescriptor(info);
									if (generar) {
										codObj.añadirInstruccion("apila",
												String.valueOf(nivel - n));
										etq = etq + 1;
									}
									if (!err) {
										return info;
									}
								}

							}
						}
					} else {
						err = true;
						error = new Error(TError.SEMANTICO, "Línea "
								+ ((AnalizadorLexico) aLexico).getLinea() + ": "
								+ "Identificador '" + nameVar
								+ "' no existente.");
					}
				} else if (token.getCategoria() == TToken.TKGORRO) {
					token = aLexico.obtenerToken();
					if (!comprobarErrorLexico(token)) {
						InfoTS a;
						a = reconoceDescriptor();
						if (!err) {
							if (restricciones.restriccionDesPuntero(a)) {
								a = reconoceRDescriptor(a);
								if (!err) {
									codObj.añadirInstruccion("apilaInd");
									codObj.añadirInstruccion("apila", String
											.valueOf(nivel - a.getNivel()));
									etq = etq + 2;
									return a;
								}
							} else {
								err = true;
								error = new Error(TError.SEMANTICO, "Línea "
										+ ((AnalizadorLexico) aLexico).getLinea()
										+ ": " + "Identificador '"
										+ token.getLexema()
										+ "' no es una variable puntero.");
							}
						}
					}
				} else {
					err = true;
					error = new Error(TError.SEMANTICO, "Línea "
							+ ((AnalizadorLexico) aLexico).getLinea() + ": "
							+ "Se esperaba Identificador o puntero.");
				}
			}
		}
		return null;
	}

	/**
	 * Método que se encarga de reconocer la producción:
	 * RDescriptor := .IDEN RDescriptor | lambda
	 * @param info Información heredada del resto de descriptores.
	 * @return Información sintetizada del resto de descriptores.
	 */
	private InfoTS reconoceRDescriptor(InfoTS info) {
		if (!err) {
			if (!comprobarErrorLexico(token)) {
				// Reconocemos .
				if (token.getCategoria() == TToken.TKPUNTO) {
					if (info.getClase() == TClase.VAR) {
						// .IDEN
						token = aLexico.obtenerToken();
						if (!comprobarErrorLexico(token)) {
							if (reconoceTToken(token.getCategoria(),
									TToken.TKIDEN)) {
								EntradaTS reg = info.getTipo();
								if ((reg.getNombreTipo() == TTipo.REGISTRO)) {
									EntradaCampoRegistroTS campo = ((EntradaRegistroTS) reg)
											.buscar(token.getLexema());
									if (campo.getNombreCampo() != null) {
										String valorOffset = Integer
												.toString(campo.getOffset());
										codObj.añadirInstruccion("apila",
												valorOffset);
										codObj.añadirInstruccion("suma");
										etq = etq + 2;
										InfoTS aux = new InfoTS();
										aux.setTipo(((EntradaRegistroTS) reg)
												.buscar(token.getLexema())
												.getTipoCampo());
										aux.setClase(TClase.VAR);
										token = aLexico.obtenerToken();
										if (!comprobarErrorLexico(token)) {
											aux = reconoceRDescriptor(aux);
											return aux;
										}
									} else {
										err = true;
										error = new Error(
												TError.SEMANTICO,
												"Línea "
														+ ((AnalizadorLexico) aLexico)
																.getLinea()
														+ ": "
														+ "Identificador '"
														+ token.getLexema()
														+ "' no es un campo del registro.");
									}
								} else {
									err = true;
									error = new Error(TError.SEMANTICO,
											"Línea "
													+ ((AnalizadorLexico) aLexico)
															.getLinea() + ": "
													+ "No es un tipo registro!");
								}
							}
						}
					} else {
						err = true;
						error = new Error(TError.SEMANTICO, "Línea "
								+ ((AnalizadorLexico) aLexico).getLinea() + ": "
								+ "Identificador '" + token.getLexema()
								+ "' no es una variable.");
					}
				} else {
					if (info != null) {
						return info;

					}
				}
			}
		}
		return null;
	}

	/**
	 * Método que comprueba si la categoría del token es válida.
	 * 
	 * @param categoriaToken
	 *            Categoría del token que queremos hacer la comprobación.
	 * @param categoriaR
	 *            Categoría con la que queremos comprobar la categoría del
	 *            token.
	 * @return Devuelve cierto si ambas categorías son iguales.
	 */
	private boolean reconoceTToken(int categoriaToken, int categoriaR) {
		if (categoriaR != (categoriaToken)) {
			String mensaje = "";
			if (categoriaR == TToken.TKPROGRAMA)
				mensaje = mensaje + "Se esperaba 'PROGRAMA'";
			if (categoriaR == TToken.TKCONS)
				mensaje = mensaje + "Se esperaba 'CONS'";
			if (categoriaR == TToken.TKVAR)
				mensaje = mensaje + "Se esperaba 'VAR'";
			if (categoriaR == TToken.TKINICIO)
				mensaje = mensaje + "Se esperaba 'INICIO'";
			if (categoriaR == TToken.TKFIN)
				mensaje = mensaje + "Se esperaba 'FIN'";
			if (categoriaR == TToken.TKENT)
				mensaje = mensaje + "Se esperaba 'ENT'";
			if (categoriaR == TToken.TKLOG)
				mensaje = mensaje + "Se esperaba 'LOG'";
			if (categoriaR == TToken.TKNUM)
				mensaje = mensaje + "Se esperaba número entero";
			if (categoriaR == TToken.TKCIERTO)
				mensaje = mensaje + "Se esperaba 'CIERTO'";
			if (categoriaR == TToken.TKFALSO)
				mensaje = mensaje + "Se esperaba 'FALSO'";
			if (categoriaR == TToken.TKIDEN)
				mensaje = mensaje + "Se esperaba identificador";
			if (categoriaR == TToken.TKCOMA)
				mensaje = mensaje + "Se esperaba ','";
			if (categoriaR == TToken.TKPYC)
				mensaje = mensaje + "Se esperaba ';'";
			if (categoriaR == TToken.TKPABRE)
				mensaje = mensaje + "Se esperaba '('";
			if (categoriaR == TToken.TKPCIERRA)
				mensaje = mensaje + "Se esperaba ')'";
			if (categoriaR == TToken.TKASIG)
				mensaje = mensaje + "Se esperaba ':='";
			if (categoriaR == TToken.TKSUMA)
				mensaje = mensaje + "Se esperaba '+'";
			if (categoriaR == TToken.TKRESTA)
				mensaje = mensaje + "Se esperaba '-'";
			if (categoriaR == TToken.TKOR)
				mensaje = mensaje + "Se esperaba '||'";
			if (categoriaR == TToken.TKPROD)
				mensaje = mensaje + "Se esperaba '*'";
			if (categoriaR == TToken.TKDIV)
				mensaje = mensaje + "Se esperaba '/'";
			if (categoriaR == TToken.TKMOD)
				mensaje = mensaje + "Se esperaba 'MOD'";
			if (categoriaR == TToken.TKAND)
				mensaje = mensaje + "Se esperaba '&'";
			if (categoriaR == TToken.TKIGUAL)
				mensaje = mensaje + "Se esperaba '=='";
			if (categoriaR == TToken.TKDESIGUAL)
				mensaje = mensaje + "Se esperaba '!='";
			if (categoriaR == TToken.TKMENOR)
				mensaje = mensaje + "Se esperaba '<'";
			if (categoriaR == TToken.TKMENORIGUAL)
				mensaje = mensaje + "Se esperaba '<='";
			if (categoriaR == TToken.TKMAYOR)
				mensaje = mensaje + "Se esperaba '>'.";
			if (categoriaR == TToken.TKMAYORIGUAL)
				mensaje = mensaje + "Se esperaba '>='";
			if (categoriaR == TToken.TKNEG)
				mensaje = mensaje + "Se esperaba '!'";
			if (categoriaR == TToken.TKSI)
				mensaje = mensaje + "Se esperaba 'SI'";
			if (categoriaR == TToken.TKENTONCES)
				mensaje = mensaje + "Se esperaba 'ENTONCES'";
			if (categoriaR == TToken.TKSINO)
				mensaje = mensaje + "Se esperaba 'SINO'";
			if (categoriaR == TToken.TKMIENTRAS)
				mensaje = mensaje + "Se esperaba 'MIENTRAS'";
			if (categoriaR == TToken.TKHACER)
				mensaje = mensaje + "Se esperaba 'HACER'";
			if (categoriaR == TToken.TKTIPO)
				mensaje = mensaje + "Se esperaba 'TIPO'";
			if (categoriaR == TToken.TKREG)
				mensaje = mensaje + "Se esperaba 'REG'";
			if (categoriaR == TToken.TKPROC)
				mensaje = mensaje + "Se esperaba 'PROC'";
			if (categoriaR == TToken.TKNUEVO)
				mensaje = mensaje + "Se esperaba 'NUEVO'";
			if (categoriaR == TToken.TKGORRO)
				mensaje = mensaje + "Se esperaba '^'";
			if (categoriaR == TToken.TKPUNTO)
				mensaje = mensaje + "Se esperaba '.'";
			if (categoriaR == TToken.TKPROCEDIMIENTO)
				mensaje = mensaje + "Se esperaba 'PROCEDIMIENTO'";

			mensaje = "Línea " + ((AnalizadorLexico) aLexico).getLinea() + ": "
					+ mensaje + ".";
			// Creamos el error sintáctico con el mensaje.
			error = new Error(TError.SINTACTICO, mensaje);
			err = true;
			return false;
		}
		return true;
	}

	/**
	 * Comprueba si un token leido es válido o se presenta un error léxico.
	 * @param tk token Token leido.
	 * @return Cierto si hay error léxico.
	 */
	private boolean comprobarErrorLexico(TToken tk) {
		if (tk == null) {
			err = true;
			error = ((AnalizadorLexico) aLexico).getError();
			return true;
		}
		return false;
	}

}
