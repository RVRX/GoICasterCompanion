package goistreamtoolredux.algorithm;

import goistreamtoolredux.App;
import goistreamtoolredux.controller.TimerPane;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;

public class CustomTimer extends Timers {

    private static CustomTimer singleton = new CustomTimer();
    private Timer currentTimer;
    protected boolean isTimerRunning = false;

    public Timer getCurrentTimer() {
        return currentTimer;
    }

    private CustomTimer() {}

    /* Static 'instance' method */
    public static CustomTimer getInstance() {
        return singleton;
    }

    //File paths
//    String timerLength = FileManager.inputPath + "TimerLength.txt";
//    String timerTXT = FileManager.outputPath + "Timer.txt";


    /*--- Methods ---*/

    /**
     * Start the timer (initial or after a pause)
     * @throws IOException
     * @throws NoSuchElementException
     * @see #stop()
     * @see #pause()
     * @see #restart()
     */
    public void start() throws IOException, NoSuchElementException, InvalidDataException {
        if (!isTimerRunning) { //prevent starting when there is already a timer. no doubling up!
            if (get() <= 0) { //if timer is currently at 0, restart
                restart();
            } else { //otherwise, resume from last position by starting goistreamtoolredux.algorithm.CountdownTimer task
                isTimerRunning = true;
                currentTimer = new Timer();
                TimerTask task = new CountdownTimerCustom();
                currentTimer.schedule(task,0, 1000);
            }
        } else System.err.println("Timer already running!");
    }

    /**
     * Completely stops and cancels the timer. NOT EQUIVALENT TO A PAUSE.
     * Starting after this state will restart the timer.
     *
     * @throws NullPointerException there is no current timer or {@link TimerTask} running
     * @throws IOException if I/O error is encountered when writing to Timer.txt
     * @see #start()
     * @see #pause()
     * @see #restart()
     */
    public void stop() throws NullPointerException, IOException {
        //stop the current TimerTask
        if (currentTimer != null) {
            currentTimer.cancel();
            isTimerRunning = false;
        }
        //set Timer.txt to nothing [will cause start() to restart timer if called next]
        set(0);
    }

    /**
     * Pauses the current timer state. Starting from this after this state will
     * continue the timer from where the timer left off.
     * Does nothing if there is no active timer
     *
     * @see #start()
     * @see #stop()
     * @see #restart()
     */
    public void pause() {
        //cancel TimerTask to stop countdown. Keeps value, so start() can resume from it
        if (currentTimer != null) {
            currentTimer.cancel();
            isTimerRunning = false;
        }
    }

    /**
     * Resets the timer back to beginning value and starts it again
     * @see #start()
     * @see #stop()
     * @see #pause()
     */
    public void restart() throws IOException, InvalidDataException {
        //stop timer
        if (currentTimer != null) {
            currentTimer.cancel();
            isTimerRunning = false;
        }

        //write initial timer length to Timer.txt
        set(getInitialTimerLength());

        //start
        start();
    }
}

/**
 * Begins countdown from current value in the <code>Timer.txt</code> file. {@link TimerTask} to be scheduled in {@link Timer#schedule(TimerTask, long, long)} for the countdown timer system.
 * @implNote Be careful with scheduling this function too fast/often, it doesn't implement any
 * file locks and is not atomic as of the current version.
 * @implNote Does not use system clock (or at least efficiently. Timer will start to lag behind if more system resources are used or application is active with other tasks
 */
class CountdownTimerCustom extends TimerTask {

    public void run() {

        /*todo, implement lock*/

        LobbyTimer timer = LobbyTimer.getInstance();

        try {
            int currentTime = timer.get();
            if (currentTime <= 0) {
                System.out.println("Timer Finished!\n>");
                LobbyTimer.getInstance().isTimerRunning = false;
                timer.getCurrentTimer().cancel();
            } else {
                timer.set(currentTime - 1);
                TimerPane timerPaneController = (TimerPane) App.getMasterController().getTimerPaneController();
                timerPaneController.setLobbyTimerText(String.valueOf(currentTime - 1));
            }
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (InvalidDataException exception) {
            exception.printStackTrace();
        }
    }
}

