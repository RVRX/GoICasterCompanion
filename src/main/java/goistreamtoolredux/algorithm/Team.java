package goistreamtoolredux.algorithm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.stream.Stream;

public class Team {
    String teamName;
    String abbreviatedName;
    Path teamLogo;

    public Team(String teamName, String abbreviatedName, Path teamLogo) {
        this.teamName = teamName;
        this.abbreviatedName = abbreviatedName;
        this.teamLogo = teamLogo;
    }

    public Team(String teamName, String abbreviatedName) {
        this.teamName = teamName;
        this.abbreviatedName = abbreviatedName;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getAbbreviatedName() {
        return abbreviatedName;
    }

    public Path getTeamLogo() {
        return teamLogo;
    }

    /**
     * Loads all teams in from the <code>input/teams.txt</code> file
     * @return list of {@link Team}
     * @throws IOException
     */
    public static LinkedList<Team> getAllTeamsFromDisk() throws IOException {
        //list for return
        LinkedList<Team> teamObservableList = new LinkedList<>();

        //get all files in team_logos folder
        LinkedList<Path> allTeamImages = new LinkedList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(FileManager.inputPath + "team_logos"))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(allTeamImages::add);
        }

        //open input/teams.txt
        File teamsTXT = new File(FileManager.inputPath + "teams.txt");
        Scanner scanner = new Scanner(teamsTXT); //FileNotFoundException

        //for each line of teams.txt
        while (scanner.hasNext()) {
            String line = scanner.nextLine();

            //get team name and abbreviation
            int indexOfSplit = line.indexOf("|"); //will return -1 if blank line or missing "|"
            String shortName = line.substring(indexOfSplit + 1).toUpperCase();
            String fullName = line.substring(0, indexOfSplit);

            //find teamLogo using team name
            Path teamLogo = FileManager.findFile(allTeamImages, fullName);

            //add team to return list, constructor choice depends on if a logo was found
            if (teamLogo == null) {
                teamObservableList.add(new Team(fullName, shortName));
            } else teamObservableList.add(new Team(fullName, shortName, teamLogo));
        }

        return teamObservableList;
    }

}
