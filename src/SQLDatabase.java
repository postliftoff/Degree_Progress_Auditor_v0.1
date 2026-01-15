import java.sql.*;

public class SQLDatabase {

    // The URL linking to my database file.
    private static final String URL = "jdbc:sqlite:DegreeProgressAuditor.db";

    // The method that establishes the connection.
    public static Connection connectToDatabase() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase(){
        // The SQL statement that initializes the entire database and creates a table.
        String sqlTableCreationPrompt = "CREATE TABLE IF NOT EXISTS theory_courses (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "course_name TEXT NOT NULL," +
                "credit_hours INTEGER," +
                "attendance INTEGER," +
                "sessionals_score INTEGER," +
                "midterm_exam_score INTEGER," +
                "final_exam_score INTEGER," +
                "total_theory_score INTEGER," +
                "grade_points REAL" +
                ");";

        // Said SQL Statement being executed.
        /* The reason we use try-catch is because establishing a connection with the SQL DB uses up memory and
        resources, we want the database to be closed automatically when we are done utilising it.
         */
        try (Connection conn = connectToDatabase(); Statement stmt = conn.createStatement()){
            stmt.execute(sqlTableCreationPrompt);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addTheoryCourseToDb(TheoryCourse theoryCourse) {

        // This prompt creates a parameterised statement in the database and fills in values given by the user.
        String sqlTheoryCoursePrompt = "INSERT INTO theory_courses(course_name,credit_hours,attendance" +
        ",sessionals_score,midterm_exam_score,final_exam_score,total_theory_score,grade_points)" +
                "VALUES (?,?,?,?,?,?,?,?)";

        try (Connection conn = connectToDatabase(); PreparedStatement pstmt = conn.prepareStatement(sqlTheoryCoursePrompt)){

            //Assigning each value one by one
            pstmt.setString(1, theoryCourse.courseName);
            pstmt.setInt(2, theoryCourse.creditHours);
            pstmt.setInt(3, theoryCourse.attendanceInPercentage);
            pstmt.setInt(4, theoryCourse.sessionalsScore);
            pstmt.setInt(5, theoryCourse.midtermExamScore);
            pstmt.setInt(6, theoryCourse.finalExamScore);
            pstmt.setInt(7, theoryCourse.totalTheoryScore);
            pstmt.setDouble(8, theoryCourse.gradePoints);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Failed to save database: " + e.getMessage());
        }
    }
}
