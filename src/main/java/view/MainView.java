package view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Music;
import model.MusicList;

import java.io.ByteArrayInputStream;
import java.io.File;


public class MainView extends Application {

    private MenuBar menuBar = new MenuBar();
    private Button runButton = new Button("START");
    private HBox hBox = new HBox(15);
    private VBox vBox = new VBox(5);
    private BorderPane borderPane = new BorderPane();
    private ImageView imageView = new ImageView();
    private Label musicNameLabel = new Label("Please choose a song to play!");
    private Slider volumnSlider = new Slider();

    private File choosedFile;
    private MediaPlayer mediaPlayer;
    private MusicList musicList = new MusicList();
    private Music currentMusic;

    public void start(Stage primaryStage) throws Exception{
        setLabel(primaryStage);
        setSlider(primaryStage);
        setImage(primaryStage);
        setMenu(primaryStage);
        setButton(primaryStage);
        setBox(primaryStage);
        setPane(primaryStage);
        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("musicplayer");
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }

    private void setSlider(Stage stage){
        volumnSlider.setPrefWidth(100);
        volumnSlider.setValue(50);
        volumnSlider.setShowTickLabels(true);
        volumnSlider.setShowTickMarks(true);
    }

    private void setLabel(Stage stage){
        musicNameLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 20));
    }

    private void setImage(Stage stage){
        File defaultImageFile = new File("out/production/musicplayer/default_image.jpg");
        String url = defaultImageFile.toURI().toString();
        imageView.setImage(new Image(url));
        imageView.setFitWidth(400);
        imageView.setFitHeight(400);
        imageView.setCache(true);
    }

    private void setPane(Stage stage){
        borderPane.setTop(menuBar);
        borderPane.setCenter(vBox);
        borderPane.setBottom(hBox);
    }

    private void setBox(Stage stage){
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(runButton, volumnSlider);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(musicNameLabel, imageView);
    }

    private void setButton(Stage stage){
        runButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (currentMusic != null){
                    if (runButton.getText().equals("START")) {
                        mediaPlayer.play();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                runButton.setText("PAUSE");
                            }
                        });
                    }
                    else{
                        mediaPlayer.pause();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                runButton.setText("START");
                            }
                        });
                    }
                }
                else{
                    System.out.println("There is no music you can play");
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
                    if (currentMusic != null)
                        mediaPlayer.stop();
                    currentMusic = new Music(choosedFile.getPath());
                    musicList.add(currentMusic);
                    Media media = new Media(new File(currentMusic.getFilename()).toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.setOnEndOfMedia(() -> {
                        mediaPlayer.stop();
                        mediaPlayer.seek(Duration.ZERO);
                        mediaPlayer.play();
                    });
                    mediaPlayer.volumeProperty().bind(volumnSlider.valueProperty().divide(100));
                    mediaPlayer.play();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            runButton.setText("PAUSE");
                            updateUI(currentMusic);
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

    private void updateUI(Music music){
        updateImage(music);
        updateLabel(music);
    }

    private void updateLabel(Music music){
        musicNameLabel.setText(music.getTitle() + "\n Artist: " + music.getArtist());
    }

    private void updateImage(Music music){
        try{
            Image img = new Image(new ByteArrayInputStream(music.getAlbumImageData()));
            imageView.setImage(img);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean validFileType(File choosedFile){
        String fileName = choosedFile.getName();
        if ("mp3".equals(fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase()))
            return true;
        return false;
    }
}
