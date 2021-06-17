import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {

    //System independent path to output folder
    public static String outputPath = System.getProperty("user.dir") + File.separator + "output" + File.separator;
    //System independent path to input folder
    public static String inputPath = System.getProperty("user.dir") + File.separator + "input" + File.separator;

    public static void main(String[] args) {
        System.out.println("Welcome to GoIStreamToolRedux CLI!\n\nAwaiting User input...");
        boolean live = true;
        Scanner scanner = new Scanner(System.in);
        while (live) {
            System.out.print("> ");
            String input = scanner.nextLine();
            CLIParse(input);
        }
    }

    private static void CLIParse(String input) {
        Scanner scanner = new Scanner(System.in); //stdin

        String parsedInput = "";
        if (input.contains(" ")) {
            parsedInput = input.substring(0,input.indexOf(" "));
        } else {
            parsedInput = input;
        }

        switch (parsedInput.toLowerCase()) {
            case "quit": case "exit": case "q":
                System.out.println("exiting...");
                System.exit(0);
                break;
            case "help":
                System.out.println("'setTeam' or 'st' to set a new team");
                System.out.println("'setMap' or 'sm' to set current map");
                System.out.println("'verify' to verify existence of required folders");
                System.out.println("'settimer' to set timer length"); //todo implement setTimer()
                System.out.println("'starttimer' to start timer"); //todo implement startTimer()
                System.out.println("'setnumber' to set tournament number");
                break;
            case "setteam": case "st":
                System.out.print("Enter team's full name: ");
                String teamName = scanner.nextLine();
                System.out.print("\nEnter team's letter: ");
                String teamLetter = scanner.nextLine();
                try {
                    if (setTeam(teamName,teamLetter)) {
                        System.out.println("Updated!");
                    } else {
                        System.err.println("Failed to Update Team Completely. Risk of Incomplete update!");
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                } catch (IllegalArgumentException exception) {
                    System.err.println(exception.getMessage());
                }
                break;
            case "setmap": case "sm":
                System.out.print("Enter full map name: ");
                String mapName = scanner.nextLine();
                try {
                    if (setMap(mapName)) {
                        System.out.println("Current Map Updated!");
                    } else {
                        System.err.println("Failed to Update Current Map");
                    }
                } catch (NoSuchFileException exception) {
                    System.err.println("NoSuchFileException! Missing map png? Details: " + exception.getMessage());
                } catch (IOException exception) {
                    System.err.println("I/O Error! reason: " + exception.getMessage());
                }
                break;
            case "setnumber":
                System.out.print("Enter tournament number: ");
                String tourneyNumber = scanner.nextLine();
                    if (setTourneyNumber(tourneyNumber)) {
                        System.out.println("Current tournament number updated!");
                    } else {
                        System.err.println("Failed to update current tournament number.");
                    }
                break;
            case "verify": case "v":
                if (verifyFolders()) {
                    System.out.println("All folders verified!");
                } else System.err.println("Folder recreation failed!");
                break;
            default:
                System.out.println("Unknown Input. Type 'help' for commands");
        }
    }

    /**
     * Sets the current tournament number.
     *
     * @param number new number representing current tournament
     * @return false if IO operation failed
     */
    private static boolean setTourneyNumber(String number) {
        //write 'number' to TournamentNumber.txt
        try {
            Writer fileWriter = new FileWriter(outputPath + "TournamentNumber.txt");
            fileWriter.write(number);
            fileWriter.close();
            System.out.println("TournamentNumber.txt Updated");
        } catch (IOException e) {
            System.err.println("Couldn't write to TournamentNumber.txt, reason: " + e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Checks for <code>input</code> and <code>output</code> folders, creates them if necessary.
     *
     * @return <code>true</code> if folders found or recreation successful; <code>false</code> otherwise.
     */
    private static boolean verifyFolders() {
        System.out.println("verifying folders");
        boolean status = true;

        //check input folder
        Path inputPathObject = Paths.get(inputPath);
        if (!Files.exists(inputPathObject)) {
            System.out.println("Input folder cannot be found... recreating");
            if (inputPathObject.toFile().mkdirs()) {
                System.out.println("Input folder created successfully");
            } else {
                System.err.println("Error when creating input directory!");
                status = false;
            }
        } else System.out.println("Input folder found");

        //check output folder
        Path outputPathObject = Paths.get(outputPath);
        if (!Files.exists(outputPathObject)) {
            System.out.println("Output folder cannot be found... recreating");
            if (outputPathObject.toFile().mkdirs()) {
                System.out.println("Output folder created successfully");
            } else {
                System.err.println("Error when creating output directory!");
                status = false;
            }
        } else System.out.println("Output folder found");

        return status;
    }

    /**
     * Checks if a map name is a valid map found in <code>maps.txt</code>.
     *
     * @param map Full name of a map to check
     * @return
     */
    private static boolean isValidMap(String map) throws FileNotFoundException {
        //open Maps.txt
        File teamsTXT = new File(inputPath + "maps.txt");
        Scanner scanner = new Scanner(teamsTXT);
        //parse through File for 'map'
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase(map)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Changes the current map.
     * DOES NOT CHECK FOR VALIDITY OF INPUT, HOWEVER INPUT MUST BE VALID
     *
     * {@// TODO: 6/17/21 error handling, check up on file writes! }
     *
     * @param mapName
     * @return
     * @throws IOException
     */
    private static boolean setMap(String mapName) throws IOException {

        /*--- Check Map Validity ---*/
        try {
            if (!isValidMap(mapName)) {
                System.err.println("That map does not exist.");
                return false;
            }
        } catch (FileNotFoundException e) {
            System.err.println("Map file could not be found, reason:" + e.getMessage());
            return false;
        }

        /*--- Update Map.png ---*/
        //copy map from map_images
        Files.copy(Paths.get(inputPath + "map_images" + File.separator + mapName + ".png"), Paths.get(outputPath + "Map.png"), StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Map.png Updated");

        /*--- Update Map.txt ---*/
        Writer fileWriter = new FileWriter(outputPath + "Map.txt");
        fileWriter.write(mapName);
        fileWriter.close();
        System.out.println("Map.txt Updated");

        return true;
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
     * @param newTeamName Name of the team. Must occur in teams.txt
     * @param teamIdentifier Value to identify team. Must be a valid character for filesystem
     * @return success or fail
     * @throws IOException error writing, reading, or converting files.
     * @throws IllegalArgumentException Thrown if fcn args contain invalid characters or do not correspond to a team in teams.txt
     */
    static boolean setTeam(String newTeamName, String teamIdentifier) throws IOException, IllegalArgumentException {

        //throw exception if teamIdentifier contains filesystem illegal characters
        final String[] ILLEGAL_CHARACTERS = { "/", "\n", "\r", "\t", "\0", "\f", "`", "?", "*", "\\", "<", ">", "|", "\"", ":", ".", ".." };
        if (Arrays.stream(ILLEGAL_CHARACTERS).anyMatch(teamIdentifier::contains)) {
            throw new IllegalArgumentException("Team identifier contains illegal characters");
        }
        //throw exception if team name has the separator character "|" used by teams.txt
        if (newTeamName.contains("|")) {
            throw new IllegalArgumentException("Team name contains illegal character, '|'");
        }

        teamIdentifier = teamIdentifier.toUpperCase();
        System.out.println("setTeam("+newTeamName+","+teamIdentifier+")");

        /*--- Update TXTs ---*/

        //get file names
        String teamTXT = "Team" + teamIdentifier + ".txt";
        String teamShortTXT = "TeamShort" + teamIdentifier + ".txt";

        //update output file "TeamShortX.txt"
        Writer fileWriter2 = new FileWriter(outputPath + teamShortTXT); //TeamShortX.txt
        String shortname = getShortName(newTeamName);
        if (shortname == null) {
            throw new IllegalArgumentException("Team '" + newTeamName + "' does not exist in teams.txt");
        }
        fileWriter2.write(shortname); //write shortName to file
        fileWriter2.close();
        System.out.println(teamShortTXT + " updated.");

        //update output file "TeamX.txt"
        Writer fileWriter = new FileWriter(outputPath + teamTXT); //TeamX.txt
        fileWriter.write(newTeamName);
        fileWriter.close();
        System.out.println(teamTXT + " updated.");



        /*--- Update Image ---*/

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
            //remove current output team logo to prevent mismatch
            // better to have right text and no logo, than right text and wrong logo.
            File outputTeamLogo = new File(outputPath + "Team" + teamIdentifier + ".png");
            outputTeamLogo.delete();
            return false;
        }
        System.out.println("Found Team logo: " + teamLogo);

        //If extension is .png move it, else convert then move.
        String logoExt = teamLogo.getFileName().toString().substring(teamLogo.getFileName().toString().indexOf(".") + 1);
        if (logoExt.equalsIgnoreCase("png")) {

            //copy from teamLogo Path to TeamX.png
            Files.copy(teamLogo,Paths.get(outputPath + "Team" + teamIdentifier + ".png"), StandardCopyOption.REPLACE_EXISTING);
        } else { //logo not PNG
            try {
                //convert to png
                boolean wasConverted = convertFormat(teamLogo.toString(),inputPath + "team_logos" + File.separator + newTeamName + ".png","PNG");
                if (wasConverted) {
                    System.out.println("Image converted to PNG");

                    //delete old photo
                    if(teamLogo.toFile().delete()) {
                        System.out.println("File deleted successfully");
                    }
                    else {
                        System.err.println("Failed to delete the file");
                        return false;
                    }

                    //copy from teamLogo Path to TeamX.png
                    Files.copy(Paths.get(inputPath + "team_logos" + File.separator + newTeamName + ".png"),Paths.get(outputPath + "Team" + teamIdentifier + ".png"), StandardCopyOption.REPLACE_EXISTING);

                } else { //todo, remove current output team logo if new logo cannot be converted
                    System.err.println("Image could not be converted to PNG");
                    //remove current output team logo to prevent mismatch
                    // better to have right text and no logo, than right text and wrong logo.
                    File outputTeamLogo = new File(outputPath + "Team" + teamIdentifier + ".png");
                    outputTeamLogo.delete();
                }
            } catch (IOException exception) {
                System.err.println("Error during converting image.");
                exception.printStackTrace();
                return false;
            }
        }

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
        //todo open input/teams.txt

        //todo parse through File for shortName

        //todo get and return corresponding longName

        return null;
    }

    /**
     * Parses `teams.txt` to get a team's longName from its shortName.
     * Case insensitive search. Uppercase return.
     * Can be used to verify the existence of a team by their full team name.
     *
     * @param longName longName to find corresponding shortName of
     * @return Shortname, or null if no shortname can be found
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

    /**
     * Converts an image to another format
     * @author <a href="https://www.codejava.net/java-se/graphics/convert-image-formats">Nam Ha Minh</a>
     *
     * @param inputImagePath Path of the source image
     * @param outputImagePath Path of the destination image
     * @param formatName the format to be converted to, one of: jpeg, png,
     * bmp, wbmp, and gif
     * @return true if successful, false otherwise
     * @throws IOException if errors occur during writing
     */
    public static boolean convertFormat(String inputImagePath,
                                        String outputImagePath, String formatName) throws IOException {
        FileInputStream inputStream = new FileInputStream(inputImagePath);
        FileOutputStream outputStream = new FileOutputStream(outputImagePath);

        // reads input image from file
        BufferedImage inputImage = ImageIO.read(inputStream);

        // writes to the output image in specified format
        boolean result = ImageIO.write(inputImage, formatName, outputStream);

        // needs to close the streams
        outputStream.close();
        inputStream.close();


        return result;
    }
}