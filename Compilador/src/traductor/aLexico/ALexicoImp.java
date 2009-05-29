package traductor.aLexico;

import java.io.FileReader;
import java.io.IOException;

import excepciones.CompiladorException;
import excepciones.LexicException;

//

/**
 * Implementacion del analizador lexico.
 * 
 */
public class ALexicoImp extends ALexico {

	private char buffer;
	private int lineaActual;
	private Token token;
	private static FileReader ficheroFuente;
	private MaquinaEstados estado;

	/**
	 * Constructoras del analizador
	 */
	public ALexicoImp() {
		buffer = ' ';
		lineaActual = 1;
		estado = MaquinaEstados.EINICIAL;
		token = new Token();
	}

	public ALexicoImp(FileReader f) {
		ficheroFuente = f;
		buffer = ' ';
		lineaActual = 1;
		estado = MaquinaEstados.EINICIAL;
		token = new Token();
	}

	public int getLinea() {
		return lineaActual;
	}

	public FileReader getFichero() {
		return ficheroFuente;
	}

	public void setFichero(FileReader f) {
		buffer = ' ';
		lineaActual = 1;
		ficheroFuente = f;
	}

	/**
	 * Metodo que convierte en minusculas cada letra mayuscula
	 */
	private void pasaAMinusculas() {
		if (buffer >= 'A' && buffer <= 'Z')
			buffer = Character.toLowerCase(buffer);
	}

	private void transita(MaquinaEstados e) throws IOException {
		estado = e;
		buffer = (char) ficheroFuente.read();
		pasaAMinusculas();
	}

	/**
	 * lee las palabras de una linea del programa, las introduce en el buffer
	 * 
	 * @param s
	 * @return
	 * @throws IOException
	 */
	private String leePalabra(String s) throws IOException {
		pasaAMinusculas();
		while ((buffer >= 'a' && buffer <= 'z') || (buffer >= '0' && buffer <= '9')) {
			s += "" + buffer;
			buffer = (char) ficheroFuente.read();
			pasaAMinusculas();
		}
		return s;
	}

	private String leeNumero(String s) throws IOException {
		pasaAMinusculas();
		while (buffer >= '0' && buffer <= '9') {
			s += "" + buffer;
			buffer = (char) ficheroFuente.read();
			pasaAMinusculas();
		}
		return s;
	}

	/**
	 * Evalua los String de entrada para completar la informacion del token
	 * 
	 * @param s
	 */
	private void evaluaPalabra(String s) {
		if (s.equals("program"))
			rellenaInfoToken(EnumToken.PROGRAM, s, null, MaquinaEstados.EINICIAL);
		else if (s.equals("begin"))
			rellenaInfoToken(EnumToken.INICIO, s, null, MaquinaEstados.EINICIAL);
		else if (s.equals("end"))
			rellenaInfoToken(EnumToken.FIN, s, null, MaquinaEstados.EINICIAL);
		else if (s.equals("var"))
			rellenaInfoToken(EnumToken.TVAR, s, null, MaquinaEstados.EINICIAL);
		else if (s.equals("read"))
			rellenaInfoToken(EnumToken.LEER, s, null, MaquinaEstados.EINICIAL);
		else if (s.equals("write"))
			rellenaInfoToken(EnumToken.ESCRIBIR, s, null, MaquinaEstados.EINICIAL);
		else if (s.equals("integer"))
			rellenaInfoToken(EnumToken.TIPENT, s, null, MaquinaEstados.EINICIAL);
		else if (s.equals("boolean"))
			rellenaInfoToken(EnumToken.TIPBOOL, s, null, MaquinaEstados.EINICIAL);
		else if (s.equals("true"))
			rellenaInfoToken(EnumToken.TRUE, s, s, MaquinaEstados.EINICIAL);
		else if (s.equals("false"))
			rellenaInfoToken(EnumToken.FALSE, s, s, MaquinaEstados.EINICIAL);
		else if (s.equals("and"))
			rellenaInfoToken(EnumToken.AND, s, null, MaquinaEstados.EINICIAL);
		else if (s.equals("mod"))
			rellenaInfoToken(EnumToken.MODULO, s, null, MaquinaEstados.EINICIAL);
		else if (s.equals("or"))
			rellenaInfoToken(EnumToken.OR, s, null, MaquinaEstados.EINICIAL);
		else if (s.equals("not"))
			rellenaInfoToken(EnumToken.NOT, s, null, MaquinaEstados.EINICIAL);
		else if (s.equals("if"))
			rellenaInfoToken(EnumToken.SI, s, null, MaquinaEstados.EINICIAL);
		else if (s.equals("then"))
			rellenaInfoToken(EnumToken.ENTONCES, s, null, MaquinaEstados.EINICIAL);
		else if (s.equals("else"))
			rellenaInfoToken(EnumToken.SINO, s, null, MaquinaEstados.EINICIAL);
		else if (s.equals("while"))
			rellenaInfoToken(EnumToken.MIENTRAS, s, null, MaquinaEstados.EINICIAL);
		else if (s.equals("do"))
			rellenaInfoToken(EnumToken.DO, s, null, MaquinaEstados.EINICIAL);
		else if (s.equals("array"))
			rellenaInfoToken(EnumToken.TARRAY, s, null, MaquinaEstados.EINICIAL);
		else if (s.equals("of"))
			rellenaInfoToken(EnumToken.OF, s, null, MaquinaEstados.EINICIAL);
		else if (s.equals("type"))
			rellenaInfoToken(EnumToken.TIPO, s, null, MaquinaEstados.EINICIAL);
		else if (s.equals("procedure"))
			rellenaInfoToken(EnumToken.PROC, s, null, MaquinaEstados.EINICIAL);
		else if (s.equals("new"))
			rellenaInfoToken(EnumToken.NUEVO, s, null, MaquinaEstados.EINICIAL);
		else if (s.equals("dispose"))
			rellenaInfoToken(EnumToken.LIBERAR, s, null, MaquinaEstados.EINICIAL);
		else if (s.equals("nil"))
			rellenaInfoToken(EnumToken.NIL, s, null, MaquinaEstados.EINICIAL);
		else
			rellenaInfoToken(EnumToken.ID, s, null, MaquinaEstados.EINICIAL);
	}

	/**
	 * Completa las caracteristicas del objeto token
	 * 
	 * @param tok
	 * @param s
	 * @param valor
	 * @param m
	 */
	private void rellenaInfoToken(EnumToken tok, String s, Object valor, MaquinaEstados m) {
		token.setCategoria(tok);
		token.setLexema(s);
		estado = m;
	}

	/*
	 * Procesa el siguiente token
	 */
	public Token obtenerToken() throws CompiladorException {
		String lexema = "";
		try {
			boolean finFichero = Character.isDefined(buffer);
			while (finFichero) {
				finFichero = Character.isDefined(buffer);
				switch (estado) {
					case EINICIAL :
						switch (buffer) {
							case '\n' :
								lineaActual++;
								transita(MaquinaEstados.EINICIAL);
								break;
							case ' ' :
								transita(MaquinaEstados.EINICIAL);
								break;
							case '\r' :
								transita(MaquinaEstados.EINICIAL);
								break;
							case '\t' :
								transita(MaquinaEstados.EINICIAL);
								break;
							case '0' :
								lexema += buffer;
								transita(MaquinaEstados.ENUMCERO);
								break;
							case '1' :
							case '2' :
							case '3' :
							case '4' :
							case '5' :
							case '6' :
							case '7' :
							case '8' :
							case '9' :
								lexema += buffer;
								transita(MaquinaEstados.ENUMERO);
								break;
							case 'a' :
							case 'b' :
							case 'c' :
							case 'd' :
							case 'e' :
							case 'f' :
							case 'g' :
							case 'h' :
							case 'i' :
							case 'j' :
							case 'k' :
							case 'l' :
							case 'm' :
							case 'n' :
							case 'o' :
							case 'p' :
							case 'q' :
							case 'r' :
							case 's' :
							case 't' :
							case 'u' :
							case 'v' :
							case 'w' :
							case 'x' :
							case 'y' :
							case 'z' :
								lexema += buffer;
								transita(MaquinaEstados.EIDENOPALRES);
								break;
							case ',' :
								lexema += buffer;
								transita(MaquinaEstados.ECOMA);
								break;
							case '.' :
								lexema += buffer;
								transita(MaquinaEstados.EPUNTO);
								break;
							case ';' :
								lexema += buffer;
								transita(MaquinaEstados.EPUNTOYCOMA);
								break;
							case ':' :
								lexema += buffer;
								transita(MaquinaEstados.EDOSPUNTOS);
								break;
							case '(' :
								lexema += buffer;
								transita(MaquinaEstados.EPARENTESISAPER);
								break;
							case ')' :
								lexema += buffer;
								transita(MaquinaEstados.EPARENTESISCIER);
								break;
							case '[' :
								lexema += buffer;
								transita(MaquinaEstados.ECORCHETEAPER);
								break;
							case ']' :
								lexema += buffer;
								transita(MaquinaEstados.ECORCHETECIER);
								break;
							case '+' :
							case '-' :
							case '*' :
							case '/' :
								lexema += buffer;
								transita(MaquinaEstados.ESUMORESOMULODIV);
								break;
							case '=' :
								lexema += buffer;
								transita(MaquinaEstados.EIGUAL);
								break;
							case '<' :
								lexema += buffer;
								transita(MaquinaEstados.EMENOR);
								break;
							case '>' :
								lexema += buffer;
								transita(MaquinaEstados.EMAYOR);
								break;
							case '^' :
								lexema += buffer;
								transita(MaquinaEstados.EPUNTERO);
								break;
							default :
								if (ficheroFuente.ready()) {
									throw new LexicException(" '" + buffer + "'" + ".", lineaActual);
								}
						}
						break;
					case ENUMCERO :
						lexema = leeNumero(lexema);
						if (!((buffer >= 'a' && buffer <= 'z') || (buffer >= 'A' && buffer <= 'Z'))) {
							rellenaInfoToken(EnumToken.DIGITO, lexema, Integer.parseInt(lexema), MaquinaEstados.EINICIAL);
							return token;
						} else {
							throw new LexicException(" '" + lexema + buffer + "'" + ".", lineaActual);
						}
					case ENUMERO :
						lexema = leeNumero(lexema);
						if (!(buffer >= 'a' && buffer <= 'z')) {
							rellenaInfoToken(EnumToken.DIGITO, lexema, Integer.parseInt(lexema), MaquinaEstados.EINICIAL);
							return token;
						} else {
							throw new LexicException(" '" + lexema + buffer + "'" + ".", lineaActual);
						}
					case EIDENOPALRES :
						lexema = leePalabra(lexema);
						evaluaPalabra(lexema);
						return token;
					case ECOMA :
						rellenaInfoToken(EnumToken.COMA, lexema, null, MaquinaEstados.EINICIAL);
						return token;
					case EPUNTO :
						rellenaInfoToken(EnumToken.PUNTO, lexema, null, MaquinaEstados.EINICIAL);
						return token;
					case EPUNTOYCOMA :
						rellenaInfoToken(EnumToken.PYCOMA, lexema, null, MaquinaEstados.EINICIAL);
						return token;
					case EPARENTESISAPER :
						rellenaInfoToken(EnumToken.PA, lexema, null, MaquinaEstados.EINICIAL);
						return token;
					case EPARENTESISCIER :
						rellenaInfoToken(EnumToken.PC, lexema, null, MaquinaEstados.EINICIAL);
						return token;
					case ECORCHETEAPER :
						rellenaInfoToken(EnumToken.CA, lexema, null, MaquinaEstados.EINICIAL);
						return token;
					case ECORCHETECIER :
						rellenaInfoToken(EnumToken.CC, lexema, null, MaquinaEstados.EINICIAL);
						return token;
					case EPUNTERO :
						rellenaInfoToken(EnumToken.TPUNTERO, lexema, null, MaquinaEstados.EINICIAL);
						return token;
					case EDOSPUNTOS :
						if (buffer == '=') {
							lexema += buffer;
							transita(MaquinaEstados.EASIGNACION);
						} else {
							rellenaInfoToken(EnumToken.DOSPUNTOS, lexema, null, MaquinaEstados.EINICIAL);
							return token;
						}
						break;
					case EASIGNACION :
						rellenaInfoToken(EnumToken.ASIG, lexema, null, MaquinaEstados.EINICIAL);
						return token;
					case ESUMORESOMULODIV :
						if (lexema.equals("+")) {
							rellenaInfoToken(EnumToken.SUMA, lexema, null, MaquinaEstados.EINICIAL);
							return token;
						} else if (lexema.equals("-")) {
							rellenaInfoToken(EnumToken.RESTA, lexema, null, MaquinaEstados.EINICIAL);
							return token;
						} else if (lexema.equals("*")) {
							rellenaInfoToken(EnumToken.MUL, lexema, null, MaquinaEstados.EINICIAL);
							return token;
						} else if (lexema.equals("/")) {
							rellenaInfoToken(EnumToken.DIV, lexema, null, MaquinaEstados.EINICIAL);
							return token;
						}
						break;
					case EIGUAL :
						rellenaInfoToken(EnumToken.IGUAL, lexema, null, MaquinaEstados.EINICIAL);
						return token;
					case EMENOR :
						if (buffer == '=') {
							lexema += buffer;
							transita(MaquinaEstados.EMENORIGUAL);
						} else if (buffer == '>') {
							lexema += buffer;
							transita(MaquinaEstados.EDISTINTO);
						} else {
							rellenaInfoToken(EnumToken.MENOR, lexema, null, MaquinaEstados.EINICIAL);
							return token;
						}
						break;
					case EMAYOR :
						if (buffer == '=') {
							lexema += buffer;
							transita(MaquinaEstados.EMAYORIGUAL);
						} else {
							rellenaInfoToken(EnumToken.MAYOR, lexema, null, MaquinaEstados.EINICIAL);
							return token;
						}
						break;
					case EMENORIGUAL :
						rellenaInfoToken(EnumToken.MENORIGUAL, lexema, null, MaquinaEstados.EINICIAL);
						return token;
					case EMAYORIGUAL :
						rellenaInfoToken(EnumToken.MAYORIGUAL, lexema, null, MaquinaEstados.EINICIAL);
						return token;
					case EDISTINTO :
						rellenaInfoToken(EnumToken.DISTINTO, lexema, null, MaquinaEstados.EINICIAL);
						return token;
				}
			}
		} catch (IOException e) {
			throw new CompiladorException("Se ha producido un error de lectura en la línea" + lineaActual);
		}
		return null;
	}

}
