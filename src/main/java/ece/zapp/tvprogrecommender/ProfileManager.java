package ece.zapp.tvprogrecommender;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ProfileManager {
    //Déclaration des variables
    private Connection myCon;
    
    public ProfileManager(String url, String user, String password) throws SQLException {
        //Etablissement de la connection à la BDD
        System.setProperty("jdbc.drivers", "com.mysql.jdbc.Driver");
        myCon = DriverManager.getConnection(url, user, password);
        //Pas d'auto commit
        myCon.setAutoCommit(false);
    }
}
