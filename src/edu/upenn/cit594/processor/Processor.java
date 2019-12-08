package edu.upenn.cit594.processor;

import edu.upenn.cit594.data.*;
import edu.upenn.cit594.datamanagement.PopulationTextReader;
import edu.upenn.cit594.datamanagement.Reader;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.calculators.PropertyCalculator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Processor {

    protected Reader populationReader;
    protected Reader parkingReader;
    protected Reader propertyReader;
    private Map<Integer, Population> populationZipcodeMap;
    private Map<Integer, LinkedList<Parking>> parkingZipcodeMap;
    private Map<Integer, LinkedList<Property>> propertyZipcodeMap;

    public Processor (Reader populationReader, Reader parkingReader, Reader propertyReader) {
        // store the readers and read data from files
        this.populationReader = populationReader;
        this.parkingReader = parkingReader;
        this.propertyReader = propertyReader;

        this.populationZipcodeMap = (Map<Integer, Population>) populationReader.read();
        this.parkingZipcodeMap = (Map<Integer, LinkedList<Parking>>) parkingReader.read();
        this.propertyZipcodeMap = (Map<Integer, LinkedList<Property>>) propertyReader.read();
    }

    /**
     * This function get the flu count by each state and return a Map
     * @return Map<State, Integer>
     */
    public Integer getTotalPopulation () {
        Integer totalNumberOfPeople = 0;

        for (Population population: this.populationZipcodeMap.values()) {
            totalNumberOfPeople += population.getNumberOfPeople();
        }

        return totalNumberOfPeople;
    }

    public Map<Integer, Double> getTotalFinesPerCapita () {
        Map<Integer, Double> totalFinesPerCapitaMap = new HashMap<>();

        for (Integer zipcode:populationZipcodeMap.keySet()) {
            Integer numberOfPeople = populationZipcodeMap.get(zipcode).getNumberOfPeople();

            Integer totalFines = 0;

            LinkedList<Parking> parkingList = parkingZipcodeMap.get(zipcode);

            if (parkingList == null) {
                continue;
            }

            for (Parking parking: parkingList) {
                totalFines += parking.getFine();
            }

            totalFinesPerCapitaMap.put(zipcode, totalFines / (double) numberOfPeople);
        }

        return totalFinesPerCapitaMap;
    }

    public Integer getAvgResidentialMarketValue (Integer zipcode) {
        return new PropertyCalculator().calculateAvgForMarketValue(propertyZipcodeMap, zipcode);
    }

    public Integer getAvgResidentialTotalLivableArea (Integer zipcode) {
        return new PropertyCalculator().calculateAvgForTotalLivableArea(propertyZipcodeMap, zipcode);
    }

    public Integer getTotalResidentialMarketValuePerCapita (Integer zipcode) {
        int totalResidentialMarketValuePerCapita = 0;
        double totalMarketValue = 0.0;
        int totalResidentialCount = populationZipcodeMap.get(zipcode).getNumberOfPeople();

        LinkedList<Property> propertyList = propertyZipcodeMap.get(zipcode);

        if (propertyList == null) {
            return 0;
        }

        for (Property property: propertyList) {
            totalMarketValue += property.getMarketValue();
        }

        totalResidentialMarketValuePerCapita = (int) totalMarketValue / totalResidentialCount;

        return totalResidentialMarketValuePerCapita;
    }

//    /**
//     * This function get all the logs need to record in the log file and return them in a list
//     * @return List<String>
//     */
//    public List<String> getAllLogs () {
//        List<String> logs = new LinkedList<>();
//
//        for (Twitter twitter: twitters) {
//            if (Processor.isFluTweet(twitter.getText())) {
//                State state = findNearestState(twitter.getLatitude(), twitter.getLongitude());
//
//                String log = state.getName() + "    " + twitter.getText();
//                logs.add(log);
//            }
//        }
//
//        return logs;
//    }
//
//    private static Boolean isFluTweet (String text) {
//
//        String pattern = "#?flu\\W?";
//
//        String[] textArr = text.toLowerCase().split(" ");
//        for (String word: textArr) {
//            if (Pattern.matches(pattern, word)) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    private State findNearestState (Double latitude, Double longtitue) {
//        State nearestState = null;
//        Double shortestDistance = Double.POSITIVE_INFINITY;
//
//        // Calculate the distance for each state and find the nearest state
//        for ( State state: states ) {
//            Double distance = Processor.calculateEuclideanDistance(latitude, longtitue, state.getLatitude(), state.getLongitude());
//
//            if ( distance < shortestDistance) {
//                nearestState = state;
//                shortestDistance = distance;
//            }
//        }
//
//        return nearestState;
//    }
//
//    private static Double calculateEuclideanDistance (Double latitude1, Double longtitue1, Double latitude2, Double longtitue2) {
//        return Math.sqrt(Math.pow((latitude1 - latitude2), 2) + Math.pow((longtitue1 - longtitue2), 2));
//    }
}
