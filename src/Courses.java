import javax.swing.*;

// This class is for whenever I want to add a new course for my auditor.
public abstract class Courses {
    int semesterNumber;
    String courseName;
    String courseType;
    int creditHours;
    int attendanceInPercentage;
    double gradePoints;
    int state = 0; // The state variable is used in processing the input, it cycles through the labels.

    abstract void processNextInput(String input, GUI gui); // Method for initialising changes depending on type of course

    // If the user presses back in the midst of adding a course, this will abort the entire process.
    void resetState(){
        state = 0;
    }
}

// Courses are of two types, Theory and Lab. I used inheritance here to make them all have the generic traits.

class TheoryCourse extends Courses {
    int sessionalsScore;
    int midtermExamScore;
    int finalExamScore;
    int totalTheoryScore;

    /* Below is the implementation of the method used when creating a new theory course:
     We pass the input from the GUI and the GUI itself into the method (The current active GUI).
    The reason for this is the GUI needs to have its label updated everytime we need a new input.
    */

    @Override
    void processNextInput(String input, GUI gui) {
        try {
            switch (state) {
                case 0:
                    this.semesterNumber = Integer.parseInt(input);
                    /* The input we get here is fully in String form,
                    We must parse our input to int form using wrapper classes.
                    */
                    gui.theoryCoursePrompt.setText("Enter the theory course's name:");
                    state++;
                    break;
                case 1:
                    this.courseName = input;
                    gui.theoryCoursePrompt.setText("Enter the credit hours");
                    state++;
                    break;
                case 2:
                    this.creditHours = Integer.parseInt(input);
                    gui.theoryCoursePrompt.setText("Enter the attendance in percentage (without % sign)");
                    state++;
                    break;
                case 3:
                    if(Integer.parseInt(input) < 0 || Integer.parseInt(input) > 100) {
                        JOptionPane.showMessageDialog(gui.frame, "Enter attendance between 0 and 100", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    this.attendanceInPercentage = Integer.parseInt(input);
                    gui.theoryCoursePrompt.setText("Enter the sessional score");
                    state++;
                    break;
                case 4:
                    if (Integer.parseInt(input) < 0 || Integer.parseInt(input) > 20) {
                        JOptionPane.showMessageDialog(gui.frame, "Enter sessionals score between 0 and 20", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    this.sessionalsScore = Integer.parseInt(input);
                    gui.theoryCoursePrompt.setText("Enter the midterm exam score");
                    state++;
                    break;
                case 5:
                    if (Integer.parseInt(input) < 0 || Integer.parseInt(input) > 30) {
                        JOptionPane.showMessageDialog(gui.frame, "Enter midterm exam score between 0 and 30", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    this.midtermExamScore = Integer.parseInt(input);
                    gui.theoryCoursePrompt.setText("Enter the final exam score");
                    state++;
                    break;
                case 6:
                    if (Integer.parseInt(input) < 0 || Integer.parseInt(input) > 50) {
                        JOptionPane.showMessageDialog(gui.frame, "Enter final exam score between 0 and 50", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    this.finalExamScore = Integer.parseInt(input);
                    this.courseType = "Theory";
                    this.totalTheoryScore = sessionalsScore + midtermExamScore + finalExamScore;
                    this.gradePoints = GPCalculator.calculatorForTheoryGP(this.totalTheoryScore, this.creditHours);

                    DBCourseManagement.addTheoryCourseToDb(this); // Saves the current theory course to the database.

                    gui.theoryCoursePrompt.setText("Theory course has been saved!");
                    gui.nextButton.setEnabled(false);
                    gui.textField.setEnabled(false);

                    state = 0;
                    break;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(gui.frame, "Please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

class LabCourse extends Courses {
    int labManualScore;
    int projectScore;
    int labExamScore;
    int totalLabScore;

    // The same method with the implementation for a lab course.
    @Override
    void processNextInput(String input, GUI gui) {
        try {
            switch (state) {
                case 0:
                    this.semesterNumber = Integer.parseInt(input);
                    gui.labCoursePrompt.setText("Enter the lab course's name:");
                    state++;
                    break;
                case 1:
                    this.courseName = input;
                    gui.labCoursePrompt.setText("Enter the credit hours");
                    state++;
                    break;
                case 2:
                    this.creditHours = Integer.parseInt(input);
                    gui.labCoursePrompt.setText("Enter the attendance in percentage (without % sign)");
                    state++;
                    break;
                case 3:
                    if(Integer.parseInt(input) < 0 || Integer.parseInt(input) > 100) {
                        JOptionPane.showMessageDialog(gui.frame, "Enter attendance between 0 and 100", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    this.attendanceInPercentage = Integer.parseInt(input);
                    gui.labCoursePrompt.setText("Enter the lab manual score");
                    state++;
                    break;
                case 4:
                    if (Integer.parseInt(input) < 0 || Integer.parseInt(input) > 15) {
                        JOptionPane.showMessageDialog(gui.frame, "Enter lab manual score between 0 and 15", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    this.labManualScore = Integer.parseInt(input);
                    gui.labCoursePrompt.setText("Enter the project score");
                    state++;
                    break;
                case 5:
                    if (Integer.parseInt(input) < 0 || Integer.parseInt(input) > 15) {
                        JOptionPane.showMessageDialog(gui.frame, "Enter project score between 0 and 15", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    this.projectScore = Integer.parseInt(input);
                    gui.labCoursePrompt.setText("Enter the lab exam score");
                    state++;
                    break;
                case 6:
                    if (Integer.parseInt(input) < 0 || Integer.parseInt(input) > 20) {
                        JOptionPane.showMessageDialog(gui.frame, "Enter lab exam score between 0 and 20", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    this.labExamScore = Integer.parseInt(input);
                    this.courseType = "Lab";
                    this.totalLabScore = labManualScore + projectScore + labExamScore;
                    this.gradePoints = GPCalculator.calculatorForLabGP(this.totalLabScore, this.creditHours);

                    DBCourseManagement.addLabCourseToDb(this); // Saves the current lab course to the database

                    gui.labCoursePrompt.setText("Lab course has been saved!");
                    gui.nextButton.setEnabled(false);
                    gui.textField.setEnabled(false);

                    state = 0;
                    break;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(gui.frame, "Please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}