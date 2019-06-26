
public class Set_Game_Logic {
	static boolean allEqual(int a, int b, int c) {
		return (a == b) && (b == c);
	}

	static boolean allDifferent(int a, int b, int c) {
		return (a != b) && (b != c) && (a != c);
	}

	// super imortant below. how tf did i get the color of the card when the card
	// doesnt have a Color variable?
	// go back to the card class and read the top two lines of comment.
	// the columns and rows do NOT REFER TO THE COLS AND ROWS ON THE ACTUAL GRID.
	// they refer to the column and rows on this pic
	// https://amiealbrecht.files.wordpress.com/2016/08/set-cards.jpg?w=1250
	// so when u look at the card class's getCol, it actually returns the column the
	// card was from in the image.
	// so the single striped green diamond would be in column 1 of the image.
	// so really, if we want to compare the prpoerties (color, stripedness, shape,
	// etc), then we have to compare its position
	// on the chart with that of other shapes.

	static boolean sameColor(Card a, Card b, Card c) {
		return allEqual(a.getCol() / 3, b.getCol() / 3, c.getCol() / 3); // everything in first 3 rows same color (so /
																			// 3),
		// everything in the next 3 rows same color, etc.
	}

	static boolean sameShape(Card a, Card b, Card c) {
		return allEqual(a.getCol() % 3, b.getCol() % 3, c.getCol() % 3); // try these formulas for urself; they work
	}

	static boolean sameFill(Card a, Card b, Card c) {
		return allEqual(a.getRow() / 3, b.getRow() / 3, c.getRow() / 3);
	}

	static boolean sameCount(Card a, Card b, Card c) {
		return allEqual(a.getRow() % 3, b.getRow() % 3, c.getRow() % 3);
	}

	static boolean diffColor(Card a, Card b, Card c) {
		return allDifferent(a.getCol() / 3, b.getCol() / 3, c.getCol() / 3); // everything in first 3 rows same color
																				// (so %3),

	}

	static boolean diffShape(Card a, Card b, Card c) {
		return allDifferent(a.getCol() % 3, b.getCol() % 3, c.getCol() % 3); // everything in first 3 rows same color
																				// (so %3),

	}

	static boolean diffFill(Card a, Card b, Card c) {
		return allDifferent(a.getRow() / 3, b.getRow() / 3, c.getRow() / 3); // everything in first 3 rows same color
																				// (so %3),

	}

	static boolean diffCount(Card a, Card b, Card c) {
		return allDifferent(a.getRow() % 3, b.getRow() % 3, c.getRow() % 3); // everything in first 3 rows same color
																				// (so %3),

	}

	static boolean isSet(Card a, Card b, Card c) {
		return (sameColor(a, b, c) || diffColor(a, b, c)) && (sameShape(a, b, c) || diffShape(a, b, c))
				&& (sameFill(a, b, c) || diffFill(a, b, c)) && (sameCount(a, b, c) || diffCount(a, b, c));
	}
}
