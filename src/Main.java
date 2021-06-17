import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
                System.out.println("'setMap' or 'sm' to set current map"); //todo
                System.out.println("'clean' to empty output folder"); //todo
                System.out.println("'verify' to verify existence of required folders"); //todo
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
                        System.out.println("Failed to Update Team Completely. Risk of Incomplete update!");
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                break;
            case "setmap": case "sm":
                System.out.print("Enter full map name: ");
                String mapName = scanner.nextLine();
                try {
                    if (setMap(mapName)) {
                        System.out.println("Current Map Updated!");
                    } else {
                        System.out.println("Failed to Update Current Map");
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                break;
            case "verify": case "v":
                verifyFolders();
            default:
                System.out.println("Unknown Input. Type 'help' for commands");
        }
    }

    /**
     * Checks for the existence of the application's directories in the cwd
     */
    private static void verifyFolders() {

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

        /*--- Update Map.png ---*/
        //copy map from map_images
        Files.copy(Paths.get(inputPath + "map_images" + File.separator + mapName + ".png"), Paths.get(outputPath + "Map.png"), StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Map.png Updated");

        /*--- Update Map.txt ---*/
        Writer fileWriter = new FileWriter(outputPath + "Map.txt"); //TeamX.txt
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
     * @param newTeamName Name of the team. MUST BE A VALID TEAM!
     * @param teamIdentifier Either "A" or "B".
     * @return success or fail
     * @throws IOException
     */
    static boolean setTeam(String newTeamName, String teamIdentifier) throws IOException {
        teamIdentifier = teamIdentifier.toUpperCase();
        System.out.println("setTeam("+newTeamName+","+teamIdentifier+")");

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
                        System.out.println("Failed to delete the file");
                        return false;
                    }

                    //copy from teamLogo Path to TeamX.png
                    Files.copy(Paths.get(inputPath + "team_logos" + File.separator + newTeamName + ".png"),Paths.get(outputPath + "Team" + teamIdentifier + ".png"), StandardCopyOption.REPLACE_EXISTING);

                } else {
                    System.out.println("Image could not be converted to PNG");
                }
            } catch (IOException exception) {
                System.out.println("Error during converting image.");
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