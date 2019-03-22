package com.jtool.bl;

import com.jtool.model.Report;
import com.jtool.service.JenkinsParser;
import com.jtool.utils.Constants;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.SortedSet;
import java.util.TreeMap;

/*import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;*/

/**
 * Created by bhushan.raut on 9/21/2016.
 */
public class ReportsGenerator {

    private JenkinsParser mParser;
    private ArrayList<Report> mReportList;
    private TreeMap<String, ArrayList<Report>> mReportsHashMap;
    private OnOperationSuccess onOperationSuccess;

    public void generateReport(OnOperationSuccess onOperationSuccess, String filePath, ArrayList<String> urlList) throws IOException {
        this.onOperationSuccess = onOperationSuccess;
        mParser = new JenkinsParser();
        mReportList = mParser.getReportList(urlList);
        mReportsHashMap = new TreeMap<String, ArrayList<Report>>() {
        };

        for (Report rep : mReportList) {
            if (!mReportsHashMap.containsKey(rep.getmStatus())) {
                mReportsHashMap.put(rep.getmStatus(), new ArrayList<>());
            }
            mReportsHashMap.get(rep.getmStatus()).add(rep);
        }
        //writing to excel file at specified location
        writeExcel(filePath);

    }

    /**
     * Writes Report model to excel file
     *
     * @param report
     * @param row
     * @param wb
     */
    private void writeBook(Report report, Row row, Workbook wb) {

        Cell cell = row.createCell(0);
        cell.setCellValue(report.getmId());

        cell = row.createCell(1);
        cell.setCellValue(report.getmTestName());

        cell = row.createCell(2);
        cell.setCellValue(report.getmStatus());

        cell = row.createCell(3);
        cell.setCellValue(report.getmFailureReasons());

        //report URL
        XSSFCreationHelper helper = (XSSFCreationHelper) wb.getCreationHelper();
        XSSFHyperlink reportUrlLink = helper.createHyperlink(Hyperlink.LINK_URL);
        //HSSFHyperlink reportUrlLink = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
        reportUrlLink.setTooltip("Click to see more");
        reportUrlLink.setAddress(report.getmReportUrl());

        cell = row.createCell(4);
        cell.setCellValue(report.getmReportUrl());
        cell.setHyperlink(reportUrlLink);

/*
        //console URL
        HSSFHyperlink consoleUrlLink = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
        consoleUrlLink.setAddress(report.getmConsoleLogUrl());
        cell = row.createCell(5);
        cell.setCellValue("See Console Log");
        cell.setHyperlink(consoleUrlLink);*/


    }

    /**
     * Creates excel file and writes data to it
     *
     * @param excelFilePath
     * @throws IOException
     */
    public void writeExcel(String excelFilePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        int rowCount = 0;
        Row row = sheet.createRow(rowCount);
        createHeaders(row);
        ArrayList<Report> list = null;
        SortedSet<String> keys = (SortedSet<String>) mReportsHashMap.keySet();

        for (String key : keys) {

            list = mReportsHashMap.get(key);
            //create a function for it
            for (int index = 0; index < list.size(); index++) {
                if (index % 2 == 0) {
                    Report report = list.get(index);
                    row = sheet.createRow(++rowCount);
                    writeBook(report, row, workbook);
                }
            }
            list.clear();
        }
        //checking file exist already or not//used for naming files
        File file = null;
        //saving file on basis on time stamp
        Calendar calendar = Calendar.getInstance();
        file = new File(excelFilePath + Constants.FILENAME + "_" + calendar.getTimeInMillis() + Constants.FILE_EXTENSION);
        //Write the workbook in file system // excelFilePath + "\\" + fName
        FileOutputStream out = new FileOutputStream(file);
        workbook.write(out);
        out.close();
        //call back on UI to show success/failure
        onOperationSuccess.fileWriteSuccessful();
    }

    /**
     * writes headers to excel file
     *
     * @param row
     */
    private void createHeaders(Row row) {
        Cell cell = row.createCell(0);
        cell.setCellValue("ID");

        cell = row.createCell(1);
        cell.setCellValue("TEST NAME");

        cell = row.createCell(2);
        cell.setCellValue("STATUS");

        cell = row.createCell(3);
        cell.setCellValue("REASONS");

        cell = row.createCell(4);
        cell.setCellValue("REPORT URL");
/*
        cell = row.createCell(5);
        cell.setCellValue("CONSOLE URL");*/
    }

}
