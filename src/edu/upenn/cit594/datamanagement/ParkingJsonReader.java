package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.data.Parking;
import edu.upenn.cit594.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class ParkingJsonReader implements Reader<Map<Integer, LinkedList<Parking>>> {

    private String filename;

    public ParkingJsonReader(String name) {
        filename = name;
    }

    /**
     * This function reads the raw data in a text file into a Map<Integer, LinkedList<Parking>>. The map key is the zipcode
     * @return Map<Integer, LinkedList<Parking>>
     */
    public Map<Integer, LinkedList<Parking>>  read() {
        Logger.writeLog(System.currentTimeMillis() + " " + filename);
        Map<Integer, LinkedList<Parking>> parkingZipcodeMap = new HashMap<>();
        try  {
            JSONArray parkingArr = (JSONArray) new JSONParser().parse(new FileReader(filename));

            for (Object parkingObj: parkingArr) {
                JSONObject parkingJsonObj = (JSONObject) parkingObj;
                Parking parking = new Parking();


                if (parkingJsonObj.get("zip_code") == null || parkingJsonObj.get("zip_code").toString().isEmpty() ||
                    parkingJsonObj.get("fine") == null || parkingJsonObj.get("zip_code").toString().isEmpty()
                ) {
                    continue;
                }

                Integer zipcode = Integer.parseInt((String) parkingJsonObj.get("zip_code"));
                Integer fine = Math.toIntExact((Long) parkingJsonObj.get("fine"));
                String state = (String) parkingJsonObj.get("state");

                if (!"PA".equals(state)) {
                    continue;
                }

                parking.setZipCode(zipcode);
                parking.setFine(fine);

                parkingZipcodeMap.putIfAbsent(zipcode, new LinkedList<>());
                LinkedList<Parking> parkingList = parkingZipcodeMap.get(zipcode);
                parkingList.add(parking);
                parkingZipcodeMap.put(zipcode, parkingList);
            }
        } catch (IOException e) {
            System.out.println("Unable to read the file. Check the file name and permission.");
            System.exit(0);
        } catch (ParseException e) {
            System.out.println("Unable to read the file");
            System.exit(0);
        }

        return parkingZipcodeMap;
    }
}
