package structuralModels;


public class Channel extends Variable {
	
	String direction;
	
	public Channel(String name, String type, Boolean pointer, String direction) {
		super(name, type, pointer);
		this.direction = direction; 
	}
	
	public String getDirection(){
		return direction;
	}

}
