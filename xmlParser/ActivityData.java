package xmlParser;

import java.util.ArrayList;

import structuralModels.BehaviourType;

public class ActivityData {
	private ArrayList<BehaviourType> behaviours;
	private String tagged;
	
	public ActivityData(String tagged, ArrayList<BehaviourType> behaviours){
		this.tagged = tagged;
		this.behaviours = behaviours;
	}
	
	public String getTagged(){
		return tagged;
	}
	
	public ArrayList<BehaviourType> getBehaviours(){
		return behaviours;
	}
}
