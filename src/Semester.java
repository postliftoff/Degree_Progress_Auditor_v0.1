import java.util.ArrayList;

public class Semester {
    ArrayList<Courses> semester = new ArrayList<>();
    double GPA;

    void gpaOfSemester(){ // Needs to be used in GUI
        double semesterGradePoints = 0;
        double semesterCreditHours = 0;
        for(Courses courses : semester){
            semesterGradePoints += courses.gradePoints;
            semesterCreditHours += courses.creditHours;
        }
    }

    void addTheoryCoursesToSemester(TheoryCourse theoryCourses){
        semester.add(theoryCourses);
    }

    void addLabCoursesToSemester(LabCourse labCourses){
        semester.add(labCourses);
    }
}

// This class is for whenever I want to add a new course for my auditor.
abstract class Courses {
    ArrayList<TheoryCourse> theoryCourses = new ArrayList<>();
    ArrayList<LabCourse> labCourses = new ArrayList<>();
    int semesterNumber;
    String courseName;
    String courseType;
    int creditHours;
    int attendanceInPercentage;
    double gradePoints;
    Semester semester = new Semester();
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
                this.semesterNumber = Integer.parseInt(input);
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
                /* The input we get here is fully in String form,
                We must parse our input to int form using wrapper classes.
                */
                gui.theoryCoursePrompt.setText("Enter the attendance in percentage (without % sign)");
                state++;
                break;
            case 3:
                if(Integer.parseInt(input) >= 0 && Integer.parseInt(input) <= 100){
                this.attendanceInPercentage = Integer.parseInt(input);
                }
                gui.theoryCoursePrompt.setText("Enter the sessional score");
                state++;
                break;
            case 4:
                this.sessionalsScore = Integer.parseInt(input);
                gui.theoryCoursePrompt.setText("Enter the midterm exam score");
                state++;
                break;
            case 5:
                this.midtermExamScore = Integer.parseInt(input);
                gui.theoryCoursePrompt.setText("Enter the final exam score");
                state++;
                break;
            case 6:
                this.finalExamScore = Integer.parseInt(input);
                this.courseType = "Theory";
                this.totalTheoryScore = sessionalsScore + midtermExamScore + finalExamScore;
                this.gradePoints = gpCalculator.calculatorForGP(this.totalTheoryScore,this.creditHours);

                SQLDatabase.addTheoryCourseToDb(this); // Saves the current theory course to the database.
                gui.updateTheoryTable();
                theoryCourses.add(this);
                semester.addTheoryCoursesToSemester(this);

                cgpa.addToCGPA(gradePoints, creditHours);
                cgpa.calculateCGPA();

                gui.theoryCoursePrompt.setText("Theory course has been saved!");
                gui.nextButton.setEnabled(false);
                gui.textField.setEnabled(false);

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
                if(Integer.parseInt(input) >= 0 && Integer.parseInt(input) <= 100){
                    this.attendanceInPercentage = Integer.parseInt(input);
                }
                gui.labCoursePrompt.setText("Enter the lab manual score");
                state++;
                break;
            case 4:
                this.labManualScore = Integer.parseInt(input);
                gui.labCoursePrompt.setText("Enter the project score");
                state++;
                break;
            case 5:
                this.projectScore = Integer.parseInt(input);
                gui.labCoursePrompt.setText("Enter the lab exam score");
                state++;
                break;
            case 6:
                this.labExamScore = Integer.parseInt(input);
                this.courseType = "Lab";
                this.totalLabScore = labManualScore + projectScore + labExamScore;
                this.gradePoints = gpCalculator.calculatorForGP(this.totalLabScore,this.creditHours);

                SQLDatabase.addLabCourseToDb(this); // Saves the current lab course to the database
                labCourses.add(this);
                semester.addLabCoursesToSemester(this);

                cgpa.addToCGPA(gradePoints,creditHours);
                cgpa.calculateCGPA();

                gui.labCoursePrompt.setText("Lab course has been saved!");
                gui.nextButton.setEnabled(false);
                gui.textField.setEnabled(false);

                state = 0;
                break;
        }
    }
}
