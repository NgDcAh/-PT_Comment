package objects;

import Main.Game;

public class Archer extends GameObjects{

    private int tileY;

    public Archer(int x, int y, int objType) {
        super(x, y, objType);
        tileY = y / Game.TILES_SIZE;
        initHitbox(32, 32);
    }

    public void update() {
        updateAnimationTick();
    }

    public int getTileY() {
        return tileY;
    }

}
