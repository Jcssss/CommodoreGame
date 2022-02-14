/* Car.java
 * Creators: Justin Siu, Justin Li, Rafael Edora
 * Purpose: One of the three bosses
 * charges at you from off the screen
 */

public class Car extends Boss {

	public Car(Game g, String r) {
		super(g, r, 5000, 1, 750);

		// initializes variables
		this.moveDirection = 270;
		this.centerX = -this.getWidth();
		this.centerY = this.game.WINDOW_HEIGHT / 2;
		this.center();
	} // Car

	// need own move patterns because moves off the screen
	public void move(long delta) {

		// if the car is off the screen and has not recently attacked, randomly spawns
		// somewhere outside the screen
		if ((this.x <= -this.getWidth() || x >= game.WINDOW_WIDTH || y <= -this.getHeight() || y >= game.WINDOW_HEIGHT)
				&& System.currentTimeMillis() - this.lastTimeShot > this.shotInterval) {
			int newX = 0;
			int newY = 0;

			// randomly selects a side to spawn on
			switch ((int) (Math.random() * 4)) {
			case 0: // N
				newY = -this.getRadius();
				newX = (int) (Math.random() * (this.game.WINDOW_WIDTH - this.getRadius() - 20));
				break;
			case 1: // E
				newX = this.game.WINDOW_WIDTH;
				newY = (int) (Math.random() * (this.game.WINDOW_HEIGHT - this.getRadius() - 20));
				break;
			case 2: // S
				newY = this.game.WINDOW_HEIGHT;
				newX = (int) (Math.random() * (this.game.WINDOW_WIDTH - this.getRadius() - 20));
				break;
			case 3: // W
				newX = -this.getRadius();
				newY = (int) (Math.random() * (this.game.WINDOW_HEIGHT - this.getRadius() - 20));
				break;
			} // switch

			this.centerX = newX;
			this.centerY = newY;

			this.seePlayer();
			this.updateLastTimeShot();
		} // if

		// moves the entity normally
		this.moveDirection = this.direction;
		this.setMovement();
		this.centerX += (delta * this.dx) / 1000;
		this.centerY += (delta * this.dy) / 1000;
		this.center();
	} // move
} // Car
