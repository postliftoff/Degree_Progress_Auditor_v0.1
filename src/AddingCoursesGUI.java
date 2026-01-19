import javax.swing.*;
import java.awt.*;

public class AddingCoursesGUI {
    GUI mainUI;
    JPanel theoryPanel, labPanel;
    JButton theoryBackButton, labBackButton;

    AddingCoursesGUI(GUI mainUI) {
        this.mainUI = mainUI;
    }

    // An instance of a theory course is needed here, to provide the logic from the class.
    TheoryCourse theoryCourse = new TheoryCourse();

    void addTheoryCourse() {
        mainUI.contentPanel.removeAll();

        mainUI.theoryCoursePrompt = new JLabel("Enter the semester number:");
        mainUI.textField = new JTextField(20);
        mainUI.nextButton = new JButton("Next");
        theoryBackButton = new JButton("Back");
        theoryPanel = new JPanel(new FlowLayout());

        mainUI.theoryCourseButton.setEnabled(false);
        mainUI.labCourseButton.setEnabled(true);
        mainUI.showCoursesButton.setEnabled(true);
        mainUI.whatIfButton.setEnabled(true);

        mainUI.theoryCoursePrompt.setSize(200, 50);

        mainUI.theoryCoursePrompt.setForeground(mainUI.ashBrown);
        mainUI.textField.setBackground(mainUI.powderPetal);
        mainUI.nextButton.setBackground(mainUI.twilightIndigo);
        mainUI.nextButton.setForeground(mainUI.powderBlue);
        theoryBackButton.setBackground(mainUI.twilightIndigo);
        theoryBackButton.setForeground(mainUI.powderBlue);

        theoryPanel.add(mainUI.theoryCoursePrompt);
        theoryPanel.add(mainUI.textField);
        theoryPanel.add(mainUI.nextButton);
        theoryPanel.add(theoryBackButton);

        mainUI.contentPanel.add(theoryPanel, BorderLayout.CENTER);

        mainUI.contentPanel.setVisible(true);
        mainUI.splitPane.setRightComponent(mainUI.contentPanel);
        mainUI.frame.revalidate();
        mainUI.frame.repaint();

        mainUI.nextButton.addActionListener(_ -> processTheoryInput());

        mainUI.textField.addActionListener(_ -> processTheoryInput());

        theoryBackButton.addActionListener(_ -> {
            mainUI.contentPanel.removeAll();
            mainUI.contentPanel.add(mainUI.welcomeLabel);
            theoryCourse.resetState();
            mainUI.theoryCourseButton.setEnabled(true);
            mainUI.frame.revalidate();
            mainUI.frame.repaint();
        });
    }

    // An instance of a lab course is needed here, to provide the logic from the class.
    LabCourse labCourse = new LabCourse();

    void addLabCourse() {
        mainUI.contentPanel.removeAll();

        mainUI.labCoursePrompt = new JLabel("Enter the semester number:");
        mainUI.textField = new JTextField(20);
        mainUI.nextButton = new JButton("Next");
        labBackButton = new JButton("Back");
        labPanel = new JPanel(new FlowLayout());

        mainUI.labCourseButton.setEnabled(false);
        mainUI.theoryCourseButton.setEnabled(true);
        mainUI.showCoursesButton.setEnabled(true);
        mainUI.whatIfButton.setEnabled(true);

        mainUI.labCoursePrompt.setSize(200, 50);

        mainUI.labCoursePrompt.setForeground(mainUI.ashBrown);
        mainUI.textField.setBackground(mainUI.powderPetal);
        mainUI.nextButton.setBackground(mainUI.twilightIndigo);
        mainUI.nextButton.setForeground(mainUI.powderBlue);
        labBackButton.setBackground(mainUI.twilightIndigo);
        labBackButton.setForeground(mainUI.powderBlue);

        labPanel.add(mainUI.labCoursePrompt);
        labPanel.add(mainUI.textField);
        labPanel.add(mainUI.nextButton);
        labPanel.add(labBackButton);

        mainUI.contentPanel.add(labPanel, BorderLayout.CENTER);

        mainUI.contentPanel.setVisible(true);
        mainUI.splitPane.setRightComponent(mainUI.contentPanel);
        mainUI.frame.revalidate();
        mainUI.frame.repaint();

        mainUI.nextButton.addActionListener(_ -> processLabInput());

        mainUI.textField.addActionListener(_ -> processLabInput());

        labBackButton.addActionListener(_ -> {
            mainUI.contentPanel.removeAll();
            mainUI.contentPanel.add(mainUI.welcomeLabel);
            labCourse.resetState();
            mainUI.labCourseButton.setEnabled(true);
            mainUI.frame.revalidate();
            mainUI.frame.repaint();
        });
    }

    private void processTheoryInput() { // A listener for button to add a theory course.
        String userInput = mainUI.textField.getText();
        if (!userInput.isEmpty()) {
            theoryCourse.processNextInput(userInput, mainUI); // "this" refers to the current GUI.
            mainUI.textField.setText("");
        }
    }

    private void processLabInput() { // A listener for button to add a lab course.
        String userInput = mainUI.textField.getText();
        if (!userInput.isEmpty()) {
            labCourse.processNextInput(userInput, mainUI); // "this" refers to the current GUI.
            mainUI.textField.setText("");
        }
    }
}