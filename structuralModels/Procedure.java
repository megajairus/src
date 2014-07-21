package structuralModels;

import java.util.ArrayList;

public class Procedure {
	private String name;
	private ArrayList<Variable> parameters;
	private Variable returnType;
	
	public Procedure(String name, ArrayList<Variable> parameters, Variable returnType){
		this.name = name;
		this.parameters = parameters;
		this.returnType = returnType;
	}
	
	public String getName(){
		return name;
	}
	
	public int size(){
		return parameters.size();
	}
	
	public ArrayList<Variable> getParameters(){
		return parameters;
	}
	
	public Variable getReturn(){
		return returnType;
	}
	
	public void setReturn(Boolean bool, String type){
		returnType.setPointer(bool);
		returnType.setType(type);
	}

	public void setParameter(int k, boolean b, String type) {
		parameters.get(k).setPointer(b);
		parameters.get(k).setType(type);
		
	}

}
