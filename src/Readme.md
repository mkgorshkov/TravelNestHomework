## Running Instructions
In order to run the project properly, the chromedriver and path to the chromedriver must be properly set:
1) In `AirBnbScraper.java` modify `System.setProperty("webdriver.chrome.driver","resources\\chromedriver.exe");` to select the appropriate driver based on the OS.
2) In this area you may also need to modify naming conventions of paths

## Considerations
- In a real scenario, the unit tests would have move coverage and be more extensive
- Ideally we want a more decoupled system such that if an amenity or a property type gets added we need less maintenance
- We want to cache or otherwise store the results into a DB for easier retrieving or if AirBNB in unavailable, potentially have a
strategy such that we only scrape content which is new, or on a schedule
