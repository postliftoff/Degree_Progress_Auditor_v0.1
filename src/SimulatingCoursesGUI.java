import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SimulatingCoursesGUI {
    GUI mainUI;
    JLabel gradeSelectPrompt, creditHourSimulPrompt, simulationSemesterPrompt;
    JButton simulationNextButton, simulationBackButton;
    JComboBox<GradeToGP> hypoGradeSelectDropdown;
    JComboBox<Integer> semesterSelectDropdown;
    JPanel semesterPanel, gradeAndCrhrsPanel, simulButtonsPanel;

    SimulatingCoursesGUI(GUI mainUI) {
        this.mainUI = mainUI;
    }

    void simulateCourses() {
        mainUI.contentPanel.removeAll();

        mainUI.theoryCourseButton.setEnabled(true);
        mainUI.labCourseButton.setEnabled(true);
        mainUI.showCoursesButton.setEnabled(true);
        mainUI.whatIfButton.setEnabled(false);

        simulationSemesterPrompt = new JLabel("Simulate for semester: ");
        semesterSelectDropdown = new JComboBox<>();
        semesterDropdownOptions();
        gradeSelectPrompt = new JLabel("Select Hypothetical Grade:");
        hypoGradeSelectDropdown = new JComboBox<>(GradeToGP.values());
        creditHourSimulPrompt = new JLabel("Enter the hypothetical credit hours:");
        mainUI.textField = new JTextField(20);
        simulationNextButton = new JButton("Simulate");
        simulationBackButton = new JButton("Back");
        semesterPanel = new JPanel(new FlowLayout());
        gradeAndCrhrsPanel = new JPanel(new FlowLayout());
        simulButtonsPanel = new JPanel(new FlowLayout());

        simulationSemesterPrompt.setForeground(mainUI.ashBrown);
        semesterSelectDropdown.setBackground(mainUI.powderPetal);
        semesterSelectDropdown.setForeground(mainUI.ashBrown);
        gradeSelectPrompt.setForeground(mainUI.ashBrown);
        hypoGradeSelectDropdown.setBackground(mainUI.powderPetal);
        hypoGradeSelectDropdown.setForeground(mainUI.ashBrown);
        creditHourSimulPrompt.setForeground(mainUI.ashBrown);
        mainUI.textField.setBackground(mainUI.powderPetal);
        simulationNextButton.setBackground(mainUI.twilightIndigo);
        simulationNextButton.setForeground(mainUI.powderBlue);
        simulationBackButton.setBackground(mainUI.twilightIndigo);
        simulationBackButton.setForeground(mainUI.powderBlue);

        semesterPanel.add(simulationSemesterPrompt);
        semesterPanel.add(semesterSelectDropdown);
        gradeAndCrhrsPanel.add(gradeSelectPrompt);
        gradeAndCrhrsPanel.add(hypoGradeSelectDropdown);
        gradeAndCrhrsPanel.add(creditHourSimulPrompt);
        gradeAndCrhrsPanel.add(mainUI.textField);
        simulButtonsPanel.add(simulationNextButton);
        simulButtonsPanel.add(simulationBackButton);

        mainUI.contentPanel.add(semesterPanel, BorderLayout.NORTH);
        mainUI.contentPanel.add(gradeAndCrhrsPanel, BorderLayout.CENTER);
        mainUI.contentPanel.add(simulButtonsPanel, BorderLayout.SOUTH);

        simulationNextButton.addActionListener(_ -> processSimulationInput());

        mainUI.textField.addActionListener(_ -> processSimulationInput());

        simulationBackButton.addActionListener(_ -> {
            mainUI.contentPanel.removeAll();
            mainUI.contentPanel.add(mainUI.welcomeLabel);
            mainUI.whatIfButton.setEnabled(true);
            mainUI.frame.revalidate();
            mainUI.frame.repaint();
        });

        mainUI.frame.revalidate();
        mainUI.frame.repaint();
    }

    // A method to process the entire simulation (Obtaining and utilising inputs)
    private void processSimulationInput() {
        try {
            GradeToGP selectedHypoGrade = (GradeToGP) hypoGradeSelectDropdown.getSelectedItem();

            Integer selectedSemesterSimulation = (Integer) semesterSelectDropdown.getSelectedItem();
            if (selectedSemesterSimulation == null) {
                JOptionPane.showMessageDialog(mainUI.frame, "Please select a semester");
                return;
            }

            String userInput = mainUI.textField.getText();
            if (userInput.isEmpty()) {
                JOptionPane.showMessageDialog(mainUI.frame, "Please enter the credit hours");
                return;
            }

            int hypoCreditHours = Integer.parseInt(userInput);
            double hypoGradePoints = selectedHypoGrade.getGradePointsPerGrade() * hypoCreditHours;

            double[] semesterTotals = GPACalculations.totalsForGPA(selectedSemesterSimulation);
            double hypoGPA = ((semesterTotals[1] + hypoCreditHours > 0)) ? (semesterTotals[0] + hypoGradePoints) / (semesterTotals[1] + hypoCreditHours) : 0;

            double[] degreeTotals = GPACalculations.totalsForCGPA();
            double hypoCGPA = ((degreeTotals[1] + hypoCreditHours) > 0) ? (degreeTotals[0] + hypoGradePoints) / (degreeTotals[1] + hypoCreditHours) : 0;

            String result = String.format("Hypothetical GPA: %.2f \nHypothetical CGPA: %.2f ", hypoGPA, hypoCGPA);

            JOptionPane.showMessageDialog(mainUI.frame, result);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainUI.frame, "Please enter an integer number for credit hours");
        }

    }

    private void semesterDropdownOptions() {
        /* SQL: DISTINCT filters out all duplicates, only unique rows are part of the result set
        This is useful, as we only want one semester number to appear, per semester.
        */
        String populateDropdownPrompt = "SELECT DISTINCT semester_number FROM theory_courses " +
                "UNION SELECT DISTINCT semester_number FROM lab_courses ORDER BY 1";

        try (Connection conn = SQLDatabase.connectToDatabase();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(populateDropdownPrompt)) {

            while (rs.next()) {
                semesterSelectDropdown.addItem(rs.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}