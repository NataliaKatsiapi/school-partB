package daos;

import entities.Trainer;
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

public class TrainerDao {
    
    private final String URL = "jdbc:mysql://localhost:3306/privateschool?serverTimezone=UTC"; // αντί για localhost μπορούμε να βάλουμε μια ip
    private final String USERNAME = "root";
    private final String PASS = "12345";
    private final String getTrainers = "SELECT * FROM trainer";
    private final String insertTrainer = "INSERT INTO trainer(tfname, tlname, tsubject) VALUES(?,?,?)"; 
    private final String getTrainerById = "SELECT * FROM trainer WHERE tid = ?";

    
    private Connection conn;
    
    private Connection getConnection() {
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASS);
        } catch (SQLException ex) {
            Logger.getLogger(TrainerDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    private void closeConnections(ResultSet rs, Statement st) {
        try {
            rs.close();
            st.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(TrainerDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     private void closeConnections(PreparedStatement pst) {
        try {
            pst.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(TrainerDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public List<Trainer> getTrainers() {
        List<Trainer> trainers = new ArrayList();
        try {

            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery(getTrainers);

            while (rs.next()) {
                trainers.add(new Trainer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
            }
            closeConnections(rs, st);

        } catch (SQLException ex) {
            Logger.getLogger(TrainerDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return trainers;
    }
     
    public boolean insertTrainer(Trainer t) {
        boolean inserted = false;
        try {
            PreparedStatement pst = getConnection().prepareStatement(insertTrainer);
            pst.setString(1, t.getFirstName());
            pst.setString(2, t.getLastName());
            pst.setString(3, t.getSubject());
            int result = pst.executeUpdate(); 
            if (result > 0) {
                System.out.println("Trainer inserted successfully");
                inserted = true;
            } 
            closeConnections(pst);
            
        } catch (SQLException ex) {
            Logger.getLogger(TrainerDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return inserted;
    }
    
    public Trainer getTrainerById(int tid) {
        Trainer t = null;
        try {
            PreparedStatement pst = getConnection().prepareStatement(getTrainerById);
            pst.setInt(1, tid);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                t = new Trainer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TrainerDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return t;
    }
}
