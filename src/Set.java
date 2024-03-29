import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class Set extends PApplet {
	static PImage cimg;

	// for extracting pieces of image at
	// http://clojure.paris/resources/public/imgs/all-cards.png
	public static final int SHEET_LENGTH = 9; // # of cards in a row (or column) on the sheet
	public static final int NUM_CARDS = 81; // # of cards in the sheet
	public static final int CARD_WIDTH = 138; // width in pixels of a card
	public static final int CARD_HEIGHT = 97; // height in pixels of a card
	public static final int CARD_X_SPACER = 1; // space between cards in the x-direction on the sheet
	public static final int CARD_Y_SPACER = 1; // space between cards in the y-direction on the sheet
	// offsets into the sheet of cards
	final static int SHEET_LEFT_OFFSET = 7;
	final static int SHEET_TOP_OFFSET = 7;

	// for locating cards on the play grid
	public static final int GRID_LEFT_OFFSET = 16; // dist from left to start drawing grid
	public static final int GRID_TOP_OFFSET = 72; // dist from top to start drawing grid
	public static final int GRID_X_SPACER = 8; // separation between cards horizontally
	public static final int GRID_Y_SPACER = 8; // separation between cards vertically
	public static final int BEGIN_COLS = 4; // beginning # of columns in the grid
	public static final int ROWS = 3; // # of rows in the grid
	public static final int MAX_COLS = 7; // max # of columns in the grid
	public static int currentCols = BEGIN_COLS;

	// from top of window to bottom of grid:
	public static final int GRID_BOTTOM = GRID_TOP_OFFSET + ROWS * (CARD_HEIGHT + GRID_Y_SPACER);

	// from top of window to top of buttons
	final static int BUTTON_LEFT_OFFSET = GRID_LEFT_OFFSET;
	final static int BUTTON_TOP_OFFSET = GRID_BOTTOM + 16;
	final static int BUTTON_WIDTH = 200;
	final static int BUTTON_HEIGHT = 56;

	// 4 buttons: Add Cards, Find Set, New Game, Pause
	public static final int NUM_BUTTONS = 4;

	static Grid grid;
	static Deck deck;

	// score info
	public static PFont scoreFont;
	public final int SCORE_FILL = color(0, 0, 0); //
	public static int score;
	public static int SCORE_LEFT_OFFSET = GRID_LEFT_OFFSET;
	public static int SCORE_TOP_OFFSET = 25;

	// timer
	public static int gameTimer = 0;
	public static int setTimer = 0;
	public static int runningTimerStart;
	public static int timeElapsed = 0;

	// timer info
	public static PFont timerFont;
	public static int runningTimer;
	public static int runningTimerEnd;
	public static final int TIMER_LEFT_OFFSET = SCORE_LEFT_OFFSET + 256;
	public static final int TIMER_TOP_OFFSET = SCORE_TOP_OFFSET;

	// msg info
	public static PFont messageFont;
	public final int MESSAGE_FILL = color(0, 0, 0);
	public static int message;
	public static final int MESSAGE_LEFT_OFFSET = TIMER_LEFT_OFFSET + 256;
	public static final int MESSAGE_TOP_OFFSET = TIMER_TOP_OFFSET;

	// directions info
	public static PFont keyOptionsFont;
	public final int KEY_OPTIONS_FILL = color(0, 0, 0);
	public static final int KEY_OPTIONS_LEFT_OFFSET = GRID_LEFT_OFFSET;
	public static final int KEY_OPTIONS_TOP_OFFSET = BUTTON_TOP_OFFSET + BUTTON_HEIGHT + 48;
	public static final String keyOptions = "q, w, e, r, [t, y, u]: top row;\na, s, d, f, [g, h, j]: second row;\n"
			+ "z, x, c, v, [b, n, m]: third row\n"
			+ "+ to add cards, - to find a set, SPACE to pause, ENTER/RETURN for a new game";

	public final int BACKGROUND_COLOR = color(189, 195, 199);
	public static final int HIGHLIGHT_TICKS = 35;
	public static final int FIND_SET_TICKS = 60;
	public static int highlightCounter = 0;

	// state:
	// 0 --> normal play
	// 1 --> 3 cards selected (for freezing highlights)
	// 2 --> find sset selected
	// 3 --> gg
	// 4 --> paused
	public static enum State {
		PLAYING, EVAL_SET, FIND_SET, GAME_OVER, PAUSED
	};

	public static State state = State.PLAYING;

	public static void main(String[] args) {
		PApplet.main("Set");
	}

	@Override
	public void settings() {
		size(1056, 568);
	}

	@Override
	public void setup() {
		background(BACKGROUND_COLOR);

		fill(color(0, 0, 0));
		text("Loading...", 50, 150);

		newGame(); // starts a new game
		initFonts(); // fonts for text
		initSpriteSheet(); // gets the pics of cards ready
	}

	@Override
	public void draw() {
		background(BACKGROUND_COLOR);

		showScore();
		Timer_Procedures.showTimer(this); // show the timer
		showMessage(); // display the customized messages for each game mode
		drawButtons(); // make all the buttons
		drawDirections(); // e.g. keyboard shortcuts

		grid.display(); // draw the actual grid
		grid.highlightSelectedCards();

		if (grid.tripleSelected() && state == State.PLAYING) {
			state = State.EVAL_SET;
			highlightCounter = 0;
		}

		// 3 cards selected; process them
		if (state == State.EVAL_SET) {
			if (highlightCounter == HIGHLIGHT_TICKS) { // 35 ticks showing special highlight
				grid.processTriple();
			} else {
				highlightCounter = highlightCounter + 1;
			}
			// find set clicked
		} else if (state == State.FIND_SET) {
			if (highlightCounter == FIND_SET_TICKS) { // 35 ticks showing special highlight
				state = State.PLAYING;
				grid.clearSelected();
				score -= 5;
			} else {
				highlightCounter = highlightCounter + 1;
			}
		}
	}

	public void drawButtons() {
		// start, stop, clear rectangles in gray
		fill(color(221, 221, 221));
		for (int i = 0; i < NUM_BUTTONS; i++) {
			rect(BUTTON_LEFT_OFFSET + i * (BUTTON_WIDTH + 12), BUTTON_TOP_OFFSET, BUTTON_WIDTH, BUTTON_HEIGHT);
		}

		// set text color on the buttons to blue
		fill(color(0, 0, 255));

		text("Add Cards", BUTTON_LEFT_OFFSET + 18, BUTTON_TOP_OFFSET + 22);
		text(" Find Set", BUTTON_LEFT_OFFSET + 18 + BUTTON_WIDTH + 12, BUTTON_TOP_OFFSET + 22);
		text("New Game", BUTTON_LEFT_OFFSET + 18 + 2 * (BUTTON_WIDTH + 12), BUTTON_TOP_OFFSET + 22);
		if (state == State.PAUSED) {
			text("Resume", BUTTON_LEFT_OFFSET + 45 + 3 * (BUTTON_WIDTH + 12), BUTTON_TOP_OFFSET + 22);
		} else {
			text("Pause", BUTTON_LEFT_OFFSET + 54 + 3 * (BUTTON_WIDTH + 12), BUTTON_TOP_OFFSET + 22);
		}
	}

	public void newGame() {
		deck = new Deck(this); // PApplet; creates a new deck so u can see the cards (var deck is declared
								// above)
		grid = new Grid(this); // PApplet; creates the grid on which the cards will be put on (var grid is
								// declared above)

		score = 0;
		currentCols = 4;
		state = State.PLAYING; // i think this is already done above
		message = 0; // message is an int. basically the int 0 corresponds to a specific msg, int 1
						// is another msg, int 2 is another, and so forth
		for (int i = 0; i < currentCols * ROWS; i++) {
			grid.addCardToBoard(deck.deal()); // fil the deck with 12 cards
		}

		timeElapsed = 0; // already did above
		runningTimerStart = millis();
	}

	public void initFonts() {
		scoreFont = createFont("ComicSansMS-Bold", 32);
		messageFont = scoreFont;
		timerFont = scoreFont;
		keyOptionsFont = createFont("Times New Roman", 14);
		textAlign(LEFT, CENTER);
	}

	public void drawDirections() {
		fill(KEY_OPTIONS_FILL);
		textFont(keyOptionsFont);
		text(keyOptions, KEY_OPTIONS_LEFT_OFFSET, KEY_OPTIONS_TOP_OFFSET);
	}

	public void initSpriteSheet() {
		// source of images for card pics
		String url = "https://amiealbrecht.files.wordpress.com/2016/08/set-cards.jpg?w=1250";

		cimg = loadImage(url, "png");
	}

	public void showScore() {
		textFont(scoreFont);
		fill(SCORE_FILL);
		text("Score: " + score, SCORE_LEFT_OFFSET, SCORE_TOP_OFFSET);
	}

	public void showMessage() {
		textFont(messageFont);
		String str = "";
		switch (message) {
		case 0:
			str = "Welcome to SET!";
			break;
		case 1:
			str = "Set found!";
			break;
		case 2:
			str = "Sorry, not a set!";
			break;
		case 3:
			str = "Cards added to board...";
			break;
		case 4:
			str = "There is a set on the board!";
			break;
		case 5:
			str = "No cards left in the deck!";
			break;
		case 6:
			str = "No set on board to find!";
			break;
		case 7:
			str = "GAME OVER!";
			break;
		case 8:
			str = "\"" + key + "\"" + " not an active key!";
			break;
		case 9:
			str = "Game paused";
			break;
		case 10:
			str = "Game resumed";
			break;
		default:
			str = "Something is wrong. :-(";
		}
		text(str, MESSAGE_LEFT_OFFSET, MESSAGE_TOP_OFFSET);
	}

	// A BUNCH OF RANDOM HELPER FUNCTIONS FOR CLICKING AND KEY PRESSES AND STUFFS
	@Override
	public void keyPressed() {
		if (key == ENTER || key == RETURN) {
			newGame();
			return;
		}

		if (state == State.GAME_OVER)
			return;

		if (key == ' ') {
			Timer_Procedures.togglePauseResume(this);
			return;
		}

		// no fair playing with timer paused
		if (state != State.PLAYING)
			return;

		String legal = "QWERTYUASDFGHJZXCVVBNMqwertyuasdfghjzxcvbnm+=-_Pp ";
		if (legal.indexOf(key) < 0) {
			message = 8;
			return;
		}

		if ("+=-_ ".indexOf(key) >= 0) {
			switch (key) {
			case ' ':
				Timer_Procedures.togglePauseResume(this);
				break;
			case '=':
			case '+':
				grid.addColumn();
				break;
			case '_':
			case '-':
				state = State.FIND_SET;
				highlightCounter = 0;
				break;
			default:
				break;
			}
			return;
		}

		int col = "qazQAZ".indexOf(key) >= 0 ? 0
				: "wsxWSX".indexOf(key) >= 0 ? 1
						: "edcEDC".indexOf(key) >= 0 ? 2
								: "rfvRFV".indexOf(key) >= 0 ? 3
										: "tgbTGB".indexOf(key) >= 0 ? 4
												: "yhnYHN".indexOf(key) >= 0 ? 5 : "ujmUJM".indexOf(key) >= 0 ? 6 : 7;
		int row = "qwertyuQWERTYU".indexOf(key) >= 0 ? 0 : "asdfghjASDFGHJ".indexOf(key) >= 0 ? 1 : 2;

		if (col < currentCols) {
			grid.updateSelected(col, row);
		} else {
			message = 8;
		}
	}

	// I use mousePressed() and mouseReleased() instead of
	// mouseClicked() to confirm that a clicked button
	// should include a release as well.
	// the strat: gather info about
	// where a mouse was pressed and released; if two locations match, then yay all
	// good

	// when a non-button is clicked, use -1 to indicate none selected
	int mousex = -1;
	int mousey = -1;
	// clickedRow and clickedCol make sure that
	// where the mouse was pressed needs to be the same as the cell where the
	// mouse was released in order for a toggle to happen.
	int clickedRow = -1;
	int clickedCol = -1;
	int buttonSelected = -1;
	int buttonReleased = -1;

	@Override
	public void mousePressed() {
		if (Utility_Functions.between(mouseX, GRID_LEFT_OFFSET, grid.rightOffset())
				&& Utility_Functions.between(mouseY, GRID_TOP_OFFSET, GRID_BOTTOM)) {
			int xPos = (mouseX - GRID_LEFT_OFFSET) % (CARD_WIDTH + GRID_X_SPACER);
			int yPos = (mouseY - GRID_TOP_OFFSET) % (CARD_HEIGHT + GRID_Y_SPACER);

			if ((Utility_Functions.between(xPos, CARD_WIDTH + 1, CARD_WIDTH + GRID_X_SPACER)
					|| Utility_Functions.between(yPos, CARD_HEIGHT + 1, CARD_HEIGHT + GRID_Y_SPACER))) {
			} else {
				clickedCol = (mouseX - GRID_LEFT_OFFSET) / (CARD_WIDTH + GRID_X_SPACER);
				clickedRow = (mouseY - GRID_TOP_OFFSET) / (CARD_HEIGHT + GRID_Y_SPACER);
				// grid.updateSelected(clickedCol, clickedRow);
			}
		} else {
			buttonSelected = -1;
			for (int i = 0; i < NUM_BUTTONS; i++) {
				if (Utility_Functions.between(mouseX, BUTTON_LEFT_OFFSET + i * (BUTTON_WIDTH + 12),
						BUTTON_LEFT_OFFSET + i * (BUTTON_WIDTH + 12) + BUTTON_WIDTH)
						&& Utility_Functions.between(mouseY, BUTTON_TOP_OFFSET, BUTTON_TOP_OFFSET + BUTTON_HEIGHT)) {
					mousex = mouseX;
					mousey = mouseY;
					// 0: Add Cards, 1: Find Set, 2: New Game, 3: Pause Game
					buttonSelected = i;
				}
			}
		}
	}

	@Override
	public void mouseReleased() {
		// buttonReleased determines if a button was clicked
		// Must set to -1 here as if not, do not want state change
		buttonReleased = -1;

		// If the click was on the grid, toggle the appropriate cell
		if (Utility_Functions.between(mouseX, GRID_LEFT_OFFSET, grid.rightOffset())
				&& Utility_Functions.between(mouseY, GRID_TOP_OFFSET, GRID_BOTTOM) && !(state == State.GAME_OVER)
				&& !(state == State.PAUSED)) {
			int xPos = (mouseX - GRID_LEFT_OFFSET) % (CARD_WIDTH + GRID_X_SPACER);
			int yPos = (mouseY - GRID_TOP_OFFSET) % (CARD_HEIGHT + GRID_Y_SPACER);

			if ((Utility_Functions.between(xPos, CARD_WIDTH + 1, CARD_WIDTH + GRID_X_SPACER)
					|| Utility_Functions.between(yPos, CARD_HEIGHT + 1, CARD_HEIGHT + GRID_Y_SPACER))) {
			} else {
				int col = (mouseX - GRID_LEFT_OFFSET) / (CARD_WIDTH + GRID_X_SPACER);
				int row = (mouseY - GRID_TOP_OFFSET) / (CARD_HEIGHT + GRID_Y_SPACER);
				if (row == clickedRow && col == clickedCol) {
					grid.updateSelected(clickedCol, clickedRow);
				}
			}
		} else { // Figure out if a button was clicked and, if so, which one
			for (int i = 0; i < NUM_BUTTONS; i++) {
				if (Utility_Functions.between(mouseX, BUTTON_LEFT_OFFSET + i * (BUTTON_WIDTH + 12),
						BUTTON_LEFT_OFFSET + i * (BUTTON_WIDTH + 12) + BUTTON_WIDTH)
						&& Utility_Functions.between(mouseY, BUTTON_TOP_OFFSET, BUTTON_TOP_OFFSET + BUTTON_HEIGHT)) {
					buttonReleased = i;
				}
			}
			if (buttonSelected == buttonReleased && buttonReleased >= 0) {
				if (buttonReleased == 2) {
					newGame();
				} else if (state != State.GAME_OVER) {
					switch (buttonReleased) {
					case 0:
						grid.addColumn();
						break;
					case 1:
						state = State.FIND_SET;
						highlightCounter = 0;
						break;
					case 3:
						Timer_Procedures.togglePauseResume(this);
						break;
					default:
						break;
					}
				}
			}
		}
	}

}
