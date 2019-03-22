package com.jtool;

import com.jtool.bl.OnOperationSuccess;
import com.jtool.bl.ReportsGenerator;
import com.jtool.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class JToolDriver implements OnOperationSuccess {

    private static ArrayList<String> sJenkinsLinksList;
    private static String sFilePath;

    /**
     * Non UI Tool Driver
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) {
        JToolDriver driver = new JToolDriver();
        try {
            driver.run();
        } catch (IOException |IllegalArgumentException e){
            System.out.println("Exception IO");
            e.printStackTrace();
        }
    }

    private void run() throws IOException {
        //get url from UI and get file storage path
        sJenkinsLinksList = new ArrayList<>();
        sFilePath = Constants.FILE_PATH;
        sJenkinsLinksList.addAll(Arrays.asList(Constants.JENKINS_BUILD_URL));
        ReportsGenerator reportsGenerator = new ReportsGenerator();
        reportsGenerator.generateReport(this, sFilePath, sJenkinsLinksList);
    }

    @Override
    public void fileWriteSuccessful() {
        System.out.println(Constants.SUCCESS_MESSAGE);
    }
}
