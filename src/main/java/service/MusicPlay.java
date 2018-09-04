package service;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class MusicPlay {
    public static MediaPlayer play(File file, MediaPlayer mediaPlayer){
        Media media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(20);
        mediaPlayer.play();
        return mediaPlayer;
    }
}
