package objects;

import Main.Game;

import static utilz.Constants.Objects.BOX;

public class GameContainer extends GameObjects{

    public GameContainer(int x, int y, int objType) {
        super(x, y, objType);
        createHitbox();
    }

    private void createHitbox() {
        if (objType == BOX) {
            initHitbox(25, 18);

            xDrawOffset = (int) (7 * Game.SCALE);
            yDrawOffset = (int) (12 * Game.SCALE);

        }
        hitBox.y += yDrawOffset + (int) (Game.SCALE * 2);
        hitBox.x += xDrawOffset / 2;
    }

    public void update() {
        if (doAnimation)
            updateAnimationTick();
    }
}
