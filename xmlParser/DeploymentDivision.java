package xmlParser;

import java.util.ArrayList;

import structuralModels.Artifact;
import structuralModels.BehaviourElement;
import structuralModels.Channel;
import structuralModels.Component;
import structuralModels.ComponentInstance;
import structuralModels.Connection;
import structuralModels.Deployment;
import structuralModels.Interface;
import structuralModels.InternodeConnection;
import structuralModels.Struct;
import structuralModels.Variable;

public class DeploymentDivision {
	static StructureData pack;

	public static void writeNodes(StructureData structure,
			ArrayList<Deployment> deployments, ArrayList<BehaviourElement> component_behaviour) {
		for(int i = 0; i < deployments.size(); i++){
			pack = new StructureData();
			pack.setOrderNumber(i + 1);
			buildStructure(deployments.get(i), structure, pack);
			WriteXMLFile.createIntermediateLanguage(pack, component_behaviour, deployments.size());
			System.out.println("**********************************");
		}
	}

	private static void buildStructure(Deployment deployment,
			StructureData structure, StructureData pack) {
		ArrayList<Artifact> arts = deployment.getArtifactList();
		pack.setNodeName(deployment.getName());
		
		AddInstances(arts, structure.getInstanceList(), pack);
		AddComponents(pack.getInstanceList(), structure.getComponentList(), structure.getStructList(), pack);
		AddInterfaces(pack.getComponentList(), structure.getInterfaceList(), structure.getStructList(), pack);
		addStructs(pack.getStructList(), structure.getStructList(), pack);
		addConnections(structure.getConnectionList(), pack);
		addInterconnections(structure.getInterNodeList(), pack);
	}

	private static void addInterconnections(ArrayList<InternodeConnection> interNodeList, StructureData pack) {
		for(int i = 0; i < interNodeList.size(); i++){
			String in_name = interNodeList.get(i).getToComponent();
			String out_name = interNodeList.get(i).getFromComponent();
			if(pack.instanceExistsByName(in_name) || pack.instanceExistsByName(out_name)){
				if(!(pack.instanceExistsByName(in_name) && pack.instanceExistsByName(out_name))){
					String other_node = interNodeList.get(i).getInternode();
					if(!other_node.equals(pack.getNodeName())){
							pack.addInternode(interNodeList.get(i));
					}
				}
			}
		}
	}

	private static void addConnections(ArrayList<Connection> connections, StructureData pack) {
		for(int i = 0; i < connections.size(); i++){
			String in_name = connections.get(i).getToComponent();
			String out_name = connections.get(i).getFromComponent();
			if(pack.instanceExistsByName(in_name) && pack.instanceExistsByName(out_name)){
				pack.addConnection(connections.get(i));
			}
		}
	}

	private static void addStructs(ArrayList<Struct> structs1,
			ArrayList<Struct> structs2, StructureData pack) {
		for(int i = 0; i < structs1.size(); i++){
			addStructsFromVariables(structs1.get(i).getVariables(), structs2, pack);
		}
	}

	private static void AddInterfaces(ArrayList<Component> components,
			ArrayList<Interface> interfaces, ArrayList<Struct> structs,
			StructureData pack) {
		for(int i = 0; i < components.size(); i++){
			for(int j = 0; j < components.get(i).presentsSize(); j++){
				for(int k = 0; k < interfaces.size(); k++){
					String old_name = components.get(i).getPresents(j);
					String new_name = interfaces.get(k).getName();
					if(old_name.equals(new_name)){
						if(!pack.interfaceExists(new_name)){
							pack.addInterface(interfaces.get(k));
							addStructsFromChannels(interfaces.get(k).getChannels(), structs, pack);
						}
						
					}
				}
			}
		}
	}

	private static void addStructsFromChannels(ArrayList<Channel> channels,
			ArrayList<Struct> structs, StructureData pack2) {
		for(int i = 0; i < channels.size(); i++){
			for(int j = 0; j < structs.size(); j++){
				String old_name = channels.get(i).getType();
				String new_name = structs.get(j).getName();
				if(old_name.equals(new_name)){
					if(!pack.structExists(new_name)){
						pack.addStruct(structs.get(j));
					}
					
				}
			}
		}
	}

	private static void AddComponents(ArrayList<ComponentInstance> instances,
			ArrayList<Component> components, ArrayList<Struct> structs, StructureData pack)  {
		for(int i = 0; i < instances.size(); i++){
			for(int j = 0; j < components.size(); j++){
				String old_name = instances.get(i).getComponentName();
				String new_name = components.get(j).getName();
				if(old_name.equals(new_name)){
					if(!pack.componentExists(new_name)){
						pack.addComponent(components.get(j));
						addStructsFromVariables(components.get(j).getVariables(), structs, pack);
					}
					
				}
			}
		}
	}

	private static void addStructsFromVariables(ArrayList<Variable> variables,
			ArrayList<Struct> structs, StructureData pack2) {
		for(int i = 0; i < variables.size(); i++){
			for(int j = 0; j < structs.size(); j++){
				String old_name = variables.get(i).getType();
				String new_name = structs.get(j).getName();
				if(old_name.equals(new_name)){
					if(!pack.structExists(new_name)){
						pack.addStruct(structs.get(j));
					}
					
				}
			}
		}
	}

	private static void AddInstances(ArrayList<Artifact> arts,
			ArrayList<ComponentInstance> instances, StructureData pack) {
		for(int i = 0; i < arts.size(); i++){
			for(int j = 0; j < instances.size(); j++){
				String old_name = arts.get(i).getName();
				String new_name = instances.get(j).getInstanceName();
				if(old_name.equals(new_name)){
					if(!pack.instanceExistsByName(new_name)){
						pack.addInstance(instances.get(j));
					}
					
				}
			}
		}
	}
	
}
