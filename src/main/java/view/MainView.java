package view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Music;
import model.MusicList;
import service.MusicPlay;

import java.io.File;


public class MainView extends Application {

    private MenuBar menuBar = new MenuBar();
    private File choosedFile;
    private MediaPlayer mediaPlayer;
    private MusicList musicList = new MusicList();

    public void start(Stage primaryStage) throws Exception{
        setMenu(primaryStage);
        Group root = new Group(menuBar);
        Scene scene = new Scene(root, 600, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("musicplayer");
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }

    private void setMenu(Stage stage){
        Menu fileMenu = new Menu("File");
        MenuItem openFileMenu = new MenuItem("Open");
        openFileMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                choosedFile = fileChooser.showOpenDialog(stage);
                if (choosedFile != null && !validFileType(choosedFile)){
                    System.out.println("Invalid file type: only .mp3 are allowed.");
                    return;
                }
                try {
                    Music music = new Music(choosedFile.getPath());
                    musicList.add(music);
                   if (mediaPlayer != null)
                       mediaPlayer.stop();
                    mediaPlayer = MusicPlay.play(choosedFile, mediaPlayer);
                }catch (Exception e){
                    e.printStackTrace();
                }
//                mediaPlayer = MusicPlay.play(choosedFile);
            }
        });
        fileMenu.getItems().add(openFileMenu);
        menuBar.getMenus().add(fileMenu);
    }

    private boolean validFileType(File choosedFile){
        String fileName = choosedFile.getName();
        if ("mp3".equals(fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase()))
            return true;
        return false;
    }
}
