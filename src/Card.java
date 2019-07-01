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
		if (Set.state == Set.State.PAUSED) {
			parent.fill(parent.color(255, 136, 0));
			parent.rect(Set.GRID_LEFT_OFFSET + boardCol * (Set.CARD_WIDTH + Set.GRID_X_SPACER),
					Set.GRID_TOP_OFFSET + boardRow * (Set.CARD_HEIGHT + Set.GRID_Y_SPACER),
					Set.CARD_WIDTH, Set.CARD_HEIGHT);
		} else {
			parent.image(Set.cimg,
					Set.GRID_LEFT_OFFSET + boardCol * (Set.CARD_WIDTH + Set.GRID_X_SPACER),
					Set.GRID_TOP_OFFSET + boardRow * (Set.CARD_HEIGHT + Set.GRID_Y_SPACER),
					Set.CARD_WIDTH, Set.CARD_HEIGHT,
					Set.SHEET_LEFT_OFFSET + col * Set.CARD_WIDTH,
					Set.SHEET_TOP_OFFSET + row * Set.CARD_HEIGHT,
					(col + 1) * Set.CARD_WIDTH + Set.CARD_X_SPACER,
					(row + 1) * Set.CARD_HEIGHT + Set.CARD_Y_SPACER);
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