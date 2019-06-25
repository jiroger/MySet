import java.util.ArrayList;

import processing.core.PApplet;

public class Deck {
	PApplet parent;
	private ArrayList<Card> cards = new ArrayList<Card>();

	public Deck(PApplet p) {
		parent = p;
		initDeck();
	}

	public void initDeck() {
		for (int col = 0; col < SET_Final.SHEET_LENGTH; col++) {
			for (int row = 0; row < SET_Final.SHEET_LENGTH; row++) {
				cards.add(new Card(col, row, parent));
			}
		}
	}

	public Card getCard(int n) {
		return cards.get(n);
	}

	public Card deal() {
		if (cards.size() == 0)
			return null;

		return cards.remove((int) (Math.random() * cards.size()));
	}

	public int size() {
		return cards.size();
	}
}