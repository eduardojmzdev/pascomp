package traductor.aLexico;

import java.io.FileReader;

//clase que genera las instancias relacionadas con los tokens 
public abstract class ALexico {
	

	private static ALexico instancia;
	

	public static ALexico getInstance(){
		if (instancia==null)
			instancia = new ALexicoImp();
		return instancia;
	}

	public abstract Token obtenerToken() throws Exception;
	
	public abstract void setFichero(FileReader f);
	
	public abstract int getLinea();	
	
	public static void reset(){
		instancia = null;
	}
}
