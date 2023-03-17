package objects;

import Main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.Objects.*;

public class Bullet {

    private Rectangle2D.Float hitbox;
    protected int aniTick, aniIndex;
    protected int ANI_SPEED = 25;
    private int dir;
    private boolean active = true;

    public Bullet(int x, int y, int dir) {
        int xOffset = (int) (-3 * Game.SCALE);
        int yOffset = (int) (8 * Game.SCALE);

        if (dir == 1)
            xOffset = (int) (29 * Game.SCALE);

        hitbox = new Rectangle2D.Float(x + xOffset, y + yOffset, BULLET_WIDTH - 10, BULLET_HEIGHT - 10);
        this.dir = dir;
    }

    public void drawHitbox(Graphics g, int xLvlOffset) {
        g.setColor(Color.PINK);
        g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    public void update() {
        updateAnimationTick();
    }

    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= 6) {
                aniIndex = 0;
            }
        }
    }

    // đạn bay, lấy vị trí đạn nhân SPEED
    public void updatePos() {
        hitbox.x += dir * SPEED;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public int getAniIndex() {
        return aniIndex;
    }
}
