import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

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

        //update output file "TeamShortX.txt"
        Writer fileWriter2 = new FileWriter(outputPath + teamShortTXT); //TeamShortX.txt
        fileWriter2.write("TODO SHORTNAME GOES HERE"); //todo get shortname from input/teams.txt
        fileWriter2.close();

        return false;
    }


    /**
     * Parses `teams.txt` to get a team's longName from its shortName
     */
    void getLongName(String shortName) {
        //open input/teams.txt
        /*todo*/
        //parse through File for shortName
        /*todo*/
        //get and return corresponding longName
        /*todo*/
    }

    /**
     * Parses `teams.txt` to get a team's longName from its shortName
     */
    void getShortName(String longName) {
        //open input/teams.txt
        /*todo*/
        //parse through File for longName
        /*todo*/
        //get and return corresponding shortname
        /*todo*/
    }
}