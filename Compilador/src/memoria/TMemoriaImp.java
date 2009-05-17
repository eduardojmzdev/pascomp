package memoria;

import java.util.ArrayList;

public class TMemoriaImp extends TMemoria{

	private ArrayList<Object> aL;
	
	public TMemoriaImp(){
		aL=new ArrayList<Object>();
		
	}
	public Object damePosicion (int pos){
		if ((pos>0)&&(pos<aL.size()))
		return aL.get(pos);
	
	return null;
	}
	
	public void almacenaElemento(int pos, Object elemento){		
		aL.add(pos,elemento);
	}
	
	public int dameTam(){
		return aL.size();
		
	}
	
	
}
