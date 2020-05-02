package pt.isel.ls.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import pt.isel.ls.model.entities.Class;
import pt.isel.ls.model.entities.Course;
import pt.isel.ls.model.entities.Group;
import pt.isel.ls.model.entities.Programme;
import pt.isel.ls.model.entities.Student;
import pt.isel.ls.model.entities.Teacher;
import pt.isel.ls.model.entities.User;

public class EntitySet {

    public static List<Class> getClasses(PreparedStatement ps) throws SQLException {
        List<Class> classes = new LinkedList<>();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            classes.add(new Class(rs.getString(1), rs.getInt(2),
                    rs.getString(3), rs.getString(4)));
        }
        return classes;
    }

    public static List<Course> getCourses(PreparedStatement ps) throws SQLException {
        List<Course> courses = new LinkedList<>();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            courses.add(new Course(rs.getString(1), rs.getString(2),
                    rs.getInt(3)));
        }
        return courses;
    }

    public static List<Group> getGroups(PreparedStatement ps) throws SQLException {
        List<Group> groups = new LinkedList<>();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            groups.add(new Group(rs.getInt(1),rs.getString(2),
                    rs.getInt(3),rs.getString(4),rs.getString(5)));
        }
        return groups;
    }

    public static List<Programme> getProgrammes(PreparedStatement ps) throws SQLException {
        List<Programme> programmes = new LinkedList<>();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            programmes.add(new Programme(rs.getString(1),
                    rs.getString(2), rs.getInt(3)));
        }
        return programmes;
    }

    public static List<Student> getStudents(PreparedStatement ps) throws SQLException {
        List<Student> students = new LinkedList<>();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            students.add(new Student(rs.getInt(1),
                    rs.getString(2), rs.getString(3)));
        }
        return students;
    }

    public static List<Teacher> getTeachers(PreparedStatement ps) throws SQLException {
        List<Teacher> teachers = new LinkedList<>();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            teachers.add(new Teacher(rs.getInt(1), rs.getString(2)));
        }
        return teachers;
    }

    public static List<User> getUsers(PreparedStatement ps) throws SQLException {
        List<User> users = new LinkedList<>();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            users.add(new User(rs.getString(1), rs.getString(2)));
        }
        return users;
    }

}
