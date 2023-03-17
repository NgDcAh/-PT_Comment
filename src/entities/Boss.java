package entities;

import Main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import static utilz.Constants.Direction.LEFT;
import static utilz.Constants.Direction.RIGHT;
import static utilz.Constants.EnemyConstants.*;

public class Boss extends Enemy{
    private Rectangle2D.Float attackBox;
    private int attackBoxOffsetX;
    private Random rand = new Random();
    public Boss(float x, float y) {
        super(x, y, BOSS_WIDTH, BOSS_HEIGHT, BOSS);
        initHitBox(x, y, (int) (24 * Game.SCALE), (int) (30 * Game.SCALE));
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (20 * Game.SCALE), (int) (20 * Game.SCALE));
        attackBoxOffsetX = (int) (Game.SCALE * 30);
    }

    public void update(int[][] lvlData,Player player) {
        updateBehavior(lvlData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        if (walkDir == RIGHT) {
            attackBox.x = hitBox.x + hitBox.width;
        } else if (walkDir == LEFT) {
            attackBox.x = hitBox.x - hitBox.width;
        }
        attackBox.y = hitBox.y + (Game.SCALE * 4);
    }

    private void updateBehavior(int[][] lvlData, Player player) {
        if (firstUpdate)
            firstUpdateCheck(lvlData);

        if (inAir)
            updateInAir(lvlData);
        else {
            switch (enemyState) {
                case BOSS_RUNNING:
                    if (canSeePlayer(lvlData, player)) {
                        turnTowardPlayer(player);
                        if (isPlayerCloseForAttack(player)) {
                            int start = 0;
                            start += rand.nextInt(2);
                            if (start == 0) {
                                newState(BOSS_ATTACK_2);
                            } else if (start == 1) {
                                newState(BOSS_ATTACK_3);
                            }
                        }
                    }

                    move(lvlData);
                    break;
                case BOSS_ATTACK_2, BOSS_ATTACK_3:
                    if (aniIndex == 0)
                        attackChecked = false;
                    if (aniIndex == 3 && !attackChecked)
                        checkPlayerHit(attackBox, player);
                    break;
            }
        }

    }


    public void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.PINK);
        g.drawRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    public int flipX() {
        if (walkDir == LEFT) {
            return 160;
        } else {
            return 0;
        }
    }

    public int flipW() {
        if (walkDir == LEFT) {
            return -1;
        } else {
            return 1;
        }
    }
}
