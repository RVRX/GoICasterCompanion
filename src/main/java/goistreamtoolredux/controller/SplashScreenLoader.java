package goistreamtoolredux.controller;

import javafx.application.Preloader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SplashScreenLoader extends Preloader {

    private Stage splashScreen;

    @Override
    public void start(Stage stage) throws Exception {
        splashScreen = stage;
        splashScreen.initStyle(StageStyle.UNDECORATED);
        stage.initStyle(StageStyle.TRANSPARENT);
        splashScreen.setScene(createScene());
        splashScreen.show();
    }

    public Scene createScene() {
//        StackPane root = new StackPane();
        File mapFile = new File("/goistreamtoolredux/splash-halfsize.png");
//            mapImage.setImage(new Image(mapFile.toURI().toString()));
//        Image image = new Image(mapFile.toURI().toString());
//        ImageView imageView = new ImageView(image);


        //Creating an image
        Image image = null;
        try {
            // TODO: 6/29/21 MODIFY PATH TO NOT BE ABSOLUTE
            image = new Image(new FileInputStream(new File("/Users/rvrx/IdeaProjects/GoIStreamToolRedux/src/main/resources/goistreamtoolredux/splash-halfsize.png")));
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
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
        if (notification instanceof StateChangeNotification) {
            splashScreen.hide();
        }
    }

}