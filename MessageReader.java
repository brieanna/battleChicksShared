package battleChicksShared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.json.JSONObject;

public class MessageReader implements Runnable {

	private Socket socket;
	private BufferedReader reader;
	public BattleChicks battleGUI = new BattleChicks();

	public MessageReader(Socket socket, BattleChicks battleGUI) throws IOException {
		this.socket = socket;
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.battleGUI = battleGUI;
	}

	@Override
	public void run() {
		while (true) {
			try {
				String r = reader.readLine();
//				battleGUI.recieveChatTextArea.append(r);
				System.out.println("incoming: " + r);
				JSONObject message = new JSONObject(r);
				IncomingHandlerInterface.handle(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
