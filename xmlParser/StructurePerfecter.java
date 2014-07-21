package xmlParser;

import java.util.ArrayList;

import structuralModels.Associate;
import structuralModels.Component;
import structuralModels.ComponentInstance;
import structuralModels.Interface;
import structuralModels.Struct;

public class StructurePerfecter {
	public static StructureData finishStructureData(StructureData structure){
		fillVariableTypes(structure);
		linkComponentPresents(structure);
		setConnections(structure);
		setComponenInstances(structure);
		return structure;
	}

	private static void setComponenInstances(StructureData structure) {
		for(int i = 0; i < structure.instanceSize(); i++){
			String component_id = structure.getInstance(i).getComponentId();
			String component_name = findComponent(component_id, structure.getComponentList());
			structure.getInstance(i).setComponentName(component_name);
		}
		
	}

	private static String findComponent(String id, ArrayList<Component> components) {
		for (int i = 0; i < components.size(); i++){
			if (components.get(i).getId().equals(id)){
				return components.get(i).getName();
			}
		}
		return null;
	}

	private static void setConnections(StructureData structure) {
		for(int i = 0; i < structure.connectionSize(); i++){
			String to = structure.getConnections(i).getToComponentID();
			to = findComponentInstance(to, structure.getInstanceList());
			to = findAssociate(to, structure.getAssociateList());
			structure.getConnections(i).setToComponent(to);
			String from = structure.getConnections(i).getFromComponentID();
			from = findComponentInstance(from, structure.getInstanceList());
			from = findAssociate(from, structure.getAssociateList());
			structure.getConnections(i).setFromComponent(from);
		}
		
	}

	private static String findAssociate(String id,
			ArrayList<Associate> associateList) {
		for (int i = 0; i < associateList.size(); i++){
			if (associateList.get(i).getId().equals(id)){
				return associateList.get(i).getName();
			}
		}
		return id;
	}
	

	private static String findComponentInstance(String id, ArrayList<ComponentInstance> arrayList) {
		
		for (int i = 0; i < arrayList.size(); i++){
			if (arrayList.get(i).getInstanceId().equals(id)){
				return arrayList.get(i).getInstanceName();
			}
		}
		return id;
	}

	private static void linkComponentPresents(StructureData structure) {
		for(int i=0; i < structure.componetSize(); i++){
			for(int j=0; j < structure.getComponents(i).presentsSize(); j++){
				String present = structure.getComponents(i).getPresents(j);
				present = findInterface(present, structure.getInterfaceList());
				structure.getComponents(i).setPresents(j, present);
			}
		}
		
	}

	private static String findInterface(String present, ArrayList<Interface> interfaceList) {
		for (int i = 0; i < interfaceList.size(); i++){
			if (interfaceList.get(i).getId().equals(present)){
				return interfaceList.get(i).getName();
			}
		}
		return present;
	}

	private static void fillVariableTypes(StructureData structure) {
		checkComponentVariables(structure);
		checkStructVariables(structure);
		checkInterfaceVariables(structure);
	}

	private static void checkInterfaceVariables(StructureData structure) {
		for(int i=0; i < structure.interfaceSize(); i++){
			for(int j=0; j < structure.getInterfaces(i).size(); j++){
				if (structure.getInterfaces(i).getChannelPointer(j)){
					String type = structure.getInterfaces(i).getChannelType(j);
					type = findType(type, structure.getStructList());
					structure.getInterfaces(i).setChannelType(j, type);
					structure.getInterfaces(i).setChannelPointer(j, false);
				}
				else{
					if (structure.getInterfaces(i).getChannelType(j).equals("Boolean")){
						structure.getInterfaces(i).setChannelType(j, "bool");
					}
				}
			}
		}
	}

	private static void checkStructVariables(StructureData structure) {
		for(int i=0; i < structure.structSize(); i++){
			for(int j=0; j < structure.getStructs(i).variablesSize(); j++){
				if (structure.getStructs(i).getVariablePointer(j)){
					String type = structure.getStructs(i).getVariableType(j);
					type = findType(type, structure.getStructList());
					structure.getStructs(i).setVariableType(j, type);
					structure.getStructs(i).setVariablePointer(j, false);
				}
				else{
					if (structure.getStructs(i).getVariableType(j).equals("Boolean")){
						structure.getStructs(i).setVariableType(j, "bool");
					}
				}
			}
		}
	}

	private static void checkComponentVariables(StructureData structure) {
		for(int i=0; i < structure.componetSize(); i++){
			for(int j=0; j < structure.getComponents(i).variablesSize(); j++){
				if (structure.getComponents(i).getVariablePointer(j)){
					String type = structure.getComponents(i).getVariableType(j);
					type = findType(type, structure.getStructList());
					structure.getComponents(i).setVariableType(j, type);
					structure.getComponents(i).setVariablePointer(j, false);
				}
				else{
					if (structure.getComponents(i).getVariableType(j).equals("Boolean")){
						structure.getComponents(i).setVariableType(j, "bool");
					}
				}
			}
			for(int j=0; j < structure.getComponents(i).procedureSize(); j++){
				if (structure.getComponents(i).getProcedureReturnPointer(j)){
					String type = structure.getComponents(i).getProcedureReturn(j);
					type = findType(type, structure.getStructList());
					structure.getComponents(i).getProcedure(j).setReturn(false, type);
				}
				else{
					if (structure.getComponents(i).getProcedureReturn(j).equals("Boolean")){
						structure.getComponents(i).getProcedure(j).setReturn(false, "bool");
					}
				}
				for(int k = 0; k < structure.getComponents(i).procedureParameterSize(j); k++){
					if(structure.getComponents(i).getProcedureParameterPointer(j, k)){
						String type = structure.getComponents(i).getProcedureVariableType(j, k);
						type = findType(type, structure.getStructList());
						structure.getComponents(i).getProcedure(j).setParameter(k, false, type);
					}
					else{
						if (structure.getComponents(i).getProcedureVariableType(j, k).equals("Boolean")){
							structure.getComponents(i).getProcedure(j).setParameter(k, false, "bool");
						}
					}
				}
				
			}
		}
	}

	

	private static String findType(String type, ArrayList<Struct> structs) {
		for (int i = 0; i < structs.size(); i++){
			if (structs.get(i).getId().equals(type)){
				return structs.get(i).getName();
			}
		}
		return type;
	}
	
	


}
