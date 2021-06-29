package goistreamtoolredux.controller;

import javafx.application.Preloader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SplashScreenLoader extends Preloader {

    private Stage splashScreen;

    @Override
    public void start(Stage stage) throws Exception {
        splashScreen = stage;
        splashScreen.initStyle(StageStyle.UNDECORATED); //no top bar
        stage.initStyle(StageStyle.TRANSPARENT); //allow image to be seen through
        splashScreen.setScene(createScene());
        splashScreen.setOpacity(.5); //slightly transparent
        splashScreen.show();
    }

    public Scene createScene() {

        //Creating an image object
        Image image = null;
        try {
            image = new Image("/goistreamtoolredux/images/splash-halfsize.png");
        } catch (NullPointerException | IllegalArgumentException exception) {
            exception.printStackTrace(); //should never happen unless app is corrupted
        }

        //Setting the image view
        ImageView imageView = new ImageView(image);

        //Setting the position of the image
        imageView.setX(50);
        imageView.setY(25);

        //setting the fit height and width of the image view
        imageView.setFitHeight(455);
        imageView.setFitWidth(500);

        //Setting the preserve ratio of the image view
        imageView.setPreserveRatio(true);

        //Creating a Group object
        Group root = new Group(imageView);

        //Creating a scene object
        Scene scene = new Scene(root, 600, 500);

        scene.setFill(Color.TRANSPARENT);
        return scene;
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification notification) {
        if (notification instanceof ProgressNotification) {
            if (((ProgressNotification) notification).getProgress() == 1) {
                System.out.println("Closing Preloader");
                splashScreen.hide();
            }
        }
    }

}