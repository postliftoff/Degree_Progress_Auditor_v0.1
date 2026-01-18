import java.sql.*;

public class SQLDatabase {

    // The URL linking to my database file.
    private static final String URL = "jdbc:sqlite:DegreeProgressAuditor.db";

    // The method that establishes the connection.
    public static Connection connectToDatabase() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeTheoryTable(){
        // The SQL statement that initializes the entire database and creates a table.
        String sqlTableCreationPrompt = "CREATE TABLE IF NOT EXISTS theory_courses (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "semester_number INTEGER," +
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

    public static void initializeLabTable(){
        // The SQL statement that initializes the entire database and creates a table.
        String sqlTableCreationPrompt = "CREATE TABLE IF NOT EXISTS lab_courses (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "semester_number INTEGER," +
                "course_name TEXT NOT NULL," +
                "credit_hours INTEGER," +
                "attendance INTEGER," +
                "lab_manual_score INTEGER," +
                "project_score INTEGER," +
                "lab_exam_score INTEGER," +
                "total_lab_score INTEGER," +
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
        String sqlTheoryCoursePrompt = "INSERT INTO theory_courses(semester_number,course_name,credit_hours,attendance" +
        ",sessionals_score,midterm_exam_score,final_exam_score,total_theory_score,grade_points)" +
                "VALUES (?,?,?,?,?,?,?,?,?)";

        try (Connection conn = connectToDatabase(); PreparedStatement pstmt = conn.prepareStatement(sqlTheoryCoursePrompt)){

            //Assigning each value one by one
            pstmt.setInt(1, theoryCourse.semesterNumber);
            pstmt.setString(2, theoryCourse.courseName);
            pstmt.setInt(3, theoryCourse.creditHours);
            pstmt.setInt(4, theoryCourse.attendanceInPercentage);
            pstmt.setInt(5, theoryCourse.sessionalsScore);
            pstmt.setInt(6, theoryCourse.midtermExamScore);
            pstmt.setInt(7, theoryCourse.finalExamScore);
            pstmt.setInt(8, theoryCourse.totalTheoryScore);
            pstmt.setDouble(9, theoryCourse.gradePoints);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Failed to save theory course: " + e.getMessage());
        }
    }

    public static void addLabCourseToDb(LabCourse labCourse) {

        // This prompt creates a parameterised statement in the database and fills in values given by the user.
        String sqlLabCoursePrompt = "INSERT INTO lab_courses(semester_number,course_name,credit_hours,attendance" +
        ",lab_manual_score,project_score,lab_exam_score,total_lab_score,grade_points)" +
                "VALUES (?,?,?,?,?,?,?,?,?)";

        try (Connection conn = connectToDatabase(); PreparedStatement pstmt = conn.prepareStatement(sqlLabCoursePrompt)){

            //Assigning each value one by one
            pstmt.setInt(1, labCourse.semesterNumber);
            pstmt.setString(2, labCourse.courseName);
            pstmt.setInt(3, labCourse.creditHours);
            pstmt.setInt(4, labCourse.attendanceInPercentage);
            pstmt.setInt(5, labCourse.labManualScore);
            pstmt.setInt(6, labCourse.projectScore);
            pstmt.setInt(7, labCourse.labExamScore);
            pstmt.setInt(8, labCourse.totalLabScore);
            pstmt.setDouble(9, labCourse.gradePoints);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Failed to save lab course: " + e.getMessage());
        }
    }

    public static void deleteCourse(int id, String chosenTable){
        // A method to delete chosen courses.
        String deleteCoursePromptSQL = "DELETE FROM " + chosenTable + " WHERE id = ?";

        try (Connection conn = SQLDatabase.connectToDatabase();
        PreparedStatement pstmt = conn.prepareStatement(deleteCoursePromptSQL)){
            pstmt.setInt(1,id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to delete course: " + e.getMessage());
        }
    }
}
