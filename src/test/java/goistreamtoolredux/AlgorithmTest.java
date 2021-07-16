package goistreamtoolredux;

import goistreamtoolredux.algorithm.FileManager;
import goistreamtoolredux.algorithm.LobbyTimer;
import goistreamtoolredux.algorithm.Team;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Tests for Algorithm (Non-UI tests)
 */
public class AlgorithmTest {

    @BeforeAll
    static void verify() {
        try {
            FileManager.verifyContent();
        } catch (IOException exception) {
            exception.printStackTrace();
            System.err.println("Could not verify content for tests!");
        }
    }

    @Test
    public void readTeamsFromDiskTest() throws IOException {
        LinkedList<Team> teamLinkedList = Team.getAllTeamsFromDisk();
        ObservableList<Team> teams = FXCollections.observableArrayList();
        teams.addAll(teamLinkedList);
        for (Team aTeam :
                teams) {
            String logo = null;
            if (aTeam.getTeamLogo() != null) {
                logo = aTeam.getTeamLogo().toString();
            }
            System.out.println(aTeam.getTeamName() + " (" + aTeam.getAbbreviatedName() + "): " + logo);
        }
    }

    @Test
    public void convertToMinTest() {
        assertEquals("3:20", LobbyTimer.convertToMinuteFormat(200));
        assertEquals("0:59", LobbyTimer.convertToMinuteFormat(59));
        assertEquals("1:00", LobbyTimer.convertToMinuteFormat(60));
        assertEquals("34:12", LobbyTimer.convertToMinuteFormat(2052));
    }

    @Test
    public void convertFromMinTest() {
        assertEquals(60, LobbyTimer.convertFromMinuteFormat("01:00"));
        assertEquals(60, LobbyTimer.convertFromMinuteFormat("1:00"));
        assertEquals(200, LobbyTimer.convertFromMinuteFormat("03:20"));
        assertEquals(200, LobbyTimer.convertFromMinuteFormat("3:20"));
        assertEquals(0, LobbyTimer.convertFromMinuteFormat("00:00"));
        assertEquals(0, LobbyTimer.convertFromMinuteFormat("0:00"));
    }

    @Test
    public void setOutputPathPreference() {
        String foo; // 'outputTest' directory
        if (System.getProperty("os.name").toLowerCase().contains("mac os")) {
            foo = System.getProperty("user.home") + File.separator + "Library" + File.separator +
                    "Application Support" + File.separator + "GoIStreamToolRedux" + File.separator + "outputTest" + File.separator;
        } else {
            foo = System.getProperty("user.dir") + File.separator + "outputTest" + File.separator;
        }

        //completely wipe system preferences for this class.
        Preferences prefs = Preferences.userRoot().node("/goistreamtoolredux/algorithm");
        try {
            prefs.clear();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }

        //attempt to reset non-existent preference (was just wiped above)
        FileManager.resetOutputPath();

        //set new output path
        FileManager.setOutputPath(foo);

        //test that output path was set
        assertEquals(foo, FileManager.getOutputPath());

        //reset
        FileManager.resetOutputPath();

        //assert successful reset
        assertEquals(FileManager.getDefaultOutputPath(), FileManager.getOutputPath());
    }

    @Test
    public void setInputPathPreference() {
        String foo; // 'inputTest' directory
        if (System.getProperty("os.name").toLowerCase().contains("mac os")) {
            foo = System.getProperty("user.home") + File.separator + "Library" + File.separator +
                    "Application Support" + File.separator + "GoIStreamToolRedux" + File.separator + "inputTest" + File.separator;
        } else {
            foo = System.getProperty("user.dir") + File.separator + "inputTest" + File.separator;
        }

        //completely wipe system preferences for this class.
        Preferences prefs = Preferences.userRoot().node("/goistreamtoolredux/algorithm");
        try {
            prefs.clear();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }

        //attempt to reset non-existent preference (was just wiped above)
        FileManager.resetInputPath();

        //set new input path
        FileManager.setInputPath(foo);

        //test that input path was set
        assertEquals(foo, FileManager.getInputPath());

        //reset
        FileManager.resetInputPath();

        //assert successful reset
        assertEquals(FileManager.getDefaultInputPath(), FileManager.getInputPath());
    }

    @Test
    public void gettingTeamFromDisk() {
        try {
            FileManager.setTeam("Cult of Phobos","X");
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        assertEquals("Cult of Phobos", Team.getTeamFromDisk("X"));




        try {
            FileManager.setTeam("The Pomelos","X");
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        assertEquals("The Pomelos", Team.getTeamFromDisk("X"));

    }

}
