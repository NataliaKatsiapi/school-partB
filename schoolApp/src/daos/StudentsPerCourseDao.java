package daos;

import entities.Course;
import entities.Student;
import entities.StudentsPerCourse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentsPerCourseDao {

    private final String URL = "jdbc:mysql://localhost:3306/privateschool?serverTimezone=UTC"; 
    private final String USERNAME = "root";
    private final String PASS = "12345";
    private final String getStudentsPerCourses = "SELECT s.*, c.*, t.tname FROM student AS s, course AS c, course_student AS cs, ctype AS t WHERE cs.cid = c.cid AND cs.sid = s.sid AND c.ctype = t.typeid";
    private final String insertStudentCourse = "INSERT INTO course_student(cid, sid) VALUES(?,?)";
    private final String getStudentsWithMultipleCourses = "SELECT s.*, count(cs.sid) AS NumberOfCourses FROM course_student AS cs, student AS s "
            + "WHERE cs.sid = s.sid GROUP BY cs.sid HAVING count(cs.sid) > ?";

    private Connection conn;

    private Connection getConnection() {
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASS);
        } catch (SQLException ex) {
            Logger.getLogger(StudentsPerCourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    private void closeConnections(ResultSet rs, Statement st) {
        try {
            rs.close();
            st.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(StudentsPerCourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     private void closeConnections(PreparedStatement pst) {
        try {
            pst.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(StudentsPerCourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<StudentsPerCourse> getStudentsPerCourses() {
        List<StudentsPerCourse> studentsPerCourses = new ArrayList();
        CourseDao cdao = new CourseDao();
        for(Course c : cdao.getCourses()) {
            studentsPerCourses.add(new StudentsPerCourse(c));
        }
        try {
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(getStudentsPerCourses);

            while(rs.next()) {
                for (StudentsPerCourse spc:studentsPerCourses) {
                    if (spc.getCourse().getId() == rs.getInt(6)) {
                        spc.getStudents().add(new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getDate(5).toLocalDate()));
                    }
                }
            }
            closeConnections(rs, st);
            
        } catch (SQLException ex) {
            Logger.getLogger(StudentsPerCourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return studentsPerCourses;

    }
      public boolean insertStudentCourse(Student s, Course c) {
        boolean inserted = false;
        try {
            PreparedStatement pst = getConnection().prepareStatement(insertStudentCourse);
            pst.setInt(1, c.getId());
            pst.setInt(2, s.getId());
          
           int result = pst.executeUpdate();
            if (result > 0) {
                System.out.println("Student inserted successfully into course");
                inserted = true;
            }
            closeConnections(pst);

        } catch (SQLException ex) {
            Logger.getLogger(StudentsPerCourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return inserted;
    }
      
    public List<Student> getStudentsWithMultipleCourses() {
        List<Student> students = new ArrayList();
        try {
           PreparedStatement pst = getConnection().prepareStatement(getStudentsWithMultipleCourses);
           pst.setInt(1, 1);
           
           ResultSet rs = pst.executeQuery();
           while(rs.next()) {
               students.add(new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getDate(5).toLocalDate()));
           }
            closeConnections(pst);
           
        } catch (SQLException ex) {
            Logger.getLogger(StudentsPerCourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return students;
    }
}
