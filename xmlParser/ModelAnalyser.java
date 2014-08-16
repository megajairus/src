package xmlParser;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import structuralModels.BehaviourElement;
import structuralModels.Deployment;
import structuralModels.TempInterConnection;


public class ModelAnalyser {
	private static final String PAPYRUS_UML_STATE_MACHINE_DIAGRAM = "PapyrusUMLStateMachineDiagram";
	private static final String PAPYRUS_UML_SEQUENCE_DIAGRAM = "PapyrusUMLSequenceDiagram";
	private static final String PAPYRUS_UML_CLASS_DIAGRAM = "PapyrusUMLClassDiagram";
	private static final String PAPYRUS_UML_DEPLOYMENT_DIAGRAM = "PapyrusUMLDeploymentDiagram";
	private static StructureData structure = new StructureData();
	private static ArrayList<ActivityData> activity = new ArrayList<ActivityData>();
	private static ArrayList<BehaviourElement>  component_behaviour = new ArrayList<BehaviourElement>();
	private static ArrayList<Deployment> deployments = new ArrayList<Deployment>();
	private static ArrayList<TempInterConnection> temps = new ArrayList<TempInterConnection>();
	public ModelAnalyser(){
	}
	
	public static void main(String argv[]) throws ParserConfigurationException{
		boolean model_excepted;
		File folder = OpenFolder.pickFolderPath();
		setName(folder);
		ArrayList<Document> docs = ReadXMLFiles.readFile(folder, PAPYRUS_UML_CLASS_DIAGRAM);
		model_excepted = StructureExtractor.loadDateFields(docs, structure);
		if (!model_excepted){
			ErrorMessages.cannotContinue();
			return;
		}
		StructurePerfecter.finishStructureData(structure);
		model_excepted = StructureValidation.validateStructureNotation(structure);
		if (!model_excepted){
			ErrorMessages.cannotContinue();
			return;
		}
		docs = ReadXMLFiles.readFile(folder, PAPYRUS_UML_STATE_MACHINE_DIAGRAM);
		model_excepted = StateMachineExtractor.loadBehaviours(docs, structure, component_behaviour);
		if (!model_excepted){
			ErrorMessages.cannotContinue();
			return;
		}
		model_excepted = StateMachineValidation.checkReceiveSendChannels(structure, component_behaviour);
		if (!model_excepted){
			ErrorMessages.cannotContinue();
			return;
		}
		docs = ReadXMLFiles.readFile(folder, PAPYRUS_UML_DEPLOYMENT_DIAGRAM);
		model_excepted = DeploymentExtractor.loadDateFields(docs, deployments, temps);
		if (!model_excepted){
			ErrorMessages.cannotContinue();
			return;
		}
		DeploymentPerfecter.finishPackageDate(structure, deployments, temps);
		
		if(deployments.size() > 0){
			model_excepted = DeploymentValidation.validateDeploymentNotation(structure, deployments);
			if(!model_excepted){
				ErrorMessages.cannotContinue();
				return;
			}
			DeploymentDivision.writeNodes(structure,deployments, component_behaviour);
		}
		else{
			WriteXMLFile.createIntermediateLanguage(structure, component_behaviour, 1);
		}
		System.out.println("----------------------------");
	}

	private static void setName(File folder) {
		String path = folder.getPath();
		String[] folder_names = path.split("\\\\");
		int length = folder_names.length - 1;
		structure.setNodeName(folder_names[length]);
	}
	
	
}
