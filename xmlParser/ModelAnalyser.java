package xmlParser;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import structuralModels.BehaviourElement;


public class ModelAnalyser {
	private static final String PAPYRUS_UML_STATE_MACHINE_DIAGRAM = "PapyrusUMLStateMachineDiagram";
	private static final String PAPYRUS_UML_SEQUENCE_DIAGRAM = "PapyrusUMLSequenceDiagram";
	private static final String PAPYRUS_UML_CLASS_DIAGRAM = "PapyrusUMLClassDiagram";
	private static StructureData structure = new StructureData();
	private static ArrayList<ActivityData> activity = new ArrayList<ActivityData>();
	private static ArrayList<BehaviourElement>  component_behaviour = new ArrayList<BehaviourElement>();
	public ModelAnalyser(){
	}
	
	public static void main(String argv[]) throws ParserConfigurationException{
		boolean model_excepted;
		File folder = OpenFolder.pickFolderPath();
		ArrayList<Document> docs = ReadXMLFiles.readFile(folder, PAPYRUS_UML_CLASS_DIAGRAM);
		model_excepted = StructureExtractor.loadDateFields(docs, structure);
		if (!model_excepted){
			ErrorMessages.cannotContinue();
			return;
		}
		StructurePerfecter.finishStructureData(structure);
		StructureValidation.validateStructureNotation(structure);
		docs = ReadXMLFiles.readFile(folder, PAPYRUS_UML_STATE_MACHINE_DIAGRAM);
		StateMachineExtractor.loadBehaviours(docs, structure, component_behaviour);
		WriteXMLFile.createIntermediateLanguage(structure, component_behaviour);
		System.out.println("----------------------------");
	}
	
	
}
