package xmlParser;


import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import structuralModels.BehaviourElement;
import structuralModels.BehaviourType;
import structuralModels.Component;
import structuralModels.ComponentInstance;
import structuralModels.Connection;
import structuralModels.Interface;
import structuralModels.Struct;

public class WriteXMLFile {


	public static void createIntermediateLanguage(StructureData structure, ArrayList<BehaviourElement> component_behaviour){
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("xsd:schema");
			rootElement.setAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
			doc.appendChild(rootElement);
			
			for(int i = 0; i < structure.structSize(); i++){
				rootElement.appendChild(structNode(doc, structure.getStructs(i)));
			}
			for(int i = 0; i < structure.interfaceSize(); i++){
				rootElement.appendChild(interfaceNode(doc, structure.getInterfaces(i)));
			}
			for(int i = 0 ; i < structure.componetSize(); i++){
				rootElement.appendChild(componentNode(doc, structure.getComponents(i), component_behaviour));
			}
			for(int i =0 ; i < structure.instanceSize(); i++){
				rootElement.appendChild(instanceNode(doc, structure.getInstance(i)));
			}
			Element e_connection = doc.createElement("connect");
			for(int i =0 ; i < structure.connectionSize(); i++){
				e_connection.appendChild(connectionNodeTo(doc, structure.getConnections(i)));
				e_connection.appendChild(connectionNodeFrom(doc, structure.getConnections(i)));
			}
			
			rootElement.appendChild(e_connection);
			saveXML(doc);
		
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		  } catch (TransformerException tfe) {
			tfe.printStackTrace();
		  }
	
}
	
	private static Node instanceNode(Document doc, ComponentInstance instance) {
		Element e_instance = doc.createElement("instance");
		e_instance.setAttribute("name", instance.getInstanceName());
		e_instance.setAttribute("component", instance.getComponentName());
		return e_instance;
	}
	private static void saveXML(Document doc)
			throws TransformerFactoryConfigurationError,
			TransformerConfigurationException, TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File("C:\\Users\\User\\workspace\\file3.xml"));
		StreamResult console = new StreamResult(System.out);
		 transformer.transform(source, console);
		transformer.transform(source, result);
	}
	private static Node connectionNodeFrom(Document doc, Connection connections) {
		Element e_from = doc.createElement("from");
		e_from.setAttribute("name", connections.getFromComponent());
		e_from.setAttribute("on", connections.getOutChannel());
		return e_from;
	}
	private static Node connectionNodeTo(Document doc, Connection connections) {
		Element e_to = doc.createElement("to");
		
		e_to.setAttribute("name", connections.getToComponent());
		e_to.setAttribute("on", connections.getInChannel());
		return e_to;
	}
	private static Node componentNode(Document doc, Component component, ArrayList<BehaviourElement> component_behaviour) {
		Element e_components= doc.createElement("component");
		Element e_attribute= doc.createElement("attribute");
		e_attribute.setAttribute("name", component.getName());
		e_components.appendChild(e_attribute);
		for (int i =0; i < component.presentsSize(); i++){
			Element e_presents = doc.createElement("presents");
			e_presents.setAttribute("name", component.getPresents(i));
			e_components.appendChild(e_presents);
		}
		for (int i = 0; i < component.variablesSize(); i++){
			Element e_variable = doc.createElement("field");
			if(component.getVariableName(i).contains("=")){
				String [] names = component.getVariableName(i).split("=");
				String name = names[0].replaceAll("\\s", "");
				String value = names[1].replaceAll("\\s", "");
				e_variable.setAttribute("name", name);
				e_variable.setAttribute("value", value);
			}
			else{
				e_variable.setAttribute("name", component.getVariableName(i));
				
			}
			e_variable.setAttribute("type", component.getVariableType(i));
			e_components.appendChild(e_variable);
		}
		e_attribute = doc.createElement("constructor");
		e_components.appendChild(e_attribute);
		
		
		e_components.appendChild(assignBehaviour(component.getName(), component_behaviour, doc));
		return e_components;
	}
	
	private static Node assignBehaviour(String name, ArrayList<BehaviourElement> component_behaviour, Document doc) {
		System.out.println(name);
		for(int i = 0; i < component_behaviour.size(); i++){
			if (component_behaviour.get(i).getOwner().equals(name)){
				Node node = doc.importNode(component_behaviour.get(i).getElement(), true);
				return node;
			}
		}
		Element behaviour = doc.createElement("behaviour");
		return behaviour;
	}
	private static Node insertBehabiours(Document doc, ArrayList<BehaviourType> behaviours) {
		Element e_behaviours = doc.createElement("behaviour");
		for (int i = 0; i < behaviours.size(); i++){
			e_behaviours.appendChild(BehaviourWriter.findBehaviour(doc, behaviours.get(i)));
		}
		return e_behaviours;
	}
	private static Node interfaceNode(Document doc, Interface inter) {
		Element e_interface= doc.createElement("interface");
		Element e_attribute= doc.createElement("attribute");
		e_attribute.setAttribute("name", inter.getName());
		e_interface.appendChild(e_attribute);
		for (int i =0; i < inter.size(); i++){
			Element e_channel = doc.createElement("channel");
			e_channel.setAttribute("name", inter.getChannelName(i));
			e_channel.setAttribute("type", inter.getChannelType(i));
			e_channel.setAttribute("direction", inter.getChannelDirection(i));
			e_interface.appendChild(e_channel);
		}
		return e_interface;
	}
	private static Node structNode(Document doc, Struct struct) {
		Element e_struct= doc.createElement("struct");
		Element e_attribute= doc.createElement("attribute");
		e_attribute.setAttribute("name", struct.getName());
		e_struct.appendChild(e_attribute);
		for (int i =0; i < struct.variablesSize(); i++){
			Element e_variable = doc.createElement("field");
			e_variable.setAttribute("name", struct.getVariableName(i));
			e_variable.setAttribute("type", struct.getVariableType(i));
			e_struct.appendChild(e_variable);
		}
		return e_struct;
	}
	
	}