package objects;

import Gamestate.Playing;
import Main.Game;
import entities.Player;
import levels.Level;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import static utilz.CheckMethods.*;
import static utilz.Constants.Objects.*;

public class ObjectManager {

    private Playing playing;
    private BufferedImage spikeImg;
    private BufferedImage[][] potionImgs, containerImgs, archerImgs;
    private BufferedImage[] bulletImgs;
    private ArrayList<Spike> spikes;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Archer> archers;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private Random rand = new Random();

    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImgs();
    }

    // nhân vật chạm vào thuốc
    public void checkObjectTouched(Rectangle2D.Float hitbox) {
        for (Potion p : potions)
            if (p.isActive()) {
                if (hitbox.intersects(p.getHitbox())) { // tra gg intersects
                    p.setActive(false);
                    applyEffectToPlayer(p);
                }
            }
    }

    // áp dụng thuốc
    public void applyEffectToPlayer(Potion p) {
        if (p.getObjType() == RED_POTION)
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        else
            playing.getPlayer().changePower(BLUE_POTION_VALUE);
    }

    // đánh object
    public void checkObjectHit(Rectangle2D.Float attackbox) {
        for (GameContainer gc : containers)
            if (gc.isActive() && !gc.doAnimation) { // nếu cái thùng hiển thị và ko animation
                if (gc.getHitbox().intersects(attackbox)) {
                    gc.setAnimation(true); // chạy animation vỡ
                    int type = 0;
                    type += rand.nextInt(2); //random cái type, 1 là ra máu, 2 là ra mana
                    potions.add(new Potion((int) (gc.getHitbox().x + gc.getHitbox().width / 2), (int) (gc.getHitbox().y - gc.getHitbox().height / 2), type));
                    return;
                }
            }
        for (Archer a: archers)
            if (a.isActive()) {
                if (a.getHitbox().intersects(attackbox)) {
                    if (a.getObjType() == ARCHER_ATTACK_LEFT)
                        a.setObjType(ARCHER_DEATH_LEFT);
                    else if (a.getObjType() == ARCHER_ATTACK_RIGHT)
                        a.setObjType(ARCHER_DEATH_RIGHT);
                    if (a.getAniIndex() == 9)
                        a.setActive(false);
                }
            }
    }


    // chạm bẫy
    public void checkSpikeTouched(Player p) {
        for (Spike s : spikes)
            if (s.getHitbox().intersects(p.getHitBox()))
                p.kill();
    }

    public void loadObjects(Level newLevel) {
        spikes = newLevel.getSpikes();
        potions = newLevel.getPotions();
        containers = newLevel.getContainers();
        archers = newLevel.getArchers();
        bullets.clear();
    }

    // cắt ảnh và thêm vào mảng
    /*
        Ví dụ với thuốc
            cái ảnh thuốc potions_sprites.png trong folder GUI coi là 1 mảng [2][7] mỗi phần tử 12x16
            chạy qua vòng lặp cắt ảnh băng getSubImage(phải hiểu tham số trong này)
            thêm vào mảng potionImgs[][]
    */
    private void loadImgs() {
        spikeImg = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS); // bẫy có 1 ảnh

        // thuốc
        BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
        potionImgs = new BufferedImage[2][7];
        for (int j = 0; j < potionImgs.length; j++)
            for (int i = 0; i < potionImgs[j].length; i++)
                potionImgs[j][i] = potionSprite.getSubimage(12 * i, 16 * j, 12, 16);

        // thùng
        BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
        containerImgs = new BufferedImage[2][8];
        for (int j = 0; j < containerImgs.length; j++)
            for (int i = 0; i < containerImgs[j].length; i++)
                containerImgs[j][i] = containerSprite.getSubimage(40 * i, 30 * j, 40, 30);

        // xạ thủ
        BufferedImage archerSprite = LoadSave.GetSpriteAtlas(LoadSave.ARCHER_ATLAS);
        archerImgs = new BufferedImage[3][9];
        for (int j = 0; j < archerImgs.length; j++)
            for (int i = 0; i < archerImgs[j].length; i++)
                archerImgs[j][i] = archerSprite.getSubimage(64 * i, 64 * j, 64, 64);

        // đạn
        BufferedImage bulletSprite = LoadSave.GetSpriteAtlas(LoadSave.BULLET);
        bulletImgs = new BufferedImage[6];
        for (int i = 0; i < bulletImgs.length; i++) {
            bulletImgs[i] = bulletSprite.getSubimage(34 * i, 0, 34, 33);
        }
    }

    public void update(int[][] lvlData, Player player) {
        for (Potion p : potions)
            if (p.isActive())
                p.update();

        for (GameContainer gc : containers)
            if (gc.isActive())
                gc.update();

        updateArcher(lvlData, player);
        updateBullet(lvlData, player);
    }

    private void updateBullet(int[][] lvlData, Player player) {
        for (Bullet b : bullets) {
            if (b.isActive()) { // nếu đạn đang hiển thị
                b.updatePos(); // update đạn bay
                if (b.getHitbox().intersects(player.getHitBox())) {
                    player.changeHealth(-20);
                    b.setActive(false); // sau khi chạm đạn biến mất
                }
                else if (IsBulletHittingLevel(b, lvlData))
                    b.setActive(false); // sau khi chạm đạn biến mất
            }
            b.update();
        }
    }

    private void updateArcher(int[][] lvlData, Player player) {
        for (Archer a : archers) {
            a.update();
            if (a.getObjType() == ARCHER_ATTACK_LEFT || a.getObjType() == ARCHER_ATTACK_RIGHT) {
                if (a.getAniIndex() == 3 && a.getAniTick() == 0) {
                    shootArcher(a);
                }
            }
        }
    }


    // bắn
    // mặc định dir = -1 là bắn từ trái qua phải
    private void shootArcher(Archer a) {
        int dir = -1;
        if (a.getObjType() == ARCHER_ATTACK_LEFT) {
            dir = 1;
        }
        bullets.add(new Bullet((int) a.getHitbox().x, (int) a.getHitbox().y, dir));
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawTraps(g, xLvlOffset);
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
        drawArchers(g, xLvlOffset);
        drawBullet(g, xLvlOffset);
    }

    // hiển thị bẫy
    private void drawTraps(Graphics g, int xLvlOffset) {
        for (Spike s : spikes)
            g.drawImage(spikeImg, (int) (s.getHitbox().x - xLvlOffset), (int) (s.getHitbox().y - s.getyDrawOffset()), SPIKE_WIDTH, SPIKE_HEIGHT, null);

    }

    // hiển thị thùng
    private void drawContainers(Graphics g, int xLvlOffset) {
        for (GameContainer gc : containers)
            if (gc.isActive()) {
                //gc.drawHitbox(g, xLvlOffset);
                int type = 0;
                g.drawImage(containerImgs[type][gc.getAniIndex()], (int) (gc.getHitbox().x - gc.getxDrawOffset() - xLvlOffset), (int) (gc.getHitbox().y - gc.getyDrawOffset()), CONTAINER_WIDTH,
                        CONTAINER_HEIGHT, null);
            }
    }

    // hiển thị thuốc
    private void drawPotions(Graphics g, int xLvlOffset) {
        for (Potion p : potions)
            if (p.isActive()) {
                //p.drawHitbox(g, xLvlOffset);
                int type = 0;
                if (p.getObjType() == RED_POTION)
                    type = 1;
                g.drawImage(potionImgs[type][p.getAniIndex()], (int) (p.getHitbox().x - p.getxDrawOffset() - xLvlOffset), (int) (p.getHitbox().y - p.getyDrawOffset()), POTION_WIDTH, POTION_HEIGHT,
                        null);
            }
    }


    // hiển thị xạ thủ
    private void drawArchers(Graphics g, int xLvlOffset) {
        for (Archer a : archers) {
            if (a.isActive()) {
                //a.drawHitbox(g, xLvlOffset);
                int type = 1;
                if (a.getObjType() == ARCHER_DEATH_LEFT || a.getObjType() == ARCHER_DEATH_RIGHT)
                    type = 2;
                int x = (int) (a.getHitbox().x - xLvlOffset);
                int width = ARCHER_WIDTH;
                if (a.getObjType() == ARCHER_ATTACK_RIGHT || a.getObjType() == ARCHER_DEATH_RIGHT) {
                    x += width;
                    width *= -1;
                }
                g.drawImage(archerImgs[type][a.getAniIndex()], x - 18, (int) (a.getHitbox().y - 26), width, ARCHER_HEIGHT, null);
            }
        }
    }

    // hiển thị đạn
    private void drawBullet(Graphics g, int xLvlOffset) {
        for (Bullet b : bullets) {
            if (b.isActive()) {
                //b.drawHitbox(g, xLvlOffset);
                g.drawImage(bulletImgs[b.getAniIndex()], (int) (b.getHitbox().x - xLvlOffset - 4), (int) (b.getHitbox().y - 4), BULLET_WIDTH, BULLET_HEIGHT, null);
            }
        }
    }

    public void resetAllObjects() {
        for (Potion p : potions)
            p.reset();
        for (GameContainer gc : containers)
            gc.reset();
        for (Archer a : archers)
            a.reset();

    }
}
