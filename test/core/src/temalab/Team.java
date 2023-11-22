package temalab;

import java.util.ArrayList;
import java.util.HashMap;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public final class Team {
	private Color color;
	private String name;
	private HashMap<Integer, Unit> units;
	
	public Team(String name) {
		units = new HashMap<Integer, Unit>();
		if(name == "white") {
			this.color = new Color(1, 1, 1, 1);
		} else if(name == "red") {
			this.color = new Color(1, 0, 0, 1);
		} else {
			this.color = new Color(0f, 0f, 0f, 1);
		}
		this.name = name;
	}
	public void render(ShapeRenderer sr, SpriteBatch sb, BitmapFont bf) {
		units.forEach((id, u) -> {
			u.render(sr, sb, bf, color);
		});
	}
	public Color getColor() {
		return color;
	}
	public String getName() {
		return name;
	}
	public void addUnit(Unit v) {
		this.units.put(v.getUUID(), v);
	}
	public HashMap<Integer, Unit> units() {
		return this.units;
	}
	public ArrayList<Integer> unitIDs() {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		units.forEach((id, u) -> {
			ids.add(u.getUUID());
		});
		return ids;
	}

	public ArrayList<Unit> requestUnits(Position pos, float size) {
		var view = new ArrayList<Unit>();
		units.forEach((id, u) -> {
			if(pos.inDistance(u.pos(), size) && u.pos() != pos) {
				view.add(u);
			}
		});
		return view;
	}

	public ArrayList<UnitView> requestUnitViews(Position pos, float size) {
		var view = new ArrayList<UnitView>();
		units.forEach((id, u) -> {
			if(pos.inDistance(u.pos(), size) && u.pos() != pos) {
				view.add(u.getView());
			}
		});
		return view;
	}
	
	public void doAction(String[] answer) {
		units.forEach((id, u) -> {
			u.updateWorld();
		});
	}

	public void makeShot(int damage, int x, int y) {
		units.forEach((id, u) -> {
			if(u.pos.equals(new Position(x, y))) {
				u.takeShot(damage);
			}
		});
	}

	public void unitDied(int id) {
		units.remove(id);
	}
	
	public ArrayList<String> teamMembersToString() {
		var res = new ArrayList<String>();
		units.forEach((id, u) -> {
			res.add(u.toString());
		});
		return res;
	}
}
