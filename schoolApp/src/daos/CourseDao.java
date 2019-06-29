package daos;

import entities.Course;
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

public class CourseDao {

    private final String URL = "jdbc:mysql://localhost:3306/privateschool?serverTimezone=UTC"; // αντί για localhost μπορούμε να βάλουμε μια ip
    private final String USERNAME = "root";
    private final String PASS = "12345";
    private final String getCourses = "SELECT * FROM course AS c, ctype AS t WHERE c.ctype = t.typeid";
    private final String getCourseById = "SELECT * FROM course AS c, ctype AS t WHERE c.ctype = t.typeid AND c.cid = ?";
    private final String insertCourse = "INSERT INTO course(ctitle, cstream, cstart_date, cend_date, ctype) VALUES (?,?,?,?,?)";
    private Connection conn;

    private Connection getConnection() {
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASS);
        } catch (SQLException ex) {
            Logger.getLogger(CourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    private void closeConnections(ResultSet rs, Statement st) {
        try {
            rs.close();
            st.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(CourseDao.class.getName()).log(Level.SEVERE, null, ex);
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

    public List<Course> getCourses() {
        List<Course> courses = new ArrayList();
        try {

            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(getCourses);

            while (rs.next()) {
                Date cstart_date = rs.getDate(4);
                Date cend_date = rs.getDate(5);

                courses.add(new Course(rs.getInt(1), rs.getString(2), rs.getString(3),
                        cstart_date.toLocalDate(), cend_date.toLocalDate(), rs.getString(8)));
            }
            closeConnections(rs, st);

        } catch (SQLException ex) {
            Logger.getLogger(CourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return courses;
    }

    public Course getCourseById(int cid) {
        Course c = null;
        try {
            PreparedStatement pst = getConnection().prepareStatement(getCourseById);
            pst.setInt(1, cid);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
            
                Date cstart_date = rs.getDate(4);
                Date cend_date = rs.getDate(5);

                c = new Course(rs.getInt(1), rs.getString(2), rs.getString(3),
                        cstart_date.toLocalDate(), cend_date.toLocalDate(), rs.getString(8));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return c;
    }

    public boolean insertCourse(Course c) {
        boolean inserted = false;
        try {
            PreparedStatement pst = getConnection().prepareStatement(insertCourse);
            pst.setString(1, c.getTitle());
            pst.setString(2, c.getStream());
            pst.setString(3, c.getStartDate().toString());
            pst.setString(4, c.getEndDate().toString());
            if (c.getType().equalsIgnoreCase("full")) {
                pst.setInt(5, 2);
            } else {
                pst.setInt(5, 1);
            }
            int result = pst.executeUpdate();
            if (result > 0) {
                System.out.println("Course inserted successfully");
                inserted = true;
            }
            closeConnections(pst);
        } catch (SQLException ex) {
            Logger.getLogger(CourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return inserted;
    }

}
