package battleChicksShared;

import org.json.JSONObject;

public class IncomingHandlerInterface {

	public static void handle(JSONObject message){
		System.out.println("this is the type: " + message.opt("type"));
	}
}
// this is a test to make sure that only these files go to github -Brie