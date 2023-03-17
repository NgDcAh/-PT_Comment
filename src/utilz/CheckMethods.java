package utilz;

import Main.Game;
import entities.AssassinEnemy;
import entities.SpearEnemy;
import objects.*;
import entities.Boss;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.Objects.*;

public class CheckMethods {

    // kiểm tra xem có thể di chuyển đến đây hay ko
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        // xét vị trí đứng hiện tại
        if (!isSolid(x, y, lvlData)) {
            // xét vị trí trái phải lên xuống
            if (!isSolid(x + width, y + height, lvlData)) {
                // xét vị trí trái phải
                if (!isSolid(x + width, y, lvlData)) {
                    // xét vị trí lên xuống
                    if (!isSolid(x, y + height, lvlData)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // kiểm tra xem nó có phải khối vuông trên màn hình hiển thị
    public static boolean isSolid(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * Game.TILES_SIZE;
        if (x < 0 || x >= maxWidth) {
            return true;
        }
        if (y < 0 || y >= Game.GAME_HEIGHT) {
            return true;
        }
        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;

        return isTileSolid((int) xIndex, (int) yIndex, lvlData);
    }

    // kiểm tra xem nó có phải là khối vuông
    public static boolean isTileSolid(int xTile, int yTile, int[][] lvlData) {
        int value = lvlData[(int) yTile][(int) xTile];
        // 58 là giá trị trống
        if (value >= 150 || value < 0 || value != 58) {
            return true;
        } else {
            return false;
        }
    }

    public static float GetEntityPositionNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile = (int) (hitbox.x / Game.TILES_SIZE);
        if (xSpeed > 0) {
            int tilePosition = currentTile * Game.TILES_SIZE;
            int xOffSet = (int) (Game.TILES_SIZE - hitbox.width);
            return tilePosition + xOffSet - 1;
        } else {
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static float getEntityPositionUnderRootOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile = (int) (hitbox.y / Game.TILES_SIZE);
        if (airSpeed > 0) {
            int tileYPosition = currentTile * Game.TILES_SIZE;
            int yOffSet = (int) (Game.TILES_SIZE - hitbox.height);
            return tileYPosition + yOffSet - 1;
        } else {
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static boolean isEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        if (!isSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData)) {
            if (!isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        if (xSpeed > 0) {
            return isSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        } else {
            return isSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        }
    }

    public static boolean IsAllTilesClear(int xStart, int xEnd, int y, int[][] lvlData) {
        for (int i = 0; i < xEnd - xStart; i++)
            if (isTileSolid(xStart + i, y, lvlData))
                return false;
        return true;
    }

    public static boolean isAllTileWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
        for (int i = 0; i < xEnd - xStart; i++) {
            if (isTileSolid(xStart + i, y, lvlData))
                return false;
            if (!isTileSolid(xStart + i, y + 1, lvlData))
                return false;
        }

        return true;
    }

    public static boolean isSightClear(int[][] lvlData, Rectangle2D.Float firstHitBox, Rectangle2D.Float secondHitBox, int yTile) {
        int firstXTile = (int) (firstHitBox.x / Game.TILES_SIZE);
        int secondXTile = (int) (secondHitBox.x / Game.TILES_SIZE);

        if (firstXTile > secondXTile) {
            return isAllTileWalkable(secondXTile, firstXTile, yTile, lvlData);
        } else {
            return isAllTileWalkable(firstXTile, secondXTile, yTile, lvlData);
        }
    }

    // kiểm tra đạn chạm vào vật thể
    public static boolean IsBulletHittingLevel(Bullet b, int[][] lvlData) {
        return isSolid(b.getHitbox().x + b.getHitbox().width / 2, b.getHitbox().y + b.getHitbox().height / 2, lvlData);

    }

    // cách tạo level
    // vẽ cái ảnh như trong folder level
    // cái ảnh sẽ là mảng 2 chiều mỗi phần tử là 1x1 px
    // xét theo hệ màu RGB, lấy màu đỏ làm chủ đạo
    // chỉ số màu đỏ của mỗi ô trong ảnh sẽ tương ứng với thứ tự các khối trong mảng levelSprites
    // ta sẽ có mảng lvlData 2 chiều gồm giá trị index mà index thuộc levelSprites[index]

    public static int[][] GetLevelData(BufferedImage img) {
        int[][] lvlData = new int[img.getHeight()][img.getWidth()];

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();
                if (value >= 150)
                    value = 0;
                lvlData[j][i] = value;
            }
        return lvlData;
    }

    public static ArrayList<AssassinEnemy> GetAssassins(BufferedImage img) {
        ArrayList<AssassinEnemy> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == ASSASSIN)
                    list.add(new AssassinEnemy(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
            }
        return list;
    }

    public static ArrayList<SpearEnemy> GetSpearman(BufferedImage img) {
        ArrayList<SpearEnemy> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == SPEARMAN)
                    list.add(new SpearEnemy(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
            }
        return list;
    }

    public static Boss GetBoss(BufferedImage img) {
        Boss boss = null;
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == BOSS) {
                    boss = new Boss(i * Game.TILES_SIZE, j * Game.TILES_SIZE);
                    break;
                }
            }
        return boss;
    }

    public static Point GetPlayerSpawn(BufferedImage img) {
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == 100)
                    return new Point(i * Game.TILES_SIZE, j * Game.TILES_SIZE);
            }
        return new Point(1 * Game.TILES_SIZE, 1 * Game.TILES_SIZE);
    }

    public static ArrayList<Spike> getSpikes(BufferedImage img) {
        ArrayList<Spike> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == SPIKE) {
                    list.add(new Spike(i * Game.TILES_SIZE, j * Game.TILES_SIZE, SPIKE));
                }
            }
        return list;
    }

    public static ArrayList<Potion> GetPotions(BufferedImage img) {
        ArrayList<Potion> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == RED_POTION || value == BLUE_POTION)
                    list.add(new Potion(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));
            }

        return list;
    }

    public static ArrayList<GameContainer> GetContainers(BufferedImage img) {
        ArrayList<GameContainer> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == BOX || value == BARREL)
                    list.add(new GameContainer(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));
            }

        return list;
    }

    public static ArrayList<Archer> GetArchers(BufferedImage img) {
        ArrayList<Archer> list = new ArrayList<>();

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == ARCHER_ATTACK_LEFT || value == ARCHER_ATTACK_RIGHT)
                    list.add(new Archer(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));
            }

        return list;
    }
}
