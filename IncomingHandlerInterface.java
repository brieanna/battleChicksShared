package BattleChicks;

import org.json.JSONObject;

public class IncomingHandlerInterface {

	static boolean turn;
	static boolean reset;
	static boolean hit;
	static String win;
	static BattleShipGUI gui = new BattleShipGUI();
	static JSONObject myObject;

	public static void handle(JSONObject message) {
		myObject = message;

		String type = message.optString("type");
		switch (type) {
		case "login":
			System.out.println(type);
			break;

		case "application":
			JSONObject mess = message.optJSONObject("message");
			applicationHandler(mess);
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
		}
	}

	public static void applicationHandler(JSONObject mess) {
		if (mess.has("turn")) {
			turn = mess.optBoolean("turn");
			gui.setTurn(turn);
		}
		else if (mess.has("hit")) {
			hit = mess.optBoolean("hit");
			String coordinate = mess.optString("coordinate");
			gui.hitMiss(hit, coordinate);
		}
		else if (mess.has("win")) {
			win = mess.optString("win");
			gui.setTurn(false);
			gui.updateTextArea(win + ": WINS!");
			gui.getChatMessage(win + ": WINS!");
		}
		else if (mess.has("reset")) {
			reset = mess.optBoolean("reset");
			gui.updateTextArea("Game has ended please reset board.");
		}
	}
}
