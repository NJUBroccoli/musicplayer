package view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Music;
import model.MusicList;

import java.io.File;


public class MainView extends Application {

    private MenuBar menuBar = new MenuBar();
    private Button runButton = new Button("START");
    private Button pauseButton = new Button("PAUSE");

    private File choosedFile;
    private MediaPlayer mediaPlayer;
    private MusicList musicList = new MusicList();
    private Music currentMusic;
    Double slTime ;

    public void start(Stage primaryStage) throws Exception{
        setMenu(primaryStage);
        setButton(primaryStage);
        Group root = new Group(menuBar, runButton, pauseButton);
        Scene scene = new Scene(root, 600, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("musicplayer");
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }

    private void setButton(Stage stage){
        runButton.setLayoutX(250);
        runButton.setLayoutY(250);
        pauseButton.setLayoutX(350);
        pauseButton.setLayoutY(250);
        runButton.setVisible(true);
        pauseButton.setVisible(false);
        runButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (currentMusic != null){
                    mediaPlayer.play();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            switchRunButton();
                        }
                    });
                }
                else{
                    System.out.println("There is no music you can play");
                }
            }
        });
        pauseButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (currentMusic != null){
                    if (mediaPlayer != null) {
                        mediaPlayer.pause();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                switchRunButton();
                            }
                        });
                    }
                }
                else{
                    System.out.println("There is no music you can pause");
                }
            }
        });
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
                    currentMusic = new Music(choosedFile.getPath());
                    musicList.add(currentMusic);
                    Media media = new Media(new File(currentMusic.getFilename()).toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.setOnEndOfMedia(() -> {
                        mediaPlayer.stop();
                        mediaPlayer.seek(Duration.ZERO);
                    });
                    mediaPlayer.play();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            switchRunButton();
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        fileMenu.getItems().add(openFileMenu);
        menuBar.getMenus().add(fileMenu);
    }

    private void switchRunButton(){
        if (runButton.isVisible()){
            runButton.setVisible(false);
            pauseButton.setVisible(true);
        }
        else{
            pauseButton.setVisible(false);
            runButton.setVisible(true);
        }
    }

    private boolean validFileType(File choosedFile){
        String fileName = choosedFile.getName();
        if ("mp3".equals(fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase()))
            return true;
        return false;
    }
}
