package goistreamtoolredux;

import com.sun.javafx.application.LauncherImpl;
import goistreamtoolredux.controller.SplashScreenLoader;

public class Main {

    public static void main(String[] args) {
        LauncherImpl.launchApplication(App.class, SplashScreenLoader.class, args);
    }
}
