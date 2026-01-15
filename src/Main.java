public class Main {
    public static void main(String[] args) {
        SQLDatabase database = new SQLDatabase();
        database.initializeDatabase();
        GUI gui = new GUI();
        gui.guiStart();
    }
}

// Validity checks pending for all the attributes the uer has to enter at runtime.