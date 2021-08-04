package goistreamtoolredux.algorithm;

import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;
import goistreamtoolredux.controller.BindHotkeyPane;

import javax.swing.*;
import java.io.IOException;
import java.util.prefs.Preferences;

import static goistreamtoolredux.controller.BindHotkeyPane.IS_TIMER_ONE;

public class GlobalHotKeys {

    private static final Preferences prefs = Preferences.userRoot().node("/goistreamtoolredux/algorithm");


    /**
     * Clears all global hotkeys, then re-instates, pulling values from preferences.
     * This method should only be called once an application run. Calling it again,
     * with updated values will lead to errors
     */
    public static void updateHotKeys() {
        System.out.println("App.updateHotKeys");
        Provider provider = Provider.getCurrentProvider(false);
        System.out.println("Provider has been reset");

        //global hotkey init
        if (prefs.getBoolean(BindHotkeyPane.IS_HOTKEYS_ENABLED, false)) {
            System.out.println("init hotkeys...");

            //if switch hotkey is set
            String timerSwitchHotKeyString = prefs.get(BindHotkeyPane.HOTKEY_TIMER_SWITCH, null);
            if (timerSwitchHotKeyString != null && !timerSwitchHotKeyString.equals("")) {
                //add listener with that keybinding
                provider.register(KeyStroke.getKeyStroke(timerSwitchHotKeyString), getHotKeySwitchListener());
            }

            //if play/pause hotkey is set
            String timerPlayPauseHotKeyString = prefs.get(BindHotkeyPane.HOTKEY_PLAY_PAUSE, null);
            if (timerPlayPauseHotKeyString != null && !timerPlayPauseHotKeyString.equals("")) {
                //add listener with that keybinding
                provider.register(KeyStroke.getKeyStroke(timerPlayPauseHotKeyString), getHotKeyPlayPauseListener());
            }
        }
    }

    /**
     * Swaps to the other timer length, and restarts the timer
     * @return the listener function used for dealing with a timer switch request
     */
    public static HotKeyListener getHotKeySwitchListener() {
        return hotKey -> {
            System.out.println("Timer Swap Hotkey Pressed");
            //swap the current pref
            prefs.putBoolean(IS_TIMER_ONE, !prefs.getBoolean(IS_TIMER_ONE,false));
            //restart the timer
            try {
                AppTimer.getInstance().restart();
            } catch (IOException exception) {
                exception.printStackTrace();
            } catch (InvalidDataException exception) {
                exception.printStackTrace();
            }
        };
    }

    /**
     * @return the listener function used for dealing with a play/pause request
     */
    public static HotKeyListener getHotKeyPlayPauseListener() {
        return hotKey -> {
            System.out.println("Timer Play/Pause Hotkey Pressed");
            if (AppTimer.getInstance().isTimerRunning) {
                AppTimer.getInstance().pause();
            } else {
                //try to start timer
                try {
                    AppTimer.getInstance().start();
                } catch (IOException exception) {
                    //todo
                    exception.printStackTrace();
                } catch (InvalidDataException exception) {
                    //todo
                    exception.printStackTrace();
                }
            }
        };
    }
}
