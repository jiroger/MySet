import processing.core.PApplet;

public class Timer_Procedures {
	static PApplet parent; // this is needed so this class knows what to put the graphics on (PApplet)
	public static int TIMER_FILL;

	public static void togglePauseResume(PApplet p) {
		parent = p;
		if (Set.state == Set.State.PAUSED) {
			resumeGame(parent);
		} else {
			pauseGame(parent);
		}
	}

	public static void pauseGame(PApplet theParent) {
		Set.state = Set.State.PAUSED;
		Set.timeElapsed += theParent.millis() - Set.runningTimerStart;
		Set.message = 9;
	}

	public static void resumeGame(PApplet theParent) {
		Set.state = Set.State.PLAYING;
		Set.runningTimerStart = theParent.millis();
		Set.message = 10;
	}

	static void showTimer(PApplet p) {
		parent = p;
		TIMER_FILL = parent.color(0, 0, 0);
		parent.textFont(Set.timerFont);
		parent.fill(TIMER_FILL);
		// If the game is paused, show time elapsed
		// If the game is over, show time to complete
		// Otherwise, show time elapsed so far in current game
		if (Set.state == Set.State.PAUSED) {
			parent.text("Time: " + Set.timeElapsed / 1000, Set.TIMER_LEFT_OFFSET,
					Set.TIMER_TOP_OFFSET);
		} else if (Set.state == Set.State.GAME_OVER) {
			parent.text(
					"Time: " + (Set.runningTimerEnd - Set.runningTimerStart + Set.timeElapsed) / 1000,
					Set.TIMER_LEFT_OFFSET, Set.TIMER_TOP_OFFSET);
		} else {
			parent.text("Time: " + (parent.millis() - Set.runningTimerStart + Set.timeElapsed) / 1000,
					Set.TIMER_LEFT_OFFSET, Set.TIMER_TOP_OFFSET);
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
		int secs = Math.round(Set.timeElapsed / 1000); // time elapsed is in milliseconds; divide by 1000 to get
																// seconds
		return Math.max(300 - secs, 0); // if less than 300 secs, u get extra. if not, u dont get anything
	}
}
