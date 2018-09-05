package view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.embed.swt.FXCanvas;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

import java.io.ByteArrayInputStream;
import java.io.File;


public class MainView extends Application {

    private MenuBar menuBar = new MenuBar();
    private Button runButton = new Button("START");
    private GridPane controlPane = new GridPane();
    private VBox imageVBox = new VBox();
    private BorderPane borderPane = new BorderPane();
    private ImageView imageView = new ImageView();
    private Label musicNameLabel = new Label("Please choose a song to play!");
    private Label musicArtistLabel = new Label();
    private Slider volumnSlider = new Slider();
    private Slider timeSlider = new Slider();
    private Label timeLabel = new Label();
    private VBox musicListVBox = new VBox();
    private Label musicListLabel = new Label("Music List");
    private ListView<MusicLabel> musicListView = new ListView<>();
    private ChoiceBox<String> playModeChoice = new ChoiceBox<>();

    private File choosedFile;
    private MediaPlayer mediaPlayer;
    private MusicList musicList = new MusicList();
    private Music currentMusic;
    private Double currentTime = new Double(0);
    private Double totalTime = new Double(0);
    private int playMode = LIST_LOOP;

    private static final String FONT_TYPE = "Times New Roman";
    private static final int SELF_LOOP = 1;
    private static final int LIST_LOOP = 2;
    private static final int RANDOM_LOOP = 3;

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
        playModeChoice.getSelectionModel().select(1);
        playModeChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                switch (newValue.intValue()){
                    case SELF_LOOP: playMode = SELF_LOOP; break;
                    case LIST_LOOP: playMode = LIST_LOOP; break;
                    case RANDOM_LOOP: playMode = RANDOM_LOOP; break;
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
        musicNameLabel.setFont(Font.font(FONT_TYPE, FontWeight.BOLD, FontPosture.REGULAR, 25));
        musicNameLabel.setAlignment(Pos.CENTER);
        musicListLabel.setFont(Font.font(FONT_TYPE, FontWeight.LIGHT, FontPosture.REGULAR, 15));
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
                        mediaPlayer.stop();
                    currentMusic = new Music(choosedFile.getPath());
                    musicList.add(currentMusic);
                    updateMediaPlayer();
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

    private void updateMediaPlayer(){
        Media media = new Media(new File(currentMusic.getFilename()).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnReady(() -> {
            totalTime = mediaPlayer.getStopTime().toSeconds();
        });
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.stop();
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.play();
        });
        mediaPlayer.currentTimeProperty().addListener(ov->{
            currentTime = mediaPlayer.getCurrentTime().toSeconds();
            timeLabel.setText(secToStr(currentTime) + "/" + secToStr(totalTime));
            timeSlider.setValue(currentTime / totalTime * 100);
        });
        timeSlider.valueProperty().addListener(ov->{
            if (timeSlider.isValueChanging()){
                mediaPlayer.seek(mediaPlayer.getTotalDuration().multiply(timeSlider.getValue() / 100));
            }
        });
        mediaPlayer.volumeProperty().bind(volumnSlider.valueProperty().divide(100));
        mediaPlayer.play();
    }

    private void updateUI(Music music, boolean addNewMusic){
        updateImage(music);
        updateLabel(music);
        if (addNewMusic)
            updateMusicList(music, true);
    }

    private void updateMusicList(Music music, boolean addNewMusicLabel){
        if (addNewMusicLabel) {
            MusicLabel musicLabel = new MusicLabel();
            musicLabel.setFont(Font.font(FONT_TYPE, FontWeight.LIGHT, FontPosture.REGULAR, 13));
            musicLabel.setText(music.getTitle() + "---" + music.getArtist());
            musicLabel.setMusic(music);
            musicLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() > 1) {
                        if (currentMusic != null)
                            mediaPlayer.stop();
                        currentMusic = ((MusicLabel) event.getSource()).getMusic();
                        updateMediaPlayer();
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

    private void updateLabel(Music music){
        musicNameLabel.setText(music.getTitle());
        musicArtistLabel.setText(music.getArtist());
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

    private String secToStr(Double seconds){
        Integer count = seconds.intValue();
        Integer Hours = count / 3600;
        count = count % 3600;
        Integer Minutes = count /60;
        count = count % 60;
        return Hours.toString()+":"+Minutes.toString()+":"+count.toString();
    }
}
