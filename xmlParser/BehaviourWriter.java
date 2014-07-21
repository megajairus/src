package xmlParser;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import structuralModels.BehaviourType;

public class BehaviourWriter {
	
	public static Node insertBehaviours(Document doc, ArrayList<BehaviourType> behaviours){
		Element e_behaviours = doc.createElement("behaviour");
		e_behaviours = findBehaviourType(e_behaviours, behaviours, doc, 0);
		
		return e_behaviours;
	}

	private static Element findBehaviourType(Element currant_element, ArrayList<BehaviourType> behaviours, Document doc, int index) {
		Element added_element= null;
		for (int i = index; i < behaviours.size(); i++){
			
			BehaviourType behaviour = behaviours.get(i);
			if(behaviour.getType().equals("endClose")){
				return currant_element;
			}
			if(behaviour.getType().equals("receive")){
				added_element =  createReceive(doc, behaviour);
			}
			if(behaviour.getType().equals("send")){
				added_element = createSend(doc, behaviour);
			}
			if(behaviour.getType().equals("if")){
				added_element = createIf(doc, behaviours, i);
				i = findNextEnd(behaviours, i);
			}
			if(behaviour.getType().equals("else")){
				added_element = createElse(doc, behaviours, i);
				i = findNextEnd(behaviours, i);
			}
			if(behaviour.getType().equals("for")){
				added_element = createFor(doc, behaviours, i);
				i = findNextEnd(behaviours, i);
			}
			if(behaviour.getType().equals("toDo")){
				added_element  = createToDo(doc, behaviour);
			}
			currant_element.appendChild(added_element);
			
		}
		
		return currant_element;
	}

	private static Element createFor(Document doc,
			ArrayList<BehaviourType> behaviours, int i) {
		BehaviourType behaviour = behaviours.get(i);
		Element for_clause = doc.createElement("for");
		for_clause .setAttribute("initial", behaviour.getAttribute(0));
		for_clause .setAttribute("limit", behaviour.getAttribute(1));
		for_clause = findBehaviourType(for_clause, behaviours, doc, (i + 1));
		return for_clause;
	}

	private static Element createElse(Document doc,
			ArrayList<BehaviourType> behaviours, int i) {
		BehaviourType behaviour = behaviours.get(i);
		Element else_clause = doc.createElement("else");
		else_clause = findBehaviourType(else_clause, behaviours, doc, (i + 1));
		return else_clause;
	}

	private static int findNextEnd(ArrayList<BehaviourType> behaviours, int i) {
		for (int j = i; j < behaviours.size(); j++){
			if(behaviours.get(j).getType().equals("endClose")){
				return j++;
			}
		}
		return 0;
	}

	private static Element createIf(Document doc, ArrayList<BehaviourType> behaviours, int i) {
		BehaviourType behaviour = behaviours.get(i);
		Element if_clause = doc.createElement("if");
		if_clause.setAttribute("clause", behaviour.getAttribute(0));
		if_clause = findBehaviourType(if_clause, behaviours, doc, (i +1));
		return if_clause;
	}

	public static Node findBehaviour(Document doc, BehaviourType behaviour) {
		if(behaviour.getType().equals("receive"))
			return createReceive(doc, behaviour);
		if(behaviour.getType().equals("send"))
			return createSend(doc, behaviour);
		return createToDo(doc, behaviour);
	}

	private static Element createToDo(Document doc, BehaviourType behaviour) {
		Element toDo = doc.createElement("toDo");
		if(behaviour.size() > 0){
			toDo.setAttribute("text", behaviour.getAttribute(0));
		}
		return toDo;
	}

	private static Element createSend(Document doc, BehaviourType behaviour) {
		Element send = doc.createElement("send");
		send.setAttribute("identifier", behaviour.getAttribute(0));
		send.setAttribute("to", behaviour.getAttribute(1));
		return send;
	}

	private static Element createReceive(Document doc, BehaviourType behaviour) {
		Element receive = doc.createElement("receive");
		receive.setAttribute("identifier", behaviour.getAttribute(0));
		receive.setAttribute("from", behaviour.getAttribute(1));
		return receive;
	}

}
