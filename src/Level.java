import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * This Level class is responsible for managing all of the objects in your game
 * The GameEngine creates a new Level object for each level, and then calls 
 * that Level object's update() method repeatedly until it returns either 
 * "ADVANCE" (to proceed to the next level) or "QUIT" (to end the entire game).
 * <br/><br/>
 * This class should contain and make use of the following private fields:
 * <tt><ul>
 * <li>private Random rng</li>
 * <li>private Taxi taxi</li>
 * <li>private ArrayList<WarpStar> warpStars</li>
 * <li>private ArrayList<GasCloud> gasClouds</li>
 * <li>private ArrayList<Planet> planets</li>
 * <li>private int destinationPlanetIndex</li>
 * </ul></tt>
 */
public class Level {	

	// initialized variables and objects
	private Taxi taxi; 
	private int destinationPlanetIndex;
	private Random rng;
	ArrayList<WarpStar> warpStars = new ArrayList<WarpStar>();
	ArrayList<GasCloud> gasClouds = new ArrayList<GasCloud>();
	ArrayList<Planet> planets = new ArrayList<Planet>();

	/**
	 * This constructor initializes a new level object, so that the GameEngine
	 * can begin calling its update() method to advance the game's play.  In
	 * the process of this initialization, all of the objects in the current
	 * level should be instantiated and initialized to their starting states.
	 * @param rng is the ONLY Random number generator that should be used by 
	 * throughout this level and by any of the objects within it.
	 * @param levelFilename is either null (when a random level should be 
	 * loaded) or a reference to the custom level file that should be loaded.
	 */
	public Level(Random rng, String levelFilename) { 

		// takes rng parameter and sets it equal to field variable
		this.rng = rng;
		// if it doesn't detect a level file, will load a random level instead
		if (levelFilename != null) {
			loadCustomLevel(levelFilename);
		} else {
			loadRandomLevel();
		}

	}

	/**
	 * The GameEngine calls this method repeatedly to update all of the objects
	 * within your game, and to enforce all of your game's rules.
	 * @param time is the time in milliseconds that have elapsed since the last
	 * time this method was called (or your constructor was called). This can 
	 * be used to help control the speed of moving objects within your game.
	 * @return "CONTINUE", "ADVANCE", or "QUIT".  When this method returns
	 * "CONTINUE" the GameEngine will continue to play your game by repeatedly
	 * calling it's update() method.  Returning "ADVANCE" instructs the 
	 * GameEngine to end the current level, create a new level, and to start
	 * updating that new level object instead of the current one. Returning 
	 * "QUIT" instructs the GameEngine to end the entire game.  In the case of
	 * either "QUIT" or "ADVANCE" being returned, the GameEngine presents a
	 * short pause and transition message to help the player notice the change.
	 */
	public String update(int time) {
		taxi.update(time);	// calls taxi update method
		// returns "QUIT" if user pressed SPACEBAR after lost.
		if (taxi.update(time)) {
			return "QUIT";
		}
		// loops through to create Gas clouds, calling update and handleFueling
		for (int i=0; i <gasClouds.size(); i++) {
			gasClouds.get(i).update(time);
			gasClouds.get(i).handleFueling(taxi);
			// removes a Gas cloud when Taxi comes into contact with 
			// a Gas cloud
			if(gasClouds.get(i).shouldRemove()) {
				gasClouds.remove(i);
				i--;	// used to double check the removal of Gas clouds
			}
		}
		// loops through to create Warp stars, calling update and
		// handleNavigation
		for (int i=0; i <warpStars.size(); i++) {
			warpStars.get(i).update();
			warpStars.get(i).handleNavigation(taxi);
		}

		// loops through to create Planets, calling update and
		// handleLanding
		for (int i=0; i <planets.size(); i++) {
			planets.get(i).update(time);
			if (planets.get(i).handleLanding(taxi)) {
				// once taxi lands on destination, set destination to next
				// in the array and change appearance to normal
				planets.get(destinationPlanetIndex).setDestination(false);
				destinationPlanetIndex++;
				// returns "ADVANCE" when level completed, goes to next level
				if (destinationPlanetIndex == planets.size()) {
					return "ADVANCE";
				}
			}
		}
		// sets the appearance to destination depending on the PlanetIndex
		planets.get(destinationPlanetIndex).setDestination(true);

		return "CONTINUE";
	}	

	/**
	 * This method returns a string of text that will be displayed in the upper
	 * left hand corner of the game window.  Ultimately this text should convey
	 * the taxi's fuel level, and their progress through the destinations.
	 * However, this may also be useful for temporarily displaying messages
	 * that help you to debug your game.
	 * @return a string of text to be displayed in the upper-left hand corner
	 * of the screen by the GameEngine.
	 */
	public String getHUDMessage() {
		if (taxi.getFuel() <= 0 && !taxi.hasCrashed()) {
			return "You've run out of fuel!" + "\n" 
					+ "Press the SPACEBAR to end this game.";
		} else if (taxi.hasCrashed()){
			return "You've crashed into a planet!" + "\n" 
					+ "Press the SPACEBAR to end this game.";
		} else {
			return "Fuel: " +  Float.toString(taxi.getFuel()) + "\n" 
					+ "Fares: " + destinationPlanetIndex + "/" + planets.size();
		}
	}

	/**
	 * This method initializes the current level to contain a single taxi in 
	 * the center of the screen, along with 6 randomly positioned objects of 
	 * each of the following types: warp stars, gasClouds, and planets.
	 */
	private void loadRandomLevel() {
		// sets taxi position to the middle of the screen
		taxi = new Taxi(GameEngine.getWidth()/2,GameEngine.getHeight()/2);
		// randomizes position of Warp stars, Gas clouds, and planets
		for (int i= 0; i<6; i++) {
			warpStars.add(new WarpStar((rng.nextFloat()*GameEngine.getWidth())
					,(rng.nextFloat()*GameEngine.getHeight())));
			gasClouds.add(new GasCloud((rng.nextFloat()*GameEngine.getWidth())
					,(rng.nextFloat()*GameEngine.getHeight()), 0));
			planets.add(new Planet(rng, this.planets));
		}
		planets.get(destinationPlanetIndex).setDestination(true);
	}

	/**
	 * This method initializes the current level to contain each of the objects
	 * described in the lines of text from the specified file.  Each line in
	 * this file contains the type of an object followed by the position that
	 * it should be initialized to start the level.
	 * @param levelFilename is the name of the file (relative to the current
	 * working directory) that these object types and positions are loaded from
	 * @return true after the specified file's contents are successfully loaded
	 * and false whenever any problems are encountered related to this loading
	 */
	private boolean loadCustomLevel(String levelFilename) { 
		// File Format: ObjectType @ 12.3, 45.6
		File levelFile = new File(levelFilename);
		Scanner input = null;
		try {
			// takes in file input
			input = new Scanner(levelFile);
			// while there are lines to read, keep looping
			while(input.hasNext()) {
				String objectType = input.next();
				if (input.hasNext("@")) {
					input.next();
					// takes in the firstFloat and the comma
					String firstFloatRaw = input.next();
					// uses substring to isolate firstFloat from comma
					firstFloatRaw = firstFloatRaw.substring
							(0, firstFloatRaw.length()-1);
					// converts string to float
					Float firstFloat = Float.parseFloat(firstFloatRaw);
					Float secondFloat = input.nextFloat();
					// creates specific object depending on type specified
					if(objectType.equals("TAXI")) {
						this.taxi = new Taxi(firstFloat, secondFloat);
					}
					if(objectType.equals("GAS")) {
						GasCloud customCloud = new GasCloud
								(firstFloat, secondFloat, 0);
						this.gasClouds.add(customCloud);
					}
					if(objectType.equals("PLANET")) {
						Planet customPlanet = new Planet
								(firstFloat, secondFloat);
						this.planets.add(customPlanet);
					}
					if(objectType.equals("WARP_STAR")) {
						WarpStar customStar = new WarpStar
								(firstFloat, secondFloat);
						this.warpStars.add(customStar);
					}
				}
			}
			return true;
		} catch (NullPointerException a) {
			return false;	// if object type is not detected, load random
		} catch (FileNotFoundException b) {
			b.printStackTrace();
			return false;	// if no file is detected, load random level
		} finally {
			if (input != null) {	
				input.close();	// closes scanner
			}
		}
	}

	/**
	 * This method creates and runs a new GameEngine with its first Level. Any
	 * command line arguments passed into this program are treated as a list of
	 * custom level filenames that should be played in order by the player.
	 * @param args is the sequence of custom level filenames to play through
	 */
	public static void main(String[] args) {
		GameEngine.start(null,args);
	} 
}
