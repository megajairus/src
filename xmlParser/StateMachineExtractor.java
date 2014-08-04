package xmlParser;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import structuralModels.BehaviourElement;
import structuralModels.BehaviourType;

public class StateMachineExtractor {

	private static final String XMI_ID = "xmi:id";
	private static final String SOURCE = "source";
	private static final String UML_PSEUDOSTATE = "uml:Pseudostate";
	private static final String XMI_TYPE = "xmi:type";
	private static final String PACKAGED_ELEMENT = "packagedElement";
	private static final String TRANSITION = "transition";
	private static final String SUBVERTEX = "subvertex";
	private static  boolean exceptable;
	private static Element saved_state;
	private static Element saved_action;
	private static boolean skip_clause;
	public static boolean loadBehaviours(ArrayList<Document> docs, StructureData structure, ArrayList<BehaviourElement> component_behaviour) throws ParserConfigurationException {
		setExceptable(true);
		for(int documentIndex = 0; documentIndex < docs.size(); documentIndex++){
			NodeList machines = docs.get(documentIndex).getElementsByTagName(PACKAGED_ELEMENT);
			for(int i =0; i < machines.getLength(); i++){
				Element e_machine = (Element) machines.item(i);
				if(structure.componentExists(e_machine.getAttribute("name"))){
					setSavedState(null);
					setSkipClause(false);
					DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
					Document doc = docBuilder.newDocument();
					NodeList states = docs.get(documentIndex).getElementsByTagName(SUBVERTEX);
					NodeList transitions = docs.get(documentIndex).getElementsByTagName(TRANSITION);
					Element start_state = findStart(states);
					Element new_element = doc.createElement("behaviour");
					Element next_action = getNextAction(transitions, start_state);
					Element next_state = getNextState(states, next_action);
					if (next_state == null || next_action == null){
						return getExceptable();
					}
					new_element = (trackBehaviours(states, transitions, next_state, next_action, new_element, doc));
					component_behaviour.add(new BehaviourElement(new_element, e_machine.getAttribute("name")));
				}
			}
		}
		return getExceptable();
	}

	private static Element trackBehaviours(NodeList states, NodeList transitions, Element state, Element action,Element parent,  Document doc) {
		while(!state.getAttribute(XMI_TYPE).equals("uml:FinalState")){
			if(state.getAttribute("kind").equals("join")){
				action = getNextAction(transitions, state);
				if (action == null){
					return parent;
				}
				state = getNextState(states, action);
				if (state == null || action == null){
					return parent;
				}
				setSavedAction(action);
				setSavedState(state);
				return parent;
			}
			parent.appendChild(parseState(action, state, doc, transitions, states));
			action = getNextAction(transitions, state);
			if (action == null){
				return parent;
			}
			state = getNextState(states, action);
			if (state == null || action == null){
				return parent;
			}
			
		}
		return parent;
	}

	

	private static Element parseState(Element action, Element state, Document doc, NodeList transitions, NodeList states) {
		String [] action_list = action.getAttribute("name").split(" ");
		if (action_list[0].equals("receive")){
			if(StateMachineValidation.validateReceive(state, action)){
				return parseReceiveAction(action_list, state, doc);
			}
			else{setExceptable(false);}
		}
		if (action_list[0].equals("send")){
			if(StateMachineValidation.validateSend(state, action)){
				return parseSendAction(action_list, state, doc);
			}
			else {setExceptable(false);}
		}
		if (action_list[0].equals("assign")){
			if(StateMachineValidation.validateAssign(state, action)){
				return parseAssignAction(action_list, state, doc);
			}
			else{setExceptable(false);}
		}
		if (action_list[0].equals("new")){
			if(StateMachineValidation.validateNew(state, action)){
				return parseNewStructAction(action, state, doc);
			}
			else{setExceptable(false);}
		}
		if (action_list[0].equals("print")){
			if(StateMachineValidation.validatePrint(state, action)){
				return parsePrintAction(action_list, state, doc);
			}
			else{setExceptable(false);;}
		}
		if (action_list[0].equals("if")){
			if(StateMachineValidation.validateIf(state, action)){
				return parseIfAction(action_list, state, doc, transitions, states);
			}
			else{setExceptable(false);}
		}
		if (action_list[0].equals("for")){
			if(StateMachineValidation.validateFor(state, action)){
				return parseForAction(action_list, state, doc, transitions, states);
			}
			else{setExceptable(false);}
		}
		return parseComment(state, doc);
	}

	private static Element parseForAction(String[] action_list, Element state,
			Document doc, NodeList transitions, NodeList states) {
		String name = state.getAttribute("name");
		String []for_part = name.split(":");
		String [] identifier = for_part[0].split("=");
		Element element = doc.createElement("for_clause");
		element.setAttribute("identifier", identifier[0]);
		element.setAttribute("value", identifier[1]);
		element.setAttribute("limit", for_part[1]);
		Element next_action = getNextSecondAction(transitions,states, state, "then");
		if (next_action == null){
			return element;
		}
		Element next_state = getNextState(states, next_action);
		if (next_state == null){
			return element;
		}
		element = (trackBehaviours(states, transitions, next_state, next_action, element, doc));
		setSkipClause(true);
		return element;
	}

	private static Element parseIfAction(String[] action_list, Element state,
			Document doc, NodeList transitions, NodeList states) {
		String name = state.getAttribute("name");
		Element element = doc.createElement("if_clause");
		element.setAttribute("clause", name);
		Element next_action = getNextSecondAction(transitions, states, state, "then");
		if (next_action == null){
			return element;
		}
		Element next_state = getNextState(states, next_action);
		if (next_state == null){
			return element;
		}
		Element then_element = doc.createElement("then");
		then_element = (trackBehaviours(states, transitions, next_state, next_action, then_element, doc));
		element.appendChild(then_element);
		next_action = getNextSecondAction(transitions, states,  state, "else");
		if ( next_action == null){
			return element;
		}
		next_state = getNextState(states, next_action);
		if(next_state == null){
			return element;
		}
		Element else_element = doc.createElement("else");
		else_element = (trackBehaviours(states, transitions, next_state,next_action, else_element, doc));
		element.appendChild(else_element);
		setSkipClause(true);
		return element;
	}

	

	private static Element parseComment(Element state, Document doc) {
		String name = state.getAttribute("name");
		Element element = doc.createElement("comment");
		element.setAttribute("text", name);
		return element;
	}

	private static Element parsePrintAction(String[] action_list,
		Element state, Document doc) {
		String state_name = state.getAttribute("name");
		String [] state_list = state_name.split(" ");
		state_list = state_list[0].split("\\.");
		Element element = doc.createElement("print");
		if (action_list.length > 1){
			element.setAttribute("type", action_list[1]);
		}
		else{
			element.setAttribute("type", "");
		}
		if(state_name.contains("\"")){
			element.setAttribute("variable", parseQuotes(state_name));
		}
		else{
			element.setAttribute("variable", state_list[0]);
		}
		return element;
	}
	
	private static String parseQuotes(String name) {
		int start;
		int end;
		start = name.indexOf("\"");
		end = name.indexOf("\"", (start + 1));
		return ("'" + name.substring(start+1, end) + "'");
	}

	private static Element parseNewStructAction(Element action,
			Element state, Document doc) {
		String [] action_list = action.getAttribute("name").split(":");
		String [] state_list = state.getAttribute("name").split(" ");
		Element element = doc.createElement("variable");
		element.setAttribute("type", action_list[1]);
		Element attribute = doc.createElement("attribute");
		attribute.setAttribute("name", state_list[0]);
		element.appendChild(attribute);
		attribute = doc.createElement("attribute");
		attribute.setAttribute("allocation", "dynamic");
		element.appendChild(attribute);
		String [] parameters = action_list[2].split(",");
		for (int i = 0; i < parameters.length; i++){
			Element param = doc.createElement("parameter");
			param.setAttribute("name", parameters[i].replaceAll(" ", ""));
			element.appendChild(param);
		}
		return element;
	}

	private static Element parseAssignAction(String[] action_list,
			Element state, Document doc) {
		String [] state_list = state.getAttribute("name").split(" ");
		Element element = doc.createElement("variable");
		element.setAttribute("name", state_list[0]);
		element.setAttribute("allocation", "static");
		element.setAttribute("bindingTo", action_list[2]);
		return element;
	}

	private static Element parseSendAction(String[] action_list,
			Element state, Document doc) {
		String [] state_list = state.getAttribute("name").split(" ");
		Element element = doc.createElement("send");
		String [] identifiers = state_list[0].split(":");
		element.setAttribute("identifier", identifiers[0]);
		if(identifiers.length > 1){
			element.setAttribute("value", identifiers[1]);
		}
		element.setAttribute("on", action_list[2]);
		return element;
	}

	private static Element parseReceiveAction(String[] action_list,
			Element state, Document doc) {
		String [] state_list = state.getAttribute("name").split(" ");
		Element element = doc.createElement("receive");
		String [] identifiers = state_list[0].split(":");
		element.setAttribute("identifier", identifiers[0]);
		if(identifiers.length > 1){
			element.setAttribute("value", identifiers[1]);
		}
		element.setAttribute("from", action_list[2]);
		
		return element;
	}
	
	private static Element getNextAction(NodeList transitions, Element currant_state) {
		Element state;
		if(isSkipClause()){
			state = getSavedState();
			if(!StateMachineValidation.checkTransitionAmount(transitions, state)){
				setExceptable(false);
			}
			return getSavedAction();
		}
		if(!StateMachineValidation.checkTransitionAmount(transitions, currant_state)){
			setExceptable(false);
		}
		for(int i =0; i < transitions.getLength(); i++){
			Element line = (Element) transitions.item(i);
			if(line.getAttribute(SOURCE).equals(currant_state.getAttribute(XMI_ID))){
				if (!line.getAttribute("name").equals("then") &&  !line.getAttribute("name").equals("else")){
					return line;
				}
			}
		}
		System.out.println("c");
		return null;
	}

	private static Element getNextState(NodeList states, Element action) {
		if(isSkipClause()){
			System.out.println("inside this loop");
			Element new_state = getSavedState();
			setSavedState(null);
			setSkipClause(false);
			return new_state;
		}
		for(int j = 0; j < states.getLength(); j++){
			Element new_state = (Element) states.item(j);
			if(action.getAttribute("target").equals(new_state.getAttribute(XMI_ID))){
				return new_state;
			}
		}
		return null;
		
	}
	
	private static Element getNextSecondAction(NodeList transitions, NodeList states, Element state,
			String string) {
		for(int i =0; i < transitions.getLength(); i++){
			Element line = (Element) transitions.item(i);
			if(line.getAttribute(SOURCE).equals(state.getAttribute(XMI_ID))){
				if (line.getAttribute("name").equals(string)){
					state = getNextState(states, line);
					return getNextAction(transitions, state);
				}
			}
		}
		return null;
	}

	private static Element findStart(NodeList states) {
		for(int i = 0; i < states.getLength(); i++){
			Element state = (Element) states.item(i);
			if(state.getAttribute(XMI_TYPE).equals(UML_PSEUDOSTATE)){
				if(state.getAttribute("kind").equals("")){
					return state;
				}
			}
		}
		return null;
	}

	public static boolean getExceptable() {
		return exceptable;
	}

	public static void setExceptable(boolean exceptable) {
		StateMachineExtractor.exceptable = exceptable;
	}

	public static Element getSavedState() {
		return saved_state;
	}

	public static void setSavedState(Element saved_element) {
		StateMachineExtractor.saved_state = saved_element;
	}

	public static boolean isSkipClause() {
		return skip_clause;
	}

	public static void setSkipClause(boolean skip_clause) {
		StateMachineExtractor.skip_clause = skip_clause;
	}

	public static Element getSavedAction() {
		return saved_action;
	}

	public static void setSavedAction(Element saved_action) {
		StateMachineExtractor.saved_action = saved_action;
	}

}
