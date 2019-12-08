package edu.upenn.cit594.processor.calculators.operators;

import edu.upenn.cit594.data.Property;

/**
 * @author atu
 */

public class PropertyMarketValueFieldGetter implements PropertyFieldGetter {

    @Override
    public double get(Property property) {
        return property.getMarketValue();
    }
}
