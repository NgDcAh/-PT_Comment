package utilz;

import Main.Game;
import entities.AssassinEnemy;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.ASSASSIN;

public class LoadSave {
    public final static String PLAYER_ATLAS = "/Character.png";
    public final static String LEVEL_ATLAS = "/TileSets/tilesaet_1.png";
    public final static String MENU_BUTTONS = "/GUI/button_atlas.png";
    public final static String MENU_BACKGROUND = "/GUI/Menu_BackGround.png";
    public final static String PAUSE_BACKGROUND = "/GUI/pause_menu.png";
    public final static String SOUND_BUTTON = "/GUI/sound_button.png";
    public final static String URM_BUTTONS = "/GUI/urm_buttons.png";
    public final static String VOLUME_BUTTONS = "/GUI/volume_buttons.png";
    public final static String BACKGROUND_IMG = "/Background/Background_menu.png";
    public final static String PLAYING_BACKGROUND_1 = "/Background/Background_1.png";
    public final static String PLAYING_MOUNTAIN_1 = "/Background/Mountain_1.png";
    public final static String ENEMIES_ASSASSIN = "/Enemies/assassin.png";
    public final static String ENEMIES_SPEARMAN = "/Enemies/Spearman.png";
    public final static String ENEMIES_BOSS = "/Enemies/boss.png";
    public final static String STATUS_BAR = "/health_power_bar.png";
    public final static String DEATH_SCREEN = "/GUI/death_screen.png";
    public final static String COMPELED_IMG = "/GUI/completed_sprite.png";
    public final static String TRAP_ATLAS = "/GUI/trap_atlas.png";
    public final static String POTION_ATLAS = "/GUI/potions_sprites.png";
    public final static String CONTAINER_ATLAS = "/GUI/objects_sprites.png";
    public final static String ARCHER_ATLAS = "/Enemies/archer.png";
    public final static String BULLET = "/Enemies/bullet.png";
    public final static String OPTION_BACKGROUND = "/GUI/options_background.png";

    public static BufferedImage GetSpriteAtlas(String filename){
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream(filename);
        try {
            img = ImageIO.read(is);
        }
        catch(
        IOException e){
            e.printStackTrace();
        } finally{
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return img;
    }

    public static BufferedImage[] GetAllLevels() {
        URL url = LoadSave.class.getResource("/Level");
        File file = null;

        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        File[] files = file.listFiles();
        File[] fileSorted = new File[files.length];

        for (int i = 0; i < fileSorted.length; i++) {
            for (int j = 0; j < files.length; j++) {
                if (files[j].getName().equals((i + 1) + ".png")) {
                    fileSorted[i] = files[j];
                }
            }
        }

        BufferedImage[] imgs = new BufferedImage[fileSorted.length];

        for (int i = 0; i < imgs.length; i++) {
            try {
                imgs[i] = ImageIO.read(fileSorted[i]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return imgs;
    }

}
