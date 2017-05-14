package summativeprogram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StartScreen {

    private static JComboBox dateList;
    private static JComboBox monthList;
    private static JComboBox yearList;
    private static JComboBox daysList;
    private static JComboBox teachersNoField;
    private static JFrame frame;
    private static JPanel panelNorth;
    private static JPanel inputArea;
    private static JPanel dateArea;
    private static JPanel panelCenter;
    private static JPanel btnArea;
    private static JPanel panelSouth;
    private static JPanel southPanelCenter;
    private static JPanel blankPanel;
    private static JButton nextBtn;
    private static JButton createTimeTableBtn;
    private static JButton editBtn;
    private static JButton exitBtn;
    private static JTextField[] awayTeacherFields;
    private static String folderPath = SplashScreen.getFolderPath();

    /*
     * Post-condition: will create a screen to get input from the user
     */
    public static void startScreen() throws IOException {
        // creates a new frame, sets layout to BorderLayout and sets color to white //
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setBackground(Color.white);

        setScreenSize(frame);  // gets screen size and set window size accordingly
        showTitle();        //shows the title

        // creates input screen //
        inputArea = new JPanel(new BorderLayout());
        designCentralPanel();
        drawButtons();

        // Next Button - creates text fields and disables date input area //
        nextBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                try {
                    disableComboBoxes();
                    designSouthPanel();
                    panelSouth.setVisible(true);
                    createTimeTableBtn.setEnabled(true);
                    editBtn.setEnabled(true);
                } catch (IOException ex) {
                    Logger.getLogger(StartScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Create Timetable Button - creates the timetable for the day //
        createTimeTableBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                try {
                    nextBtn.setEnabled(false);      //disables the next button
                    panelSouth.setVisible(false);   //hides the text fields area
                    editBtn.setEnabled(false);      //disable the edit button
                    createTimeTableBtn.setEnabled(false);   //disables the create timetable button

                    createBlankPanel();     // creates a new blank panel

                    //creates the label to prompt user about successful timetable creation //
                    JLabel finishLabel = new JLabel();
                    finishLabel.setText("The timetable named '" +
                            PrintTimetable.getFileName() +
                            "' has been created in the 'Timetables' folder!");
                    finishLabel.setFont(new Font("Serif", Font.PLAIN, 30));
                    finishLabel.setPreferredSize(new Dimension(1100, 200));
                    blankPanel.add(finishLabel);
                    blankPanel.setVisible(true);

                    PrintTimetable.createTimetable();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(StartScreen.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(StartScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Exit Button - exits the program //
        exitBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });

        // Edit Button - allows the user to re-enter the date and the no of teachers away //
        editBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                enableComboBoxes();     //re-enables the combo boxes
                createTimeTableBtn.setEnabled(false);   //disables the create timetable button
                createBlankPanel();     //creates a new blank panel
                panelSouth.setVisible(false);       //hides the text fields area

                int noOfAwayTeachers = getNoOfAwayTeachers();

                // sets the created text fields to null //
                for (int i = 0; i < noOfAwayTeachers; i++) {
                    awayTeacherFields[i].setVisible(false);
                    awayTeacherFields[i] = null;
                }

            }
        });
        frame.setVisible(true);
    }

    /*
     * Post-condition: will create a blank white panel
     */
    public static void createBlankPanel() {
        blankPanel = new JPanel();      //creates a new panel
        blankPanel.setBackground(Color.white);      //sets background to white
        blankPanel.setPreferredSize(new Dimension(1100, 370));
        frame.add(blankPanel, BorderLayout.SOUTH);
    }

    /*
     * Post-condition: will disable the combo boxes
     */
    public static void disableComboBoxes() {
        dateList.setEnabled(false);
        monthList.setEnabled(false);
        yearList.setEnabled(false);
        daysList.setEnabled(false);
        teachersNoField.setEnabled(false);
    }

    /*
     * Post-condition: will enable the combo boxes
     */
    public static void enableComboBoxes() {
        dateList.setEnabled(true);
        monthList.setEnabled(true);
        yearList.setEnabled(true);
        daysList.setEnabled(true);
        teachersNoField.setEnabled(true);
    }

    /*
     * Input: name of frame to be resized
     * Post-condition: will set the frame size to max screen size
     */
    public static void setScreenSize(JFrame x) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();      //gets screen width
        double height = screenSize.getHeight();    //get screen height
        x.setSize((int) width, (int) height);  //sets window size to max
    }

    /*
     * Post-condition: will display the title of the program
     */
    public static void showTitle() throws IOException {
        // Open the title images //
        BufferedImage title1 = ImageIO.read(new File(folderPath + "/Images/title1.png"));
        BufferedImage title2 = ImageIO.read(new File(folderPath + "/Images/title2.png"));
        JLabel titleLabel1 = new JLabel();
        JLabel titleLabel2 = new JLabel();
        titleLabel1.setIcon(new ImageIcon(title1));
        titleLabel2.setIcon(new ImageIcon(title2));

        // Displays the title on the input screen //
        panelNorth = new JPanel();
        panelNorth.add(titleLabel1);
        panelNorth.add(titleLabel2);
        frame.add(panelNorth, BorderLayout.NORTH);
    }

    /*
     * Post-condition: will get the complete date provided by the user
     */
    public static String getDate() throws IOException {
        String completeDate;
        completeDate = daysList.getSelectedItem().toString() + ". "
                + dateList.getSelectedItem().toString()
                + " " + monthList.getSelectedItem().toString()
                + ", " + yearList.getSelectedItem().toString();
        return completeDate;
    }

    /*
     * Post-condition: will get the current day
     */
    public static String getDay() {
        return daysList.getSelectedItem().toString();
    }

    /*
     * Post-condition: will get the no of teachers that are away
     */
    public static int getNoOfAwayTeachers() {
        return teachersNoField.getSelectedIndex() + 1;
    }

    /*
     * Post-condition: will design the panel for getting names of away teachers
     */
    public static void designSouthPanel() throws IOException {
        JPanel southPanelTitle = new JPanel();
        southPanelCenter = new JPanel();

        // creates a label for getting full names of away teachers //
        JLabel teacherNamesLabel = new JLabel();
        teacherNamesLabel.setText("Please Enter Full Names of the Teachers: (Last, First)");
        teacherNamesLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        southPanelTitle.add(teacherNamesLabel);
        panelSouth.add(southPanelTitle, BorderLayout.NORTH);

        createTextFields();     //creates text fields to get names

        panelSouth.add(southPanelCenter, BorderLayout.CENTER);
        panelSouth.revalidate();
        panelSouth.repaint();
    }

    /*
     * Post-condition: will create the input area of the input screen
     */
    public static void designCentralPanel() {
        dateArea = new JPanel(new FlowLayout());    //creates a panel for date input

        // displays a label to get the date //
        JLabel dateLabel = new JLabel();
        dateLabel.setText("Please enter the date for teacher absences:");
        dateLabel.setFont(new Font("Serif", Font.PLAIN, 35));
        dateArea.add(dateLabel);

        // for the date combo box
        String[] dates = new String[31];
        for (int i = 1; i <= 31; i++) {
            dates[i - 1] = Integer.toString(i);
        }
        //for the month combo box
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
            "Aug", "Sept", "Oct", "Nov", "Dec"};
        String[] years = {"2016", "2017", "2018", "2019", "2020"};  //for the year combo box
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri"};    //for the day combo box

        //creates combo boxes to get full date //
        dateList = new JComboBox(dates);
        monthList = new JComboBox(months);
        yearList = new JComboBox(years);
        daysList = new JComboBox(days);

        // adjusts the size //
        dateList.setPreferredSize(new Dimension(70, 80));
        monthList.setPreferredSize(new Dimension(80, 80));
        yearList.setPreferredSize(new Dimension(100, 80));
        daysList.setPreferredSize(new Dimension(100, 80));

        // sets the font //
        dateList.setFont(new Font("Serif", Font.PLAIN, 18));
        monthList.setFont(new Font("Serif", Font.PLAIN, 18));
        yearList.setFont(new Font("Serif", Font.PLAIN, 18));
        daysList.setFont(new Font("Serif", Font.PLAIN, 18));

        // adds the combo boxes to the panel //
        dateArea.add(daysList);
        dateArea.add(dateList);
        dateArea.add(monthList);
        dateArea.add(yearList);

        // adds the panel to the frame //
        inputArea.add(dateArea, BorderLayout.NORTH);
        frame.add(inputArea, BorderLayout.CENTER);

        /** For center of the input area. **/
        panelCenter = new JPanel(new FlowLayout()); // creates a new panel

        // for the no of teachers away combo box
        String[] teacherNumbers = new String[15];
        for (int i = 1; i <= 15; i++) {
            teacherNumbers[i - 1] = Integer.toString(i);
        }

        // creates a label to get the no of away teachers //
        JLabel teachersNoLabel = new JLabel();
        teachersNoLabel.setText("How many teachers are away?");
        teachersNoLabel.setFont(new Font("Serif", Font.PLAIN, 35));
        panelCenter.add(teachersNoLabel);

        // creates a combo box to get no of teachers away //
        teachersNoField = new JComboBox(teacherNumbers);
        teachersNoField.setPreferredSize(new Dimension(70, 40));
        teachersNoField.setFont(new Font("Serif", Font.PLAIN, 18));
        panelCenter.add(teachersNoField);

        inputArea.add(panelCenter, BorderLayout.CENTER);
    }

    /*
     * Post-condition: will createthe next, edit, create timetable, and
     * exit buttons
     */
    public static void drawButtons() {
        btnArea = new JPanel(new FlowLayout());     //creates a panel for buttons area

        // creates the next button //
        nextBtn = new JButton("Next");
        nextBtn.setPreferredSize(new Dimension(300, 70));
        nextBtn.setFont(new Font("Serif", Font.PLAIN, 18));
        btnArea.add(nextBtn);

        // creates the create timetable button //
        createTimeTableBtn = new JButton("Create Timetable");
        createTimeTableBtn.setPreferredSize(new Dimension(300, 70));
        createTimeTableBtn.setFont(new Font("Serif", Font.PLAIN, 18));
        createTimeTableBtn.setEnabled(false);
        btnArea.add(createTimeTableBtn);

        // creates the edit button //
        editBtn = new JButton("Edit");
        editBtn.setPreferredSize(new Dimension(300, 70));
        editBtn.setFont(new Font("Serif", Font.PLAIN, 18));
        editBtn.setEnabled(false);
        btnArea.add(editBtn);

        //creates the exit button //
        exitBtn = new JButton("Exit");
        exitBtn.setPreferredSize(new Dimension(300, 70));
        exitBtn.setFont(new Font("Serif", Font.PLAIN, 18));
        exitBtn.setEnabled(true);
        btnArea.add(exitBtn);
        inputArea.add(btnArea, BorderLayout.SOUTH);

        // creates a panel for teachers names text fields //
        panelSouth = new JPanel(new BorderLayout());
        panelSouth.setPreferredSize(new Dimension(1100, 370));
        frame.add(panelSouth, BorderLayout.SOUTH);
    }

    /*
     * Post-condition: will create text fields for getting away teacher names
     */
    public static void createTextFields() {
        int noOfAwayTeachers = getNoOfAwayTeachers();

        awayTeacherFields = new JTextField[noOfAwayTeachers];

        // creates text fields to get the names of teachers away //
        for (int i = 0; i < noOfAwayTeachers; i++) {
            JLabel counter = new JLabel();
            counter.setText(Integer.toString(i + 1));
            southPanelCenter.add(counter);

            awayTeacherFields[i] = new JTextField();
            awayTeacherFields[i].setPreferredSize(new Dimension(350, 40));
            southPanelCenter.add(awayTeacherFields[i]);
        }
    }

    /*
     * Post-condition: will obtain the name of the teachers that are away
     */
    public static String[] getAwayTeacherNames() {
        int noOfAwayTeachers = getNoOfAwayTeachers();
        String[] awayTeachersNames = new String[noOfAwayTeachers];

        // reads names of the teachers that are away //
        for (int i = 0; i < noOfAwayTeachers; i++) {
            awayTeachersNames[i] = awayTeacherFields[i].getText();
        }
        return awayTeachersNames;
    }
}
