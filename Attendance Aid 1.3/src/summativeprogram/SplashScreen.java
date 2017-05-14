package summativeprogram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.net.*;
import javax.imageio.ImageIO;

public class SplashScreen {

    public static JWindow window;
    public static double width;
    public static double height;
    public static JLabel titleLabel1;
    public static JLabel titleLabel2;
    public static JLabel blankLabel;
    public static JButton startBtn;
    public static String folderPath;

    /*
     * Pre-condition: none
     * Post-condition: will display the welcome screen with the start button
     */
    public static void welcomeScreen() throws MalformedURLException, IOException {
        window = new JWindow();             //create new window
        startBtn = new JButton("Start");    //create start button

        // set screen size and display title //
        setScreenSize(window);
        openTitleImages();

        // set layout to BorderLayout //
        BorderLayout layout = new BorderLayout();
        window.setLayout(layout);

        // Create green frame for welcome screen //
        createNorthPanel();
        createEastPanel();
        createWestPanel();
        createSouthPanel();
        createCenterPanel();

        window.setVisible(true);

        // show input screen when start button selected //
        startBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                try {
                    StartScreen.startScreen();
                    window.setVisible(false);
                    window.dispose();
                } catch (IOException ex) {
                    Logger.getLogger(SplashScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    /*
     * Input: name of window to be resized
     * Post-condition: will set the window size to max screen size
     */
    public static void setScreenSize(JWindow x) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = screenSize.getWidth();      //gets screen width
        height = screenSize.getHeight();    //get screen height
        x.setSize((int) width, (int) height);  //sets window size to max
    }

    /*
     * Pre-condition: none
     * Post-condition: will create JLabels and set their icons to images used
     * for the title
     */
    public static void openTitleImages() throws IOException {
        // obtains the file path to the program folder
        folderPath = new File("Attendance Aid 1.3").getAbsolutePath();
        folderPath = folderPath.substring(0, folderPath.length()-19);


        // opens images to be used for the title//
        BufferedImage title1 = ImageIO.read(new File(folderPath + "/Images/title1.png"));
        BufferedImage title2 = ImageIO.read(new File(folderPath + "/Images/title2.png"));
        BufferedImage blank = ImageIO.read(new File(folderPath + "/Images/blank.jpg"));
        ImageIcon titleIcon1 = new ImageIcon(title1);
        ImageIcon titleIcon2 = new ImageIcon(title2);
        ImageIcon blankIcon = new ImageIcon(blank);
        titleLabel1 = new JLabel();
        titleLabel2 = new JLabel();
        blankLabel = new JLabel();
        titleLabel1.setIcon(titleIcon1);
        titleLabel2.setIcon(titleIcon2);
        blankLabel.setIcon(blankIcon);
    }

    /*
     * Post-condition: will return full path of the program folder
     */
    public static String getFolderPath() {
        return folderPath;
    }

    /*
     * Pre-condition: none
     * Post-condition: will create the top part of the green bordering panel on
     * the screen
     * The next three procedures do the same function for right, left, and top
     * parts of the panel.
     */
    private static void createNorthPanel() {
        // creates panel for the top and sets color to green //
        JPanel panelNorth = new JPanel();
        panelNorth.setBackground(Color.green);
        panelNorth.setPreferredSize(new Dimension((int) width, (int) (height / 3.25)));
        window.add(panelNorth, BorderLayout.NORTH);
    }

    private static void createEastPanel() {
        // creates the right side panel and sets color to green //
        JPanel panelEast = new JPanel();
        panelEast.setBackground(Color.green);
        panelEast.setPreferredSize(new Dimension((int) width / 10, (int) height));
        window.add(panelEast, BorderLayout.EAST);
    }

    private static void createWestPanel() {
        //creates left side panel and sets color to green //
        JPanel panelWest = new JPanel();
        panelWest.setBackground(Color.green);
        panelWest.setPreferredSize(new Dimension((int) width / 10, (int) height));
        window.add(panelWest, BorderLayout.WEST);
    }

    private static void createSouthPanel() {
        // creates the bottom panel and sets color to green //
        JPanel panelSouth = new JPanel();
        panelSouth.setBackground(Color.green);
        panelSouth.setPreferredSize(new Dimension((int) width, (int) (height / 3.25)));
        window.add(panelSouth, BorderLayout.SOUTH);
    }

    /*
     * Pre-condition: none
     * Post-condition: will display the center panel with the title images and
     * the start button
     */
    private static void createCenterPanel() {
        //creates center panel and sets background to white //
        JPanel panelCenter = new JPanel();
        panelCenter.setBackground(Color.white);
        panelCenter.setPreferredSize(new Dimension((int) (width / 2), (int) (height / 3)));

        // adds the title images to the panel //
        panelCenter.add(titleLabel1);
        panelCenter.add(titleLabel2);
        panelCenter.add(blankLabel);

        // adds the start button to the panel //
        startBtn.setPreferredSize(new Dimension(200, 100));
        startBtn.setFont(new Font("Calibri", Font.PLAIN, 25));
        panelCenter.add(startBtn);
        panelCenter.setBorder(BorderFactory.createLineBorder(Color.black));  //creates a border for the center panel

        window.add(panelCenter, BorderLayout.CENTER);   //adds the panel to the window
    }
}
