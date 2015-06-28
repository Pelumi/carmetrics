package com.cars.analysis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pelumi<pelumi@maven.ai>
 *         Created on 28/06/15 at 18:20.
 *
 * Scrapes information about a car from a bunch of pages on auto trader for further analysis
 */
public class AutoTrader {
    private static final int TOTAL_PAGES = 82;
    private static final String AUTO_TRADER_SEED_URL = "http://www.autotrader.co.uk/search/used/cars/volkswagen/passat/postcode/bs320dx/radius/1501/price-from/0/sort/priceasc/channel/cars/maximum-mileage/up_to_30000_miles/onesearchad/used%2Cnearlynew%2Cnew/page/";

    public static void main(String[] args) {
        List<Car> allCars = new ArrayList<Car>();
        AutoTrader autoTrader = new AutoTrader();
        for (int i = 0; i < TOTAL_PAGES; i++) {
            //loop through pages
            String pageDoc = Util.fetchPage(AUTO_TRADER_SEED_URL + i);
            //if page fetch fails, continue to next page
            if (pageDoc == null) {
                continue;
            }

            Document doc = Jsoup.parse(pageDoc);
            Elements elements = doc.select("li.search-page__result");
            for (Element carElement : elements) {
                Car car = autoTrader.scrapeCardetails(carElement);
                if (car != null) {
                    allCars.add(car);
                }
            }
        }
        //persist cars to csv
        Util.listToCSV(allCars);
    }

    private Car scrapeCardetails(Element carEle) {
        Element titleEle = carEle.select("h1.search-result__title").first();
        if (titleEle != null) {
            String name = titleEle.text();
            String url = titleEle.select("a").attr("href");
            String price = carEle.select("div.search-result__price").first().text();
            String mileage = null;
            String tankCap = null;
            String gearType = null;
            String fuelType = null;
            String year = null;

            Elements attributesEle = carEle.select("ul.search-result__attributes").first().select("li");
            int total = attributesEle.size();
            int count = 0;
            for (Element attribute : attributesEle) {
                count++;
                String attr = attribute.text();
                if (attr.startsWith("20") && year == null) {
                    year = attr;
                } else if (attr.contains("miles")) {
                    mileage = attr;
                } else if (attr.endsWith("L")) {
                    tankCap = attr;
                } else if (attr.contains("Automatic") || attr.contains("Manual")) {
                    gearType = attr;
                } else if (count == total) {
                    fuelType = attr;
                }
            }

            return (new Car(name, url, price, mileage, tankCap, gearType, fuelType, year));

        } else return null;
    }
}
