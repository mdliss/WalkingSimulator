///////////////////////// TOP OF FILE COMMENT BLOCK ////////////////////////////
//
// Title:           WalkingSim Program
// Course:          CS 300, Summer, 2023
//
// Author:          Max Liss-'s-Gravemade
// Email:           lisssgravema@wisc.edu
// Lecturer's Name: Michelle Jensen
//
///////////////////////////////// CITATIONS ////////////////////////////////////
//
// https://cs300-www.cs.wisc.edu/wp/wp-content/uploads/2020/12/fall2022/p2/doc/Walker.html
// https://cs300-www.cs.wisc.edu/wp/wp-content/uploads/2020/12/fall2022/p2/doc/Utility.html
//https://cs300-www.cs.wisc.edu/wp/wp-content/uploads/2020/12/fall2022/p2/doc/package-summary.html
//https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Random.html
//
/////////////////////////////// 80 COLUMNS WIDE ////////////////////////////////

import java.util.Arrays;
import java.util.Random;
import java.io.File;
import processing.core.PImage;

/**
 * This program simulates walking characters on the screen.
 * 
 * @author Max Liss-'s-Gravemade
 *
 */
public class WalkingSim {

	private static Random randGen;
	private static int bgColor;
	private static PImage[] frames;
	private static Walker[] walkers;

	/**
	 * The main method that starts the WalkingSim program.
	 * 
	 * @param args The command-line arguments (not used)
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Utility.runApplication();
	}

	/**
	 * Sets up the program by initializing frames, walkers, and background color.
	 * 
	 */
	public static void setup() {

		frames = new PImage[Walker.NUM_FRAMES];
		randGen = new Random();
		bgColor = randGen.nextInt();
		final int ARRAY_LENGTH = 8;

		for (int index = 0; index < Walker.NUM_FRAMES; index++) {
			String picturePath = "images" + File.separator + "walk-" + index + ".png";
			frames[index] = Utility.loadImage(picturePath);
		}

		int numWalkers = randGen.nextInt(ARRAY_LENGTH);
		walkers = new Walker[ARRAY_LENGTH];
		
		for (int i = 0; i < numWalkers; i++) {
			float x = randGen.nextInt(Utility.width());
			float y = randGen.nextInt(Utility.height());
			walkers[i] = new Walker(x, y);
		}

	}

	/**
	 * Draws the walkers on the screen and updates their positions.
	 * 
	 */
	public static void draw() {
		Utility.background(bgColor);

		for (Walker walker : walkers) {
			if (walker != null) {
				int currentFrameIndex = walker.getCurrentFrame();
				float positionX = walker.getPositionX();
				float positionY = walker.getPositionY();

				if (walker.isWalking()) {
					positionX += 3;
					positionX %= Utility.width();
					walker.setPositionX(positionX);
					walker.update();
				}
				Utility.image(frames[currentFrameIndex], positionX, positionY);

			}
		}
	}

	/**
	 * Checks if the mouse is over a walker.
	 * 
	 * @param walker The walker to check
	 * @return true if mouse is over a walker, false otherwise
	 */
	public static boolean isMouseOver(Walker walker) {

		float walkerX = walker.getPositionX();
		float walkerY = walker.getPositionY();
		PImage currentFrame = frames[walker.getCurrentFrame()];

		float left = walkerX - currentFrame.width / 2;
		float right = walkerX + currentFrame.width / 2;
		float top = walkerY - currentFrame.height / 2;
		float bottom = walkerY + currentFrame.height / 2;

		float mouseX = Utility.mouseX();
		float mouseY = Utility.mouseY();

		boolean isOverX = mouseX >= left && mouseX <= right;
		boolean isOverY = mouseY >= top && mouseY <= bottom;

		return isOverX && isOverY;
	}

	/**
	 * Makes a walker start walking when its clicked.
	 */
	public static void mousePressed() {
		for (int i = 0; i < walkers.length; i++) {
			Walker walker = walkers[i];
			if (walker != null && isMouseOver(walker)) {
				walker.setWalking(true);
				break;
			}
		}
	}

	/**
	 * Stops all of the walkers from walking
	 * 
	 * @param key The key that was pressed
	 */
	public static void keyPressed(char key) {
		if (key == 'a' || key == 'A') {
			addNewWalker();
		} else if (key == 's' || key == 'S') {
			stopAllWalkers();
		}

	}

	/**
	 * Adds a new walker to the program.
	 */
	private static void addNewWalker() {
		int numWalkers = walkers.length;
		final int MAX_WALKERS = 8;

		if (numWalkers < MAX_WALKERS) {
			float x = randGen.nextInt(Utility.width());
			float y = randGen.nextInt(Utility.height());
			Walker newWalker = new Walker(x, y);

			for (int i = 0; i < numWalkers; i++) {
				if (walkers[i] == null) {
					walkers[i] = newWalker;
					numWalkers++;
					return;
				}
			}

			Walker[] newWalkers = Arrays.copyOf(walkers, numWalkers + 1);
			newWalkers[numWalkers] = newWalker;
			walkers = newWalkers;
			numWalkers++;

		}
	}


	/**
	 * Stops all walkers by setting their walking status to false.
	 */
	private static void stopAllWalkers() {

		for (int i = 0; i < walkers.length; i++) {
			if (walkers[i] != null) {
				walkers[i].setWalking(false);
			}
		}
	}

}
