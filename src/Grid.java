import java.util.ArrayList;

import processing.core.PApplet;

public class Grid {
	PApplet parent;
	public final int SELECTED_HIGHLIGHT;
	public final int CORRECT_HIGHLIGHT;
	public final int INCORRECT_HIGHLIGHT;
	public final int FOUND_HIGHLIGHT;
	// In the physical SET game, cards are placed on the table.
	// The table contains the grid of cards and is typically called the board.
	Card[][] board = new Card[SET_Final.MAX_COLS][SET_Final.ROWS]; // Array that contains cards

	ArrayList<Location> selectedLocs = new ArrayList<Location>(); // Locations selected by the player
	ArrayList<Card> selectedCards = new ArrayList<Card>(); // Cards selected by the player
															// (corresponds to the locations)
	int cardsInPlay; // Number of cards visible on the board

	public Grid(PApplet p) {
		cardsInPlay = 0;
		parent = p;
		SELECTED_HIGHLIGHT = parent.color(255, 221, 0);
		CORRECT_HIGHLIGHT = parent.color(0, 255, 0);
		INCORRECT_HIGHLIGHT = parent.color(255, 0, 0);
		FOUND_HIGHLIGHT = parent.color(17, 204, 204);
	}

	// DISPLAY CODE

	public void display() {

		int cols = cardsInPlay / 3;
		for (int col = 0; col < cols; col++) {
			for (int row = 0; row < SET_Final.ROWS; row++) {
				board[col][row].display(col, row);
			}
		}
	}

	public void highlightSelectedCards() {
		// TODO Auto-generated method stub

	}

	public boolean tripleSelected() {
		// TODO Auto-generated method stub
		return false;
	}

	public void processTriple() {
		// TODO Auto-generated method stub

	}

	public void addCardToBoard(Object deal) {
		// TODO Auto-generated method stub

	}

	public void clearSelected() {
		// TODO Auto-generated method stub

	}

}
