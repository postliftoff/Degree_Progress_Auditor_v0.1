public class Main {
    public static void main(String[] args) {
        SQLDatabase.initializeTheoryTable();
        SQLDatabase.initializeLabTable();
        GUI gui = new GUI();
        gui.guiStart();
    }
}