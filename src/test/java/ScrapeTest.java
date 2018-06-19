import common.AirbnbAmenities;
import common.AirbnbProperty;
import model.AirbnbPage;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.NoSuchElementException;

import static junit.framework.TestCase.fail;

public class ScrapeTest {
    @Test
    public void scrapeKnownBnB(){
        String URL = "https://www.airbnb.co.uk/rooms/19278160?s=51";

        AirBnbScraper scrape = new AirBnbScraper(URL);
        scrape.scrapeSite();
        AirbnbPage page = scrape.getPageParams();

        Assert.assertEquals("https://www.airbnb.co.uk/rooms/19278160?s=51", page.getURL());
        Assert.assertEquals("York Place: Luxurious  apartment For Two adults.", page.getName());
        Assert.assertEquals(AirbnbProperty.APARTMENT, page.getPropertyType());
        Assert.assertEquals("1 bedroom", page.getNumBedroomsPretty());
        Assert.assertEquals(new Double(1.0), page.getNumBedrooms());
        Assert.assertEquals(new Double(1.0), page.getNumBathrooms());
        Assert.assertEquals(false, page.isBathroomShared());

        HashSet<AirbnbAmenities> set = new HashSet<AirbnbAmenities>();
        set.add(AirbnbAmenities.CO2);
        set.add(AirbnbAmenities.PRIVATE_ENTRANCE);
        set.add(AirbnbAmenities.SHAMPOO);
        set.add(AirbnbAmenities.HEATING);
        set.add(AirbnbAmenities.HOT_WATER);
        set.add(AirbnbAmenities.WORKSPACE);
        set.add(AirbnbAmenities.SMOKE_DETECTOR);
        set.add(AirbnbAmenities.TV);
        set.add(AirbnbAmenities.IRON);
        set.add(AirbnbAmenities.WASHER);
        set.add(AirbnbAmenities.WIFI);
        set.add(AirbnbAmenities.HANGERS);
        set.add(AirbnbAmenities.ESSENTIALS);
        set.add(AirbnbAmenities.LOCKBOX);
        set.add(AirbnbAmenities.BED_LINENS);
        set.add(AirbnbAmenities.KITCHEN);
        set.add(AirbnbAmenities.HAIR_DRYER);
        set.add(AirbnbAmenities.PAID_PARKING);

        Assert.assertEquals(set, page.getAmenities());
    }

    @Test
    public void scrapeSomeExamples(){
        String URL = "https://www.airbnb.co.uk/rooms/14531512?s=51";
        AirBnbScraper scrape = new AirBnbScraper(URL);
        scrape.scrapeSite();
        AirbnbPage page = scrape.getPageParams();

        Assert.assertNotNull(page);

        URL = "https://www.airbnb.co.uk/rooms/13074457?location=Edinburgh&s=4GGaMJUd";
        scrape = new AirBnbScraper(URL);
        scrape.scrapeSite();
        page = scrape.getPageParams();

        Assert.assertNotNull(page);

        URL = "https://www.airbnb.co.uk/rooms/12296258?location=Edinburgh%2C%20United%20Kingdom";
        scrape = new AirBnbScraper(URL);
        scrape.scrapeSite();
        page = scrape.getPageParams();

        Assert.assertNotNull(page);

        URL = "https://www.airbnb.co.uk/rooms/19329960?location=Edinburgh%2C%20United%20Kingdom&s=QCklE5pG";
        scrape = new AirBnbScraper(URL);
        scrape.scrapeSite();
        page = scrape.getPageParams();

        Assert.assertNotNull(page);
    }

    @Test
    public void scrapenonBnB(){
        String URL = "https://google.ca";
        try{
            AirBnbScraper scrape = new AirBnbScraper(URL);
            scrape.scrapeSite();
            fail();
        }catch(Exception e){
            Assert.assertTrue(e.getMessage().contains("Cannot scrape non-airbnb sites."));
        }
    }

    @Test
    public void scrapeBnBNoLongerAvailable(){
        String URL = "https://www.airbnb.co.uk/rooms/19292873?s=51";
        try{
            AirBnbScraper scrape = new AirBnbScraper(URL);
            scrape.scrapeSite();
            fail();
        }catch(Exception e){
            Assert.assertTrue(e instanceof org.openqa.selenium.NoSuchElementException);
        }

    }
}
