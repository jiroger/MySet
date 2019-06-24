import processing.core.PApplet;

public class Timer_Procedures {
	static PApplet parent; // this is needed so this class knows what to put the graphics on (PApplet)
	public static int TIMER_FILL;

	public static void togglePauseResume(PApplet p) {
		parent = p;
		if (SET_Final.state == SET_Final.State.PAUSED) {
			resumeGame(parent);
		} else {
			pauseGame(parent);
		}
	}

	public static void pauseGame(PApplet theParent) {
		SET_Final.state = SET_Final.State.PAUSED;
		SET_Final.timeElapsed += theParent.millis() - SET_Final.runningTimerStart;
		SET_Final.message = 9;
	}

	public static void resumeGame(PApplet theParent) {
		SET_Final.state = SET_Final.State.PLAYING;
		SET_Final.runningTimerStart = theParent.millis();
		SET_Final.message = 10;
	}

	static void showTimer(PApplet p) {
		parent = p;
		TIMER_FILL = parent.color(0, 0, 0);
		parent.textFont(SET_Final.timerFont);
		parent.fill(TIMER_FILL);
		// If the game is paused, show time elapsed
		// If the game is over, show time to complete
		// Otherwise, show time elapsed so far in current game
		if (SET_Final.state == SET_Final.State.PAUSED) {
			parent.text("Time: " + SET_Final.timeElapsed / 1000, SET_Final.TIMER_LEFT_OFFSET,
					SET_Final.TIMER_TOP_OFFSET);
		} else if (SET_Final.state == SET_Final.State.GAME_OVER) {
			parent.text(
					"Time: " + (SET_Final.runningTimerEnd - SET_Final.runningTimerStart + SET_Final.timeElapsed) / 1000,
					SET_Final.TIMER_LEFT_OFFSET, SET_Final.TIMER_TOP_OFFSET);
		} else {
			parent.text("Time: " + (parent.millis() - SET_Final.runningTimerStart + SET_Final.timeElapsed) / 1000,
					SET_Final.TIMER_LEFT_OFFSET, SET_Final.TIMER_TOP_OFFSET);
		}
	}

	public static int timerScore() {
		// Returns the number of points scored based on the timer, which is
		// the GREATER of:
		// 300 minus the number of seconds taken when the game ends
		// 0
		//
		// If it took 277 seconds to finish the game, this should return 23 (300-277=23)
		// If it took 435 seconds to finish the game, this should return 0 (435 > 300)
		int secs = Math.round(SET_Final.timeElapsed / 1000); // time elapsed is in milliseconds; divide by 1000 to get
																// seconds
		return Math.max(300 - secs, 0); // if less than 300 secs, u get extra. if not, u dont get anything
	}
}
