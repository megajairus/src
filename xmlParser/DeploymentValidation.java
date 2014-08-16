package xmlParser;

import java.util.ArrayList;

import structuralModels.Component;
import structuralModels.ComponentInstance;
import structuralModels.Deployment;
import structuralModels.Interface;
import structuralModels.InternodeConnection;

public class DeploymentValidation {

	public static boolean checkConnectionName(String type) {
		String[] name_array = type.split(":");
		if(name_array.length < 3){
			ErrorMessages.internodeConnectionNotEnoughInfo(type);
			return false;
		}
		return true;
	}

	public static boolean validateDeploymentNotation(StructureData structure,
			ArrayList<Deployment> deployments) {
		boolean exceptable = true;
		if(!checkInstances(structure.getInstanceList(), deployments)){
			exceptable = false;
		}
		if(!checkConnetionChannels(structure)){
			exceptable = false;
		}
		return exceptable;
	}

	private static boolean checkInstances(ArrayList<ComponentInstance> instances,
			ArrayList<Deployment> deployments) {
		boolean exceptable = true;
		exceptable = checkInstanceOneLimit(instances, deployments);
		exceptable = checkIfInstanceExists(instances, deployments);
		return exceptable;
	}
		

	private static boolean checkIfInstanceExists(
			ArrayList<ComponentInstance> instances,
			ArrayList<Deployment> deployments) {
		boolean goodModel = true;
		for(int i = 0; i < deployments.size(); i++){
			for(int j=0; j < deployments.get(i).artifactSize(); j++){
				Boolean exceptable = false;
				String name = deployments.get(i).getArtifact(j).getName();
				name = name.replaceAll(" ", "");
				for(int k = 0; k < instances.size(); k++){
					String temp = instances.get(k).getInstanceName();
					temp = temp.replaceAll(" ", "");
					if(name.equals(temp)){
						exceptable = true;
					}
				}
				if(!exceptable){
					ErrorMessages.deploymentInstanceNotDefined(name);
					goodModel=false;
				}
			}
		}
		return goodModel;
	}

	private static boolean checkInstanceOneLimit(
			ArrayList<ComponentInstance> instances,
			ArrayList<Deployment> deployments) {
		boolean exceptable = true;
		for(int i = 0; i < instances.size(); i++){
			int counter = 0;
			String name = instances.get(i).getInstanceName();
			name = name.replaceAll(" ", "");
			for(int j=0; j < deployments.size(); j++){
				for(int k = 0; k < deployments.get(j).artifactSize(); k++){
					String temp = deployments.get(j).getArtifact(k).getName();
					temp = temp.replaceAll(" ", "");
					if(name.equals(temp)){
						counter++;
					}
				}
			}
			if(counter > 1){
				ErrorMessages.instanceUsedMoreThanOnce(name, counter);
				exceptable = false;
			}
			if(counter == 0){
				ErrorMessages.instanceNotUsed(name, counter);
			}
		}
		return exceptable;
	}

	private static boolean checkConnetionChannels(StructureData structure) {
		Boolean is_correct = true;
		ArrayList<InternodeConnection> connections = structure.getInterNodeList();
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
	private static boolean connectionChannelExisits(String channel,
			String instance, StructureData structure) {
		
		if(structure.instanceExistsByName(instance)){
			for(int i = 0; i < structure.instanceSize(); i++){
				if(structure.getInstance(i).getInstanceName().equals(instance)){
					String component_name = structure.getInstance(i).getComponentName();
					int index = structure.getCompoentIndex(component_name);
					Boolean channel_exists = checkPeresnts(structure, structure.getComponents(index), channel);
					if(!channel_exists){
						ErrorMessages.interNodeConnectionChannelNameEorror(channel, structure.getInstance(i).getInstanceName());
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
	private static boolean connectionChannelInDirection(String channel,
			String instance, StructureData structure) {
		if(structure.instanceExistsByName(instance)){
			for(int i = 0; i < structure.instanceSize(); i++){
				if(structure.getInstance(i).getInstanceName().equals(instance)){
					String component_name = structure.getInstance(i).getComponentName();
					int index = structure.getCompoentIndex(component_name);
					Boolean channel_exists = checkPeresntsInDirection(structure, structure.getComponents(index), channel);
					if(!channel_exists){
						ErrorMessages.internodeConnectionChannelDirectionEorror(channel, structure.getInstance(i).getInstanceName(), "in");
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
	private static boolean connectionChannelOutDirection(String channel,
			String instance, StructureData structure) {
		if(structure.instanceExistsByName(instance)){
			for(int i = 0; i < structure.instanceSize(); i++){
				if(structure.getInstance(i).getInstanceName().equals(instance)){
					String component_name = structure.getInstance(i).getComponentName();
					int index = structure.getCompoentIndex(component_name);
					Boolean channel_exists = checkPeresntsOutDirection(structure, structure.getComponents(index), channel);
					if(!channel_exists){
						ErrorMessages.internodeConnectionChannelDirectionEorror(channel, structure.getInstance(i).getInstanceName(), "out");
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

}
