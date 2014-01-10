package ece.zapp.tvprogrecommender;

import java.sql.*;

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
    
    
    public void weightCalculation () throws SQLException{
        
        Statement stmt = myCon.createStatement();
        
        //récupère tous les id utilisateurs
        ResultSet rs = stmt.executeQuery(
        "SELECT USERID FROM USERS"); 
        
        while(rs.next()){
           //récupère tous les id des programmes vu pour un utilisateur
           ResultSet rs2 = stmt.executeQuery(
           "SELECT PROGID"
           + "WHERE USERID LIKE 'rs.getLong(0)' FROM USERSHISTORIC"); 
         
           
        }
        
        //ex de data à rentrer après calcul
        stmt.executeUpdate("INSERT INTO ARTISTPREFERENCES VALUES (1, 1, 0.99)"); 
        stmt.executeUpdate("INSERT INTO ARTISTPREFERENCES VALUES (2, 1, 0.19)"); 
        
        
    }
}
