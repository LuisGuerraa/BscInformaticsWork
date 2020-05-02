package pt.isel.ls;

public class Template {

    //                                      TEMPLATES

    public static final String OPTION = "/";

    public static final String TIME = "/time";

    public static final String COURSES = "/courses";

    public static final String PROGRAMMES = "/programmes";

    public static final String STUDENTS = "/students";

    public static final String TEACHERS = "/teachers";

    public static final String USERS = "/users";

    public static final String COURSES_ACR = "/courses/{acr}";

    public static final String PROGRAMMES_PID = "/programmes/{pid}";

    public static final String STUDENTS_NUM = "/students/{num}";

    public static final String TEACHERS_NUM = "/teachers/{num}";

    public static final String COURSES_ACR_CLASSES = "/courses/{acr}/classes";

    public static final String PROGRAMMES_PID_COURSES = "/programmes/{pid}/courses";

    public static final String COURSES_ACR_CLASSES_SEM = "/courses/{acr}/classes/{sem}";

    public static final String COURSES_ACR_CLASSES_SEM_NUM = "/courses/{acr}/classes/{sem}/{num}";

    public static final String COURSES_ACR_CLASSES_SEM_NUM_STUDENTS = "/courses/{acr}/classes"
            + "/{sem}/{num}/students";

    public static final String COURSES_ACR_CLASSES_SEM_NUM_TEACHERS = "/courses/{acr}/classes"
            + "/{sem}/{num}/teachers";

    public static final String TEACHERS_NUM_CLASSES = "/teachers/{num}/classes";

    public static final String GROUPS = "/courses/{acr}/classes/{sem}/{num}/groups";

    public static final String GROUPS_GNO = "/courses/{acr}/classes/{sem}/{num}/groups/{gno}";

    public static final String GROUPS_GNO_NUMBER_OF_STUDENTS_TO_ADD =
            "/courses/{acr}/classes/{sem}/{num}/groups/{gno}/{students}";

    public static final String GROUPS_GNO_STUDENTS = "/courses/{acr}/classes"
            + "/{sem}/{num}/groups/{gno}/students";

    public static final String STUDENT_GROUP = "/courses/{acr}/classes"
            + "/{sem}/{num}/groups/{gno}/students/{numStu}";


    //                                     NEW FORMS


    private static final String NEW_FORM = "/new%s";

    public static final String NEW_FORM_COURSES = newForm(COURSES); // -> /new/courses

    public static final String NEW_FORM_COURSES_ACR_CLASSES
            = newForm(COURSES_ACR_CLASSES); // /new/courses/{acr}/classes

    public static final String NEW_FORM_PROGRAMMES = newForm(PROGRAMMES);

    public static final String NEW_FORM_PROGRAMMES_PID_COURSES = newForm(PROGRAMMES_PID_COURSES);

    public static final String NEW_FORM_TEACHERS = newForm(TEACHERS);

    public static final String NEW_FORM_STUDENTS = newForm(STUDENTS);

    public static final String NEW_FORM_COURSES_ACR_CLASSES_SEM_NUM_TEACHERS =
            newForm(COURSES_ACR_CLASSES_SEM_NUM_TEACHERS);

    public static final String NEW_FORM_COURSES_ACR_CLASSES_SEM_NUM_STUDENTS =
            newForm(COURSES_ACR_CLASSES_SEM_NUM_STUDENTS);

    public static final String NEW_FORM_GROUPS = newForm(GROUPS);

    public static final String NEW_FORM_GROUPS_GNO = newForm(GROUPS_GNO);

    public static final String NEW_FORM_GROUPS_GNO_STUDENTS_TO_ADD =
            newForm(GROUPS_GNO_NUMBER_OF_STUDENTS_TO_ADD);


    //                                     OTHER FORMS


    private static final String COURSES_ACR_FORM = "/courses/%s";

    private static final String COURSES_ACR_CLASSES_FORM = "/courses/%s/classes";

    private static final String COURSES_ACR_CLASSES_SEM_FORM = "/courses/%s/classes/%s";

    public static final String COURSES_ACR_CLASSES_SEM_NUM_FORM = "/courses/%s/classes/%s/%s";

    private static final String TEACHER_CLASS_FORM = "/courses/%s/classes/%s/%s/teachers";

    private static final String STUDENT_CLASS_FORM = "/courses/%s/classes/%s/%s/students";

    private static final String GROUP_CLASS_FORM = "/courses/%s/classes/%s/%s/groups";

    private static final String STUDENT_GROUP_FORM = "/courses/%s/classes/%s/%s/groups/%s/students";

    public static final String GROUP_GNO_FORM = "/courses/%s/classes/%s/%s/groups/%s";

    private static final String STUDENTS_NUM_FORM = "/students/%s";

    private static final String TEACHERS_NUM_FORM = "/teachers/%s";

    private static final String PROGRAMMES_PID_FORM = "/programmes/%s";

    private static final String PROGRAMMES_PID_COURSES_FORM = "/programmes/%s/courses";



    //                                      METHODS


    public static String newForm(String path) {
        return String.format(NEW_FORM, path);
    }

    public static String getCoursesAcr(String acr) {
        return String.format(COURSES_ACR_FORM, acr);
    }

    public static String getCoursesAcrClasses(String acr) {
        return String.format(COURSES_ACR_CLASSES_FORM, acr);
    }

    public static String getCoursesAcrClassesSem(String acr, String sem) {
        return String.format(COURSES_ACR_CLASSES_SEM_FORM, acr, sem);
    }

    public static String getCoursesAcrClassesSemNum(String acr, String sem, String num) {
        return String.format(COURSES_ACR_CLASSES_SEM_NUM_FORM, acr, sem, num);
    }

    public static String getProgrammesPidCourses(String pid) {
        return String.format(PROGRAMMES_PID_COURSES_FORM, pid);
    }

    public static String getProgrammesPid(String pid) {
        return String.format(PROGRAMMES_PID_FORM, pid);
    }

    public static String getStudentOfNum(int num) {
        return String.format(STUDENTS_NUM_FORM, num);
    }

    public static String getTeacherOfNum(int num) {
        return String.format(TEACHERS_NUM_FORM, num);
    }



    public static String getTeacherClass(String acr, String sem, String num) {
        return  String.format(TEACHER_CLASS_FORM, acr, sem, num);
    }

    public static String getStudentClass(String acr, String sem, String num) {
        return  String.format(STUDENT_CLASS_FORM, acr, sem, num);
    }

    public static String getGroupsClass(String acr, String sem, String num) {
        return  String.format(GROUP_CLASS_FORM, acr, sem, num);
    }

    public static String getStudentGroup(String acr, String sem, String num, int gno) {
        return String.format(STUDENT_GROUP_FORM, acr, sem, num, gno);
    }

    public static String getGroupsGno(String acr, String sem, String num, int gno) {
        return String.format(GROUP_GNO_FORM, acr, sem, num, gno);
    }

}
