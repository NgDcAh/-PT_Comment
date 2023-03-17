package Gamestate;

import Main.Game;
import UI.AudioOption;
import UI.UrmButtons;
import UI.PauseButtons;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import static utilz.Constants.UI.PauseButtons.*;

public class GameOption extends State implements StateMethod {
    private AudioOption audioOption;
    private BufferedImage backgroudImg, optionBackgroundImg;
    private int bgX, bgY, bgW, bgH;
    private UrmButtons menuB;

    public GameOption(Game game) {
        super(game);
        loadImgs();
        loadButtons();
        audioOption = game.getAudioOption();
    }

    private void loadButtons() {
        int menuX = (int) (387 * Game.SCALE);
        int menuY = (int) (325 * Game.SCALE);
        menuB = new UrmButtons(menuX, menuY, URM_SIZE, URM_SIZE, 2);

    }

    private void loadImgs() {
        backgroudImg = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_IMG);
        optionBackgroundImg = LoadSave.GetSpriteAtlas(LoadSave.OPTION_BACKGROUND);
        bgW = (int) (optionBackgroundImg.getWidth() * Game.SCALE);
        bgH = (int) (optionBackgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (33 * Game.SCALE);
    }

    @Override
    public void update() {
        menuB.update();
        audioOption.update();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroudImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(optionBackgroundImg, bgX, bgY, bgW, bgH, null);
        menuB.draw(g);
        audioOption.draw(g);
    }

    public void mouseDragged(MouseEvent e) {
        audioOption.mouseDragged(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuB)) {
            menuB.setMousePressed(true);
        } else
            audioOption.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isIn(e, menuB)) {
            if (menuB.isMousePressed())
                GameState.state = GameState.MENU;
        } else
            audioOption.mouseReleased(e);

        menuB.resetBools();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);

        if (isIn(e, menuB))
            menuB.setMouseOver(true);
        else
            audioOption.mouseMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            GameState.state = GameState.MENU;

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private boolean isIn(MouseEvent e, PauseButtons b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
