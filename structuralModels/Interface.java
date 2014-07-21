package structuralModels;

import java.util.ArrayList;

public class Interface {
	private String name;
	private String id;
	private  ArrayList<Channel> channels;
	
	public Interface(String name, String id, ArrayList<Channel>  channels){
		this.name = name;
		this.id = id;
		this.channels = channels;
	}
	
	public void addVaiable(String name, String type, Boolean pointer, String direction){
		channels.add(new Channel(name, type, pointer, direction));
	}
	
	public String getName(){
		return name;
	}
	
	public String getId(){
		return id;
	}
	
	
	public int size(){
		return channels.size();
	}
	
	public String getChannelName(int index){
		return channels.get(index).getName();
	}
	
	public String getChannelType(int index){
		return channels.get(index).getType();
	}
	
	public Boolean getChannelPointer(int index){
		return channels.get(index).getPointer();
	}
	
	public void setChannelType(int index, String type){
		channels.get(index).setType(type);
	}
	
	public void setChannelPointer(int index, Boolean pointer){
		channels.get(index).setPointer(pointer);
	}
	public String getChannelDirection(int index){
		return channels.get(index).getDirection();
	}
}
