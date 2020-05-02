package pt.isel.ls.configuration;

import pt.isel.ls.App;
import pt.isel.ls.Template;
import pt.isel.ls.TransactionManager;
import pt.isel.ls.commands.ExitApp;
import pt.isel.ls.commands.GetCommands;
import pt.isel.ls.commands.GetTime;
import pt.isel.ls.commands.StartServerListening;
import pt.isel.ls.commands.classes.CreateClassOnCourseOfAcr;
import pt.isel.ls.commands.classes.GetClassByCourseOfSemesterAndNum;
import pt.isel.ls.commands.classes.GetClassesOfCourse;
import pt.isel.ls.commands.classes.GetClassesOfCourseAndSemester;
import pt.isel.ls.commands.classes.GetClassesOfTeacherOfNum;
import pt.isel.ls.commands.course.CreateCourse;
import pt.isel.ls.commands.course.CreateCourseToProgrammeOfPid;
import pt.isel.ls.commands.course.GetCourseOfAcr;
import pt.isel.ls.commands.course.GetCourses;
import pt.isel.ls.commands.course.GetCoursesOfProgrammePid;
import pt.isel.ls.commands.forms.PostCoursesAcrForm;
import pt.isel.ls.commands.forms.PostCoursesForm;
import pt.isel.ls.commands.forms.PostGroupsAcrSemNumForm;
import pt.isel.ls.commands.forms.PostGroupsAcrSemNumGnoForm;
import pt.isel.ls.commands.forms.PostProgrammeForm;
import pt.isel.ls.commands.forms.PostProgrammesPid;
import pt.isel.ls.commands.forms.PostStudentsAcrSemNumForm;
import pt.isel.ls.commands.forms.PostStudentsForm;
import pt.isel.ls.commands.forms.PostTeachersAcrSemNumForm;
import pt.isel.ls.commands.forms.PostTeachersForm;
import pt.isel.ls.commands.group.AddGroupToClass;
import pt.isel.ls.commands.group.GetGroupOfNum;
import pt.isel.ls.commands.group.GetGroupsOfClass;
import pt.isel.ls.commands.programme.CreateProgramme;
import pt.isel.ls.commands.programme.GetProgramme;
import pt.isel.ls.commands.programme.GetProgrammeOfPid;
import pt.isel.ls.commands.student.AddStudentToClass;
import pt.isel.ls.commands.student.AddStudentToGroup;
import pt.isel.ls.commands.student.CreateStudent;
import pt.isel.ls.commands.student.DeleteStudentFromGroup;
import pt.isel.ls.commands.student.GetStudentOfNum;
import pt.isel.ls.commands.student.GetStudents;
import pt.isel.ls.commands.student.GetStudentsOfClass;
import pt.isel.ls.commands.student.UpdateStudent;
import pt.isel.ls.commands.teacher.AddTeacherToClass;
import pt.isel.ls.commands.teacher.CreateTeacher;
import pt.isel.ls.commands.teacher.GetTeacherOfNum;
import pt.isel.ls.commands.teacher.GetTeachers;
import pt.isel.ls.commands.teacher.GetTeachersOfClass;
import pt.isel.ls.commands.teacher.UpdateTeacher;
import pt.isel.ls.commands.user.GetUsers;
import pt.isel.ls.request.Method;

public class Configuration {

    private CommandProperties[] commandsProperties;
    private TransactionManager tm;
    private App app;

    public Configuration(App app, TransactionManager tm) {
        this.tm = tm;
        this.app = app;
    }

    public Configuration initProperties() {
        commandsProperties = new CommandProperties[]{
                // Forms
                new CommandProperties(Method.GET, Template.NEW_FORM_COURSES,
                        new PostCoursesForm()),

                new CommandProperties(Method.GET, Template.NEW_FORM_COURSES_ACR_CLASSES,
                        new PostCoursesAcrForm()),

                new CommandProperties(Method.GET, Template.NEW_FORM_PROGRAMMES,
                        new PostProgrammeForm()),

                new CommandProperties(Method.GET, Template.NEW_FORM_PROGRAMMES_PID_COURSES,
                        new PostProgrammesPid()),

                new CommandProperties(Method.GET, Template.NEW_FORM_TEACHERS,
                        new PostTeachersForm()),

                new CommandProperties(Method.GET, Template.NEW_FORM_STUDENTS,
                        new PostStudentsForm()),

                new CommandProperties(Method.GET,
                        Template
                                .NEW_FORM_COURSES_ACR_CLASSES_SEM_NUM_TEACHERS,
                        new PostTeachersAcrSemNumForm()),

                new CommandProperties(Method.GET,
                        Template
                                .NEW_FORM_COURSES_ACR_CLASSES_SEM_NUM_STUDENTS,
                        new PostStudentsAcrSemNumForm()),

                new CommandProperties(Method.GET,
                        Template.NEW_FORM_GROUPS,
                        new PostGroupsAcrSemNumForm()),

                new CommandProperties(Method.GET,
                        Template.NEW_FORM_GROUPS_GNO,
                        new PostGroupsAcrSemNumGnoForm()),

                // Options management
                new CommandProperties(Method.OPTION, Template.OPTION,
                        new GetCommands(this)),
                new CommandProperties(Method.EXIT, Template.OPTION,
                        new ExitApp()),
                new CommandProperties(Method.LISTEN, Template.OPTION,
                        new StartServerListening(app)),

                // Course management
                new CommandProperties(Method.GET, Template.COURSES,
                        new GetCourses(tm)),

                new CommandProperties(Method.GET, Template.COURSES_ACR,
                        new GetCourseOfAcr(tm)),

                new CommandProperties(Method.POST, Template.COURSES,
                        new CreateCourse(tm)),

                new CommandProperties(Method.POST, Template.COURSES_ACR_CLASSES,
                        new CreateClassOnCourseOfAcr(tm)),

                new CommandProperties(Method.GET, Template.COURSES_ACR_CLASSES,
                        new GetClassesOfCourse(tm)),

                new CommandProperties(Method.GET, Template.COURSES_ACR_CLASSES_SEM,
                        new GetClassesOfCourseAndSemester(tm)),

                new CommandProperties(Method.GET, Template.COURSES_ACR_CLASSES_SEM_NUM,
                        new GetClassByCourseOfSemesterAndNum(tm)),

                // Programme management
                new CommandProperties(Method.GET, Template.PROGRAMMES,
                        new GetProgramme(tm)),
                new CommandProperties(Method.GET, Template.PROGRAMMES_PID,
                        new GetProgrammeOfPid(tm)),
                new CommandProperties(Method.GET, Template.PROGRAMMES_PID_COURSES,
                        new GetCoursesOfProgrammePid(tm)),
                new CommandProperties(Method.POST, Template.PROGRAMMES,
                        new CreateProgramme(tm)),
                new CommandProperties(Method.POST, Template.PROGRAMMES_PID_COURSES,
                        new CreateCourseToProgrammeOfPid(tm)),

                // User management
                new CommandProperties(Method.GET, Template.USERS,
                        new GetUsers(tm)),

                //  Student management
                new CommandProperties(Method.POST, Template.STUDENTS,
                        new CreateStudent(tm)),
                new CommandProperties(Method.GET, Template.STUDENTS,
                        new GetStudents(tm)),
                new CommandProperties(Method.GET, Template.STUDENTS_NUM,
                        new GetStudentOfNum(tm)),
                new CommandProperties(Method.POST, Template.COURSES_ACR_CLASSES_SEM_NUM_STUDENTS,
                        new AddStudentToClass(tm)),
                new CommandProperties(Method.GET, Template.COURSES_ACR_CLASSES_SEM_NUM_STUDENTS,
                        new GetStudentsOfClass(tm)),
                new CommandProperties(Method.PUT, Template.STUDENTS_NUM,
                        new UpdateStudent(tm)),

                //  Group management
                new CommandProperties(Method.POST, Template.GROUPS,
                        new AddGroupToClass(tm)),

                new CommandProperties(Method.GET, Template.GROUPS,
                        new GetGroupsOfClass(tm)),

                new CommandProperties(Method.GET, Template.GROUPS_GNO,
                        new GetGroupOfNum(tm)),

                new CommandProperties(Method.POST, Template.GROUPS_GNO_STUDENTS,
                        new AddStudentToGroup(tm)),

                new CommandProperties(Method.DELETE, Template.STUDENT_GROUP,
                        new DeleteStudentFromGroup(tm)),

                //  Teacher management
                new CommandProperties(Method.GET, Template.TEACHERS,
                        new GetTeachers(tm)),

                new CommandProperties(Method.GET, Template.TEACHERS_NUM,
                        new GetTeacherOfNum(tm)),

                new CommandProperties(Method.POST, Template.TEACHERS,
                        new CreateTeacher(tm)),

                new CommandProperties(Method.POST,  Template.COURSES_ACR_CLASSES_SEM_NUM_TEACHERS,
                        new AddTeacherToClass(tm)),

                new CommandProperties(Method.GET, Template.COURSES_ACR_CLASSES_SEM_NUM_TEACHERS,
                        new GetTeachersOfClass(tm)),

                new CommandProperties(Method.GET, Template.TEACHERS_NUM_CLASSES,
                        new GetClassesOfTeacherOfNum(tm)),

                new CommandProperties(Method.PUT, Template.TEACHERS_NUM,
                        new UpdateTeacher(tm)),

                //  Time management
                new CommandProperties(Method.GET, Template.TIME,
                        new GetTime())
        };
        return this;
    }

    public CommandProperties[] getCommandsProperties() {
        return commandsProperties;
    }
}