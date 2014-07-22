package xmlParser;

public class ErrorMessages {
	public static void ConnectionChannelNameEorror(String channel, String instance){
		String error_message = "Model Notation Error: Channel <" + channel + 
				"> in the instance <" + instance + 
				"> is not defined in any component's presented interfaces\n" +
				"    1) check if presented interfaces have same channel\n" +
				"    2) check channel spelling in dependence notation\n" +
				"    3) check channel spelling in interface\n";
		System.err.println(error_message);
	}

	public static void channelNotationNoColon(String chanel_name, String inter_name) {
		String error_message = "Model Notation Error: in interface " + inter_name + 
				"in channel" + chanel_name +
				"\n     Channel must follow <outgoing channel> <colon> <incoming channel>.\n" + 
				"     add colon and direction\n";
		System.err.println(error_message);
	}

	public static void channelNotationMoreThanOneColon(String chanel_name, String inter_name) {
		String error_message = "Model Notation Error: in interface " + inter_name + 
				"in channel" + chanel_name +
				"\n     Channel must follow <outgoing channel> <colon> <incoming channel>.\n" + 
				"     A more than one colon is in the messaage.\n";
		System.err.println(error_message);
		
	}

	public static void cannotContinue() {
		String error_message = "Modeling Error\n" +
				"     Erros in model willnot allow Intermidiate Notation to be created";
		System.err.println(error_message);
		
	}

	public static void channelDirectionNotation(String chanel_name,
			String inter_name, String direction) {
		String error_message = "Model Notation Error: in interface " + inter_name + 
				"in channel" + chanel_name +
				"\n     Channel must follow <outgoing channel> <colon> <incoming channel>.\n" + 
				"     Direction <" + direction + 
				"> must be either <'in'> or <'out'> \n";
		System.err.println(error_message);
	}

	public static void ConnectionChannelDirectionEorror(String channel,
			String instance, String direction) {
		String error_message = "Model Notation Error: Channel <" + channel + 
				"> in the instance <" + instance +
				"> is looking for direction <" + direction +
				"> \n is not the right direction in interface definition \n";
		System.err.println(error_message);
		
	}
}
