package temalab;

import com.badlogic.gdx.math.Vector2;

public final class Position {
	private int x;
	private int y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int x() {
		return x;
	}
	public int y() {
		return y;
	}
	public String toString() {
		return "x: " + x + " y: " + y;
	}
	public Vector2 screenCoords() {
		float size = Map.instance().squareSize();
		float udc = Map.instance().universalDistanceConstant();
		return new Vector2((udc * size + udc * x * size), (udc * size + udc * y * size));
	}

	public boolean inDistance(Position p2, float dist) {
		return Math.pow(this.x - p2.x(), 2) + Math.pow(this.y - p2.y(), 2) <= dist * dist;
	}
}