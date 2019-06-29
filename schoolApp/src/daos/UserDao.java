package daos;

import entities.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDao {

    private final String URL = "jdbc:mysql://localhost:3306/privateschool?serverTimezone=UTC"; // αντί για localhost μπορούμε να βάλουμε μια ip
    private final String USERNAME = "root";
    private final String PASS = "12345";
    private Connection conn;
    private final String getUser = "SELECT * FROM user WHERE username = ? AND password = ?";

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

    public boolean getUser(User u) {
        try {

            PreparedStatement pst = getConnection().prepareStatement(getUser);
            pst.setString(1, u.getUsername());
            pst.setString(2, u.getPassword());

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                closeConnections(pst);
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;

    }

}
