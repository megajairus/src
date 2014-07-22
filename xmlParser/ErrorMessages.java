package xmlParser;

public class ErrorMessages {
	public static void ConnectionChannelNameEorror(String channel, String instance){
		String error_message = "Model Notation Error: Channel <" + channel + 
				"> in the instance <" + instance + 
				"> is not defined in any component's presented interfaces\n" +
				" 1) check if presented interfaces have same channel\n" +
				" 2) check channel spelling in dependence notation\n" +
				" 3) check channel spelling in interface";
		System.err.println(error_message);
	}
}
