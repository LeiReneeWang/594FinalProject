package edu.upenn.cit594.ui;

import edu.upenn.cit594.data.State;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.Processor;

import java.text.ParseException;
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
        String zipcodeRaw;
        int zipcode;

        while (true) {
            System.out.println("Enter 0-6:");

            try {
                int choice = scanner.nextInt();
                Logger.writeLog(System.currentTimeMillis() + " " + choice);

                if (choice < 0 || choice > 6) {
                    System.out.println("Your input is not correct. Please enter a number between 0-6");
                    System.exit(0);
                }

                switch (choice) {
                    case 0:
                        scanner.close();
                        System.out.println("System exit. Thanks for using");
                        System.exit(0);
                        break;
                    case 1:
                        // Get the total number of the population
                        Integer totalNumberOfPeople= processor.getTotalPopulation();
                        System.out.println("The total population is: " + totalNumberOfPeople);
                        break;
                    case 2:
                        Map<Integer, Double> totalFinesPerCapitaMap = processor.getTotalFinesPerCapita();
                        System.out.println("The total fines per capita is: ");
                        displaytotalFinesPerCapitaByZipcode(totalFinesPerCapitaMap);
                        break;
                    case 3:
                        System.out.println("Please enter a zipcode");

                        zipcodeRaw = scanner.next();
                        Logger.writeLog(System.currentTimeMillis() + " " + zipcodeRaw);

                        try {
                            zipcode = Integer.parseInt(zipcodeRaw);
                        } catch (NumberFormatException e) {
                            zipcode = -1;
                        }

                        int avgResidentialMarketValue = processor.getAvgResidentialMarketValue(zipcode);
                        System.out.println("The average residential market value is: " + avgResidentialMarketValue);
                        break;
                    case 4:
                        System.out.println("Please enter a zipcode");

                        zipcodeRaw = scanner.next();
                        Logger.writeLog(System.currentTimeMillis() + " " + zipcodeRaw);

                        try {
                            zipcode = Integer.parseInt(zipcodeRaw);
                        } catch (NumberFormatException e) {
                            zipcode = -1;
                        }

                        int avgResidentialTotalLivableArea = processor.getAvgResidentialTotalLivableArea(zipcode);
                        System.out.println("The average residential total livable area is: " + avgResidentialTotalLivableArea);
                        break;
                    case 5:
                        System.out.println("Please enter a zipcode");

                        zipcodeRaw = scanner.next();
                        Logger.writeLog(System.currentTimeMillis() + " " + zipcodeRaw);

                        try {
                            zipcode = Integer.parseInt(zipcodeRaw);
                        } catch (NumberFormatException e) {
                            zipcode = -1;
                        }

                        int totalResidentialMarketValuePerCapita = processor.getTotalResidentialMarketValuePerCapita(zipcode);
                        System.out.println("The total residential market value per capita is: " + totalResidentialMarketValuePerCapita);
                        break;
                    case 6:
                        break;
                    default:
                        System.out.println("Your input is not correct. Please enter a number between 0-6");
                        System.exit(0);
                }
            } catch (InputMismatchException e) {
                Logger.writeLog(System.currentTimeMillis() + " " + scanner.next());
                System.out.println("Your input is not correct. Please enter a number between 0-6");
                scanner.close();
                System.exit(0);
            }
        }
    }

    public static void displaytotalFinesPerCapitaByZipcode(Map<Integer, Double> totalFinesPerCapitaMap) {
        // Copy the zipcode in the Map into a list to sort. Then output by state in ascending order
        List<Integer> zipcodeList = new LinkedList<>();
        totalFinesPerCapitaMap.forEach((state, count) -> {
            zipcodeList.add(state);
        });

        Collections.sort(zipcodeList);

        zipcodeList.forEach((zipcode) -> {
            System.out.printf(zipcode + " %.4f\n", Math.floor(totalFinesPerCapitaMap.get(zipcode) * 10000) / 10000.0);
        });
    }
}
