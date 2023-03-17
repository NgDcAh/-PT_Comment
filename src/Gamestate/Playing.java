package Gamestate;

import Main.Game;
import UI.GameOverOverlay;
import UI.LevelCompletedOverLay;
import UI.PauseOverlay;
import audio.AudioPlayer;
import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import objects.ObjectManager;
import utilz.LoadSave;
import static utilz.Constants.Environment.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Playing extends State implements StateMethod{
    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private GameOverOverlay gameOverOverlay;
    private ObjectManager objectManager;
    private LevelCompletedOverLay levelCompletedOverLay;
    private boolean paused = false;
    private PauseOverlay pauseOverlay;
    private int xLvlOffset;
    private int leftBorder = (int) (0.2 * Game.GAME_WIDTH); // lấy vị trí 20% chiều dài cửa sổ làm mốc
    private int rightBorder = (int) (0.8 * Game.GAME_WIDTH); // lấy vị trí 80% chiều dài cửa sổ làm mốc
    private int maxLvlOffsetX;
    private BufferedImage bgImage_1, mountain_1;
    private boolean gameOver;
    private boolean lvlCompleted;
    private boolean playerDying;

    public Playing(Game game) {
        super(game);
        initClasses();

        bgImage_1 = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND_1);
        mountain_1 = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_MOUNTAIN_1);

        calcLvOffset();
        loadStartLevel();
    }

    public void loadNextLevel() {
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        resetAll();
    }

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());
    }

    // tính maxLvlOffsetX
    private void calcLvOffset() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
    }

    private void initClasses() {
        player = new Player(500, 200, (int) (60 * Game.SCALE), (int) (40 * Game.SCALE), this);
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager(this);
        player.loadLvlData(levelManager.getCurrentLevel().getLvlData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        pauseOverlay =  new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverLay = new LevelCompletedOverLay(this);
    }
    @Override
    public void update() {

        if (paused) {
            pauseOverlay.update();
        } else if (lvlCompleted) {
            levelCompletedOverLay.update();
        } else if (gameOver) {
            gameOverOverlay.update();
        } else if (playerDying) {
            player.update();
        } else {
            levelManager.update();
            objectManager.update(levelManager.getCurrentLevel().getLvlData(), player);
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLvlData(), player);
            checkCloseToBorder();
        }
    }

    // xét trường hợp nhân vật chạm 2 vào 2 mốc trái và phải
    private void checkCloseToBorder() {
        int playerX = (int) player.getHitBox().x; // lấy vị trí hiện tại
        int diff = playerX - xLvlOffset; // lấy vị trí mới
        // Chỗ này hơi khó hiểu
        /*
            khi vị trí nhân vật đi qua rightBorder là viều phải
            nó sẽ sinh ra giá trị cho biến xLvlOffset = vị trí nhân vật - vị trí viền phải là 1 KHOẢNG
            xem các phương thức draw hình ảnh ra màn hình, ở tham số x ta thấy luôn trừ đi xLvlOffset
            là nó kéo tất cả hình ảnh về phía bên trái, nó tạo hiệu ứng map đang di chuyển
            bởi vì đi qua viền phải rồi nên phải tính lại biến diff = playerX - xLvlOffset nó sẽ ra vị trí mới chưa qua viền phải
            viền trái tương tự
         */

        if (diff > rightBorder) {
            xLvlOffset += diff - rightBorder;
        } else if (diff < xLvlOffset) {
            xLvlOffset += diff - leftBorder;
        }
        /*
            maxLvlOffsetX là chiều dài tối đa mà màn hình có thể kéo theo (tính ở class Level)
            thử cmt lại đoạn code dưới chạy thấy nó sẽ kéo mán hình ra vô hạn
         */
        if (xLvlOffset > maxLvlOffsetX) {
            xLvlOffset = maxLvlOffsetX;
        } else if(xLvlOffset < 0) {
            xLvlOffset = 0;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(bgImage_1, 0, 0, Game.GAME_WIDTH, Game.GAME_WIDTH, null);
        drawEnvironment(g);
        levelManager.draw(g, xLvlOffset);
        player.render(g, xLvlOffset);
        enemyManager.draw(g, xLvlOffset);
        objectManager.draw(g, xLvlOffset);
        if (paused) {
            g.setColor(new Color(0,0,0, 100));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        } else if (gameOver) {
            gameOverOverlay.draw(g);
        } else if (lvlCompleted) {
            levelCompletedOverLay.draw(g);
        }
    }

    // vẽ núi
    private void drawEnvironment(Graphics g) {
        for (int i = 0; i < 3; i++) {
            g.drawImage(mountain_1, i * PB1_WIDTH - (int) (xLvlOffset * 0.3), (int) (20 * Game.SCALE), PB1_WIDTH, PB1_HEIGHT, null);
        }
    }

    // reset khi sang level mới hoặc chơi lại
    public void resetAll() {
        gameOver = false;
        paused = false;
        lvlCompleted = false;
        playerDying = false;
        player.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObjects();
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setMaxLvlOffset(int lvlOffset) {
        this.maxLvlOffsetX = lvlOffset;
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }

    public void checkSpikeTouched(Player player) {
        objectManager.checkSpikeTouched(player);
    }

    public void checkObjectHit(Rectangle2D.Float attackBox) {
        objectManager.checkObjectHit(attackBox);
    }

    public void checkPotionTouched(Rectangle2D.Float hitbox) {
        objectManager.checkObjectTouched(hitbox);
    }

    public void mouseDragged(MouseEvent e) {
        if (!gameOver) {
            if (paused) {
                pauseOverlay.mouseDragged(e);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                player.setAttack(true);
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                player.powerAttack();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!gameOver) {
            if (paused) {
                pauseOverlay.mousePressed(e);
            } else if (lvlCompleted) {
                levelCompletedOverLay.mousePressed(e);
            }
        } else {
            gameOverOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!gameOver) {
            if (paused) {
                pauseOverlay.mouseReleased(e);
            } else if (lvlCompleted) {
                levelCompletedOverLay.mouseReleased(e);
            }
        } else {
            gameOverOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameOver) {
            if (paused) {
                pauseOverlay.mouseMoved(e);
            } else if (lvlCompleted) {
                levelCompletedOverLay.mouseMoved(e);
            }
        } else {
            gameOverOverlay.mouseMoved(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) {
            gameOverOverlay.keyPressed(e);
        } else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(false);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(false);
                    break;
            }
        }
    }

    public void unpauseGame() {
        paused = false;
    }

    public void setLevelCompleted(boolean levelCompleted) {
        this.lvlCompleted = levelCompleted;
        if(levelCompleted)
            game.getAudioPlayer().lvlCompleted();
    }

    public Player getPlayer() {
        return player;
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public ObjectManager getObjectManager() {
        return objectManager;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public void windowFocusLost() {
        player.setDirBooleans();
    }

    public void setPlayerDying(boolean b) {
        this.playerDying = playerDying;
    }
}
