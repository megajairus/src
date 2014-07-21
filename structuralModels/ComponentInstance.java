package structuralModels;

public class ComponentInstance {
	private String instance_name;
	private String instance_id;
	private String component_id;
	private String component_name;
	
	public ComponentInstance(String name, String id, String component){
		instance_name = name;
		instance_id = id;
		component_id = component;
		component_name = new String();
	}
	public String getInstanceId(){
		return instance_id;
	}
	public String getInstanceName(){
		return instance_name;
	}
	
	public String getComponentId(){
		return component_id;
	}
	
	public String getComponentName(){
		return component_name;
	}
	
	public void setComponentName(String name){
		component_name = name;
	}
}
