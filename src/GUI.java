import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class GUI {
    JFrame frame;
    JColorChooser colorChooser;
    JSplitPane splitPane;
    JPanel sidePanel, contentPanel;
    JLabel welcomeLabel, theoryCoursePrompt ,labCoursePrompt, courseContainer;
    JTextField textField;
    JButton labCourseButton, labBackButton, theoryCourseButton, theoryBackButton, showCoursesButton, showCoursesBackButton, nextButton, zafarButton;

    void guiStart() {
//        colorChooser = new JColorChooser(); Could be addded to a settings tab.
        frame = new JFrame("Degree Progress Auditor");
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        sidePanel = new JPanel();
        contentPanel = new JPanel();
        welcomeLabel = new JLabel("Welcome to the Degree Progress Auditor!");
        theoryCourseButton = new JButton("Add New Theory Course");
        labCourseButton = new JButton("Add New Lab Course");
        showCoursesButton = new JButton("Show Courses");
        zafarButton = new JButton("Zafar");

        frame.setLayout(new BorderLayout());
        frame.setSize(1000, 700);

//        splitPane.setDividerLocation(0.2);
//        splitPane.setResizeWeight(0.05);
        splitPane.setOneTouchExpandable(true);

        sidePanel.setLayout(new GridLayout(1,4));
        sidePanel.setSize(100, 600);

        theoryCourseButton.setSize(30, 40);
        labCourseButton.setSize(30, 40);
        showCoursesButton.setSize(30, 40);

        sidePanel.add(BorderLayout.WEST,theoryCourseButton);
        sidePanel.add(BorderLayout.WEST,labCourseButton);
        sidePanel.add(BorderLayout.WEST,showCoursesButton);
        sidePanel.add(BorderLayout.WEST,zafarButton);
//        sidePanel.add(BorderLayout.EAST,colorChooser);

        contentPanel.setSize(1000,600);
        contentPanel.setLayout(new FlowLayout());
        contentPanel.add(welcomeLabel);

        splitPane.setLeftComponent(sidePanel);
        splitPane.setRightComponent(contentPanel);
        frame.add(BorderLayout.CENTER,splitPane);

        // The theory course button calls the method to add a new theory course.
        theoryCourseButton.addActionListener(e -> addTheoryCourse());
        Semester sem = new Semester();
        zafarButton.addActionListener(e -> sem.printSemester());

        // The lab course button calls the method to add a new lab course.
        labCourseButton.addActionListener(e -> addLabCourse());

        // The show course button shows you added courses.
        showCoursesButton.addActionListener(e -> displayCourses());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // An instance of a theory course is needed here, to provide the logic from the class.
    TheoryCourse theoryCourse = new TheoryCourse();

    void addTheoryCourse() {
        contentPanel.removeAll();
        theoryCoursePrompt = new JLabel("Enter the theory course's name");
        textField = new JTextField(20);
        nextButton = new JButton("Next");
        theoryBackButton = new JButton("Back");

        theoryCourseButton.setEnabled(false);
        labCourseButton.setEnabled(true);
        showCoursesButton.setEnabled(true);

        theoryCoursePrompt.setSize(200, 50);

        contentPanel.add(theoryCoursePrompt);
        contentPanel.add(textField);
        contentPanel.add(nextButton);
        contentPanel.add(theoryBackButton);

        contentPanel.setVisible(true);
        splitPane.setRightComponent(contentPanel);
        frame.revalidate();
        frame.repaint();

        nextButton.addActionListener(e -> processTheoryInput());

        textField.addActionListener( e -> processTheoryInput());

        theoryBackButton.addActionListener(e -> {
            contentPanel.removeAll();
            contentPanel.add(welcomeLabel);
//            contentPanel.setVisible(false);
            theoryCourseButton.setEnabled(true);
            frame.revalidate();
            frame.repaint();
        });
    }

    private void processTheoryInput(){ // A listener for button to add a theory course.
        String userInput = textField.getText();
        if(!userInput.isEmpty()) {
            theoryCourse.processNextInput(userInput, this); // "this" refers to the current GUI.
            textField.setText("");
        }
    }

    LabCourse labCourse = new LabCourse();

    void addLabCourse() {
        contentPanel.removeAll();
        labCoursePrompt = new JLabel("Enter the lab course's name");
        textField = new JTextField(20);
        nextButton = new JButton("Next");
        labBackButton = new JButton("Back");

        labCourseButton.setEnabled(false);
        theoryCourseButton.setEnabled(true);
        showCoursesButton.setEnabled(true);

        labCoursePrompt.setSize(200, 50);

        contentPanel.add(labCoursePrompt);
        contentPanel.add(textField);
        contentPanel.add(nextButton);
        contentPanel.add(labBackButton);

        contentPanel.setVisible(true);
        splitPane.setRightComponent(contentPanel);
        frame.revalidate();
        frame.repaint();

        nextButton.addActionListener(e -> processLabInput());

        textField.addActionListener( e -> processLabInput());

        labBackButton.addActionListener(e -> {
            contentPanel.removeAll();
            contentPanel.add(welcomeLabel);
//            contentPanel.setVisible(false);
            labCourseButton.setEnabled(true);
            frame.revalidate();
            frame.repaint();
        });
    }

    private void processLabInput(){ // A listener for button to add a lab course.
        String userInput = textField.getText();
        if(!userInput.isEmpty()) {
            labCourse.processNextInput(userInput, this); // "this" refers to the current GUI.
            textField.setText("");
        }
    }

    void displayCourses(){
        showCoursesBackButton = new JButton("Back");

        contentPanel.removeAll();
        theoryCourseButton.setEnabled(true);
        labCourseButton.setEnabled(true);
        showCoursesButton.setEnabled(false);

        for(TheoryCourse course : theoryCourse.theoryCourses) {
            courseContainer = new JLabel();
            courseContainer.setText("Course name: " + course.courseName + "\nCredit Hours: " + course.creditHours +
                    "Course Type: " + course.courseType + "\nYour Attendance: " + course.attendanceInPercentage +
                    "%\nYour Total Theory Score: " + course.totalTheoryScore + "Grade Points: " + course.gradePoints);
            contentPanel.add(courseContainer);
        }

        for(LabCourse course : labCourse.labCourses) {
            courseContainer = new JLabel();
            courseContainer.setText("Course name: " + course.courseName + " Credit Hours: " + course.creditHours +
                    " Course Type: " + course.courseType + " Your Attendance: " + course.attendanceInPercentage +
                    "% Your Total Theory Score: " + course.totalLabScore + " Grade Points: " + course.gradePoints);
            contentPanel.add(courseContainer);
        }

        contentPanel.add(showCoursesBackButton);
        contentPanel.setVisible(true);

        splitPane.setRightComponent(contentPanel);
        frame.revalidate();
        frame.repaint();

        showCoursesBackButton.addActionListener(e -> {
            contentPanel.removeAll();
            contentPanel.add(welcomeLabel);
//            contentPanel.setVisible(false);
            showCoursesButton.setEnabled(true);
            frame.revalidate();
            frame.repaint();
        });
    }
}
