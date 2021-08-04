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
    private long timeAtPause = -1; //Positive value indicates the time at paused, -1 indicates timer is not resuming from pause

    public Timer getCurrentTimer() {
        return currentTimer;
    }
    public long getStartTime() {
        return startTime;
    }
    public int getCurrentTimerStartLength() {
        return currentTimerStartLength;
    }
    public void setTimeAtPause(long timeAtPause) {
        this.timeAtPause = timeAtPause;
    }

    private AppTimer() {}

    /* Static 'instance' method */
    public static AppTimer getInstance() {
        return singleton;
    }


    /**
     * Starts the timer (initial or after a pause), is one is not already running (i.e., {@link #isTimerRunning} is false).
     * <p>
     * Sets the value of {@link #startTime} to be either:
     * <ul>
     *     <li>If resuming from a pause – time at initial start, plus difference between resume and initial pause
     *     <li>If initial start – Current system time.
     * </ul>
     *
     * <p>Sets the value of {@link #isTimerRunning} to true before starting the
     *
     * @throws IOException
     * @throws NoSuchElementException
     *
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
                if (timeAtPause < 0) { //new timer, looking to be created
                    startTime = System.currentTimeMillis();

                    //set length this timer will use
                    currentTimerStartLength = getInitialTimerLength();
                } else { //paused timer, looking to resume
                    //if timer was paused, use a time calculated off of the saved state.
                    //New start time will be old start time plus the time elapsed between resume and pause,
                    //  thereby pushing forward the start time by however long the timer was paused for.
                    //T_s = T_s + (T_resume - T_pause)
                    startTime = startTime + (System.currentTimeMillis() - timeAtPause);

                    //no need to update currentTimerStartLength, as we want to stick with the one that was used before
                    // pause, so as to not update the length partway through the timer (not good).
                }



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
        //remove saved state
        timeAtPause = -1;
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
            //save state
            timeAtPause = System.currentTimeMillis();
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
        stop();

        //write initial timer length to Timer.txt
        try {
            set(getInitialTimerLength());
        } catch (NoSuchElementException exception) {
            App.showExceptionDialog(exception, "Error starting/restarting timer.", "Please delete your 'Timer.txt' and restart the application. Submit a bug report if the problem persists.");
            return;
        }

        //start
        start();
    }

}

/**
 * Begins countdown from current value in the <code>Timer.txt</code> file.
 * When timer reaches zero, sets the <code>Timer.txt</code> file to value of {@link Preferences} key <code>"timer_end_text"</code>.
 *
 * <p>Counts down by comparing value of {@link AppTimer#getStartTime()} with {@link System#currentTimeMillis()}, and converting to int via {@link Math#toIntExact(long)}
 *
 * @implNote This class is a {@link TimerTask} to be scheduled with {@link Timer#schedule(TimerTask, long, long)}.
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

                //remove saved state
                AppTimer.getInstance().setTimeAtPause(-1);
            } else { //timer still has more to count

                //get new time for timer
                long elapsedSinceStartMilli = System.currentTimeMillis() - timer.getStartTime();
                // timerLength - timeSinceStart
                long foo = timer.getCurrentTimerStartLength() - (elapsedSinceStartMilli/1000);

                timer.set(Math.toIntExact(foo)); //todo catch ArithmeticException
                TimerPane timerPaneController = (TimerPane) App.getMasterController().getTimerPaneController();
                if (timerPaneController != null) {
                    timerPaneController.setLobbyTimerText(String.valueOf(foo));
                }
            }
        } catch (FileNotFoundException exception) {
            System.err.println("Error encountered in timer task (FileNotFoundException). Reason: '" + exception.getMessage() + "'\nCancelling Timer...");
            exception.printStackTrace();
            AppTimer.getInstance().isTimerRunning = false;
            AppTimer.getInstance().setTimeAtPause(-1);
            timer.getCurrentTimer().cancel();
        } catch (IOException exception) {
            System.err.println("Error encountered in timer task (IOException). Reason: '" + exception.getMessage() + "'\nCancelling Timer...");
            exception.printStackTrace();
            AppTimer.getInstance().isTimerRunning = false;
            AppTimer.getInstance().setTimeAtPause(-1);
            timer.getCurrentTimer().cancel();
        } catch (IllegalArgumentException exception) {
            //possibly due to timer being set for negative seconds
            // timer should be shut off
            System.err.println("Error encountered in timer task (IllegalArgumentException). Reason: '" + exception.getMessage() + "'\nCancelling Timer...");
            exception.printStackTrace();
            AppTimer.getInstance().isTimerRunning = false;
            AppTimer.getInstance().setTimeAtPause(-1);
            timer.getCurrentTimer().cancel();
        }
    }
}
