package goistreamtoolredux;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogCrashHandler {
    static Logger logger = Logger.getLogger("GoISTR"); //get logger

    private static boolean isSafeExitedLog0;
    private static boolean isSafeExitedLog1;

    /**
     * Creates logfiles - <code>GoISTR.log.0</code> & <code>GoISTR.log.1</code> - if not already made.
     * Checks logs for crashes, indicates crash status to {@link #isSafeExitedLog0} and {@link #isSafeExitedLog1}.
     * Generate crash report to <code>GoISTRCrash.log</code> if there was a crash.
     * Sets up the logger "GoISTR" and its handler.
     *
     * @// TODO: 6/21/21 old implementation in previous app sent crash report, no such mail feature is set up in this app yet
     */
    static void run() {
        boolean isNew0 = true; //used to indicate if logfile 0 is new
        boolean isNew1 = true; //user to indicate if logfile 1 is new

        //determine if log files have already been created or not. Necessary for detecting crashes.
        try {

            //create log files if not already there
            File log1 = new File("GoISTR.log.1");
            File log0 = new File("GoISTR.log.0");

            //check if new
            isNew0 = log0.createNewFile();
            isNew1 = log1.createNewFile(); // if file already exists will do nothing

            //For both logs; if not new, check for safe exit. If new, mark as safe exit.
            if (!isNew0) { //if pre-existing log
                String log0Tail = tail(log0); //get tail
                if (log0Tail != null) { //if tail isn't empty
                    isSafeExitedLog0 = log0Tail.equals("INFO: Exiting") || log0Tail.contains("crash detected");
                }
            } else { //if new log
                isSafeExitedLog0 = true;
            }
            if (!isNew1) {
                String log1Tail = tail(log1);
                isSafeExitedLog1 = log1Tail.equals("INFO: Exiting") || log1Tail.contains("crash detected");
            } else { //if new log
                isSafeExitedLog1 = true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //if (there is a crash), generate crash report
        if (!isSafeExitedLog0 || !isSafeExitedLog1) {
            //create crash report
            try {
                File crashReportFile = new File("GoISTRCrash.log");

                //clean file contents
                crashReportFile.createNewFile();
                crashReportFile.delete();
                crashReportFile.createNewFile();

                //fill with crash report

                List<String> lines;


                //convert logfile into string
                PrintWriter writer = new PrintWriter(crashReportFile);

                String crashReportContents;
                //get correct logfile, write crash report header
                if (!isSafeExitedLog0) { //log0 was a crash
                    System.out.println("Reading logfile 0");
                    lines = Files.readAllLines(Paths.get("GoISTR.log.0"), StandardCharsets.US_ASCII); //read logfile 0
                    writer.write("---BEGIN CRASH REPORT [LOG " + 0 + " ]---" + System.lineSeparator());
                } else if (!isSafeExitedLog1){ //log1 was a crash
                    System.out.println("Reading logfile 1");
                    lines = Files.readAllLines(Paths.get("GoISTR.log.1"), StandardCharsets.US_ASCII); //read logfile 1
                    writer.write("---BEGIN CRASH REPORT [LOG " + 1 + " ]---" + System.lineSeparator());
                } else {
                    System.err.println("Something went wrong");
                    return;
                }

                //write logfile to crash report
                for (String line :
                        lines) {
                    writer.write(line + System.lineSeparator());
                }
                //write ending note
                writer.write("---END CRASH REPORT---");

                //closer writer
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("log0 exited safely? : "+ isSafeExitedLog0);
        System.out.println("log1 exited safely? : "+ isSafeExitedLog1);


        //report a new line of contents to crashed logs so they no longer indicate a crash
        //This prevents reading that a log indicates a crash more than once.
        if (!isSafeExitedLog0 || isNew0) {
            System.out.println("isNew0 or is not safe 0");
            try { //try log 0
                Writer bufferedWriterLog0 = new BufferedWriter(new FileWriter("GoISTR.log.0", true));
                bufferedWriterLog0.append("crash detected or new file created");
                bufferedWriterLog0.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!isSafeExitedLog1 || isNew1) {
            System.out.println("isNew1 or is not safe 1");
            try {//try log 1
                Writer bufferedWriterLog1 = new BufferedWriter(new FileWriter("GoISTR.log.1", true));
                bufferedWriterLog1.append("crash detected or new file created");
                bufferedWriterLog1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //setting up logger
        try {
            //define handler
            FileHandler handler = new FileHandler("GoISTR.log", 1000000,2); //1MB of logs per file, two files.
            SimpleFormatter formatter = new SimpleFormatter();
            handler.setFormatter(formatter);
            handler.setLevel(Level.ALL);

            //add handler to logger
            logger.addHandler(handler);
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.setLevel(Level.ALL);
    }

    public static String tail(File file) {
        RandomAccessFile fileHandler = null;
        try {
            fileHandler = new RandomAccessFile( file, "r" );
            long fileLength = fileHandler.length() - 1;
            StringBuilder sb = new StringBuilder();

            for(long filePointer = fileLength; filePointer != -1; filePointer--){
                fileHandler.seek( filePointer );
                int readByte = fileHandler.readByte();

                if( readByte == 0xA ) {
                    if( filePointer == fileLength ) {
                        continue;
                    }
                    break;

                } else if( readByte == 0xD ) {
                    if( filePointer == fileLength - 1 ) {
                        continue;
                    }
                    break;
                }

                sb.append( ( char ) readByte );
            }

            String lastLine = sb.reverse().toString();
            return lastLine;
        } catch( java.io.FileNotFoundException e ) {
            logger.warning("File was not found, " + e);
            e.printStackTrace();
            return null;
        } catch( java.io.IOException e ) {
            logger.warning("IO Exception, " + e);
            e.printStackTrace();
            return null;
        } finally {
            if (fileHandler != null )
                try {
                    fileHandler.close();
                } catch (IOException e) {
                    /* ignore */
                }
        }
    }
}
