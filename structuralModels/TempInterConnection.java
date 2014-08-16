package structuralModels;

public class TempInterConnection {
	private String type;
	private String in_id;
	private String out_id;
	
	public TempInterConnection(String type, String in_id, String out_id){
		this.type = type;
		this.in_id = in_id;
		this.out_id = out_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOutId() {
		return out_id;
	}

	public void setOutId(String out_id) {
		this.out_id = out_id;
	}

	public String getInId() {
		return in_id;
	}

	public void setInId(String in_id) {
		this.in_id = in_id;
	}

}
