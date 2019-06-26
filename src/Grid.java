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

	// GRID MUTATIONS

	// highlights or remove highlight
	// changes # of cards selected
	public void updateSelected(int col, int row) {
		Card card = board[col][row];

		if (selectedCards.contains(card)) {
			int index = selectedCards.indexOf(card);
			selectedLocs.remove(index);
			selectedCards.remove(card);
			// score--;
		} else {
			selectedLocs.add(new Location(col, row));
			selectedCards.add(card);
		}

		// System.out.println("Cards = " + selectedCards + ", Locations = " +
		// selectedLocs);
	}

	public void removeSet() {
		// note to self: cards should not change locations unless
		// # of cols goes down for better ux. if # cols go down, cards from rightmost
		// col moves to fill in the cards removed from selected set

		selectedLocs.sort(null); // orders the selected locations

		if (cardsInPlay > 12 || SET_Final.deck.size() == 0) {
			for (int i = 0; i < 3; i++) {
				int removedCardCol = selectedLocs.get(i).getCol(); // the column of the card in the grid we want to
																	// remove
				int removedCardRow = selectedLocs.get(i).getRow(); // the row of the card in the grid we want to remove
				int lastCard = cardsInPlay - 1; // basically both the row and column function take in a number n to
												// determine
				// the card's location. the number n is what u get if u were to squash the 2d
				// array into a 1d array. right now
				// the value lastcard has bascailly means that its looking at the last card on
				// the table
				board[removedCardCol][removedCardRow] = board[col(lastCard)][row(lastCard)]; // we replace one of the
																								// cards we want
				// to remove with a card from the far right.

				cardsInPlay--; // we just removed a card from play, so one less card
			}
			SET_Final.currentCols--; // we are collapsing the # of cols cuz we just removed one
		} else if (cardsInPlay == 12 && SET_Final.deck.size() > 0) {
			for (int i = 0; i < 3; i++) {
				int removedCardCol = selectedLocs.get(i).getCol(); // the column of the card we want to remove
				int removedCardRow = selectedLocs.get(i).getRow(); // the row of the card we want to remove
				board[removedCardCol][removedCardRow] = SET_Final.deck.deal();
				// no need for currentCols-- because the minimum # of cards is 12
			}
		}
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
		int highlight;
		if (SET_Final.state == SET_Final.State.FIND_SET) {
			highlight = FOUND_HIGHLIGHT;
			selectedLocs = findSet();
			if (selectedLocs.size() == 0) {
				SET_Final.message = 6;
				return;
			}
		} else if (selectedLocs.size() < 3) {
			highlight = SELECTED_HIGHLIGHT;
		} else {
			highlight = Set_Game_Logic.isSet(selectedCards.get(0), selectedCards.get(1), selectedCards.get(2))
					? CORRECT_HIGHLIGHT
					: INCORRECT_HIGHLIGHT;
		}
		for (Location loc : selectedLocs) {
			drawHighlight(loc, highlight);
		}
	}

	public void drawHighlight(Location loc, int highlightColor) {
		parent.stroke(highlightColor);
		parent.strokeWeight(5);
		parent.noFill();
		int col = loc.getCol();
		int row = loc.getRow();
		parent.rect(SET_Final.GRID_LEFT_OFFSET + col * (SET_Final.CARD_WIDTH + SET_Final.GRID_X_SPACER),
				SET_Final.GRID_TOP_OFFSET + row * (SET_Final.CARD_HEIGHT + SET_Final.GRID_Y_SPACER),
				SET_Final.CARD_WIDTH, SET_Final.CARD_HEIGHT);
		parent.stroke(parent.color(0, 0, 0));
		parent.strokeWeight(1);
	}

	// if there is a set on the board, existsSet() returns ArrayList containing
	// the locations of three cards that form a set, an empty ArrayList (not null)
	// otherwise
	public ArrayList<Location> findSet() {
		ArrayList<Location> locs = new ArrayList<Location>();
		for (int i = 0; i < SET_Final.currentCols * 3 - 2; i++) {
			for (int j = i + 1; j < SET_Final.currentCols * 3 - 1; j++) {
				for (int k = j + 1; k < SET_Final.currentCols * 3; k++) {
					if (Set_Game_Logic.isSet(board[col(i)][row(i)], board[col(j)][row(j)], board[col(k)][row(k)])) {
						locs.add(new Location(col(i), row(i)));
						locs.add(new Location(col(j), row(j)));
						locs.add(new Location(col(k), row(k)));
						return locs;
					}
				}
			}
		}
		return new ArrayList<Location>();
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
	// UTILITY FUNCTIONS FOR GRID CLASS

	public int col(int n) {
		return n / 3;
	}

	public int row(int n) {
		return n % 3;
	}

	public int rightOffset() {
		return SET_Final.GRID_LEFT_OFFSET + SET_Final.currentCols * (SET_Final.CARD_WIDTH + SET_Final.GRID_X_SPACER);
	}

}
