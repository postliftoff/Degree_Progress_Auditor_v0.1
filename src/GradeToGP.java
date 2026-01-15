public enum GradeToGP {
    A(4.00), A_MINUS(3.66), // All the constants for A
    B_PLUS(3.33),B(3.00), B_MINUS(2.66), // All constants for B
    C_PLUS(2.33),C(2.00), C_MINUS(1.66), // All constants for C
    D_PLUS(1.30),D(1.00), // All constants for D
    F(0.00); // All constants for F

    private final double gradePointsPerGrade;

    private GradeToGP(double gradePointsPerGrade) {
        this.gradePointsPerGrade = gradePointsPerGrade;
    }

    public double getGradePointsPerGrade() {
        return gradePointsPerGrade;
    }
}
