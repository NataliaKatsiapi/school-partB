package daos;

import entities.Course;
import entities.Trainer;
import entities.TrainersPerCourse;
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

public class TrainersPerCourseDao {

    private final String URL = "jdbc:mysql://localhost:3306/privateschool?serverTimezone=UTC";
    private final String USERNAME = "root";
    private final String PASS = "12345";
    private Connection conn;
    private final String getTrainersPerCourses = "SELECT  t.*, c.*, ct.tname FROM course AS c, trainer AS t, trainer_course AS tc, ctype AS ct "
            + "WHERE tc.tid = t.tid AND tc.cid = c.cid AND ct.typeid = ctype";
    private final String insertTrainerCourse = "INSERT INTO trainer_course(tid, cid) VALUES(?,?)";

    private Connection getConnection() {
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASS);
        } catch (SQLException ex) {
            Logger.getLogger(TrainersPerCourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    private void closeConnections(ResultSet rs, Statement st) {
        try {
            rs.close();
            st.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(TrainersPerCourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void closeConnections(PreparedStatement pst) {
        try {
            pst.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(TrainersPerCourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<TrainersPerCourse> getTrainersPerCourses() {
        List<TrainersPerCourse> trainersPerCourses = new ArrayList();
        CourseDao cdao = new CourseDao();
        for (Course c : cdao.getCourses()) {
            trainersPerCourses.add(new TrainersPerCourse(c));
        }
        try {
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(getTrainersPerCourses);

            while (rs.next()) {
                for (TrainersPerCourse tpc : trainersPerCourses) {
                    if (tpc.getCourse().getId() == rs.getInt(5)) {
                        tpc.getTrainers().add(new Trainer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
                    }
                }
            }
            closeConnections(rs, st);

        } catch (SQLException ex) {
            Logger.getLogger(TrainersPerCourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return trainersPerCourses;

    }

    public boolean insertTrainerCourse(Trainer t, Course c) {
        boolean inserted = false;
        try {
            PreparedStatement pst = getConnection().prepareStatement(insertTrainerCourse);
            pst.setInt(1, t.getId());
            pst.setInt(2, c.getId());

            int result = pst.executeUpdate();
            if (result > 0) {
                System.out.println("Trainer inserted successfully into course");
                inserted = true;
            }
            closeConnections(pst);

        } catch (SQLException ex) {
            Logger.getLogger(TrainersPerCourseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return inserted;
    }
}
