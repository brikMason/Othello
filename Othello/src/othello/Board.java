package othello;

public class Board {
	private int BOARD_WIDTH = 8;
	private int BOARD_HEIGHT = 8;
	public enum CellState {
		EMPTY, BLACK, WHITE
	}
	private CellState[][] board = new CellState[BOARD_WIDTH][BOARD_HEIGHT];

	/**
	 * Draws the game board to the screen.
	 * 
	 * @author Wes Maxwell
	 * @since 2017-09-10
	 */
	public void drawBoard() {
		System.out.printf("     %d    %d    %d    %d    %d    %d    %d    %d  \n", 1, 2, 3, 4, 5, 6, 7, 8);
		for (int i = 0; i < 8; i++) {
			char s0, s1, s2, s3, s4, s5, s6, s7;
			s0 = s1 = s2 = s3 = s4 = s5 = s6 = s7 = ' ';

			if (board[i][0] == CellState.BLACK)
				s0 = 'B';
			else if (board[i][0] == CellState.WHITE)
				s0 = 'W';
			if (board[i][1] == CellState.BLACK)
				s1 = 'B';
			else if (board[i][1] == CellState.WHITE)
				s1 = 'W';
			if (board[i][2] == CellState.BLACK)
				s2 = 'B';
			else if (board[i][2] == CellState.WHITE)
				s2 = 'W';
			if (board[i][3] == CellState.BLACK)
				s3 = 'B';
			else if (board[i][3] == CellState.WHITE)
				s3 = 'W';
			if (board[i][4] == CellState.BLACK)
				s4 = 'B';
			else if (board[i][4] == CellState.WHITE)
				s4 = 'W';
			if (board[i][5] == CellState.BLACK)
				s5 = 'B';
			else if (board[i][5] == CellState.WHITE)
				s5 = 'W';
			if (board[i][6] == CellState.BLACK)
				s6 = 'B';
			else if (board[i][6] == CellState.WHITE)
				s6 = 'W';
			if (board[i][7] == CellState.BLACK)
				s7 = 'B';
			else if (board[i][7] == CellState.WHITE)
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

	/**
	 * Initializes the game board.
	 * 
	 * @author Wes Maxwell
	 * @since 2017-09-10
	 * 
	 */
	public void init() {
		for (int i = 0; i <= 7; i++)
			for (int j = 0; j <= 7; j++)
				board[i][j] = CellState.EMPTY;

		board[3][3] = CellState.WHITE;
		board[3][4] = CellState.BLACK;
		board[4][3] = CellState.BLACK;
		board[4][4] = CellState.WHITE;

	}
	
	/**
	 * Determines if the current player has a move.
	 * 
	 * @author Wes Maxwell
	 * @since 2017-09-10
	 * @return true if player has valid move, false otherwise.
	 */
	public boolean playerHasMove(String whosTurn) {
		for (int i = 1; i <= 8; i++)
			for (int j = 1; j <= 8; j++)
				if (isMoveValid(i + "," + j, true, whosTurn))
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
	public boolean isMoveValid(String input, boolean validateOnly, String whosTurn) {
		boolean valid = false;
		String[] values = input.split(",");
		int x = Integer.parseInt(values[0]);
		int y = Integer.parseInt(values[1]);
		x--;
		y--;

		if (board[x][y] != CellState.EMPTY) {
			return false;
		}

		CellState c;
		if ("Black".equals(whosTurn))
			c = CellState.BLACK;
		else
			c = CellState.WHITE;

		// Check Vertical - Up and Down
		// Up
		for (int row = x - 1; row >= 0; row--) {
			if ((x - 1) == row) {
				if (board[row][y] == c || board[row][y] == CellState.EMPTY) {
					break;
				}
			} // Testing second row up...
			else if (board[row][y] == CellState.EMPTY) {
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
				if (board[row][y] == c || board[row][y] == CellState.EMPTY) {
					break;
				}
			} // Testing second row down...
			else if (board[row][y] == CellState.EMPTY) {
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
				if (board[x][col] == c || board[x][col] == CellState.EMPTY) {
					break;
				}
			} // Testing second column left...
			else if (board[x][col] == CellState.EMPTY) {
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
				if (board[x][col] == c || board[x][col] == CellState.EMPTY) {
					break;
				}
			} // Testing second column left...
			else if (board[x][col] == CellState.EMPTY) {
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
				if (board[x - i][y - i] == c || board[x - i][y - i] == CellState.EMPTY) {
					break;
				}
			} // Testing second upper left...
			else if (board[x - i][y - i] == CellState.EMPTY) {
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
				if (board[x + i][y + i] == c || board[x + i][y + i] == CellState.EMPTY) {
					break;
				}
			} // Testing second lower right...
			else if (board[x + i][y + i] == CellState.EMPTY) {
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
			if (board[x][y] == CellState.EMPTY) {

				if ("Black".equals(whosTurn))
					board[x][y] = CellState.BLACK;
				else
					board[x][y] = CellState.WHITE;
			} else {
				System.out.println("Selected cell is not empty!");
				return false;
			}
		}
		return valid;
	}
	
	
	
	public CellState getCell(int i, int j) {
		return board [i][j];
	}
	
	/**
	 * Calculates the score and prints winner message to screen.
	 * 
	 * @author Wes Maxwell
	 * @since 2017-09-10
	 */
	public int getScore(CellState cellState) {
		int ii = 0;
		for (int i = 1; i < 8; i++)
			for (int j = 1; j < 8; j++)
				if (board[i][j] == cellState)
					ii++;
		return ii;
	}
	public String automatePlay(String whosTurn) {
		String retVal = null;
		for (int i = 1; i <= 8; i++)
			for (int j = 1; j <= 8; j++)
				if (isMoveValid(i + "," + j, true, whosTurn)) {
					retVal = i + "," + j;
				}
		
		return retVal;
	}
}
