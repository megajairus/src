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
			else{
				if (!connectionChannelInDirection(connections.get(i).getInChannel(), connections.get(i).getToComponent(), structure)){
					is_correct = false;
				}
			}
			if (!connectionChannelExisits(connections.get(i).getOutChannel(), connections.get(i).getFromComponent(), structure)){
				is_correct = false;
			}
			else{
				if (!connectionChannelOutDirection(connections.get(i).getOutChannel(), connections.get(i).getFromComponent(), structure)){
					is_correct = false;
				}
			}
		}
		return is_correct;
	}

	private static boolean connectionChannelOutDirection(String channel,
			String instance, StructureData structure) {
		if(structure.instanceExists(instance)){
			for(int i = 0; i < structure.instanceSize(); i++){
				if(structure.getInstance(i).getInstanceName().equals(instance)){
					String component_name = structure.getInstance(i).getComponentName();
					int index = structure.getCompoentIndex(component_name);
					Boolean channel_exists = checkPeresntsOutDirection(structure, structure.getComponents(index), channel);
					if(!channel_exists){
						ErrorMessages.ConnectionChannelDirectionEorror(channel, structure.getInstance(i).getInstanceName(), "out");
						return false;
					}
				}
			}
		}
		return true;
	}

	private static Boolean checkPeresntsOutDirection(StructureData structure,
			Component component, String channel) {
		for(int i = 0; i < component.presentsSize(); i++){
			String presents = component.getPresents(i);
			for(int j = 0; j < structure.interfaceSize(); j++){
				if (structure.getInterfaces(j).getName().equals(presents)){
					return checkInterfaceChannelsDirection(structure.getInterfaces(j), channel, "out");
				}
			}
		}
		return false;
	}

	private static boolean connectionChannelInDirection(String channel,
			String instance, StructureData structure) {
		if(structure.instanceExists(instance)){
			for(int i = 0; i < structure.instanceSize(); i++){
				if(structure.getInstance(i).getInstanceName().equals(instance)){
					String component_name = structure.getInstance(i).getComponentName();
					int index = structure.getCompoentIndex(component_name);
					Boolean channel_exists = checkPeresntsInDirection(structure, structure.getComponents(index), channel);
					if(!channel_exists){
						ErrorMessages.ConnectionChannelDirectionEorror(channel, structure.getInstance(i).getInstanceName(), "in");
						return false;
					}
				}
			}
		}
		return true;
	}

	private static Boolean checkPeresntsInDirection(StructureData structure,
			Component component, String channel) {
		for(int i = 0; i < component.presentsSize(); i++){
			String presents = component.getPresents(i);
			for(int j = 0; j < structure.interfaceSize(); j++){
				if (structure.getInterfaces(j).getName().equals(presents)){
					return checkInterfaceChannelsDirection(structure.getInterfaces(j), channel, "in");
				}
			}
		}
		return false;
	}

	private static Boolean checkInterfaceChannelsDirection(
			Interface inter, String channel, String direction) {
		for(int i = 0; i < inter.size(); i++){
			if(inter.getChannelName(i).equals(channel)){
				if(inter.getChannelDirection(i).equals(direction)){
					return true;
				}
				else{
					return false;
				}
			}
		}
		return false;
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

	public static boolean channelNameNotation(String chanel_name, String inter_name) {
		String[] name_array = chanel_name.split(":");
		if(name_array.length < 2){
			ErrorMessages.channelNotationNoColon(chanel_name, inter_name);
			return false;
		}
		if(name_array.length > 2){
			ErrorMessages.channelNotationMoreThanOneColon(chanel_name, inter_name);
		}
		String direction = name_array[1];
		direction = direction.replaceAll(" ", "");
		if(!direction.equals("in") && !direction.equals("out")){
			ErrorMessages.channelDirectionNotation(chanel_name, inter_name, direction);
			return false;
		}
		return true;
	}
}
