package battleChicksShared;

import org.json.JSONObject;

public class IncomingHandlerInterface {
private static JSONObject myObject;
	public static void handle(JSONObject message){
		myObject = message;
//		String first = myObject.getJSONObject("type").getString("message");
		
		String type = (String) message.opt("type");
		switch (type){
		case "login":
			System.out.println(type);
			break;
		case "application":
			System.out.println(type);
			break;
		case "acknowledge":
			System.out.println(type);
			break;
		case "chat":
			System.out.println(type);
			break;
		case "message":
			System.out.println(type);
			break;
		}
		
	}
	
	public static void turn(JSONObject message){
		//String turn = obj.getJSONObject("turn");
		Object myTurn = message.opt("turn");
		if(myTurn.equals("true")){
			System.out.println("it was true!!!!");
		}else{
			System.out.println("it was false :(");
		}
		System.out.println("???");
	}
}
// test