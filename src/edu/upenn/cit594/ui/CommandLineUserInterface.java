package edu.upenn.cit594.ui;

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
        String zipcodeRaw;
        int zipcode;

        while (true) {
            System.out.println("Enter 0-6:");

            try {
                int choice = scanner.nextInt();
                Logger.writeLog(System.currentTimeMillis() + " " + choice);

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

                        long avgResidentialMarketValue = processor.getAvgResidentialMarketValue(zipcode);
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

                        long avgResidentialTotalLivableArea = processor.getAvgResidentialTotalLivableArea(zipcode);
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

                        long totalResidentialMarketValuePerCapita = processor.getTotalResidentialMarketValuePerCapita(zipcode);
                        System.out.println("The total residential market value per capita is: " + totalResidentialMarketValuePerCapita);
                        break;
                    case 6:
                        displayMktValueToFineRatio(processor.getMktValueToFinesPerCapitaRatio(processor.getTotalFinesPerCapita(), processor));
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

    private static void displaytotalFinesPerCapitaByZipcode(Map<Integer, Double> totalFinesPerCapitaMap) {
        for (Integer zipcode : totalFinesPerCapitaMap.keySet()) {
            Double fine = totalFinesPerCapitaMap.get(zipcode);
            if (fine > 0) {
                System.out.printf(zipcode + " %.4f\n", Math.floor(fine * 10000) / 10000.0);
            }
        };
    }

    private static void displayMktValueToFineRatio(TreeMap<Integer, Double> mktVlaueToFinesPerCapitaRatioMap) {
        System.out.println("The ratio of residential market value per capita to fines per capita for each areas is:");
        Set<Integer> zipcodes = mktVlaueToFinesPerCapitaRatioMap.keySet();
        for(Integer zipcode: zipcodes) {
            System.out.printf(zipcode + " %.4f\n", Math.floor(mktVlaueToFinesPerCapitaRatioMap.get(zipcode) * 10000) / 10000.0);
        }
    }
}
