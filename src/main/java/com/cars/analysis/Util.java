package com.cars.analysis;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Pelumi<pelumi@maven.ai>
 *         Created on 28/06/15 at 19:39.
 */
public class Util {
    private static final Logger logger = Logger.getLogger(Util.class.getName());

    /**
     * Fetch a webpage as a huge string lump given it's URL
     *
     * @param url the web page to be fetched
     * @return the html markup of the page
     */
    protected static String fetchPage(String url) {
        Document doc = null;
        Connection c = null;
        String pageHtml = null;

        try {
            c = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com").ignoreHttpErrors(true).followRedirects(true)
                    .timeout(30 * 1000).ignoreContentType(true);
            doc = c.get();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception in fetching page ", e);
        }
        int statusCode = c.response().statusCode();
        if (statusCode != 200) {
            if (statusCode == 404) {
                logger.log(Level.INFO, url + "Returned error 404: Not Found");
            } else if (statusCode == 403) {
                logger.log(Level.INFO, url + " returned 403: Forbidden");
            } else if (statusCode == -1) {
                logger.log(Level.INFO, url + " is malformed");
            } else {
                logger.log(Level.INFO, url + "Error fetching url at: " + url + ". Status code returned is :" + statusCode);
            }
        }
        if (doc != null)
            pageHtml = doc.html();
        return pageHtml;
    }

    protected static void sleepInSecs(int secs) {
        try {
            Thread.sleep(secs * 1000);
        } catch (InterruptedException e) {
            //be quiet you fool, tell no one about your folly
        }
    }

    /**
     * converts the fields in an object to a list of title strings
     *
     * @param cls the object whose fields should be retrieved
     * @return a string array containing field names as Title phrases
     */
    protected static String[] getFields(Object cls) {
        Class classType = cls.getClass();
        System.out.println(classType.getSimpleName());
        Field[] fields = classType.getDeclaredFields();
        String[] fieldNames = new String[fields.length];

        for (int i = 0; i < fields.length; i++) {
            //split camel case and capitalise words
            fieldNames[i] = WordUtils.capitalizeFully(StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(fields[i].getName()), " "));
            System.out.println(fieldNames[i]);
        }
        return fieldNames;
    }

    /**
     * Takes in a raw list type and writes it fields to CSV.
     * It naively assumes that all the fields are primitive types and implement a proper toString() method.
     *
     * @param items the list of items to be written to csv
     * @return true or false depending on success
     */
    protected static void listToCSV(List items) {
        Class classType = items.get(0).getClass();
        FileWriter fileWriter = null;
        CSVPrinter csvFilePrinter = null;

        //Create the CSVFormat object with "\n" as a record delimiter
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator("\n");
        String[] FILE_HEADER = getFields(items.get(0));
        try {

            //initialize FileWriter object and save file with class name
            fileWriter = new FileWriter(classType.getSimpleName() + ".csv");

            //initialize CSVPrinter object
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);

            //Create CSV file header
            csvFilePrinter.printRecord(FILE_HEADER);

            //Write a new student object list to the CSV file
            for (Object car : items) {
                List<String> dataRecord = new ArrayList();
                //retrieve object fields
                Field[] fields = classType.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    dataRecord.add(field.get(car).toString());
                }
                csvFilePrinter.printRecord(dataRecord);
            }
            logger.info("CSV file was created successfully !!!");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
                csvFilePrinter.close();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error while flushing/closing fileWriter/csvPrinter");
                e.printStackTrace();
            }
        }
    }
}
