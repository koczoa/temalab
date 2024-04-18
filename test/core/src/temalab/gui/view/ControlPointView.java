package temalab.gui.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import temalab.common.ControlPointListener;
import temalab.model.ControlPoint;

public class ControlPointView implements ControlPointListener{
    private Vector2 center;
    private int size;
    private Color c;
    private MapView mv;

    public ControlPointView(ControlPoint cp, MapView mv) {
        this.mv = mv;
        this.center = cp.pos().screenCoords();
        this.size = cp.size();
        this.c = new Color(Color.CYAN);
        cp.registerListener(this);
    }

    public void render(ShapeRenderer sr, SpriteBatch sb) {
        float sqareSize = mv.squareSize() * (size + 1.5f);
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(c);
        sr.circle(center.x, center.y, sqareSize);
        sr.end();
    }

    @Override
    public void onColorChange(Color c) {
        this.c = c;
    }
}