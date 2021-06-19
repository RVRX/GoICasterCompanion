import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Singleton timer class.
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


    /*--- Methods ---*/

    /**
     * Write timer total to (input?) file
     */
    public void setTimerLength(int seconds) throws IOException {
        Writer fileWriter = new FileWriter(Main.inputPath + "timerLength.txt");
        fileWriter.write(seconds);
        fileWriter.close();
    }

    public void reset() {

    }

    public int getTimerLength() throws IOException, NoSuchElementException {
        File timerSettings = new File(Main.inputPath + "timerLength.txt");
        Scanner scanner = new Scanner(timerSettings);
        return scanner.nextInt();
    }

    public void start() throws IOException, NoSuchElementException {
        int length = getTimerLength();
        currentTimer = new Timer();
        TimerTask task = new CustomTask();
        currentTimer.schedule(task,0, 1000);
    }

    /**
     *
     * @throws NullPointerException there is no current timer
     */
    public void stop() throws NullPointerException {
        /*stop timer update*/
        currentTimer.cancel();
        /*todo wipe timer.txt*/
    }

    public void pause() {
        /*stop timer*/
        currentTimer.cancel();
        /*todo, save state, have start() read saved state if it exists*/
    }

    /**
     * Set the <b>current</b> value of the timer
     */
    public void set(int seconds) throws IOException {
        Writer fileWriter = new FileWriter(Main.outputPath + "timer.txt");
        fileWriter.write(String.valueOf(seconds));
        fileWriter.close();
    }

    /**
     * Gets the <b>current</b> value of the timer.
     * @return
     * @throws FileNotFoundException
     * @throws NoSuchElementException
     */
    public int get() throws FileNotFoundException, NoSuchElementException {
        File timerSettings = new File(Main.outputPath + "timer.txt");
        Scanner scanner = new Scanner(timerSettings);
        return scanner.nextInt();
    }
}

class CustomTask extends TimerTask {

    public void run() {

        /*todo, implement lock*/

        CustomTimer timer = CustomTimer.getInstance();

        try {
            int currentTime = timer.get();
            if (currentTime <= 0) {
                System.out.println();
                timer.getCurrentTimer().cancel();
            } else {
                System.out.println("Timer value is " + (currentTime -1));
                timer.set(currentTime - 1);
            }
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
