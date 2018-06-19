import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import common.AirbnbAmenities;
import common.AirbnbProperty;
import model.AirbnbPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/*
    Will scrape AirBnb at a given URL for certain parameters.
 */
public class AirBnbScraper {
    // Generic driver is defined
    private WebDriver driver;
    private AirbnbPage pageParams;

    // AirBNB opened at a specific URL for a specific listing
    public AirBnbScraper(String url){
        if(!url.contains("airbnb")){
            throw new RuntimeException("Cannot scrape non-airbnb sites.");
        }

        System.setProperty("webdriver.chrome.driver","src\\main\\resources\\chromedriver.exe");

        driver = new ChromeDriver();
        pageParams = new AirbnbPage();
        pageParams.setURL(url);
        driver.navigate().to(url);
    }

    /*
        Run and scrape the information we have predefined as needed
     */
    public void scrapeSite(){
        scrapePropertyType();
        scrapePropertyName();
        scrapeBedrooms();
        scrapeBathrooms();
        scrapeAmenities();
        driver.quit();

        /*
        In this implementation we will print out a prettyprint of the details of the property where in the real world
        we would likely cache or store this into a database
         */
        System.out.println(pageParams.toString());
    }

    /*
       Run through and scrape specified areas of the site based on the classNames, inside here we have a span that
       ultimately contains our property type.
    */
    private void scrapePropertyType(){
        WebElement propertyTypeWrapper = driver.findElement(By.className("_1hh2h7tb"));
        WebElement propertyTypeInnerSpan = propertyTypeWrapper.findElement(By.cssSelector("span"));

        // Check to see if this a property that matches something we have in our enum, otherwise, we need to account
        // for it in the future.
        String propertyText = propertyTypeInnerSpan.getText();
        for(AirbnbProperty p : AirbnbProperty.values()){
            //contains because it could be "private room in ____"
            if(propertyText.toUpperCase().contains(p.getId().toUpperCase())){
                pageParams.setPropertyType(p);
            }
        }

        //If we didn't find a match, we should have a hard stop for this records since we wouldn't get the complete data
        if(pageParams.getPropertyType() == null){
            throw new RuntimeException("The property type could not be inferred from this page.");
        }
    }

    /*
        We scrape the property name based on the classname and then look for the internal text.
     */
    private void scrapePropertyName(){
        WebElement propertyNameWrapper = driver.findElement(By.className("_1xu9tpch"));

        String propertyName = propertyNameWrapper.getAttribute("textContent");
        if(propertyName != null && propertyName.length() > 0){
            pageParams.setName(propertyName);
        }else{
            throw new RuntimeException("The property name could not be inferred from this page.");
        }
    }

    /*
        We scrape the bedrooms for both the number of bedrooms (could be a half size) as well as the pretty print in order to show "Studio" rather
        than 0 beds.
     */
    private void scrapeBedrooms(){
        WebElement propertyDetailsWrapper = driver.findElement(By.xpath("//div[@class='_36rlri'][2]/div[@class='_qtix31']/div[@class='_1thk0tsb']/span[@class='_fgdupie']"));

        String bedroomDetails = propertyDetailsWrapper.getText();
        if(bedroomDetails != null && bedroomDetails.length() > 0){
            pageParams.setNumBedroomsPretty(bedroomDetails);

            // Do the conversions for doubles as necessary
            if(bedroomDetails.equalsIgnoreCase("studio")){
                pageParams.setNumBedrooms(0.0);
            }else{
                //Cut out the bedrooms, there may be a half bedroom...
                String numBedrooms = bedroomDetails.split(" ")[0];
                Double numBedroomRepresentation = Double.parseDouble(numBedrooms);

                pageParams.setNumBedrooms(numBedroomRepresentation);
            }
        }
    }

    /*
        For bathrooms we have an added constraint that it may be a shared bathroom or a private bathroom. We can keep track
        of this by setting the boolean as necessary based on the Bathroom string.
     */
    private void scrapeBathrooms(){
        WebElement propertyDetailsWrapper = driver.findElement(By.xpath("//div[@class='_36rlri'][4]/div[@class='_qtix31']/div[@class='_1thk0tsb']/span[@class='_fgdupie']"));

        String bathroomDetails = propertyDetailsWrapper.getText();
        if(bathroomDetails != null && bathroomDetails.length() > 0){
            String numBaths = bathroomDetails.split(" ")[0];
            Double numBathsDoubleRepresentation = Double.parseDouble(numBaths);

            pageParams.setNumBathrooms(numBathsDoubleRepresentation);

            // Avoid case issues but converting to uppercase
            boolean isShared = false;
            if(bathroomDetails.toUpperCase().contains("SHARED")){
                isShared = true;
            }

            pageParams.setBathroomShared(isShared);
        }
    }

    /*
        We must treat this differently than the other selections as not all the amenities show up in the source as elements.
        It seems like it is dynamically generated JS from the JSON data which is found in the code. As such it may just
        be easier to parse this JSON. In fact, we probably could have parsed the JSON for the rest of the parameters as well.

        ex.

        listing_amenities : [{
            "category": null,
            "icon": "icon-air-conditioning",
            "id": 5,
            "is_business_ready_feature": false,
            "is_present": false,
            "is_safety_feature": false,
            "name": "Air Conditioning",
            "select_list_view_photo": null,
            "select_tile_view_photo": null,
            "tag": "ac",
            "tooltip": ""
          }...
         ]
     */
    private void scrapeAmenities(){
        String fullSource = driver.getPageSource();
        int listingAmenitiesIndex = fullSource.indexOf("listing_amenities");
        int startOfJson = listingAmenitiesIndex + "listing_amenities".length();
        int endOfJson = startOfJson + fullSource.substring(startOfJson).indexOf("]");

        JsonParser parser = new JsonParser();

        // Account for off by 1/2 errors when dealing with the index to leave the [ and ]
        JsonElement amenitiesElement = parser.parse(fullSource.substring(startOfJson + 2, endOfJson+1));
        JsonArray amenitiesArray = amenitiesElement.getAsJsonArray();

        HashSet<AirbnbAmenities> availableAmenities = new HashSet<AirbnbAmenities>();

        // Check for any amenities which are present, add them as the ENUM into our pageparams object
        for(JsonElement e : amenitiesArray){
            JsonObject amenityObject = e.getAsJsonObject();
            boolean propertyhas = amenityObject.get("is_present").getAsBoolean();
            String name = amenityObject.get("name").getAsString().toUpperCase();

            if(propertyhas){
                for(AirbnbAmenities am : AirbnbAmenities.values()){
                    if(am.getId().toUpperCase().equals(name)){
                        availableAmenities.add(am);
                    }
                }
            }
        }

        pageParams.setAmenities(availableAmenities);
    }

    public AirbnbPage getPageParams(){
        return pageParams;
    }
}
