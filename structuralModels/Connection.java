package structuralModels;


public class Connection {
	private String in_channel;
	private String out_channel;
	private String from_component;
	private String to_component;
	private String from_component_id;
	private String to_component_id;
	
	public Connection(String in_channel, String out_channel, String to_component_id, String from_component_id){
		this.in_channel = in_channel;
		this.out_channel = out_channel;
		this.from_component_id = from_component_id;
		this.to_component_id = to_component_id;
	}
	
	public Connection(String in_channel, String out_channel, String to_component_id, String from_component_id,
			String from_component, String to_component) {
		this.in_channel = in_channel;
		this.out_channel = out_channel;
		this.from_component_id = from_component_id;
		this.to_component_id = to_component_id;
		this.from_component = from_component;
		this.to_component = to_component;
		
	}

	public void setComponents(String from_component, String to_component){
		this.from_component = from_component;
		this.to_component = to_component;
	}
	
	public String getInChannel(){
		return in_channel;
	}
	
	public String getOutChannel(){
		return out_channel;
	}
	
	public String getFromComponent(){
		return from_component;
	}
	
	public String getToComponent(){
		return to_component;
	}
	
	public String getFromComponentID(){
		return from_component_id;
	}
	
	public String getToComponentID(){
		return to_component_id;
	}
	
	public void setToComponent(String to_component){
		this.to_component = to_component;
	}
	
	public void setFromComponent(String from_component){
		this.from_component = from_component;
	}
	
}
