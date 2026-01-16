public class Main {
    public static void main(String[] args) {
        SQLDatabase database = new SQLDatabase();
        database.initializeDatabase();
        GUI gui = new GUI();
        gui.guiStart();
    }
}

// Needs validity checks pending for all the attributes the user has to enter at runtime.
// Make changes to LabCourse, same as theoryCourse