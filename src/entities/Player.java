package entities;

import Gamestate.Playing;
import Main.Game;
import audio.AudioPlayer;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import static utilz.Constants.PlayerConstants.*;
import static utilz.CheckMethods.*;
import static utilz.Constants.*;

public class Player extends Entity{
    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed;
    private int playerAction = IDLE;
    private boolean moving = false, attacking = false;
    private boolean left, up, right, down, jump;
    private float playerSpeed = 1.5f;
    private int[][] lvlData;
    private float xDrawOffset = 40 * Game.SCALE;
    private float yDrawOffset = 52 * Game.SCALE;
    //Jumping / Gravity
    private float airSpeed = 0f;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;
    private int tileY = 0;
    //Status bar
    private BufferedImage statusBarImg;
    private int statusBarWidth = (int) (192 * Game.SCALE);
    private int statusBarHeight = (int) (58 * Game.SCALE);
    private int statusBarX = (int) (10 * Game.SCALE);
    private int statusBarY = (int) (10 * Game.SCALE);
    private int healthBarWidth = (int) (150 * Game.SCALE);
    private int healthBarHeight = (int) (4 * Game.SCALE);
    private int healthBarXStart = (int) (34 * Game.SCALE);
    private int healthBarYStart = (int) (14 * Game.SCALE);
    private int maxHealth = 100;

    private int powerBarWidth = (int) (104 * Game.SCALE);
    private int powerBarHeight = (int) (2 * Game.SCALE);
    private int powerBarXStart = (int) (44 * Game.SCALE);
    private int powerBarYStart = (int) (34 * Game.SCALE);
    private int powerWidth = powerBarWidth;
    private int powerMaxValue = 500;
    private int powerValue = powerMaxValue;
    private int currentHealth = maxHealth;
    private int healthWidth = healthBarWidth;

    // Attack box
    private Rectangle2D.Float attackBox;
    private int flipX = 0;
    private int flipW = 1;
    private boolean attackChecked;
    private Playing playing;

    private boolean powerAttackActive;
    private int powerAttackTick;
    private int powerGrowSpeed = 15;
    private int powerGrowTick;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        loadAnimations();
        initHitBox(x, y, (int) (24 * Game.SCALE), (int) (28 * Game.SCALE));
        initAttackBox();
    }

    //set v trí player
    public void setSpawn(Point spawn) {
        this.x = spawn.x;
        this.y = spawn.y;
        hitBox.x = x;
        hitBox.y = y;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (20 * Game.SCALE), (int) (20 * Game.SCALE));
        resetAttackBox();
    }

    public void update() {
        updateHealthBar();
        updatePowerBar();
        if (currentHealth <= 0) {
            if (playerAction != DEAD){
                playerAction = DEAD;
                aniTick = 0;
                aniIndex = 0;
                playing.setPlayerDying(true);
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
            } else if (aniIndex == GetSpriteAmount(DEAD) - 1 && aniTick >= aniSpeed - 1){
                playing.setGameOver(true);
                playing.getGame().getAudioPlayer().stopSong();
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
            } else {
                updateAnimationTick();
            }
            return;
        }
        updateAttackBox();
        updatePosition();
        if (moving) {
            checkSpikesTouched();
            checkPotionTouched();
            tileY = (int) (hitBox.y / Game.TILES_SIZE);
            if (powerAttackActive) {
                powerAttackTick++;
                if (powerAttackTick >= 35) {
                    powerAttackTick = 0;
                    powerAttackActive = false;
                }
            }
        }
        if (attacking) {
            checkAttack();
        }
        updateAnimationTick();
        setAnimation();
    }


    private void checkSpikesTouched() {
        playing.checkSpikeTouched(this);
    }

    private void checkPotionTouched() {
        playing.checkPotionTouched(hitBox);
    }

    private void checkAttack() {
        if (attackChecked || aniIndex != 1)
            return;
        attackChecked = true;
        playing.checkEnemyHit(attackBox);
        playing.checkObjectHit(attackBox);
        playing.getGame().getAudioPlayer().playAttackSound();

        if (powerAttackActive)
            attackChecked = false;
        playing.getGame().getAudioPlayer().playAttackSound();
    }

    private void updateAttackBox() {
        if (right && left) {
            if (flipW == 1) {
                attackBox.x = hitBox.x + hitBox.width + (int) (Game.SCALE * 10);
            } else {
                attackBox.x = hitBox.x - hitBox.width - (int) (Game.SCALE * 10);
            }
        }
        else if (right || (powerAttackActive && flipW == 1)) {
            attackBox.x = hitBox.x + hitBox.width;
        } else if (left || (powerAttackActive && flipW == -1)) {
            attackBox.x = hitBox.x - hitBox.width;
        }
        attackBox.y = hitBox.y + (Game.SCALE * 4);
    }

    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
    }

    private void updatePowerBar() {
        powerWidth = (int) ((powerValue / (float) powerMaxValue) * powerBarWidth);

        powerGrowTick++;
        if (powerGrowTick >= powerGrowSpeed) {
            powerGrowTick = 0;
            changePower(1);
        }
    }

    public void render(Graphics g, int lvlOffset) {
        g.drawImage(animations[playerAction][aniIndex],
                (int) (hitBox.x - xDrawOffset) - lvlOffset + flipX,
                (int) (hitBox.y - yDrawOffset),  160 * flipW, 160, null);
        //drawHitBox(g, lvlOffset);
        //drawAttackBox(g, lvlOffset);

        drawUI(g);
    }

    private void drawAttackBox(Graphics g, int lvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int) (attackBox.x - lvlOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        //hp
        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY,healthWidth, healthBarHeight);
        //mana
        g.setColor(Color.yellow);
        g.fillRect(powerBarXStart + statusBarX, powerBarYStart + statusBarY, powerWidth, powerBarHeight);
    }

    private void setAnimation() {
        int startAni = playerAction;

        if (moving){
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }
        if (inAir) {
            if (airSpeed < 0){
                playerAction = JUMPING;
            } else {
                playerAction = FALLING;
            }
        }
        if (powerAttackActive) {
            playerAction = ATTACK_2;
            aniIndex = 1;
            aniTick = 0;
            return;
        }
        if (attacking) {
            playerAction = ATTACK;
            if (startAni != ATTACK) {
                aniIndex = 1;
                aniTick = 0;
                return;
            }
        }
        if (startAni != playerAction) {
            resetAniTick();
        }

    }

    private void updatePosition() {
        moving = false;
        if (jump){
            jump();
        }
        if (!inAir){
            if (!powerAttackActive) {
                if ((!left && !right) || (right && left)) {
                    return;
                }
            }
        }

        float xSpeed = 0;

        if(left && !right) {
            xSpeed -= playerSpeed;
            flipX = 160;
            flipW = -1;
        }
        if(right && !left) {
            xSpeed += playerSpeed;
            flipX = 0;
            flipW = 1;
        }
        if (!inAir) {
            if (!isEntityOnFloor(hitBox, lvlData)) {
                inAir = true;
            }
        }

        if (powerAttackActive) {
            if ((!left && !right) || (left && right)) {
                if (flipW == -1)
                    xSpeed = -playerSpeed ;
                else
                    xSpeed = playerSpeed ;
            }

            xSpeed *= 3;
        }

        if (inAir && !powerAttackActive) {
            if (CanMoveHere(hitBox.x , hitBox.y + airSpeed, hitBox.width, hitBox.height, lvlData)) {
                hitBox.y += airSpeed;
                airSpeed += GRAVITY;
                updateXPosition(xSpeed);
            }
            else {
                hitBox.y = getEntityPositionUnderRootOrAboveFloor(hitBox, airSpeed);
                if (airSpeed > 0) {
                    resetInAir();
                } else {
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXPosition(xSpeed);
            }
        }
        else{
            updateXPosition(xSpeed);
        }
        moving = true;
    }

    private void jump() {
        if (inAir){
            return;
        }
        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
        inAir = true;
        airSpeed = jumpSpeed;
    }

    public void changeHealth (int value) {
        currentHealth += value;

        if (currentHealth <= 0) {
            currentHealth = 0;
            //gameOver();
        } else if (currentHealth >= maxHealth) {
            currentHealth = maxHealth;
        }
    }

    public void kill() {
        currentHealth = 0;
    }

    public void changePower(int value) {
        powerValue += value;
        if (powerValue >= powerMaxValue)
            powerValue = powerMaxValue;
        else if (powerValue <= 0)
            powerValue = 0;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPosition(float xSpeed) {
        if (CanMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, lvlData)) {
            hitBox.x += xSpeed;
        }
        else {
            hitBox.x = GetEntityPositionNextToWall(hitBox, xSpeed);
            if (powerAttackActive) {
                powerAttackActive = false;
                powerAttackTick = 0;
            }
        }
    }
    private void updateAnimationTick(){
        if (playerAction == ATTACK) {
            loadAnimationTick(10);
        } else {
            loadAnimationTick(20);
        }
    }

    private void loadAnimationTick(int aniSpeed) {
        aniTick++;
        if (aniTick >= aniSpeed){
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(playerAction)){
                aniIndex = 0;
                attacking = false;
                attackChecked = false;
            }
        }
    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void loadAnimations() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
        animations = new BufferedImage[7][19];
        for (int i = 0; i < animations.length; i++) {
            for (int j = 0; j < animations[i].length; j++) {
                animations[i][j] = img.getSubimage(j * 64, i * 64, 64, 64);
            }
        }

        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (!isEntityOnFloor(hitBox, lvlData)) {
            inAir = true;
        }
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setDirBooleans() {
        left = false;
        up = false;
        right = false;
        down = false;
    }

    public void setAttack(boolean attacking){
        this.attacking = attacking;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
        up = false;
        down = false;
    }

    public void resetAll() {
        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        airSpeed = 0f;
        playerAction = IDLE;
        currentHealth = maxHealth;
        powerValue = powerMaxValue;
        hitBox.x = x;
        hitBox.y = y;
        resetAttackBox();

        if (!isEntityOnFloor(hitBox, lvlData))
            inAir = true;
    }

    private void resetAttackBox() {
        if (flipW == 1) {
            attackBox.x = hitBox.x + hitBox.width + (int) (Game.SCALE * 10);
        } else {
            attackBox.x = hitBox.x - hitBox.width - (int) (Game.SCALE * 10);
        }
    }

    public void powerAttack() {
        if (powerAttackActive) {
            return;
        }
        if (powerValue >= 200) {
            powerAttackActive = true;
            changePower(-200);
        }
    }
}
