package entities;

import Gamestate.Playing;
import levels.Level;
import utilz.LoadSave;
import static utilz.Constants.EnemyConstants.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] assassin, spearman, bossImg;
    private ArrayList<AssassinEnemy> assassins = new ArrayList<>();
    private ArrayList<SpearEnemy> spearmans = new ArrayList<>();
    private Boss boss;

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImg();
    }

    public void loadEnemies(Level level) {
        assassins = level.getAssassinEnemies();
        spearmans = level.getSpearmanEnemies();
        boss = level.getBoss();
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for (AssassinEnemy a: assassins) {
            if (a.isActive()) {
                if (a.getCurrentHealth() > 0){
                    if (attackBox.intersects(a.getHitBox())) {
                        a.hurt(10);
                        return;
                    }
                }
            }
        }
        for (SpearEnemy s: spearmans) {
            if (s.isActive()) {
                if (s.getCurrentHealth() > 0){
                    if (attackBox.intersects(s.getHitBox())) {
                        s.hurt(10);
                        return;
                    }
                }
            }
        }
        if (checkActiveBoss()) {
            if (boss.isActive()) {
                if (boss.getCurrentHealth() > 0) {
                    if (attackBox.intersects(boss.getHitBox())) {
                        boss.hurt(10);
                        return;
                    }
                }
            }
        }
    }

    private void loadEnemyImg() {
        assassin = new BufferedImage[5][19];
        spearman = new BufferedImage[5][19];
        bossImg = new BufferedImage[5][10];

        BufferedImage a = LoadSave.GetSpriteAtlas(LoadSave.ENEMIES_ASSASSIN);
        BufferedImage s = LoadSave.GetSpriteAtlas(LoadSave.ENEMIES_SPEARMAN);
        BufferedImage b = LoadSave.GetSpriteAtlas(LoadSave.ENEMIES_BOSS);
        for (int j = 0; j < assassin.length; j++){
            for (int i = 0; i < assassin[j].length; i++) {
                assassin[j][i] = a.getSubimage(i * ENEMY_1_WIDTH_DEFAULT, j * ENEMY_1_HEIGHT_DEFAULT, ENEMY_1_WIDTH_DEFAULT, ENEMY_1_HEIGHT_DEFAULT);
            }
        }
        for (int j = 0; j < spearman.length; j++){
            for (int i = 0; i < spearman[j].length; i++) {
                spearman[j][i] = s.getSubimage(i * ENEMY_1_WIDTH_DEFAULT, j * ENEMY_1_HEIGHT_DEFAULT, ENEMY_1_WIDTH_DEFAULT, ENEMY_1_HEIGHT_DEFAULT);
            }
        }
        for (int j = 0; j < bossImg.length; j++){
            for (int i = 0; i < bossImg[j].length; i++) {
                bossImg[j][i] = b.getSubimage(i * BOSS_WIDTH_DEFAULT, j * BOSS_HEIGHT_DEFAULT, BOSS_WIDTH_DEFAULT, BOSS_HEIGHT_DEFAULT);
            }
        }

    }

    public void update(int[][] lvlData, Player player) {
        boolean isAnyActive = false;
        for (AssassinEnemy a : assassins) {
            if (a.isActive()) {
                a.update(lvlData, player);
                isAnyActive = true;
            }
        }
        for (SpearEnemy s : spearmans) {
            if (s.isActive()) {
                s.update(lvlData, player);
                isAnyActive = true;
            }
        }
        if (checkActiveBoss()) {
            if (boss.isActive()) {
                boss.update(lvlData, player);
                isAnyActive = true;
            }
        }
        if (!isAnyActive) {
            playing.setLevelCompleted(true);
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawEnemies(g, xLvlOffset);
    }

    public void drawEnemies(Graphics g, int xLvlOffset) {
        for (AssassinEnemy a : assassins) {
            if (a.isActive()) {
                g.drawImage(assassin[a.getEnemyState()][a.getAniIndex()],
                        (int) a.getHitBox().x - xLvlOffset - ENEMY_DRAWOFFSET_X + a.flipX(),
                        (int) a.getHitBox().y - ENEMY_DRAWOFFSET_Y,
                        160 * a.flipW(), 160, null);
                //a.drawHitBox(g, xLvlOffset);
                //a.drawAttackBox(g, xLvlOffset);
            }
        }
        for (SpearEnemy s : spearmans) {
            if (s.isActive()) {
                g.drawImage(spearman[s.getEnemyState()][s.getAniIndex()],
                        (int) s.getHitBox().x - xLvlOffset - ENEMY_DRAWOFFSET_X + s.flipX(),
                        (int) s.getHitBox().y - ENEMY_DRAWOFFSET_Y,
                        160 * s.flipW(), 160, null);
                //s.drawHitBox(g, xLvlOffset);
                //s.drawAttackBox(g, xLvlOffset);
            }
        }
        if (checkActiveBoss()) {
            if (boss.isActive()) {
                g.drawImage(bossImg[boss.getEnemyState()][boss.getAniIndex()],
                        (int) boss.getHitBox().x - xLvlOffset - ENEMY_DRAWOFFSET_X + boss.flipX(),
                        (int) boss.getHitBox().y - ENEMY_DRAWOFFSET_Y,
                        170 * boss.flipW(), 150, null);
                //boss.drawHitBox(g, xLvlOffset);
                //boss.drawAttackBox(g, xLvlOffset);
            }
        }
    }

    public boolean checkActiveBoss() {
        if (playing.getLevelManager().getCurrentLevel() == playing.getLevelManager().getLastLevel()){
            return true;
        }
        return false;
    }

    public void resetAllEnemies() {
        for (AssassinEnemy a : assassins)
            a.resetEnemy();
        for (SpearEnemy s : spearmans)
            s.resetEnemy();
        if (checkActiveBoss()) {
            boss.resetEnemy();
        }
    }
}
