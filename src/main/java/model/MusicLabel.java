package model;

import javafx.scene.control.Label;

public class MusicLabel extends Label {
    private Music music;
    public MusicLabel(){
        super();
    }
    public MusicLabel(String text){
        super(text);
    }
    public void setMusic(Music music){
        this.music = music;
    }
    public Music getMusic(){
        return music;
    }
}
