package structuralModels;

import java.util.ArrayList;

public class Deployment {
	private String name;
	private ArrayList<Artifact> artifact_list;
	private ArrayList<InternodeConnection> inter_list;
	
	public Deployment(String name){
		this.name = name;
		artifact_list = new ArrayList<Artifact>();
		inter_list =  new ArrayList<InternodeConnection>();
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
	public void setArtifact_list(ArrayList<Artifact> artifactList) {
		this.artifact_list = artifact_list;
	}
	public ArrayList<InternodeConnection> getInterList() {
		return inter_list;
	}
	public void setInterList(ArrayList<InternodeConnection> inter_list) {
		this.inter_list = inter_list;
	}
	public void addArtifact(Artifact art){
		artifact_list.add(art);
	}
	public void addInter(InternodeConnection inter){
		inter_list.add(inter);
	}
	public Artifact getArtifact(int index){
		return artifact_list.get(index);
	}
	public InternodeConnection getInter(int index){
		return inter_list.get(index);
	}
}
