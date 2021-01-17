import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class SpaceInvaders extends Application {

    private ObservableList<Node> nodes;
    private List<Laser> enemyLasers;
    private List<Laser> playerLasers;
    private Controller controller;
    private Pane root;
    private Scene scene;
    private Enemy enemy;
    private Player player;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("template.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        scene = new Scene(root);
        nodes = root.getChildren();
        controller=loader.getController();
        initValues();

        playerEvent();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Space Invaders");
        primaryStage.show();
        new AnimationTimer() {
            long lastNanoTime = System.nanoTime();
            @Override
            public void handle(long currentNanoTime) {
                // Calculate time since last update.
                double elapsedTime = (currentNanoTime - lastNanoTime) / 1_000_000_000.0;
                lastNanoTime = currentNanoTime;

                player.reload(elapsedTime);
                enemy.update(elapsedTime);

                handleEnemyShoots(elapsedTime);
                handlePlayerShoots(elapsedTime);

            }

        }.start();
    }

    // add LEFT/RIGHT move on to the player
    private void playerEvent() {
        scene.setOnKeyPressed(event -> {
         switch (event.getCode()){
             case LEFT:
                 player.left();
                  break;
             case RIGHT:
                 player.right();
                 break;
             case SPACE:
                 playerShoot();
                 break;
         }
                });
    }

    private void initValues() {
        enemy =new Enemy();
        player=new Player();

        nodes.add(enemy);
        nodes.add(player);

        enemyLasers=new LinkedList<>();
        playerLasers=new LinkedList<>();
    }

    public void handleEnemyShoots(double time){
        Optional<Laser> laserOptional=enemy.fire();
        if(laserOptional.isPresent()){
            enemyLasers.add(laserOptional.get());
            nodes.add(laserOptional.get());
        }
        Iterator<Laser> laserIterable=  enemyLasers.iterator();
        while (laserIterable.hasNext()){
            Laser laser=laserIterable.next();
            laser.update(time);
            if(laser.getTranslateY() >= Controller.SCREEN_HEIGHT) {
                laserIterable.remove();
                nodes.remove(laser);
            }


        }
    }

    public void handlePlayerShoots(double time){
        Iterator<Laser> it=playerLasers.listIterator();
        while(it.hasNext()){
            Laser laser=it.next();
            laser.update(time);
            if(laser.getTranslateY() <= 30) {
                it.remove();
                nodes.remove(laser);
            }

            Optional<Bounds> damagedBounds = enemy.enemyGotShot(laser);

            damagedBounds.ifPresent(bounds -> {
                it.remove();
                nodes.remove(laser);
                enemy.setGotShooted(true);
                controller.updateScore(10);
            });

        }

    }

    private void playerShoot(){
        Laser laser=player.shoot();
        if(player.canFire()){
          nodes.add(laser);
          playerLasers.add(laser);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
