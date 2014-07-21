package xmlParser;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import structuralModels.BehaviourType;

public class SequenceExtractor {

	private static final String INTERACTION_OPERATOR = "interactionOperator";
	private static final String XMI_TYPE = "xmi:type";
	private static final String UML_COMBINED_FRAGMENT = "uml:CombinedFragment";
	private static final String COVERED = "covered";
	private static final String SEND_EVENT = "sendEvent";
	private static final String XMI_ID = "xmi:id";
	private static final String RECEIVE_EVENT = "receiveEvent";
	private static final String MESSAGE = "message";
	private static final String LIFELINE = "lifeline";
	private static final String FRAGMENT = "fragment";

	public static void loadBehaviours(ArrayList<Document> docs, StructureData structure) {
		for(int documentIndex = 0; documentIndex < docs.size(); documentIndex++){
			NodeList fragments = docs.get(documentIndex).getElementsByTagName(FRAGMENT);
			NodeList life_lines = docs.get(documentIndex).getElementsByTagName(LIFELINE);
			NodeList messages = docs.get(documentIndex).getElementsByTagName(MESSAGE);
			searchLifeLines(structure, fragments, life_lines, messages);
			}
		
	}

	private static void searchLifeLines(StructureData structure,
			NodeList fragments, NodeList life_lines, NodeList messages) {
		for(int i = 0; i < life_lines.getLength(); i++){
			Element life = (Element) life_lines.item(i);
			String name = life.getAttribute("name");
			name = name.replaceAll("\\s", "");
			String life_id = life.getAttribute(XMI_ID);
			if(structure.componentExists(name)){
				ArrayList<BehaviourType> behaviours = new ArrayList<BehaviourType>();
				searchFragments(fragments, messages, life_id, behaviours);
				int index = structure.getCompoentIndex(name);
				structure.setComponentBehaviour(index, behaviours);
			}
		}
	}

	private static void searchFragments(NodeList fragments, NodeList messages,
			String life_id, ArrayList<BehaviourType> behaviours) {
		int skip = 0;
		for(int j =0; j < fragments.getLength(); j++){
			if (skip > 0){
				skip--;
			}
			else{
				Element fragment = (Element) fragments.item(j);
				String [] covered= getAllCovered(fragment.getAttribute(COVERED));
				if(containsString(life_id, covered)){
					if (fragment.getAttribute(XMI_TYPE).equals(UML_COMBINED_FRAGMENT)){
						skip = parseCombined(fragment, messages, life_id, behaviours);
					}
					else{
						String message_id = fragment.getAttribute("message");
						String fragment_id = fragment.getAttribute(XMI_ID);
						searchMessages(messages, behaviours,
								fragment, message_id, fragment_id);	
					}
				}
			}
		}
	}

	private static int parseCombined(Element fragment, NodeList messages,
			String life_id, ArrayList<BehaviourType> behaviours) {
		ArrayList<String> attr = new ArrayList<String>();
		int skip = 0;
		String type = fragment.getAttribute(INTERACTION_OPERATOR);
		if (type.equals("alt")){
			NodeList  operands = fragment.getElementsByTagName("operand");
			if (operands.getLength() > 0){
				Element if_clause = (Element) operands.item(0);
				attr.add(if_clause.getAttribute("name"));
				behaviours.add(new BehaviourType("if", attr));
				NodeList new_fragments = if_clause.getElementsByTagName(FRAGMENT);
				searchFragments(new_fragments, messages, life_id, behaviours);
				skip = skip + new_fragments.getLength();
				behaviours.add(new BehaviourType("endClose", new ArrayList<String>()));
			}
			if (operands.getLength() > 1){
				Element else_clause = (Element) operands.item(1);
				behaviours.add(new BehaviourType("else", new ArrayList<String>()));
				NodeList new_fragments = else_clause.getElementsByTagName(FRAGMENT);
				searchFragments(new_fragments, messages, life_id, behaviours);
				skip = skip + new_fragments.getLength();
				behaviours.add(new BehaviourType("endClose", new ArrayList<String>()));
			}
		
		}
		if(type.equals("loop")){
			NodeList  operands = fragment.getElementsByTagName("operand");
			Element if_clause = (Element) operands.item(0);
			String [] expressions = (fragment.getAttribute("name")).split(":");
			attr.add(expressions[0]);
			attr.add(expressions[1]);
			behaviours.add(new BehaviourType("for", attr));
			NodeList new_fragments = if_clause.getElementsByTagName(FRAGMENT);
			searchFragments(new_fragments, messages, life_id, behaviours);
			skip = skip + new_fragments.getLength();
			behaviours.add(new BehaviourType("endClose", new ArrayList<String>()));
				
		}
		return skip;
	}

	private static boolean containsString(String life_id, String[] covered) {
		for(int i = 0; i< covered.length; i++){
			if(covered[i].equals(life_id)){
				return true;
			}
		}
		return false;
	}

	private static String[] getAllCovered(String attribute) {
		String[] covered = attribute.split(" ");
		return covered;
	}

	private static void searchMessages(NodeList messages, ArrayList<BehaviourType> behaviours, Element fragment, String message_id, String fragment_id) {
		for(int k = 0; k < messages.getLength(); k++){
			Element message = (Element) messages.item(k);
			if (message.getAttribute(XMI_ID).equals(message_id)){
				ArrayList<String> attr = new ArrayList<String>();
				attr.add(message.getAttribute("name"));
				attr.add(fragment.getAttribute("name"));
				if (message.getAttribute(RECEIVE_EVENT).equals(fragment_id)){
					behaviours.add(new BehaviourType("receive", attr));
				}
				if (message.getAttribute(SEND_EVENT).equals(fragment_id)){
					behaviours.add(new BehaviourType("send", attr));
				}
			}
		}
	}

}
