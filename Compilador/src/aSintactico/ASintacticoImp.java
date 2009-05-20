package aSintactico;

import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import aLexico.ALexico;
import aLexico.EnumToken;
import aLexico.Token;
import tablaSimbolos.*;
import tablaSimbolos.PropTiposPointer;
import tablaSimbolos.TElemento;
import tablaSimbolos.TTipo;
import tablaSimbolos.TablaSimbolos;

public class ASintacticoImp extends ASintactico {

	private Token token;

	private int direccion;
	
	private int contadorInstrucciones=0;
	
	private int nivel;
	
	private boolean hayError;
	
	private PrintWriter ficheroSalida;
	
	private ArrayList<String> codigoTexto = new ArrayList<String>();

	private int tamaño;
	
	private String nombreAmbito;
	
	public boolean getErrores() {
		return hayError;
	}

	public void analizar(FileReader ficheroFuente) throws Exception {
		direccion = 0;
		hayError = false;
		ALexico.getInstance().setFichero(ficheroFuente);
		reconocePrograma();
	}
	
	private void volcarFicheroCodigo() {
		for (int i=0;i<codigoTexto.size();i++){
			this.ficheroSalida.println(codigoTexto.get(i));
		}
		ficheroSalida.close();
		
	}

	private void reconocePrograma() throws Exception {
		tamaño=0;
		reconoceCabecera();
		reconoceRestoPrograma();
		token = ALexico.getInstance().obtenerToken();
		compruebaTokens(token.getCategoria(),EnumToken.PUNTO);
		token = ALexico.getInstance().obtenerToken();
		if (token!=null&&comparaTokens(token.getCategoria(),EnumToken.PUNTO)){
			String error="Error Sintactico: Línea "+ALexico.getInstance().getLinea()+"'.' No esperado";
			throw new Exception(error);
		}
		volcarFicheroCodigo();		
	}
	
	private void reconoceCabecera() throws Exception {
		
		token = ALexico.getInstance().obtenerToken();
		if (!hayErrorLexico(token)
				&& (compruebaTokens(token.getCategoria(),
						EnumToken.PROGRAM))) {
			reconoceIdentificadorCabecera();
		}
		token = ALexico.getInstance().obtenerToken();
		if (!hayErrorLexico(token)
				&& (compruebaTokens(token.getCategoria(),
						EnumToken.PUNTOYCOMA))) {
			token = ALexico.getInstance().obtenerToken();
		}
	}
	
	
	private void reconoceRestoPrograma()throws Exception {
			int tmp0=0;
			if (!hayErrorLexico(token)
					&& (comparaTokens(token.getCategoria(), EnumToken.CONST))) {
				reconoceZonaConstantes();
			}
			if (!hayErrorLexico(token)
					&& (comparaTokens(token.getCategoria(), EnumToken.TYPE))) {
				reconoceZonaTipos();
			}
			if (!hayErrorLexico(token)
					&& (comparaTokens(token.getCategoria(), EnumToken.VAR))) {
				reconoceDeclaraciones();
			}
			if (!hayErrorLexico(token)
					&& (comparaTokens(token.getCategoria(), EnumToken.PROCEDURE))) {
				String codigo="cargaCP;.";
				emitirCodigo(codigo);
				codigo="ir-a();.";
				emitirCodigo(codigo);
				tmp0=contadorInstrucciones;
				direccion=1;
//////////////////////Ojo con el dos
				reconoceProcedimiento();
				epilogo(nivel+1);
				codigo="irIndice;.";
				emitirCodigo(codigo);
				while (!hayErrorLexico(token)
						&& (!comparaTokens(token.getCategoria(), EnumToken.BEGIN))){
					if (!hayErrorLexico(token)
							&& (compruebaTokens(token.getCategoria(), EnumToken.PROCEDURE))) {
						direccion=1;
						reconoceProcedimiento();
						epilogo(nivel+1);
						codigo="irIndice;.";
						emitirCodigo(codigo);
					}
				}
				parchea(tmp0,contadorInstrucciones);
			}
			if (!hayErrorLexico(token)
				&& (compruebaTokens(token.getCategoria(), EnumToken.BEGIN))) {
		    reconoceCuerpo();
		}	
	}


	private void reconoceProcedimiento() throws Exception{
		token = ALexico.getInstance().obtenerToken();
		if (!hayErrorLexico(token)
				&& (compruebaTokens(token.getCategoria(),
						EnumToken.IDENTIFICADOR))) {
			String nombreProc = token.getLexema();
			token = ALexico.getInstance().obtenerToken();
			if (!hayErrorLexico(token)
					&& (comparaTokens(token.getCategoria(),
							EnumToken.PUNTOYCOMA))){
				TablaSimbolos tabla = TablaSimbolos.getInstance();
				if (!existeID(nombreProc)) {
					prologo(nivel,tamaño);
					reconocePuntoYComa();
					nombreAmbito = nombreProc;
					PropTiposPro p= new PropTiposPro (new ArrayList<Parametro>());
					tabla.añadeProcedimiento(nombreProc,nivel, p,contadorInstrucciones-13);								
					nivel++;
					tabla.crearNivel(nivel);
					tabla.añadeProcedimiento(nombreProc,nivel, p,contadorInstrucciones-13);
					reconoceRestoPrograma();
					token = ALexico.getInstance().obtenerToken();
					reconocePuntoYComa();
					tabla.liberarNivel(nivel);
					nivel--;
				}
			}
			else if (!hayErrorLexico(token)
					&& (compruebaTokens(token.getCategoria(),
							EnumToken.PARENTESISAPER))){
				TablaSimbolos tabla = TablaSimbolos.getInstance();
				if (!existeID(nombreProc)) {					
					nivel++;
					nombreAmbito = nombreProc;
					tabla.crearNivel(nivel);
					PropTiposPro p= new PropTiposPro (reconoceParametrosProcedimiento());
					int tamaux=0;
					for (int i=0;i<p.getParametros().size();i++){
					tamaux+=p.getParametros().get(i).getTipo().getTam();
					}
					prologo(nivel,tamaux);
					tabla.añadeProcedimiento(nombreProc,nivel-1, p,contadorInstrucciones-13);
					tabla.añadeProcedimiento(nombreProc,nivel, p,contadorInstrucciones-13);
					reconoceRestoPrograma();
					token = ALexico.getInstance().obtenerToken();
					reconocePuntoYComa();
					tabla.liberarNivel(nivel);
					nivel--;				
				}
			}
		}
		
	}

	private ArrayList<Parametro> reconoceParametrosProcedimiento() throws Exception{
		token = ALexico.getInstance().obtenerToken();
		ArrayList<Parametro> lparametros= new ArrayList<Parametro>();
		boolean fin=false;
		while(!fin){
			Modo porVariable=Modo.VALOR;
			ArrayList<String> aL = new ArrayList<String>();
			if (!hayErrorLexico(token)
					&& (comparaTokens(token.getCategoria(),
							EnumToken.VAR))) {
				porVariable=Modo.VARIABLE;
				token = ALexico.getInstance().obtenerToken();
			}
			if (!hayErrorLexico(token)
					&& (compruebaTokens(token.getCategoria(),
							EnumToken.IDENTIFICADOR))) {
				aL.add(token.getLexema());
			}
			token = ALexico.getInstance().obtenerToken();
			while (!hayErrorLexico(token)
					&& (comparaTokens(token.getCategoria(), EnumToken.PUNTOYCOMA))
					&& !hayError) {
				token = ALexico.getInstance().obtenerToken();
				if (!hayErrorLexico(token)
						&& (compruebaTokens(token.getCategoria(),
								EnumToken.IDENTIFICADOR))) {
					aL.add(token.getLexema());// resto
				}
				token = ALexico.getInstance().obtenerToken();
			}
			if (!hayErrorLexico(token)
					&& (compruebaTokens(token.getCategoria(),
							EnumToken.DOSPUNTOS))) {
				token = ALexico.getInstance().obtenerToken();// obtenemos el tipo

				if (!hayErrorLexico(token)
						&& (comparaTokens(token.getCategoria(),
								EnumToken.BOOLEAN))) {
					for (int i = 0; i < aL.size(); i++) {
						if (!existeID((String) aL.get(i))) {
							TablaSimbolos tabla = TablaSimbolos.getInstance();
							PropTipos p=tabla.dameTipo("boolean",nivel);
							tabla.añadeVariable((String) aL.get(i),direccion, p,nivel);
							Parametro par=new Parametro(porVariable,p,(String) aL.get(i));
							lparametros.add(par);
							direccion++;
						}
					}
				} else if (!hayErrorLexico(token)
						&& (comparaTokens(token.getCategoria(),
								EnumToken.INTEGER))) {
					for (int i = 0; i < aL.size(); i++) {
						if (!existeID((String) aL.get(i))) {
							TablaSimbolos tabla = TablaSimbolos.getInstance();
							PropTipos p=tabla.dameTipo("integer",nivel);
							tabla.añadeVariable((String) aL.get(i),direccion, p,nivel);
							Parametro par=new Parametro(porVariable,p,(String) aL.get(i));
							lparametros.add(par);
							direccion++;
						}
					}
				} else if (!hayErrorLexico(token)
						&& (comparaTokens(token.getCategoria(),
								EnumToken.IDENTIFICADOR))) {
					for (int i = 0; i < aL.size(); i++) {
						if (!existeID((String) aL.get(i))) {
							TablaSimbolos tabla = TablaSimbolos.getInstance();
							if (tabla.existeTipo(token.getLexema(),nivel)){
								PropTipos p=tabla.dameTipo(token.getLexema(),nivel);
								tabla.añadeVariable((String) aL.get(i),direccion, p,nivel);
								Parametro par=new Parametro(porVariable,p,(String) aL.get(i));
								lparametros.add(par);
								direccion++;
								//Emitir el codigo
							}
						}
					}
				}else if (!hayErrorLexico(token)
						&& (comparaTokens(token.getCategoria(),
								EnumToken.ARRAY))) {
					int tam=reconoceDeclaracionArray();
					for (int i = 0; i < aL.size(); i++) {
						if (!existeID((String) aL.get(i))) {
							TablaSimbolos tabla = TablaSimbolos.getInstance();
							if (tabla.existeTipo(token.getLexema(),nivel)){
								PropTipos p= tabla.dameTipo(token.getLexema(),nivel);
								tabla.añadeVariable((String) aL.get(i),direccion, p,nivel);
								Parametro par=new Parametro(porVariable,p,(String) aL.get(i));
								lparametros.add(par);
								direccion=direccion+tam*tabla.dameTipo(token.getLexema(),nivel).getTam();
								//Emitir el codigo
							}
						}
					}
				}else if (!hayErrorLexico(token)
						&& (compruebaTokens(token.getCategoria(),
								EnumToken.PUNTERO))) {
						token = ALexico.getInstance().obtenerToken();
						if (!hayErrorLexico(token)
								&& (compruebaTokens(token.getCategoria(),
										EnumToken.IDENTIFICADOR))) {
							for (int i = 0; i < aL.size(); i++) {
								if (!existeID((String) aL.get(i))) {
									TablaSimbolos tabla = TablaSimbolos.getInstance();
									if (tabla.existeTipo(token.getLexema(),nivel)){
										PropTipos p=tabla.dameTipo(token.getLexema(),nivel);
										tabla.añadeVariable((String) aL.get(i), direccion, p,nivel);
										Parametro par=new Parametro(porVariable,p,(String) aL.get(i));
										lparametros.add(par);
										direccion++;
										//Emitir el codigo
									}
								}
							}
						}
					}
				
			}
			token = ALexico.getInstance().obtenerToken();
			if (!hayErrorLexico(token)
					&& (comparaTokens(token.getCategoria(),
							EnumToken.PARENTESISCIER))) {
				fin=true;
			}
			else{
				compruebaTokens(token.getCategoria(),EnumToken.PUNTOYCOMA);
				token = ALexico.getInstance().obtenerToken();
			}
		}
		token = ALexico.getInstance().obtenerToken();
		reconocePuntoYComa();
		return lparametros;
	}
	
	private void reconoceZonaTipos()throws Exception {
		token = ALexico.getInstance().obtenerToken();
		reconoceTipo();
		reconoceRestoTipos();
	}

	private void reconoceRestoTipos()throws Exception {
		while (!hayErrorLexico(token)
				&& (comparaTokens(token.getCategoria(),
						EnumToken.IDENTIFICADOR)) && !hayError) {
			reconoceTipo();
		}
	}
		

	private void reconoceTipo() throws Exception {
		if (!hayErrorLexico(token)
				&& (compruebaTokens(token.getCategoria(),
						EnumToken.IDENTIFICADOR))) {
			String nombre = token.getLexema();
			TablaSimbolos Ts = TablaSimbolos.getInstance();
			if(!Ts.existeTipoNivel(nombre,nivel)){				
				token = ALexico.getInstance().obtenerToken();
				if (!hayErrorLexico(token)
						&& (compruebaTokens(token.getCategoria(),
								EnumToken.IGUAL))) {
					token = ALexico.getInstance().obtenerToken();
				
					if (!hayErrorLexico(token)
							&& (comparaTokens(token.getCategoria(),
									EnumToken.INTEGER))) {					
						PropTipos p=Ts.dameTipo(token.getLexema(),nivel);
						Ts.añadeTipo(nombre,p,nivel);
						}
					else if (!hayErrorLexico(token)
							&& (comparaTokens(token.getCategoria(),
									EnumToken.BOOLEAN))) {
						PropTipos p=Ts.dameTipo(token.getLexema(),nivel);
						Ts.añadeTipo(nombre,p,nivel);
						}
					else if (!hayErrorLexico(token)
							&& (comparaTokens(token.getCategoria(),
									EnumToken.IDENTIFICADOR))) {
							if (existeTipo(token.getLexema())) {
								PropTipos p=Ts.dameTipo(token.getLexema(),nivel);
								Ts.añadeTipo(nombre,p,nivel);
							}
						}
					else if (!hayErrorLexico(token)
							&& (comparaTokens(token.getCategoria(),
									EnumToken.ARRAY))) {
						token = ALexico.getInstance().obtenerToken();
						if (!hayErrorLexico(token)
								&& (compruebaTokens(token.getCategoria(),
										EnumToken.CORCHETEAPER))) {
							token = ALexico.getInstance().obtenerToken();
							int tam=reconoceIntervalo();
							token = ALexico.getInstance().obtenerToken();
							if (!hayErrorLexico(token)
									&& (compruebaTokens(token.getCategoria(),
											EnumToken.CORCHETECIER))) {
								token = ALexico.getInstance().obtenerToken();
								if (!hayErrorLexico(token)
										&& (compruebaTokens(token.getCategoria(),
												EnumToken.OF))) {
									token = ALexico.getInstance().obtenerToken();		
									TablaSimbolos tabla = TablaSimbolos.getInstance();
									if (tabla.existeTipo(token.getLexema(),nivel)){
										PropTiposArray p=new PropTiposArray(tam,Ts.dameTipo(token.getLexema(),nivel));
										Ts.añadeTipo(nombre,p,nivel);
									
									}																																																						
								}
							}																
						}
					}
					else if (!hayErrorLexico(token)
							&& (compruebaTokens(token.getCategoria(),
									EnumToken.PUNTERO))) {
							token = ALexico.getInstance().obtenerToken();
							TablaSimbolos tabla = TablaSimbolos.getInstance();
							if (tabla.existeTipo(token.getLexema(),nivel)){
								PropTiposPointer p=new PropTiposPointer(Ts.dameTipo(token.getLexema(),nivel));
								Ts.añadeTipo(nombre,p,nivel);
							
							}								
						}
					token = ALexico.getInstance().obtenerToken();
					reconocePuntoYComa();
					}						
				}
			else{throw new Exception("Identificador de tipo: "+nombre+" repetido "+ ALexico.getInstance().getLinea());
				
			}
			}
		}


	private int reconoceIntervalo()throws Exception {
		int num=Integer.parseInt(token.getLexema());
		if(num!=0)throw new Exception("Intervalo no válido "+ ALexico.getInstance().getLinea());		
		else{
			token = ALexico.getInstance().obtenerToken();
			if (!hayErrorLexico(token)&& (compruebaTokens(token.getCategoria(),EnumToken.PUNTO))) {
				token = ALexico.getInstance().obtenerToken();
				if (!hayErrorLexico(token)
						&& (compruebaTokens(token.getCategoria(),
								EnumToken.PUNTO))) {
					token = ALexico.getInstance().obtenerToken();
					if (!hayErrorLexico(token)
							&& (compruebaTokens(token.getCategoria(),
									EnumToken.NUMERO))) {
						int num2=Integer.parseInt(token.getLexema());
						if(num2<0)throw new Exception("El intervalo debe ser positivo "+ ALexico.getInstance().getLinea());					
						return num2+1;
						}
					}
				}
		}
		return 0;
	}		



	private void reconoceZonaConstantes() throws Exception {
		token = ALexico.getInstance().obtenerToken();
		reconoceConstantes();
		reconoceRestoConstantes();
	}

	
	private void reconoceRestoConstantes() throws Exception{
		while (!hayErrorLexico(token)
				&& (comparaTokens(token.getCategoria(),
						EnumToken.IDENTIFICADOR)) && !hayError) {
			reconoceConstantes();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private void reconoceConstantes() throws Exception {
		if (!hayErrorLexico(token)
				&& (compruebaTokens(token.getCategoria(),
						EnumToken.IDENTIFICADOR))) {
			String nombre = token.getLexema();
			token = ALexico.getInstance().obtenerToken();
			if (!hayErrorLexico(token)
					&& (compruebaTokens(token.getCategoria(),
							EnumToken.IGUAL))) {
				token = ALexico.getInstance().obtenerToken();
				if (!hayErrorLexico(token)
						&& (comparaTokens(token.getCategoria(),
								EnumToken.TRUE))) {
					if (!existeIDNivel(nombre)) {
						TablaSimbolos Ts = TablaSimbolos.getInstance();
						PropTipos p=Ts.dameTipo("boolean",nivel);
						Ts.añadeConstante(nombre,direccion,p,nivel);
						String codigoEmitido="apila(TRUE)"+";"+"desapilaDireccion("+(direccion-1)+");.";
						emitirCodigo(codigoEmitido);
						direccion++;
					}
				} else if (!hayErrorLexico(token)
						&& (comparaTokens(token.getCategoria(),
								EnumToken.FALSE))) {
					if (!existeIDNivel(nombre)) {
		
						TablaSimbolos Ts = TablaSimbolos.getInstance();
						PropTipos p=Ts.dameTipo("boolean",nivel);
						Ts.añadeConstante(nombre,direccion,p,nivel);
						String codigoEmitido="apila(FALSE)"+";"+"desapilaDireccion("+(direccion-1)+");.";
						emitirCodigo(codigoEmitido);
						direccion++;
					}
				} else if (!hayErrorLexico(token)
						&& (comparaTokens(token.getCategoria(),
								EnumToken.NUMERO))) {
					if (!existeIDNivel(nombre)) {
						TablaSimbolos Ts = TablaSimbolos.getInstance();
						PropTipos p=Ts.dameTipo("integer",nivel);
						Ts.añadeConstante(nombre,direccion,p,nivel);
						String codigoEmitido="apila("+token.getLexema()+")"+";"+"desapilaDireccion("+(direccion-1)+");.";
						emitirCodigo(codigoEmitido);
						direccion++;
					}
				}else if (!hayErrorLexico(token)
						&& (compruebaTokens(token.getCategoria(),
								EnumToken.CORCHETEAPER))) {
					if (!existeIDNivel(nombre)) {
						TablaSimbolos Ts = TablaSimbolos.getInstance();
						token = ALexico.getInstance().obtenerToken();
						String tipo="";
						if (comparaTokens(token.getCategoria(),EnumToken.NUMERO)) tipo="integer";
						else if (comparaTokens(token.getCategoria(),EnumToken.TRUE)||comparaTokens(token.getCategoria(),EnumToken.FALSE)) tipo="boolean";
						else if(compruebaTokens(token.getCategoria(),EnumToken.IDENTIFICADOR)){
							tipo=tipoConstante(token.getLexema());
						}
						ArrayList arrayConstante = obtenerArrayConstante(tipo);
						PropTiposArray p=new PropTiposArray(arrayConstante.size(),Ts.dameTipo(tipo,nivel));
						Ts.añadeConstante(nombre,direccion,p,nivel);
						direccion=direccion+arrayConstante.size();
					}
				}
				token = ALexico.getInstance().obtenerToken();
				reconocePuntoYComa();
			}
		}
	}
	
	
	@SuppressWarnings("unchecked")
	//Aqui habria que hacer el emite codigo
	private ArrayList obtenerArrayConstante(String tipo) throws Exception {
		ArrayList<String> aConst= new ArrayList<String>();
		if (tipo.equals("integer")){
			aConst.add(token.getLexema());
			token = ALexico.getInstance().obtenerToken();
			while (!hayErrorLexico(token)&&(comparaTokens(token.getCategoria(),EnumToken.COMA))){
				token = ALexico.getInstance().obtenerToken();
				if (!hayErrorLexico(token)&& (comparaTokens(token.getCategoria(),EnumToken.NUMERO))){
					aConst.add(token.getLexema());
					token = ALexico.getInstance().obtenerToken();
				}
				else if (!hayErrorLexico(token)&& (compruebaTokens(token.getCategoria(),EnumToken.IDENTIFICADOR))&&(tipoConstante(token.getLexema()).equals("integer"))){
					aConst.add(token.getLexema());
					token = ALexico.getInstance().obtenerToken();
				}
			}
			compruebaTokens(token.getCategoria(),EnumToken.CORCHETECIER);
			return aConst;
		}
		if (tipo.equals("boolean")){
			aConst.add(token.getLexema());
			token = ALexico.getInstance().obtenerToken();
			while (!hayErrorLexico(token)&&(comparaTokens(token.getCategoria(),EnumToken.COMA))){
				token = ALexico.getInstance().obtenerToken();
				if (!hayErrorLexico(token)&& (comparaTokens(token.getCategoria(),EnumToken.TRUE)||comparaTokens(token.getCategoria(),EnumToken.FALSE))){
					aConst.add(token.getLexema());
					token = ALexico.getInstance().obtenerToken();
				}
				else if (!hayErrorLexico(token)&& (compruebaTokens(token.getCategoria(),EnumToken.IDENTIFICADOR))&&(tipoConstante(token.getLexema()).equals("boolean"))){
					aConst.add(token.getLexema());
					token = ALexico.getInstance().obtenerToken();
				}
			}
			compruebaTokens(token.getCategoria(),EnumToken.CORCHETECIER);
			return aConst;
		}
		return null;
	}
	
	private String tipoConstante(String nombre)throws Exception{
		TablaSimbolos Ts = TablaSimbolos.getInstance();
		if (Ts.dameTipo(nombre,nivel).getNombreTipo()==TTipo.BOOLEAN) return "boolean";
		else if (Ts.dameTipo(nombre,nivel).getNombreTipo()==TTipo.INTEGER) return "integer";
		else throw new Exception("Valor introducido no válido "+ ALexico.getInstance().getLinea());
	}

	private void emitirCodigo(String codigoEmitido) {
		String subCadena="";
		int puntero=0;
		while (codigoEmitido.charAt(puntero)!='.'){
			while (codigoEmitido.charAt(puntero)!=';'){
				subCadena+=codigoEmitido.charAt(puntero);
				puntero++;
			}
		codigoTexto.add(subCadena);
		subCadena="";
		puntero++;
		contadorInstrucciones++;
		}
	}

	private void reconoceDeclaraciones() throws Exception {
		token = ALexico.getInstance().obtenerToken();
		reconoceDeclaracion();
		reconoceRestoDeclaracion();
	}
	

	private void reconoceDeclaracion() throws Exception {
		ArrayList<String> aL = new ArrayList<String>();
		if (!hayErrorLexico(token)
				&& (compruebaTokens(token.getCategoria(),
						EnumToken.IDENTIFICADOR))) {
			aL.add(token.getLexema());
		}
		token = ALexico.getInstance().obtenerToken();
		while (!hayErrorLexico(token)
				&& (comparaTokens(token.getCategoria(), EnumToken.COMA))
				&& !hayError) {
			token = ALexico.getInstance().obtenerToken();
			if (!hayErrorLexico(token)
					&& (compruebaTokens(token.getCategoria(),
							EnumToken.IDENTIFICADOR))) {
				aL.add(token.getLexema());// resto
			}
			token = ALexico.getInstance().obtenerToken();
		}
		if (!hayErrorLexico(token)
				&& (compruebaTokens(token.getCategoria(),
						EnumToken.DOSPUNTOS))) {
			token = ALexico.getInstance().obtenerToken();// obtenemos el tipo

			if (!hayErrorLexico(token)
					&& (comparaTokens(token.getCategoria(),
							EnumToken.BOOLEAN))) {
				for (int i = 0; i < aL.size(); i++) {
					if (!existeIDNivel((String) aL.get(i))) {
						TablaSimbolos tabla = TablaSimbolos.getInstance();
						PropTipos p=tabla.dameTipo("boolean",nivel);
						tabla.añadeVariable((String) aL.get(i),direccion, p,nivel);
						String codigoEmitido="apilaDireccion("+(1+nivel)+");.";
						emitirCodigo(codigoEmitido);
						codigoEmitido="apila("+(direccion-1)+");.";
						emitirCodigo(codigoEmitido);
						codigoEmitido="suma;.";
						emitirCodigo(codigoEmitido);
						codigoEmitido="apila(FALSE);.";
						emitirCodigo(codigoEmitido);
						codigoEmitido="desapilaIndice;.";
						emitirCodigo(codigoEmitido);
						direccion++;
					}
				}
			} else if (!hayErrorLexico(token)
					&& (comparaTokens(token.getCategoria(),
							EnumToken.INTEGER))) {
				for (int i = 0; i < aL.size(); i++) {
					if (!existeIDNivel((String) aL.get(i))) {
						TablaSimbolos tabla = TablaSimbolos.getInstance();
						PropTipos p=tabla.dameTipo("integer",nivel);
						tabla.añadeVariable((String) aL.get(i),direccion, p,nivel);
						String codigoEmitido="apilaDireccion("+(1+nivel)+");.";
						emitirCodigo(codigoEmitido);
						codigoEmitido="apila("+(direccion-1)+");.";
						emitirCodigo(codigoEmitido);
						codigoEmitido="suma;.";
						emitirCodigo(codigoEmitido);
						codigoEmitido="apila(0);.";
						emitirCodigo(codigoEmitido);
						codigoEmitido="desapilaIndice;.";
						emitirCodigo(codigoEmitido);
						direccion++;
					}
				}
			} else if (!hayErrorLexico(token)
					&& (comparaTokens(token.getCategoria(),
							EnumToken.IDENTIFICADOR))) {
				for (int i = 0; i < aL.size(); i++) {
					if (!existeIDNivel((String) aL.get(i))) {
						TablaSimbolos tabla = TablaSimbolos.getInstance();
						if (tabla.existeTipo(token.getLexema(),nivel)){
							PropTipos p=tabla.dameTipo(token.getLexema(),nivel);
							tabla.añadeVariable((String) aL.get(i),direccion, p,nivel);
							if (compruebaTiposNoExcep(p,tabla.dameTipo("boolean",nivel))){
								//Array prototipado en booleanos
								String codigoEmitido="apilaDireccion("+(1+nivel)+");.";
								emitirCodigo(codigoEmitido);
								codigoEmitido="apila("+(direccion-1)+");.";
								emitirCodigo(codigoEmitido);
								codigoEmitido="suma;.";
								emitirCodigo(codigoEmitido);
								codigoEmitido="apila(FALSE);.";
								emitirCodigo(codigoEmitido);
								codigoEmitido="desapilaIndice;.";
								emitirCodigo(codigoEmitido);
							}
							else if (compruebaTiposNoExcep(p,tabla.dameTipo("integer",nivel))){
								//Array prototipado en enteros
								String codigoEmitido="apilaDireccion("+(1+nivel)+");.";
								emitirCodigo(codigoEmitido);
								codigoEmitido="apila("+(direccion-1)+");.";
								emitirCodigo(codigoEmitido);
								codigoEmitido="suma;.";
								emitirCodigo(codigoEmitido);
								codigoEmitido="apila(0);.";
								emitirCodigo(codigoEmitido);
								codigoEmitido="desapilaIndice;.";
								emitirCodigo(codigoEmitido);
								}
							else{
								String codigoEmitido="apilaDireccion("+(1+nivel)+");.";
								emitirCodigo(codigoEmitido);
								codigoEmitido="apila("+(direccion-1)+");.";
								emitirCodigo(codigoEmitido);
								codigoEmitido="suma;.";
								emitirCodigo(codigoEmitido);
								codigoEmitido="apila(null);.";
								emitirCodigo(codigoEmitido);
								codigoEmitido="desapilaIndice;.";
								emitirCodigo(codigoEmitido);
							}
							direccion++;
						}
					}
				}
			}else if (!hayErrorLexico(token)
					&& (comparaTokens(token.getCategoria(),
							EnumToken.ARRAY))) {
				int tam=reconoceDeclaracionArray();
				for (int i = 0; i < aL.size(); i++) {
					if (!existeIDNivel((String) aL.get(i))) {
						TablaSimbolos tabla = TablaSimbolos.getInstance();
						if (tabla.existeTipo(token.getLexema(),nivel)){
							PropTiposArray p= new PropTiposArray(tam,tabla.dameTipo(token.getLexema(),nivel));
							tabla.añadeVariable((String) aL.get(i),direccion, p,nivel);
							int tope=direccion+tam*tabla.dameTipo(token.getLexema(),nivel).getTam();
							for (int j=tope-tam*tabla.dameTipo(token.getLexema(),nivel).getTam();j<tope;j++){
								//Emitir la dupla de codigo correspondiente al tipado basico del array
								if (compruebaTiposNoExcep(p.getRef(),tabla.dameTipo("boolean",nivel))){
									//Array prototipado en booleanos
									String codigoEmitido="apilaDireccion("+(nivel+1)+");.";
									emitirCodigo(codigoEmitido);
									codigoEmitido="apila("+(j-(tope-tam*tabla.dameTipo(token.getLexema(),nivel).getTam()))+");.";
									emitirCodigo(codigoEmitido);
									codigoEmitido="suma;.";
									emitirCodigo(codigoEmitido);
									codigoEmitido="apila("+(direccion-1)+");.";
									emitirCodigo(codigoEmitido);
									codigoEmitido="suma;.";
									emitirCodigo(codigoEmitido);
									codigoEmitido="apila(FALSE);.";
									emitirCodigo(codigoEmitido);
									codigoEmitido="desapilaIndice;.";
									emitirCodigo(codigoEmitido);
								}
								else if (compruebaTiposNoExcep(p.getRef(),tabla.dameTipo("integer",nivel))){
									//Array prototipado en enteros
									String codigoEmitido="apilaDireccion("+(nivel+1)+");.";
									emitirCodigo(codigoEmitido);
									codigoEmitido="apila("+(j-(tope-tam*tabla.dameTipo(token.getLexema(),nivel).getTam()))+");.";
									emitirCodigo(codigoEmitido);
									codigoEmitido="suma;.";
									emitirCodigo(codigoEmitido);
									codigoEmitido="apila("+(direccion-1)+");.";
									emitirCodigo(codigoEmitido);
									codigoEmitido="suma;.";
									emitirCodigo(codigoEmitido);
									codigoEmitido="apila(0);.";
									emitirCodigo(codigoEmitido);
									codigoEmitido="desapilaIndice;.";
									emitirCodigo(codigoEmitido);
									}
								else{
									throw new Exception("Tipo no aceptado en esta versión en línea "+ ALexico.getInstance().getLinea());
								}
							}
							//Emitir el codigo
						}
					}
					
				}
				
			}else if (!hayErrorLexico(token)
					&& (compruebaTokens(token.getCategoria(),
							EnumToken.PUNTERO))) {
					token = ALexico.getInstance().obtenerToken();
					if (!hayErrorLexico(token)
							&& (comparaTokens(token.getCategoria(),
									EnumToken.INTEGER))) {
						for (int i = 0; i < aL.size(); i++) {
							if (!existeIDNivel((String) aL.get(i))) {
								TablaSimbolos tabla = TablaSimbolos.getInstance();
								PropTiposPointer p=new PropTiposPointer(tabla.dameTipo("integer",nivel));
								tabla.añadeVariable((String) aL.get(i), direccion, p,nivel);
								String codigoEmitido="apilaDireccion("+(1+nivel)+");.";
								emitirCodigo(codigoEmitido);
								codigoEmitido="apila("+(direccion-1)+");.";
								emitirCodigo(codigoEmitido);
								codigoEmitido="suma;.";
								emitirCodigo(codigoEmitido);
								codigoEmitido="apila(null);.";
								emitirCodigo(codigoEmitido);
								codigoEmitido="desapilaIndice;.";
								emitirCodigo(codigoEmitido);
								direccion++;
							
							}
						}						
					}
					else if (!hayErrorLexico(token)
							&& (comparaTokens(token.getCategoria(),
									EnumToken.BOOLEAN))) {
						for (int i = 0; i < aL.size(); i++) {
							if (!existeIDNivel((String) aL.get(i))) {
								TablaSimbolos tabla = TablaSimbolos.getInstance();
								PropTiposPointer p=new PropTiposPointer(tabla.dameTipo("boolean",nivel));
								tabla.añadeVariable((String) aL.get(i), direccion, p,nivel);
								String codigoEmitido="apilaDireccion("+(1+nivel)+");.";
								emitirCodigo(codigoEmitido);
								codigoEmitido="apila("+(direccion-1)+");.";
								emitirCodigo(codigoEmitido);
								codigoEmitido="suma;.";
								emitirCodigo(codigoEmitido);
								codigoEmitido="apila(null);.";
								emitirCodigo(codigoEmitido);
								codigoEmitido="desapilaIndice;.";
								emitirCodigo(codigoEmitido);
								direccion++;
							}
						}
						
						
					}
					
					else if (!hayErrorLexico(token)
							&& (comparaTokens(token.getCategoria(),
									EnumToken.IDENTIFICADOR))) {
						for (int i = 0; i < aL.size(); i++) {
							if (!existeIDNivel((String) aL.get(i))) {
								TablaSimbolos tabla = TablaSimbolos.getInstance();
								if (tabla.existeTipo(token.getLexema(),nivel)){
									PropTiposPointer p=new PropTiposPointer(tabla.dameTipo(token.getLexema(),nivel));
									tabla.añadeVariable((String) aL.get(i), direccion, p,nivel);
									direccion++;
									//Emitir el codigo
								}
							}
						}
					}
					else if (!hayErrorLexico(token)
							&& (compruebaTokens(token.getCategoria(),
									EnumToken.ARRAY))) {

						int tam=reconoceDeclaracionArray();
						for (int i = 0; i < aL.size(); i++) {
							if (!existeIDNivel((String) aL.get(i))) {
								TablaSimbolos tabla = TablaSimbolos.getInstance();
								if (tabla.existeTipo(token.getLexema(),nivel)){
									PropTiposArray p= new PropTiposArray(tam,tabla.dameTipo(token.getLexema(),nivel));
									PropTiposPointer punt=new PropTiposPointer(p);
									tabla.añadeVariable((String) aL.get(i),direccion, punt,nivel);
									direccion=direccion+tam*tabla.dameTipo(token.getLexema(),nivel).getTam();
									for (int j=direccion-tam*tabla.dameTipo(token.getLexema(),nivel).getTam();j<direccion;j++){
										//Emitir la dupla de codigo correspondiente al tipado basico del array
										if (compruebaTiposNoExcep(p.getRef(),tabla.dameTipo("boolean",nivel))){
											//Array prototipado en booleanos
										String codigoEmitido="apila(FALSE)"+";"+"desapilaDireccion("+(j-1)+");.";
										emitirCodigo(codigoEmitido);
										}
										else if (compruebaTiposNoExcep(p.getRef(),tabla.dameTipo("integer",nivel))){
											//Array prototipado en enteros
											String codigoEmitido="apila(0)"+";"+"desapilaDireccion("+(j-1)+");.";
											emitirCodigo(codigoEmitido);
											}
										else{
											throw new Exception("No se permiten arrays multidimensionales ni arrays de punteros en línea "+ ALexico.getInstance().getLinea());
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
	
	
	private int reconoceDeclaracionArray() throws Exception {
		int tam=0;
		token = ALexico.getInstance().obtenerToken();
		if (!hayErrorLexico(token)&& (compruebaTokens(token.getCategoria(),EnumToken.CORCHETEAPER))) {
			token = ALexico.getInstance().obtenerToken();
			tam=reconoceIntervalo();
			token = ALexico.getInstance().obtenerToken();
			if (!hayErrorLexico(token)&& (compruebaTokens(token.getCategoria(),EnumToken.CORCHETECIER))) {
				token = ALexico.getInstance().obtenerToken();
				if (!hayErrorLexico(token)&& (compruebaTokens(token.getCategoria(),EnumToken.OF))) {
					token = ALexico.getInstance().obtenerToken();
				}
			}
		}
		return tam;
	}

	private void reconoceRestoDeclaracion() throws Exception {
		while (!hayErrorLexico(token)
				&& (comparaTokens(token.getCategoria(), EnumToken.IDENTIFICADOR))
				&& !hayError) {
			reconoceDeclaracion();
		}
	}	
	
	
	private void reconoceCuerpo() throws Exception {
		token = ALexico.getInstance().obtenerToken();
		if (!hayErrorLexico(token)&&!comparaTokens(token.getCategoria(), EnumToken.END)){
			reconoceInstrucciones();
		}
		
	}
	
	
	private void reconoceInstrucciones() throws Exception {
		reconoceInstruccion();
		reconoceRestoInstrucciones();
	}
	
	
	private void reconoceInstruccion() throws Exception {
		TablaSimbolos t=TablaSimbolos.getInstance();
		if (!hayErrorLexico(token)&& (comparaTokens(token.getCategoria(),EnumToken.IDENTIFICADOR))) {
			String asignada=token.getLexema();
			token=ALexico.getInstance().obtenerToken();
			if(comparaTokens(token.getCategoria(),EnumToken.ASIGNACION)){
				//es una variable normal, se queda como en la primera version,
			
				if (IDDeclarado(asignada)&& (t.obtenerInfo(asignada,nivel).dameElemento()==TElemento.VAR)){							
					PropTipos p=t.obtenerInfo(asignada,nivel).getProp();
					if (p.getNombreTipo()==TTipo.PUNTERO){
						p=new PropTipos(TTipo.VACIO,0);
					}
					if(compruebaTipos (p,reconoceAsignacion())){
						String codigoEmitido="apila("+(t.obtenerInfo(asignada,nivel).dameDir()-1)+");.";
						emitirCodigo(codigoEmitido);
						codigoEmitido="apila("+(1+(t.dameNivel(asignada,nivel)))+");.";
						emitirCodigo(codigoEmitido);
						codigoEmitido="apilaIndice;.";
						emitirCodigo(codigoEmitido);
						codigoEmitido="suma;.";
						emitirCodigo(codigoEmitido);
						if (nivel>0){
							PropTiposPro aux=((PropTiposPro)t.dameTipo(nombreAmbito,nivel));
							String aux2="";
							for (int i=0;i<aux.getParametros().size();i++){
								aux2=aux.getParametros().get(i).getNombre();
								if ((aux2.equals(asignada))&&(aux.getParametros().get(i).getModo()==Modo.VARIABLE)){
									String codigo="apilaIndice;.";
									emitirCodigo(codigo);	
								}
							}
						}
						codigoEmitido="flip;.";
						emitirCodigo(codigoEmitido);
						codigoEmitido="desapilaIndice;.";
						emitirCodigo(codigoEmitido);
						reconocePuntoYComa();
					}																																	
				}
			}
			else if(comparaTokens(token.getCategoria(),EnumToken.CORCHETEAPER)){
				//es un array, el nombre del array esta en asignada
				token=ALexico.getInstance().obtenerToken();
				if(compruebaTipos(reconoceExpresion(),t.dameTipo("integer",nivel))){
					if(compruebaTokens(token.getCategoria(),EnumToken.CORCHETECIER)){
						//Obtener direccion del array
						String codigoEmitir="apila("+(t.obtenerInfo(asignada,nivel).dameDir()-1)+");.";
						emitirCodigo(codigoEmitir);
						codigoEmitir="apilaDireccion("+(1+(t.dameNivel(asignada,nivel)))+");.";
						emitirCodigo(codigoEmitir);
						codigoEmitir="suma;.";
						emitirCodigo(codigoEmitir);
						codigoEmitir="suma;.";
						emitirCodigo(codigoEmitir);
						token=ALexico.getInstance().obtenerToken();
						if(comparaTokens(token.getCategoria(),EnumToken.ASIGNACION)){
											
							if (IDDeclarado(asignada)&& (t.obtenerInfo(asignada,nivel).dameElemento()==TElemento.VAR)){							
								PropTiposArray p=(PropTiposArray)t.obtenerInfo(asignada,nivel).getProp();
								if(compruebaTipos (p.getRef(),reconoceAsignacion())){
									String codigo="desapilaIndice;.";
									emitirCodigo(codigo);
									reconocePuntoYComa();
								}																																	
							}
						}
					}
				}
			}
			else if(comparaTokens(token.getCategoria(),EnumToken.PUNTERO)){			
				//el nombre del puntero está en asignada
				if (IDDeclarado(asignada)&& (t.obtenerInfo(asignada,nivel).dameElemento()==TElemento.VAR)){							
					token=ALexico.getInstance().obtenerToken();
					if (!hayErrorLexico(token)&&comparaTokens(token.getCategoria(),EnumToken.CORCHETEAPER)){
						token=ALexico.getInstance().obtenerToken();
						if(compruebaTipos(reconoceExpresion(),t.dameTipo("integer",nivel))){
							if (!hayErrorLexico(token)&&compruebaTokens(token.getCategoria(),EnumToken.CORCHETECIER)){																
								token=ALexico.getInstance().obtenerToken();
								if (!hayErrorLexico(token)&&compruebaTokens(token.getCategoria(),EnumToken.ASIGNACION)){
									PropTipos p=((PropTiposArray)(((PropTiposPointer)t.dameTipo(asignada, nivel)).getRef())).getRef();
									if(compruebaTipos(p,reconoceAsignacion())){
										reconocePuntoYComa();
										//emitir codigo																																							
									}
								}
							}
						}
					}
				else if (!hayErrorLexico(token)&&comparaTokens(token.getCategoria(),EnumToken.ASIGNACION)){
						
					PropTiposPointer p =(PropTiposPointer) t.obtenerInfo(asignada,nivel).getProp();
					String codigo="apilaDireccion("+(1+(t.dameNivel(asignada,nivel)))+");.";
					emitirCodigo(codigo);
					codigo="apila("+(TablaSimbolos.getInstance().obtenerInfo(asignada,nivel).dameDir()-1)+");.";
					emitirCodigo(codigo);
					codigo="suma;.";
					emitirCodigo(codigo);
					codigo="apilaIndice;.";//////???????
					emitirCodigo(codigo);
					if(compruebaTipos (p.getRef(),reconoceAsignacion())){
						reconocePuntoYComa();
						codigo="desapilaIndice;.";
						emitirCodigo(codigo);															
					}	
				}
				}
			}
			else if (comparaTokens(token.getCategoria(),EnumToken.PARENTESISAPER)){
				//llamada a un procedimiento con parentesis
				int i =0;
				int nParam=((PropTiposPro)t.dameTipo(asignada,nivel)).getParametros().size();
				tamaño=0;
				token=ALexico.getInstance().obtenerToken();
				apilaRet((9*nParam)+contadorInstrucciones+11);
				String excodigo="apilaDireccion(0);.";
				emitirCodigo(excodigo);
				excodigo="apila(2);.";
				emitirCodigo(excodigo);
				excodigo="suma;.";
				emitirCodigo(excodigo);
				excodigo="apila(Hueco);.";
				emitirCodigo(excodigo);
				excodigo="desapilaIndice;.";
				emitirCodigo(excodigo);
				while (!comparaTokens(token.getCategoria(),EnumToken.PARENTESISCIER)){
						if(i>=nParam){throw new Exception("Número de parámetros incorrecto en línea "+ ALexico.getInstance().getLinea());}
						if (!hayErrorLexico(token)&&comparaTokens(token.getCategoria(),EnumToken.IDENTIFICADOR)){
							String nombreParam=token.getLexema();
							//Tenemos variable entera.
							//Determinamos el modo
							tamaño+=t.dameTipo(nombreParam,nivel).getTam();
							if (((PropTiposPro)t.dameTipo(asignada,nivel)).getParametros().get(i).getModo()==Modo.VARIABLE){
							//Apilar direccion de comienzo variable
							String codigo="apilaDireccion("+(1+t.dameNivel(nombreParam,nivel))+");.";
							emitirCodigo(codigo);
							codigo="apila("+(t.obtenerInfo(nombreParam,nivel).dameDir()-1)+");.";
							emitirCodigo(codigo);
							codigo="suma;.";
							emitirCodigo(codigo);
							codigo="apilaDireccion(0);.";
							emitirCodigo(codigo);
							codigo="apila("+(2+tamaño)+");.";
							emitirCodigo(codigo);
							codigo="suma;.";
							emitirCodigo(codigo);
							codigo="flip;.";
							emitirCodigo(codigo);
							codigo="desapilaIndice;.";
							emitirCodigo(codigo);
							codigo="burbuja;.";
							emitirCodigo(codigo);
							}else//Porvalor
							{
								String codigo="apilaDireccion("+(1+t.dameNivel(nombreParam,nivel))+");.";
								emitirCodigo(codigo);
								codigo="apila("+(t.obtenerInfo(nombreParam,nivel).dameDir()-1)+");.";
								emitirCodigo(codigo);
								codigo="suma;.";
								emitirCodigo(codigo);
								codigo="apilaIndice;.";
								emitirCodigo(codigo);
								codigo="apilaDireccion(0);.";
								emitirCodigo(codigo);
								codigo="apila("+(2+tamaño)+");.";
								emitirCodigo(codigo);
								codigo="suma;.";
								emitirCodigo(codigo);
								codigo="flip;.";
								emitirCodigo(codigo);
								codigo="desapilaIndice;.";
								emitirCodigo(codigo);								
							}
							token=ALexico.getInstance().obtenerToken();
							if (!hayErrorLexico(token)&&comparaTokens(token.getCategoria(),EnumToken.CORCHETEAPER)){
								token=ALexico.getInstance().obtenerToken();
								if(compruebaTipos(reconoceExpresion(),t.dameTipo("integer",nivel))){
									if (!hayErrorLexico(token)&&compruebaTokens(token.getCategoria(),EnumToken.CORCHETECIER)){
										PropTipos param=((PropTiposPro)t.dameTipo(asignada,nivel)).getParametros().get(i).getTipo();
										PropTipos parametroEsperado=((PropTiposArray)t.dameTipo(nombreParam,nivel)).getRef();
										if(compruebaTipos(param,parametroEsperado)){
											if(((PropTiposPro)t.dameTipo(asignada,nivel)).getParametros().get(i).getModo()==Modo.VARIABLE){											
												if (t.ambitoConstante(nombreParam,nivel)){
													throw new Exception("Constante pasada por variable en línea "+ ALexico.getInstance().getLinea());												
												}																																	
											}
												token=ALexico.getInstance().obtenerToken();
												if (!hayErrorLexico(token)&&comparaTokens(token.getCategoria(),EnumToken.COMA)){
													token=ALexico.getInstance().obtenerToken();												
												}	
												i++;	
																																																		
										}
									}
								}
							}						
							else if (!hayErrorLexico(token)&&comparaTokens(token.getCategoria(),EnumToken.PUNTERO)){
								token=ALexico.getInstance().obtenerToken();
								if (!hayErrorLexico(token)&&comparaTokens(token.getCategoria(),EnumToken.CORCHETEAPER)){
									token=ALexico.getInstance().obtenerToken();
									if(compruebaTipos(reconoceExpresion(),t.dameTipo("integer",nivel))){
										if (!hayErrorLexico(token)&&compruebaTokens(token.getCategoria(),EnumToken.CORCHETECIER)){
											PropTipos param=((PropTiposPro)t.dameTipo(asignada,nivel)).getParametros().get(i).getTipo();
											PropTipos parametroEsperado=((PropTiposArray)((PropTiposPointer)t.dameTipo(nombreParam,nivel)).getRef()).getRef();
											if(compruebaTipos(param,parametroEsperado)){
												if(((PropTiposPro)t.dameTipo(asignada,nivel)).getParametros().get(i).getModo()==Modo.VARIABLE){											
													if (t.ambitoConstante(nombreParam,nivel)){
														throw new Exception("Constante pasada por variable en línea "+ ALexico.getInstance().getLinea());												
														}																																	
												}
													token=ALexico.getInstance().obtenerToken();
													if (!hayErrorLexico(token)&&comparaTokens(token.getCategoria(),EnumToken.COMA)){
														token=ALexico.getInstance().obtenerToken();												
													}	
													i++;	
																																																			
											}
										}
									}
								}
								else if (!hayErrorLexico(token)&&(comparaTokens(token.getCategoria(),EnumToken.COMA)||comparaTokens(token.getCategoria(),EnumToken.PARENTESISCIER))){
									PropTipos param=((PropTiposPro)t.dameTipo(asignada,nivel)).getParametros().get(i).getTipo();
									PropTipos parametroEsperado=((PropTiposPointer)t.dameTipo(nombreParam,nivel)).getRef();
									if(compruebaTipos(param,parametroEsperado)){
										if(((PropTiposPro)t.dameTipo(asignada,nivel)).getParametros().get(i).getModo()==Modo.VARIABLE){																															
											if(!hayErrorLexico(token)&&(comparaTokens(token.getCategoria(),EnumToken.COMA))){
												token=ALexico.getInstance().obtenerToken();
											}											
										}else{
											if (!hayErrorLexico(token)&&(comparaTokens(token.getCategoria(),EnumToken.COMA))){
												token=ALexico.getInstance().obtenerToken();											
											}											
										}
										i++;																																																		
										}
								}
								else{
									throw new Exception("Token No esperado en línea "+ ALexico.getInstance().getLinea());
									}				
							}
							else if (!hayErrorLexico(token)&&(comparaTokens(token.getCategoria(),EnumToken.PARENTESISCIER)||comparaTokens(token.getCategoria(),EnumToken.COMA))){
								
									PropTipos param=((PropTiposPro)t.dameTipo(asignada,nivel)).getParametros().get(i).getTipo();
									PropTipos parametroEsperado=t.dameTipo(nombreParam,nivel);
									if(compruebaTipos(param,parametroEsperado)){
										if(((PropTiposPro)t.dameTipo(asignada,nivel)).getParametros().get(i).getModo()==Modo.VARIABLE){											
											if (t.ambitoConstante(nombreParam,nivel)){
												throw new Exception("Constante pasada por variable en línea "+ ALexico.getInstance().getLinea());												
												}																																	
										}
										if (!hayErrorLexico(token)&&comparaTokens(token.getCategoria(),EnumToken.COMA)){
											token=ALexico.getInstance().obtenerToken();	
										}	
										i++;
									}																												
							}
							else{
								throw new Exception("Parametro no valido en línea "+ ALexico.getInstance().getLinea());
								
								}
						}
						else if (!hayErrorLexico(token)&&comparaTokens(token.getCategoria(),EnumToken.TRUE)){						
							PropTipos param=((PropTiposPro)t.dameTipo(asignada,nivel)).getParametros().get(i).getTipo();
							if(compruebaTipos(param,t.dameTipo("boolean",nivel))){
								if(((PropTiposPro)t.dameTipo(asignada,nivel)).getParametros().get(i).getModo()==Modo.VARIABLE){											
									throw new Exception("Constante pasada por variable en línea "+ ALexico.getInstance().getLinea());																																																					
									}else{
									token=ALexico.getInstance().obtenerToken();
									if (!hayErrorLexico(token)&&comparaTokens(token.getCategoria(),EnumToken.COMA)){
										token=ALexico.getInstance().obtenerToken();							
									}	
									i++;
								}																																							
							}		
						}
						else if (!hayErrorLexico(token)&&comparaTokens(token.getCategoria(),EnumToken.FALSE)){
							PropTipos param=((PropTiposPro)t.dameTipo(asignada,nivel)).getParametros().get(i).getTipo();
							if(compruebaTipos(param,t.dameTipo("boolean",nivel))){
								if(((PropTiposPro)t.dameTipo(asignada,nivel)).getParametros().get(i).getModo()==Modo.VARIABLE){											
									throw new Exception("Constante pasada por variable en línea "+ ALexico.getInstance().getLinea());																																																					
									}else{
									token=ALexico.getInstance().obtenerToken();
									if (!hayErrorLexico(token)&&comparaTokens(token.getCategoria(),EnumToken.COMA)){
										token=ALexico.getInstance().obtenerToken();							
									}	
									i++;
								}																																							
							}
						}
						else if (!hayErrorLexico(token)&&compruebaTokens(token.getCategoria(),EnumToken.NUMERO)){
							PropTipos param=((PropTiposPro)t.dameTipo(asignada,nivel)).getParametros().get(i).getTipo();
							if(compruebaTipos(param,t.dameTipo("integer",nivel))){
								if(((PropTiposPro)t.dameTipo(asignada,nivel)).getParametros().get(i).getModo()==Modo.VARIABLE){											
									throw new Exception("Constante pasada por variable en línea "+ ALexico.getInstance().getLinea());																																																					
								}else{
									token=ALexico.getInstance().obtenerToken();
									if (!hayErrorLexico(token)&&comparaTokens(token.getCategoria(),EnumToken.COMA)){
										token=ALexico.getInstance().obtenerToken();							
									}	
									i++;
								}																																							
							}
						}
						
					}
					//Aqui nosotros emitimos la llamada
					String codigo="ir-a("+t.obtenerInfo(asignada,nivel).dameDir()+");.";
					emitirCodigo(codigo);
					//fin del bucle de reconocimiento de parámetros
					if (i!=nParam){
						throw new Exception("Número de parámetros incorrecto en línea "+ ALexico.getInstance().getLinea());					
					}
					token=ALexico.getInstance().obtenerToken();
					if (comparaTokens(token.getCategoria(),EnumToken.PUNTOYCOMA)){
						reconocePuntoYComa();
					}
				}
				else if (comparaTokens(token.getCategoria(),EnumToken.PUNTOYCOMA)){
					reconocePuntoYComa();
					apilaRet(contadorInstrucciones+6);
					String codigo="ir-a("+TablaSimbolos.getInstance().obtenerInfo(asignada,nivel).dameDir()+");.";
					emitirCodigo(codigo);
				}
		}
		else if (!hayErrorLexico(token)&& (comparaTokens(token.getCategoria(),EnumToken.WRITE))) {
			reconoceEscritura();
			String codigoEmitido="write;.";
			emitirCodigo(codigoEmitido);
		}					
		else if (!hayErrorLexico(token)&& (comparaTokens(token.getCategoria(),EnumToken.READ))) {
			reconoceLectura();						
		}
		else if (!hayErrorLexico(token)&& (comparaTokens(token.getCategoria(),EnumToken.IF))) {
			token=ALexico.getInstance().obtenerToken();
			if(compruebaTipos(reconoceExpresion(),t.dameTipo("boolean",nivel))){
				String codigoEmitido="ir-f();.";
				emitirCodigo(codigoEmitido);
				int etq1=contadorInstrucciones;
				//salvar el ccpp para luego parchear				
				if (!hayErrorLexico(token)&& (compruebaTokens(token.getCategoria(),EnumToken.THEN))) {
					reconoceCuerpoBucle();
					parchea(etq1,contadorInstrucciones+1);
				}
				if (!hayErrorLexico(token)&& (comparaTokens(token.getCategoria(),EnumToken.ELSE))) {
					codigoEmitido="ir-a();.";
					emitirCodigo(codigoEmitido);
					int etq2=contadorInstrucciones;
					// salvar ccpp para luego parchear
					reconoceCuerpoBucle();
					parchea(etq2,contadorInstrucciones);
					//reconocePuntoYComa();
				
				}
				//else{reconocePuntoYComa();}
			}
		}
		else if (!hayErrorLexico(token)&& comparaTokens(token.getCategoria(),EnumToken.NEW)){
			token=ALexico.getInstance().obtenerToken();
			if (!hayErrorLexico(token)&& compruebaTokens(token.getCategoria(),EnumToken.PARENTESISAPER)){
				token=ALexico.getInstance().obtenerToken();
				if (!hayErrorLexico(token)&& compruebaTokens(token.getCategoria(),EnumToken.IDENTIFICADOR)){
					String nombreVar=token.getLexema();
					if(t.dameTipo(nombreVar,nivel).getNombreTipo()==TTipo.PUNTERO){
						token=ALexico.getInstance().obtenerToken();
						if (!hayErrorLexico(token)&& compruebaTokens(token.getCategoria(),EnumToken.PARENTESISCIER)){
							int contador=0;
							PropTiposPointer aux;
							boolean sigue=true;
							aux=(PropTiposPointer)TablaSimbolos.getInstance().dameTipo(nombreVar,nivel);
							while (sigue){
							sigue=aux.getRef().getNombreTipo()==TTipo.PUNTERO;
							contador++;
							sigue=sigue&&contador<6;
							}
							if (contador==6){
								throw new Exception("Tipos mutuamente recursivos no sportados en línea "+ ALexico.getInstance().getLinea());
								}else{
							contador=aux.getRef().getTam();	
							}
							String codigo="new("+contador+");.";
							emitirCodigo(codigo);
							//Guardar su dire
							codigo="apilaDireccion("+(1+nivel)+");.";
							emitirCodigo(codigo);
							codigo="apila("+(TablaSimbolos.getInstance().obtenerInfo(nombreVar,nivel).dameDir()-1)+");.";
							emitirCodigo(codigo);
							codigo="suma;.";
							emitirCodigo(codigo);
							codigo="flip;.";
							emitirCodigo(codigo);
							codigo="desapilaIndice;.";
							emitirCodigo(codigo);
							token=ALexico.getInstance().obtenerToken();
							reconocePuntoYComa();
						}
					}
				}			
			}
		}
		
		else if (!hayErrorLexico(token)&& comparaTokens(token.getCategoria(),EnumToken.DISPOSE)){
			token=ALexico.getInstance().obtenerToken();
			if (!hayErrorLexico(token)&& compruebaTokens(token.getCategoria(),EnumToken.PARENTESISAPER)){
				token=ALexico.getInstance().obtenerToken();
				if (!hayErrorLexico(token)&& compruebaTokens(token.getCategoria(),EnumToken.IDENTIFICADOR)){
					String nombreVar=token.getLexema();
					if(t.dameTipo(nombreVar,nivel).getNombreTipo()==TTipo.PUNTERO){
						token=ALexico.getInstance().obtenerToken();
						if (!hayErrorLexico(token)&& compruebaTokens(token.getCategoria(),EnumToken.PARENTESISCIER)){
							/////
							String codigo = "apilaDireccion("+(1+nivel)+");.";
							emitirCodigo(codigo);
							codigo="apila("+(TablaSimbolos.getInstance().obtenerInfo(nombreVar,nivel).dameDir()-1)+");.";
							emitirCodigo(codigo);
							codigo="suma;.";
							emitirCodigo(codigo);
							codigo="copia;.";
							emitirCodigo(codigo);
							codigo="apilaIndice;.";
							emitirCodigo(codigo);
							codigo="del("+TablaSimbolos.getInstance().dameTipo(nombreVar,nivel).getTam()+");.";
							emitirCodigo(codigo);
							codigo="apila(null);.";
							emitirCodigo(codigo);
							codigo="desapilaIndice;.";
							emitirCodigo(codigo);
							token=ALexico.getInstance().obtenerToken();
							reconocePuntoYComa();
						}
					}
				}			
			}
			
		}
		else if (!hayErrorLexico(token)&& (compruebaTokens(token.getCategoria(),EnumToken.WHILE))) {
			token=ALexico.getInstance().obtenerToken();
			//salvar el ccpp para luego parchear
			int etq0=contadorInstrucciones;
			if(compruebaTipos(reconoceExpresion(),t.dameTipo("boolean",nivel))){
				String codigoEmitido="ir-f();.";
				int etq1=contadorInstrucciones;
				emitirCodigo(codigoEmitido);
				
				if (!hayErrorLexico(token)&& (compruebaTokens(token.getCategoria(),EnumToken.DO))) {
					reconoceCuerpoBucle();
					
					codigoEmitido="ir-a("+(etq0)+");.";
					emitirCodigo(codigoEmitido);
					parchea(etq1+1,contadorInstrucciones);
				}			
				//parchea ir-f 
			}
		}			
	}

	private void parchea(int etq1, int contadorInstrucciones) {
		String instruccion=codigoTexto.get(etq1-1);
		//Se trata de una instruccion ir-f
		if(instruccion.contains("f")){
			instruccion="ir-f("+contadorInstrucciones+")";
			codigoTexto.remove(etq1-1);
			codigoTexto.add(etq1-1,instruccion);
		}
		//y aqui se trata ir-a
		else{
			instruccion="ir-a("+contadorInstrucciones+")";
			codigoTexto.remove(etq1-1);
			codigoTexto.add(etq1-1,instruccion);
		}		
	}

	private void reconoceCuerpoBucle()throws Exception  {
		
		token = ALexico.getInstance().obtenerToken();
		if (!hayErrorLexico(token)&&comparaTokens(token.getCategoria(), EnumToken.BEGIN)){
			if (!hayErrorLexico(token)&&!comparaTokens(token.getCategoria(), EnumToken.END)){
				token = ALexico.getInstance().obtenerToken();
				reconoceInstrucciones();				
				token = ALexico.getInstance().obtenerToken();			
				if (!hayErrorLexico(token)&&comparaTokens(token.getCategoria(), EnumToken.PUNTOYCOMA)){
					reconocePuntoYComa();
				}
			}						
		}
		else{
			reconoceInstruccion();			
		}
		
	}

	private void reconoceRestoInstrucciones() throws Exception {
		while (!hayErrorLexico(token)&&(!comparaTokens(token.getCategoria(), EnumToken.END))){
			reconoceInstruccion();
		}
	}


	private void reconocePuntoYComa() throws Exception {
		if (!hayErrorLexico(token)&& (compruebaTokens(token.getCategoria(),EnumToken.PUNTOYCOMA))) {
			while (!hayErrorLexico(token)&& (comparaTokens(token.getCategoria(),EnumToken.PUNTOYCOMA))) {
				token = ALexico.getInstance().obtenerToken();
			}
		}
	}

	private void reconoceLectura() throws Exception{
		token = ALexico.getInstance().obtenerToken();
		TablaSimbolos t=TablaSimbolos.getInstance();
		if (!hayErrorLexico(token)&&(compruebaTokens(token.getCategoria(),EnumToken.PARENTESISAPER))){
			token = ALexico.getInstance().obtenerToken();
			if (!hayErrorLexico(token)&& (comparaTokens(token.getCategoria(),EnumToken.IDENTIFICADOR))) {
				String asignada=token.getLexema();
				token=ALexico.getInstance().obtenerToken();
				if(comparaTokens(token.getCategoria(),EnumToken.PARENTESISCIER)){
					//es una variable normal, se queda como en la primera version,				
					if (IDDeclarado(asignada)&& (t.obtenerInfo(asignada,nivel).dameElemento()==TElemento.VAR)){														
						if (compruebaTiposNoExcep(t.dameTipo("integer",nivel),t.dameTipo(asignada,nivel))
								||compruebaTiposNoExcep(t.dameTipo("boolean",nivel),t.dameTipo(asignada,nivel))){ 
								//emitir codigo	
							
						}
					}
				}
				else if(comparaTokens(token.getCategoria(),EnumToken.CORCHETEAPER)){
					//es un array, el nombre del array esta en asignada
					token=ALexico.getInstance().obtenerToken();
					if(compruebaTipos(reconoceExpresion(),t.dameTipo("integer",nivel))){
						if(compruebaTokens(token.getCategoria(),EnumToken.CORCHETECIER)){
							//emitir codigo
							token=ALexico.getInstance().obtenerToken();																		
							if (IDDeclarado(asignada)&& (t.obtenerInfo(asignada,nivel).dameElemento()==TElemento.VAR)){
					
										//emitir codigo					
																																										
							}							
						}
					}
				}
				else if(comparaTokens(token.getCategoria(),EnumToken.PUNTERO)){			
					//el nombre del puntero está en asignada
					if (IDDeclarado(asignada)&& (t.obtenerInfo(asignada,nivel).dameElemento()==TElemento.VAR)){													
						token=ALexico.getInstance().obtenerToken();						
							//emitir codigo																																																											
					}
				}																															
			}
																								
		if (!hayErrorLexico(token)&& (compruebaTokens(token.getCategoria(),EnumToken.PARENTESISCIER))){
			token = ALexico.getInstance().obtenerToken();
			reconocePuntoYComa();
		}
	  }
	}
	
	 	
	private void reconoceEscritura()throws Exception {
		token = ALexico.getInstance().obtenerToken();
		if (!hayErrorLexico(token)&&(compruebaTokens(token.getCategoria(),EnumToken.PARENTESISAPER))){
			token = ALexico.getInstance().obtenerToken();
			reconoceExpresion();													
			if (!hayErrorLexico(token)&& (compruebaTokens(token.getCategoria(),EnumToken.PARENTESISCIER))){
				token = ALexico.getInstance().obtenerToken();
				reconocePuntoYComa();
			/*	String codigo="write;.";
				emitirCodigo(codigo);*/
			}
		}
	}
	
	private PropTipos reconoceExpresion()throws Exception{
		PropTipos tp=null;
		PropTipos tp2=null;
		tp=reconoceExpresionSuma();
		tp2=reconoceRestoExpresion(tp);
		return tp2;	
}

	private PropTipos reconoceRestoExpresion(PropTipos t) throws Exception{
		TablaSimbolos tabla=TablaSimbolos.getInstance();
		PropTipos tp=null;
		PropTipos tp2=null;
		
		if (!hayErrorLexico(token)&& (comparaTokens(token.getCategoria(),EnumToken.IGUAL))){			
			token=ALexico.getInstance().obtenerToken();
			tp=reconoceExpresionSuma();
			
			if (compruebaTipos(t,tp)){ 
				String codigoEmitido="igual;.";
				emitirCodigo(codigoEmitido);
				tp2=reconoceRestoExpresion(t);
				return tabla.dameTipo("boolean",nivel);
			}
		}
		else if (!hayErrorLexico(token)&& (comparaTokens(token.getCategoria(),EnumToken.MENOR))){
			token=ALexico.getInstance().obtenerToken();
			tp=reconoceExpresionSuma();
			if (compruebaTipos(t,tp)){ 
				String codigoEmitido="menor;.";
				emitirCodigo(codigoEmitido);					
				tp2=reconoceRestoExpresion(t);
				return tabla.dameTipo("boolean",nivel);
			}
		}
		else if (!hayErrorLexico(token)&& (comparaTokens(token.getCategoria(),EnumToken.MAYOR))){
			token=ALexico.getInstance().obtenerToken();
			tp=reconoceExpresionSuma();
			if (compruebaTipos(t,tp)){ 
				String codigoEmitido="mayor;.";
				emitirCodigo(codigoEmitido);					
				tp2=reconoceRestoExpresion(t);
				return tabla.dameTipo("boolean",nivel);
			}
		}
		else if (!hayErrorLexico(token)&& (comparaTokens(token.getCategoria(),EnumToken.MENORIGUAL))){
			token=ALexico.getInstance().obtenerToken();
			tp=reconoceExpresionSuma();
			if (compruebaTipos(t,tp)){ 
				String codigoEmitido="menorIgual;.";
				emitirCodigo(codigoEmitido);					
				tp2=reconoceRestoExpresion(t);
				return tabla.dameTipo("boolean",nivel);
			}
		}
		else if (!hayErrorLexico(token)&& (comparaTokens(token.getCategoria(),EnumToken.MAYORIGUAL))){
			token=ALexico.getInstance().obtenerToken();
			tp=reconoceExpresionSuma();
			if (compruebaTipos(t,tp)){ 
				String codigoEmitido="mayorIgual;.";
				emitirCodigo(codigoEmitido);					
				tp2=reconoceRestoExpresion(t);
				return tabla.dameTipo("boolean",nivel);
			}
		}
		else if (!hayErrorLexico(token)&& (comparaTokens(token.getCategoria(),EnumToken.DISTINTO))){
			token=ALexico.getInstance().obtenerToken();
			tp=reconoceExpresionSuma();
			if (compruebaTipos(t,tp)){ 
				String codigoEmitido="distinto;.";
				emitirCodigo(codigoEmitido);					
				tp2=reconoceRestoExpresion(t);
				return tabla.dameTipo("boolean",nivel);
			}
		}
		return t;
	}

	private PropTipos reconoceExpresionSuma()  throws Exception{
		PropTipos tp=null;
		PropTipos tp2=null;
	
		tp=reconoceExpresionProd();
		tp2=reconoceRestoExpresionSum(tp);	
		return tp2;
	}
	
	private PropTipos reconoceRestoExpresionSum(PropTipos t) throws Exception{
		PropTipos tp=null;
		PropTipos tp2=null;
		TablaSimbolos tabla=TablaSimbolos.getInstance();
		if (!hayErrorLexico(token)&& (comparaTokens(token.getCategoria(),EnumToken.SUMA))){
			token=ALexico.getInstance().obtenerToken();
			tp=reconoceExpresionProd();
			
			if (compruebaTipos(t,tp)){
				String codigoEmitido="suma;.";
				emitirCodigo(codigoEmitido);					
				tp2=reconoceRestoExpresionSum(tp);
				return tabla.dameTipo("integer",nivel);				
			}
		}
		else if (!hayErrorLexico(token)&& (comparaTokens(token.getCategoria(),EnumToken.RESTA))){
			token=ALexico.getInstance().obtenerToken();
			tp=reconoceExpresionProd();
			if (compruebaTipos(t,tp)){
				String codigoEmitido="resta;.";
				emitirCodigo(codigoEmitido);					
				tp2=reconoceRestoExpresionSum(tp);
				return tabla.dameTipo("integer",nivel);		
			}
			
		}
		else if (!hayErrorLexico(token)&& (comparaTokens(token.getCategoria(),EnumToken.OR))){
			token=ALexico.getInstance().obtenerToken();
			tp=reconoceExpresionProd();
			if (compruebaTipos(t,tp)){
				String codigoEmitido="or;.";
				emitirCodigo(codigoEmitido);					
				tp2=reconoceRestoExpresionSum(tp);
				return tabla.dameTipo("boolean",nivel);			
			}
		}
		return t;			
	}

	private PropTipos reconoceExpresionProd() throws Exception{
		PropTipos tp=null;
		PropTipos tp2=null;
		tp=reconoceExpresionNeg();
		tp2=reconoceRestoExpresionProd(tp);
		
		return tp2;				
	}
	
	
	private PropTipos reconoceRestoExpresionProd(PropTipos t) throws Exception{
		PropTipos tp=null;
		PropTipos tp2=null;
		TablaSimbolos tabla=TablaSimbolos.getInstance();
		if (!hayErrorLexico(token)&& (comparaTokens(token.getCategoria(),EnumToken.PRODUCTO))){
			token=ALexico.getInstance().obtenerToken();
			tp=reconoceExpresionNeg();
			if (compruebaTipos(t,tp)){
				String codigoEmitido="producto;.";
				emitirCodigo(codigoEmitido);					
				tp2=reconoceRestoExpresionProd(tp);
				return tabla.dameTipo("integer",nivel);			
			}
			
		}		
		else if (!hayErrorLexico(token)&& (comparaTokens(token.getCategoria(),EnumToken.DIVISION))){
			token=ALexico.getInstance().obtenerToken();
			tp=reconoceExpresionNeg();
			if (compruebaTipos(t,tp)){
				String codigoEmitido="division;.";
				emitirCodigo(codigoEmitido);					
				tp2=reconoceRestoExpresionProd(tp);
				return tabla.dameTipo("integer",nivel);			
			}
		}
		else if (!hayErrorLexico(token)&& (comparaTokens(token.getCategoria(),EnumToken.AND))){
			token=ALexico.getInstance().obtenerToken();
			tp=reconoceExpresionNeg();
			if (compruebaTipos(t,tp)){
				String codigoEmitido="and;.";
				emitirCodigo(codigoEmitido);					
				tp2=reconoceRestoExpresionProd(tp);
				return tabla.dameTipo("boolean",nivel);			
			}
		}
		else if (!hayErrorLexico(token)&& (comparaTokens(token.getCategoria(),EnumToken.MODULO))){
			token=ALexico.getInstance().obtenerToken();
			tp=reconoceExpresionNeg();
			if (compruebaTipos(t,tp)){
				String codigoEmitido="moduloEntero;.";
				emitirCodigo(codigoEmitido);					
				tp2=reconoceRestoExpresionProd(tp);
				return tabla.dameTipo("integer",nivel);			
			}
		}
		
		return t;		
	}
	
	
	private PropTipos reconoceExpresionNeg() throws Exception{
		PropTipos tp;
		TablaSimbolos tabla=TablaSimbolos.getInstance();
		if (!hayErrorLexico(token)&& (comparaTokens(token.getCategoria(),EnumToken.NOT))){
			token=ALexico.getInstance().obtenerToken();
			tp=reconoceExpresion();
			if (compruebaTipos(tp,tabla.dameTipo("boolean",nivel))){
				String codigoEmitido="negacion;.";
				emitirCodigo(codigoEmitido);					
			return tp;				
			}			
		}
		else if (!hayErrorLexico(token)&& (comparaTokens(token.getCategoria(),EnumToken.PARENTESISAPER))){
			token=ALexico.getInstance().obtenerToken();
			tp=reconoceExpresion();
			compruebaTokens(token.getCategoria(),EnumToken.PARENTESISCIER);
			token=ALexico.getInstance().obtenerToken();
			return tp;
		} 		
		else if (!hayErrorLexico(token)&& comparaTokens(token.getCategoria(),EnumToken.NUMERO)){
			String codigoEmitido="apila("+token.getLexema()+");.";
			emitirCodigo(codigoEmitido);
			token=ALexico.getInstance().obtenerToken();
			return tabla.dameTipo("integer",nivel);			
		}
		else if (!hayErrorLexico(token)&& comparaTokens(token.getCategoria(),EnumToken.NIL)){
			//emitir codigo
			token=ALexico.getInstance().obtenerToken();
			return new PropTipos(TTipo.VACIO,0);			
		}
		else if (!hayErrorLexico(token)&& comparaTokens(token.getCategoria(),EnumToken.TRUE)){
			String codigoEmitido="apila(TRUE);.";
			emitirCodigo(codigoEmitido);
			token=ALexico.getInstance().obtenerToken();
			return tabla.dameTipo("boolean",nivel);			
		}
		else if (!hayErrorLexico(token)&& comparaTokens(token.getCategoria(),EnumToken.FALSE)){
			String codigoEmitido="apila(FALSE);.";
			emitirCodigo(codigoEmitido);
			token=ALexico.getInstance().obtenerToken();
			return tabla.dameTipo("boolean",nivel);			
		}
		
		if (!hayErrorLexico(token)&& (compruebaTokens(token.getCategoria(),EnumToken.IDENTIFICADOR))){
			String asignado=token.getLexema();
			if (tabla.existeConstante(asignado,nivel)||tabla.existeVariable(asignado,nivel)){

				token = ALexico.getInstance().obtenerToken();
			
				//al avanzar, el token se queda apuntando a lo siguiente al nombre, que será, o un puntero, o un array, o el parentesis de cierre
				if (!hayErrorLexico(token)&&(comparaTokens(token.getCategoria(),EnumToken.CORCHETEAPER))){
					
					token=ALexico.getInstance().obtenerToken();
					if(compruebaTipos(reconoceExpresion(),tabla.dameTipo("integer",nivel))){
						if(compruebaTokens(token.getCategoria(),EnumToken.CORCHETECIER)){
							String  codigo="apilaDireccion("+(1+(tabla.dameNivel(asignado,nivel)))+");.";
							emitirCodigo(codigo);
							codigo="apila("+(tabla.obtenerInfo(asignado,nivel).dameDir()-1)+");.";
							emitirCodigo(codigo);
							codigo="suma;.";
							emitirCodigo(codigo);
							codigo="suma;.";
							emitirCodigo(codigo);
							codigo="apilaIndice;.";
							emitirCodigo(codigo);
							token=ALexico.getInstance().obtenerToken();
							return ((PropTiposArray) tabla.dameTipo(asignado,nivel)).getRef();
						}
					}
				}
				else if (!hayErrorLexico(token)&& comparaTokens(token.getCategoria(),EnumToken.PUNTERO)){
					//es un puntero
					token=ALexico.getInstance().obtenerToken();
					if (!hayErrorLexico(token)&&(comparaTokens(token.getCategoria(),EnumToken.CORCHETEAPER))){						
						token=ALexico.getInstance().obtenerToken();
						if(compruebaTipos(reconoceExpresion(),tabla.dameTipo("integer",nivel))){
							if(compruebaTokens(token.getCategoria(),EnumToken.CORCHETECIER)){
								//emitir codigo
								token=ALexico.getInstance().obtenerToken();
								return ((PropTiposArray)((PropTiposPointer) tabla.dameTipo(asignado,nivel)).getRef()).getRef();
							}
						}
					}
					else{
					String codigo="apilaDireccion("+(1+(tabla.dameNivel(asignado,nivel)))+");.";
					emitirCodigo(codigo);
					codigo="apila("+(tabla.obtenerInfo(asignado,nivel).dameDir()-1)+");.";
					emitirCodigo(codigo);
					codigo="suma;.";
					emitirCodigo(codigo);	
					codigo="apilaIndice;.";
					emitirCodigo(codigo);
					codigo="apilaIndice;.";
					emitirCodigo(codigo);	
					return ((PropTiposPointer) tabla.dameTipo(asignado,nivel)).getRef();
					}	
				}
			}
			
			//Escritura
			String codigo="apilaDireccion("+(1+(tabla.dameNivel(asignado,nivel)))+");.";
			emitirCodigo(codigo);
			codigo="apila("+(tabla.obtenerInfo(asignado,nivel).dameDir()-1)+");.";
			emitirCodigo(codigo);
			codigo="suma;.";
			emitirCodigo(codigo);
			codigo="apilaIndice;.";
			emitirCodigo(codigo);
			if (nivel>0){
				PropTiposPro aux=((PropTiposPro)tabla.dameTipo(nombreAmbito,nivel));
				String aux2="";
				for (int i=0;i<aux.getParametros().size();i++){
					aux2=aux.getParametros().get(i).getNombre();
					if ((aux2.equals(asignado))&&(aux.getParametros().get(i).getModo()==Modo.VARIABLE)){
						codigo="apilaIndice;.";
						emitirCodigo(codigo);	
					}
				}
			}
			return tabla.dameTipo(asignado,nivel);
		}
		else if (!hayErrorLexico(token)&& comparaTokens(token.getCategoria(),EnumToken.SUMA)){
			token=ALexico.getInstance().obtenerToken();	
			if (compruebaTokens(token.getCategoria(),EnumToken.NUMERO)){									
				String codigoEmitido="apila("+"-"+token.getLexema()+");.";
				emitirCodigo(codigoEmitido);
				token=ALexico.getInstance().obtenerToken();
				return tabla.dameTipo("integer",nivel);
			}
		}
		else if (!hayErrorLexico(token)&& compruebaTokens(token.getCategoria(),EnumToken.RESTA)){
			token=ALexico.getInstance().obtenerToken();
			if (compruebaTokens(token.getCategoria(),EnumToken.NUMERO)){								
				String codigoEmitido="apila("+"-"+token.getLexema()+");.";
				emitirCodigo(codigoEmitido);
				token=ALexico.getInstance().obtenerToken();				
				return tabla.dameTipo("integer",nivel);
			}
		}		
		
		return null;
	}
			
	
	private PropTipos reconoceAsignacion()throws Exception{

	PropTipos devolver=null;
	if (!hayErrorLexico(token)&&(compruebaTokens(token.getCategoria(),EnumToken.ASIGNACION))){
		token=ALexico.getInstance().obtenerToken();
		devolver= reconoceExpresion();
	}
	return devolver;
}	


	private void reconoceIdentificadorCabecera() throws Exception {
		token = ALexico.getInstance().obtenerToken();
		if (!hayErrorLexico(token)
				&& (compruebaTokens(token.getCategoria(),
						EnumToken.IDENTIFICADOR))) {
			if (!existeID(token.getLexema())) {
				//Ver que se hace aqui con el indentiicador del programa
				//ya que ahora no tenemos el TError
				nombreAmbito=token.getLexema();
				TablaSimbolos ts = TablaSimbolos.getInstance();
				ts.añadeIdenProgram(token.getLexema(),0,null);//A la espera del proptiposProc			
				direccion++;
			} else {
				hayError = true;
			}
		}
	}

	
	private boolean compruebaTokens(EnumToken tokenRecibido,
			EnumToken tokenEsperado) throws Exception {
		if (tokenRecibido != tokenEsperado) {
			String mensajeError = "";
			String cadenaToken = cadenaToken(tokenEsperado);
			mensajeError = mensajeError + "Se esperaba '" + cadenaToken;
			mensajeError = "Error Sintactico: Línea "
					+ ALexico.getInstance().getLinea() + ": " + mensajeError
					+ "'";
			hayError = true;
			throw new Exception(mensajeError);
		}
		return true;
	}
		
	private boolean comparaTokens(EnumToken tokenRecibido,
			EnumToken tokenEsperado) {
		return (tokenRecibido == tokenEsperado);
	}

	private String cadenaToken(EnumToken tokenEsperado) {
		switch (tokenEsperado) {
		case PROGRAM:
			return "PROGRAM";
		case BEGIN:
			return "BEGIN";
		case END:
			return "END";
		case VAR:
			return "VAR";
		case CONST:
			return "CONST";
		case INTEGER:
			return "TIPO";
		case BOOLEAN:
			return "TIPO";
		case READ:
			return "READ";
		case WRITE:
			return "WRITE";
		case TRUE:
			return "TRUE";
		case FALSE:
			return "FALSE";
		case NOT:
			return "NOT";
		case PARENTESISAPER:
			return "(";
		case PARENTESISCIER:
			return ")";
		case PUNTO:
			return ".";
		case COMA:
			return ";";
		case PUNTOYCOMA:
			return ";";
		case DOSPUNTOS:
			return ":";
		case ASIGNACION:
			return ":=";
		case SUMA:
			return "+";
		case RESTA:
			return "-";
		case OR:
			return "OR";
		case PRODUCTO:
			return "*";
		case DIVISION:
			return "/";
		case AND:
			return "AND";		
		case MODULO:
			return "MOD";
		case IGUAL:
			return "=";
		case DISTINTO:
			return "<>";
		case MAYOR:
			return ">";
		case MENOR:
			return "<";
		case MAYORIGUAL:
			return ">=";
		case MENORIGUAL:
			return "<=";
		case IDENTIFICADOR:
			return "Identificador";
		case NUMERO:
			return "Valor";
		default:
			return "";
		}
	}

	private boolean existeIDNivel(String t) throws Exception {
		TablaSimbolos ts = TablaSimbolos.getInstance();
		if (ts.existeTipoNivel(t,nivel)||ts.existeVariableNivel(t,nivel)||ts.existeConstanteNivel(t,nivel)||ts.existeProcedimientoNivel(t,nivel)) {
			String mensajeError = "Identificador '" + t + "' repetido";
			mensajeError = "Error Sintactico: Línea "
					+ ALexico.getInstance().getLinea() + ": " + mensajeError
					+ ".";
			throw new Exception(mensajeError);
		}
		return false;
	}
	

	private boolean existeID(String t) throws Exception {
		TablaSimbolos ts = TablaSimbolos.getInstance();
		if (ts.existeTipo(t,nivel)||ts.existeVariable(t,nivel)||ts.existeConstante(t,nivel)||ts.existeProcedimiento(t,nivel)) {
			String mensajeError = "Identificador '" + t + "' repetido";
			mensajeError = "Error Sintactico: Línea "
					+ ALexico.getInstance().getLinea() + ": " + mensajeError
					+ ".";
			throw new Exception(mensajeError);
		}
		return false;
	}
	
	private boolean IDDeclarado(String t) throws Exception {				
		
		TablaSimbolos ts = TablaSimbolos.getInstance();
		if (!ts.existeVariable(t,nivel)) {
			String mensajeError = "El identificador '" + t + "' no ha sido declarado";
			mensajeError = "Error Sintactico: Línea "
					+ ALexico.getInstance().getLinea() + ": " + mensajeError
					+ ".";
			throw new Exception(mensajeError);
		}
		return true;
	}
	
	private boolean existeTipo(String nombre) throws Exception{
		TablaSimbolos ts = TablaSimbolos.getInstance();
		if (!ts.existeTipo(nombre,nivel)) {
			String mensajeError = "El tipo '" + nombre + "' no ha sido declarado";
			mensajeError = "Error Sintactico: Línea "
					+ ALexico.getInstance().getLinea() + ": " + mensajeError
					+ ".";
			throw new Exception(mensajeError);
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

///////////////////////////////////////////////////////////////////////////////////////////
	
	
	/**private TInfo devRef(PropTipos exp,TablaSimbolos ts) throws Exception{
		
		
		// si es un tipo construido
		if (exp.getNombreTipo() == TTipo.CONSTRUIDO)  {
			
			// y si existe ese tipo en la tabla de símbolos
			if (ts.existeTipo(exp.getProp().getNombreTipo()),nivel)  {
				
				// llama recursivamente para el tipo sobre el que se ha construido el tipo construido
				return devRef(ts.obtenerInfo(exp.getProp().getNombreTipo()));
				
				// sino está en la tabla devuelve error
			} else return null;
				
			
			// si no es un tipo construido, es porque ya estamos en un tipo básico
		} else return exp;
	}**/
	
	///////////////////////////////////////////////////////////////////////////////////////////
	private boolean compatibleTipos2(PropTipos e1, PropTipos e2,ArrayList<ParejaTipos> visitadas) throws Exception{
		
		TablaSimbolos ts = TablaSimbolos.getInstance();
		//Creamos la pareja de los tipos para insertarla o no luego 
		ParejaTipos par = new ParejaTipos(e1,e2);
		
		//Si esa pareja ya se ha visitado... devolvemos true
		if (visitadas.contains(par)) {return true;}
		
		//sino, la añadimos 
		else visitadas.add(par);
			
		//Si los dos tipos de la pareja son tipos básicos devolvemos cierto
		
		//si son tipos basicos...devuelve true
		if ((e1.getNombreTipo()==TTipo.INTEGER && 
			 e2.getNombreTipo()==TTipo.INTEGER )
			 
			 ||
			 
			 (e1.getNombreTipo()==TTipo.BOOLEAN && 
			  e2.getNombreTipo()==TTipo.BOOLEAN )
			 
			 
			  ||
				 
				 (e1.getNombreTipo()==TTipo.VACIO && 
				  e2.getNombreTipo()==TTipo.VACIO )
				 
				 ) {return true;}
		
		// Si no son tipos simples, hay que ir a por sus referencias
		
		// Si el tipo referido es el primero, e1...
		else if (e1.getNombreTipo()==TTipo.CONSTRUIDO) return compatibleTipos2(((PropTiposNombrado)e1).getRef(),e2,visitadas);
		
		// Si el tipo referido es el segundo, e2...
		else if (e2.getNombreTipo()==TTipo.CONSTRUIDO) return compatibleTipos2(e1,((PropTiposNombrado)e2).getRef(),visitadas);
		
		
		// Si los dos tipos son arrays ...
		else if (((e1.getNombreTipo()==TTipo.ARRAY && 
				  e2.getNombreTipo()==TTipo.ARRAY ))
			
			    &&
			    //y tienen el mismo numero de elementos
			    ((PropTiposArray)e1).getTam() == ((PropTiposArray)e2).getTam())
			    
			    
		
		return compatibleTipos2(((PropTiposArray)e1).getRef(),((PropTiposArray)e2).getRef(),visitadas);
		
		
		
		
		//Si son punteros los dos....
		else if ((e1.getNombreTipo()==TTipo.PUNTERO && 
				  e2.getNombreTipo()==TTipo.PUNTERO ))
			
			return compatibleTipos2(((PropTiposPointer)e1).getRef(),((PropTiposPointer)e2).getRef(),visitadas);
		
		else return false;
		
		
		
		
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////
	private boolean compruebaTipos(PropTipos e1, PropTipos e2) throws Exception{
		boolean compatible=false;
		ArrayList<ParejaTipos> visitadas= new ArrayList<ParejaTipos>();
		compatible =compatibleTipos2(e1,e2,visitadas);
		if (compatible) return true;
		else{
			throw new Exception("Tipos no compatibles en línea "+ ALexico.getInstance().getLinea());
			}
	}
	
	private boolean compruebaTiposNoExcep(PropTipos e1, PropTipos e2){
		boolean compatible=false;
		ArrayList<ParejaTipos> visitadas= new ArrayList<ParejaTipos>();
		try {
			compatible =compatibleTipos2(e1,e2,visitadas);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (compatible) return true;
		else{
			return false;
		}
	}
	
	private void prologo(int nivel,int tamLocales){
		String codigo="apilaDireccion(0);.";
		emitirCodigo(codigo);
		codigo="apila(2);.";
		emitirCodigo(codigo);
		codigo="suma;.";
		emitirCodigo(codigo);
		codigo="apilaDireccion("+(nivel+1)+");.";
		emitirCodigo(codigo);
		codigo="desapilaIndice;.";
		emitirCodigo(codigo);
		codigo="apilaDireccion(0);.";
		emitirCodigo(codigo);
		codigo="apila(3);.";
		emitirCodigo(codigo);
		codigo="suma;.";
		emitirCodigo(codigo);
		codigo="desapilaDireccion("+(nivel+1)+");.";
		emitirCodigo(codigo);
		codigo="apilaDireccion(0);.";
		emitirCodigo(codigo);
		codigo="apila("+(2+tamLocales)+");.";
		emitirCodigo(codigo);
		codigo="suma;.";
		emitirCodigo(codigo);
		codigo="desapilaDireccion(0);.";
		emitirCodigo(codigo);
	}
	
	private void epilogo(int nivel){
		String codigo="apilaDireccion("+(nivel+1)+");.";
		emitirCodigo(codigo);
		codigo="apila(2);.";
		emitirCodigo(codigo);
		codigo="resta;.";
		emitirCodigo(codigo);
		codigo="apilaIndice;.";
		emitirCodigo(codigo);
		codigo="apilaDireccion("+(nivel+1)+");.";
		emitirCodigo(codigo);
		codigo="apila(3);.";
		emitirCodigo(codigo);
		codigo="resta;.";
		emitirCodigo(codigo);
		codigo="copia;.";
		emitirCodigo(codigo);
		codigo="desapilaDireccion(0);.";
		emitirCodigo(codigo);
		codigo="apila(2);.";
		emitirCodigo(codigo);
		codigo="suma;.";
		emitirCodigo(codigo);
		codigo="apilaIndice;.";
		emitirCodigo(codigo);
		codigo="desapilaDireccion("+(nivel+1)+");.";
		emitirCodigo(codigo);
	}
	private void apilaRet(int ret){
		String codigo="apilaDireccion(0);.";
		emitirCodigo(codigo);
		codigo="apila(1);.";
		emitirCodigo(codigo);
		codigo="suma;.";
		emitirCodigo(codigo);
		codigo="apila("+ret+");.";
		emitirCodigo(codigo);
		codigo="desapilaIndice;.";
		emitirCodigo(codigo);
		}
	
	@Override
	public void setCodigo(PrintWriter codigo) {
		this.ficheroSalida=codigo;
		
	}
}