package daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import entities.Assignment;
import entities.Student;
import java.sql.Date;
import java.sql.PreparedStatement;
public class AssignmentDao {
    
    private final String URL = "jdbc:mysql://localhost:3306/privateschool?serverTimezone=UTC"; // αντί για localhost μπορούμε να βάλουμε μια ip
    private final String USERNAME = "root";
    private final String PASS = "12345";
    private final String getAssignments = "SELECT * FROM assignment";
    private final String insertAssignment = "INSERT INTO assignment(atitle, adescr, asub_date, cid) VALUES(?,?,?,?)";
    private final String getAssignmentById = "SELECT * FROM assignment WHERE aid = ?";
    private final String insertStudentAssignment = "INSERT INTO student_assignment(oralmark, totalmark, sid, aid) VALUES(?,?,?,?)";
    private Connection conn;

    private Connection getConnection() {
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASS);
        } catch (SQLException ex) {
            Logger.getLogger(AssignmentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    private void closeConnections(ResultSet rs, Statement st) {
        try {
            rs.close();
            st.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(AssignmentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void closeConnections(PreparedStatement pst) {
        try {
            pst.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(AssignmentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Assignment> getAssignments() {
        List<Assignment> assignments = new ArrayList();
        try {
            CourseDao cdao = new CourseDao();
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(getAssignments);

            while (rs.next()) {
                Date subDate = rs.getDate(4);
                assignments.add(new Assignment(rs.getInt(1), rs.getString(2), rs.getString(3), subDate.toLocalDate(), cdao.getCourseById(rs.getInt(5))));
            }
            closeConnections(rs, st);

        } catch (SQLException ex) {
            Logger.getLogger(AssignmentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return assignments;
    }
    
    public boolean insertAssignment(Assignment a) {
        boolean inserted = false;
        try {
            PreparedStatement pst = getConnection().prepareStatement(insertAssignment);
            pst.setString(1, a.getTitle());
            pst.setString(2, a.getDescription());
            pst.setString(3, a.getSubDate().toString());
            pst.setInt(4, a.getCourse().getId());
            int result = pst.executeUpdate(); 
            if (result > 0) {
                System.out.println("Assignment inserted successfully");
                inserted = true;
            } 
            closeConnections(pst);
        } catch (SQLException ex) {
            Logger.getLogger(AssignmentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return inserted;
    }
    
    public Assignment getAssignmentById(int aid) {
        Assignment a = null;
        try {
            PreparedStatement pst = getConnection().prepareStatement(getAssignmentById);
            pst.setInt(1, aid);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                a = new Assignment(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4).toLocalDate());
            }
        } catch (SQLException ex) {
            Logger.getLogger(StudentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return a;
    }
    
    public boolean insertStudentAssignment(Student s, Assignment a) {
        boolean inserted = false;
        try {
            PreparedStatement pst = getConnection().prepareStatement(insertStudentAssignment);
            pst.setDouble(1, a.getOralMark());
            pst.setDouble(2, a.getTotalMark());
            pst.setInt(3, s.getId());
            pst.setInt(4, a.getId());
            int result = pst.executeUpdate(); 
            if (result > 0) {
                System.out.println("Inserted successfully");
                inserted = true;
            } 
            closeConnections(pst);
            
        } catch (SQLException ex) {
            Logger.getLogger(AssignmentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return inserted;
    }
}
