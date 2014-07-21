package structuralModels;

import java.util.ArrayList;

public class Component extends Struct{
	private ArrayList<String> presents;
	private ArrayList<BehaviourType> behaviours;
	private ArrayList<Procedure> procedures;
	
	public Component(String name, String id, ArrayList<String> presents, ArrayList<Variable> variables, ArrayList<Procedure> procedures){
		super(name, id, variables);
		this.presents = presents;
		behaviours  = new ArrayList<BehaviourType>();
		this.procedures = procedures;
	}
	public void addProcedure(Procedure procedure){
		this.procedures.add(procedure);
	}
	public Procedure getProcedure(int index){
		return procedures.get(index);
	}
	public void setBehaviours(ArrayList<BehaviourType> behaviours){
		this.behaviours = behaviours;
	}
	
	public BehaviourType getBehaviour(int index){
		return behaviours.get(index);
	}
	
	public ArrayList<BehaviourType> getBehaviour(){
		return behaviours;
	}
	
	
	public void addPresents(String presents){
		this.presents.add(presents);
	}
	
	
	
	public String getPresents(int index){
		return presents.get(index);
	}
	
	
	public int presentsSize(){
		return presents.size();
	}
	
	public void setPresents(int index, String presents){
		this.presents.set(index, presents);
	}
	public int procedureSize() {
		if (!procedures.isEmpty()){
			return procedures.size();
		}
		return 0;
	}
	public int procedureParameterSize(int index){
		return procedures.get(index).size();
	}
	public String getProcedureReturn(int index){
		return procedures.get(index).getReturn().getType();
	}
	public Boolean getProcedureReturnPointer(int index){
		return procedures.get(index).getReturn().getPointer();
	}
	public Boolean getProcedureParameterPointer(int procedure_index, int parameder_index){
		return procedures.get(procedure_index).getParameters().get(parameder_index).getPointer();
	}
	public String getProcedureVariableType(int procedure_index, int parameder_index){
		return procedures.get(procedure_index).getParameters().get(parameder_index).getType();
	}

}
