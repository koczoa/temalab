package temalab;

import com.badlogic.gdx.math.Vector2;

public final class Position {
	private int x;
	private int y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Position(Position p) {
		this.x = p.x;
		this.y = p.y;
	}
	
	public int x() {
		return x;
	}
	public int y() {
		return y;
	}
	public String toString() {
		return  x + " " + y;
	}
	public Vector2 screenCoords() {
		float size = Map.instance().squareSize();
		float udc = Map.instance().universalDistanceConstant();
		return new Vector2((udc * size + udc * x * size), (udc * size + udc * y * size));
	}

	public boolean inDistance(Position p2, float dist) {
		return Math.pow(this.x - p2.x(), 2) + Math.pow(this.y - p2.y(), 2) <= dist * dist;
	}

	public boolean isNeighbouring(Position p) {
		return Math.abs(this.x - p.x) <= 1 || Math.abs(this.y - p.y) <= 1;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash = Integer.hashCode(hash + x);
		hash = Integer.hashCode(hash + y);
		return hash;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Position) {
			Position pos = (Position)obj;
			return pos.x == this.x && pos.y == this.y;
		}
		return false;
	}
}