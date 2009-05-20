package aLexico;


import java.io.FileReader;
import java.io.IOException;



public class ALexicoImp extends ALexico {
	

	private char buffer;
	

	private int lineaActual;
	

	private Token token;
	

	private static FileReader ficheroFuente;
	
	
	private MaquinaEstados estado;


	public ALexicoImp(){
		buffer = ' ';
		lineaActual = 1;
		estado=MaquinaEstados.EINICIAL;
		token = new Token();
	}
	

	public ALexicoImp(FileReader f){
		ficheroFuente = f;
		buffer = ' ';
		lineaActual = 1;
		estado=MaquinaEstados.EINICIAL;
		token = new Token();
	}
	
	public int getLinea(){
		return lineaActual;
	}
	
	public FileReader getFichero(){
		return ficheroFuente;
	}
	
	public void setFichero(FileReader f){
		buffer = ' ';
		lineaActual = 1; 
		ficheroFuente = f;
	}
	
	
	private void pasaAMinusculas(){
		if (buffer >='A' && buffer <='Z') buffer=Character.toLowerCase(buffer);
	}
	
	private void transita(MaquinaEstados e) throws IOException{
		estado=e;
		buffer=(char)ficheroFuente.read();
		pasaAMinusculas();
	}
	
	private String leePalabra(String s) throws IOException{
		pasaAMinusculas();
		while ((buffer>='a' && buffer<='z')||(buffer>='0' && buffer<='9')){
			s+=""+buffer;
			buffer=(char)ficheroFuente.read();
			pasaAMinusculas();
		}
		return s;
	}
	
	
	private String leeNumero(String s) throws IOException{
		pasaAMinusculas();
		while (buffer>='0' && buffer<='9'){
			s+=""+buffer;
			buffer=(char)ficheroFuente.read();
			pasaAMinusculas();
		}
		return s;
	}
	
	
	private void evaluaPalabra(String s){
		if (s.equals("program")) rellenaInfoToken(EnumToken.PROGRAM,s,null,MaquinaEstados.EINICIAL);
		else if (s.equals("begin")) rellenaInfoToken(EnumToken.BEGIN,s,null,MaquinaEstados.EINICIAL);
		else if (s.equals("end")) rellenaInfoToken(EnumToken.END,s,null,MaquinaEstados.EINICIAL);
		else if (s.equals("var")) rellenaInfoToken(EnumToken.VAR,s,null,MaquinaEstados.EINICIAL);
		else if (s.equals("const")) rellenaInfoToken(EnumToken.CONST,s,null,MaquinaEstados.EINICIAL);
		else if (s.equals("read")) rellenaInfoToken(EnumToken.READ,s,null,MaquinaEstados.EINICIAL);
		else if (s.equals("write")) rellenaInfoToken(EnumToken.WRITE,s,null,MaquinaEstados.EINICIAL);
		else if (s.equals("integer")) rellenaInfoToken(EnumToken.INTEGER,s,null,MaquinaEstados.EINICIAL);
		else if (s.equals("boolean")) rellenaInfoToken(EnumToken.BOOLEAN,s,null,MaquinaEstados.EINICIAL);
		else if (s.equals("true")) rellenaInfoToken(EnumToken.TRUE,s,s,MaquinaEstados.EINICIAL);
		else if (s.equals("false")) rellenaInfoToken(EnumToken.FALSE,s,s,MaquinaEstados.EINICIAL);
		else if (s.equals("and")) rellenaInfoToken(EnumToken.AND,s,null,MaquinaEstados.EINICIAL);
		else if (s.equals("div")) rellenaInfoToken(EnumToken.DIV,s,null,MaquinaEstados.EINICIAL);
		else if (s.equals("mod")) rellenaInfoToken(EnumToken.MODULO,s,null,MaquinaEstados.EINICIAL);
		else if (s.equals("or")) rellenaInfoToken(EnumToken.OR,s,null,MaquinaEstados.EINICIAL);
		else if (s.equals("not")) rellenaInfoToken(EnumToken.NOT,s,null,MaquinaEstados.EINICIAL);
		else if (s.equals("if")) rellenaInfoToken(EnumToken.IF,s,null,MaquinaEstados.EINICIAL);
		else if (s.equals("then")) rellenaInfoToken(EnumToken.THEN,s,null,MaquinaEstados.EINICIAL);
		else if (s.equals("else")) rellenaInfoToken(EnumToken.ELSE,s,null,MaquinaEstados.EINICIAL);
		else if (s.equals("while")) rellenaInfoToken(EnumToken.WHILE,s,null,MaquinaEstados.EINICIAL);
		else if (s.equals("do")) rellenaInfoToken(EnumToken.DO,s,null,MaquinaEstados.EINICIAL);
		else if (s.equals("array")) rellenaInfoToken(EnumToken.ARRAY,s,null,MaquinaEstados.EINICIAL);
		else if (s.equals("of")) rellenaInfoToken(EnumToken.OF,s,null,MaquinaEstados.EINICIAL);
		else if (s.equals("type")) rellenaInfoToken(EnumToken.TYPE,s,null,MaquinaEstados.EINICIAL);
		else if (s.equals("procedure")) rellenaInfoToken(EnumToken.PROCEDURE,s,null,MaquinaEstados.EINICIAL);
		else if (s.equals("new")) rellenaInfoToken(EnumToken.NEW,s,null,MaquinaEstados.EINICIAL);
		else if (s.equals("dispose")) rellenaInfoToken(EnumToken.DISPOSE,s,null,MaquinaEstados.EINICIAL);
		else if (s.equals("nil")) rellenaInfoToken(EnumToken.NIL,s,null,MaquinaEstados.EINICIAL);
		else rellenaInfoToken(EnumToken.IDENTIFICADOR,s,null,MaquinaEstados.EINICIAL);
	}


	private void rellenaInfoToken(EnumToken tok,String s,Object valor,MaquinaEstados m){
		token.setCategoria(tok);
		token.setLexema(s);		
		estado= m;
	}
	

	public Token obtenerToken() throws Exception{
		String lexema = "";
		try{
			boolean finFichero=Character.isDefined(buffer);
			while(finFichero){
				finFichero=Character.isDefined(buffer);
				switch(estado){
				case EINICIAL: 
					switch(buffer){
					case '\n':
						lineaActual++;
						transita(MaquinaEstados.EINICIAL);
						break;
					case ' ':
						transita(MaquinaEstados.EINICIAL);
						break;
					case '\r':
						transita(MaquinaEstados.EINICIAL);
						break;
					case '\t':
						transita(MaquinaEstados.EINICIAL);
						break;
					case '0': 
						lexema += buffer;
						transita(MaquinaEstados.ENUMCERO);
						break;
					case '1':case '2':case '3':case '4':case '5':case '6':case '7':case '8':case '9':
						lexema +=buffer;
						transita(MaquinaEstados.ENUMERO);
						break;
					case 'a':case 'b':case 'c':case 'd':case 'e':case 'f':case 'g':case 'h':case 'i':case 'j':case 'k':case 'l':case 'm':
					case 'n':case 'o':case 'p':case 'q':case 'r':case 's':case 't':case 'u':case 'v':case 'w':case 'x':case 'y':case 'z':
						lexema +=buffer; 
						transita(MaquinaEstados.EIDENOPALRES);
						break;
					case ',':
						lexema +=buffer;
						transita(MaquinaEstados.ECOMA);
						break;
					case '.':
						lexema +=buffer;
						transita(MaquinaEstados.EPUNTO);
						break;
					case ';':
						lexema +=buffer;
						transita(MaquinaEstados.EPUNTOYCOMA);
						break;
					case ':':
						lexema +=buffer;
						transita(MaquinaEstados.EDOSPUNTOS);
						break;
					case '(':
						lexema +=buffer;
						transita(MaquinaEstados.EPARENTESISAPER);
						break;
					case ')':
						lexema +=buffer;
						transita(MaquinaEstados.EPARENTESISCIER);
						break;
					case '[':
						lexema +=buffer;
						transita(MaquinaEstados.ECORCHETEAPER);
						break;
					case ']':
						lexema +=buffer;
						transita(MaquinaEstados.ECORCHETECIER);
						break;
					case '+':case '-':case '*':case'/':
						lexema +=buffer; 
						transita(MaquinaEstados.ESUMORESOMULODIV);
						break;
					case '=':
						lexema +=buffer;
						transita(MaquinaEstados.EIGUAL);
						break;
					case '<':
						lexema +=buffer; 
						transita(MaquinaEstados.EMENOR);
						break;
					case '>':
						lexema +=buffer; 
						transita(MaquinaEstados.EMAYOR);
						break;
					case '^':
						lexema +=buffer; 
						transita(MaquinaEstados.EPUNTERO);
						break;
					default:
						if(ficheroFuente.ready()){
							throw new Exception("Error Lexico: Linea "+lineaActual+":"+" '"+buffer+"'"+".");
						}
					}
					break;
				case ENUMCERO:
					lexema=leeNumero(lexema);
					if (!((buffer>='a' && buffer<='z')||(buffer>='A' && buffer <='Z'))){
						rellenaInfoToken(EnumToken.NUMERO,lexema,Integer.parseInt(lexema),MaquinaEstados.EINICIAL);
						return token;
					}
					else{
						throw new Exception("Error Lexico: Linea "+lineaActual+":"+" '"+lexema+buffer+"'"+".");
						}
				case ENUMERO:
					lexema=leeNumero(lexema);
					if (!(buffer>='a' && buffer<='z')){
						rellenaInfoToken(EnumToken.NUMERO,lexema,Integer.parseInt(lexema),MaquinaEstados.EINICIAL);	
						return token;
					}
					else{
						throw new Exception("Error Lexico: Linea "+lineaActual+":"+" '"+lexema+buffer+"'"+".");
						}						
				case EIDENOPALRES:
					lexema=leePalabra(lexema);
					evaluaPalabra(lexema);
					return token;
				case ECOMA:
					rellenaInfoToken(EnumToken.COMA,lexema,null,MaquinaEstados.EINICIAL);
					return token;
				case EPUNTO:
					rellenaInfoToken(EnumToken.PUNTO,lexema,null,MaquinaEstados.EINICIAL);
					return token;
				case EPUNTOYCOMA:
					rellenaInfoToken(EnumToken.PUNTOYCOMA,lexema,null,MaquinaEstados.EINICIAL);
					return token;
				case EPARENTESISAPER:
					rellenaInfoToken(EnumToken.PARENTESISAPER,lexema,null,MaquinaEstados.EINICIAL);
					return token;
				case EPARENTESISCIER:
					rellenaInfoToken(EnumToken.PARENTESISCIER,lexema,null,MaquinaEstados.EINICIAL);
					return token;
				case ECORCHETEAPER:
					rellenaInfoToken(EnumToken.CORCHETEAPER,lexema,null,MaquinaEstados.EINICIAL);
					return token;
				case ECORCHETECIER:
					rellenaInfoToken(EnumToken.CORCHETECIER,lexema,null,MaquinaEstados.EINICIAL);
					return token;
				case EPUNTERO:
					rellenaInfoToken(EnumToken.PUNTERO,lexema,null,MaquinaEstados.EINICIAL);
					return token;
				case EDOSPUNTOS:
					if (buffer== '='){
						lexema +=buffer; 
						transita(MaquinaEstados.EASIGNACION);
					}
					else{
						rellenaInfoToken(EnumToken.DOSPUNTOS,lexema,null,MaquinaEstados.EINICIAL);
						return token;
					}
					break;
				case EASIGNACION:
					rellenaInfoToken(EnumToken.ASIGNACION,lexema,null,MaquinaEstados.EINICIAL);
					return token;
				case ESUMORESOMULODIV:
					if (lexema.equals("+")){
						rellenaInfoToken(EnumToken.SUMA,lexema,null,MaquinaEstados.EINICIAL);
						return token;
					}
					else if (lexema.equals("-")){
						rellenaInfoToken(EnumToken.RESTA,lexema,null,MaquinaEstados.EINICIAL);
						return token;
					}
					else if (lexema.equals("*")){
						rellenaInfoToken(EnumToken.PRODUCTO,lexema,null,MaquinaEstados.EINICIAL);
						return token;
					}
					else if (lexema.equals("/")){
						rellenaInfoToken(EnumToken.DIVISION,lexema,null,MaquinaEstados.EINICIAL);
						return token;
					}
					break;
				case EIGUAL:
					rellenaInfoToken(EnumToken.IGUAL,lexema,null,MaquinaEstados.EINICIAL);
					return token;
				case EMENOR:
					if (buffer== '='){
						lexema +=buffer; 
						transita(MaquinaEstados.EMENORIGUAL);
					}
					else if (buffer== '>'){
						lexema +=buffer; 
						transita(MaquinaEstados.EDISTINTO);
					}
					else{
						rellenaInfoToken(EnumToken.MENOR,lexema,null,MaquinaEstados.EINICIAL);
						return token;
					}
					break;
				case EMAYOR:
					if (buffer== '='){
						lexema +=buffer; 
						transita(MaquinaEstados.EMAYORIGUAL);
					}
					else{
						rellenaInfoToken(EnumToken.MAYOR,lexema,null,MaquinaEstados.EINICIAL);
						return token;
					}
					break;
				case EMENORIGUAL:
					rellenaInfoToken(EnumToken.MENORIGUAL,lexema,null,MaquinaEstados.EINICIAL);
					return token;
				case EMAYORIGUAL:
					rellenaInfoToken(EnumToken.MAYORIGUAL,lexema,null,MaquinaEstados.EINICIAL);
					return token;
				case EDISTINTO:
					rellenaInfoToken(EnumToken.DISTINTO,lexema,null,MaquinaEstados.EINICIAL);
					return token;
				}
			}
		}
		catch(IOException e){
			//TODO tratar la excepcion
			//Tratar excepción
		}
		return null;
	}
	
}
