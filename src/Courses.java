import java.util.ArrayList;

// This class is for whenever I want to add a new course for my auditor.
public abstract class Courses {
    ArrayList<TheoryCourse> theoryCourses = new ArrayList<>();
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
                this.gradePoints = (double) this.totalTheoryScore / this.creditHours;
                gui.theoryCoursePrompt.setText("Course has been saved!");
                gui.nextButton.setEnabled(false);
                gui.textField.setEnabled(false);
                theoryCourses.add(this);
                state = 0;
                break;
        }
    }
}

//class LabCourse extends Courses {
//    int labManualScore;
//    int projectScore;
//    int labExamScore;
//    int totalLabScore;
//
//    // The same method with the implementation for a lab course.
//    @Override
//    void initialiseCourse(){
//        System.out.println("Enter the course name:");
//        courseName = input.nextLine();
//        System.out.println("Enter the credit hours:");
//        creditHours = input.nextInt();
//        System.out.println("Enter the attendance in percentage (without the % sign):");
//        attendanceInPercentage = input.nextInt();
//        System.out.println("Enter the lab manual score:");
//        labManualScore = input.nextInt();
//        System.out.println("Enter the project score:");
//        projectScore = input.nextInt();
//        System.out.println("Enter the lab exam score:");
//        labExamScore = input.nextInt();
//    }
//}
