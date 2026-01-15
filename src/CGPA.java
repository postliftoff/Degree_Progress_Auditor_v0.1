public class CGPA {
    double CGPA;
    double cumulativeGradePoints;
    double cumulativeCreditHours;

    void addToCGPA(double gradePoints, double creditHours){
        cumulativeGradePoints += gradePoints;
        cumulativeCreditHours += creditHours;
    }

    void calculateCGPA(){
        CGPA = cumulativeGradePoints/cumulativeCreditHours;
    }

}
