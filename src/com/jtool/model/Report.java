package com.jtool.model;

import java.util.ArrayList;

/**
 * Model contains data for each Test Id to be written on file
 */
public class Report {
    private String mId;
    private String mStatus;
    private String mTestName;
    private String mConsoleLogUrl;
    private String mFailureReasons;

    public String getmTestName() {
        return mTestName;
    }

    public void setmTestName(String mTestName) {
        this.mTestName = mTestName;
    }

    private String mReportUrl;

    public String getmFailureReasons() {
        return mFailureReasons;
    }

    public void setmFailureReasons(String mFailureReasons) {
        this.mFailureReasons = mFailureReasons;
    }


    public Report() {
    }

    public Report(String mId, String mStatus, String mReportUrl, String mConsoleLogUrl, ArrayList<String> mFailureReasonsList) {
        this.mId = mId;
        this.mStatus = mStatus;
        this.mFailureReasons = "";
        // this.mReportUrl = mReportUrl;
        //this.mConsoleLogUrl = mConsoleLogUrl;
        // this.mFailureReasonsList = mFailureReasonsList;
    }

    public String getmReportUrl() {
        return mReportUrl;
    }

    public void setmReportUrl(String mReportUrl) {
        this.mReportUrl = mReportUrl;
    }


    public String getmConsoleLogUrl() {
        return mConsoleLogUrl;
    }

    public void setmConsoleLogUrl(String mConsoleLogUrl) {
        this.mConsoleLogUrl = mConsoleLogUrl;
    }


    public void setmId(String mId) {
        this.mId = mId;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getmId() {
        return mId;
    }

    public String getmStatus() {
        return mStatus;
    }
}

