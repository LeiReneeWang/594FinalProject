package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.data.Population;
import edu.upenn.cit594.data.Property;
import edu.upenn.cit594.logging.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class PropertyTextReader implements Reader<Map<Integer, LinkedList<Property>>> {

    private String filename;

    public PropertyTextReader(String name) {
        filename = name;
    }

    /**
     * This function reads the raw data in a text file into a Map<Integer, LinkedList<Property>>
     * @return Map<Integer, LinkedList<Property>>
     */
    public Map<Integer, LinkedList<Property>> read() {
        Logger.writeLog(System.currentTimeMillis() + " " + filename);
        Map<Integer, LinkedList<Property>> propertyZipcodeMap = new HashMap<>();
        try {
            Scanner scanner = new Scanner(new File(filename));

            // Get title
            String[] titles = scanner.nextLine().split(",");

            Integer zipcodeIndex = Arrays.asList(titles).indexOf("zip_code");
            Integer marketValueIndex = Arrays.asList(titles).indexOf("market_value");
            Integer totalLivableAreaIndex = Arrays.asList(titles).indexOf("total_livable_area");

            while(scanner.hasNextLine()){
                Property property = new Property();

                // remove comma within quotes
                String nextline = scanner.nextLine();
                StringBuilder builder = new StringBuilder(nextline);
                boolean inQuotes = false;
                for (int currentIndex = 0; currentIndex < builder.length(); currentIndex++) {
                    char currentChar = builder.charAt(currentIndex);
                    if (currentChar == '\"') inQuotes = !inQuotes; // toggle state
                    if (currentChar == ',' && inQuotes) {
                        builder.setCharAt(currentIndex, ';');
                    }
                }
                String[] arr = builder.toString().split(",", titles.length);

                if (arr.length != titles.length) {
                    System.out.println(nextline);
                    continue;
                }

                String zipcodeStr = arr[zipcodeIndex];
                String marketValueStr = arr[marketValueIndex];
                String totalLivableAreaStr = arr[totalLivableAreaIndex];

                if (zipcodeStr.isEmpty() || zipcodeStr.substring(0, 5).length() != 5 || marketValueStr.isEmpty() || totalLivableAreaStr.isEmpty()) {
                    continue;
                }

                try {
                    Integer zipcode = Integer.valueOf(zipcodeStr.substring(0, 5));
                    Double marketValue = Double.valueOf(marketValueStr);
                    Double totalLivableArea = Double.valueOf(totalLivableAreaStr);

                    property.setZipCode(zipcode);
                    property.setMarketValue(marketValue);
                    property.setTotalLivableArea(totalLivableArea);

                    propertyZipcodeMap.putIfAbsent(zipcode, new LinkedList<>());
                    LinkedList<Property> propertyList = propertyZipcodeMap.get(zipcode);
                    propertyList.add(property);
                    propertyZipcodeMap.put(zipcode, propertyList);
                } catch (Exception e) {
                    continue;
                }
            }

            scanner.close();
        } catch ( FileNotFoundException e) {
            System.out.println("Unable to read the file. Check the file name and permission.");
            System.exit(0);
        }

        return propertyZipcodeMap;
    }
}
