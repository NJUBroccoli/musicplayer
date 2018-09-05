package model;

import service.GP;

import java.util.Iterator;
import java.util.Random;
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
        printMusicInfo();
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

    public Music getNextMusic(Music currentMusic, int playMode){
        if (musics.isEmpty()){
            System.out.println("The music list is empty, and next music cannot be played.");
            return null;
        }
        if (playMode == GP.LIST_LOOP){
            Iterator<Music> iterator = musics.iterator();
            while (iterator.hasNext()){
                Music music = iterator.next();
                if (music == currentMusic)
                    return iterator.hasNext()? iterator.next() : musics.elementAt(0);
            }
        }
        else if (playMode == GP.RANDOM_LOOP){
            Random random = new Random();
            int musicNum = musics.size();
            int nextIndex = random.nextInt(musicNum);
            return musics.elementAt(nextIndex);
        }
        return null;
    }

    private void printMusicInfo(){
        Iterator<Music> iterator = musics.iterator();
        for (;iterator.hasNext();){
            iterator.next().printMusicInfo();
        }
    }
}
