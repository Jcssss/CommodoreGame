/* Sniper.java
 * Creator: Justin Siu, Justin Li, Rafael Edora
 * Purpose: boss in the game, shoots fast shots at the player
 */
public class Sniper extends Boss {

	public Sniper(Game g, String r) {
		super(g, r, 5000, 3, 300.0);
	} // Sniper

	public void move(long delta) {
		this.seePlayer();
		super.move(delta);
	} // move
} // Sniper
