package othello;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import othello.Board.CellState;

/**
 * Implements game Othello.
 * 
 * @author Wes Maxwell
 * @since 2017-09-10
 *
 */
public class Othello {
	enum PLAY_MODE {
		SINGLE_PLAYER, TWO_PLAYER, SIMULATION
	}

	private PLAY_MODE playMode = null;
	private int simulationCount = 0;

	public static void main(String[] args) {
		Othello othello = new Othello();
		othello.title();

		othello.play();

	}

	private Board board;
	private Scanner scanner = new Scanner(System.in);
	private String whosTurn;
	private int playerCannotMove = 0;

	/**
	 * Constructor for Othello, the driving class for Othello.
	 * 
	 * @author Wes Maxwell
	 * @since 2017-09-10
	 * 
	 */

	public Othello() {
		this.board = new Board();
		board.init();
	}

	/**
	 * Title display for Othello, allows user to select mode of play.
	 * 
	 * @author wesmaxwell
	 * @since 2017-10-6
	 * 
	 */

	public void title() {
		String t = "Othello\n" + "\n" + "by Wes Maxwell\n" + "Fall 2017\n" + "\n" + "Options:\n"
				+ "  1. 1-player: Human vs. AI\n" + "  2. 2-player: Human vs. Human\n" + "  3. Simulation: AI vs. AI\n";

		System.out.print(t);
		try {
			while (true) {

				String input = scanner.nextLine();
				if ("1".equals(input)) {
					System.out.print("Single player\n");
					playMode = PLAY_MODE.SINGLE_PLAYER;
					break;
				} else if ("2".equals(input)) {
					System.out.print("2player\n");
					playMode = PLAY_MODE.TWO_PLAYER;
					break;
				} else if ("3".equals(input)) {
					log(Boolean.TRUE, "-SIMULATION MODE-");
					playMode = PLAY_MODE.SIMULATION;
					System.out.print("Enter simulation count: ");
					input = scanner.nextLine();
					log(Boolean.TRUE, ">>>>>>>" + input);
					try {
						simulationCount = Integer.parseInt(input);

					} catch (Exception e) {
						log(Boolean.TRUE, "Input Error");
						continue;
					}
					break;
				} else {
					log(Boolean.TRUE, "Invalid input. Please enter 1, 2, or 3.");
				}

			}
		} catch (Exception e) {

		} finally {
			// scanner.close();
		}
	}

	/**
	 * Main handler for printing scores and analyzing results.
	 * 
	 * @author wesmaxwell
	 * @since 2017-10-6
	 * 
	 */

	public void play() {
		if (playMode == PLAY_MODE.SINGLE_PLAYER || playMode == PLAY_MODE.TWO_PLAYER) {
			go();
		} else {
			ArrayList<Integer> blackScores = new ArrayList<Integer>();
			ArrayList<Integer> whiteScores = new ArrayList<Integer>();

			while (simulationCount > 0) {
				simulationCount--;
				log(Boolean.TRUE, "Simulations left: " + simulationCount);
				log(Boolean.TRUE, "***************************************");
				go();
				blackScores.add(board.getScore(CellState.BLACK));
				whiteScores.add(board.getScore(CellState.WHITE));
				board.init();
			}

			analyze(blackScores, whiteScores);
		}
	}

	// This method runs analysis on the spread of the scores.
	private void analyze(ArrayList<Integer> blackScores, ArrayList<Integer> whiteScores) {

		Map<Integer, Integer> data = new TreeMap<Integer, Integer>();
		for (int i = 0; i < blackScores.size(); i++) {
			int diff = blackScores.get(i) - whiteScores.get(i);
			if (data.containsKey(diff)) {
				data.put(diff, data.get(diff) + 1);
			} else {
				data.put(diff, 1);
			}
		}
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("Othello_Analysis.csv", "UTF-8");
			int total = 0;
			int count = 0;
			log(Boolean.TRUE, "Spread - Occurances");
			for (Integer key : data.keySet()) {
				total += data.get(key);
				count += key * data.get(key);

				log(Boolean.TRUE, key + ", " + data.get(key));
				writer.println(key + "," + data.get(key));

			}
			log(Boolean.TRUE, "AVG " + (double) ((double) total / (double) count));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			if (null != writer) {
				writer.flush();
				writer.close();
			}
		}

	}

	/**
	 * This method draws the board and accepts and validates user input.
	 * 
	 * @author wesmaxwell
	 * @since 2017-10-12
	 */
	public void go() {
		whosTurn = "Black";
		try {

			while (true) {

				if (!board.playerHasMove(whosTurn)) {
					playerCannotMove++;

					if (playerCannotMove > 1) {
						log(Boolean.TRUE, "NO PLAYER CAN MOVE - QUITTING");
						break;
					} else {
						log(Boolean.FALSE, whosTurn + " has no valid moves they must PASS.");
					}
				} else {
					playerCannotMove = 0;

					String input = null;
					if (playMode == PLAY_MODE.TWO_PLAYER) {
						board.drawBoard();
						System.out.print(whosTurn + " Enter your move (row, column): ");
						input = scanner.nextLine();
					} else if (playMode == PLAY_MODE.SINGLE_PLAYER && "Black".equals(whosTurn)) {
						board.drawBoard();
						System.out.print(whosTurn + " Enter your move (row, column): ");
						input = scanner.nextLine();
					} else {
						input = board.automatePlay(whosTurn);
					}

					if ("QUIT".equals(input.toUpperCase()))
						break;

					if (!isDataValid(input)) {
						log(Boolean.TRUE, "Input is invald. " + input);
						continue;
					}

					if (!board.isMoveValid(input, false, whosTurn)) {
						log(Boolean.TRUE, "Move is illegal.");
						continue;
					}
				}

				if ("Black".equals(whosTurn))
					whosTurn = "White";
				else
					whosTurn = "Black";
			}

			printScore();
		} finally {
			if (scanner != null)
				scanner.close();
			scanner = null;
		}

	}

	/**
	 * Determines if player input is valid.
	 * 
	 * 
	 * @author Wes Maxwell
	 * @since 2017-09-10
	 * @param input
	 *            string row,column
	 * @return true if data is valid, false otherwise.
	 */
	private boolean isDataValid(String input) {
		String[] values = input.split(",");
		if (2 != values.length) {
			return false;
		}
		// Validate X value
		try {
			int val = Integer.parseInt(values[0]);
			if (val < 1 || val > 8)
				return false;
		} catch (NumberFormatException e) {
			return false;
		}

		// Validate Y value
		try {
			int val = Integer.parseInt(values[1]);
			if (val < 1 || val > 8)
				return false;
		} catch (NumberFormatException e) {
			return false;
		}

		return true;
	}

	/**
	 * Calculates the score and prints winner message to screen.
	 * 
	 * @author Wes Maxwell
	 * @since 2017-09-10
	 */
	private void printScore() {
		int black = board.getScore(CellState.BLACK);
		int white = board.getScore(CellState.WHITE);
		log(Boolean.TRUE, "=======================================");
		log(Boolean.TRUE, "==               SCORE               ==");
		log(Boolean.TRUE, "=======================================");
		log(Boolean.TRUE, "BLACK: " + black);
		log(Boolean.TRUE, "WHITE: " + white);

		if (black > white) {
			log(Boolean.TRUE, "\nCongratulations BLACK!");
		} else if (black < white) {
			log(Boolean.TRUE, "\nCongratulations WHITE!");
		} else {
			log(Boolean.TRUE, "\nWOW!!! A tie!");
		}

		return;
	}

	// This method determines if the board should be printed or hidden.
	private void log(Boolean pnt, String msg) {
		if (playMode == PLAY_MODE.SIMULATION) {
			if (pnt) {
				System.out.println(msg);
			}
		} else {
			System.out.println(msg);
		}
	}
}
