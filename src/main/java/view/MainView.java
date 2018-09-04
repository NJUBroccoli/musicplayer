package view;

import com.mpatric.mp3agic.Mp3File;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Music;
import service.MusicPlay;
import java.io.File;


public class MainView extends Application {

    private MenuBar menuBar = new MenuBar();
    private File choosedFile;
    private MediaPlayer mediaPlayer;

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
                try {
                    Music music = new Music(choosedFile.getPath());
                }catch (Exception e){
                    e.printStackTrace();
                }
//                mediaPlayer = MusicPlay.play(choosedFile);
            }
        });
        fileMenu.getItems().add(openFileMenu);
        menuBar.getMenus().add(fileMenu);
    }
}
