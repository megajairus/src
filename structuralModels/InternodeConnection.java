package structuralModels;

public class InternodeConnection extends Connection {
	
	private String internode;
	private String type;
	
	public InternodeConnection(String in_channel, String out_channel,
			String to_component_id, String from_component_id) {
		super(in_channel, out_channel, to_component_id, from_component_id);
	}
	public InternodeConnection(Connection connect, String internode, String type){
		super(connect.getInChannel(), connect.getOutChannel(), connect.getToComponentID(), connect.getToComponentID(), connect.getFromComponent(), connect.getToComponent());
		this.internode = internode;
		this.type = type;
	}
	public String getInternode() {
		return internode;
	}
	public void setInternode(String internode) {
		this.internode = internode;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
