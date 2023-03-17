package levels;

import Main.Game;
import entities.AssassinEnemy;
import entities.Boss;
import entities.SpearEnemy;
import objects.Archer;
import objects.GameContainer;
import objects.Potion;
import objects.Spike;
import utilz.CheckMethods;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.CheckMethods.*;

public class Level {
    private BufferedImage img;
    private ArrayList<AssassinEnemy> assassinEnemies;
    private ArrayList<SpearEnemy> spearmanEnemies;
    private Boss boss;
    private ArrayList<Spike> spikes;
    private ArrayList<GameContainer> containers;
    private ArrayList<Potion> potions;
    private ArrayList<Archer> archers;
    private int[][] lvlData;
    private int lvlTilesWide;
    private int maxTileOffset;
    private int maxLvlOffsetX;
    private Point playerSpawn;

    public Level(BufferedImage img){
        this.img = img;
        createLevelData();
        createEnemies();
        createSpikes();
        createContainers();
        createPotions();
        createArchers();
        calcLvlOffsets();
        calcPlayerSpawn();
    }

    // tạo xạ thủ
    private void createArchers() {
        archers = CheckMethods.GetArchers(img);
    }
    // tạo bẫy
    private void createSpikes() {
        spikes = CheckMethods.getSpikes(img);
    }
    // tạo thùng
    private void createContainers() {
        containers = CheckMethods.GetContainers(img);
    }
    // tạo thuốc
    private void createPotions() {
        potions = CheckMethods.GetPotions(img);
    }
    // lấy vị trí nhân vật xuất hiện
    private void calcPlayerSpawn() {
        playerSpawn = GetPlayerSpawn(img);
    }


    private void calcLvlOffsets() {
        lvlTilesWide = img.getWidth(); /* chiều dài cả level, là cái ảnh trong folder Levels
                                          (mỗi ô trong ảnh kích thuoc 1px tương ứng với 1 ô)
                                        */
        maxTileOffset = lvlTilesWide - Game.TILES_IN_WIDTH; /* tổng chiều dài phần không hiển thị ra cửa sổ window
                                                            (phần hiển thị chỉ có 26 ô ví dụ ảnh dài 40 ô
                                                            thì chiểu dài ko hiển thị là 40 - 26 = 14 ô)
                                                               */
        maxLvlOffsetX = Game.TILES_SIZE * maxTileOffset; // nhân với kích thước thật hiển thị ra màn hình để ra chiểu dài thật
    }

    // tạo quái
    private void createEnemies() {
        assassinEnemies = GetAssassins(img);
        spearmanEnemies = GetSpearman(img);
        boss = GetBoss(img);
    }
    // tạo level
    private void createLevelData() {
        lvlData = GetLevelData(img); //xem class CheckMethods dòng 136
    }
    // load mấy cái khối vuông để tạo map
    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }

    public int[][] getLvlData() {
        return lvlData;
    }

    public int getLvlOffset() {
        return maxLvlOffsetX;
    }

    public ArrayList<AssassinEnemy> getAssassinEnemies() {
        return assassinEnemies;
    }

    public ArrayList<SpearEnemy> getSpearmanEnemies() {
        return spearmanEnemies;
    }

    public Boss getBoss() {
        return boss;
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }

    public ArrayList<Spike> getSpikes() {
        return spikes;
    }

    public ArrayList<Potion> getPotions() {
        return potions;
    }

    public ArrayList<GameContainer> getContainers() {
        return containers;
    }
    public ArrayList<Archer> getArchers() {
        return archers;
    }
}
