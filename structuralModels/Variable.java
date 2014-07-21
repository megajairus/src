package structuralModels;

public class Variable {
	private String name;
	private String type;
	private Boolean pointer;
	public Variable(String name, String type, Boolean pointer){
		this.name = name;
		this.type = type;
		this.pointer = pointer;
	}
	
	public String getName(){
		return name;
	}
	
	public String getType(){
		return type;
	}
	public Boolean getPointer(){
		return pointer;
	}
	public void setType(String type){
		this.type = type;
	}
	public void setPointer(Boolean pointer){
		this.pointer = pointer;
	}

}
