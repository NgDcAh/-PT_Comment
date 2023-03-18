package Gamestate;

import Main.Game;
import UI.MenuButton;
import audio.AudioPlayer;

import java.awt.event.MouseEvent;

public class State {
    protected Game game;
    public State (Game game) {
        this.game = game;
    }
    public Game getGame() {
        return game;
    }

    // kiểm tra chuột có ở vị trí nút( nút ở menu) ko
    public boolean isIn (MouseEvent e, MenuButton mb) {
        return mb.getBounds().contains(e.getX(), e.getY()); // getBound() tạo 1 vùng bao quanh menubutton (xem class MenuButton)
                                                            // contain() trả về true nếu chuột ở trong vùng bao quanh
    }

    public void setGameState(GameState state) {
        switch (state) {
            case MENU -> game.getAudioPlayer().playSong(AudioPlayer.MENU);
            case PLAYING -> game.getAudioPlayer().playSong(AudioPlayer.LEVEL);
        }

        GameState.state = state;
    }
}
