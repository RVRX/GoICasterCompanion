import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Singleton timer class.
 * Warning: gets confusing with all the uses of timer as a word, vs the actual JDK Timer class.
 */
public class CustomTimer {

    private static CustomTimer singleton = new CustomTimer();
    private Timer currentTimer;

    public Timer getCurrentTimer() {
        return currentTimer;
    }

    private CustomTimer() {}

    /* Static 'instance' method */
    public static CustomTimer getInstance() {
        return singleton;
    }

    //File paths
    String timerLength = Main.inputPath + "TimerLength.txt";
    String timerTXT = Main.outputPath + "Timer.txt";


    /*--- Methods ---*/

    /**
     * Sets the value the timer will go to on initial start, or after a restart or stop
     * @see #getInitialTimerLength()
     */
    public void setInitialTimerLength(int seconds) throws IOException {
        //open TimerLength file and set a new value
        Writer fileWriter = new FileWriter(timerLength);
        fileWriter.write(String.valueOf(seconds));
        fileWriter.close();
    }

    /**
     * Resets the timer back to beginning value and starts it again
     * @see #start()
     * @see #stop()
     * @see #pause()
     */
    public void restart() throws IOException {
        //stop timer
        if (currentTimer != null) {
            currentTimer.cancel();
        }

        //write initial timer length to Timer.txt
        set(getInitialTimerLength());

        //start
        start();
    }

    /**
     * Gets the total length of the timer. Not to be confused with the current length,
     * this value is where the timer will go to when restarting or starting after a stop.
     *
     * @return timer length in seconds
     * @throws IOException
     * @throws NoSuchElementException TimerLength.txt contains an unexpected character.
     * @see #setInitialTimerLength(int)
     */
    public int getInitialTimerLength() throws IOException, NoSuchElementException {
        //open TimerLength file and gets the current value
        File timerSettings = new File(timerLength);
        Scanner scanner = new Scanner(timerSettings);
        System.out.println("SCANNER FUCK U: " + new Scanner(timerSettings).nextLine());
        return scanner.nextInt();
    }

    /**
     * Start the timer (initial or after a pause)
     * @throws IOException
     * @throws NoSuchElementException
     * @see #stop()
     * @see #pause()
     * @see #restart()
     */
    public void start() throws IOException, NoSuchElementException {
        if (get() <= 0) { //if timer is currently at 0, restart
            restart();
        } else { //otherwise, resume from last position by starting CountdownTimer task
            currentTimer = new Timer();
            TimerTask task = new CountdownTimer();
            currentTimer.schedule(task,0, 1000);
        }
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
        }
    }

    /**
     * Set the <b>current</b> value of the timer
     * @throws IOException if any I/O error occurs when accessing Timer.txt
     */
    public void set(int seconds) throws IOException {
        Writer fileWriter = new FileWriter(timerTXT);
        fileWriter.write(String.valueOf(seconds));
        fileWriter.close();
    }

    /**
     * Gets the <b>current</b> value of the timer.
     * @return time left in seconds.
     * @throws FileNotFoundException timer.txt cannot be found
     * @throws NoSuchElementException timer.txt contains an unexpected character
     */
    public int get() throws FileNotFoundException, NoSuchElementException {
        //open Timer.txt and scan first integer
        File timerSettings = new File(timerTXT);
        Scanner scanner = new Scanner(timerSettings);
        return scanner.nextInt();
    }
}

/**
 * Begins countdown from current value in the <code>Timer.txt</code> file. {@link TimerTask} to be scheduled in {@link Timer#schedule(TimerTask, long, long)} for the countdown timer system.
 * @implNote Be careful with scheduling this function too fast/often, it doesn't implement any
 * file locks and is not atomic as of the current version.
 * @implNote Does not use system clock (or at least efficiently. Timer will start to lag behind if more system resources are used or application is active with other tasks
 */
class CountdownTimer extends TimerTask {

    public void run() {

        /*todo, implement lock*/

        CustomTimer timer = CustomTimer.getInstance();

        try {
            int currentTime = timer.get();
            if (currentTime <= 0) {
                System.out.println("Timer Finished!\n>");
                timer.getCurrentTimer().cancel();
            } else {
                timer.set(currentTime - 1);
            }
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
