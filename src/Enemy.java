import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


import java.util.Iterator;
import java.util.ListIterator;
import java.util.Optional;

/**
 * @author N.STITOU (STITOU NAJLAA)
 * @Date de création 12/01/2021
 */
public class Enemy extends Pane {

    private static final int COLS = 11;
    // this constant is used to avoid unlimited shoots
    private static final double RELOAD_TIME_SEC = 1;
    private static final String ORANGE_ALIEN_IMAGE="images/orangeAlien.png";
    private static final String PINK_ALIEN_IMAGE="images/pinkAlien.png";
    private static final String RED_ALIEN_IMAGE="images/redAlien.png";
    private static final String ENEMY_LASER="images/alienShot.png";
    private static final double ROWS = 5 ;

    private double speedPerNanoTime = 100;
    private double speedDelta = 2;
    private double fireSpeed=300;
    private double positionX,initPositionX = 144.0;
    private double positionY,initPositionY = 100.0;
    private double gap = 20.0;
    private double rowHeight = gap;
    private double maxHeight=initPositionY+(4*rowHeight);
    // this variable helps to avoid infinite shoots by taking a break while Aliens are moving
    private double reloadingTime;
    private boolean isGotShooted=false;


    private ObservableList<Node> nodes;
    ListIterator<Node> it;

    public Enemy(){
        setTranslateY(positionY);
        //Orange Alien Creation
        createAliens(0,2,ORANGE_ALIEN_IMAGE);
        //Pink Alien Creation
        createAliens(2,2,PINK_ALIEN_IMAGE);
        //Red Alien Creation
        createAliens(4,1,RED_ALIEN_IMAGE);
        nodes=getChildren();
         it = nodes.listIterator(nodes.size());
    }


    private void createAliens(int start,int rows, String imageUrl){
        for (int i = 0; i < COLS; i++) {
            for (int j = 0; j < rows; j++) {
                ImageView alien = new ImageView(imageUrl);
                alien.setTranslateX(i * (35 + gap));
                //the start is equal to rows+start of previous method call
                alien.setTranslateY((j+start) * (25 + gap));
                getChildren().add(alien);
            }
        }

    }

    public void update(double time) {

       // init positions when the aliens go down at maxHeight
        if(positionY==maxHeight){
            positionX=initPositionX;
            positionY=initPositionY;
        }
        else if(positionX + getWidth() > Controller.RIGHT_BORDER) {
            speedDelta += 1;
            speedPerNanoTime = -100 - speedDelta;
            positionY += rowHeight;
        }
        else if(positionX < Controller.LEFT_BORDER) {
            speedDelta += 1;
            speedPerNanoTime = 100 + speedDelta;
            positionY += rowHeight;
        }

        setTranslateX(positionX);
        setTranslateY(positionY);
        positionX += speedPerNanoTime * time;

        reload(time);
    }

    /**
     * this method will prepare one enemy for shooting
     * on the position in relation to the player
     *
     * @return Optional to check whether there is a fire released or not
     */
    public Optional<Laser> fire() {
        if(readyToShoot()) {
            while (it.hasPrevious()) {
                ImageView alien = (ImageView) it.previous();
                double x = alien.getTranslateX();

                if (nodes.stream().noneMatch(anotherAlien ->
                        alien.getTranslateX() == anotherAlien.getTranslateX() &&
                                alien.getTranslateY() < anotherAlien.getTranslateY())) {
                    Laser laser = new Laser(ENEMY_LASER, fireSpeed);
                    laser.setPosition(getTranslateX() + alien.getTranslateX() + alien.getFitWidth() / 2,
                            alien.getTranslateY() + alien.getFitHeight());
                    return Optional.of(laser);
                }
            }
        }
        if(isGotShooted || !it.hasPrevious()){
            it= nodes.listIterator(nodes.size());
            isGotShooted=false;
        }
        return Optional.empty();
    }

    public Optional<Bounds> enemyGotShot(Laser laser) {
        Iterator<Node> it = getChildren().iterator();

        while(it.hasNext()) {
            Node alien = it.next();

            Bounds bounds = getBoundsInParent();
            Bounds alienBounds = alien.getBoundsInParent();
            Bounds absolutAlienBounds = new BoundingBox(
                    bounds.getMinX() + alienBounds.getMinX(),
                    bounds.getMinY() + alienBounds.getMinY(),
                    alienBounds.getWidth(), alienBounds.getHeight());

            if(absolutAlienBounds.intersects(laser.getBoundsInParent())) {
                it.remove();
                return Optional.of(absolutAlienBounds);
            }
        }

        return Optional.empty();
    }


    public void reload(double time) {
        reloadingTime += time;
    }

    public boolean readyToShoot() {
        //shoot every second
        if(reloadingTime >= RELOAD_TIME_SEC) {
            reloadingTime = 0;
            return true;
        }

        return false;
    }

    public void setGotShooted(boolean gotShooted) {
        isGotShooted = gotShooted;
    }
}
