package structuralModels;

import java.util.ArrayList;

public class BehaviourType {
	private String type;
	private ArrayList<String> attributes;
	
	public BehaviourType(String type, ArrayList<String> attributes){
		this.type = type;
		this.attributes = attributes;
	}
	
	public String getType(){
		return type;
	}
	
	public String getAttribute(int index){
		return attributes.get(index);
	}
	
	public int size(){
		return attributes.size();
	}

}
