package edu.upenn.cit594;
import edu.upenn.cit594.datamanagement.*;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.Processor;
import edu.upenn.cit594.ui.CommandLineUserInterface;


/**
 * @author atu
 */

public class Main {

    public static void main (String[] args) {

        // Check whether there are 5 input args
        if (args.length < 5) {
            System.out.println("Needs 5 args [parkingFileFormat] [parkingFileName] [propertyFileName] [populationFileName] [logFileName]");
            System.exit(0);
        }

        // Get arguments
        String parkingFileFormat = args[0];
        String parkingFileName = args[1];
        String propertyFileName = args[2];
        String populationFileName = args[3];
        String logFileName = args[4];

        // Init the log file name in the singleton Logger
        Logger.init(logFileName);
        Logger.writeLog(System.currentTimeMillis() + " " + parkingFileFormat + " " + parkingFileName + " " + propertyFileName + " " + populationFileName + " " + logFileName);

        // Init and create the Readers
        Reader populationReader = new PopulationTextReader(populationFileName);
        Reader propertyReader = new PropertyTextReader(propertyFileName);
        Reader parkingReader = null;

        if (parkingFileFormat.equals("csv")) {
            parkingReader = new ParkingTextReader(parkingFileName);
        } else if ( parkingFileFormat.equals("json") ) {
            parkingReader = new ParkingJsonReader(parkingFileName);
        } else {
            System.out.println("The parkingFileFormat is invalid. Only csv or json");
            System.exit(0);
        }

        // Init and create the Processor
        Processor processor = new Processor(populationReader, parkingReader, propertyReader);

        // Create the UI interface and start to run
        CommandLineUserInterface ui = new CommandLineUserInterface(processor);
        ui.start();
    }
}
