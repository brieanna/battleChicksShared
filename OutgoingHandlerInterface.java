package battleChicksShared;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

public class OutgoingHandlerInterface {

	public static String login(String username) {
		JSONObject user = new JSONObject();
		user.put("type", "login");
		JSONObject name = new JSONObject();
		name.put("username", username);
		user.put("message", name);
		System.out.println(user.toString());
		return user.toString();
	}

	public static String sendChat(String message) {
		JSONObject chat = new JSONObject();
		chat.put("type", "chat");
		chat.put("message", message);
		System.out.println(chat.toString());
		return chat.toString();
	}

	public static String whoIs() {
		JSONObject who = new JSONObject();
		who.put("type", "whois");
		System.out.println(who.toString());
		return who.toString();
	}

	public static String sendGameBoard(ArrayList<String> gameBoard) {
		
		Object [] gameboard = gameBoard.toArray();
		JSONArray ships = new JSONArray(Arrays.asList(gameboard));
		JSONObject message = new JSONObject();
		message.put("type", "application");
		JSONObject board = new JSONObject();
		board.put("module", "battlechicks");
		board.put("gameboard", ships);
		message.put("message", board);
		System.out.println(message.toString());
		return message.toString();
		/*
		 * {"type":"application","message":{"module":
		 * "battlechicks","gameboard"😞"A1","B1","C1","A6","B6","C6","A5","B5",
		 * "C5","A4","B4","C4","A3","B3","C3","A2","B2","C2","A7"]}}
		 */
	}

	public static String fire(String spot) {
		JSONObject message = new JSONObject();
		message.put("type", "application");
		JSONObject box = new JSONObject();
		box.put("module", "battlechicks");
		box.put("fire", spot);
		message.put("message", box);
		System.out.println(message.toString());
		return message.toString();
	}
	
	public static String restart() {
		JSONObject message = new JSONObject();
		message.put("type", "application");
		JSONObject stop = new JSONObject();
		stop.put("module", "battlechicks");
		stop.put("restart", true);
		message.put("message", stop);
		System.out.println(message.toString());
		return message.toString();
	}

}
