package BattleChicks;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.ArrayList;

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
			sendPanel, submitPanel, onePanel, twoPanel, threePanel, fourPanel, fivePanel, sixPanel, usernamePanel,
			shipPanel, winLosePanel;
	private static JLabel headLabel, userNameLabel, winLoseLabel, shipLabel, shipHeadLabel;
	private static JTextField userNameTextField;
	private static JTextArea chatTextArea, textTextArea, instructionTextArea;
	private static JScrollPane chatScrollPane, textScrollPane;
	public JButton[][] gridButtons = new JButton[10][10];
	public JButton[][] myBoard;
	public static JRadioButton verticalRadio, horizontalRadio;
	private static JButton sendButton, submitButton;
	private static String instructions = "Instructions\nEnter your username.\nPlace all five ships on your grid "
			+ "and hit the submit button. Your username will be added and you will be connected to the game.\n"
			+ "In order to make a hit, press the appropriate button on your opponents grid.";
	private static int twoSquare, threeSquare, fourSquare, fiveSquare;
	private static ArrayList<String> battleshipButtons;

	public BattleShipGUI() {

	}

	public void showGUI() {
		frame = new JFrame("Battle Ship");
		mainPanel = new JPanel();
		mainPanel.setVisible(true);
		mainPanel.setLayout(new GridLayout(3, 2));
		frame.add(mainPanel);

		// instruction Panel
		onePanel = new JPanel(new GridLayout(3, 1));
		onePanel.setVisible(true);
		instructionPanel = new JPanel();
		instructionPanel.setVisible(true);
		instructionTextArea = new JTextArea(20, 40);
		instructionTextArea.append(instructions);
		instructionTextArea.setLineWrap(true);
		instructionTextArea.setEditable(false);
		instructionPanel.add(instructionTextArea);
		shipPanel = new JPanel(new GridLayout(4, 1));
		shipHeadLabel = new JLabel("Ships", SwingConstants.CENTER);
		shipHeadLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		shipPanel.add(shipHeadLabel);
		verticalRadio = new JRadioButton("Vertical Ship");
		verticalRadio.setBorder(new EmptyBorder(10, 150, 10, 20));
		verticalRadio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// radioButtonActionPerformed(e);
			}
		});
		horizontalRadio = new JRadioButton("Horizontal Ship");
		horizontalRadio.setBorder(new EmptyBorder(10, 150, 10, 20));
		horizontalRadio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// radioButtonActionPerformed(e);
			}
		});
		shipPanel.add(verticalRadio);
		shipPanel.add(horizontalRadio);
		shipLabel = new JLabel(twoSquare + " - Two Square  " + threeSquare + " - Three Square  " + fourSquare
				+ " - Four Square  " + fiveSquare + " - Five Square", SwingConstants.CENTER);
		shipPanel.add(shipLabel);
		submitPanel = new JPanel();
		submitPanel.setVisible(true);
		submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				submitButtonActionPerformed();
			}
		});
		submitPanel.add(submitButton);
		onePanel.add(instructionPanel);
		onePanel.add(shipPanel);
		onePanel.add(submitPanel);

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
		((JButton) e.getSource()).setBackground(Color.MAGENTA);
		String clickedButton = ((JButton) e.getSource()).getText();
		System.out.println(clickedButton);
	}

	public void myButtonActionPerformed(ActionEvent e) {
		((JButton) e.getSource()).setBackground(Color.ORANGE);
		String clickedButton = ((JButton) e.getSource()).getText();
		battleshipButtons = new ArrayList<String>();
		battleshipButtons.add(clickedButton);
		System.out.println(battleshipButtons);

		String coordinate = ((JButton) e.getSource()).getText();
		char[] coords = coordinate.toCharArray();
		Character[] Coo = { coords[0], coords[1] };
		char[] letters = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J' };
		char[] numbers = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		int r = 0;
		int c = 0;

		for (int x = 0; x < 10; x++) {
			if (Coo[0].equals(letters[x])) {

				r = x;
			} else {
				System.out.print("row else " + x + " ,");
			}

			if (Coo[1].equals(numbers[x])) {
				c = x;
			} else {
				System.out.print("column else " + x + " ,");
			}
		}
		int i = 3; // size 4 ship need for loop to change color and send
					// coordinate to arrayList
		myBoard[r][c].setBackground(Color.ORANGE);
		// myBoard[row + 1][column].setBackground(Color.ORANGE); // makes ship
		// verticle
		myBoard[r][c + i].setBackground(Color.orange); // makes ship
																// horizontal
	}

	public void sendButtonActionPerformed() {
		String chat = textTextArea.getText();
		writer.println(OutgoingHandlerInterface.sendChat(chat));
		writer.flush();

		chatTextArea.append("\n" + chat);
	}

	public void submitButtonActionPerformed() {
		String username = userNameLabel.getText();
		writer.println(OutgoingHandlerInterface.login(username));
		writer.flush();

		writer.println(OutgoingHandlerInterface.sendGameBoard(battleshipButtons));
		writer.flush();
	}

	public void radioButtionActionPerformed(ActionEvent e) {
		// ((JRadioButton)e.getSource()).g
	}

}
