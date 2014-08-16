package xmlParser;

import java.util.ArrayList;

import structuralModels.Associate;
import structuralModels.BehaviourType;
import structuralModels.Component;
import structuralModels.ComponentInstance;
import structuralModels.Connection;
import structuralModels.Interface;
import structuralModels.InternodeConnection;
import structuralModels.Struct;

public class StructureData {
	private ArrayList<Component> component_list;
	private ArrayList<Interface> interface_list;
	private ArrayList<Struct> struct_list;
	private ArrayList<Connection> connection_list;
	private ArrayList<ComponentInstance> instance_list;
	private ArrayList<Associate> associate_list;
	private ArrayList<InternodeConnection> node_list;
	private String node_name;
	private int order_number;
	public StructureData(){
		 component_list = new ArrayList<Component>();
		 interface_list = new ArrayList<Interface>();
		 struct_list = new ArrayList<Struct>();
		 connection_list = new ArrayList<Connection>();
		 instance_list = new ArrayList<ComponentInstance>();
		 associate_list = new ArrayList<Associate>();
		 node_list = new ArrayList<InternodeConnection>();
		 node_name = "file";
		 order_number = 1;
	}
	
	public void addInternode(InternodeConnection node){
		node_list.add(node);
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
	
	public int interNodeSize(){
		return node_list.size();
	}
	
	public InternodeConnection getInterNode(int index){
		return node_list.get(index);
	}
	
	public boolean componentExists(String name){
		for (int i =0 ; i < component_list.size(); i++){
			if(name.equals(component_list.get(i).getName())){
				return true;
			}
		}
		return false;
	}
	
	public boolean instanceExistsByName(String name){
		for (int i =0 ; i < instance_list.size(); i++){
			if(name.equals(instance_list.get(i).getInstanceName())){
				return true;
			}
		}
		return false;
	}
	public boolean instanceExistsById(String id){
		for (int i =0 ; i < instance_list.size(); i++){
			if(id.equals(instance_list.get(i).getInstanceId())){
				return true;
			}
		}
		return false;
	}
	public boolean associateExistsById(String id){
		for (int i =0 ; i < associate_list.size(); i++){
			if(id.equals(associate_list.get(i).getId())){
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

	public ArrayList<InternodeConnection> getInterNodeList() {
		return node_list;
	}

	public void setInterNodeList(ArrayList<InternodeConnection> inter_list) {
		this.node_list = inter_list;
	}

	public String getNodeName() {
		return node_name;
	}

	public void setNodeName(String node_name) {
		this.node_name = node_name;
	}

	public boolean structExists(String name) {
		for (int i =0 ; i < struct_list.size(); i++){
			if(name.equals(struct_list.get(i).getName())){
				return true;
			}
		}
		return false;
	}

	public boolean interfaceExists(String name) {
		for (int i =0 ; i < interface_list.size(); i++){
			if(name.equals(interface_list.get(i).getName())){
				return true;
			}
		}
		return false;
	}

	public boolean connectionExistsByName(String from_name, String to_name) {
		for (int i =0 ; i < connection_list.size(); i++){
			if(from_name.equals(connection_list.get(i).getFromComponent())){
				if(to_name.equals(connection_list.get(i).getToComponent()))
				return true;
			}
		}
		return false;
	}

	public int getOrderNumber() {
		return order_number;
	}

	public void setOrderNumber(int order_number) {
		this.order_number = order_number;
	}
	

}
