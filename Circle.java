/* Circle.java
 * Creators: Justin Siu, Justin Li, Rafael Edora
 * Purpose: Used as hitboxes.
 * due to rotation, circles worked better than squares
 */

public class Circle {
	private double radius;
	private int x;
	private int y;

	// default constructor
	public Circle() {
		this.x = 0;
		this.y = 0;
		this.radius = 1;
	} // Circle

	public Circle(int x, int y, double radius) {
		setBounds(x, y, radius);
	} // Circle

	public int getX() {
		return this.x;
	} // getX

	public int getY() {
		return this.y;
	} // getY

	public double getRadius() {
		return this.radius;
	} // getRadius

	// calculates the distance between the center of two circles
	private static double distanceBetween(Circle c1, Circle c2) {
		return Math.sqrt(Math.pow(c1.x - c2.x, 2) + Math.pow(c1.y - c2.y, 2));
	} // distanceBetween

	// sets the bounds of the circle
	public void setBounds(int x, int y, double radius) {
		this.radius = radius;
		this.x = x;
		this.y = y;
	} // setBounds

	// test whether two circles intersect each other (collide)
	public boolean intersects(Circle other) {
		return Circle.distanceBetween(this, other) <= this.radius + other.radius;
	} // intersects
} // Circle
