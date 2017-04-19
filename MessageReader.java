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
				String readIn = reader.readLine();
//				battleGUI.recieveChatTextArea.append(r);
				System.out.println("incoming: " + readIn);
				JSONObject message = new JSONObject(readIn);
//				IncomingHandlerInterface.turn(message);
				IncomingHandlerInterface.handle(message);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
//try and ObjectMapper
/*
 * import jackson
 * ObjectMapper mapper  = new ObjectMapper();
 * final ArbiterOneResponse response = mapper.readValue(jsonResponse, ArbiterOneResponseclass):
 * 
 * [4/19/17, 10:35:46 AM] Ray Cox: import com.fasterxml.jackson.annotation.JsonInclude;
	import com.fasterxml.jackson.core.JsonProcessingException;
	import com.fasterxml.jackson.databind.DeserializationFeature;
	import com.fasterxml.jackson.databind.ObjectMapper;
	import com.fasterxml.jackson.databind.SerializationFeature;
	import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
<dependency>
<groupId>org.glassfish.jersey.media</groupId>
<artifactId>jersey-media-json-jackson</artifactId>
<version>${jersey.version}</version>
</dependency>
 */
