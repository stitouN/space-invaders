import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Optional;

/**
 * @author N.STITOU (STITOU NAJLAA)
 * @Date de création 13/01/2021
 */
public class Player extends Pane {

    private static final String PLAYER_IMAGE = "images/player.png" ;
    private static final String PLAYER_SHOT="images/playerShot.png";
    private static final double RELOAD_TIME_SEC = 0.5;

    private double reloadingTime;

    public Player(){
        ImageView imageView = new ImageView(PLAYER_IMAGE);
        getChildren().add(imageView);
        reset();
    }

    public void reset() {
        setTranslateX(Controller.SCREEN_WIDTH / 2 - 24);
        setTranslateY(Controller.SCREEN_HEIGHT - 60);
    }

    public void left() {
        double x = getTranslateX() - Controller.PLAYER_SPEED;

        if(x < Controller.LEFT_BORDER) x = Controller.LEFT_BORDER;

        setTranslateX(x);
    }

    public void right() {
        double x = getTranslateX() + Controller.PLAYER_SPEED;

        if(x + getWidth() > Controller.RIGHT_BORDER) x = Controller.RIGHT_BORDER - getWidth();

        setTranslateX(x);
    }

    public Laser shoot(){
       Laser laser=new Laser(PLAYER_SHOT,Controller.PLAYER_BULLET_SPEED);
       laser.setPosition(getTranslateX()+19,getTranslateY());
       return laser;
    }

    public boolean playerGotShot(Laser laser) {
        if(getBoundsInParent().intersects(laser.getBoundsInParent())) {
            reset();
            return true;
        }
        return false;
    }

    public void reload(double time) {
        reloadingTime += time;
    }

    public boolean canFire() {
        if(reloadingTime >= RELOAD_TIME_SEC) {
            reloadingTime = 0;
            return true;
        }

        return false;
    }
}
