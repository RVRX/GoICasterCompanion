package goistreamtoolredux.algorithm;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.prefs.Preferences;

public class Timers {


    private static Preferences prefs = Preferences.userRoot().node("/goistreamtoolredux/algorithm");
    private static final String TIMER_ONE_LENGTH = "timer_one_length";
    private static final String TIMER_TWO_LENGTH = "timer_two_length";
    private static final String IS_TIMER_ONE = "is_timer_one";

    //File paths
    String timerLength = FileManager.inputPath + "TimerLength.txt";
    String timerTXT = FileManager.outputPath + "Timer.txt";

    /*--- Methods ---*/

    /**
     * Sets the value the timer will go to on initial start, or after a restart or stop
     *
     * @param seconds a positive integer
     * @see #getInitialTimerLength()
     */
    public void setInitialTimerLength(int seconds) throws IOException, IllegalArgumentException {
        prefs.putInt(TIMER_TWO_LENGTH, seconds);
    }

    public void setInitialTimerTwoLength(int seconds) {
        prefs.putInt(TIMER_TWO_LENGTH, seconds);
    }


    /**
     * Gets the total length of the timer. Not to be confused with the current length,
     * this value is where the timer will go to when restarting or starting after a stop.
     *
     * Gotten from the preferences API, defaults to 240 if no preference set yet.
     *
     * @return timer length in seconds
     * @see #setInitialTimerLength(int)
     */
    public int getInitialTimerLength() {
        if (prefs.getBoolean(IS_TIMER_ONE, true)) {
            //if timer one is selected, or must be defaulted to
            return prefs.getInt(TIMER_ONE_LENGTH, 240);
        } else {
            //if timer two is selected
            return prefs.getInt(TIMER_TWO_LENGTH, 240);
        }
    }


    /**
     * Set the <b>current</b> value of the timer
     *
     * @param seconds a second value (non-negative)
     * @throws IOException if any I/O error occurs when accessing Timer.txt
     */
    public void set(int seconds) throws IOException {
        if (seconds < 0) {
            throw new IllegalArgumentException("Cannot set a timer for negative seconds!");
        }
        Writer fileWriter = new FileWriter(timerTXT);
        fileWriter.write(convertToMinuteFormat(seconds));
        fileWriter.close();
    }

    /**
     * Gets the <b>current</b> value of <code>timer.txt</code>.
     * If the timer file is empty or invalid, the default timer will be retrieved
     *
     * @return time left in seconds.
     * @throws FileNotFoundException timer.txt cannot be found
     */
    public int get() throws IOException {
        //open Timer.txt and scan first integer
        File timerSettings = new File(timerTXT);
        Scanner scanner = new Scanner(timerSettings);
        try {
            return convertFromMinuteFormat(scanner.nextLine());
        } catch (NumberFormatException | NoSuchElementException exception) {
            /*
            NumberFormatException:
             occurs when timer.txt does not contain int - this can be because of user input
             but more commonly due to user setting their own end text in the file.
             in this case, we just return the preferred length.
            NoSuchElementException:
             timer file is empty (no line found)
             return preferred timer start value
             */

            return getInitialTimerLength();

        }
    }

    /**
     * Converts from seconds to <code>MM:SS</code> format
     * @param totalSecs
     * @return <code>M:SS</code> if <code>totalSecs &lt; 60</code>, else <code>MM:SS</code>
     */
    public static String convertToMinuteFormat(int totalSecs) {

        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;

        String timeString;

        if (minutes < 10) {
            timeString = String.format("%01d:%02d", minutes, seconds);
        } else {
            timeString = String.format("%02d:%02d", minutes, seconds);
        }

        return timeString;
    }

    /**
     * Converts from <code>MM:SS</code> or <code>M:SS</code> to seconds
     * @param value
     * @return
     */
    public static int convertFromMinuteFormat(String value) {

        int minutesSection;
        int seconds;

        if (value.length() == 5) { // MM:SS
            minutesSection = Integer.parseInt(value.substring(0,2)) * 60;
            seconds = Integer.parseInt(value.substring(3));
        } else { // M:SS
            minutesSection = Integer.parseInt(value.substring(0,1)) * 60;
            seconds = Integer.parseInt(value.substring(2));
        }

        return minutesSection + seconds;
    }
}
