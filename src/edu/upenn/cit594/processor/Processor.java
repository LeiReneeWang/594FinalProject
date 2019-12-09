package edu.upenn.cit594.processor;

import edu.upenn.cit594.data.Parking;
import edu.upenn.cit594.data.Population;
import edu.upenn.cit594.data.Property;
import edu.upenn.cit594.datamanagement.Reader;
import edu.upenn.cit594.processor.calculators.PropertyCalculator;

import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
     * This function get total population
     * @return Integer
     */
    public Integer getTotalPopulation () {
        Integer totalNumberOfPeople = 0;

        for (Population population: this.populationZipcodeMap.values()) {
            totalNumberOfPeople += population.getNumberOfPeople();
        }

        return totalNumberOfPeople;
    }

    public TreeMap<Integer, Double> getTotalFinesPerCapita () {
        TreeMap<Integer, Double> totalFinesPerCapitaMap = new TreeMap<>();

        for (Integer zipcode:populationZipcodeMap.keySet()) {
            Integer numberOfPeople = populationZipcodeMap.get(zipcode).getNumberOfPeople();

            Integer totalFines = 0;

            LinkedList<Parking> parkingList = parkingZipcodeMap.get(zipcode);

            if (parkingList == null || numberOfPeople == null || numberOfPeople == 0) {
                continue;
            }

            for (Parking parking: parkingList) {
                totalFines += parking.getFine();
            }

            totalFinesPerCapitaMap.put(zipcode, totalFines / (double) numberOfPeople);
        }

        return totalFinesPerCapitaMap;
    }

    public long getAvgResidentialMarketValue (Integer zipcode) {
        return new PropertyCalculator().calculateAvgForMarketValue(propertyZipcodeMap, zipcode);
    }

    public long getAvgResidentialTotalLivableArea (Integer zipcode) {
        return new PropertyCalculator().calculateAvgForTotalLivableArea(propertyZipcodeMap, zipcode);
    }

    public long getTotalResidentialMarketValuePerCapita (Integer zipcode) {

        if (zipcode == null || zipcode == -1) {
            return 0;
        }

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

        return (long) totalMarketValue / totalResidentialCount;
    }

    public TreeMap<Integer, Double> getMktValueToFinesPerCapitaRatio (TreeMap<Integer, Double> FinesPerCapitaMap, Processor processor) {
     TreeMap<Integer, Double> mktValueToFinesPerCapitaRatioMap = new TreeMap<>();
     Set<Integer> zipcodes = FinesPerCapitaMap.keySet();
     for(Integer zipcode: zipcodes) {
         Double fine = FinesPerCapitaMap.get(zipcode);
         long totalMktValuePerCapita = processor.getTotalResidentialMarketValuePerCapita(zipcode);
         Double ratio = totalMktValuePerCapita / fine;
         if (fine > 0) {
             mktValueToFinesPerCapitaRatioMap.put(zipcode, ratio);
         }
     }

     return mktValueToFinesPerCapitaRatioMap;
    }
}
