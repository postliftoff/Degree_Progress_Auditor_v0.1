import java.awt.*;
import javax.swing.*;

public class GUI {
    JFrame frame;
    JPanel sidePanel;
    JPanel contentPanel;
    JLabel theoryCoursePrompt;
    JLabel courseContainer;
    JTextField textField;
    JButton theoryCourseButton;
    JButton showCoursesButton;
    JButton showCoursesBackButton;
    JButton nextButton;
    JButton addTheoryBackButton;

    void guiStart() {
        frame = new JFrame("Degree Progress Auditor");
        sidePanel = new JPanel();
        contentPanel = new JPanel();
        theoryCourseButton = new JButton("Add New Theory Course");
        showCoursesButton = new JButton("Show Courses");

        frame.setLayout(new BorderLayout());
        frame.setSize(1200, 600);

        sidePanel.setLayout(new GridLayout(4,1));
        sidePanel.setSize(200, 500);
        sidePanel.add(BorderLayout.WEST,theoryCourseButton);
        sidePanel.add(BorderLayout.WEST,showCoursesButton);
        frame.add(BorderLayout.WEST,sidePanel);

        contentPanel.setSize(800,500);
        contentPanel.setLayout(new FlowLayout());

        // The theory course button calls the method to add a new theory course.
        theoryCourseButton.addActionListener(e -> addTheoryCourse());

        // The show course button shows you added courses.
        showCoursesButton.addActionListener(e -> displayCourses());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // An instance of a theory course is needed here, to provide the logic from the class.
    TheoryCourse theoryCourse = new TheoryCourse();

    void addTheoryCourse() {
        contentPanel.removeAll();
        theoryCoursePrompt = new JLabel("Enter the course name");
        textField = new JTextField(20);
        nextButton = new JButton("Next");
        addTheoryBackButton = new JButton("Back");

        theoryCourseButton.setEnabled(false);
        showCoursesButton.setEnabled(true);

        theoryCoursePrompt.setSize(200, 50);

        contentPanel.add(theoryCoursePrompt);
        contentPanel.add(textField);
        contentPanel.add(nextButton);
        contentPanel.add(addTheoryBackButton);

        contentPanel.setVisible(true);
        frame.add(BorderLayout.CENTER,contentPanel);
        frame.revalidate();
        frame.repaint();

        nextButton.addActionListener(e -> processInput());

        textField.addActionListener( e -> processInput());

        addTheoryBackButton.addActionListener(e -> {
            contentPanel.removeAll();
            contentPanel.setVisible(false);
            theoryCourseButton.setEnabled(true);
            frame.revalidate();
            frame.repaint();
        });
    }

    private void processInput(){ // A listener for button to add a theory course.
        String userInput = textField.getText();
        if(!userInput.isEmpty()) {
            theoryCourse.processNextInput(userInput, this); // "this" refers to the current GUI.
            textField.setText("");
        }
    }

    void displayCourses(){
        courseContainer = new JLabel();
        showCoursesBackButton = new JButton("Back");

        contentPanel.removeAll();
        theoryCourseButton.setEnabled(true);
        showCoursesButton.setEnabled(false);

        for(TheoryCourse course : theoryCourse.theoryCourses) {
            courseContainer.setText("Course name: " + course.courseName + "\nCredit Hours: " + course.creditHours +
                    "Course Type: " + course.courseType + "\nYour Attendance: " + course.attendanceInPercentage +
                    "%\nYour Total Theory Score: " + course.totalTheoryScore + "Grade Points: " + course.gradePoints);
        }

        contentPanel.add(courseContainer);
        contentPanel.add(showCoursesBackButton);
        contentPanel.setVisible(true);

        frame.add(BorderLayout.CENTER,contentPanel);
        frame.revalidate();
        frame.repaint();

        showCoursesBackButton.addActionListener(e -> {
            contentPanel.removeAll();
            contentPanel.setVisible(false);
            showCoursesButton.setEnabled(true);
            frame.revalidate();
            frame.repaint();
        });
    }
}
