package model;

import java.util.Iterator;
import java.util.Vector;

public class MusicList {
    private Vector<Music> musics = new Vector<Music>();
    public MusicList(Music ... musics){
        int musicsLength = musics.length;
        for (int i = 0; i < musicsLength; i++)
            this.musics.add(musics[i]);
    }
    public void add(Music music){
        Iterator<Music> iterator = musics.iterator();
        for (;iterator.hasNext();){
            Music m = iterator.next();
            if (m.getTitle().equals(music.getTitle())) {
                System.out.println("Filename conflicts. Add failed.");
                return;
            }
        }
        musics.add(music);
    }
    public void delete(Music music){
        Iterator<Music> iterator = musics.iterator();
        for (;iterator.hasNext();){
            Music m = iterator.next();
            if (m == music) {
                musics.remove(music);
                return;
            }
        }
        System.out.println("The music doesn't exist in the list.");
    }
    public Music getByName(String musicName){
        Iterator<Music> iterator = musics.iterator();
        for (;iterator.hasNext();){
            Music music = iterator.next();
            if (music.getTitle().equals(musicName)) {
                return music;
            }
        }
        System.out.println("Cannot find the music you want to open.");
        return null;
    }
}
