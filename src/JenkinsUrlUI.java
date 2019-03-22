import com.jtool.bl.OnOperationSuccess;
import com.jtool.bl.ReportsGenerator;
import com.jtool.utils.Constants;
import javafx.application.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by bhushan.raut on 9/23/2016.
 */
public class JenkinsUrlUI extends JFrame implements OnOperationSuccess {
    private JPanel panel;
    private JButton fileLocationBtn;
    private JButton startBtn;
    private JEditorPane urlEditorPanel;
    private static String urls;
    private static String filePath = "";
    private static JFrame frame;
    private int width = 800;
    private int height = 350;

    /**
     * Constructor
     */
    public JenkinsUrlUI() {

        OnOperationSuccess onOperationSuccess = this;
        //start processing of urls
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                urls = urlEditorPanel.getText();
                //check conditions
                //can also check format for sile location and urls
                if (!urls.isEmpty() && !filePath.isEmpty()) {
                    ReportsGenerator reportsGenerator = new ReportsGenerator();
                    //generate list
                    String[] lines = urls.split("\\n?\\r");
                    ArrayList<String> urlList = new ArrayList<>();
                    for (int i = 0; i < lines.length; i++) {
                        urlList.add(lines[0].trim() + Constants.REPORT_HTML);
                    }
                    //start processing
                    try {
                        reportsGenerator.generateReport(onOperationSuccess, filePath + "\\", urlList);
                    } catch (IOException | IllegalArgumentException ioe) {
                        showFailureMessage();
                        ioe.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, Constants.WARNING_MESSAGE);
                }
                startBtn.disable();
            }
        });

        fileLocationBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //if else check for null filePath
                JFileChooser f = new JFileChooser();
                f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                f.showSaveDialog(null);
                filePath = f.getSelectedFile().toString();
                fileLocationBtn.disable();
            }
        });
    }

    /**
     * Shows failure Alert message
     */
    private void showFailureMessage() {
        JOptionPane.showMessageDialog(frame, Constants.ERROR_MESSAGE);
    }


    /**
     * UI Tool Driver
     *
     * @param args
     */
    public static void main(String[] args) {
        frame = new JFrame(Constants.TOOL_NAME);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JenkinsUrlUI jenkinsUrlUI = new JenkinsUrlUI();
        frame.setContentPane(jenkinsUrlUI.panel);
        frame.setPreferredSize(new Dimension(jenkinsUrlUI.width, jenkinsUrlUI.height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /*JScrollPane scroll = new JScrollPane(jenkinsUrlUI.urlEditorPanel);
        jenkinsUrlUI.getContentPane().add(scroll);*/
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void fileWriteSuccessful() {
        JOptionPane.showMessageDialog(frame, Constants.SUCCESS_MESSAGE);
    }
}
