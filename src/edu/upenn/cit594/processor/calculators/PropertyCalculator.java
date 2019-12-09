package edu.upenn.cit594.processor.calculators;

import edu.upenn.cit594.data.Property;
import edu.upenn.cit594.processor.calculators.operators.PropertyFieldGetter;
import edu.upenn.cit594.processor.calculators.operators.PropertyMarketValueFieldGetter;
import edu.upenn.cit594.processor.calculators.operators.PropertyTotalLivableAreaFieldGetter;

import java.util.LinkedList;
import java.util.Map;

/**
 * @author atu
 */

public class PropertyCalculator {

    public long calculateAvg (Map<Integer, LinkedList<Property>> propertyZipcodeMap, Integer zipcode, PropertyFieldGetter fieldGetter) {

        if (zipcode == null || zipcode == -1 || propertyZipcodeMap == null) {
            return 0;
        }

        double totalFieldValue = 0.0;
        int totalResidentialCount = 0;

        LinkedList<Property> propertyList = propertyZipcodeMap.get(zipcode);

        if (propertyList == null || propertyList.isEmpty()) {
            return 0;
        }

        for (Property property: propertyList) {
            totalFieldValue += fieldGetter.get(property);
            totalResidentialCount++;
        }

        return (long) totalFieldValue / totalResidentialCount;
    }

    public long calculateAvgForMarketValue(Map<Integer, LinkedList<Property>> propertyZipcodeMap, Integer zipcode) {
        return calculateAvg(propertyZipcodeMap, zipcode, new PropertyMarketValueFieldGetter());
    }

    public long calculateAvgForTotalLivableArea(Map<Integer, LinkedList<Property>> propertyZipcodeMap, Integer zipcode) {
        return calculateAvg(propertyZipcodeMap, zipcode, new PropertyTotalLivableAreaFieldGetter());
    }
}
