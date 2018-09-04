package service;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class MusicPlay {
    public static MediaPlayer play(File file){
        Media media = new Media(file.toURI().toString());
//        PrintMessage.printMusicInfo(media);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(20);
        mediaPlayer.play();
        return mediaPlayer;
    }
}
