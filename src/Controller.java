import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public static final double SCREEN_WIDTH = 800;
    public static final double SCREEN_HEIGHT = 600;

    public static final double BORDER = 32;
    public static final double LEFT_BORDER = BORDER;
    public static final double RIGHT_BORDER = SCREEN_WIDTH - BORDER;

    public static final double PLAYER_SPEED=10.0;
    public static final double PLAYER_BULLET_SPEED=-300.0;
    public static final double ENEMY_SPEED=0.5;
    public static final double ENEMY_VERTICAL_SPEED=10.0;
    public static final int PLAYER_LIVES=3;
    public static final String LIFE_IMAGE="images/player.png";

    @FXML
    private Label scoreLabel;
    @FXML
    private HBox livesCounter;
    @FXML
    private Label livesLabel;

    private int lives;
    private int score;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
      reset();
    }

    public void reset() {
        score = 0;
        lives = PLAYER_LIVES;

        scoreLabel.setText("0");
        livesCounter.getChildren().clear();
        livesCounter.getChildren().add(livesLabel);

        for (int i = 0; i < PLAYER_LIVES; i++) {
            ImageView imageView = new ImageView(LIFE_IMAGE);
            livesCounter.getChildren().add(imageView);
        }
    }

    public void updateScore(int value) {
        score += value;
        scoreLabel.setText(String.valueOf(score));
    }

    public void decreaseLivesCounter(){
        livesCounter.getChildren().remove(0);
        lives--;
    }

    public int getLives() {
        return lives;
    }
}
