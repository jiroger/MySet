import processing.core.PApplet;

// col and row for card class refers to the col and row in the sprite sheet
public class Card {
	PApplet parent;
	private int col; // col # in sheet
	private int row; // row # in sheet

	public Card(int col, int row, PApplet p) {
		this.col = col;
		this.row = row;
		parent = p;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public void display(int boardCol, int boardRow) {
		if (SET_Final.state == SET_Final.State.PAUSED) {
			parent.fill(parent.color(255, 136, 0));
			parent.rect(SET_Final.GRID_LEFT_OFFSET + boardCol * (SET_Final.CARD_WIDTH + SET_Final.GRID_X_SPACER),
					SET_Final.GRID_TOP_OFFSET + boardRow * (SET_Final.CARD_HEIGHT + SET_Final.GRID_Y_SPACER),
					SET_Final.CARD_WIDTH, SET_Final.CARD_HEIGHT);
		} else {
			parent.image(SET_Final.cimg,
					SET_Final.GRID_LEFT_OFFSET + boardCol * (SET_Final.CARD_WIDTH + SET_Final.GRID_X_SPACER),
					SET_Final.GRID_TOP_OFFSET + boardRow * (SET_Final.CARD_HEIGHT + SET_Final.GRID_Y_SPACER),
					SET_Final.CARD_WIDTH, SET_Final.CARD_HEIGHT,
					SET_Final.SHEET_LEFT_OFFSET + col * SET_Final.CARD_WIDTH,
					SET_Final.SHEET_TOP_OFFSET + row * SET_Final.CARD_HEIGHT,
					(col + 1) * SET_Final.CARD_WIDTH + SET_Final.CARD_X_SPACER,
					(row + 1) * SET_Final.CARD_HEIGHT + SET_Final.CARD_Y_SPACER);
		}
	}

	public boolean equals(Card other) {
		return (col == other.col && row == other.row);
	}

	@Override
	public String toString() {
		return "C(" + col + "," + row + ")";
	}
}