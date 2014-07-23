package xmlParser;

import java.util.ArrayList;

import structuralModels.Component;
import structuralModels.Connection;
import structuralModels.Interface;

public class StructureValidation {
	
	public static boolean validateStructureNotation(StructureData structure){
		return connectionChannelNameNotation(structure);
	}
	
	public static boolean connectionChannelNameNotation(StructureData structure){
		Boolean is_correct = true;
		ArrayList<Connection> connections = structure.getConnectionList();
		for (int i = 0; i < connections.size(); i++){
			String in_chan = connections.get(i).getInChannel();
			String in_component = connections.get(i).getToComponent();
			String out_chan = connections.get(i).getOutChannel();
			String out_component = connections.get(i).getFromComponent();
			if (!connectionChannelExisits(in_chan, in_component, structure)){
				is_correct = false;
			}
			else{
				if (!connectionChannelInDirection(in_chan, in_component, structure)){
					is_correct = false;
				}
			}
			if (!connectionChannelExisits(out_chan, out_component, structure)){
				is_correct = false;
			}
			else{
				if (!connectionChannelOutDirection(out_chan, out_component, structure)){
					is_correct = false;
				}
			}
		}
		return is_correct;
	}

	private static boolean connectionChannelOutDirection(String channel,
			String instance, StructureData structure) {
		if(structure.instanceExistsByName(instance)){
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
		if(structure.instanceExistsByName(instance)){
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
		
		if(structure.instanceExistsByName(instance)){
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

	public static boolean connectionNameNotation(String name) {
		String[] name_array = name.split(":");
		if(name_array.length < 2){
			ErrorMessages.connectionNotationNoTwoChannels(name);
			return false;
		}
		if(name_array.length > 2){
			ErrorMessages.channelNotationMoreThanTwoChannels(name);
		}
		return true;
	}

	public static boolean checkDependencies(StructureData structure) {
		boolean exceptable = true;
		for (int i = 0; i < structure.connectionSize(); i++){
			String id = structure.getConnections(i).getFromComponentID();
			if(!checkDepenencyNode(structure, id)){
				String name = structure.getConnections(i).getOutChannel() + ":" + structure.getConnections(i).getInChannel();
				ErrorMessages.badOutgoingDependency(name);
				exceptable = false;
			}
			id = structure.getConnections(i).getToComponentID();
			if(!checkDepenencyNode(structure, id)){
				String name = structure.getConnections(i).getOutChannel() + ":" + structure.getConnections(i).getInChannel();
				ErrorMessages.badIncomingDependency(name);
				exceptable = false;
			}
		}
		return exceptable;
	}

	private static boolean checkDepenencyNode(StructureData structure, String id) {
		if(structure.instanceExistsById(id)){
			return true;
		}
		if(structure.associateExistsById(id)){
			return true;
		}
		return false;
	}
}
