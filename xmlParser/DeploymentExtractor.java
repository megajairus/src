package xmlParser;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import structuralModels.Artifact;
import structuralModels.Deployment;
import structuralModels.TempInterConnection;

public class DeploymentExtractor {
private static final String NESTED_CLASSIFIER = "nestedClassifier";
private static final String UML_DEPENDENCY = "uml:Dependency";
private static final String XMI_ID = "xmi:id";
private static final String UML_NODE = "uml:Node";
private static final String PACKAGED_ELEMENT = "packagedElement";
private static final String XMI_TYPE = "xmi:type";
private static boolean exceptable;
public static boolean loadDateFields(ArrayList<Document> docs,
ArrayList<Deployment> deployments,
	ArrayList<TempInterConnection> temps) {
	setExceptable(true);
	for(int documentIndex = 0; documentIndex < docs.size(); documentIndex++){
		NodeList node_list = docs.get(documentIndex).getElementsByTagName(PACKAGED_ELEMENT);
		for (int i = 0; i < node_list.getLength(); i++) {
			Node node = node_list.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				
				Element eElement = (Element) node;
				
				if (eElement.getAttribute(XMI_TYPE).equals(UML_NODE)){
					deployments.add(parseDevise(eElement));
				}
				if (eElement.getAttribute(XMI_TYPE).equals(UML_DEPENDENCY)){
					temps.add(parseConnections(eElement));
				}
			}
		}
	}
	return exceptable;
}

private static TempInterConnection parseConnections(Element element) {
	String type = element.getAttribute("name");
	if(!DeploymentValidation.checkConnectionName(type)){
		setExceptable(false);
	}
	String client_id = element.getAttribute("client");
	String supplier_id = element.getAttribute("supplier");
	TempInterConnection temp = new TempInterConnection(type, supplier_id, client_id);
	return temp;
}

private static Deployment parseDevise(Element element) {
	String name = element.getAttribute("name");
	Deployment pack = new Deployment(name);
	NodeList nodes = element.getElementsByTagName(NESTED_CLASSIFIER);
	for (int i = 0; i < nodes.getLength(); i++) {
		Node node = nodes.item(i);
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element arts = (Element) node;
			name = arts.getAttribute("name");
			String id = arts.getAttribute(XMI_ID);
			pack.addArtifact(new Artifact(name, id));
		}
	}
	return pack;
}

public static boolean isExceptable() {
	return exceptable;
}

public static void setExceptable(boolean exceptable) {
	DeploymentExtractor.exceptable = exceptable;
}



}