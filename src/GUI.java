import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GUI {
    JFrame frame;
    JSplitPane splitPane;
    DefaultTableModel theoryModel, labModel;
    JPanel menuButtonsPanel, contentPanel;
    JLabel welcomeLabel, theoryCoursePrompt ,labCoursePrompt;
    JTextField textField;
    JButton labCourseButton, theoryCourseButton, showCoursesButton, nextButton, whatIfButton;

    // Colour palette
    Color powderBlue = new Color(174, 197, 235);
    Color twilightIndigo = new Color(58, 64, 90);
    Color powderPetal = new Color(249, 222, 201);
    Color ashBrown = new Color(104, 80, 68);

    void guiStart() {
        // Instances to use methods.
        AddingCoursesGUI addingCoursesGUI = new AddingCoursesGUI(this);
        DisplayingCoursesGUI displayingCoursesGUI = new DisplayingCoursesGUI(this);
        SimulatingCoursesGUI simulatingCoursesGUI = new  SimulatingCoursesGUI(this);

        frame = new JFrame("Degree Progress Auditor");

        // Panel initialisation
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        menuButtonsPanel = new JPanel();
        contentPanel = new JPanel();
        contentPanel.setForeground(powderPetal);
        welcomeLabel = new JLabel("Welcome to the Degree Progress Auditor!", SwingConstants.CENTER);
        welcomeLabel.setForeground(ashBrown);

        // Button initialisation
        theoryCourseButton = new JButton("Add New Theory Course");
        labCourseButton = new JButton("Add New Lab Course");
        showCoursesButton = new JButton("Show Courses");
        whatIfButton = new JButton("What-If?");

        // Button colours
        theoryCourseButton.setBackground(twilightIndigo);
        labCourseButton.setBackground(twilightIndigo);
        showCoursesButton.setBackground(twilightIndigo);
        whatIfButton.setBackground(twilightIndigo);

        theoryCourseButton.setForeground(powderBlue);
        labCourseButton.setForeground(powderBlue);
        showCoursesButton.setForeground(powderBlue);
        whatIfButton.setForeground(powderBlue);

        // Making models for the tables in the display method.
        String[] theoryColumns = {"ID","Course Name","Credit Hours","Attendance","Sessionals",
                "Midterm Exam","Final Exam","Total Score","Grade Points"};
        theoryModel = new DefaultTableModel(theoryColumns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] labColumns = {"ID","Course Name","Credit Hours","Attendance","Lab Manual",
                "Project","Lab Exam","Total Score","Grade Points"};
        labModel = new DefaultTableModel(labColumns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        frame.setLayout(new BorderLayout());
        frame.setSize(800, 700);

        splitPane.setOneTouchExpandable(true);

        menuButtonsPanel.setLayout(new GridLayout(1,4));
        menuButtonsPanel.setPreferredSize(new Dimension(30, 50));

        menuButtonsPanel.add(theoryCourseButton);
        menuButtonsPanel.add(labCourseButton);
        menuButtonsPanel.add(showCoursesButton);
        menuButtonsPanel.add(whatIfButton);

        theoryCourseButton.setMnemonic(KeyEvent.VK_T);
        labCourseButton.setMnemonic(KeyEvent.VK_L);
        showCoursesButton.setMnemonic(KeyEvent.VK_S);
        whatIfButton.setMnemonic(KeyEvent.VK_W);

        setHandCursor(theoryCourseButton);
        setHandCursor(labCourseButton);
        setHandCursor(showCoursesButton);
        setHandCursor(whatIfButton);

        contentPanel.setPreferredSize(new Dimension(1000,600));
        contentPanel.setLayout(new BorderLayout(10,10));
        contentPanel.add(welcomeLabel, BorderLayout.CENTER);

        welcomeLabel.setOpaque(true);

        splitPane.setLeftComponent(menuButtonsPanel);
        splitPane.setRightComponent(contentPanel);
        frame.add(splitPane, BorderLayout.CENTER);

        // The theory course button calls the method to add a new theory course.
        theoryCourseButton.addActionListener(_ -> addingCoursesGUI.addTheoryCourse());

        // The lab course button calls the method to add a new lab course.
        labCourseButton.addActionListener(_ -> addingCoursesGUI.addLabCourse());

        // The show course button shows you added courses.
        showCoursesButton.addActionListener(_ -> displayingCoursesGUI.displayCourses());

        // The what-if button simulates a hypothetical GPA.
        whatIfButton.addActionListener(_ -> simulatingCoursesGUI.simulateCourses());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // A helper method to make every button have a hand cursor.
    private void setHandCursor(JButton button) {
        button.setCursor(new Cursor (Cursor.HAND_CURSOR));
    }

}

