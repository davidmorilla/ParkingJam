package es.upm.pproject.parkingjam.parking_jam.view.utils;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class MusicPlayer {
    private Clip clip;
    private FloatControl volumeControl;

    public MusicPlayer(String soundFilePath) {
        try {
            URL soundFile = getClass().getClassLoader().getResource(soundFilePath);
            if (soundFile == null) {
                throw new IllegalArgumentException("Sound file not found: " + soundFilePath);
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            // Obtener el control de volumen si está disponible
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                setVolume(0.5f); // Ajusta el volumen al 50%
            }

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music continuously
        }
    }

    public void playErase() {
        Clip erase = null;
        try {
            URL soundFile = getClass().getClassLoader().getResource("erase.wav");
            if (soundFile == null) {
                throw new IllegalArgumentException("Sound file not found: erase.wav" );
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            erase = AudioSystem.getClip();
            erase.open(audioInputStream);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        if (erase != null) {
            erase.setFramePosition(0);
            erase.start();
        }
    }

    public void playLevelSuccess() {
        Clip levelSuccess = null;
        try {
            URL soundFile = getClass().getClassLoader().getResource("levelSuccess.wav");
            if (soundFile == null) {
                throw new IllegalArgumentException("Sound file not found: levelSuccess.wav" );
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            levelSuccess = AudioSystem.getClip();
            levelSuccess.open(audioInputStream);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        if (levelSuccess != null) {
            levelSuccess.setFramePosition(0);
            levelSuccess.start();
        }
    }

    public void playLevelStart() {
        Clip restartLevel = null;
        try {
            URL soundFile = getClass().getClassLoader().getResource("restartLevel.wav");
            if (soundFile == null) {
                throw new IllegalArgumentException("Sound file not found: restartLevel.wav" );
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            restartLevel = AudioSystem.getClip();
            restartLevel.open(audioInputStream);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        if (restartLevel != null) {
            restartLevel.setFramePosition(0);
            restartLevel.start();
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void setVolume(float volume) {
        if (volumeControl != null) {
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            float range = max - min;
            float gain = min + (range * volume);
            volumeControl.setValue(gain);
        }
    }
}
