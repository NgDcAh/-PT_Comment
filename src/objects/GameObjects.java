package objects;

import Main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.Objects.*;
import static utilz.Constants.Objects.GetSpriteAmount;

public class GameObjects {
    protected int x, y, objType;
    protected Rectangle2D.Float hitBox;
    protected boolean doAnimation, active = true;
    protected int aniTick, aniIndex;
    protected int ANI_SPEED;
    protected int xDrawOffset, yDrawOffset;

    public GameObjects(int x, int y, int objType) {
        this.x = x;
        this.y = y;
        this.objType = objType;
    }

    // chạy Animation
    /*
        Game chạy trong vòng lặp vô hạn
        aniTick là để đo thời gian, mỗi lần lặp aniTick++ tức là + 1
        ANI_SPEED sẽ là giới hạn thời gian
        khi aniTick++ đến mức ANI_SPEED thì aniIndex++ tức là load 1 hình
        cách tạo animation là chạy các hình ảnh liên tiếp nhau
        khi aniIndex > GetSpriteAmount(objType) - số lượng khung hình của objType thì aniIndex đặt lại = 0
    */

    protected void updateAnimationTick() {
        aniTick++;
        int startAni = objType;

        if (objType == ARCHER_ATTACK_LEFT || objType == ARCHER_ATTACK_RIGHT) {
            ANI_SPEED = 60;
        } else {
            ANI_SPEED = 25;
        }
            if (aniTick >= ANI_SPEED) {
                aniTick = 0;
                aniIndex++;
                if (aniIndex >= GetSpriteAmount(objType)) {
                    aniIndex = 0;

                    switch (objType) {
                        case BOX:
                            active = false;// khi đánh vào chạy animation vỡ và ko hiển thị nữa
                            break;
                        case ARCHER_DEATH_LEFT, ARCHER_DEATH_RIGHT:
                            active = false;// chạy animation chết và ko hin thị nữa
                    }
                }
            }
        if (startAni != objType) {
            resetAniTick(); // ví dụ khi đang load đến khung hình 4 cửa animation 1
                            // mà animation chuyển sang 2 thì nó sẽ chuyển sang khung hình 4 của animation 2
                            // nó làm hiện tượng giật lag mất hình nên khi chuyển phải reset tất cả
        }
    }

    private void resetAniTick() {
        aniIndex = 0;
        aniTick = 0;
    }

    // reset khi bắt đầu choi lại
    public void reset() {
        aniIndex = 0;
        aniTick = 0;

        if (objType == BOX) {
            doAnimation = false;
            active = true;
        }
        else if (objType == RED_POTION || objType == BLUE_POTION){
            doAnimation = true;
            active = false;
        }
        else if (objType == ARCHER_DEATH_LEFT) {
            objType = ARCHER_ATTACK_LEFT;
            active = true;
        }
        else if (objType == ARCHER_DEATH_RIGHT) {
            objType = ARCHER_ATTACK_RIGHT;
            active = true;
        }
    }

    // khởi tạo hitbox
    protected void initHitbox( int width, int height) {
        hitBox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
    }

    public void drawHitbox(Graphics g, int xLvlOffset) {
        g.setColor(Color.PINK);
        g.drawRect((int) hitBox.x - xLvlOffset, (int) hitBox.y, (int) hitBox.width, (int) hitBox.height);
    }

    public int getObjType() {
        return objType;
    }

    public void setObjType(int objType) {
        this.objType = objType;
    }

    public Rectangle2D.Float getHitbox() {
        return hitBox;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setAnimation(boolean doAnimation) {
        this.doAnimation = doAnimation;
    }

    public int getxDrawOffset() {
        return xDrawOffset;
    }

    public int getyDrawOffset() {
        return yDrawOffset;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public int getAniTick() {
        return aniTick;
    }

}
