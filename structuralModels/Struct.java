package structuralModels;

import java.util.ArrayList;

public class Struct {
	private String name;
	private ArrayList<Variable> variables;
	private String id;
	public Struct(String name, String id, ArrayList<Variable> variables){
		this.name = name;
		this.id = id;
		this.variables = variables;
	}
	
	
	public void addVaiable(String name, String type, Boolean pointer){
		variables.add(new Variable(name, type, pointer));
	}
	
	public String getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	
	public int size(){
		return variables.size();
	}
	
	public String getVariableName(int index){
		return variables.get(index).getName();
	}
	
	public String getVariableType(int index){
		return variables.get(index).getType();
	}
	
	public int variablesSize(){
		return variables.size();
	}
	
	public Boolean getVariablePointer(int index){
		return variables.get(index).getPointer();
	}
	
	public void setVariableType(int index, String type){
		 variables.get(index).setType(type);
	}
	public void setVariablePointer(int index, Boolean pointer){
		 variables.get(index).setPointer(pointer);
	}
}
