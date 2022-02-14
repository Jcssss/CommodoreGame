/* Corpse.java
 * Creators: Justin Siu, Justin Li, Rafael Edora
 * Purpose: an entity spawned in the position of dead enemies
 */

import java.awt.AlphaComposite;
import java.awt.Graphics2D;

public class Corpse extends Entity {

	private long spawnTime = 0; // the time that the corpse was spawned at
	private final long FADE_TIME = 5000; // the time it takes for the corpse to fade out
	private final long FADE_DELAY = 1000; // the time the corpse spends completely opaque
	private float opacity = 0; // the opacity of the corpse

	public Corpse(Game g, String r, int newX, int newY) {
		super(g, r, newX, newY, 0.0, 1, 4.0);
		this.spawnTime = System.currentTimeMillis() + FADE_DELAY;
	} // Corpse

	// draws the entity
	public void draw(Graphics2D g) {

		// sets the opacity of the corpse
		opacity = (1 - ((System.currentTimeMillis() - spawnTime) / (float) FADE_TIME));

		// ensures that no errors occur

		// if the opacity is greater than one, sets it to one
		if (opacity > 1) {
			opacity = 1;

			// if the opacity is less than 0, sets it to 0
		} else if (opacity < 0) {
			opacity = 0;
		} // if

		// draws the image
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		super.draw(g);
	} // draw

	public float getOpacity() {
		return this.opacity;
	} // getOpacity

	public void collidedWith(Entity other) {

	} // collidedWith
} // Corpse