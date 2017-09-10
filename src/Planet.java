import java.util.ArrayList;
import java.util.Random;
/**
 * The Planet class has its own methods which are called in the level
 * class. These methods include the constructor which constructs the graphic
 * image of the Planet and implements its abilities in the game.
 * 
 * Bugs: (None that I'm aware of!) 
 * 
 * @author Susie C. and Krishna P. 
 */
public class Planet {

	// initializes variables
	private Graphic graphic;
	private boolean isDestination;

	/**
	 * Initialize this planet to be displayed at the specified position.
	 *
	 * @param x is the horizontal coordinate of this planet's initial position
	 * @param y is the vertical coordinate of this planet's initial position
	 */
	public Planet(float x, float y) {
		graphic = new Graphic("PLANET");
		graphic.setPosition(x, y);
	}

	/**
	 * Initialized this planet to be displayed at a randomly chose position, 
	 * and ensures that this randomly chosen position does not overlap with 
	 * the position of a previously created Planet.
	 *
	 * @param rng is used to generate random positions - using nextFloat()
	 * @param planets is the collection of planets that this new planet 
	 * cannot be overlapping (while it is, a new random position must 
	 * be generated)
	 */
	public Planet(Random rng, ArrayList<Planet> planets) {
		graphic = new Graphic("PLANET");

		// if true, given position coordinates will be randomized
		while(true){

			float x = rng.nextFloat() * GameEngine.getWidth();
			float y = rng.nextFloat() * GameEngine.getHeight();
			graphic.setPosition(x, y);

			boolean overlapped = false;	// initialize boolean variable to false
			// check all possible planet coordinates for a collision in game
			for (int i=0; i <planets.size(); i++) {
				if (graphic.isCollidingWith(planets.get(i).graphic)) {
					overlapped = true;
				}
			}
			// if the randomized planet is not overlapping previously generated
			// planet, new planet position will be set to those coordinates
			if(!overlapped){
				graphic.setPosition(x, y);
				break;
			}

		}
	}

	/**
	 * This method detects and handles collisions between taxis and planets 
	 * that result in either crashing: when traveling at warp speed, or in 
	 * reaching a destination and progressing through the current level.
	 *
	 * @param taxi is the taxi that might be colliding with this planet
	 * @return true when the taxi safely lands on this planet and this planet
	 * is marked as the current destination, otherwise it returns false
	 */
	public boolean handleLanding(Taxi taxi) {
		if (taxi.checkCollision(this.graphic)) {
			if(taxi.isTravellingAtWarp()) {
				taxi.crash();	// calls the method crash from the taxi class
				return false;
			}
			else if (this.isDestination) {
				return true;
			} else {
				taxi.crash();	// calls the method crash from the taxi class
				return false;
			}
		}
		return false;
	}

	/**
	 * This method set the current planet to either be the current 
	 * destination or not, and updates the appearance of it's 
	 * graphic accordingly
	 *
	 * @param isDestination - is true when this planet is being 
	 * marked as the current destination, and false when it is 
	 * being unmarked or returned to its status as a normal planet
	 * @return void
	 */
	public void setDestination(boolean isDestination) {
		this.isDestination = isDestination;
		if (isDestination) {
			graphic.setAppearance("DESTINATION");
		} else {
			graphic.setAppearance("PLANET");
		}
	}

	/**
	 * This method simply draws the current planet at it's current position.
	 *
	 * @param time is the number of milliseconds that have elapsed since 
	 * the last update
	 * @return void
	 */
	public void update(int time) {
		graphic.draw();
	}

}
