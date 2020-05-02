package pt.isel.ls;

import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.postgresql.ds.PGSimpleDataSource;
import pt.isel.ls.commands.classes.GetClassByCourseOfSemesterAndNum;
import pt.isel.ls.commands.classes.GetClassesOfCourse;
import pt.isel.ls.commands.classes.GetClassesOfCourseAndSemester;
import pt.isel.ls.commands.classes.GetClassesOfTeacherOfNum;
import pt.isel.ls.commands.course.GetCourseOfAcr;
import pt.isel.ls.commands.course.GetCourses;
import pt.isel.ls.commands.course.GetCoursesOfProgrammePid;
import pt.isel.ls.commands.group.GetGroupOfNum;
import pt.isel.ls.commands.group.GetGroupsOfClass;
import pt.isel.ls.commands.programme.GetProgramme;
import pt.isel.ls.commands.programme.GetProgrammeOfPid;
import pt.isel.ls.commands.student.GetStudentOfNum;
import pt.isel.ls.commands.student.GetStudents;
import pt.isel.ls.commands.student.GetStudentsOfClass;
import pt.isel.ls.commands.teacher.GetTeacherOfNum;
import pt.isel.ls.commands.teacher.GetTeachers;
import pt.isel.ls.commands.teacher.GetTeachersOfClass;
import pt.isel.ls.commands.user.GetUsers;
import pt.isel.ls.configuration.Connector;
import pt.isel.ls.model.Entity;
import pt.isel.ls.model.entities.Class;
import pt.isel.ls.model.entities.Course;

import pt.isel.ls.model.entities.Group;
import pt.isel.ls.model.entities.Programme;
import pt.isel.ls.model.entities.Student;
import pt.isel.ls.model.entities.Teacher;
import pt.isel.ls.model.entities.User;

import pt.isel.ls.request.Parameter;
import pt.isel.ls.result.Result;

public class GetMethodsTests {
    private static TransactionManager tm;

    @BeforeClass
    public static void startTest() {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        String jdbcUrl = System.getenv("JDBC_DATABASE_URL");
        if (jdbcUrl == null) {
            UserInterface.log("JDBC_DATABASE_URL is not defined, ending");
            return;
        }
        ds.setUrl(jdbcUrl);
        tm = new TransactionManager(new Connector(ds));
    }

    @Test
    public void getUsers() throws SQLException {
        GetUsers getUsers = new GetUsers(tm);
        final List<Result> list = getUsers.execute(new Parameter()).getResults();

        final List<Entity> list1 = list.get(0).getListOfEntities();
        final List<Entity> listCmp = new LinkedList<>();
        listCmp.add(new User("professor1@gmail.com", "Pedro Felix"));
        listCmp.add(new User("professor2@outlook.com", "Joao Oliveira"));
        listCmp.add(new User("professor3@gmail.com", "Jose Gomes"));
        listCmp.add(new User("professor4@gmail.com", "Manuel Machado"));
        listCmp.add(new User("professor5@hotmail.com", "Maria Tavares"));
        listCmp.add(new User("aluno1@outlook.com", "Filipe Cardoso"));
        listCmp.add(new User("aluno2@gmail.com", "Diogo Mendes"));
        listCmp.add(new User("aluno3@hotmail.com", "Joana Bastos"));
        listCmp.add(new User("aluno4@outlook.com", "Andre Santos"));
        listCmp.add(new User("aluno5@gmail.com", "Filipa Andrade"));
        listCmp.add(new User("aluno6@hotmail.com", "Tiago Santana"));
        listCmp.add(new User("luis@hotmail.com", "Luis Guerra"));
        listCmp.add(new User("pedro@gmail.com", "Pedro Tereso"));
        listCmp.add(new User("wilson@hotmail.com", "Wilson Costa"));
        Assert.assertEquals(list1, listCmp);
    }


    @Test
    public void getStudents() throws SQLException {
        GetStudents getStudents = new GetStudents(tm);
        final List<Result> listResults = getStudents.execute(new Parameter()).getResults();

        final List<Entity> students = listResults.get(0).getListOfEntities();
        final List<Entity> list = new LinkedList<>();
        list.add(new Student(40001, "aluno1@outlook.com", "LEIC"));
        list.add(new Student(40002, "aluno2@gmail.com", "LEIC"));
        list.add(new Student(40003, "aluno3@hotmail.com", "LEIC"));
        list.add(new Student(40004, "aluno4@outlook.com", "LMATE"));
        list.add(new Student(40005, "aluno5@gmail.com", "LMATE"));
        list.add(new Student(40006, "aluno6@hotmail.com", "LMATE"));
        list.add(new Student(43593, "wilson@hotmail.com", "LEIC"));
        list.add(new Student(43755, "luis@hotmail.com", "LEIC"));
        list.add(new Student(44015, "pedro@gmail.com", "LEIC"));
        Assert.assertEquals(students, list);
    }

    @Test
    public void getStudentOfNum() throws SQLException {
        GetStudentOfNum getStudentOfNum = new GetStudentOfNum(tm);
        final List<Result> results = getStudentOfNum.execute(
                new Parameter("num=43593")).getResults();

        final List<Entity> listStudent = results.get(0).getListOfEntities();

        Assert.assertEquals(listStudent, new LinkedList<Entity>(Collections.singleton(
                new Student(43593, "wilson@hotmail.com", "LEIC"))));

        final List<Entity> classes = results.get(1).getListOfEntities();
        final List<Entity> listCmp = new LinkedList<>();
        listCmp.add(new Class("41D", 1819, "v", "LS"));
        listCmp.add(new Class("41D", 1819, "v", "AVE"));
        listCmp.add(new Class("41D", 1819, "v", "SO"));

        Assert.assertEquals(classes, listCmp);

        final List<Entity> groups = results.get(2).getListOfEntities();
        List<Entity> list = new LinkedList<>();
        list.add(new Group(1, "41D", 1819, "v", "LS"));
        list.add(new Group(1, "41D", 1819, "v", "AVE"));
        list.add(new Group(1, "41D", 1819, "v", "SO"));

        Assert.assertEquals(groups, list);
    }


    @Test
    public void getTeachers() throws SQLException {
        GetTeachers getTeachers = new GetTeachers(tm);
        final List<Result> results = getTeachers.execute(new Parameter()).getResults();

        final List<Entity> listCmp = new LinkedList<>();
        listCmp.add(new Teacher(1, "professor1@gmail.com"));
        listCmp.add(new Teacher(2, "professor2@outlook.com"));
        listCmp.add(new Teacher(3, "professor3@gmail.com"));
        listCmp.add(new Teacher(4, "professor4@gmail.com"));
        listCmp.add(new Teacher(5, "professor5@hotmail.com"));
        Assert.assertEquals(results.get(0).getListOfEntities(), listCmp);
    }

    @Test
    public void getTeachersOfClass() throws SQLException {
        // get /courses/LS/classes/1819v/41D/teachers
        GetTeachersOfClass getTeachersOfClass = new GetTeachersOfClass(tm);
        List<Result> results = getTeachersOfClass.execute(
                new Parameter("num=41D&sem=1819v&acr=LS")).getResults();

        List<Entity> listCmp = new LinkedList<>(
                Collections.singleton(new Teacher(1, "professor1@gmail.com"))
        );
        Assert.assertEquals(results.get(0).getListOfEntities(), listCmp);
    }


    @Test
    public void getTeacherOfNum() throws SQLException {
        GetTeacherOfNum getTeacherOfNum = new GetTeacherOfNum(tm);
        List<Result> results = getTeacherOfNum.execute(new Parameter("num=3")).getResults();

        List<Entity> listCmp = new LinkedList<>(
                Collections.singleton(new Teacher(3, "professor3@gmail.com")));
        Assert.assertEquals(results.get(0).getListOfEntities(), listCmp);

        listCmp = new LinkedList<>(
                Collections.singleton(new Course("SO", "Sistemas Operativos", 4)));
        Assert.assertEquals(results.get(1).getListOfEntities(), listCmp);
    }

    @Test
    public void getProgramme() throws SQLException {
        GetProgramme getProgramme = new GetProgramme(tm);
        final List<Result> results = getProgramme.execute(new Parameter()).getResults();

        final List<Entity> listCmp = new LinkedList<>();
        listCmp.add(new Programme("LEIC", "Engenharia Informatica e de Computadores", 6));
        listCmp.add(new Programme("LEIM", "Engenharia Informatica e Multimedia", 6));
        listCmp.add(new Programme("LMATE", "Engenharia de Matematica Aplicada", 6));
        Assert.assertEquals(results.get(0).getListOfEntities(), listCmp);
    }


    @Test
    public void getProgrammeOfPiD() throws SQLException {
        GetProgrammeOfPid getProgrammeOfPid = new GetProgrammeOfPid(tm);
        final List<Result> results = getProgrammeOfPid.execute(
                new Parameter("pid=LEIC")).getResults();

        final List<Entity> listCmp = new LinkedList<>(Collections.singleton(
                new Programme("LEIC", "Engenharia Informatica e de Computadores", 6)));
        Assert.assertEquals(results.get(0).getListOfEntities(), listCmp);
    }

    @Test
    public void getCourse() throws SQLException {
        GetCourses getCourses = new GetCourses(tm);
        final List<Result> results = getCourses.execute(new Parameter()).getResults();

        final List<Entity> listCmp = new LinkedList<>();
        listCmp.add(new Course("LS", "Laboratorio de Software", 1));
        listCmp.add(new Course("AVE", "Ambientes Virtuais de Execucao", 2));
        listCmp.add(new Course("IA", "Inteligencia Artificial", 3));
        listCmp.add(new Course("SO", "Sistemas Operativos", 4));
        listCmp.add(new Course("MPD", "Modelacao e Padroes Desenho", 5));
        Assert.assertEquals(results.get(0).getListOfEntities(), listCmp);
    }


    @Test
    public void getCoursesOfAcr() throws SQLException {
        GetCourseOfAcr getCourseOfAcr = new GetCourseOfAcr(tm);
        final List<Result> results = getCourseOfAcr.execute(new Parameter("acr=LS")).getResults();

        List<Entity> listCmp = new LinkedList<>(Collections.singleton(
                new Teacher(1, "professor1@gmail.com")));
        Assert.assertEquals(results.get(0).getListOfEntities(), listCmp);

        listCmp = new LinkedList<>(Collections.singleton(
                new Programme("LEIC", "Engenharia Informatica e de Computadores", 6)));
        Assert.assertEquals(results.get(1).getListOfEntities(), listCmp);
    }

    @Test
    public void getCoursesOfProgrammeId() throws SQLException {
        //get /programmes/LEIC/courses
        final GetCoursesOfProgrammePid getCoursesOfProgrammePid = new GetCoursesOfProgrammePid(tm);
        final List<Result> results = getCoursesOfProgrammePid.execute(
                new Parameter("pid=LEIC")).getResults();

        final List<Entity> listCmp = new LinkedList<>();
        listCmp.add(new Course("AVE", "Ambientes Virtuais de Execucao", 2));
        listCmp.add(new Course("IA", "Inteligencia Artificial", 3));
        listCmp.add(new Course("LS", "Laboratorio de Software", 1));
        listCmp.add(new Course("MPD", "Modelacao e Padroes Desenho", 5));
        listCmp.add(new Course("SO", "Sistemas Operativos", 4));
        Assert.assertEquals(results.get(0).getListOfEntities(), listCmp);
    }

    @Test
    public void getClassesOfCourse() throws SQLException {
        //get /courses/LS/classes
        final GetClassesOfCourse getClassesOfCourse = new GetClassesOfCourse(tm);
        final List<Result> results = getClassesOfCourse.execute(
                new Parameter("acr=LS")).getResults();

        final List<Entity> listCmp = new LinkedList<>();
        listCmp.add(new Class("41D", 1718, "i", "LS"));
        listCmp.add(new Class("41D", 1819, "v", "LS"));
        listCmp.add(new Class("41D", 1819, "i", "LS"));
        listCmp.add(new Class("42D", 1819, "v", "LS"));
        listCmp.add(new Class("42D", 1819, "i", "LS"));
        Assert.assertEquals(results.get(0).getListOfEntities(), listCmp);
    }

    @Test
    public void getClassesByCourseOfSemesterAndNum() throws SQLException {
        //get /courses/LS/classes/1819v/41D
        GetClassByCourseOfSemesterAndNum getClassByCourseOfSemesterAndNum =
                new GetClassByCourseOfSemesterAndNum(tm);

        final List<Result> results = getClassByCourseOfSemesterAndNum.execute(
                new Parameter("acr=LS&sem=1819v&num=41D")).getResults();

        List<Entity> listCmp = new LinkedList<>();
        listCmp.add(new Group(1, "41D", 1819, "v", "LS"));
        listCmp.add(new Group(2, "41D", 1819, "v", "LS"));
        Assert.assertEquals(results.get(0).getListOfEntities(), listCmp);
    }

    @Test
    public void getClassesOfCourseAndSemester() throws SQLException {
        GetClassesOfCourseAndSemester getClassesOfCourseAndSemester =
                new GetClassesOfCourseAndSemester(tm);
        final List<Result> results = getClassesOfCourseAndSemester.execute(
                new Parameter("acr=LS&sem=1819v")).getResults();

        List<Entity> listCmp = new LinkedList<>();
        listCmp.add(new Class("41D", 1819, "v", "LS"));
        listCmp.add(new Class("42D", 1819, "v", "LS"));
        Assert.assertEquals(results.get(0).getListOfEntities(), listCmp);
    }

    @Test
    public void getClassesOfTeacherOfNum() throws SQLException {
        //get /teachers/2/classes
        GetClassesOfTeacherOfNum getClassesOfTeacherOfNum = new GetClassesOfTeacherOfNum(tm);
        final List<Result> results = getClassesOfTeacherOfNum.execute(
                new Parameter("num=2")).getResults();

        List<Entity> listCmp = new LinkedList<>();
        listCmp.add(new Class("41D", 1718, "i", "AVE"));
        listCmp.add(new Class("41D", 1819, "v", "AVE"));
        listCmp.add(new Class("41D", 1819, "i", "AVE"));
        listCmp.add(new Class("42D", 1819, "v", "AVE"));
        listCmp.add(new Class("42D", 1819, "i", "AVE"));
        Assert.assertEquals(results.get(0).getListOfEntities(), listCmp);
    }

    @Test
    public void getStudentsOfClass() throws SQLException {
        // get /courses/LS/classes/1819v/41D/students
        GetStudentsOfClass getStudentsOfClass = new GetStudentsOfClass(tm);
        final List<Result> results = getStudentsOfClass.execute(
                new Parameter("num=41D&sem=1819v&acr=LS")).getResults();

        List<Entity> listCmp = new LinkedList<>();
        listCmp.add(new Student(43593, "wilson@hotmail.com", "LEIC"));
        listCmp.add(new Student(43755, "luis@hotmail.com", "LEIC"));
        listCmp.add(new Student(44015, "pedro@gmail.com", "LEIC"));
        listCmp.add(new Student(40001, "aluno1@outlook.com", "LEIC"));
        listCmp.add(new Student(40002, "aluno2@gmail.com", "LEIC"));
        listCmp.add(new Student(40003, "aluno3@hotmail.com", "LEIC"));
        Assert.assertEquals(results.get(0).getListOfEntities(), listCmp);
    }

    @Test
    public void getGroupsOfClass() throws SQLException {
        //get /courses/LS/classes/1819v/41D/groups
        GetGroupsOfClass getGroupsOfClass = new GetGroupsOfClass(tm);
        final List<Result> results = getGroupsOfClass.execute(
                new Parameter("acr=LS&sem=1819v&num=41D")).getResults();

        List<Entity> listCmp = new LinkedList<>();
        listCmp.add(new Group(1, "41D", 1819, "v", "LS"));
        listCmp.add(new Group(2, "41D", 1819, "v", "LS"));
        Assert.assertEquals(results.get(0).getListOfEntities(), listCmp);
    }

    @Test
    public void getGroupOfNum() throws SQLException {
        //get /courses/LS/classes/1819v/41D/groups/1
        GetGroupOfNum getGroupOfNum = new GetGroupOfNum(tm);
        final List<Result> results = getGroupOfNum
                .execute(new Parameter("acr=LS&sem=1819v&num=41D&gno=1")).getResults();

        List<Entity> listCmp = new LinkedList<>();
        listCmp.add(new Student(44015, "pedro@gmail.com", "LEIC"));
        listCmp.add(new Student(43593, "wilson@hotmail.com", "LEIC"));
        listCmp.add(new Student(43755, "luis@hotmail.com", "LEIC"));
        Assert.assertEquals(results.get(0).getListOfEntities(), listCmp);
    }

}