package model;

import com.mpatric.mp3agic.*;

import java.io.IOException;

public class Music extends Mp3File{
    private int ID3V;
    private long lengthInSeconds;
    private int bitrate;
    private boolean isVbr;
    private int sampleRate;
    private String track;
    private String artist;
    private String title;
    private String album;
    private String year;
    private int genre;
    private String genreDescription;
    private String comment;
    private String composer;
    private String publisher;
    private String original_artist;
    private String album_artist;
    private String copyright;
    private String url;
    private String encoder;
    private byte[] albumImageData;

    public Music(){
        super();
    }
    public Music(String pathName) throws IOException, UnsupportedTagException, InvalidDataException {
        super(pathName);
        if (this.hasId3v2Tag())
            ID3V = 2;
        else if (this.hasId3v1Tag())
            ID3V = 1;
        else
            ID3V = 0;
        fillData();
    }

    public void printMusicInfo(){
        System.out.println("##### Music Info #####\n"
                + "ID3V" + ID3V + "\n"
                + "length: " + lengthInSeconds + " secs\n"
                + "bitrate: " + bitrate + " kbps " + (isVbr ? "(VBR)" : "(CBR)") + "\n"
                + "sampleRate: " + sampleRate + " Hz\n"
                + "track: " + track + "\n"
                + "artist: " + artist + "\n"
                + "title: " + title + "\n"
                + "album: " + album + "\n"
                + "year: " + year + "\n");
        if (ID3V == 2){
            System.out.println(
                    "genre: " + genre + " (" + genreDescription + ")" + "\n"
                    + "comment: " + comment + "\n"
                    + "composer: " + composer + "\n"
                    + "publisher: " + publisher + "\n"
                    + "original_artist: " + original_artist + "\n"
                    + "album_artist: " + album_artist + "\n"
                    + "copyright: " + copyright + "\n"
                    + "url: " + url + "\n"
                    + "encoder: " + encoder + "\n");
            if (albumImageData != null){
                System.out.println("Have album image data, length: " + albumImageData.length + " bytes");
                System.out.println("Album image mime type: " + this.getId3v2Tag().getAlbumImageMimeType());
            }
        }
    }

    private void fillData(){
        lengthInSeconds = this.getLengthInSeconds();
        bitrate = this.getBitrate();
        isVbr = this.isVbr();
        sampleRate = this.getSampleRate();
        switch (ID3V){
            case 2:
                ID3v2 id3v2Tag = this.getId3v2Tag();
                track = id3v2Tag.getTrack();
                artist = id3v2Tag.getArtist();
                title = id3v2Tag.getTitle();
                album = id3v2Tag.getAlbum();
                year = id3v2Tag.getYear();
                genre = id3v2Tag.getGenre();
                genreDescription = id3v2Tag.getGenreDescription();
                comment = id3v2Tag.getComment();
                composer = id3v2Tag.getComposer();
                publisher = id3v2Tag.getPublisher();
                original_artist = id3v2Tag.getOriginalArtist();
                album_artist = id3v2Tag.getAlbumArtist();
                copyright = id3v2Tag.getCopyright();
                url = id3v2Tag.getUrl();
                encoder = id3v2Tag.getEncoder();
                albumImageData = id3v2Tag.getAlbumImage();
                break;
            case 1:
                ID3v1 id3v1Tag = this.getId3v1Tag();
                track = id3v1Tag.getTrack();
                artist = id3v1Tag.getArtist();
                title = id3v1Tag.getTitle();
                album = id3v1Tag.getAlbum();
                year = id3v1Tag.getYear();
                genre = id3v1Tag.getGenre();
                genreDescription = id3v1Tag.getGenreDescription();
                comment = id3v1Tag.getComment();
                break;
            case 0:
                System.out.println("Read MP3 Information failed.");
        }
    }

    public String getTitle(){
        return title;
    }

    public String getArtist(){
        return artist;
    }

    public byte[] getAlbumImageData(){
        return albumImageData;
    }

}
