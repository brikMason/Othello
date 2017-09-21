package othello;

import java.util.Scanner;

/**
 * Implements game Othello.
 * 
 * @author Wes Maxwell
 * @since 2017-09-10
 *
 */
public class Base {
	public static void main(String[] args) {
		new Base();

	}

	private String whosTurn;
	private int playerCannotMove = 0;

	/**
	 * Constructor for Base, the driving class for Othello.
	 * 
	 * @author Wes Maxwell
	 * @since 2017-09-10
	 * 
	 */
	public Base() {
		Scanner scanner = null;
		whosTurn = "Black";
		try {
			scanner = new Scanner(System.in);

			initBoard();

			while (true) {
				drawBoard();
				if (!currentPlayerHasMove()) {
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
					String input = scanner.nextLine();

					if ("QUIT".equals(input))
						break;

					if (!isDataValid(input)) {
						System.out.println("Input is invald. " + input);
						continue;
					}

					if (!isMoveValid(input, false)) {
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
	 * Calculates the score and prints winner message to screen.
	 * 
	 * @author Wes Maxwell
	 * @since 2017-09-10
	 */
	private void printScore() {
		int b = 0, w = 0;
		for (int i = 1; i < 8; i++)
			for (int j = 1; j < 8; j++)
				if (board[i][j] == cellState.BLACK)
					b++;
				else if (board[i][j] == cellState.WHITE)
					w++;

		System.out.println("=======================================");
		System.out.println("==               SCORE               ==");
		System.out.println("=======================================");
		System.out.println("BLACK: " + b);
		System.out.println("WHITE: " + w);

		if (b > w) {
			System.out.println("\nCongratulations BLACK!");
		} else if (b < w) {
			System.out.println("\nCongratulations WHITE!");
		} else {
			System.out.println("\nWOW!!! A tie!");
		}

		return;
	}

	/**
	 * Determines if the current player has a move.
	 * 
	 * @author Wes Maxwell
	 * @since 2017-09-10
	 * @return true if player has valid move, false otherwise.
	 */
	private boolean currentPlayerHasMove() {
		for (int i = 1; i <= 8; i++)
			for (int j = 1; j <= 8; j++)
				if (isMoveValid(i + "," + j, true))
					return true;

		return false;
	}

	/**
	 * This function verifies the current player can make a valid move if validate
	 * only is true and updates the board with the players move after validating the
	 * move is valid if validateOnly is false.
	 * 
	 * @param input
	 * @param validateOnly
	 * @return true if move is valid, false otherwise.
	 */
	private boolean isMoveValid(String input, boolean validateOnly) {
		boolean valid = false;
		String[] values = input.split(",");
		int x = Integer.parseInt(values[0]);
		int y = Integer.parseInt(values[1]);
		x--;
		y--;

		if (board[x][y] != cellState.EMPTY) {
			return false;
		}

		cellState c;
		if ("Black".equals(whosTurn))
			c = cellState.BLACK;
		else
			c = cellState.WHITE;

		// Check Vertical - Up and Down
		// Up
		for (int row = x - 1; row >= 0; row--) {
			if ((x - 1) == row) {
				if (board[row][y] == c || board[row][y] == cellState.EMPTY) {
					break;
				}
			} // Testing second row up...
			else if (board[row][y] == cellState.EMPTY) {
				break;
			} else if (board[row][y] != c) {
				continue;
			} else {
				if (validateOnly)
					return true;

				// change column
				for (int i = row; i < x; i++) {
					board[i][y] = c;
				}
				valid = true;
				break;
			}
		}
		// Down
		for (int row = x + 1; row <= 7; row++) {
			if ((x + 1) == row) {
				if (board[row][y] == c || board[row][y] == cellState.EMPTY) {
					break;
				}
			} // Testing second row down...
			else if (board[row][y] == cellState.EMPTY) {
				break;
			} else if (board[row][y] != c) {
				continue;
			} else {
				if (validateOnly)
					return true;

				// change column
				for (int i = row; i > x; i--) {
					board[i][y] = c;
				}
				valid = true;
				break;
			}
		}

		// Check Horizontal - Left and Right
		// Left
		for (int col = y - 1; col >= 0; col--) {
			if ((y - 1) == col) {
				if (board[x][col] == c || board[x][col] == cellState.EMPTY) {
					break;
				}
			} // Testing second column left...
			else if (board[x][col] == cellState.EMPTY) {
				break;
			} else if (board[x][col] != c) {
				continue;
			} else {
				if (validateOnly)
					return true;

				// change row
				for (int i = col; i < y; i++) {
					board[x][i] = c;
				}
				valid = true;
				break;
			}
		}
		// Right
		for (int col = y + 1; col <= 7; col++) {
			if ((y + 1) == col) {
				if (board[x][col] == c || board[x][col] == cellState.EMPTY) {
					break;
				}
			} // Testing second column left...
			else if (board[x][col] == cellState.EMPTY) {
				break;
			} else if (board[x][col] != c) {
				continue;
			} else {
				if (validateOnly)
					return true;

				// change row
				for (int i = col; i > y; i--) {
					board[x][i] = c;
				}
				valid = true;
				break;
			}
		}

		// Check Diagonal - Both U&D and L&R
		// Upper Left
		for (int i = 1; (x - i) >= 0 && (y - i) >= 0; i++) {
			if (i == 1) {
				if (board[x - i][y - i] == c || board[x - i][y - i] == cellState.EMPTY) {
					break;
				}
			} // Testing second upper left...
			else if (board[x - i][y - i] == cellState.EMPTY) {
				break;
			} else if (board[x - i][y - i] != c) {
				continue;
			} else {
				if (validateOnly)
					return true;

				// change diag UL
				for (int j = i; j > 0; j--) {
					board[x - j][y - j] = c;
				}
				valid = true;
				break;
			}
		}
		// Lower Right
		for (int i = 1; (x + i) <= 7 && (y + i) <= 7; i++) {
			if (i == 1) {
				if (board[x + i][y + i] == c || board[x + i][y + i] == cellState.EMPTY) {
					break;
				}
			} // Testing second lower right...
			else if (board[x + i][y + i] == cellState.EMPTY) {
				break;
			} else if (board[x + i][y + i] != c) {
				continue;
			} else {
				if (validateOnly)
					return true;

				// change diag LR
				for (int j = i; j > 0; j--) {
					board[x + j][y + j] = c;
				}
				valid = true;
				break;
			}
		}

		// !Is Valid
		if (valid) {
			if (board[x][y] == cellState.EMPTY) {

				if ("Black".equals(whosTurn))
					board[x][y] = cellState.BLACK;
				else
					board[x][y] = cellState.WHITE;
			} else {
				System.out.println("Selected cell is not empty!");
				return false;
			}
		}
		return valid;
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

	public enum cellState {
		EMPTY, BLACK, WHITE
	}

	private cellState[][] board = new cellState[8][8];

	/**
	 * Initializes the game board.
	 * 
	 * @author Wes Maxwell
	 * @since 2017-09-10
	 * 
	 */
	private void initBoard() {
		for (int i = 0; i <= 7; i++)
			for (int j = 0; j <= 7; j++)
				board[i][j] = cellState.EMPTY;

		board[3][3] = cellState.WHITE;
		board[3][4] = cellState.BLACK;
		board[4][3] = cellState.BLACK;
		board[4][4] = cellState.WHITE;

	}

	/**
	 * Draws the game board to the screen.
	 * 
	 * @author Wes Maxwell
	 * @since 2017-09-10
	 */
	private void drawBoard() {
		System.out.printf("     %d    %d    %d    %d    %d    %d    %d    %d  \n", 1, 2, 3, 4, 5, 6, 7, 8);
		for (int i = 0; i < 8; i++) {
			char s0, s1, s2, s3, s4, s5, s6, s7;
			s0 = s1 = s2 = s3 = s4 = s5 = s6 = s7 = ' ';

			if (board[i][0] == cellState.BLACK)
				s0 = 'B';
			else if (board[i][0] == cellState.WHITE)
				s0 = 'W';
			if (board[i][1] == cellState.BLACK)
				s1 = 'B';
			else if (board[i][1] == cellState.WHITE)
				s1 = 'W';
			if (board[i][2] == cellState.BLACK)
				s2 = 'B';
			else if (board[i][2] == cellState.WHITE)
				s2 = 'W';
			if (board[i][3] == cellState.BLACK)
				s3 = 'B';
			else if (board[i][3] == cellState.WHITE)
				s3 = 'W';
			if (board[i][4] == cellState.BLACK)
				s4 = 'B';
			else if (board[i][4] == cellState.WHITE)
				s4 = 'W';
			if (board[i][5] == cellState.BLACK)
				s5 = 'B';
			else if (board[i][5] == cellState.WHITE)
				s5 = 'W';
			if (board[i][6] == cellState.BLACK)
				s6 = 'B';
			else if (board[i][6] == cellState.WHITE)
				s6 = 'W';
			if (board[i][7] == cellState.BLACK)
				s7 = 'B';
			else if (board[i][7] == cellState.WHITE)
				s7 = 'W';

			System.out.printf("%s \n", "  -----------------------------------------");
			System.out.printf("%s \n", "  |    |    |    |    |    |    |    |    |");

			System.out.printf("%d %s %c %s %c %s %c %s %c %s %c %s %c %s %c %s %c %s  %d\n", (i + 1), "| ", s0, "| ",
					s1, "| ", s2, "| ", s3, "| ", s4, "| ", s5, "| ", s6, "| ", s7, "|", (i + 1));

			System.out.printf("%s \n", "  |    |    |    |    |    |    |    |    |");
		}
		System.out.printf("%s \n", "  -----------------------------------------");
		System.out.printf("     %d    %d    %d    %d    %d    %d    %d    %d  \n", 1, 2, 3, 4, 5, 6, 7, 8);
	}
}
