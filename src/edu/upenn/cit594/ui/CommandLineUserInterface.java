package edu.upenn.cit594.ui;

import edu.upenn.cit594.data.State;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.Processor;

import java.util.*;

/**
 * @author atu
 */

public class CommandLineUserInterface {

    protected Processor processor;
    protected Scanner scanner;

    public CommandLineUserInterface(Processor processor) {
        this.processor = processor;
        scanner = new Scanner(System.in);
    }

    public void start () {

        int zipcode;

        System.out.println("Enter 0-6:");

        try {
            int choice = scanner.nextInt();

            if (choice < 0 || choice > 6) {
                System.out.println("Your input is not correct. Please enter a number between 0-6");
                System.exit(0);
            }

            switch (choice) {
                case 0:
                    System.out.println("System exit. Thanks for using");
                    System.exit(0);
                    break;
                case 1:
                    // Get the total number of the population
                    Integer totalNumberOfPeople= processor.getTotalPopulation();
                    System.out.println("The total population is " + totalNumberOfPeople);
                    break;
                case 2:
                    processor.getTotalFinesPerCapita();
                    break;
                case 3:
                    System.out.println("Please enter a zipcode");
                    zipcode = scanner.nextInt();
                    processor.getAvgResidentialMarketValue(zipcode);
                    break;
                case 4:
                    System.out.println("Please enter a zipcode");
                    zipcode = scanner.nextInt();
                    processor.getAvgResidentialTotalLivableArea(zipcode);
                    break;
                case 5:
                    System.out.println("Please enter a zipcode");
                    zipcode = scanner.nextInt();
                    processor.getTotalResidentialMarketValuePerCapita(zipcode);
                    break;
                case 6:
                    break;
                default:
                    System.out.println("Your input is not correct. Please enter a number between 0-6");
                    System.exit(0);
            }

//            // Get all the logs from processer and write to the log file
//            List<String> logs = processor.getAllLogs();
//            Logger logger = Logger.getInstance();
//            logger.writeLogs(logs);

            scanner.close();
        } catch (InputMismatchException e) {
            System.out.println("Your input is not correct. Please enter a number between 0-6");
            System.exit(0);
        }
    }

    public static void displayFluCountByState(Map<State, Integer> stateFluCount) {

        // Copy the states in the Map into a list to sort. Then output by state in alphabetical order
        List<State> statesList = new LinkedList<>();
        stateFluCount.forEach((state, count) -> {
            statesList.add(state);
        });

        statesList.sort(new Comparator<State>()  {
            @Override
            public int compare(State o1, State o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        statesList.forEach((state) -> {
            System.out.println(state.getName() + ": " + stateFluCount.get(state));
        });
    }
}
