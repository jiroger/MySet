public class Location implements Comparable<Location> {
	private int col;
	private int row;

	public Location(int col, int row) { // this class needed for the highlights around cards in sprite sheet
		this.col = col;
		this.row = row;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public int getRow() {
		return row;
	}

	public int getIndex() {
		return 3 * col + row;
	}

	public boolean equals(Location other) {
		return (col == other.getCol()) && (row == other.getRow());
	}

	@Override
	public int compareTo(Location other) {
		return getIndex() - other.getIndex();
	}

	@Override
	public String toString() {
		return ("[" + col + "," + row + "]");
	}
}