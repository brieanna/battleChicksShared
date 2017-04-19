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

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

public class BattleChicks extends JFrame {
	private Socket socket;
	private int port = 8989;
	private PrintWriter writer;
	private ArrayList<String> shipCoordinates = new ArrayList<>();
	private JButton[][] myBoard;
	private int shipSize = 0;
	private JRadioButton horizontal;
	public JTextArea recieveChatTextArea;
	private JTextArea sendChatTextArea;
	private JTextArea userNameTextArea;
	private int xlShip;
	private int lShip;
	private int mShip;
	private int sShip;
	JTextArea updateTextArea;
	boolean isConnected = false;

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

		userNameTextArea = new JTextArea(3, 20);
		userNameTextArea.setEditable(true);
		upperRightPnl.add(userNameTextArea);

		horizontal = new JRadioButton("horizontal              ");
		horizontal.setSelected(true);
		JRadioButton vertical = new JRadioButton("vertical                  ");
		ButtonGroup directionBtnGroup = new ButtonGroup();
		directionBtnGroup.add(horizontal);
		directionBtnGroup.add(vertical);
		upperRightPnl.add(horizontal);
		upperRightPnl.add(vertical);

		JRadioButton xLargeShip = new JRadioButton("XL Ship");
		xLargeShip.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				shipSize = 5;
			}
		});
		JRadioButton largeShip = new JRadioButton("L Ship");
		largeShip.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				shipSize = 4;
			}
		});
		JRadioButton mediumShip = new JRadioButton("M Ship");
		mediumShip.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				shipSize = 3;
			}
		});
		JRadioButton smallShip = new JRadioButton("S Ship");
		smallShip.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				shipSize = 2;
			}
		});
		ButtonGroup shipBtnGroup = new ButtonGroup();
		shipBtnGroup.add(xLargeShip);
		shipBtnGroup.add(largeShip);
		shipBtnGroup.add(mediumShip);
		shipBtnGroup.add(smallShip);
		upperRightPnl.add(xLargeShip);
		upperRightPnl.add(largeShip);
		upperRightPnl.add(mediumShip);
		upperRightPnl.add(smallShip);

		recieveChatTextArea = new JTextArea(20, 30);
		recieveChatTextArea.setEditable(false);
		upperLeftPnl.add(recieveChatTextArea);

		sendChatTextArea = new JTextArea(10, 30);
		sendChatTextArea.setEditable(true);
		lowerLeftPnl.add(sendChatTextArea);

		JButton sendButton = new JButton();
		sendButton.setText("send");
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendAction();
			}
		});
		lowerLeftPnl.add(sendButton);

		JButton startButton = new JButton();
		startButton.setText("START");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				start();
			}
		});
		upperRightPnl.add(startButton);

		updateTextArea = new JTextArea(10, 30);
		updateTextArea.setEditable(false);
		updateTextArea.setText("Place ships then press START");
		lowerRightPnl.add(updateTextArea);

		JButton resetButton = new JButton();
		resetButton.setText("RESET");
		lowerRightPnl.add(resetButton);

		frame.setVisible(true);
	}

	protected void sendAction() {
		if (isConnected) {
			String sendTxt = sendChatTextArea.getText();
			sendChatTextArea.setText("");
			recieveChatTextArea.append(sendTxt + "\n");
			writer.println(OutgoingHandlerInterface.sendChat(sendTxt));
			writer.flush();
		} else {
			recieveChatTextArea.append("Message not sent because you are not connected \n");
		}
	}

	private JButton[][] gameBoard = new JButton[10][10];

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
				gameBoard[row][column].setBackground(Color.lightGray);
				gameBoard[row][column].setForeground(Color.GRAY);
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
		if (isConnected) {
			// send missle
			((JButton) e.getSource()).setBackground(Color.BLACK);
			((JButton) e.getSource()).setText("X");
			System.out.println("Enemy board name: " + ((JButton) e.getSource()).getName());
		}
	}

	protected void addShipActionPerformed(ActionEvent e) {
		if (isConnected) {
			// does nothing
		} else {
			char[] letters = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J' };
			char[] numbers = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
			int row = 0;
			int column = 0;

			String coordinate = ((JButton) e.getSource()).getText();

			char[] coords = coordinate.toCharArray();
			Character[] Coo = { coords[0], coords[1] };

			for (int x = 0; x < 10; x++) {
				if (Coo[0].equals(letters[x])) {
					row = x;
				} else {
				}

				if (Coo[1].equals(numbers[x])) {
					column = x;
				} else {
				}
			}
			switch (shipSize) {
			case 2:
				if (sShip < 2) {
					sShip++;
					addShipToBoard(row, column);
				} else {
					updateTextArea.setText(
							"All small ships placed. \n Place all ships, \n enter username, \n and press start to play.");
				}
				break;
			case 3:
				if (mShip < 2) {
					mShip++;
					addShipToBoard(row, column);
				} else {
					updateTextArea.setText(
							"All medium ships placed. \nPlace all ships, \n enter username, \n and press start to play.");
				}
				break;
			case 4:
				if (lShip < 1) {
					lShip++;
					addShipToBoard(row, column);
				} else {
					updateTextArea.setText(
							"All large ships placed. \n Place all ships, \n enter username, \n and press start to play.");
				}
				break;
			case 5:
				if (xlShip < 1) {
					xlShip++;
					addShipToBoard(row, column);
				} else {
					updateTextArea.setText(
							"All extra large ships placed. \n Place all ships, \n enter username, \n and press start to play.");
				}
				break;
			default:
				updateTextArea.setText(
						"Select ship size and orientation to place ships.\n Place all ships, \n enter username, \n and press start to play.");
				break;

			}
		}

	}

	private void addShipToBoard(int row, int column) {
		for (int y = 0; y < shipSize; y++) {
			if (horizontal.isSelected()) {
				myBoard[row][column + y].setBackground(Color.WHITE);
				String coordinate = myBoard[row][column + y].getText();
				shipCoordinates.add(coordinate);
			} else { // vertical
				myBoard[row + y][column].setBackground(Color.WHITE);
				String coordinate = myBoard[row + y][column].getText();
				shipCoordinates.add(coordinate);
			}
		}
	}

	public void start() {
		String name = userNameTextArea.getText();
		if (shipCoordinates.size() == 19 && !name.equals("")) {
			try {
//				socket = new Socket(InetAddress.getByName("ec2-52-41-213-54.us-west-2.compute.amazonaws.com"), port);
				socket = new Socket(InetAddress.getByName("137.190.250.60"), port);
				writer = new PrintWriter(socket.getOutputStream());

				writer.println(OutgoingHandlerInterface.login(name));
				writer.flush();

				writer.println(OutgoingHandlerInterface.sendGameBoard(shipCoordinates));
				writer.flush();
				// writer.println(OutgoingHandlerInterface.restart());
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
				BattleChicks bc = new BattleChicks();

				new Thread(new MessageReader(socket, bc/* , gb */)).start();

				// Scanner in = new Scanner(System.in);
				// String spot = in.nextLine();
				// writer.println(OutgoingHandlerInterface.fire(spot));
				// writer.flush();
				//
				// String chat = in.nextLine();
				// writer.println(OutgoingHandlerInterface.sendChat(chat));
				// writer.flush();
				isConnected = true;
				updateTextArea.setText("You are now connected!");

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			updateTextArea.setText(
					"Not all information ready to play. \n Place all ships, \n enter username, \n and press start to play.");
		}
	}

	public static void main(String[] args) {
		BattleChicks bc = new BattleChicks();
		bc.runBattleGUI();
		// bc.Start();
	}
}
