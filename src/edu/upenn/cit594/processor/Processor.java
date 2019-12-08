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

        if (zipcode == null || zipcode == -1) {
            return 0;
        }

        int totalResidentialMarketValuePerCapita = 0;
        double totalMarketValue = 0.0;
        Population population = populationZipcodeMap.get(zipcode);
        LinkedList<Property> propertyList = propertyZipcodeMap.get(zipcode);

        if (population == null || population.getNumberOfPeople() == null || propertyList == null) {
            return 0;
        }

        int totalResidentialCount = population.getNumberOfPeople();
        for (Property property: propertyList) {
            totalMarketValue += property.getMarketValue();
        }

        totalResidentialMarketValuePerCapita = (int) totalMarketValue / totalResidentialCount;

        return totalResidentialMarketValuePerCapita;
    }
}
