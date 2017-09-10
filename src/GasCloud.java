/**
 * The GasCloud class has its own methods which are called in the level
 * class. These methods include the constructor which constructs the graphic
 * image of the GasCloud and implements its abilities in the game.
 * 
 * Bugs: (None that I'm aware of!) 
 * 
 * @author Susie C. and Krishna P. 
 */
public class GasCloud {

	// initialized variables
	private Graphic graphic;
	private float rotationSpeed;
	private boolean shouldRemove;

	/**
	 * Initializes a new GasCloud object to be displayed at the specified 
	 * initial position and orientation.
	 *
	 * @param x is the initial horizontal position for this object's graphic
	 * @param y is the initial vertical position for this object's graphic
	 * @param direction is the initial orientation for this object's graphic
	 * @return (description of the return value)
	 */
	public GasCloud(float x, float y, float direction) {
		graphic = new Graphic("GAS");
		graphic.setPosition(x, y);
		graphic.setDirection(direction);

	}
	
	/**
	 * This method rotates the gas cloud before drawing in its new orientation.
	 *
	 * @param time is the time in milliseconds that have elapsed since the
	 * last time this method was called, used to control speed of objects
	 * in the game.
	 * @return void
	 */
	public void update(int time) {
		rotationSpeed = -0.001f * time;
		graphic.setDirection(graphic.getDirection() - (rotationSpeed));
		graphic.draw();
	}

	/**
	 * This accessor method retrieves whether this object should be 
	 * remove from the level yet or not.
	 *
	 * @return true after the player has collected fuel from this object,
	 * and returns false otherwise
	 */
	public boolean shouldRemove() {
		return shouldRemove;
	}

	/**
	 * This method detects whether the player's taxi is currently colliding 
	 * with this gas cloud object or not. If it is, that taxi will get more 
	 * fuel and this gas cloud will be marked for removal from the level.
	 *
	 * @param taxi is the taxi that will get addition fuel if it is colliding 
	 * with this GasCloud object
	 * @return void
	 */
	public void handleFueling(Taxi taxi) {
		if (taxi.checkCollision(this.graphic)) {
			taxi.addFuel(20);
			shouldRemove = true;
		}
	}
}
