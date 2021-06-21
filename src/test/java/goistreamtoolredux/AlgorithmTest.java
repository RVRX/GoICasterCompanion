package goistreamtoolredux;

import goistreamtoolredux.algorithm.CustomTimer;
import goistreamtoolredux.algorithm.InvalidDataException;
import goistreamtoolredux.algorithm.Team;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Tests for Algorithm (Non-UI tests)
 */
public class AlgorithmTest {

    @Test
    public void settingAndGettingInitialTimerTest() throws IOException, InvalidDataException {
        CustomTimer customTimer = CustomTimer.getInstance();

        //10
        customTimer.setInitialTimerLength(10);
        assertEquals(10, customTimer.getInitialTimerLength());


        //120
        customTimer.setInitialTimerLength(120);
        assertEquals(120, customTimer.getInitialTimerLength());

        Exception exception = assertThrows(NumberFormatException.class, () -> {
            Integer.parseInt("1a");
        });
    }

    /**
     * setting initial value to 0 is not allowed. Would cause a loop of
     * <pre>start() -> restart() -> start()</pre>
     * due to start calling restart if timer is 0, and restart starting
     * from the initial value (which would be 0), then calling start.
     */
    @Test
    public void exceptionSetInitialTimerInvalidTest() {
        //setting initial value to 0 is not allowed. Would cause a "start() -> restart() -> start()" loop
        // due to start calling restart if timer is 0, and restart starting from the initial value (which would be 0)
        // then calling start
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
           CustomTimer.getInstance().setInitialTimerLength(0);
        });
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

}
