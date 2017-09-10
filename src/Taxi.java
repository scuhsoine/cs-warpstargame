/**
 * The Taxi class has its own methods which are called in various classes
 * in the program. These methods include the constructor which constructs
 * the graphic image of the Taxi and interacts with the other classes
 * abilities.
 * 
 * Bugs: (None that I'm aware of!)
 * 
 * @author Susie C. and Krishna P. 
 */
public class Taxi {

	// initialize variables
	public Graphic graphic;
	private float thrusterSpeed;
	private float fuel;
	private float warpSpeed; 			// initialized to 0.2f
	private boolean isTravellingAtWarp; // initialized to false
	private boolean hasCrashed = false;

	/**
	 * Initialized all fields of the new taxi object so that it will appear 
	 * at the specified position.
	 *
	 * @param x specifies the initial horizontal position for this new object
	 * @param y specifies the initial vertical position for this new object
	 */
	public Taxi(float x, float y) {
		warpSpeed = 0.2f;
		thrusterSpeed = 0.01f;
		graphic = new Graphic("TAXI");
		graphic.setPosition(x,y);
		fuel = 30;
	}

	/**
	 * This method is primarily responsible for updating the taxi's position 
	 * so that the move appropriately: using thrusters or warp star travel. 
	 * When a taxi moves off an edge of the screen, they should simultaneously 
	 * enter from the opposite screen edge. The a taxi is either out of fuel 
	 * or has crashed into a planet, they should not move at all.
	 *
	 * @param time in milliseconds is used to move taxi at the correct speed
	 * @return true when the player has (either crashed or run out of fuel) 
	 * and also pressed the space bar to acknowledge that they are done 
	 * playing, in all other cases this method should return false	 
	 */
	public boolean update(int time) {
		graphic.draw();
		if(fuel > 0) {
			// if taxi the taxi is traveling at warp, update the position of taxi
			if (isTravellingAtWarp) {
				graphic.setPosition(graphic.getX() + warpSpeed*time * graphic.getDirectionX(), 
						graphic.getY() + warpSpeed*time * graphic.getDirectionY());
			}
			// controls the movement of the taxi depending on which keys are pressed
			if (GameEngine.isKeyHeld("D") || GameEngine.isKeyHeld("RIGHT")) {
				// sets a new x position of taxi
				graphic.setX(graphic.getX() + thrusterSpeed * time);
				// changes the direction the taxi is facing
				graphic.setDirection(0);
				// fuel is lowered from the movement of the taxi
				fuel = fuel - (thrusterSpeed *time);
				// stops the traveling at warp speed when key is pressed
				isTravellingAtWarp = false;
			}
			if (GameEngine.isKeyHeld("A") || GameEngine.isKeyHeld("LEFT")) {
				graphic.setX(graphic.getX() - thrusterSpeed * time);
				graphic.setDirection((float) (Math.PI));
				fuel = fuel - (thrusterSpeed *time);
				isTravellingAtWarp = false;
			}
			if (GameEngine.isKeyHeld("W") || GameEngine.isKeyHeld("UP")) {
				graphic.setY(graphic.getY() - thrusterSpeed * time);
				graphic.setDirection((float) (Math.PI/2));
				fuel = fuel - (thrusterSpeed *time);
				isTravellingAtWarp = false;
			}
			if (GameEngine.isKeyHeld("S") || GameEngine.isKeyHeld("DOWN")) {
				graphic.setY(graphic.getY() + thrusterSpeed * time);
				graphic.setDirection((float) ((3*Math.PI)/2));
				fuel = fuel - (thrusterSpeed *time);
				isTravellingAtWarp = false;
			}
		}

		// wraps the position of the taxi if the taxi goes off the screen
		if (graphic.getX() > GameEngine.getWidth()) {
			graphic.setX(graphic.getX()-GameEngine.getWidth());
		}
		if (graphic.getX() < 0) {
			graphic.setX(GameEngine.getWidth()-graphic.getX());
		}
		if (graphic.getY() > GameEngine.getHeight()) {
			graphic.setY(graphic.getY()-GameEngine.getHeight());
		}
		if (graphic.getY() < 0) {
			graphic.setY(GameEngine.getHeight()-graphic.getY());
		}

		// if the user has lost by fuel depletion or crashing, waits for the user
		// to press space to display the "YOU LOST" message.
		if (fuel <= 0 || hasCrashed) {
			if (GameEngine.isKeyPressed("SPACE")) {
				return true;
			}
		}
		return false;
	}


	/**
	 * This accessor method retrieves a taxi object's fuel level.
	 *
	 * @return the amount of fuel that this taxi currently holds.
	 */
	public float getFuel() {
		return this.fuel;
	}

	/**
	 * This method increments a taxi's fuel level by the specified amount.
	 *
	 * @param fuel is the amount that should be added to this taxi's fuel
	 * @return void
	 */
	public void addFuel(float fuel) {
		this.fuel = this.fuel + fuel;
	}

	/**
	 * Mutates the state of this taxi to begin traveling at warp speed in 
	 * the direction of the specified position.
	 *
	 * @param x is the horizontal coordinate to move at warp speed toward
	 * @param y is the vertical coordinate to move at warp speed toward
	 * @return void
	 */
	public void setWarp(float x, float y) {
		graphic.setDirection(x, y);
		isTravellingAtWarp = true;
	}

	/**
	 * This accessor retrieves whether this taxi is traveling at warp speed.
	 *
	 * @return true when traveling at warp speed, and false otherwise.
	 */
	public boolean isTravellingAtWarp() { 
		return isTravellingAtWarp;
	}

	/**
	 * This method changes the appearance of this taxi's graphic to EXPLOSION
	 *  and also changes this object's state to be crashed which effects the 
	 *  ship's movement among other things.
	 *
	 * @return void
	 */
	public void crash() {
		hasCrashed = true;
		graphic.setAppearance("EXPLOSION");	// changes graphic from 
		// planet to explosion
		this.fuel = 0;	// changes the fuel to 0 for no movement
	}

	/**
	 * This accessor retrieves whether this taxi has crashed into a planet.
	 *
	 * @return true when this taxi has crashed into a planet, otherwise false
	 */
	public boolean hasCrashed() {
		return hasCrashed;
	}

	/**
	 * Determines whether this taxi object's graphic is overlapping with 
	 * the graphic of another object in the game.
	 *
	 * @param other is the graphic to check for a collision against
	 * @return true when other overlaps with this taxi's graphic, else false
	 */
	public boolean checkCollision(Graphic other) {
		if (graphic.isCollidingWith(other)) {
			return true;
		} else {
			return false;
		}
	}

}
