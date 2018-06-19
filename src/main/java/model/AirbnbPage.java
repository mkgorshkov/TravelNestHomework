package model;

import common.AirbnbAmenities;
import common.AirbnbProperty;

import java.util.HashSet;
import java.util.List;

public class AirbnbPage {
    private String URL;
    private String name;
    private AirbnbProperty propertyType;
    //Has the ability to be Studio for 0 bedrooms or otherwise text, leaving ability to have half bedrooms
    private String numBedroomsPretty;
    private Double numBedrooms;
    //Could either be a private bathroom(s) or shared bathrooms, leaving ability to have half bathrooms
    private Double numBathrooms;
    private boolean isBathroomShared;
    private HashSet<AirbnbAmenities> amenities;

    public AirbnbPage(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AirbnbProperty getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(AirbnbProperty propertyType) {
        this.propertyType = propertyType;
    }

    public Double getNumBathrooms() {
        return numBathrooms;
    }

    public void setNumBathrooms(Double numBathrooms) {
        this.numBathrooms = numBathrooms;
    }

    public boolean isBathroomShared() {
        return isBathroomShared;
    }

    public void setBathroomShared(boolean bathroomShared) {
        isBathroomShared = bathroomShared;
    }

    public String getNumBedroomsPretty() {
        return numBedroomsPretty;
    }

    public void setNumBedroomsPretty(String numBedroomsPretty) {
        this.numBedroomsPretty = numBedroomsPretty;
    }

    public Double getNumBedrooms() {
        return numBedrooms;
    }

    public void setNumBedrooms(Double numBedrooms) {
        this.numBedrooms = numBedrooms;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }


    public HashSet<AirbnbAmenities> getAmenities() {
        return amenities;
    }

    public void setAmenities(HashSet<AirbnbAmenities> amenities) {
        this.amenities = amenities;
    }

    @Override
    public String toString(){
        return "[URL="+getURL()+" name="+getName()+" propertyType="+getPropertyType()+" numBedroomsPretty="+getNumBedroomsPretty()+
                " numBedrooms="+getNumBedrooms()+" numBathrooms="+getNumBathrooms()+" isBathroomShared="+isBathroomShared()+
                " amenities="+getAmenities()+"]";
    }
}
