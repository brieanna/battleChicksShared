package BattleChicks;


import org.json.JSONObject;

public class IncomingHandlerInterface {
static boolean turn; 
 static boolean reset;
 static boolean hit;
 static String win;
 static BattleShipGUI gui = new BattleShipGUI();

	public static void handle(JSONObject message){
		String type = message.optString("type");
		switch (type){
		case "login":
			System.out.println(type);
			break;
		case "application":
			// getting true or false for turn
			JSONObject mess = message.optJSONObject("message");
			applicationHandler(mess);
//			turn = mess.optBoolean("turn");
//			System.out.println(type + "> " + turn);
			break;
		case "acknowledge":
			String ackMessage = message.optString("message");
			gui.updateTextArea(ackMessage);
			System.out.println(type + "> " + ackMessage);
			break;
		case "chat":
			String chat = message.optString("message");
			String name = message.optString("fromUser");
			String send = name + ": " + chat;
			gui.getChatMessage(send);
			System.out.println(type + "> " + chat);
			break;
		case "error":
			String error = message.optString("message");
			gui.updateTextArea(error);
			System.out.println(type + "> " + error);
			break;
//		case "message":
//			System.out.println(type);
//			break;
		}	
	}	
	
	public static void applicationHandler(JSONObject mess){
		
		//String message = mess;
		turn = mess.optBoolean("turn");
		reset = mess.optBoolean("reset");
		hit = mess.optBoolean("hit");
		win = mess.optString("win");
		
		
		
	}
}
