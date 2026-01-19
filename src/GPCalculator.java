public class GPCalculator {

// A calculator for how many grade points you earned, by multiplying the grade per credit hour.

    static double calculatorForTheoryGP(int totalScore, int creditHours) {

        if (totalScore >= 86) {
            return GradeToGP.A.getGradePointsPerGrade() * creditHours;
        } else if (totalScore >= 80) {
            return GradeToGP.A_MINUS.getGradePointsPerGrade() * creditHours;
        } else if (totalScore >= 75) {
            return GradeToGP.B_PLUS.getGradePointsPerGrade() * creditHours;
        } else if (totalScore >= 70) {
            return GradeToGP.B.getGradePointsPerGrade() * creditHours;
        } else if (totalScore >= 67) {
            return GradeToGP.B_MINUS.getGradePointsPerGrade() * creditHours;
        } else if (totalScore >= 63) {
            return GradeToGP.C_PLUS.getGradePointsPerGrade() * creditHours;
        } else if (totalScore >= 60) {
            return GradeToGP.C.getGradePointsPerGrade() * creditHours;
        } else if (totalScore >= 57) {
            return GradeToGP.C_MINUS.getGradePointsPerGrade() * creditHours;
        } else if (totalScore >= 54) {
            return GradeToGP.D_PLUS.getGradePointsPerGrade() * creditHours;
        } else if (totalScore >= 50) {
            return GradeToGP.D.getGradePointsPerGrade() * creditHours;
        } else {
            return GradeToGP.F.getGradePointsPerGrade() * creditHours;
        }

    }

    static double calculatorForLabGP(int totalScore, int creditHours) {

        if (totalScore >= 40) {
            return GradeToGP.A.getGradePointsPerGrade() * creditHours;
        } else if (totalScore >= 35) {
            return GradeToGP.B.getGradePointsPerGrade() * creditHours;
        } else if (totalScore >= 30) {
            return GradeToGP.C.getGradePointsPerGrade() * creditHours;
        } else if (totalScore >= 25) {
            return GradeToGP.D.getGradePointsPerGrade() * creditHours;
        } else {
            return GradeToGP.F.getGradePointsPerGrade() * creditHours;
        }

    }
}