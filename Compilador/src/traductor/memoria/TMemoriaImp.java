package traductor.memoria;

import java.util.ArrayList;

/**
 * Implementacion de la memoria
 *
 */
public class TMemoriaImp extends TMemoria{

	private ArrayList<Object> aL;
	
	public TMemoriaImp(){
		aL=new ArrayList<Object>();
		
	}
	@Override
	public Object damePosicion (int pos){
		if ((pos>0)&&(pos<aL.size()))
		return aL.get(pos);
	
	return null;
	}
	
	@Override
	public void almacenaElemento(int pos, Object elemento){		
		aL.add(pos,elemento);
	}
	
	@Override
	public int dameTam(){
		return aL.size();
		
	}
	
	
}
