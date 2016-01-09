package com.queuingSystem.sounds;

/**
 * Created by 风之凌殇 on 2016/1/9.
 */

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;


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
            MusicPlayer musicPlayer = new MusicPlayer();
            while (true) {
                Scanner in = new Scanner(System.in);
                String _customerId = in.nextLine();
                String _clientId = in.nextLine();
                try {
                    musicPlayer.play(please);
                    for (int i = 0; i < _customerId.length(); ++i) {
                        musicPlayer.play(digits[_customerId.charAt(i) - '0']);
                    }
                    musicPlayer.play(customer);
                    for (int i = 0; i < _clientId.length(); ++i) {
                        musicPlayer.play(digits[_clientId.charAt(i) - '0']);
                    }
                    musicPlayer.play(desk);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void play(String file) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        audioInputStream = AudioSystem.getAudioInputStream(new File(file));
        audioFormat = audioInputStream.getFormat();
        dataLine_info = new DataLine.Info(SourceDataLine.class, audioFormat);
        sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLine_info);
        sourceDataLine.open(audioFormat, 1024);
        sourceDataLine.start();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = audioInputStream.read(buffer)) > 0) {
            sourceDataLine.write(buffer, 0, len);
        }
        audioInputStream.close();
        sourceDataLine.drain();
        sourceDataLine.close();
    }

}