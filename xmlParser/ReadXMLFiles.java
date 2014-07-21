 package xmlParser;



import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.awt.Component;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
 
public class ReadXMLFiles {
private static final String NOTATION_DIAGRAM = "notation:Diagram";


  
  public static ArrayList<Document> readFile(File folder, String model_type){
	  try {
		  File[] listOfFiles= readNotationFiles(folder);
		  ArrayList<String> file_names = findDesiredFileNames(listOfFiles, model_type);
		  ArrayList<Document> docs = readXLMStructureFiles(file_names, folder);
			return docs;
		    } catch (Exception e) {
			e.printStackTrace();
			return null;
		    }
	 
  }

private static File[] readNotationFiles(File folder) {
	FilenameFilter filter = new FilenameFilter() {
	    public boolean accept(File dir, String name) {
	        return name.endsWith(".notation");
	    }
	    
	};

	File[] listOfFiles = folder.listFiles(filter);
    return listOfFiles;
	
}

private static ArrayList<String> findDesiredFileNames(File[] listOfFiles, String model_type) throws ParserConfigurationException, SAXException, IOException {
	ArrayList<String> file_names = new ArrayList<String>();
	for(int i = 0; i< listOfFiles.length;i++){
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(listOfFiles[i]);
			doc.getDocumentElement().normalize();
			NodeList node_list = doc.getElementsByTagName(NOTATION_DIAGRAM);
			Element element = (Element) node_list.item(0);
			if(element.getAttribute("type").equals(model_type)){
				file_names.add(replaceExtention(listOfFiles[i].getName()));
			}
		}
	
	return file_names;
}

private static ArrayList<Document> readXLMStructureFiles(ArrayList<String> file_names,
		File folder) throws ParserConfigurationException,
		SAXException, IOException {
	ArrayList<Document> docs = new ArrayList<Document>();
	for(int i = 0; i< file_names.size();i++){
		File xml_file = new File(folder.getPath() + "\\" + file_names.get(i));
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xml_file);
		doc.getDocumentElement().normalize();
		docs.add(doc);
	  }
	return docs;
}
 



public static String replaceExtention(String file_name) {
	    final int lastPeriodPos = file_name.lastIndexOf('.');
        file_name = file_name.substring(0, lastPeriodPos) + ".uml";
        return file_name;
	}
  
 
}