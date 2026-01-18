public class Main {
    public static void main(String[] args) {
        SQLDatabase.initializeTheoryTable();
        SQLDatabase.initializeLabTable();
        GUI gui = new GUI();
        gui.guiStart();
    }
}

// Needs validity checks pending for all the attributes the user has to enter at runtime.
// Need exceptional handling for each input field.
// OptionPane implementation.
// Make changes to LabCourse, same as theoryCourse.
// Make tabbedPane a distinguisher for semesters.