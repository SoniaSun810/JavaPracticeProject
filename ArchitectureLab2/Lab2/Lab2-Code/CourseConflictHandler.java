import java.util.ArrayList;
import java.util.Observable;
import java.util.StringTokenizer;

public class CourseConflictHandler extends CommandEventHandler{

    Boolean isConflicted = false;

    public CourseConflictHandler(DataBase objDataBase, int iCommandEvCode, int iOutputEvCode) {
        super(objDataBase, iCommandEvCode, iOutputEvCode);
    }

    @Override
    protected String execute(String param) {
        // Parse the parameters.
        StringTokenizer objTokenizer = new StringTokenizer(param);
        String sSID     = objTokenizer.nextToken();
        String sCID     = objTokenizer.nextToken();
        String sSection = objTokenizer.nextToken();

        // Get the student and course records.
        Student objStudent = this.objDataBase.getStudentRecord(sSID);
        Course objCourse = this.objDataBase.getCourseRecord(sCID, sSection);

        ArrayList vCourse = objStudent.getRegisteredCourses();

        // Check if the given course conflicts with any of the courses the student has registered.
        for (int i=0; i<vCourse.size(); i++) {
            if (((Course) vCourse.get(i)).conflicts(objCourse)) {
                isConflicted = true;
                return "1" + " " +  sSID + " " + sCID + " " + sSection;
            }
        }
        return "0" + " " +  sSID + " " + sCID + " " + sSection;
    }

}
