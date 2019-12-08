package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.data.Population;
import edu.upenn.cit594.logging.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class PopulationTextReader implements Reader<Map<Integer, Population>> {

    private String filename;

    public PopulationTextReader(String name) {
        filename = name;
    }

    /**
     * This function reads the raw data in a text file into a Map<Integer, Population>
     * @return Map<Integer, Population>
     */
    public Map<Integer, Population> read() {
        Logger.writeLog(System.currentTimeMillis() + " " + filename);
        Map<Integer, Population> populationZipcodeMap = new HashMap<>();
        try {
            Scanner scanner = new Scanner(new File(filename));

            while(scanner.hasNextLine()){
                Population population = new Population();
                String[] arr = scanner.nextLine().split(" ");

                Integer zipcode = Integer.valueOf(arr[0]);
                Integer numberOfPeople = Integer.valueOf(arr[1]);

                population.setZipCode(zipcode);
                population.setNumberOfPeople(numberOfPeople);

                populationZipcodeMap.put(zipcode, population);
            }

            scanner.close();
        } catch ( FileNotFoundException e) {
            System.out.println("Unable to read the file. Check the file name and permission.");
            System.exit(0);
        }

        return populationZipcodeMap;
    }
}
