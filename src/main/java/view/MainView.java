package view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Music;
import model.MusicLabel;
import model.MusicList;
import service.GP;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Iterator;


public class MainView extends Application {

    private static MenuBar menuBar = new MenuBar();
    private static Button runButton = new Button("START");
    private static GridPane controlPane = new GridPane();
    private static VBox imageVBox = new VBox();
    private static BorderPane borderPane = new BorderPane();
    private static ImageView imageView = new ImageView();
    private static Label musicNameLabel = new Label("Please choose a song to play!");
    private static Label musicArtistLabel = new Label();

    public static Slider volumnSlider = new Slider();
    public static Slider timeSlider = new Slider();
    public static Label timeLabel = new Label();

    private static VBox musicListVBox = new VBox();
    private static Label musicListLabel = new Label("Music List");
    private static ListView<MusicLabel> musicListView = new ListView<>();
    private static ChoiceBox<String> playModeChoice = new ChoiceBox<>();

    private static File choosedFile;
//    private MediaPlayer mediaPlayer;
    public static MusicList musicList = new MusicList();
    public static Music currentMusic;
    public static Double currentTime = new Double(0);
    public static Double totalTime = new Double(0);
    public static int playMode = GP.SELF_LOOP;

    public void start(Stage primaryStage) throws Exception{
        setChoiceBox(primaryStage);
        setLabel(primaryStage);
        setListView(primaryStage);
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

    private void setChoiceBox(Stage stage){
        String selfLoopStr = "Self Loop";
        String listLoopStr = "List Loop";
        String randomLoopStr = "Random Loop";
        playModeChoice.setItems(FXCollections.observableArrayList(selfLoopStr, listLoopStr, randomLoopStr));
        playModeChoice.getSelectionModel().select(GP.SELF_LOOP);
        playModeChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                switch (newValue.intValue()){
                    case GP.SELF_LOOP: playMode = GP.SELF_LOOP; break;
                    case GP.LIST_LOOP: playMode = GP.LIST_LOOP; break;
                    case GP.RANDOM_LOOP: playMode = GP.RANDOM_LOOP; break;
                    default: break;
                }
            }
        });
        playModeChoice.setPrefWidth(90);
    }

    private void setListView(Stage stage){
        musicListView.setEditable(false);
        musicListView.setPrefWidth(150);
    }

    private void setSlider(Stage stage){
        volumnSlider.setPrefWidth(100);
        volumnSlider.setValue(50);
        volumnSlider.setShowTickLabels(true);
        volumnSlider.setShowTickMarks(true);
        timeSlider.setPrefWidth(400);
    }

    private void setLabel(Stage stage){
        musicNameLabel.setFont(Font.font(GP.ENG_FONT_TYPE, FontWeight.BOLD, FontPosture.REGULAR, 25));
        musicNameLabel.setAlignment(Pos.CENTER);
        musicListLabel.setFont(Font.font(GP.ENG_FONT_TYPE, FontWeight.LIGHT, FontPosture.REGULAR, 15));
        musicListLabel.setAlignment(Pos.CENTER);
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
        borderPane.setCenter(imageVBox);
        borderPane.setBottom(controlPane);
        borderPane.setLeft(musicListVBox);
        borderPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private void setBox(Stage stage){
        controlPane.add(timeSlider, 0, 0);
        controlPane.add(timeLabel, 1, 0);
        controlPane.add(playModeChoice, 0, 1);
        controlPane.add(runButton, 1, 1);
        controlPane.add(volumnSlider, 2, 1);
        controlPane.setAlignment(Pos.CENTER);
        imageVBox.setAlignment(Pos.CENTER);
        imageVBox.getChildren().addAll(musicNameLabel, musicArtistLabel, imageView);
        musicListVBox.getChildren().addAll(musicListLabel, musicListView);
        musicListVBox.setAlignment(Pos.CENTER);
    }

    private void setButton(Stage stage){
        runButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (currentMusic != null){
                    if (runButton.getText().equals("START")) {
                        currentMusic.play();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                runButton.setText("PAUSE");
                            }
                        });
                    }
                    else{
                        currentMusic.pause();
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
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MP3 Music", "*.mp3"));
                choosedFile = fileChooser.showOpenDialog(stage);
                if (choosedFile == null){
                    return;
                }
                if (!validFileType(choosedFile)){
                    System.out.println("Invalid file type: only .mp3 are allowed.");
                    return;
                }
                try {
                    if (currentMusic != null)
                        currentMusic.stop();
                    currentMusic = new Music(choosedFile.getPath());
                    musicList.add(currentMusic);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            runButton.setText("PAUSE");
                            updateUI(currentMusic, true);
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

    public static void updateUI(Music music, boolean addNewMusic){
        updateButtion();
        updateImage(music);
        updateLabel(music);
        updateMusicList(music, addNewMusic);
    }

    private static void updateButtion(){
        runButton.setText("PAUSE");
    }

    private static void updateMusicList(Music music, boolean addNewMusicLabel){
        if (addNewMusicLabel) {
            MusicLabel musicLabel = new MusicLabel();
            musicLabel.setFont(Font.font(GP.ENG_FONT_TYPE, FontWeight.LIGHT, FontPosture.REGULAR, 13));
            musicLabel.setText(music.getTitle() + "---" + music.getArtist() + "     " + secToStr(new Double(music.getLengthInSec())));
            musicLabel.setMusic(music);
            musicLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() > 1) {
                        if (currentMusic != null)
                            currentMusic.stop();

                        currentMusic = ((MusicLabel) event.getSource()).getMusic();
                        currentMusic.seek(Duration.ZERO);
                        currentMusic.play();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                runButton.setText("PAUSE");
                                updateUI(currentMusic, false);
                            }
                        });
                    }
                }
            });
            musicListView.getItems().addAll(musicLabel);
        }
        if (currentMusic != null){
            for (MusicLabel ml : musicListView.getItems()){
                if (ml.getMusic() == currentMusic) {
                    ml.setTextFill(Color.BLUE);
                }
                else
                    ml.setTextFill(Color.BLACK);
            }
        }
    }

    private static void updateLabel(Music music){
        musicNameLabel.setText(music.getTitle());
        musicArtistLabel.setText(music.getArtist());
    }

    private static void updateImage(Music music){
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

    public static String secToStr(Double seconds){
        Integer count = seconds.intValue();
        Integer Hours = count / 3600;
        count = count % 3600;
        Integer Minutes = count /60;
        count = count % 60;
        return Hours.toString()+":"+Minutes.toString()+":"+count.toString();
    }
}
