package daos;

import entities.Assignment;
import entities.AssignmentsPerCourse;
import entities.Course;
import entities.Student;
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

public class AssignmentsPerCourseDao {

    private final String URL = "jdbc:mysql://localhost:3306/privateschool?serverTimezone=UTC";
    private final String USERNAME = "root";
    private final String PASS = "12345";
    private Connection conn;
    private final String getAssignmentsPerCourses = "SELECT a.*, c.*, ct.tname FROM course AS c, assignment AS a, ctype AS ct "
            + "WHERE c.cid = a.cid AND ct.typeid = ctype";
    private final String getAssignmentsPerCoursePerStudent = "SELECT * FROM course AS c, assignment AS a, student AS s, student_assignment AS sa, ctype AS ct "
            + "WHERE c.cid = a.cid AND sa.sid = s.sid AND sa.aid = a.aid AND ct.typeid = c.ctype AND s.sid = ?";

    private Connection getConnection() {
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASS);
        } catch (SQLException ex) {
            Logger.getLogger(AssignmentsPerCourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    private void closeConnections(ResultSet rs, Statement st) {
        try {
            rs.close();
            st.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(AssignmentsPerCourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      private void closeConnections(PreparedStatement pst) {
        try {
            pst.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(AssignmentsPerCourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<AssignmentsPerCourse> getAssignmentsPerCourses() {
        List<AssignmentsPerCourse> assignmentsPerCourses = new ArrayList();
        CourseDao cdao = new CourseDao();
        for (Course c : cdao.getCourses()) {
            assignmentsPerCourses.add(new AssignmentsPerCourse(c));
        }
        try {
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(getAssignmentsPerCourses);

            while (rs.next()) {
                for (AssignmentsPerCourse apc : assignmentsPerCourses) {
                    if (apc.getCourse().getId() == rs.getInt(5)) {
                        apc.getAssignments().add(new Assignment(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4).toLocalDate()));
                    }
                }
            }
            closeConnections(rs, st);

        } catch (SQLException ex) {
            Logger.getLogger(TrainersPerCourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return assignmentsPerCourses;

    }

    //επιστρέφει μια λίστα με τις εργασίες ανά μάθημα του επιλεγμένου μαθητή 
    public List<AssignmentsPerCourse> getAssignmentsPerCoursesByStudent(Student s) {
        List<AssignmentsPerCourse> assignmentsPerCourses = new ArrayList();
         
        CourseDao cdao = new CourseDao();
        try {
            PreparedStatement pst = getConnection().prepareStatement(getAssignmentsPerCoursePerStudent);
            pst.setInt(1, s.getId());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                    boolean courseExists = false;
                    for(AssignmentsPerCourse apc:assignmentsPerCourses) {
                        if (apc.getCourse().getId() == rs.getInt(1)) {
                            courseExists = true;
                        }
                    }
                   if(!courseExists) {
                       assignmentsPerCourses.add(new AssignmentsPerCourse(new Course(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4).toLocalDate(), rs.getDate(5).toLocalDate(), 
                               (rs.getInt(6)) == 1 ? "part" : "full")));
                   }
                   
                   for(AssignmentsPerCourse apc:assignmentsPerCourses) {
                       if(apc.getCourse().getId() == rs.getInt(1)) {
                           apc.getAssignments().add(new Assignment(rs.getInt(7), rs.getString(8), rs.getString(9), rs.getDate(10).toLocalDate(), rs.getDouble(18), rs.getDouble(19)));
                       }
                   }
                
            }
            closeConnections(pst);
            

        } catch (SQLException ex) {
            Logger.getLogger(AssignmentsPerCourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return assignmentsPerCourses;

    }
    
    
    
    
}
