package common;

/**
 * Created an enum in order to be able to compare amenities in an effective way. These are all available amenities
 * based on the search page.
 */
public enum AirbnbAmenities {
    KITCHEN("Kitchen"),SHAMPOO("Shampoo"), HEATING("Heating"), AC("Air Conditioning"), WASHER("Washer"), DRYER("Dryer"),
    WIFI("Wireless Internet"), BREAKFAST("Breakfast"), FIREPLACE("Indoor Fireplace"), BUZZER("Buzzer/wireless intercom"),
    HANGERS("Hangers"), IRON("Iron"), HAIR_DRYER("Hair dryer"), WORKSPACE("Laptop friendly workspace"), TV("TV"), CRIB("Crib"),
    HIGH_CHAIR("High chair"), SELF_CHECKIN("Self check-in"), SMOKE_DETECTOR("Smoke detector"), CO2("Carbon monoxide detector"), CABLE_TV("Cable TV"),
    ESSENTIALS("Essentials"), HOT_WATER("Hot water"), PAID_PARKING("Paid parking on premises"), LOCKBOX("Lockbox"), PRIVATE_ENTRANCE("Private entrance"),
    BED_LINENS("Bed linens"), EXTRA_PILLOWS_BLANKETS("Extra pillows and blankets"), FIRST_AID_KIT("First aid kit"), FIRE_EXTINGUISHER("Fire extinguisher"),
    FREE_PARKING("Free parking on premises");

    //The actual identifier as seen on the website
    private final String id;

    AirbnbAmenities(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }
}
