import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class DisplayingCoursesGUI {
    GUI mainUI;
    JTable theoryTable, labTable;
    JScrollPane scrollPaneForTheoryCourses, scrollPaneForLabCourses;
    JTabbedPane tabbedPaneForCourses;
    JPanel semesterSortPanel, tablePanel, buttonAndGPAPanel;
    JLabel semesterGPAlabel, CGPAlabel, semesterSelectPrompt;
    JButton showCoursesBackButton, deleteCourseButton;
    JComboBox<Integer> semesterSelectDropdown;

    DisplayingCoursesGUI(GUI mainUI) {
        this.mainUI = mainUI;
    }

    void displayCourses() {
        mainUI.contentPanel.removeAll();

        mainUI.theoryCourseButton.setEnabled(true);
        mainUI.labCourseButton.setEnabled(true);
        mainUI.showCoursesButton.setEnabled(false);
        mainUI.whatIfButton.setEnabled(true);

        showCoursesBackButton = new JButton("Back");
        semesterSortPanel = new JPanel(new FlowLayout());
        tablePanel = new JPanel(new GridLayout(1,0));
        buttonAndGPAPanel = new JPanel(new FlowLayout());
        tabbedPaneForCourses = new JTabbedPane();
        theoryTable = new JTable(mainUI.theoryModel);
        theoryTable.getTableHeader().setReorderingAllowed(false);
        labTable = new JTable(mainUI.labModel);
        labTable.getTableHeader().setReorderingAllowed(false);
        scrollPaneForTheoryCourses = new JScrollPane(theoryTable);
        scrollPaneForLabCourses = new JScrollPane(labTable);
        deleteCourseButton = new JButton("Delete Course");
        semesterSelectPrompt = new JLabel("Sort by Semester:");
        semesterSelectDropdown = new JComboBox<>();
        // Calling the method to add all the options for the semesters into the dropdown menu
        semesterDropdownOptions();
        // Making it initially load the first existing semester
        if (semesterSelectDropdown.getItemCount() > 0) {
            semesterSelectDropdown.setSelectedIndex(0);
        }

        semesterGPAlabel = new JLabel();
        CGPAlabel = new JLabel();
        // Safety check if selected semester is null
        if (semesterSelectDropdown.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(mainUI.frame, "No courses to show!", "Error", JOptionPane.ERROR_MESSAGE);
            mainUI.contentPanel.removeAll();
            mainUI.contentPanel.add(mainUI.welcomeLabel);
            mainUI.showCoursesButton.setEnabled(true);
            return;
        }
        updateGPALabels((Integer) semesterSelectDropdown.getSelectedItem());

        theoryTable.setBackground(mainUI.powderPetal);
        theoryTable.setForeground(mainUI.ashBrown);
        theoryTable.getTableHeader().setBackground(mainUI.twilightIndigo);
        theoryTable.getTableHeader().setForeground(mainUI.powderBlue);

        labTable.setBackground(mainUI.powderPetal);
        labTable.setForeground(mainUI.ashBrown);
        labTable.getTableHeader().setBackground(mainUI.twilightIndigo);
        labTable.getTableHeader().setForeground(mainUI.powderBlue);

        tabbedPaneForCourses.addTab("Theory Courses", scrollPaneForTheoryCourses);
        tabbedPaneForCourses.addTab("Lab Courses",scrollPaneForLabCourses);
        tabbedPaneForCourses.setBackground(mainUI.powderPetal);
        tabbedPaneForCourses.setForeground(mainUI.ashBrown);

        semesterSelectPrompt.setForeground(mainUI.ashBrown);
        semesterSelectDropdown.setBackground(mainUI.powderPetal);
        semesterSelectDropdown.setForeground(mainUI.ashBrown);

        semesterSortPanel.add(semesterSelectPrompt);
        semesterSortPanel.add(semesterSelectDropdown);

        tablePanel.add(tabbedPaneForCourses);

        showCoursesBackButton.setBackground(mainUI.twilightIndigo);
        showCoursesBackButton.setForeground(mainUI.powderBlue);
        deleteCourseButton.setBackground(mainUI.twilightIndigo);
        deleteCourseButton.setForeground(mainUI.powderBlue);
        semesterGPAlabel.setBackground(mainUI.ashBrown);
        CGPAlabel.setBackground(mainUI.ashBrown);

        buttonAndGPAPanel.add(showCoursesBackButton);
        buttonAndGPAPanel.add(deleteCourseButton);
        buttonAndGPAPanel.add(semesterGPAlabel);
        buttonAndGPAPanel.add(CGPAlabel);

        mainUI.contentPanel.add(semesterSortPanel,BorderLayout.NORTH);
        mainUI.contentPanel.add(tablePanel, BorderLayout.CENTER);
        mainUI.contentPanel.add(buttonAndGPAPanel, BorderLayout.SOUTH);
        mainUI.contentPanel.setVisible(true);

        mainUI.splitPane.setRightComponent(mainUI.contentPanel);
        mainUI.frame.revalidate();
        mainUI.frame.repaint();

        // A listener to log changes in semesters
        semesterSelectDropdown.addActionListener(_ -> {
            updateTheoryTable();
            updateLabTable();
            updateGPALabels((Integer) semesterSelectDropdown.getSelectedItem());
        });

        // A listener to unselect a row in the lab table if a blank space is clicked
        scrollPaneForTheoryCourses.getViewport().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                theoryTable.clearSelection();
            }
        });

        // A listener to unselect a row in the theory table if a blank space is clicked
        scrollPaneForLabCourses.getViewport().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                labTable.clearSelection();
            }
        });

        deleteCourseButton.addActionListener(_ -> {

            int theoryRow = theoryTable.getSelectedRow();
            int labRow = labTable.getSelectedRow();

            if (theoryRow != -1) {
                int confirmDelete = JOptionPane.showConfirmDialog(mainUI.frame, "Are you sure you want to delete?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirmDelete == JOptionPane.YES_OPTION) {
                    int id = (int) mainUI.theoryModel.getValueAt(theoryRow, 0);
                    DBCourseManagement.deleteCourse(id, "theory_courses");
                    updateTheoryTable();
                    updateGPALabels((Integer) semesterSelectDropdown.getSelectedItem());
                    JOptionPane.showMessageDialog(mainUI.frame, "Deleted theory table");
                }
            } else if (labRow != -1) {
                int confirmDelete = JOptionPane.showConfirmDialog(mainUI.frame, "Are you sure you want to delete?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirmDelete == JOptionPane.YES_OPTION) {
                    int id = (int) mainUI.labModel.getValueAt(labRow, 0);
                    DBCourseManagement.deleteCourse(id, "lab_courses");
                    updateLabTable();
                    updateGPALabels((Integer) semesterSelectDropdown.getSelectedItem());
                    JOptionPane.showMessageDialog(mainUI.frame, "Deleted lab table");
                }
            } else {
                JOptionPane.showMessageDialog(mainUI.frame, "Please select a course to delete");
            }
        });

        showCoursesBackButton.addActionListener(_ -> {
            mainUI.contentPanel.removeAll();
            mainUI.contentPanel.add(mainUI.welcomeLabel);
            mainUI.showCoursesButton.setEnabled(true);
            mainUI.frame.revalidate();
            mainUI.frame.repaint();
        });

        updateTheoryTable();
        updateLabTable();
    }

    private void updateTheoryTable() {
        /* A method to update the theory course table, fetching data from SQLite
        We start by resetting the pre-existing rows: */

        Integer selectedSemester = (Integer) semesterSelectDropdown.getSelectedItem();
        if (selectedSemester != null) {

            mainUI.theoryModel.setRowCount(0);

            // Here, a prompt that selects fields from a table.
            String selectionPromptSQL = "SELECT id, course_name, credit_hours, attendance, sessionals_score," +
                    " midterm_exam_score, final_exam_score, total_theory_score, grade_points FROM theory_courses " +
                    "WHERE semester_number = ?";

            try (Connection conn = SQLDatabase.connectToDatabase(); // Establishing a connection
                 PreparedStatement pstmt = conn.prepareStatement(selectionPromptSQL)) { // Creating the statement

                pstmt.setInt(1, selectedSemester);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) { // Iterating through the table
                    int id = rs.getInt("id");
                    String cn = rs.getString("course_name");
                    int crhrs = rs.getInt("credit_hours");
                    int att = rs.getInt("attendance");
                    int ss = rs.getInt("sessionals_score");
                    int mes = rs.getInt("midterm_exam_score");
                    int fes = rs.getInt("final_exam_score");
                    int tts = rs.getInt("total_theory_score");
                    int gps = rs.getInt("grade_points");

                    mainUI.theoryModel.addRow(new Object[]{id, cn, crhrs, att, ss, mes, fes, tts, gps}); // Adding all the data in a row.
                }
            } catch (SQLException e) {
                System.out.println("Couldn't load table: " + e.getMessage());
            }
        }
    }

    private void updateLabTable() {
        Integer selectedSemester = (Integer) semesterSelectDropdown.getSelectedItem();
        if (selectedSemester != null) {

            mainUI.labModel.setRowCount(0);

            String selectionPromptSQL = "SELECT id, course_name, credit_hours, attendance, lab_manual_score, project_score," +
                    " lab_exam_score, total_lab_score, grade_points FROM lab_courses WHERE semester_number = ?";

            try (Connection conn = SQLDatabase.connectToDatabase(); // Establishing a connection
                 PreparedStatement pstmt = conn.prepareStatement(selectionPromptSQL)) { // Creating the statement

                pstmt.setInt(1, selectedSemester);
                ResultSet rs = pstmt.executeQuery();

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

                    mainUI.labModel.addRow(new Object[]{id, cn, crhrs, attendance, lms, ps, les, tls, gps});
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void updateGPALabels(int selectedSemester) {
        // The logic for GPA
        double[] semesterTotals = GPACalculations.totalsForGPA(selectedSemester);
        double GPA = (semesterTotals[1] > 0) ? semesterTotals[0] / semesterTotals[1] : 0;
        semesterGPAlabel.setText(String.format("GPA: %.2f", GPA));

        // The logic for CGPA
        double[] degreeTotals = GPACalculations.totalsForCGPA();
        double CGPA = (degreeTotals[1] > 0) ? degreeTotals[0] / degreeTotals[1] : 0;
        CGPAlabel.setText(String.format("CGPA: %.2f", CGPA));

    }

    private void semesterDropdownOptions(){

        String populateDropdownPrompt = "SELECT DISTINCT semester_number FROM theory_courses " +
                "UNION SELECT DISTINCT semester_number FROM lab_courses ORDER BY 1";

        try(Connection conn = SQLDatabase.connectToDatabase();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(populateDropdownPrompt)){

            while(rs.next()){
                semesterSelectDropdown.addItem(rs.getInt(1));
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
}