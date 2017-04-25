package battleChicksShared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.json.JSONObject;

public class MessageReader implements Runnable {

	private BufferedReader reader;
	public BattleChicks battleGUI;

	public MessageReader(Socket socket, BattleChicks battleGUI) throws IOException {
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.battleGUI = battleGUI;
	}

	@Override
	public void run() {
		while (true) {
			try {
				String readIn = reader.readLine();
				System.out.println("incoming: " + readIn);
				JSONObject message = new JSONObject(readIn);
				IncomingHandlerInterface.handle(message);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
