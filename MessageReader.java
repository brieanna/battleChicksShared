package BattleChicks;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.json.JSONObject;


public class MessageReader implements Runnable {

	private Socket socket;
	private BufferedReader reader;
	private BattleShipGUI gui;

	public MessageReader(Socket socket, BattleShipGUI gui) throws IOException {
		this.socket = socket;
		this.gui = gui;

		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		//reader.readLine();
	}

	@Override
	public void run() {

		while (true) {
			try {
				String r = reader.readLine();
				System.out.println("incoming: " + r);
				JSONObject message =  new JSONObject(r);
				IncomingHandlerInterface.handle(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
			}
	}

}
