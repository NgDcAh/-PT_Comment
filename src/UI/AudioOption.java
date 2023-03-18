package UI;

import Main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;

import static utilz.Constants.UI.PauseButtons.*;
import static utilz.Constants.UI.PauseButtons.SOUND_SIZE;

public class AudioOption {
    private VolumeButton volumeButton;
    private SoundButton musicButton, sfxButton;
    private Game game;

    public AudioOption(Game game) {
        this.game = game;
        createSoundButtons();
        createVolumeButton();
    }

    // nút kéo thả âm lượng
    private void createVolumeButton() {
        int vX = (int) (309 * Game.SCALE);
        int vY = (int) (278 * Game.SCALE);
        volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    // bật tắt âm thanh và hiệu ứng
    private void createSoundButtons() {
        int soundX = (int) (450 * Game.SCALE);
        int musicY = (int) (140 * Game.SCALE);
        int sfxy = (int) (186 * Game.SCALE);
        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButton(soundX, sfxy, SOUND_SIZE, SOUND_SIZE);
    }

    public void update() {
        musicButton.update();
        sfxButton.update();
        volumeButton.update();
    }

    public void draw(Graphics g) {
        musicButton.draw(g);
        sfxButton.draw(g);
        volumeButton.draw(g);
    }

    // kéo thả nút âm lượng
    public void mouseDragged(MouseEvent e){
        if (volumeButton.isMousePressed()) {
            float valueBefore = volumeButton.getFloatValue(); // xem class VoulumeButton
            volumeButton.changeX(e.getX());
            float valueAfter = volumeButton.getFloatValue();
            if(valueBefore != valueAfter)
                game.getAudioPlayer().setVolume(valueAfter);
        }
    }


    //setMousePressed, setMouseOver xem class SoundButton
    public void mousePressed(MouseEvent e) {
        if (isIn(e, musicButton)){
            musicButton.setMousePressed(true);
        } else if (isIn(e, sfxButton)){
            sfxButton.setMousePressed(true);
        } else if (isIn(e, volumeButton)) {
            volumeButton.setMousePressed(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, musicButton)) {
            if (musicButton.isMousePressed()) {
                musicButton.setMuted(!musicButton.isMuted());// setMuted xem class SoundButton
                game.getAudioPlayer().toggleSongMute(); // toggleSongMute xem class AudioPlayer
            }
        } else if (isIn(e, sfxButton)) {
            if (sfxButton.isMousePressed()) {
                sfxButton.setMuted(!sfxButton.isMuted());
                game.getAudioPlayer().toggleEffectMute(); // toggleEffectMute xem class AudioPlayer
            }
        }
        musicButton.resetBools();
        sfxButton.resetBools();
        volumeButton.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        volumeButton.setMouseOver(false);

        if (isIn(e, musicButton))
            musicButton.setMouseOver(true);
        else if (isIn(e, sfxButton))
            sfxButton.setMouseOver(true);
        else if (isIn(e, volumeButton))
            volumeButton.setMouseOver(true);
    }

    private boolean isIn(MouseEvent e, PauseButtons b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
