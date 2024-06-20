package es.upm.pproject.parkingjam.parking_jam.view.utils;

import javax.sound.sampled.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.URL;

/**
 * MusicPlayer reproduces and controlls sound files
 */
public class MusicPlayer {
    private Clip clip;
    private FloatControl volumeControl;

    private static final Logger logger = LoggerFactory.getLogger(MusicPlayer.class);

    /**
     * MusicPlayer constructor
     *
     * @param soundFilePath the sound filepath to be played
     * @throws IllegalArgumentException if sound file not found
     */
    public MusicPlayer(String soundFilePath) {
    	logger.info("Creating music player...");
        try {
        	// Get sound file and open it
            URL soundFile = getClass().getClassLoader().getResource(soundFilePath);
            if (soundFile == null) {
                throw new IllegalArgumentException("Sound file not found: " + soundFilePath);
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            // Set volumen to 50% if possible
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                setVolume(0.5f);
            }
            logger.info("Music player created.");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            logger.error("Cannot initialize the music player: {}", e.getLocalizedMessage());
        }
    }

    /**
     * Plays the music on a loop
     */
    public void play() {
    	logger.info("Playing music...");
        if (clip != null) {
            logger.info("Music started playing.");
            clip.setFramePosition(0);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music continuously
        }
    }

    /**
     * Play the erase sound
     */
    public void playErase() {
    	logger.info("Playing erase sound...");
        Clip erase = null;
        try {
        	// Get sound file and open it
            URL soundFile = getClass().getClassLoader().getResource("erase.wav");
            if (soundFile == null) {
                logger.info("Erase sound not played because it was not found.");
                throw new IllegalArgumentException("Sound file not found: erase.wav");
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            erase = AudioSystem.getClip();
            erase.open(audioInputStream);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            logger.error("Error when playing the erase sound: {}", e.getLocalizedMessage());
        }
        if (erase != null) {
            erase.setFramePosition(0);
            // Play the sound
            erase.start();
            logger.info("Erase sound played.");
        }
    }

    /**
     * Play the level success sound
     */
    public void playLevelSuccess() {
    	logger.info("Playing level success sound...");

        Clip levelSuccess = null;
        try {
        	// Get sound file and open it
            URL soundFile = getClass().getClassLoader().getResource("levelSuccess.wav");
            if (soundFile == null) {
                logger.info("Success sound was not played because it was not found.");
                throw new IllegalArgumentException("Sound file not found: levelSuccess.wav");
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            levelSuccess = AudioSystem.getClip();
            levelSuccess.open(audioInputStream);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            logger.error("Error when playing the level success sound: {}", e.getLocalizedMessage());
        }
        if (levelSuccess != null) {
            levelSuccess.setFramePosition(0);
            // Play the sound
            levelSuccess.start();
            logger.info("Success sound played.");
        }
    }

    /**
     * Play the level start sound
     */
    public void playLevelStart() {
    	logger.info("Playing level start sound...");

        Clip restartLevel = null;
        try {
        	// Get sound file and open it
            URL soundFile = getClass().getClassLoader().getResource("restartLevel.wav");
            if (soundFile == null) {
                logger.info("Start sound was not played because it was not found.");
                throw new IllegalArgumentException("Sound file not found: restartLevel.wav");
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            restartLevel = AudioSystem.getClip();
            restartLevel.open(audioInputStream);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            logger.error("Error when playing the level start sound: {}.", e.getLocalizedMessage());
        }
        if (restartLevel != null) {
            restartLevel.setFramePosition(0);
            // Play the sound
            restartLevel.start();
            logger.info("Start sound played.");
        }
    }

    /**
     * Adjust the sound volume
     *
     * @param volume the desired volumen (0.0 to 1.0)
     */
    private final void setVolume(float volume) {
    	logger.info("Setting volume to {} ...", volume);

        if (volumeControl != null) {
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            float range = max - min;
            float gain = min + (range * volume);
            volumeControl.setValue(gain);
            logger.info("Volume set to {}.", volume);
        }
    }
}
