package othello;

import java.util.Scanner;

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

	public void title() {
		String t = "Othello\n" + "\n" + "by Wes Maxwell\n" + "Fall 2017\n" + "\n" + "Options:\n"
				+ "  1. 1-player: Human vs. AI\n" + "  2. 2-player: Human vs. Human\n" + "  3. Simulation: AI vs. AI\n";

		System.out.print(t);
		Scanner scanner = null;
		try {
			scanner = new Scanner(System.in);
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
					System.out.println("-SIMULATION MODE-");
					playMode = PLAY_MODE.SIMULATION;
					System.out.print("Enter simulation count: ");
					input = scanner.nextLine();
					System.out.println(">>>>>>>" + input);
					try {
						simulationCount = Integer.parseInt(input);

					} catch (Exception e) {
						System.out.println("Input Error");
						continue;
					}
					break;
				} else {
					System.out.println("Invalid input. Please enter 1, 2, or 3.");
				}

			}
		} catch (Exception e) {

		} finally {
			scanner.close();
		}
	}

	public void play() {
		if (playMode == PLAY_MODE.SINGLE_PLAYER || playMode == PLAY_MODE.TWO_PLAYER) {
			go();
		} else {
			while (simulationCount > 0) {
				simulationCount--;
				go();
			}
		}
	}

	public void go() {
		Scanner scanner = null;
		whosTurn = "Black";
		try {
			scanner = new Scanner(System.in);

			while (true) {
				// board.drawBoard();

				if (playMode == PLAY_MODE.SIMULATION) {
					System.out.println("Simulations left: " + simulationCount);
					System.out.println("***************************************");
				} else {
					board.drawBoard();
				}

				if (!board.playerHasMove(whosTurn)) {
					playerCannotMove++;

					if (playerCannotMove > 1) {
						System.out.println("NO PLAYER CAN MOVE - QUITTING");
						break;
					} else {
						System.out.println(whosTurn + " has no valid moves they must PASS.");
					}
				} else {
					playerCannotMove = 0;

					System.out.print(whosTurn + " Enter your move (row, column): ");
					String input = null;
					if (playMode == PLAY_MODE.TWO_PLAYER) {
						input = scanner.nextLine();
					} else if (playMode == PLAY_MODE.SINGLE_PLAYER && "Black".equals(whosTurn)) {
						input = scanner.nextLine();
					} else {

						// if(playMode == PLAY_MODE.SIMULATION)
						// simulationCount --;

						input = board.automatePlay(whosTurn);
						System.out.println(input);
					}

					if ("QUIT".equals(input))
						break;

					if (!isDataValid(input)) {
						System.out.println("Input is invald. " + input);
						continue;
					}

					if (!board.isMoveValid(input, false, whosTurn)) {
						System.out.println("Move is illegal.");
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
		System.out.println("=======================================");
		System.out.println("==               SCORE               ==");
		System.out.println("=======================================");
		System.out.println("BLACK: " + black);
		System.out.println("WHITE: " + white);

		if (black > white) {
			System.out.println("\nCongratulations BLACK!");
		} else if (black < white) {
			System.out.println("\nCongratulations WHITE!");
		} else {
			System.out.println("\nWOW!!! A tie!");
		}

		return;
	}

}
