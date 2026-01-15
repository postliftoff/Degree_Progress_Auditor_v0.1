import java.util.ArrayList;

public class Semester{
    ArrayList<Courses> semester = new ArrayList<>();
    int semesterNumber;
    double GPA;

    void addTheoryCoursesToSemester(ArrayList<TheoryCourse> theoryCourses){
        semester.addAll(theoryCourses);
    }

    void addLabCoursesToSemester(ArrayList<LabCourse> labCourses){
        semester.addAll(labCourses);
    }
}

// This class is for whenever I want to add a new course for my auditor.
abstract class Courses extends Semester {
    ArrayList<TheoryCourse> theoryCourses = new ArrayList<>();
    ArrayList<LabCourse> labCourses = new ArrayList<>();
    String courseName;
    String courseType;
    int creditHours;
    int attendanceInPercentage;
    double gradePoints;
    int state = 0; // The state variable is used in processing the input, it cycles through the labels.
    abstract void processNextInput(String input, GUI gui); // Method for initialising changes depending on type of course
}

// Courses are of two types, Theory and Lab. I used inheritance here to make them all have the generic traits.

class TheoryCourse extends Courses {
    int sessionalsScore;
    int midtermExamScore;
    int finalExamScore;
    int totalTheoryScore;
    CGPA cgpa;

    /* Below is the implementation of the method used when creating a new theory course:
     We pass the input from the GUI and the GUI itself into the method (The current active GUI).
    The reason for this is the GUI needs to have its label updated everytime we need a new input.
    */

    @Override
    void processNextInput(String input, GUI gui) {
        switch (state) {
            case 0:
                this.courseName = input;
                gui.theoryCoursePrompt.setText("Enter the credit hours");
                state++;
                break;
            case 1:
                this.creditHours = Integer.parseInt(input);
                /* The input we get here is fully in String form,
                We must parse our input to int form using wrapper classes.
                */
                gui.theoryCoursePrompt.setText("Enter the attendance in percentage (without % sign)");
                state++;
                break;
            case 2:
                this.attendanceInPercentage = Integer.parseInt(input);
                gui.theoryCoursePrompt.setText("Enter the sessional score");
                state++;
                break;
            case 3:
                this.sessionalsScore = Integer.parseInt(input);
                gui.theoryCoursePrompt.setText("Enter the midterm exam score");
                state++;
                break;
            case 4:
                this.midtermExamScore = Integer.parseInt(input);
                gui.theoryCoursePrompt.setText("Enter the final exam score");
                state++;
                break;
            case 5:
                this.finalExamScore = Integer.parseInt(input);
                this.courseType = "Theory";
                this.totalTheoryScore = sessionalsScore + midtermExamScore + finalExamScore;
                this.gradePoints = gpCalculator.calculatorForGP(this.totalTheoryScore,this.creditHours);

                gui.theoryCoursePrompt.setText("Theory course has been saved!");
                gui.nextButton.setEnabled(false);
                gui.textField.setEnabled(false);

                theoryCourses.add(this);
                addTheoryCoursesToSemester(theoryCourses);

                cgpa.addToCGPA(gradePoints, creditHours);
                cgpa.calculateCGPA();

                state = 0;
                break;
        }
    }
}

class LabCourse extends Courses {
    int labManualScore;
    int projectScore;
    int labExamScore;
    int totalLabScore;
    CGPA cgpa;

    // The same method with the implementation for a lab course.
    @Override
    void processNextInput(String input, GUI gui) {
        switch (state) {
            case 0:
                this.courseName = input;
                gui.labCoursePrompt.setText("Enter the credit hours");
                state++;
                break;
            case 1:
                this.creditHours = Integer.parseInt(input);
                gui.labCoursePrompt.setText("Enter the attendance in percentage (without % sign)");
                state++;
                break;
            case 2:
                this.attendanceInPercentage = Integer.parseInt(input);
                gui.labCoursePrompt.setText("Enter the lab manual score");
                state++;
                break;
            case 3:
                this.labManualScore = Integer.parseInt(input);
                gui.labCoursePrompt.setText("Enter the project score");
                state++;
                break;
            case 4:
                this.projectScore = Integer.parseInt(input);
                gui.labCoursePrompt.setText("Enter the lab exam score");
                state++;
                break;
            case 5:
                this.labExamScore = Integer.parseInt(input);
                this.courseType = "Lab";
                this.totalLabScore = labManualScore + projectScore + labExamScore;
                this.gradePoints = gpCalculator.calculatorForGP(this.totalLabScore,this.creditHours);

                gui.labCoursePrompt.setText("Lab course has been saved!");
                gui.nextButton.setEnabled(false);
                gui.textField.setEnabled(false);

                labCourses.add(this);
                addLabCoursesToSemester(labCourses);

                cgpa.addToCGPA(gradePoints,creditHours);
                cgpa.calculateCGPA();

                state = 0;
                break;
        }
    }
}
