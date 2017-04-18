package BattleChicks;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class BattleShip 
{
	private Socket socket;
	private PrintWriter writer;
	private int port = 8989;
	private BattleShipGUI gui;
	
	public BattleShip()
	{
		try{
			
			
			socket = new Socket(InetAddress.getByName("ec2-52-41-213-54.us-west-2.compute.amazonaws.com"), port);
			writer = new PrintWriter(socket.getOutputStream());
		
			writer.println(OutgoingHandlerInterface.login("Kendra"));
			writer.flush();
			
			//writer.println(OutgoingHandlerInterface.restart());
			//writer.flush();
			
//			String [] array = {"A1", "B1", "C1", "A6", "B6", "C6", "A5", "B5", "C5", "A4", "B4", "C4", "A3", "B3", "C3", "A2", "B2", "C2", "A7"};
//			writer.println(OutgoingHandlerInterface.sendGameBoard(array));
//			writer.flush();
			
//			writer.println(OutgoingHandlerInterface.sendChat("test"));
//			writer.flush();
			
//			writer.println(OutgoingHandlerInterface.fire("A1"));
//			writer.flush();
			
//			writer.println(OutgoingHandlerInterface.whoIs());
//			writer.flush();
			
			gui = new BattleShipGUI();			
			new Thread(new MessageReader(socket, gui)).start();
			
//			Scanner in = new Scanner(System.in);
//			String chat = in.nextLine();
//			
//			writer.println(OutgoingHandlerInterface.sendChat(chat));
//			writer.flush();
//			
//			writer.println(OutgoingHandlerInterface.fire(spot));
//			writer.flush();

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
		
	public static void main(String[] args) 
	{
		BattleShipGUI gui = new BattleShipGUI();
		gui.showGUI();
		//new BattleShip();

	}

}
