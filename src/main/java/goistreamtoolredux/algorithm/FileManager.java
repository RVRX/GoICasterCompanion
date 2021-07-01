package goistreamtoolredux.algorithm;

import goistreamtoolredux.controller.Master;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.*;
import java.util.*;
import java.util.prefs.Preferences;
import java.util.stream.Stream;

public class FileManager {

//    private Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
    //set static accessor for preferences
    private static Preferences prefs = Preferences.userRoot().node("/goistreamtoolredux/algorithm");
    //set static final fields for key names
    private static final String OUTPUT_FOLDER = "output_folder";
    private static final String INPUT_FOLDER = "input_folder";

    //System independent path to output folder
    public static String outputPath = getOutputPath();
    //System independent path to input folder
    public static String inputPath = getInputPath();

    public static void main(String[] args) {
        System.out.println("Welcome to GoIStreamToolRedux CLI!\ntype 'help' for more info. Type 'verify' if first start.\nAwaiting User input...");
        boolean live = true;
        Scanner scanner = new Scanner(System.in);
        while (live) {
            System.out.print("> ");
            String input = scanner.nextLine();
            CLIParse(input);
        }
    }

    /**
     * Gets the currently set application output folder path.
     *
     * @return user preferred output folder, default if no preference is set
     * @see #getInputPath()
     */
    public static String getOutputPath() {
        return prefs.get(OUTPUT_FOLDER, getDefaultOutputPath());
    }

    /**
     * Gets the default (OS specific) application output folder path.
     *
     * This should be the "Input" folder within "Documents" or documents equivalent on the user's OS.
     * On non-MacOS Unix this will map to $HOME.
     *
     * @return full path to output folder ending with separator char (typically '<code>/</code>'),
     * so that content may be appended to it
     * @see #getDefaultInputPath()
     */
    public static String getDefaultOutputPath() {
        if (System.getProperty("os.name").toLowerCase().contains("mac os")) {
            return System.getProperty("user.home") + File.separator + "Documents" + File.separator + "GoICasterCompanion" + File.separator + "output" + File.separator;
        } else {
            return FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + File.separator + "GoICasterCompanion" + File.separator + "output" + File.separator;
        }
    }

    /**
     * Gets the currently set application input folder path.
     *
     * @return user preferred input folder, default if no preference is set
     * @see #getOutputPath()
     */
    public static String getInputPath() {
        return prefs.get(INPUT_FOLDER, getDefaultInputPath());
    }

    /**
     * Gets the default (OS specific) application input folder path.
     *
     * This should be the "Input" folder within "Documents" or documents equivalent on the user's OS.
     * On non-MacOS Unix this will map to $HOME.
     *
     * @return full path to input folder ending with separator char (typically '<code>/</code>'),
     * so that content may be appended to it
     * @see #getDefaultOutputPath()
     */
    public static String getDefaultInputPath() {
        if (System.getProperty("os.name").toLowerCase().contains("mac os")) {
            return System.getProperty("user.home") + File.separator + "Documents" + File.separator + "GoICasterCompanion" + File.separator + "input" + File.separator;
        } else {
            return FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + File.separator + "GoICasterCompanion" + File.separator + "input" + File.separator;
        }
    }

    /**
     * Sets the preferred output path
     * @param outputPath valid path to new output folder, MUST END IN FILE SEPARATOR
     */
    public static void setOutputPath(String outputPath) { //todo, input a Path instead?
        prefs.put(OUTPUT_FOLDER, outputPath);
    }

    /**
     * Sets the preferred input path
     * @param inputPath valid path to new input folder, MUST END IN FILE SEPARATOR
     */
    public static void setInputPath(String inputPath) {
        prefs.put(INPUT_FOLDER, inputPath);
    }

    /**
     * Resets the outputPath to application default
     */
    public static void resetOutputPath() {
        prefs.remove(OUTPUT_FOLDER);
    }

    /**
     * Resets the inputPath to application default
     */
    public static void resetInputPath() {
        prefs.remove(INPUT_FOLDER);
    }

    private static void CLIParse(String input) {
        Scanner scanner = new Scanner(System.in); //stdin

        switch (input) {
            case "quit": case "exit": case "q":
                System.out.println("exiting...");
                System.exit(0);
                break;
            case "help":
                System.out.println("TOURNAMENT NUMBER");
                System.out.println("    'number set', update current tourney number");

                System.out.println("TIMER");
                System.out.println("    'timer set', determine timer length");
                System.out.println("    'timer start', start the timer");
                System.out.println("    'timer pause', pause the timer");
                System.out.println("    'timer stop', ends the timer early");
                System.out.println("    'timer restart', restarts the timer");

                System.out.println("TEAMS");
                System.out.println("    'team add', add a team");
                System.out.println("    'team add-noimg', add a team (no image)");
                System.out.println("    'team set', set an active team (MAIN FCN)");

                System.out.println("MAPS");
                System.out.println("    'map set', set current map");

                break;
            case "team set": case "ts":
                System.out.print("Enter team's full name: ");
                String teamName = scanner.nextLine();
                System.out.print("\nEnter team's letter: ");
                String teamLetter = scanner.nextLine();
                try {
                    setTeam(teamName,teamLetter);
                } catch (IOException exception) {
                    exception.printStackTrace();
                } catch (IllegalArgumentException exception) {
                    System.err.println(exception.getMessage());
                }
                break;

            case "map set": case "ms":
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

            case "number set":
                System.out.print("Enter tournament number: ");
                String tourneyNumber = scanner.nextLine();
                    if (setTourneyNumber(tourneyNumber)) {
                        System.out.println("Current tournament number updated!");
                    } else {
                        System.err.println("Failed to update current tournament number.");
                    }
                break;

            case "verify": case "v":
                try {
                    verifyContent();
                    System.out.println("All folders verified!");
                } catch (IOException exception) {
                    System.err.println("Folder recreation failed!");
                    exception.printStackTrace();
                }
                break;

            case "team add":
                System.out.println("Enter Team Name: ");
                String fullName0 = scanner.nextLine();
                System.out.println("Enter team abbreviation: ");
                String shortName0 = scanner.nextLine();
                System.out.println("Enter image path: ");
                Path imagePath = Paths.get(scanner.nextLine());
                try {
                    addTeam(fullName0, shortName0, imagePath);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                break;

            case "team add-noimg":
                System.out.println("Enter Team Name: ");
                String fullName = scanner.nextLine();
                System.out.println("Enter team abbreviation: ");
                String shortName = scanner.nextLine();
                try {
                    addTeam(fullName, shortName);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                break;

            case "timer set":
                LobbyTimer timer = LobbyTimer.getInstance();
                try {
                    System.out.println("How many seconds would you like to set the timer for");
                    int setTime = scanner.nextInt();
                    timer.pause(); //pause timer to prevent ticking
                    timer.setInitialTimerLength(setTime); //set new initial
                    timer.set(setTime); //set currently displayed timer to not confuse user
                } catch (IOException exception) {
                    exception.printStackTrace();
                } catch (InputMismatchException exception) {
                    exception.printStackTrace(); //thrown by scanner
                } catch (NoSuchElementException exception) {
                    exception.printStackTrace();
                } catch (IllegalArgumentException exception) {
                    System.out.println("Cannot set initial timer value to 0!");
                }
                break;

            case "timer start":
                try {
                    LobbyTimer.getInstance().start();
                } catch (IOException exception) {
                    exception.printStackTrace();
                } catch (NoSuchElementException exception) {
                    exception.printStackTrace();
                } catch (InvalidDataException e) {
                    e.printStackTrace();
                }
                break;

            case "timer stop":
                try {
                    LobbyTimer.getInstance().stop();
                    System.out.println("Stopped!");
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                break;

            case "timer pause":
                LobbyTimer.getInstance().pause();
                break;

            case "timer restart": case "timer reset":
                try {
                    LobbyTimer.getInstance().restart();
                } catch (IOException | InvalidDataException exception) {
                    exception.printStackTrace();
                }
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
    public static boolean setTourneyNumber(String number) {
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
     * Gets the current tournament number from file
     * @return current tournament number, or 1 if IO error encountered
     * @throws NumberFormatException if tournament file does not contain valid content
     * @throws FileNotFoundException TournamentNumber.txt could not be found
     */
    public static int getTourneyNumber() throws NumberFormatException, FileNotFoundException {
        Scanner tourneyScanner = new Scanner(new File(outputPath + "TournamentNumber.txt"));
        String firstLine = tourneyScanner.nextLine();
        int foo = Integer.parseInt(firstLine);
        tourneyScanner.close();
        return foo;
    }

    /**
     * Checks for <code>input</code> and <code>output</code> folders, creates them if necessary.
     *
     * @throws IOException Error creating file or directory
     */
    public static void verifyContent() throws IOException {
        System.out.println("verifying folders");

        //check input folder
        Path inputPathObject = Paths.get(inputPath);
        if (!Files.exists(inputPathObject)) {
            System.out.println("Input folder cannot be found... recreating");
            Master.newWarning("File Verification","Input Folder Cannot be Found","During verification, the input folder could not be found in: '" + inputPath + "'. Download a pre-made input folder from the website (goicc.skyborne.net), and put it in the aforementioned directory.");
            if (inputPathObject.toFile().mkdirs()) {
                System.out.println("Input folder created successfully");
            } else {
                throw new IOException("Error when creating input directory!");
            }
        } else System.out.println("Input folder found");

        //check output folder
        Path outputPathObject = Paths.get(outputPath);
        if (!Files.exists(outputPathObject)) {
            System.out.println("Output folder cannot be found... recreating");
            if (outputPathObject.toFile().mkdirs()) {
                System.out.println("Output folder created successfully");
            } else {
                throw new IOException("Error when creating output directory!");
            }
        } else System.out.println("Output folder found");

        //check map_images folder
        Path map_imagesPathObject = Paths.get(inputPath + "map_images");
        if (!Files.exists(map_imagesPathObject)) {
            System.out.println("map_images folder cannot be found... recreating");
            if (map_imagesPathObject.toFile().mkdirs()) {
                System.out.println("map_images folder created successfully");
            } else {
                throw new IOException("Error when creating map_images directory!");
            }
        } else System.out.println("map_images folder found");

        //check team_logos folder
        Path team_logosPathObject = Paths.get(inputPath + "team_logos");
        if (!Files.exists(team_logosPathObject)) {
            System.out.println("team_logos folder cannot be found... recreating");
            if (team_logosPathObject.toFile().mkdirs()) {
                System.out.println("team_logos folder created successfully");
            } else {
                throw new IOException("Error when creating team_logos directory!");
            }
        } else System.out.println("team_logos folder found");

        //check maps.txt file
        File maps = new File(inputPath + "maps.txt");
        if (maps.createNewFile()) {
            System.out.println("maps.txt was missing and has been created");
        } else System.out.println("maps.txt file found");

        //check teams.txt
        File teams = new File(inputPath + "teams.txt");
        if (teams.createNewFile()) {
            System.out.println("teams.txt was missing and has been created");
        } else System.out.println("teams.txt file found");

        //check TimerLength.txt
        File timerLength = new File(inputPath + "TimerLength.txt");
        if (timerLength.createNewFile()) {
            System.out.println("timerLength.txt was missing and has been created");
        } else System.out.println("timerLength.txt file found");
    }

    /**
     * Checks if a map name is a valid map found in <code>maps.txt</code>.
     *
     * @param map Full name of a map to check
     * @return <code>true</code> if map was found in maps.txt; <code>false</code> otherwise.
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
     * Sets the current map.
     *
     * @param mapName Name of map to be set as current.
     * @return false if map could not be updated (Doesnt exist, IO error). True otherwise.
     * @throws IOException IO error for maps.txt opening or writing
     * @throws FileNotFoundException Likely error finding map file
     */
    public static boolean setMap(String mapName) throws IOException {

        /*--- Check Map Validity ---*/
        try {
            if (!isValidMap(mapName)) {
                System.err.println("That map does not exist in maps.txt");
                return false;
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Map file could not be found, reason:" + e.getMessage());
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
     * Sets the current map, with or without spawn locations
     *
     * @param mapName Name of map to be set as current
     * @param isSpawn If spawns should be shown on map or not
     * @throws IOException IO error for maps.txt opening or writing
     * @throws FileNotFoundException Likely error finding map file
     */
    public static void setMap(String mapName, boolean isSpawn) throws IOException {

        /*--- Check Map Validity ---*/
        try {
            if (!isValidMap(mapName)) {
                System.err.println("That map does not exist in maps.txt");
                return;
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Map file could not be found, reason:" + e.getMessage());
        }

        /*--- Update Map.png ---*/
        //copy map from map_images
        if (isSpawn) {
            try {
                //try to get spawn file
                Files.copy(Paths.get(inputPath + "map_images" + File.separator + mapName + " Spawn.png"), Paths.get(outputPath + "Map.png"), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException exception) {
                exception.printStackTrace();
                //fallback for if spawn image doesn't exist
                Files.copy(Paths.get(inputPath + "map_images" + File.separator + mapName + ".png"), Paths.get(outputPath + "Map.png"), StandardCopyOption.REPLACE_EXISTING);
                //alert user of fallback
                Master.newWarning("File Error", "Map Not Found", "Spawn map not found, falling back to non-spawn map");
            }
        } else {
            //get file
            Files.copy(Paths.get(inputPath + "map_images" + File.separator + mapName + ".png"), Paths.get(outputPath + "Map.png"), StandardCopyOption.REPLACE_EXISTING);
        }
        System.out.println("Map.png Updated");

        /*--- Update Map.txt ---*/
        Writer fileWriter = new FileWriter(outputPath + "Map.txt");
        fileWriter.write(mapName);
        fileWriter.close();
        System.out.println("Map.txt Updated");

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
     * @throws IOException error writing, reading, or converting files team files.
     * @throws IllegalArgumentException Thrown if fcn args contain invalid characters or do not correspond to a team in teams.txt
     */
    public static void setTeam(String newTeamName, String teamIdentifier) throws IOException, IllegalArgumentException {

        //check for illegal characters in input
        if (!checkCharLegality(newTeamName) || !checkCharLegality(teamIdentifier)) throw new IllegalArgumentException("Invalid team name or identifier");


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
            throw new NoSuchFileException("Team logo not found!");
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
                        throw new IOException("Failed to delete the file");
                    }

                    //copy from teamLogo Path to TeamX.png
                    Files.copy(Paths.get(inputPath + "team_logos" + File.separator + newTeamName + ".png"),Paths.get(outputPath + "Team" + teamIdentifier + ".png"), StandardCopyOption.REPLACE_EXISTING);

                } else {
                    System.err.println("Image could not be converted to PNG");
                    //remove current output team logo to prevent mismatch
                    // better to have right text and no logo, than right text and wrong logo.
                    File outputTeamLogo = new File(outputPath + "Team" + teamIdentifier + ".png");
                    outputTeamLogo.delete();
                    throw new IOException("Image could not be converted to PNG. Warning TXTs may already be updated!");
                }
            } catch (IOException exception) {
                throw new IOException("Error during converting or copying image. Reason: " + exception.getMessage(), exception);
            }
        }
    }

    /**
     * Check if a string contains illegal characters for the filesystem.
     * This is not an all-inclusive list, but should deal with most erroneous inputs.
     *
     * @param stringToCheck A string intended to be part of a file name, that needs checking.
     * @return <code>true</code> if all chars are legal, <code>false</code> otherwise
     */
    private static boolean checkCharLegality(String stringToCheck) {
        //throw exception if stringToCheck contains filesystem illegal characters
        final String[] ILLEGAL_CHARACTERS = { "/", "\n", "\r", "\t", "\0", "\f", "`", "?", "*", "\\", "<", ">", "|", "\"", ":", ".", ".."};
        return Arrays.stream(ILLEGAL_CHARACTERS).noneMatch(stringToCheck::contains);
    }

    /**
     * Adds a team with no logo
     * Updates <code>teams.txt</code>.
     *
     * @param fullName full team name to add
     * @param shortName short name of team, will be converted to caps
     * @throws IOException IO on teams.txt failed.
     */
    public static void addTeam(String fullName, String shortName) throws IOException {
        //create string
        String newTeamLine = System.lineSeparator() + fullName + "|" + shortName.toUpperCase();

        //add string to teams.txt
        Files.write(Paths.get(inputPath + "teams.txt"), newTeamLine.getBytes(), StandardOpenOption.APPEND);
    }

    /**
     * Adds a team with no logo
     * Updates <code>teams.txt</code>.
     *
     * @param fullName full team name to add
     * @param shortName short name of team, will be converted to caps
     * @param image Path to image file which will be added to input folder
     * @throws IOException file copy for image failed, or IO to teams.txt failed
     * @throws FileNotFoundException if the Path is a directory or extensionless file
     */
    public static void addTeam(String fullName, String shortName, Path image) throws IOException {
        //add team to txt
        addTeam(fullName, shortName);

        //parse image filetype
        String file = image.getFileName().toString();
        if (!file.contains(".")) throw new FileNotFoundException(""); // path cannot be a directory.
        String ext = file.substring(file.indexOf("."));

        //add image
        // copy from image Path to input/team_logos/fullName.ext
        Files.copy(image, Paths.get(inputPath + "team_logos" + File.separator + fullName + ext));
    }

    /**
     * Takes a list of {@link Path}s and finds a certain file by name, excluding extension
     * @param allTeamImages list of paths, all paths should be a file with an extension
     * @return Path of found file
     */
    protected static Path findFile(LinkedList<Path> allTeamImages, String searchFor) {
        for (Path aPath : allTeamImages) {
            if (aPath.getFileName().toString().contains(".")) { //if is a file not a directory
                if (aPath.getFileName().toString().substring(0,aPath.getFileName().toString().indexOf(".")).equalsIgnoreCase(searchFor)) {
                    return aPath;
                }
            }
        }
        return null;
    }

    /**
     * Parses `teams.txt` to get a team's longName from its shortName.
     * Case insensitive search. Uppercase return.
     * Can be used to verify the existence of a team by their full team name.
     *
     * @param longName longName to find corresponding shortName of
     * @return Shortname, or <code>null</code> if no shortname can be found
     * @throws FileNotFoundException teams.txt could not be found
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

    /**
     * Gets an updated list of maps from the <code>maps.txt</code> file
     * @return Each line of the maps.txt file, except empty lines
     * @throws FileNotFoundException if <code>maps.txt</code> file was not found
     */
    public static LinkedList<String> getAllMapsFromDisk() throws FileNotFoundException {
        //return list
        LinkedList<String> mapList = new LinkedList<>();

        //get all lines from maps.txt
        File mapFile = new File(inputPath + "maps.txt");
        Scanner scanner = new Scanner(mapFile);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (!line.equals("")) { mapList.add(line); }
        }
        return mapList;
    }

    public static void getScsBracket(int number) { //todo, can be replaced by commonsIO
        try {
            URL website = new URL("https://challonge.com/SCS" + number + ".svg");
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            new File(outputPath + "SCSBracket.svg").createNewFile();
            FileOutputStream fos = new FileOutputStream(outputPath + "SCSBracket.svg");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}