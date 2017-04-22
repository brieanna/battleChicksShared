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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

public class BattleChicks extends JFrame {
	/**
	 * created by Brie
	 */
	private static final long serialVersionUID = -3132123028681415218L;
	private Socket socket;
	private int port = 8989;
	private PrintWriter writer;
	private ArrayList<String> shipCoordinates = new ArrayList<>();
	private static JButton[][] myBoard;
	private static JButton[][] enemyGameBoard;
	private int shipSize = 0;
	private JRadioButton horizontal;
	public static JTextArea receiveChatTextArea;
	private JTextArea sendChatTextArea;
	private JTextArea userNameTextArea;
	private int xlShip;
	private int lShip;
	private int mShip;
	private int sShip;
	boolean isConnected = false;
	public static JTextArea updateTextArea;
	static boolean turn = false;
	static boolean win = false;
	static JTextArea turnTextArea;
	static JTextArea winTextArea;
	static JPanel upperRightPnl;

	static char[] letters = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J' };
	static char[] numbers = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	static int row = 0;
	static int column = 0;

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
		enemyGameBoard = createEnemyBoard(enemyBoard);
		mainPanel.add(enemyBoard);

		upperRightPnl = new JPanel();
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

		userNameTextArea = new JTextArea(2, 10);
		userNameTextArea.setEditable(true);
		upperRightPnl.add(userNameTextArea);

		JButton loginBtn = new JButton("Login");
		upperRightPnl.add(loginBtn);
		loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});

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

		receiveChatTextArea = new JTextArea(19, 29);
		receiveChatTextArea.setEditable(false);
		JScrollPane chatScrollPane = new JScrollPane(receiveChatTextArea);
		chatScrollPane.setSize(20, 30);
		chatScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		chatScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		receiveChatTextArea.setLineWrap(true);
		upperLeftPnl.add(chatScrollPane);

		sendChatTextArea = new JTextArea(10, 30);
		sendChatTextArea.setEditable(true);
		sendChatTextArea.setLineWrap(true);
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

		turnTextArea = new JTextArea(5, 30);
		turnTextArea.setEditable(false);
		turnTextArea.setText("");
		upperRightPnl.add(turnTextArea);

		updateTextArea = new JTextArea(10, 30);
		updateTextArea.setEditable(false);
		updateTextArea.setLineWrap(true);
		updateTextArea.setText("Enter your name and press Login");
		lowerRightPnl.add(updateTextArea);

		JButton resetButton = new JButton();
		resetButton.setText("RESET");
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateTextArea.setText("GameBoard has been reset. \n Place ships and press Start to play again.");
				resetCoordinates();
				clearMyBoard(myBoard, Color.BLUE);
				clearMyBoard(enemyGameBoard, Color.LIGHT_GRAY);
			}
		});
		lowerRightPnl.add(resetButton);

		JButton restartButton = new JButton();
		restartButton.setText("RESTART");
		restartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				writer.println(OutgoingHandlerInterface.restart());
				writer.flush();
				updateTextArea.setText("Game has been Restarted. Please reset your game board.");
			}
		});
		lowerRightPnl.add(restartButton);

		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		frame.setVisible(true);

	}

	protected void resetCoordinates() {
		for (int x = 0; x < shipCoordinates.size(); x++) {
			shipCoordinates.remove(x);
		}
		sShip = 0;
		mShip = 0;
		lShip = 0;
		xlShip = 0;
		
		winTextArea.setVisible(false);
		
	}

	protected void start() {
		if (shipCoordinates.size() == 19 && isConnected) {
			writer.println(OutgoingHandlerInterface.sendGameBoard(shipCoordinates));
			writer.flush();
			clearMyBoard(enemyGameBoard, Color.BLUE);
			grayMyBoard();
		} else {
			updateTextArea.setText(
					"Not all information ready to play. \n Place all ships, \n enter username and login, \n and press start to play.");
		}
	}

	protected void sendAction() {
		if (isConnected) {
			String sendTxt = sendChatTextArea.getText();
			sendChatTextArea.setText("");
			receiveChatTextArea.append("you: " + sendTxt + "\n");
			writer.println(OutgoingHandlerInterface.sendChat(sendTxt));
			writer.flush();
		} else {
			receiveChatTextArea.append("Message not sent because you are not connected \n");
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
				gameBoard[row][column].setBackground(Color.CYAN);
				gameBoard[row][column].setForeground(Color.CYAN);
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

	private JButton[][] gameBoard2 = new JButton[10][10];

	public JButton[][] createEnemyBoard(JPanel panelc) {
		panelc.setLayout(new GridLayout(10, 10));
		char letter = 'A';
		for (int row = 0; row < 10; row++) {
			for (int column = 0; column < 10; column++) {
				gameBoard2[row][column] = new JButton("" + letter + column);
				gameBoard2[row][column].setName("" + letter + column);
				// gameBoard2[row][column].setBorder(null);
				gameBoard2[row][column].setOpaque(true);
				gameBoard2[row][column].setBackground(Color.LIGHT_GRAY);
				gameBoard2[row][column].setForeground(Color.LIGHT_GRAY);
				gameBoard2[row][column].addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if (turn) {
							attackActionPerformed(e);
						} else {
							updateTextArea.setText("NOT YOUR TURN");
						}
					}
				});
				panelc.add(gameBoard2[row][column]);
			}
			letter++;
		}
		return gameBoard2;
	}

	protected void attackActionPerformed(ActionEvent e) {
		if (isConnected) {
			// send missile
			JButton myButton = ((JButton) e.getSource());
			// myButton.setBackground(Color.BLACK);
			String coordinate = myButton.getText();
			writer.println(OutgoingHandlerInterface.fire(coordinate));
			writer.flush();
			// myButton.setText("X");
		}
	}

	public static void findCoordinates(String coordinate) {
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
	}

	public void clearMyBoard(JButton[][] board, Color color) {
		for (int r = 0; r < 10; r++) {
			for (int c = 0; c < 10; c++) {
				board[r][c].setBackground(color);
				board[r][c].setForeground(color);
				board[r][c].setText("" + letters[r] + c);
			}
		}
	}

	protected void addShipActionPerformed(ActionEvent e) {
		if (isConnected) {

			String coordinate = ((JButton) e.getSource()).getText();
			findCoordinates(coordinate);

			switch (shipSize) {
			case 2:
				if (sShip < 2) {
					sShip++;
					addShipToBoard(row, column);
				} else {
					updateTextArea.setText("All small ships placed. \n Place all ships, \n and press start to play.");
				}
				break;
			case 3:
				if (mShip < 2) {
					mShip++;
					addShipToBoard(row, column);
				} else {
					updateTextArea.setText("All medium ships placed. \nPlace all ships, \n and press start to play.");
				}
				break;
			case 4:
				if (lShip < 1) {
					lShip++;
					addShipToBoard(row, column);
				} else {
					updateTextArea.setText("All large ships placed. \n Place all ships, \n and press start to play.");
				}
				break;
			case 5:
				if (xlShip < 1) {
					xlShip++;
					addShipToBoard(row, column);
				} else {
					updateTextArea
							.setText("All extra large ships placed. \n Place all ships, \n and press start to play.");
				}
				break;
			default:
				updateTextArea.setText(
						"Select ship size and orientation to place ships.\n Place all ships, \n and press start to play.");
				break;

			}
		} else {
			updateTextArea.setText("Login before placing ships");
		}

	}

	private void addShipToBoard(int row, int column) {
		// TODO: add functionality so that ships cannot overlap or go off the board.
		for (int y = 0; y < shipSize; y++) {
			if (horizontal.isSelected()) {
				myBoard[row][column + y].setBackground(Color.MAGENTA);
				String coordinate = myBoard[row][column + y].getText();
				shipCoordinates.add(coordinate);
			} else { // vertical
				myBoard[row + y][column].setBackground(Color.MAGENTA);
				String coordinate = myBoard[row + y][column].getText();
				shipCoordinates.add(coordinate);
			}
		}
	}

	public void login() {
		String name = userNameTextArea.getText();
		if (!name.equals("")) {
			try {
				socket = new Socket(InetAddress.getByName("ec2-52-41-213-54.us-west-2.compute.amazonaws.com"), port);
				writer = new PrintWriter(socket.getOutputStream());

				writer.println(OutgoingHandlerInterface.login(name));
				writer.flush();

				BattleChicks bc = new BattleChicks();

				new Thread(new MessageReader(socket, bc)).start();

				isConnected = true;
				clearMyBoard(myBoard, Color.BLUE);

				turnTextArea.setText(
						"Place Ships and press start to play. \n 1 XL ship \n 1 L ship \n 2 M ships \n 2 S ships");

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			updateTextArea.setText(
					"Not all information ready to play. \n Place all ships, \n enter username and login, \n and press start to play.");
		}
	}

	public void grayMyBoard() {
		for (int r = 0; r < 10; r++) {
			for (int c = 0; c < 10; c++) {
				if (myBoard[r][c].getBackground().equals(Color.BLUE)) {
					myBoard[r][c].setBackground(Color.LIGHT_GRAY);
				} else {
					myBoard[r][c].setBackground(Color.PINK);
				}
			}
		}
	}

	public static void setChatMessage(String send) {
		receiveChatTextArea.append(send + "\n");
	}

	public static void updateTextArea(String update) {
		updateTextArea.setText(update + "\n");
	}

	public static void setTurn(Boolean myTurn) {
		if (!win) {
			turn = myTurn;
		}
		if (turn) {
			turnTextArea.setText("Your Turn");
		} else {
			turnTextArea.setText("NOT your turn");
		}
	}
	
	public static void setWin(String winMessage){
		win = true;
		winTextArea = new JTextArea(5, 30);
		winTextArea.setEditable(false);
		winTextArea.setText("***** " + winMessage + " *****");
		winTextArea.setForeground(Color.RED);
		upperRightPnl.add(winTextArea);
	}

	public static void hitMiss(Boolean hit, String coordinate) {
		findCoordinates(coordinate);
		if (turn && hit) {
			updateTextArea.setText("   HIT");
			enemyGameBoard[row][column].setBackground(Color.RED);
			enemyGameBoard[row][column].setText("X");
			enemyGameBoard[row][column].setForeground(Color.RED);
		} else if (turn && !hit) {
			updateTextArea.setText("   MISS");
			enemyGameBoard[row][column].setBackground(Color.BLACK);
			enemyGameBoard[row][column].setText("O");
			enemyGameBoard[row][column].setForeground(Color.BLACK);
		} else if (!turn && hit) {
			updateTextArea.setText("   HIT");
			myBoard[row][column].setBackground(Color.RED);
			myBoard[row][column].setText("X");
			myBoard[row][column].setForeground(Color.RED);
		} else if (!turn && !hit) {
			updateTextArea.setText("   MISS");
			myBoard[row][column].setBackground(Color.BLACK);
			myBoard[row][column].setText("O");
			myBoard[row][column].setForeground(Color.BLACK);
		}

	}

}
