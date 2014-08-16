package xmlParser;

import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.w3c.dom.NodeList;

import structuralModels.Associate;
import structuralModels.Channel;
import structuralModels.Component;
import structuralModels.ComponentInstance;
import structuralModels.Connection;
import structuralModels.Interface;
import structuralModels.Procedure;
import structuralModels.Struct;
import structuralModels.Variable;

public class StructureExtractor {
	
	private static final String UML_ASSOCIATION = "uml:Association";
	private static final String DIRECTION = "direction";
	private static final String OWNED_PARAMETER = "ownedParameter";
	private static final String UML_OPERATION = "uml:Operation";
	private static final String UML_PROPERTY = "uml:Property";
	private static final String CLASSIFIER = "classifier";
	private static final String UML_INSTANCE_SPECIFICATION = "uml:InstanceSpecification";
	private static final String INTERFACE_REALIZATION = "interfaceRealization";
	private static final String XMI_ID = "xmi:id";
	private static final String ATTRIBUTE = "ownedAttribute";
	private static final String OPERATION = "ownedOperation";
	private static final String UML_CONNECTION = "uml:Dependency";
	private static final String UML_COMPONENT = "uml:Component";
	private static final String UML_INTERFACE = "uml:Interface";
	private static final String UML_CLASS = "uml:Class";
	private static final String XMI_TYPE = "xmi:type";
	private static final String PACKAGED_ELEMENT = "packagedElement";
	
	public static boolean loadDateFields(ArrayList<org.w3c.dom.Document>  docs, StructureData structure){
		boolean exceptable = true;
		for(int documentIndex = 0; documentIndex < docs.size(); documentIndex++){
		NodeList node_list = docs.get(documentIndex).getElementsByTagName(PACKAGED_ELEMENT);
		for (int i = 0; i < node_list.getLength(); i++) {
			Node node = node_list.item(i);
	 
	 
			if (node.getNodeType() == Node.ELEMENT_NODE) {
	 
				Element eElement = (Element) node;
				
				if (eElement.getAttribute(XMI_TYPE).equals(UML_COMPONENT)){
						structure.addComponent(parseComponent(eElement));
					}
				if (eElement.getAttribute(XMI_TYPE).equals(UML_INTERFACE)){
					if(interfaceNotationAccepted(eElement)){
						structure.addInterface(parseInterface(eElement));
					}
					else{
						exceptable = false;
					}
				}
				if (eElement.getAttribute(XMI_TYPE).equals(UML_CONNECTION)){
					if(coonectionNotationAccepted(eElement)){
						structure.addConnection(parseConnection(eElement));
					}
					else{
						exceptable=  false;
					}
				}
				if (eElement.getAttribute(XMI_TYPE).equals(UML_CLASS)){
					structure.addStruct(parseStruct(eElement));
				}
				if (eElement.getAttribute(XMI_TYPE).equals(UML_INSTANCE_SPECIFICATION)){
					structure.addInstance(parseInstance(eElement));
				}
				if (eElement.getAttribute(XMI_TYPE).equals(UML_ASSOCIATION)){
					structure.addAssociate(parseAssociate(eElement));
				}
			}
		}
		}
		if (!StructureValidation.checkDependencies(structure)){
			exceptable = false;
		}
		return exceptable;
	}


	private static boolean coonectionNotationAccepted(Element connect) {
		String name = connect.getAttribute("name");
		if(!StructureValidation.connectionNameNotation(name)){
			return false;
		}
		return true;
	}


	private static boolean interfaceNotationAccepted(Element inter) {
		boolean acceptable = true;
		String inter_name = inter.getAttribute("name");
		NodeList attributes = inter.getElementsByTagName(ATTRIBUTE);
		for (int i = 0; i < attributes.getLength(); i++) {
			Node node = attributes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				Variable variable = parsePropertyType(element);
				if (!StructureValidation.channelNameNotation(variable.getName(), inter_name)){
					acceptable = false;
				}
			}
		}
		return acceptable;
	}


	private static Associate parseAssociate(Element eElement) {
		String name = eElement.getAttribute("name");
		String id = eElement.getAttribute(XMI_ID);
		return new Associate(id, name);
	}


	private static ComponentInstance parseInstance(Element eElement) {
		String name = eElement.getAttribute("name");
		String id = eElement.getAttribute(XMI_ID);
		String component = eElement.getAttribute(CLASSIFIER);
		return new ComponentInstance(name, id, component);
	}


	private static Struct parseStruct(Element struct) {
		String struct_name = struct.getAttribute("name");
		struct_name = struct_name.replaceAll("\\s", "");
		String struct_id = struct.getAttribute(XMI_ID);
		ArrayList<Variable> variables = new ArrayList<Variable>();
		NodeList attributes = struct.getElementsByTagName(ATTRIBUTE);
		for (int i = 0; i < attributes.getLength(); i++) {
			Node node = attributes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				variables.add(parsePropertyType(element));
			}
			
		}
		return (new Struct(struct_name, struct_id, variables));
		
	}

	private static Variable parsePropertyType(Element element) {
		String name, type;
		name = element.getAttribute("name");
		type = element.getAttribute("type");
		if(type.isEmpty()){
			NodeList attributes = element.getElementsByTagName("type");
			if (attributes.getLength() > 0){
				Element e_type = (Element) attributes.item(0);
				type = translateType(e_type);
				return new Variable(name, type, false);
			}
			else{
				return new Variable(name, "any", false);
			}
		}
		else{
			
			return new Variable(name, type, true);
		}
	}
	
	


	private static String translateType(Element e_type) {
		String temp_type;
		String type = "any";
		String primitive_type = e_type.getAttribute("href");
		String[] type_array = primitive_type.split("#");
		temp_type = type_array[1];
		switch(temp_type){
		case "Integer": type = "integer"; break;
		case "UnlimitedNatural": type = "unsigned"; break;
		case "Boolean": type = "bool"; break;
		case "Real": type = "real"; break;
		case "String": type = "string"; break;
		}
			
		return type;
	}

	private static Connection parseConnection(Element connect) {
		String name = connect.getAttribute("name");
		String client_id = connect.getAttribute("client");
		String supplier_id = connect.getAttribute("supplier");
		String[] channel_array = name.split(":");
		String out_channel = channel_array[0].replaceAll(" ",  "");
		String in_channel = channel_array[1].replaceAll(" ",  "");
		Connection new_connection = new Connection(in_channel, out_channel, supplier_id, client_id);
		return new_connection;
		
	}

	private static Interface parseInterface(Element inter) {
		String inter_name = inter.getAttribute("name");
		inter_name = inter_name.replaceAll("\\s", "");
		String inter_id = inter.getAttribute(XMI_ID);
		ArrayList<Channel> channels = new ArrayList<Channel>();
		NodeList attributes = inter.getElementsByTagName(ATTRIBUTE);
		for (int i = 0; i < attributes.getLength(); i++) {
			Node node = attributes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				Variable variable = parsePropertyType(element);
				String[] name_array = variable.getName().split(":");
				String component_name  = name_array[0].replaceAll("\\s", "");
				String channel_name = name_array[1].replaceAll("\\s", "");
				channels.add(new Channel(component_name, variable.getType(), variable.getPointer(), channel_name));
			}
			
		}
		return (new Interface(inter_name, inter_id, channels));
	}

	private static Component parseComponent(Element component) {
		String component_name = component.getAttribute("name");
		component_name = component_name.replaceAll("\\s", "");
		String component_id = component.getAttribute(XMI_ID);
		ArrayList<Variable> variables = new ArrayList<Variable>();
		ArrayList<String> presents = new ArrayList<String>();
		ArrayList<Procedure> procedures = new ArrayList<Procedure>();
		NodeList attributes = component.getElementsByTagName(ATTRIBUTE);
		for (int i = 0; i < attributes.getLength(); i++) {
			Node node = attributes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				if (element.getAttribute(XMI_TYPE).equals(UML_PROPERTY)){
					variables.add(parsePropertyTypeForComponent(element));
				}
			}
		
		}
		NodeList operations = component.getElementsByTagName(OPERATION);
		for (int i = 0; i < operations.getLength(); i++) {
			Node node = operations.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				if (element.getAttribute(XMI_TYPE).equals(UML_OPERATION)){
					procedures.add(parseProcedure(element));
				}
			}
		
		}
		NodeList node_presents = component.getElementsByTagName(INTERFACE_REALIZATION);
		for (int i = 0; i < node_presents.getLength(); i++) {
			Node node = node_presents.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				presents.add(element.getAttribute("supplier"));
			}
		}
		return (new Component(component_name, component_id, presents, variables, procedures));
	}


	private static Variable parsePropertyTypeForComponent(Element element) {
			String name, type;
			name = element.getAttribute("name");
			type = element.getAttribute("type");
			if(type.isEmpty()){
				NodeList attributes = element.getElementsByTagName("type");
				if (attributes.getLength() > 0){
					Element e_type = (Element) attributes.item(0);
					type = "";
					return new Variable(name, type, false);
				}
				else{
					return new Variable(name, "any", false);
				}
			}
			else{
				
				return new Variable(name, type, true);
			}
		}
	


	private static Procedure parseProcedure(Element element) {
		ArrayList<Variable> variables = new ArrayList<Variable>();
		Variable returnType = null;
		NodeList nodes = element.getElementsByTagName(OWNED_PARAMETER);
		for(int i = 0; i < nodes.getLength(); i++){
			Element parameter = (Element) nodes.item(i);
			if (parameter.getAttribute(DIRECTION).equals("out")){
				returnType = parsePropertyType(parameter);
			}
			else{
				variables.add(parsePropertyType(parameter));
			}
		}
		String name = element.getAttribute("name");
		return new Procedure(name, variables, returnType);
	}

}
