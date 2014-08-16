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
				"\n     Channel must follow <channel> <colon> <direction>.\n" + 
				"     add colon and direction\n";
		System.err.println(error_message);
	}

	public static void channelNotationMoreThanOneColon(String chanel_name, String inter_name) {
		String error_message = "Model Notation Error: in interface " + inter_name + 
				"in channel" + chanel_name +
				"\n     Channel must follow <channel> <colon> <direction>.\n" + 
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

	public static void connectionNotationNoTwoChannels(String name) {
		String error_message = "Model Notation Error: in dependency <" + name + ">" +
				"\n     Connecion must follow <outgoing channel> <colon> <incoming channel>.\n" + 
				"     add colon and channel\n";
		System.err.println(error_message);
	}

	public static void channelNotationMoreThanTwoChannels(String name) {
		String error_message = "Model Notation Error: in dependency <" + name + ">" +
				"\n     Connecion must follow <outgoing channel> <colon> <incoming channel>.\n" + 
				"     remove unneeded colons and channels\n";
		System.err.println(error_message);
	}

	public static void badOutgoingDependency(String name) {
		String error_message = "Model Notation Error: in dependency <" + name + ">" +
				"\n     Outgoing dependency must be an instance or an associate.\n" + 
				"     1) delete dependency\n" +
				"     2) move out node to a instance or an associate\n";
		System.err.println(error_message);
	}

	public static void badIncomingDependency(String name) {
		String error_message = "Model Notation Error: in dependency <" + name + ">" +
				"\n     Incoming dependency must be an instance or an associate.\n" + 
				"     1) delete dependency\n" +
				"     2) move in node to a instance or an associate\n";
		System.err.println(error_message);
	}

	public static void sendBehaviourWrongDriction(String name, String channel) {
		String error_message = "Model Notation Error: in behaviour action <send " + name +
				" on " + channel + 
				"> is not an 'out' channle.\n" +
				"     1) change channel to 'out'\n" +
				"     2) send on different channel\n";
		System.err.println(error_message);
	}

	public static void behaviourOutChannelNotFound(String name, String channel) {
		String error_message = "Model Notation Error: in behaviour action <send " + name +
				" on " + channel + 
				"> channel is not defined.\n" +
				"     1) create channel in structure\n" +
				"     2) create state as a comment\n";
		System.err.println(error_message);
	}

	public static void receiveBehaviourWrongDriction(String name, String channel) {
		String error_message = "Model Notation Error: in behaviour action <receive " + name +
				" on " + channel + 
				"> is not an 'in' channle.\n" +
				"     1) change channel to 'in'\n" +
				"     2) send on different channel\n";
		System.err.println(error_message);
	}

	public static void behaviourInChannelNotFound(String name, String channel) {
		String error_message = "Model Notation Error: in behaviour action <receive " + name +
				" on " + channel + 
				"> channel is not defined.\n" +
				"     1) create channel in structure\n" +
				"     2) create state as a comment\n";
		System.err.println(error_message);
	}

	public static void OneTooManyTransitions(String name, int counter) {
		String error_message = "Model Notation Error: in behaviour state <" + name +
				"> has " + (counter-1)+ 
				" one too many tranistions " + 
				"> channel is not defined.\n" +
				"     1) delete tranitions\n" +
				"     2) rename tranitions 'then' or 'else' if needed\n";
		System.err.println(error_message);
	}

	public static void WrongReceiveAction(String action_name) {
		String error_message = "Model Notation Error: in behaviour action <" + action_name +
				"> needs more information \n" + 
				"     must follow <receive><from><channel name>[additional information]\n";
		System.err.println(error_message);
	}

	public static void WrongVariableState(String state_name) {
		String error_message = "Model Notation Error: in behaviour state <" + state_name +
				"> needs more information \n" + 
				"     must follow <variable>[additional information]\n";
		System.err.println(error_message);
	}

	public static void WrongSendAction(String action_name) {
		String error_message = "Model Notation Error: in behaviour action <" + action_name +
				"> needs more information \n" + 
				"     must follow <send><from><channel name>[additional information]\n";
		System.err.println(error_message);
	}

	public static void WrongAssignAction(String action_name) {
		String error_message = "Model Notation Error: in behaviour action <" + action_name +
				"> needs more information \n" + 
				"     must follow <assign><from>[any]<colon><passing objext>[additional information]\n";
		System.err.println(error_message);
	}

	public static void WrongAssignColonAction(String action_name) {
		String error_message = "Model Notation Error: in behaviour action <" + action_name +
				"> colon used but more information needed \n" + 
				"     must follow <assign><from>[any]<colon><passing objext>[additional information]\n";
		System.err.println(error_message);
	}

	public static void WrongNewActionColons(String action_name) {
		String error_message = "Model Notation Error: in behaviour action <" + action_name +
				"> needs more information \n" + 
				"     must follow <new><colon><struct type><colon>[aditional information separated by commons]\n";
		System.err.println(error_message);
	}

	public static void WrongPrintAction(String action_name) {
		String error_message = "Model Notation Error: in behaviour action <" + action_name +
				"> needs more information \n" + 
				"     must follow <print><type>\n";
		System.err.println(error_message);
	}

	public static void WrongIfState(String state_name) {
		String error_message = "Model Notation Error: in behaviour state <" + state_name +
				"> if statement needs clause \n" + 
				"     must follow <if clause>[additional information]\n";
		System.err.println(error_message);
	}

	public static void WrongForState(String state_name) {
		String error_message = "Model Notation Error: in behaviour state <" + state_name +
				"> more information needed \n" + 
				"     must follow <identifier><colon><limit><equal sign><value>[additional information]\n";
		System.err.println(error_message);
	}

	public static void internodeConnectionNotEnoughInfo(String type) {
		String error_message = "Model Notation Error: Deployment Channel <" + type + 
				"> not enough information <" + 
				"> \n"
				+ " must foolow <outgoing channel> <colon> <variable type> <colon> <incoming channel>\n";
		System.err.println(error_message);
	}

	public static void interNodeConnectionChannelNameEorror(String channel,
			String instance) {
		String error_message = "Model Notation Error: deployment channel <" + channel + 
				"> in the instance <" + instance + 
				"> is not defined in any component's presented interfaces\n" +
				"    1) check if presented interfaces have same channel\n" +
				"    2) check channel spelling in dependence notation\n" +
				"    3) check channel spelling in interface\n";
		System.err.println(error_message);
	}

	public static void internodeConnectionChannelDirectionEorror(
			String channel, String instance, String direction) {
			String error_message = "Model Notation Error: deployment channel <" + channel + 
					"> in the instance <" + instance +
					"> is looking for direction <" + direction +
					"> \n is not the right direction in interface definition \n";
			System.err.println(error_message);
	}

	public static void instanceUsedMoreThanOnce(String name, int counter) {
		String error_message = "Model Notation Error: instance name <" + name + 
				"> used more than once in deployment models <" + 
				"> \n remove " +  (counter-1) + 
				" instances in the deployment models\n ";
		System.err.println(error_message);
	}

	public static void deploymentInstanceNotDefined(String name) {
		String error_message = "Model Notation Error: instance name <" + name + 
				"> is not defined in structure models <" + 
				" \n remove instance in deployment model or define instance in class model \n";
		System.err.println(error_message);
	}

	public static void instanceNotUsed(String name, int counter) {
		String error_message = "Model Notation Error: instance name <" + name + 
				"> not defined in deployments, <" + 
				"> \n will be ignored in programming " +  
				" \n place instance in deploymen to use in coding\n";
		System.err.println(error_message);
	}

	
}
