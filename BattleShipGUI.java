package BattleChicks;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class BattleShipGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private PrintWriter writer;
	private static JFrame frame;
	private static JPanel mainPanel, headPanel, chatPanel, textPanel, opponentPanel, myGridPanel, instructionPanel,
			sendPanel, onePanel, twoPanel, threePanel, fourPanel, fivePanel, sixPanel, usernamePanel,
			shipPanel, winLosePanel;
	private static JLabel headLabel, userNameLabel, winLoseLabel;
	private static JTextField userNameTextField;
	private static JTextArea chatTextArea, textTextArea, instructionTextArea;
	private static JScrollPane chatScrollPane, textScrollPane;
	public JButton[][] gridButtons = new JButton[10][10];
	public JButton[][] myBoard;
	public static JRadioButton verticalRadio, horizontalRadio;
	private static JButton sendButton, startButton, resetButton, loginButton;
	private static String instructions = "Instructions\nEnter your username.\nPlace all five ships on your grid "
			+ "and hit the submit button.\nThe ships will be placed in the order listed below. Select horizontal"
			+ " or vertical to change the direction they are being placed.\n\n"
			+ "Your username will be added and you will be connected to the game.\n"
			+ "In order to make a hit, press the appropriate button on your opponents grid.\n"
			+ "2 - Two Square  2 - Three Square  1 - Four Square  1 - Five Square";
	private static ArrayList<String> battleshipButtons = new ArrayList<String>();
	private static ButtonGroup group;
	public int countShips = 1;
	
	private Socket socket;
	private int port = 8989;

	public BattleShipGUI() {

	}

	public void showGUI() {
		frame = new JFrame("Battle Ship");
		mainPanel = new JPanel();
		mainPanel.setVisible(true);
		mainPanel.setLayout(new GridLayout(3, 2));
		frame.add(mainPanel);

		// instruction Panel
		onePanel = new JPanel(new GridLayout(2, 1));
		onePanel.setVisible(true);
		instructionPanel = new JPanel();
		instructionPanel.setVisible(true);
		instructionTextArea = new JTextArea(20, 40);
		instructionTextArea.append(instructions);
		instructionTextArea.setLineWrap(true);
		instructionTextArea.setEditable(false);
		instructionPanel.add(instructionTextArea);
		shipPanel = new JPanel(new GridLayout(3,1));
		//shipHeadLabel = new JLabel("Ships", SwingConstants.CENTER);
		//shipHeadLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		//shipPanel.add(shipHeadLabel);
		//shipLabel = new JLabel("2 - Two Square  2 - Three Square  1 - Four Square  1 - Five Square");
		//shipLabel.setSize(500, 50);
		//shipPanel.add(shipLabel);
		group = new ButtonGroup();
		verticalRadio = new JRadioButton("Vertical Ship", true);
		verticalRadio.setBorder(new EmptyBorder(10, 100, 10, 10));
		horizontalRadio = new JRadioButton("Horizontal Ship", false);
		horizontalRadio.setBorder(new EmptyBorder(10, 10, 10, 50));
		group.add(verticalRadio);
		group.add(horizontalRadio);
		shipPanel.add(verticalRadio);
		shipPanel.add(horizontalRadio);
		//submitPanel = new JPanel();
		//submitPanel.setVisible(true);
		startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startButtonActionPerformed();
			}
		});
		resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetButtonActionPerformed();
			}
		});
		shipPanel.add(startButton);
		shipPanel.add(resetButton);
		//submitPanel.add(submitButton);
		//submitPanel.add(resetButton);
		onePanel.add(instructionPanel);
		onePanel.add(shipPanel);
		//onePanel.add(submitPanel);

		// header Panel
		twoPanel = new JPanel(new GridLayout(1, 1));
		twoPanel.setVisible(true);
		headPanel = new JPanel(new GridLayout(3, 1));
		headPanel.setVisible(true);
		headLabel = new JLabel("Battle Ship", SwingConstants.CENTER);
		headLabel.setFont(headLabel.getFont().deriveFont(32.0f));
		headLabel.setBorder(new EmptyBorder(25, 100, 25, 100));
		headPanel.add(headLabel);

		usernamePanel = new JPanel();
		usernamePanel.setVisible(true);
		usernamePanel.setSize(12, 40);
		userNameLabel = new JLabel("Username: ");
		usernamePanel.add(userNameLabel);
		userNameTextField = new JTextField(25);
		usernamePanel.add(userNameTextField);
		loginButton = new JButton("Login");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loginButtonActionPerformed();
			}
		});
		usernamePanel.add(loginButton);
		headPanel.add(usernamePanel);

		winLosePanel = new JPanel();
		winLosePanel.setVisible(true);
		winLoseLabel = new JLabel("Win/lose message will go here.", SwingConstants.CENTER);
		winLoseLabel.setFont(winLoseLabel.getFont().deriveFont(26.0f));
		winLosePanel.add(winLoseLabel);
		headPanel.add(winLosePanel);
		twoPanel.add(headPanel);

		// opponent Panel
		threePanel = new JPanel(new GridLayout());
		threePanel.setVisible(true);
		opponentPanel = new JPanel(new GridLayout(10, 10));
		opponentPanel.setVisible(true);
		opponentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		buildOpponentBoard(opponentPanel);

		threePanel.add(opponentPanel);

		// chat Panel
		fourPanel = new JPanel(new GridLayout());
		fourPanel.setVisible(true);
		chatPanel = new JPanel();
		chatTextArea = new JTextArea(20, 40);
		chatTextArea.setLineWrap(true);
		chatTextArea.setEditable(false);
		chatScrollPane = new JScrollPane(chatTextArea);
		chatScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		chatPanel.add(chatScrollPane);
		chatPanel.setVisible(true);
		fourPanel.add(chatPanel);

		// myGrid Panel
		fivePanel = new JPanel(new GridLayout());
		fivePanel.setVisible(true);
		fivePanel.setSize(50, 50);
		myGridPanel = new JPanel(new GridLayout(10, 10));
		myGridPanel.setVisible(true);
		myGridPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		myBoard = buildMyBoard(myGridPanel);

		fivePanel.add(myGridPanel);

		// send text Panel
		sixPanel = new JPanel(new GridLayout(2, 1));
		sixPanel.setVisible(true);
		textPanel = new JPanel();
		textTextArea = new JTextArea(10, 40);
		textTextArea.setLineWrap(true);
		textTextArea.setEditable(true);
		textScrollPane = new JScrollPane(textTextArea);
		textScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		textPanel.add(textScrollPane);
		textPanel.setVisible(true);
		sixPanel.add(textPanel);

		sendPanel = new JPanel();
		sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendButtonActionPerformed();
			}

		});
		sendPanel.add(sendButton);
		sixPanel.add(sendPanel);

		mainPanel.add(onePanel);
		mainPanel.add(twoPanel);
		mainPanel.add(threePanel);
		mainPanel.add(fourPanel);
		mainPanel.add(fivePanel);
		mainPanel.add(sixPanel);

		frame.setLayout(new GridLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Battle Ship");
		frame.setSize(new Dimension(1050, 1000));
		frame.setVisible(true);
	}

	public void buildOpponentBoard(JPanel panel) {
		for (int r = 0; r < 10; r++) {
			for (int c = 0; c < 10; c++) {
				String[] letters = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
				gridButtons[r][c] = new JButton("" + letters[r] + c);
				gridButtons[r][c].setPreferredSize(new Dimension(15, 15));
				gridButtons[r][c].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						opponentButtonActionPerformed(e);
					}
				});
				gridButtons[r][c].setBackground(Color.GRAY);
				panel.add(gridButtons[r][c]);
			}
		}
	}

	public JButton[][] buildMyBoard(JPanel panel) {
		for (int r = 0; r < 10; r++) {
			for (int c = 0; c < 10; c++) {
				String[] letters = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
				gridButtons[r][c] = new JButton("" + letters[r] + c);
				gridButtons[r][c].setPreferredSize(new Dimension(15, 15));
				gridButtons[r][c].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						myButtonActionPerformed(e);
					}
				});
				gridButtons[r][c].setBackground(Color.GRAY);
				panel.add(gridButtons[r][c]);
			}
		}
		return gridButtons;
	}

	public void opponentButtonActionPerformed(ActionEvent e) {
		((JButton) e.getSource()).setBackground(Color.RED);
		String clickedButton = ((JButton) e.getSource()).getText();
		System.out.println(clickedButton);
		
		writer.println(OutgoingHandlerInterface.fire(clickedButton));
		writer.flush();
	}

	public void myButtonActionPerformed(ActionEvent e) {
		String coordinate = ((JButton) e.getSource()).getText();
		char[] coords = coordinate.toCharArray();
		Character[] charArray = { coords[0], coords[1] };
		char[] letters = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J' };
		char[] numbers = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		int r = 0;
		int c = 0;

		for (int x = 0; x < 10; x++) {
			if (charArray[0].equals(letters[x])) {

				r = x;
			} else {
				System.out.print("row else " + x + " ,");
			}

			if (charArray[1].equals(numbers[x])) {
				c = x;
			} else {
				System.out.print("column else " + x + " ,");
			}
		}

		addShipsToBoard(r, c);
		
		System.out.println(battleshipButtons);
	}

	public ActionListener addShipsToBoard(int r, int c) {
		int size;

		switch (countShips) {
		case 1:
			size = 2;
			buildShip(r, c, size);
			break;
		case 2:
			size = 2;
			buildShip(r, c, size);
			break;
		case 3:
			size = 3;
			buildShip(r, c, size);
			break;
		case 4:
			size = 3;
			buildShip(r, c, size);
			break;
		case 5:
			size = 4;
			buildShip(r, c, size);
			break;
		case 6:
			size = 5;
			buildShip(r, c, size);
			break;

		}

		countShips++;

		return null;
	}

	public ActionListener buildShip(int r, int c, int size) {
			if (verticalRadio.isSelected()) {
				for (int i = 0; i < size; i++) {
					myBoard[r + i][c].setBackground(Color.PINK);
					String coord = myBoard[r + i][c].getText();
					battleshipButtons.add(coord);
				}
			} else if (horizontalRadio.isSelected()) {
				for (int i = 0; i < size; i++) {
					myBoard[r][c + i].setBackground(Color.PINK);
					String coord = myBoard[r][c + i].getText();
					battleshipButtons.add(coord);
				}
			}

		return null;
	}

	public void sendButtonActionPerformed() {
		String chat = textTextArea.getText();
		writer.println(OutgoingHandlerInterface.sendChat(chat));
		writer.flush();

		chatTextArea.append("\n" + chat);
	}

	public void startButtonActionPerformed() {
		//String username = userNameTextField.getText();
//		try {
//			socket = new Socket(InetAddress.getByName("ec2-52-41-213-54.us-west-2.compute.amazonaws.com"), port);
//			//socket = new Socket(InetAddress.getByName("137.190.250.60"), port);
//			writer = new PrintWriter(socket.getOutputStream());
//			
//			writer.println(OutgoingHandlerInterface.login(username));
//			writer.flush();
			
			writer.println(OutgoingHandlerInterface.sendGameBoard(battleshipButtons));
			writer.flush();
			
//			BattleShipGUI gui = new BattleShipGUI();
//			new Thread(new MessageReader(socket, gui)).start();
//			
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	public void loginButtonActionPerformed(){
		String username = userNameTextField.getText();
		try {
			socket = new Socket(InetAddress.getByName("ec2-52-41-213-54.us-west-2.compute.amazonaws.com"), port);
			//socket = new Socket(InetAddress.getByName("137.190.250.60"), port);
			writer = new PrintWriter(socket.getOutputStream());
			
			writer.println(OutgoingHandlerInterface.login(username));
			writer.flush();
			
			BattleShipGUI gui = new BattleShipGUI();
			new Thread(new MessageReader(socket, gui)).start();
						
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void resetButtonActionPerformed() {
		writer.println(OutgoingHandlerInterface.restart());
		writer.flush();
		
	}

}