package goistreamtoolredux.algorithm;

import goistreamtoolredux.App;
import goistreamtoolredux.controller.TimerPane;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;

/**
 * Singleton timer class.
 * Warning: gets confusing with all the uses of timer as a word, vs the actual JDK Timer class.
 */
public class AppTimer extends Timers {

    private static AppTimer singleton = new AppTimer();
    private Timer currentTimer;
    protected boolean isTimerRunning = false;
    private long startTime;
    private int currentTimerStartLength;

    public Timer getCurrentTimer() {
        return currentTimer;
    }
    public long getStartTime() {
        return startTime;
    }
    public int getCurrentTimerStartLength() {
        return currentTimerStartLength;
    }

    private AppTimer() {}

    /* Static 'instance' method */
    public static AppTimer getInstance() {
        return singleton;
    }


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

                //set start time/date
                startTime = System.currentTimeMillis();

                //set length this timer will use
                currentTimerStartLength = getInitialTimerLength();

                //set up and start TimerTask
                currentTimer = new Timer();
                TimerTask task = new CountdownTimer();
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
        try {
            set(getInitialTimerLength());
        } catch (NoSuchElementException exception) {
            App.showExceptionDialog(exception, "Error starting/restarting timer.", "Please go to the settings page and specify a lobby timer length, and save it (Cmd+S), before attempting to start the timer.");
            return;
        }

        //start
        start();
    }

}

/**
 * Begins countdown from current value in the <code>Timer.txt</code> file. {@link TimerTask} to be scheduled in {@link Timer#schedule(TimerTask, long, long)} for the countdown timer system.
 * @implNote Does not use system clock (or at least efficiently. Timer will start to lag behind if more system resources are used or application is active with other tasks
 */
class CountdownTimer extends TimerTask {

    public void run() {

        AppTimer timer = AppTimer.getInstance();

        try {
            int currentFileTime = timer.get();
            if (currentFileTime <= 0) { //timer has reached 0
                System.out.println("Timer Finished!\n>");
                AppTimer.getInstance().isTimerRunning = false;
                timer.getCurrentTimer().cancel();

                //set timer text to be the preferred text upon ending
                Writer fileWriter = new FileWriter(FileManager.outputPath + "Timer.txt");
                fileWriter.write(Preferences.userRoot().node("/goistreamtoolredux/algorithm").get("timer_end_text","0:00"));
                fileWriter.close();
            } else { //timer still has more to count

                //get new time for timer
                long elapsedSinceStartMilli = System.currentTimeMillis() - timer.getStartTime();
                // timerLength - timeSinceStart
                long foo = timer.getCurrentTimerStartLength() - (elapsedSinceStartMilli/1000);

                timer.set(Math.toIntExact(foo)); //todo catch ArithmeticException
                TimerPane timerPaneController = (TimerPane) App.getMasterController().getTimerPaneController();
                timerPaneController.setLobbyTimerText(String.valueOf(foo));
            }
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
