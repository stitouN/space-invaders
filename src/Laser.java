import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;



/**
 * @author N.STITOU (STITOU NAJLAA)
 * @Date de création 16/01/2021
 */
public class Laser extends Pane {

    private boolean alive=true;
    private double fireSpeed=0;

    public Laser(String imageFile,double speed){
        fireSpeed=speed;
        ImageView imageView=new ImageView(imageFile);
        getChildren().add(imageView);
    }

    public void setPosition(double x,double y){
        setTranslateX(x);
        setTranslateY(y);
    }

    public void update(double time){
        setTranslateY(getTranslateY() + fireSpeed * time);
    }
}
