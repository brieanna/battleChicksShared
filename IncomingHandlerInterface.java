package battleChicks;

import org.json.JSONObject;

public class IncomingHandlerInterface {

	public static void handle(JSONObject message){
		System.out.println("this is the type: " + message.opt("type"));
	}
}
