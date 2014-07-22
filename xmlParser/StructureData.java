package xmlParser;

import java.util.ArrayList;

import structuralModels.Associate;
import structuralModels.BehaviourType;
import structuralModels.Component;
import structuralModels.ComponentInstance;
import structuralModels.Connection;
import structuralModels.Interface;
import structuralModels.Struct;

public class StructureData {
	private ArrayList<Component> component_list;
	private ArrayList<Interface> interface_list;
	private ArrayList<Struct> struct_list;
	private ArrayList<Connection> connection_list;
	private ArrayList<ComponentInstance> instance_list;
	private ArrayList<Associate> associate_list;
	public StructureData(){
		 component_list = new ArrayList<Component>();
		 interface_list = new ArrayList<Interface>();
		 struct_list = new ArrayList<Struct>();
		 connection_list = new ArrayList<Connection>();
		 instance_list = new ArrayList<ComponentInstance>();
		 associate_list = new ArrayList<Associate>();
	}
	
	public void addAssociate(Associate associate){
		associate_list.add(associate);
	}
	public void addInstance(ComponentInstance instance){
		instance_list.add(instance);
	}
	public void addComponent(Component component){
		component_list.add(component);
	}
	public void addInterface(Interface inter){
		interface_list.add(inter);
	}
	public void addStruct(Struct struct){
		struct_list.add(struct);
	}
	public void addConnection(Connection connection){
		connection_list.add(connection);
	}
	public Associate getAssociate(int index){
		return associate_list.get(index);
	}
	public ComponentInstance getInstance(int index){
		return instance_list.get(index);
	}
	
	public Component getComponents(int index){
		return component_list.get(index);
	}
	public ArrayList<ComponentInstance> getInstanceList(){
		return instance_list;
	}
	public ArrayList<Component> getComponentList(){
		return component_list;
	}
	public Interface getInterfaces(int index){
		return interface_list.get(index);
	}
	public ArrayList<Interface> getInterfaceList(){
		return interface_list;
	}
	public Struct getStructs(int index){
		return struct_list.get(index);
	}
	
	public ArrayList<Struct> getStructList(){
		return struct_list;
	}
	public Connection getConnections(int index){
		return connection_list.get(index);
	}
	public ArrayList<Connection> getConnectionList(){
		return connection_list;
	}
	public int instanceSize(){
		return instance_list.size();
	}
	public int componetSize(){
		return component_list.size();
	}
	public int interfaceSize(){
		return interface_list.size();
	}
	public int structSize(){
		return struct_list.size();
	}
	public int connectionSize(){
		return connection_list.size();
	}
	
	public Boolean componentExists(String name){
		for (int i =0 ; i < component_list.size(); i++){
			if(name.equals(component_list.get(i).getName())){
				return true;
			}
		}
		return false;
	}
	
	public Boolean instanceExists(String name){
		for (int i =0 ; i < instance_list.size(); i++){
			if(name.equals(instance_list.get(i).getInstanceName())){
				return true;
			}
		}
		return false;
	}
	public int getCompoentIndex(String name){
		for (int i =0 ; i < component_list.size(); i++){
			if(name.equals(component_list.get(i).getName())){
				return i;
			}
		}
		return -1;
	}
	
	public void setComponentBehaviour(int index, ArrayList<BehaviourType> behaviours){
		component_list.get(index).setBehaviours(behaviours);
	}

	public ArrayList<Associate> getAssociateList() {
		return associate_list;
	}
	

}
