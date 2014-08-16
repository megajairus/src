package structuralModels;

import java.util.ArrayList;

public class Deployment {
	private String name;
	private ArrayList<Artifact> artifact_list;
	
	public Deployment(String name){
		this.name = name;
		artifact_list = new ArrayList<Artifact>();
	}
	
	public int artifactSize(){
		return artifact_list.size();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Artifact> getArtifactList() {
		return artifact_list;
	}
	

	private ArrayList<Artifact> extracted() {
		return this.artifact_list = artifact_list;
	}
	public void addArtifact(Artifact art){
		artifact_list.add(art);
	}
	public Artifact getArtifact(int index){
		return artifact_list.get(index);
	}
}
