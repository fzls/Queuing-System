package com.queuingSystem.sounds;

/**
 * Created by 风之凌殇 on 2016/1/9.
 */

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;


public class MusicPlayer {

    private AudioFormat audioFormat = null;
    private SourceDataLine sourceDataLine = null;
    private DataLine.Info dataLine_info = null;
    private AudioInputStream audioInputStream = null;

    public MusicPlayer() {
    }

    public static void main(String[] args) {
        String desk = "src/com/queuingSystem/sounds/desk.wav";
        String[] digits = new String[]{
                "src/com/queuingSystem/sounds/0.wav",
                "src/com/queuingSystem/sounds/1.wav",
                "src/com/queuingSystem/sounds/2.wav",
                "src/com/queuingSystem/sounds/3.wav",
                "src/com/queuingSystem/sounds/4.wav",
                "src/com/queuingSystem/sounds/5.wav",
                "src/com/queuingSystem/sounds/6.wav",
                "src/com/queuingSystem/sounds/7.wav",
                "src/com/queuingSystem/sounds/8.wav",
                "src/com/queuingSystem/sounds/9.wav",
        };
        String customer = "src/com/queuingSystem/sounds/customer.wav";
        String please = "src/com/queuingSystem/sounds/please.wav";
        try {
            new MusicPlayer().play(digits[1]);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }

    }

    public void play(String file) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        audioInputStream = AudioSystem.getAudioInputStream(new File(file));
        audioFormat = audioInputStream.getFormat();
        dataLine_info = new DataLine.Info(SourceDataLine.class, audioFormat);
        sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLine_info);
        byte[] b = new byte[1024];
        int len = 0;
        sourceDataLine.open(audioFormat, 1024);
        sourceDataLine.start();
        while ((len = audioInputStream.read(b)) > 0) {
            sourceDataLine.write(b, 0, len);
        }
        audioInputStream.close();
        sourceDataLine.drain();
        sourceDataLine.close();
    }

}