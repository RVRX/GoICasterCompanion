package goistreamtoolredux.algorithm;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.prefs.Preferences;

public class Timers {

//    private Timer currentTimer;
//    protected boolean isTimerRunning = false;

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
//        if (seconds <= 0) throw new IllegalArgumentException("Cannot set initial timer value to 0");
//        //open TimerLength file and set a new value
//        Writer fileWriter = new FileWriter(timerLength);
//        fileWriter.write(String.valueOf(seconds));
//        fileWriter.close();
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
     * @throws IOException
     * @throws NoSuchElementException TimerLength.txt contains an unexpected character.
     * @see #setInitialTimerLength(int)
     */
    public int getInitialTimerLength() throws IOException, NoSuchElementException, InvalidDataException {
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
     * Gets the <b>current</b> value of the timer.
     * If the timer file is empty or invalid, the file will be fixed,
     * and the default length (TimerLength.txt will be returned)
     *
     * @// TODO: 7/15/21 remove TimerLength.txt logic! Depreciated and unexpected
     *
     * @return time left in seconds.
     * @throws FileNotFoundException timer.txt cannot be found
     * @throws InvalidDataException incorrect values in TimerLength.txt
     */
    public int get() throws IOException, InvalidDataException {
        //open Timer.txt and scan first integer
        File timerSettings = new File(timerTXT);
        Scanner scanner = new Scanner(timerSettings);
        try {
            int foo = convertFromMinuteFormat(scanner.nextLine());
            return foo;
        } catch (NumberFormatException exception) {
            //occurs when timer.txt does not contain int - this can be because of user input
            // but more commonly due to user setting their own end text in the file.
            // in this case, we just return the preferred length.
            return getInitialTimerLength();

        } catch (NoSuchElementException e) {
            //timer file is empty or invalid.
            //write value of "TimerLength.txt" to "Timer.txt", and return such
            Writer fileWriter = new FileWriter(timerTXT);
            try {
                fileWriter.write(convertToMinuteFormat(getInitialTimerLength()));
            } catch (NoSuchElementException | InvalidDataException exception) {
                //getInitial error, so write new value to getInitial
                Writer fileWriterInitial = new FileWriter(FileManager.inputPath + "TimerLength.txt");
                fileWriterInitial.write(240);
                fileWriterInitial.close();
            }
            fileWriter.close();
        }
        return 240;
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
