package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.data.Parking;
import edu.upenn.cit594.logging.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class ParkingTextReader implements Reader<Map<Integer, LinkedList<Parking>>> {

    private String filename;

    public ParkingTextReader(String name) {
        filename = name;
    }

    /**
     * This function reads the raw data in a text file into a Map<Integer, LinkedList<Parking>>. The map key is the zipcode
     * @return Map<Integer, LinkedList<Parking>>
     */
    public Map<Integer, LinkedList<Parking>> read() {
        Logger.writeLog(System.currentTimeMillis() + " " + filename);
        Map<Integer, LinkedList<Parking>> parkingZipcodeMap = new HashMap<>();
        try {
            Scanner scanner = new Scanner(new File(filename));

            while(scanner.hasNextLine()){
                Parking parking = new Parking();
                String[] arr = scanner.nextLine().split(",");

                if (arr.length < 7) {
                    continue;
                }

                Integer zipcode = Integer.valueOf(arr[6]);
                Integer fine = Integer.valueOf(arr[1]);

                parking.setZipCode(zipcode);
                parking.setFine(fine);

                parkingZipcodeMap.putIfAbsent(zipcode, new LinkedList<>());
                LinkedList<Parking> parkingList = parkingZipcodeMap.get(zipcode);
                parkingList.add(parking);
                parkingZipcodeMap.put(zipcode, parkingList);
            }

            scanner.close();
        } catch ( FileNotFoundException e) {
            System.out.println("Unable to read the file. Check the file name and permission.");
            System.exit(0);
        }

        return parkingZipcodeMap;
    }
}
