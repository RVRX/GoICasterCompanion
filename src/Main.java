import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    //System independent path to output folder
    public static String outputPath = System.getProperty("user.dir") + File.separator + "output" + File.separator;
    //System independent path to input folder
    public static String inputPath = System.getProperty("user.dir") + File.separator + "input" + File.separator;

    public static void main(String[] args) {
        System.out.println("Welcome to GoIStreamToolRedux CLI");

        try {
            setTeam("The Skyborne", "A");
        } catch (IOException e) {
            System.out.println("Failure in setTeam()!");
            e.printStackTrace();
        }


    }

    /**
     * Sets the current team.
     * E.g., used when setting "The Skyborne" as team A.
     *
     * Updates the output teamA/B image with the new team's image.
     *  File is in the format `"Team" + teamIdentifier + ".png"`
     * Updates the team & teamShort txt files to the new team's name.
     *  File name formatted as `"Team" + teamIdentifier + ".txt"`,
     *  and `"TeamShort" + teamIdentifier + ".txt"`
     *
     * @param newTeamName Name of the team. MUST BE A VALID TEAM!
     * @param teamIdentifier Either "A" or "B".
     * @return success or fail
     * @throws IOException
     */
    static boolean setTeam(String newTeamName, String teamIdentifier) throws IOException {

        /*--- Update TXTs ---*/

        //get file names
        String teamTXT = "Team" + teamIdentifier + ".txt";
        String teamShortTXT = "TeamShort" + teamIdentifier + ".txt";

        //update output file "TeamX.txt"
        Writer fileWriter = new FileWriter(outputPath + teamTXT); //TeamX.txt
        fileWriter.write(newTeamName);
        fileWriter.close();
        System.out.println(teamTXT + " updated.");

        //update output file "TeamShortX.txt"
        Writer fileWriter2 = new FileWriter(outputPath + teamShortTXT); //TeamShortX.txt
        String shortname = getShortName(newTeamName);
        if (shortname != null) {
            fileWriter2.write(shortname); //write shortName to file
        } else {
            System.err.println("Shortname Could not be found!");
            return false;
        }
        fileWriter2.close();
        System.out.println(teamShortTXT + " updated.");


        /*--- Update Image ---*/
        /*todo Update the output teamA/B image with the new team's image.
         *  File is in the format `"Team" + teamIdentifier + ".png"`*/
        //get all files in team_logos folder
        LinkedList<Path> allTeamImages = new LinkedList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(inputPath + "team_logos"))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(allTeamImages::add);
        }
        //find specific team's logo from list
        Path teamLogo = findFile(allTeamImages, newTeamName);
        if (teamLogo == null) {
            System.err.println("team logo not found!");
            return false;
        }
        System.out.println("Found Team logo: " + teamLogo);

        //successful finish
        return true;
    }

    /**
     * Takes a list of {@link Path}s and finds a certain file by name, excluding extension
     * @param allTeamImages
     * @return Path of found file.
     */
    private static Path findFile(LinkedList<Path> allTeamImages, String searchFor) {
        for (Path aPath : allTeamImages) {
            if (aPath.getFileName().toString().substring(0,aPath.getFileName().toString().indexOf(".")).equalsIgnoreCase(searchFor)) {
                return aPath;
            }
        }
        return null;
    }


    /**
     * Parses `teams.txt` to get a team's longName from its shortName
     */
    String getLongName(String shortName) {
        //open input/teams.txt
        /*todo*/
        //parse through File for shortName
        /*todo*/
        //get and return corresponding longName
        /*todo*/
        return null;
    }

    /**
     * Parses `teams.txt` to get a team's longName from its shortName.
     * Case insensitive search. Uppercase return.
     *
     * @param longName longName to find corresponding shortName of
     * @return Returns null or shortName.
     * @throws FileNotFoundException
     */
    public static String getShortName(String longName) throws FileNotFoundException {
        //open input/teams.txt
        File teamsTXT = new File(inputPath + "teams.txt");
        Scanner scanner = new Scanner(teamsTXT);
        //parse through File for longName
        while (scanner.hasNext()) {
            String line = scanner.nextLine().toLowerCase();
            if (line.contains(longName.toLowerCase())) {
                //long name line has been found, get shortname.
                return line.substring(line.indexOf("|") + 1).toUpperCase();
            }
        }
        return null;
    }
}