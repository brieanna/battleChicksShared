package BattleChicks;


import org.json.JSONObject;

public class IncomingHandlerInterface {
private static JSONObject myObject;
 static boolean turn; 

	public static void handle(JSONObject message){
		myObject = message;
//		String first = myObject.getJSONObject("type").getString("message");
		
		String type = message.optString("type");
		switch (type){
		case "login":
			System.out.println(type);
			break;
		case "application":
			// getting true or false for turn
			JSONObject mess = message.optJSONObject("message");
			turn = mess.optBoolean("turn");
			System.out.println(type + "> " + turn);
			break;
		case "acknowledge":
			String ackMessage = message.optString("message");
			System.out.println(type + "> " + ackMessage);
			break;
		case "chat":
			String chat = message.optString("message");
			System.out.println(type + "> " + chat);
			break;
		case "message":
			System.out.println(type);
			break;
		}		
	}	
}
