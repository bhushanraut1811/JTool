package com.jtool.service;

import com.jtool.model.Report;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bhushan.raut on 9/21/2016.
 */
public class JenkinsParser {

    //handle skip if error while parsing !!!done!!!
    private ArrayList<Report> mReportList = new ArrayList<>();

    /**
     * Generates report model list by parsing specified urls
     *
     * @param urlList
     * @return
     */
    public ArrayList<Report> getReportList(ArrayList<String> urlList) {
        for (String link : urlList) {
            try {
                Document doc = Jsoup.connect(link).get();
                Elements tableElements = doc.select("table");
                Elements tableRowElements = tableElements.select(":not(thead) tr");
                String status = "";
                for (int i = 4; i < tableRowElements.size() - 1; i++) {
                    Report report = new Report();
                    Element row = tableRowElements.get(i);
                    String urlDetails = null;
                    Elements rowItems = row.select("td");
                    for (int j = 0; j < rowItems.size(); j++) {

                        if (j == 0) {
                            String st = rowItems.get(j).text();
                            String[] split = st.split(":");
                            report.setmId(split[0]);
                            if (split.length > 1)
                                report.setmTestName(split[1]);
                            //System.out.println(split[1]);
                            urlDetails = link.substring(0, link.length() - 21) + rowItems.get(j).getElementsByTag("a").get(0).attr("href");
                            report.setmReportUrl(urlDetails.trim());
                        }
                        String time = "";
                        if (j == 12) {
                            if (!rowItems.get(j - 1).text().equals("000ms")) {
                                String st = rowItems.get(j).text();
                                report.setmStatus(st);
                                System.out.println(st);
                            } else {
                                report.setmStatus("Skipped");
                                System.out.println("Skipped");
                            }
                        }
                    }
                    //adding possible failures reasons
                    if (report.getmStatus().equalsIgnoreCase("Failed")) {
                        String failureReasons = fetchDetailsReportURL(urlDetails);
                        report.setmFailureReasons(failureReasons);
                    }
                    //can change
                    if (!report.getmId().equalsIgnoreCase("Change the language of the device.")) {
                        mReportList.add(report);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mReportList;
    }

    /**
     * Fetching error message for particular ID
     *
     * @param urlDetails
     * @return
     */
    private String fetchDetailsReportURL(String urlDetails) {
        String errors = "";
        try {
            Document doc = Jsoup.connect(urlDetails).get();

            Elements elements = doc.getElementsByClass("message");
            for (Element element : elements) {
                errors = errors + element.getElementsByClass("collapse").text().substring(0, 100) + " , ";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return errors;
    }

}
