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
                "src/com/queuingSystem/sounds/ten.wav",
                "src/com/queuingSystem/sounds/hundred.wav",
                "src/com/queuingSystem/sounds/thousand.wav",
                "src/com/queuingSystem/sounds/tenThousand.wav",
        };
        String customer = "src/com/queuingSystem/sounds/customer.wav";
        String please = "src/com/queuingSystem/sounds/please.wav";
        try {
            MusicPlayer musicPlayer = new MusicPlayer();
            while (true) {
                Scanner in = new Scanner(System.in);
                String Id = in.nextLine();
                try {
                    int endingZeros = 0, index = Id.length() - 1;
                    while (index >= 0 && Id.charAt(index) == '0') {
                        endingZeros++;
                        index--;
                    }
                    boolean successiveZero = false;
                    for (int i = 0; i < Id.length(); ++i) {
                        if (Id.length() == 2 && Id.charAt(0) == '1') {
                            musicPlayer.play(digits[10]);
                            if (Id.charAt(1) != '0')
                                musicPlayer.play(digits[Id.charAt(1) - '0']);
                            break;
                        }
                        if (i <= Id.length() - 2 && Id.charAt(i) == '0' && Id.charAt(i + 1) == '0')
                            continue;
                        else
                            musicPlayer.play(digits[Id.charAt(i) - '0']);
                        int _digit = Id.length() - i + 8;
                        if (Id.charAt(i) != '0' && _digit >= 10)
                            musicPlayer.play(digits[_digit]);
                        if (i + endingZeros + 1 == Id.length())
                            break;
                    }
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