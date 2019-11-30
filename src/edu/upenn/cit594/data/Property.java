package edu.upenn.cit594.data;

/**
 * @author atu
 */

public class Property {
    private Double totalLivableArea;
    private Integer zipCode;
    private Double marketValue;
    private String buildingCode;

    public Double getTotalLivableArea() {
        return totalLivableArea;
    }

    public void setTotalLivableArea(Double totalLivableArea) {
        this.totalLivableArea = totalLivableArea;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public Double getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(Double marketValue) {
        this.marketValue = marketValue;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }
}
