package xmlParser;

import java.nio.channels.Channel;
import java.util.ArrayList;

import structuralModels.BehaviourElement;
import structuralModels.Component;
import structuralModels.Interface;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
public class StateMachineValidation {
	private static final String XMI_ID = "xmi:id";
	private static final String SOURCE = "source";

	public static boolean checkReceiveSendChannels(StructureData structure,
			ArrayList<BehaviourElement> behaviours) {
		boolean exceptable = true;
		ArrayList<Interface> interfaces = structure.getInterfaceList();
		for(int i = 0; i < behaviours.size(); i++){
			String component_name = behaviours.get(i).getOwner();
			int index = structure.getCompoentIndex(component_name);
			Component component = structure.getComponents(index);
			Element element = behaviours.get(i).getElement();
			NodeList nodes = element.getElementsByTagName("send");
			if(!checkSendChannels(nodes, component, interfaces)){
				exceptable = false;
			}
			nodes = element.getElementsByTagName("receive");
			if(!checkReceiveChannels(nodes, component, interfaces)){
				exceptable = false;
			}
		}
		return exceptable;
	}

	private static boolean checkReceiveChannels(NodeList nodes,
			Component component, ArrayList<Interface> interfaces) {
		boolean exceptable = true;
		for(int i = 0; i < nodes.getLength(); i++){
			Element send = (Element) nodes.item(i);
			String channel_name = send.getAttribute("from");
			exceptable = findInChannel(component, interfaces, send, channel_name);
		}
		return exceptable;
	}

	private static boolean findInChannel(Component component,
			ArrayList<Interface> interfaces, Element send, String channel_name) {
		int index = -1;
		for(int j = 0; j < component.presentsSize(); j++){
			String presents_name = component.getPresents(j);
			for(int k = 0; k < interfaces.size(); k++){
				String interface_name = interfaces.get(k).getName();
				if(presents_name.equals(interface_name)){
					index = checkInterfaceChannels(channel_name, interfaces.get(k));
					if(index >= 0){
						String channel_direction = interfaces.get(k).getChannelDirection(index);
						return (checkInDirection(channel_direction, send));
					}
				}
			}
		}
		if(index < 0){
			String name =  send.getAttribute("identifier");
			String channel = send.getAttribute("from");
			ErrorMessages.behaviourInChannelNotFound(name, channel);
			return false;
		}
		return false;
	}

	private static boolean checkInDirection(String channel_direction,
			Element send) {
		if(channel_direction.equals("in")){
			return true;
		}
		else{
			String name =  send.getAttribute("identifier");
			String channel = send.getAttribute("from");
			ErrorMessages.receiveBehaviourWrongDriction(name, channel);
			return false;
		}
	}

	private static boolean checkSendChannels(NodeList nodes,
			Component component, ArrayList<Interface> interfaces) {
		boolean exceptable = true;
		for(int i = 0; i < nodes.getLength(); i++){
			Element send = (Element) nodes.item(i);
			String channel_name = send.getAttribute("on");
			exceptable = findOutChannel(component, interfaces, send, channel_name);
		}
		return exceptable;
	}

	private static boolean findOutChannel(Component component,
			ArrayList<Interface> interfaces, Element send, String channel_name) {
		int index = -1;
		for(int j = 0; j < component.presentsSize(); j++){
			String presents_name = component.getPresents(j);
			for(int k = 0; k < interfaces.size(); k++){
				String interface_name = interfaces.get(k).getName();
				if(presents_name.equals(interface_name)){
					index = checkInterfaceChannels(channel_name, interfaces.get(k));
					if(index >= 0){
						String channel_direction = interfaces.get(k).getChannelDirection(index);
						return (checkOutDirection(channel_direction, send));
					}
				}
			}
		}
		if(index < 0){
			String name =  send.getAttribute("identifier");
			String channel = send.getAttribute("on");
			ErrorMessages.behaviourOutChannelNotFound(name, channel);
			return false;
		}
		return false;
	}

	private static boolean checkOutDirection(String channel_direction, Element send) {
		if(channel_direction.equals("out")){
			return true;
		}
		else{
			String name =  send.getAttribute("identifier");
			String channel = send.getAttribute("on");
			ErrorMessages.sendBehaviourWrongDriction(name, channel);
			return false;
		}
	}

	private static int checkInterfaceChannels(String channel_name,
			Interface inter) {
		for(int i = 0; i < inter.size(); i++){
			String inter_name = inter.getChannelName(i);
			if (inter_name.equals(channel_name)){
				return i;
			}
		}
		return -1;
	}

	public static boolean checkTransitionAmount(NodeList transitions,
			Element currant_state) {
		int counter = 0;
		for(int i =0; i < transitions.getLength(); i++){
			Element line = (Element) transitions.item(i);
			if(line.getAttribute(SOURCE).equals(currant_state.getAttribute(XMI_ID))){
				if (!currant_state.getAttribute("kind").equals("fork")){
					counter++;
				}
			}
		}
		if(counter < 2){
			return true;
		}
		else{
			String name = currant_state.getAttribute("name");
			ErrorMessages.OneTooManyTransitions(name, counter);
			return false;
		}
	}

	public static boolean validateFor(Element state, Element action) {
		String State_name = state.getAttribute("name");
		String [] state_list = State_name.split(":");
		if(state_list.length < 2){
			ErrorMessages.WrongForState(State_name);
			return false;
		}
		String [] limit_list = state_list[0].split("=");
		if(limit_list.length < 2){
			ErrorMessages.WrongForState(State_name);
			return false;
		}
		return true;
	}

	public static boolean validateIf(Element state, Element action) {
		String State_name = state.getAttribute("name");
		String [] state_list = State_name.split(" ");
		if(state_list[0].equals("")){
			ErrorMessages.WrongIfState(State_name);
			return false;
		}
		return true;
	}

	public static boolean validatePrint(Element state, Element action) {
		boolean exceptable = true;
		String action_name = action.getAttribute("name");
		String State_name = state.getAttribute("name");
		String [] action_list = action_name.split(" ");
		String [] state_list = State_name.split(" ");
		if(action_list.length < 2){
			ErrorMessages.WrongPrintAction(action_name);
			exceptable = false;
		}
		if(state_list[0].equals("")){
			ErrorMessages.WrongVariableState(State_name);
			exceptable = false;
		}
		return exceptable;
	}

	public static boolean validateNew(Element state, Element action) {
		boolean exceptable = true;
		String action_name = action.getAttribute("name");
		String State_name = state.getAttribute("name");
		String [] action_list = action_name.split(":");
		String [] state_list = State_name.split(" ");
		if(action_list.length < 3){
			ErrorMessages.WrongNewActionColons(action_name);
			exceptable = false;
		}
		if(state_list[0].equals("")){
			ErrorMessages.WrongVariableState(State_name);
			exceptable = false;
		}
		return exceptable;
	}

	public static boolean validateAssign(Element state, Element action) {
		boolean exceptable = true;
		String action_name = action.getAttribute("name");
		String State_name = state.getAttribute("name");
		String [] action_list = action_name.split(" ");
		String [] state_list = State_name.split(" ");
		if(action_list.length < 3){
			ErrorMessages.WrongAssignAction(action_name);
			exceptable = false;
		}
		else{
			if (action_list[2].contains(":")){
				action_list = action_list[2].split(":");
				if(action_list.length < 2){
					ErrorMessages.WrongAssignColonAction(action_name);
					exceptable = false;
				}
			}
		}
		if(state_list[0].equals("")){
			
			ErrorMessages.WrongVariableState(State_name);
			exceptable = false;
		}
		return exceptable;
	}

	public static boolean validateSend(Element state, Element action) {
		boolean exceptable = true;
		String action_name = action.getAttribute("name");
		String State_name = state.getAttribute("name");
		String [] action_list = action_name.split(" ");
		String [] state_list = State_name.split(" ");
		if(action_list.length < 3){
			ErrorMessages.WrongSendAction(action_name);
			exceptable = false;
		}
		if(state_list[0].equals("")){
			System.out.print(State_name + " <->" + state_list[1]);
			ErrorMessages.WrongVariableState(State_name);
			exceptable = false;
		}
		return exceptable;
	}

	public static boolean validateReceive(Element state, Element action) {
		boolean exceptable = true;
		String action_name = action.getAttribute("name");
		String State_name = state.getAttribute("name");
		String [] action_list = action_name.split(" ");
		String [] state_list = State_name.split(" ");
		if(action_list.length < 3){
			ErrorMessages.WrongReceiveAction(action_name);
			exceptable = false;
		}
		if(state_list[0].equals("")){
			ErrorMessages.WrongVariableState(State_name);
			exceptable = false;
		}
		return exceptable;
	}
}
