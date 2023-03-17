package UI;

import Gamestate.*;
import Main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.PauseButtons.*;

public class PauseOverlay {
    private BufferedImage backgroundImg;
    private Playing playing;
    private int bgX, bgY, bgWidth, bgHeight;
    private AudioOption audioOption;
    private UrmButtons menuB, replayB, unpauseB;

    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        audioOption = playing.getGame().getAudioOption();
        createUrmButtons();
    }

    private void createUrmButtons() {
        int menuX = (int) (313 * Game.SCALE);
        int replayX = (int) (387 * Game.SCALE);
        int unpauseX = (int) (462 * Game.SCALE);
        int bY = (int) (325 * Game.SCALE);

        menuB = new UrmButtons(menuX, bY, URM_SIZE, URM_SIZE, 2);
        replayB = new UrmButtons(replayX, bY, URM_SIZE, URM_SIZE, 1);
        unpauseB = new UrmButtons(unpauseX, bY, URM_SIZE, URM_SIZE, 0);
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
        bgHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgWidth / 2;
        bgY = (int) (25 * Game.SCALE);

    }

    public void update() {
        menuB.update();
        replayB.update();
        unpauseB.update();
        audioOption.update();
    }

    public void draw(Graphics g) {
        //background
        g.drawImage(backgroundImg, bgX, bgY, bgWidth, bgHeight, null);

        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);
        audioOption.draw(g);
    }

    public void mouseDragged(MouseEvent e){
        audioOption.mouseDragged(e);
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuB)) {
            menuB.setMousePressed(true);
        } else if (isIn(e, replayB)) {
            replayB.setMousePressed(true);
        } else if (isIn(e, unpauseB)) {
            unpauseB.setMousePressed(true);
        } else {
            audioOption.mousePressed(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, menuB)) {
            if (menuB.isMousePressed()) {
                GameState.state = GameState.MENU;
                playing.resetAll();
                playing.unpauseGame();
            }
        } else if (isIn(e, replayB)) {
            if (replayB.isMousePressed()) {
                playing.resetAll();
                playing.unpauseGame();
            }
        } else if (isIn(e, unpauseB)) {
            if (unpauseB.isMousePressed()) {
                playing.unpauseGame();
            }
        } else {
            audioOption.mouseReleased(e);
        }
        menuB.resetBools();
        replayB.resetBools();
        unpauseB.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);
        replayB.setMouseOver(false);
        unpauseB.setMouseOver(false);

        if (isIn(e, menuB)) {
            menuB.setMouseOver(true);
        } else if (isIn(e, replayB)) {
            replayB.setMouseOver(true);
        } else if (isIn(e, unpauseB)) {
            unpauseB.setMouseOver(true);
        } else {
            audioOption.mouseMoved(e);
        }
    }

    private boolean isIn(MouseEvent e, PauseButtons b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
