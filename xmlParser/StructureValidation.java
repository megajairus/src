package xmlParser;

import java.util.ArrayList;

import structuralModels.Component;
import structuralModels.Connection;
import structuralModels.Interface;

public class StructureValidation {
	
	public static boolean validateStructureNotation(StructureData structure){
		connectionChannelNameNotation(structure);
		return true;
		
	}
	
	public static boolean connectionChannelNameNotation(StructureData structure){
		Boolean is_correct = true;
		ArrayList<Connection> connections = structure.getConnectionList();
		for (int i = 0; i < connections.size(); i++){
			if (!connectionChannelExisits(connections.get(i).getInChannel(), connections.get(i).getToComponent(), structure)){
				is_correct = false;
			}
			if (!connectionChannelExisits(connections.get(i).getOutChannel(), connections.get(i).getFromComponent(), structure)){
				is_correct = false;
			}
		}
		return is_correct;
	}

	private static boolean connectionChannelExisits(String channel,
			String instance, StructureData structure) {
		
		if(structure.instanceExists(instance)){
			for(int i = 0; i < structure.instanceSize(); i++){
				if(structure.getInstance(i).getInstanceName().equals(instance)){
					String component_name = structure.getInstance(i).getComponentName();
					int index = structure.getCompoentIndex(component_name);
					Boolean channel_exists = checkPeresnts(structure, structure.getComponents(index), channel);
					if(!channel_exists){
						ErrorMessages.ConnectionChannelNameEorror(channel, structure.getInstance(i).getInstanceName());
						return false;
					}
				}
			}
		}
		
		return true;
	}

	private static boolean checkPeresnts(StructureData structure, Component component, String channel) {
		for(int i = 0; i < component.presentsSize(); i++){
			String presents = component.getPresents(i);
			for(int j = 0; j < structure.interfaceSize(); j++){
				if (structure.getInterfaces(j).getName().equals(presents)){
					return checkInterfaceChannels(structure.getInterfaces(j), channel);
				}
			}
		}
		return false;
	}

	private static boolean checkInterfaceChannels(Interface inter, String channel) {
		for(int i = 0; i < inter.size(); i++){
			if(inter.getChannelName(i).equals(channel)){
				return true;
			}
		}
		return false;
	}
}
