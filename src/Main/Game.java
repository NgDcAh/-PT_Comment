package Main;
import Gamestate.GameOption;
import Gamestate.GameState;
import Gamestate.Playing;
import Gamestate.Menu;
import UI.AudioOption;
import audio.AudioPlayer;

import java.awt.*;

public class Game implements Runnable{

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS = 120;
    private final int UPS = 200;
    private Playing playing;
    private Menu menu;
    private GameOption option;
    private AudioOption audioOption;
    private AudioPlayer audioPlayer;
    public final static int TILES_DEFAULT_SIZE = 32;// kích thước mặc định 1 ô

    public final static float SCALE = 1.5f; // mọi đối tượng trong game sẽ scale theo tỷ lệ này
    public final static int TILES_IN_WIDTH = 26; // chiều ngang 26 ô
    public final static int TILES_IN_HEIGHT = 14; // chiều rộng 14 ô
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
    public Game() {
        initClasses();

        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.setFocusable(true); // tra gg
        gamePanel.requestFocus();// tra gg

        startGameLoop();
    }
    // khởi tạo các đối tượng
    private void initClasses() {
        audioOption = new AudioOption(this);
        audioPlayer = new AudioPlayer();
        menu = new Menu(this);
        playing = new Playing(this);
        option = new GameOption(this);
    }
    //update
    public void update() {
        switch (GameState.state) {
            case MENU:
                menu.update();
                break;
            case PLAYING:
                playing.update();
                break;
            case OPTIONS:
                option.update();
                break;
            default:
                System.exit(0);
                break;
        }
    }
    // render hình ảnh
    public void render(Graphics g) {
        switch (GameState.state) {
            case MENU:
                menu.draw(g);
                break;
            case PLAYING:
                playing.draw(g);
                break;
            case OPTIONS:
                option.draw(g);
                break;
            case QUIT:
            default:
                System.exit(0);
                break;
        }
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        // đổi 1 giây sang nano giây để tăng độ chính xác
        Double timePerFrame = 1000000000.0 / FPS; //thời gian hiển thị 1 khung hình trong 1 giây (1 giây chia cho 120 ln hiển thị)
        Double timePerUpdate = 1000000000.0 / UPS; // thời gian 1 lần update trong 1 giây( 1 giây chia cho 200 lần update)
        long previousTime = System.nanoTime();
        int updates = 0;
        int frames = 0;
        double deltaU = 0;
        double deltaF = 0;
        long lastCheck = System.currentTimeMillis();
        while (true) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;
            /*
            deltaU bằng thời gian hiện tại trừ thời gian trước đó chia cho thời gian 1 lần up date
            nếu deltaU lớn >= 1 thì tức là lớn bằng thời gian 1 lần update nên gọi phươn thức update()
                => sẽ update 200 lần trong 1 giây
            update++, khi hết 1 giây update sẽ bằng 200
            deltaU--, giả sử deltaU = 1.2 thì deltaU-- = 0.2, 0.2 sẽ đc cộng tiếp với deltaU tiếp theo, làm vậy để tránh mất 0.2 nano giây
                không làm gì, tăng độ chính xác

            FPS cũng như thế
             */
            if (deltaU >= 1){
                update();
                updates++;
                deltaU--;
            }
            if (deltaF >= 1){
                gamePanel.repaint();
                frames++;
                deltaF--;
            }
            // sau 1 giây in ra màn hình
            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + "  UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }

    // khi đang ở trạng thái chơi mà tab ra tab khac thì gọi đến setDirBooleans() - làm cho nhân vật đứng im
    // cái này được gọi GameWindow
    public void windowFocusLost() {
        if (GameState.state == GameState.PLAYING) {
            playing.getPlayer().setDirBooleans();
        }
    }

    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }

    public GameOption getGameOption() {
        return option;
    }

    public AudioOption getAudioOption() {
        return audioOption;
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }
}
