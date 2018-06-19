package common;

/**
 * Created an enum in order to be able to compare types of houses in an effective way. These are some examples of types of
 * properties that were available in Edinburgh when searching on AirBnB.
 */
public enum AirbnbProperty {
    GUESTHOUSE("ENTIRE GUESTHOUSE"), APARTMENT("ENTIRE FLAT"), ROOM("PRIVATE ROOM"), LOFT("ENTIRE LOFT"),
    VILLA("ENTIRE VILLA"), CONDO("ENTIRE CONDOMINIUM"), HOUSE("ENTIRE HOUSE");

    //The actual identifier as seen on the website
    private final String id;

    AirbnbProperty(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }
}
