import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GUI {
    JFrame frame;
    JSplitPane splitPane;
    JTable theoryTable, labTable;
    DefaultTableModel theoryModel, labModel;
    JScrollPane scrollPaneForTheoryCourses, scrollPaneForLabCourses;
    JTabbedPane tabbedPaneForSemesters;
    JPanel sidePanel, contentPanel, buttonPanel;
    JLabel welcomeLabel, theoryCoursePrompt ,labCoursePrompt;
    JTextField textField;
//    JOptionPane deletePrompt;
    JButton labCourseButton, labBackButton, theoryCourseButton, theoryBackButton, showCoursesButton, showCoursesBackButton, nextButton, deleteCourseButton;

    void guiStart() {
        frame = new JFrame("Degree Progress Auditor");
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        sidePanel = new JPanel();
        contentPanel = new JPanel();
        welcomeLabel = new JLabel("Welcome to the Degree Progress Auditor!");
        theoryCourseButton = new JButton("Add New Theory Course");
        labCourseButton = new JButton("Add New Lab Course");
        showCoursesButton = new JButton("Show Courses");

        theoryCourse.cgpa = commonGPA;
        labCourse.cgpa = commonGPA;

        String[] theoryColumns = {"ID","Course Name","Credit Hours","Attendance","Sessionals Score",
                "Midterm Exam Score","Final Exam Score","Total Theory Score","Grade Points"};
        theoryModel = new DefaultTableModel(theoryColumns, 0);

        String[] labColumns = {"ID","Course Name","Credit Hours","Attendance","Lab Manual Score",
                "Project Score","Lab Exam Score","Total Lab Score","Grade Points"};
        labModel = new DefaultTableModel(labColumns, 0);

        frame.setLayout(new BorderLayout());
        frame.setSize(1000, 700);

//        splitPane.setDividerLocation(0.2);
//        splitPane.setResizeWeight(0.05);
        splitPane.setOneTouchExpandable(true);

        sidePanel.setLayout(new GridLayout(1,4));
        sidePanel.setSize(100, 600);

        theoryCourseButton.setSize(30, 40);
        labCourseButton.setSize(30, 40);
        showCoursesButton.setSize(30, 40);

        sidePanel.add(BorderLayout.WEST,theoryCourseButton);
        sidePanel.add(BorderLayout.WEST,labCourseButton);
        sidePanel.add(BorderLayout.WEST,showCoursesButton);

        contentPanel.setSize(1000,600);
        contentPanel.setLayout(new FlowLayout());
        contentPanel.add(welcomeLabel);

        splitPane.setLeftComponent(sidePanel);
        splitPane.setRightComponent(contentPanel);
        frame.add(BorderLayout.CENTER,splitPane);

        // The theory course button calls the method to add a new theory course.
        theoryCourseButton.addActionListener(e -> addTheoryCourse());
        Semester sem = new Semester();

        // The lab course button calls the method to add a new lab course.
        labCourseButton.addActionListener(e -> addLabCourse());

        // The show course button shows you added courses.
        showCoursesButton.addActionListener(e -> displayCourses());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // An instance of CGPA is required to prevent crashes
    CGPA commonGPA  = new CGPA();
    // An instance of a theory course is needed here, to provide the logic from the class.
    TheoryCourse theoryCourse = new TheoryCourse();

    void addTheoryCourse() {
        contentPanel.removeAll();
        theoryCoursePrompt = new JLabel("Enter the semester number:");
        textField = new JTextField(20);
        nextButton = new JButton("Next");
        theoryBackButton = new JButton("Back");

        theoryCourseButton.setEnabled(false);
        labCourseButton.setEnabled(true);
        showCoursesButton.setEnabled(true);

        theoryCoursePrompt.setSize(200, 50);

        contentPanel.add(theoryCoursePrompt);
        contentPanel.add(textField);
        contentPanel.add(nextButton);
        contentPanel.add(theoryBackButton);

        contentPanel.setVisible(true);
        splitPane.setRightComponent(contentPanel);
        frame.revalidate();
        frame.repaint();

        nextButton.addActionListener(e -> processTheoryInput());

        textField.addActionListener( e -> processTheoryInput());

        theoryBackButton.addActionListener(e -> {
            contentPanel.removeAll();
            contentPanel.add(welcomeLabel);
//            contentPanel.setVisible(false);
            theoryCourseButton.setEnabled(true);
            frame.revalidate();
            frame.repaint();
        });
    }

    private void processTheoryInput(){ // A listener for button to add a theory course.
        String userInput = textField.getText();
        if(!userInput.isEmpty()) {
            theoryCourse.processNextInput(userInput, this); // "this" refers to the current GUI.
            textField.setText("");
        }
    }

    // An instance of a lab course is needed here, to provide the logic from the class.
    LabCourse labCourse = new LabCourse();

    void addLabCourse() {
        contentPanel.removeAll();
        labCoursePrompt = new JLabel("Enter the semester number:");
        textField = new JTextField(20);
        nextButton = new JButton("Next");
        labBackButton = new JButton("Back");

        labCourseButton.setEnabled(false);
        theoryCourseButton.setEnabled(true);
        showCoursesButton.setEnabled(true);

        labCoursePrompt.setSize(200, 50);

        contentPanel.add(labCoursePrompt);
        contentPanel.add(textField);
        contentPanel.add(nextButton);
        contentPanel.add(labBackButton);

        contentPanel.setVisible(true);
        splitPane.setRightComponent(contentPanel);
        frame.revalidate();
        frame.repaint();

        nextButton.addActionListener(e -> processLabInput());

        textField.addActionListener( e -> processLabInput());

        labBackButton.addActionListener(e -> {
            contentPanel.removeAll();
            contentPanel.add(welcomeLabel);
//            contentPanel.setVisible(false);
            labCourseButton.setEnabled(true);
            frame.revalidate();
            frame.repaint();
        });
    }

    private void processLabInput(){ // A listener for button to add a lab course.
        String userInput = textField.getText();
        if(!userInput.isEmpty()) {
            labCourse.processNextInput(userInput, this); // "this" refers to the current GUI.
            textField.setText("");
        }
    }

    void updateTheoryTable(){
        /* A method to update the theory course table, fetching data from SQLite
        We start by resetting the pre-existing rows: */
        theoryModel.setRowCount(0);

        // Here, a prompt that selects fields from a table.
        String selectionPromptSQL = "SELECT id, course_name, credit_hours, attendance, sessionals_score," +
                " midterm_exam_score, final_exam_score, total_theory_score, grade_points FROM theory_courses";

        try (Connection conn = SQLDatabase.connectToDatabase(); // Establishing a connection
             Statement stmt = conn.createStatement(); // Creating the statement
             ResultSet rs = stmt.executeQuery(selectionPromptSQL)){ // Executing our query in SQL

            while (rs.next()){ // Iterating through the table
                int id = rs.getInt("id");
                String cn = rs.getString("course_name");
                int crhrs = rs.getInt("credit_hours");
                int att = rs.getInt("attendance");
                int ss = rs.getInt("sessionals_score");
                int mes = rs.getInt("midterm_exam_score");
                int fes = rs.getInt("final_exam_score");
                int tts = rs.getInt("total_theory_score");
                int gps = rs.getInt("grade_points");

                theoryModel.addRow(new Object[]{id,cn,crhrs,att,ss,mes,fes,tts,gps}); // Adding all the data in a row.
            }
        } catch (SQLException e){
            System.out.println("Couldn't load table: " + e.getMessage());
        }
    }

    void updateLabTable() {
        labModel.setRowCount(0);

        String selectionPromptSQL = "SELECT id, course_name, credit_hours, attendance, lab_manual_score, project_score," +
                " lab_exam_score, total_lab_score, grade_points FROM lab_courses";

        try (Connection conn = SQLDatabase.connectToDatabase();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectionPromptSQL)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String cn = rs.getString("course_name");
                int crhrs = rs.getInt("credit_hours");
                int attendance = rs.getInt("attendance");
                int lms = rs.getInt("lab_manual_score");
                int ps = rs.getInt("project_score");
                int les = rs.getInt("lab_exam_score");
                int tls = rs.getInt("total_lab_score");
                int gps = rs.getInt("grade_points");

                labModel.addRow(new Object[]{id,cn,crhrs,attendance,lms,ps,les,tls,gps});
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
    }

    void displayCourses(){
        showCoursesBackButton = new JButton("Back");

        contentPanel.removeAll();
        theoryCourseButton.setEnabled(true);
        labCourseButton.setEnabled(true);
        showCoursesButton.setEnabled(false);


        buttonPanel = new JPanel();
        tabbedPaneForSemesters = new JTabbedPane();
        theoryTable = new JTable(theoryModel);
        labTable = new JTable(labModel);
        scrollPaneForTheoryCourses = new JScrollPane(theoryTable);
        scrollPaneForLabCourses = new JScrollPane(labTable);
        deleteCourseButton = new JButton("Delete Course");

//        scrollPaneForTheoryCourses.setLayout(new FlowLayout());
//        scrollPaneForLabCourses.setLayout(new FlowLayout());
//        scrollPaneForTheoryCourses.add(theoryTable);
//        scrollPaneForLabCourses.add(labTable);

//        tabbedPaneForSemesters.setLayout(new FlowLayout());
        tabbedPaneForSemesters.add(scrollPaneForTheoryCourses);
        tabbedPaneForSemesters.add(scrollPaneForLabCourses);

        buttonPanel.add(deleteCourseButton);
        buttonPanel.add(showCoursesBackButton);

        contentPanel.add(tabbedPaneForSemesters);
        contentPanel.add(buttonPanel);
        contentPanel.setVisible(true);

        splitPane.setRightComponent(contentPanel);
        frame.revalidate();
        frame.repaint();

        deleteCourseButton.addActionListener(e -> {
            int theoryRow = theoryTable.getSelectedRow();
            int labRow = labTable.getSelectedRow();

            if (theoryRow != -1){
                int id = (int) theoryModel.getValueAt(theoryRow, 0);
                SQLDatabase.deleteCourse(id,"theory_courses");
                updateTheoryTable();
                JOptionPane.showMessageDialog(frame, "Deleted theory table");
            } else if (labRow != -1){
                int id = (int) labModel.getValueAt(labRow, 0);
                SQLDatabase.deleteCourse(id,"lab_courses");
                updateLabTable();
                JOptionPane.showMessageDialog(frame, "Deleted lab table");
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a course to delete");
            }
        });

        showCoursesBackButton.addActionListener(e -> {
            contentPanel.removeAll();
            contentPanel.add(welcomeLabel);
//            contentPanel.setVisible(false);
            showCoursesButton.setEnabled(true);
            frame.revalidate();
            frame.repaint();
        });

        updateTheoryTable();
        updateLabTable();
    }
}
