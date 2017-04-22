package battleChicksShared;

import org.json.JSONObject;

public class IncomingHandlerInterface {

	static boolean turn;
	static boolean reset;
	static boolean hit;
	static String win;
	// static BattleChicks gui = new BattleChicks();
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
			BattleChicks.updateTextArea(ackMessage);
			System.out.println(type + "> " + ackMessage);
			break;

		case "chat":
			String chat = message.optString("message");
			String name = message.optString("fromUser");
			String send = name + ": " + chat;
			BattleChicks.setChatMessage(send);
			System.out.println(type + "> " + chat);
			break;

		case "error":
			String error = message.optString("message");
			BattleChicks.updateTextArea(error);
			System.out.println(type + "> " + error);
			break;
		}
	}

	public static void applicationHandler(JSONObject mess) {
		if (mess.has("turn")) {
			turn = mess.optBoolean("turn");
			BattleChicks.setTurn(turn);
		}
		else if (mess.has("hit")) {
			hit = mess.optBoolean("hit");
			String coordinate = mess.optString("coordinate");
			BattleChicks.hitMiss(hit, coordinate);
		}
		else if (mess.has("win")) {
			win = mess.optString("win");
			String winMessage = win + ": WINS!";
			BattleChicks.setTurn(false);
			BattleChicks.setWin(winMessage);
			BattleChicks.updateTextArea(winMessage);
			BattleChicks.setChatMessage(winMessage);
		}
		else if (mess.has("reset")) {
			reset = mess.optBoolean("reset");
			BattleChicks.updateTextArea("Game has ended please reset board.");
		}
	}
}
