package daos;

import entities.Student;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentDao {

    private final String URL = "jdbc:mysql://localhost:3306/privateschool?serverTimezone=UTC"; // αντί για localhost μπορούμε να βάλουμε μια ip
    private final String USERNAME = "root";
    private final String PASS = "12345";
    private final String getStudents = "SELECT * FROM student";
    private final String insertStudent = "INSERT INTO student(sfname, slname, sfees, sdob) VALUES(?,?,?,?)";
    private final String getStudentById = "SELECT * FROM student WHERE sid = ?";
    private Connection conn;

    private Connection getConnection() {
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASS);
        } catch (SQLException ex) {
            Logger.getLogger(StudentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }
      private void closeConnections(PreparedStatement pst) {
        try {
            pst.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(StudentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void closeConnections(ResultSet rs, Statement st) {
        try {
            rs.close();
            st.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(StudentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Student> getStudents() {
        List<Student> students = new ArrayList();
        try {

            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(getStudents);

            while (rs.next()) {
                Date dob = rs.getDate(5);
                students.add(new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4), dob.toLocalDate()));
            }
            closeConnections(rs, st);

        } catch (SQLException ex) {
            Logger.getLogger(StudentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return students;
    }

    public boolean insertStudent(Student s) {
        boolean inserted = false;
        try {
            PreparedStatement pst = getConnection().prepareStatement(insertStudent);
            pst.setString(1, s.getFirstName());
            pst.setString(2, s.getLastName());
            pst.setDouble(3, s.getTuitionFees());
            pst.setString(4, s.getDateOfBirth().toString());
            int result = pst.executeUpdate(); 
            if (result > 0) {
                System.out.println("Student inserted successfully");
                inserted = true;
            } 
            closeConnections(pst);
            
        } catch (SQLException ex) {
            Logger.getLogger(StudentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return inserted;
    }

    public Student getStudentById(int sid) {
        Student c = null;
        try {
            PreparedStatement pst = getConnection().prepareStatement(getStudentById);
            pst.setInt(1, sid);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                c = new Student(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getDouble(4), rs.getDate(5).toLocalDate());
            }
        } catch (SQLException ex) {
            Logger.getLogger(StudentDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return c;
    }

}
