package battleChicksShared;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class BattleChicks extends JFrame {
	private Socket s;
	private int port = 8989;
	private PrintWriter writer;
	private ArrayList<String> shipCoordinates = new ArrayList<>();
	JButton[][] myBoard;

	public void runBattleGUI() {
		JFrame frame = new JFrame();
		frame.setSize(1100, 700);

		JPanel mainPanel = new JPanel(new GridLayout(2, 3));
		mainPanel.setVisible(true);
		frame.add(mainPanel);

		JPanel upperLeftPnl = new JPanel();
		upperLeftPnl.setVisible(true);
		mainPanel.add(upperLeftPnl);

		JPanel enemyBoard = new JPanel(new GridLayout(10, 10));
		enemyBoard.setVisible(true);
		createEnemyBoard(enemyBoard);
		mainPanel.add(enemyBoard);

		JPanel upperRightPnl = new JPanel();
		upperRightPnl.setVisible(true);
		mainPanel.add(upperRightPnl);

		JPanel lowerLeftPnl = new JPanel();
		lowerLeftPnl.setVisible(true);
		mainPanel.add(lowerLeftPnl);

		JPanel myBoardPnl = new JPanel(new GridLayout(10, 10));
		myBoardPnl.setVisible(true);
		myBoard = createMyBoard(myBoardPnl);
		mainPanel.add(myBoardPnl);

		JPanel lowerRightPnl = new JPanel();
		lowerRightPnl.setVisible(true);
		mainPanel.add(lowerRightPnl);

		JLabel userNameLbl = new JLabel();
		userNameLbl.setText("username: ");
		upperRightPnl.add(userNameLbl);

		JTextArea userNameTextArea = new JTextArea(3, 20);
		userNameTextArea.setEditable(true);
		upperRightPnl.add(userNameTextArea);

		JTextArea recieveChatTextArea = new JTextArea(20, 30);
		recieveChatTextArea.setEditable(false);
		upperLeftPnl.add(recieveChatTextArea);

		JTextArea sendChatTextArea = new JTextArea(10, 30);
		sendChatTextArea.setEditable(true);
		lowerLeftPnl.add(sendChatTextArea);

		JButton sendButton = new JButton();
		sendButton.setText("send");
		lowerLeftPnl.add(sendButton);

		JButton startButton = new JButton();
		startButton.setText("START");
		upperRightPnl.add(startButton);

		JLabel updateLbl = new JLabel();
		updateLbl.setText("Place ships then press START");
		lowerRightPnl.add(updateLbl);

		JButton resetButton = new JButton();
		resetButton.setText("RESET");
		lowerRightPnl.add(resetButton);

		frame.setVisible(true);
	}

	private JButton[][] gameBoard = new JButton[10][10]; // Declared much
															// earlier in the
															// program, right
															// after the class
															// declaration.
	// private JPanel panelc = new JPanel();

	public JButton[][] createMyBoard(JPanel panelc) {
		panelc.setLayout(new GridLayout(10, 10));
		char letter = 'A';
		for (int row = 0; row < 10; row++) {
			for (int column = 0; column < 10; column++) {
				gameBoard[row][column] = new JButton("" + letter + column);
				gameBoard[row][column].setName("" + letter + column);
				gameBoard[row][column].setOpaque(true);
				gameBoard[row][column].setBackground(Color.BLUE);
				gameBoard[row][column].setForeground(Color.BLUE);
				gameBoard[row][column].addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						
						addShipActionPerformed(e);
					}
				});
				panelc.add(gameBoard[row][column]);
			}
			letter++;
		}
		return gameBoard;
	}

	public void createEnemyBoard(JPanel panelc) {
		panelc.setLayout(new GridLayout(10, 10));
		char letter = 'A';
		for (int row = 0; row < 10; row++) {
			for (int column = 0; column < 10; column++) {
				gameBoard[row][column] = new JButton("" + letter + column);
				gameBoard[row][column].setName("" + letter + column);
				gameBoard[row][column].setOpaque(true);
				gameBoard[row][column].setBackground(Color.PINK);
				gameBoard[row][column].setForeground(Color.BLUE);
				gameBoard[row][column].addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						
						attackActionPerformed(e);
					}
				});
				panelc.add(gameBoard[row][column]);
			}
			letter++;
		}
	}

	protected void attackActionPerformed(ActionEvent e) {
		// send missle
		((JButton) e.getSource()).setBackground(Color.BLACK);
		((JButton) e.getSource()).setText("X");
		System.out.println("Enemy board name: " + ((JButton) e.getSource()).getName());
	}

	protected void addShipActionPerformed(ActionEvent e) {
		char[] letters = {'A', 'B', 'C', 'D', 'E', 'F','G', 'H', 'I', 'J'};
		char[] numbers = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
		int row = 0;
		int column = 0;
		
		String coordinate = ((JButton) e.getSource()).getText();
		shipCoordinates.add(coordinate);
		System.out.println(coordinate);
		
		char[] coords = coordinate.toCharArray();
		Character[] Coo = {coords[0], coords[1]};
		
	
		for (int x = 0; x < 10; x++) {
			if (Coo[0].equals(letters[x])) {
				row = x;
			} else {
				System.out.print("row else " + x + " ,");
			}
			
			if (Coo[1].equals(numbers[x])){
				column = x;
			}else{
				System.out.print("column else " + x + " ,");
			}
		}
		int i = 3; // size 4 ship   need for loop to change color and send coordinate to arrayList (skips the middle right now)
		myBoard[row][column].setBackground(Color.ORANGE);	
//		myBoard[row + 1][column].setBackground(Color.ORANGE); // makes ship verticle
		myBoard[row][column + i].setBackground(Color.orange); // makes ship horizontal
		
	}

	// public void actionPerformed(ActionEvent e)
	// {
	// //Some code to change a specific button
	// ((JButton)e.getSource()).setText("to what you want");
	// //In the actionlistener e.getSource() will return the button you clicked
	// }
	public void Start() {
		try {
			s = new Socket(InetAddress.getByName("ec2-52-41-213-54.us-west-2.compute.amazonaws.com"), port);
			writer = new PrintWriter(s.getOutputStream());

			writer.println(OutgoingHandlerInterface.login("Brie1"));
			writer.flush();

			// writer.println(OutgoingHandlerInterface.restart());
			// writer.flush();

			// String [] array = {"A1", "B1", "C1", "A6", "B6", "C6", "A5",
			// "B5", "C5", "A4", "B4", "C4", "A3", "B3", "C3", "A2", "B2", "C2",
			// "A7"};
			// writer.println(OutgoingHandlerInterface.sendGameBoard(array));
			// writer.flush();

			// writer.println(OutgoingHandlerInterface.fire("A1"));
			// writer.flush();

			// writer.println(OutgoingHandlerInterface.whoIs());
			// writer.flush();

			// writer.println(OutgoingHandlerInterface.sendChat("late night
			// coding!"));
			// writer.flush();
			//

			// gb = new GameBoard();

			new Thread(new MessageReader(s/* , gb */)).start();

			// Scanner in = new Scanner(System.in);
			// String spot = in.nextLine();
			// writer.println(OutgoingHandlerInterface.fire(spot));
			// writer.flush();
			//
			// String chat = in.nextLine();
			// writer.println(OutgoingHandlerInterface.sendChat(chat));
			// writer.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		BattleChicks bc = new BattleChicks();
		bc.runBattleGUI();
		// bc.Start();
	}
}
