package xmlParser;

import java.util.ArrayList;

import structuralModels.Artifact;
import structuralModels.Connection;
import structuralModels.Deployment;
import structuralModels.InternodeConnection;
import structuralModels.TempInterConnection;

public class DeploymentPerfecter {

	public static void finishPackageDate(StructureData structure,
			ArrayList<Deployment> deployments,
			ArrayList<TempInterConnection> temps) {
		fillInterNodeConnections(structure, deployments, temps);
	}

	private static void fillInterNodeConnections(StructureData structure,
			ArrayList<Deployment> deployments,
			ArrayList<TempInterConnection> temps) {
		for(int i = 0; i < temps.size(); i++){
			int in_pack = findDeployment(temps.get(i).getInId(), deployments);
			int out_pack = findDeployment(temps.get(i).getOutId(), deployments);
			String in_name = findInstance(temps.get(i).getInId(), deployments.get(in_pack));
			String out_name = findInstance(temps.get(i).getOutId(), deployments.get(out_pack));
			Connection connect = findConnection(in_name, out_name, structure.getConnectionList());
			String in_node = deployments.get(in_pack).getName();
			String out_node = deployments.get(out_pack).getName();
			String type = temps.get(i).getType();
			String [] types = type.split(":");
			if(connect != null){
			//if(checkConnection(connect, type)){
				structure.addInternode(new InternodeConnection(connect, out_node, types[1], "receive"));
				structure.addInternode(new InternodeConnection(connect, in_node, types[1], "send"));
			}
			else{
				connect = creatNewConnection(type, in_name, out_name);
				structure.addInternode(new InternodeConnection(connect, out_node, types[1], "receive"));
				structure.addInternode(new InternodeConnection(connect, in_node, types[1], "send"));
			}
		}
	}

	private static Connection creatNewConnection(String type, String in_name,
			String out_name) {
		String [] types = type.split(":");
		Connection connect = new Connection(types[2],types[0], "" , "",out_name, in_name);
		return connect;
	}

	private static boolean checkConnection(Connection connect, String type) {
		String [] types = type.split(":");
		System.out.println(types.length );
		System.out.println(types[0]);
		System.out.println(connect.getOutChannel());
		System.out.println(types[2]);
		System.out.println(connect.getInChannel());
		if(types[0].equals(connect.getOutChannel()) && types[2].equals(connect.getInChannel())){
			return true;
		}
		return false; 
	}

	private static int findDeployment(String id,
			ArrayList<Deployment> deployments) {
		for(int i = 0; i < deployments.size(); i++){
			for(int j =0; j < deployments.get(i).getArtifactList().size(); j++){
				Artifact art = deployments.get(i).getArtifact(j);
				if(art.getId().equals(id)){
					return i;
				}
			}
		}
		return -1;
	}

	private static Connection findConnection(String in_name, String out_name,
			ArrayList<Connection> connections) {
		for(int i = 0; i < connections.size(); i++){
			String in_instance = connections.get(i).getToComponent();
			String out_instance = connections.get(i).getFromComponent();
			if(in_instance.equals(in_name) && out_instance.equals(out_name)){
				return connections.get(i);
			}
			
		}
		return null;
	}

	private static String findInstance(String id, Deployment deployment) {
		for(int i =0; i < deployment.getArtifactList().size(); i++){
			Artifact art = deployment.getArtifact(i);
			if(art.getId().equals(id)){
				return art.getName();
			}
		}
		return null;
	}
}

