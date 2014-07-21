package structuralModels;

import org.w3c.dom.Element;

public class BehaviourElement {
	private Element element;
	private String onwer;
	
	public BehaviourElement(Element element,  String onwer){
		this.element = element;
		this.onwer = onwer;
	}
	
	public Element getElement(){
		return element;
	}
	
	public String getOwner(){
		return onwer;
	}

}
