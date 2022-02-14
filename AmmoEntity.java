/* AmmoEntity.java
 * Creators: Justin Siu, Justin Li, Rafael Edora
 * Purpose: only needed for instanceof purposes
 */

public class AmmoEntity extends Pickups {

	public final int MAG_SIZE = 3;

	public AmmoEntity(Game g, String r, int newX, int newY) {
		super(g, r, newX, newY); // calls the constructor in Entity
	} // AmmoEntity

} // AmmoEntity class