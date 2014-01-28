package ece.zapp.tvprogrecommender;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TvProgRecommender {
    public static void main(String[] args) {
        ProfileManager profileManager;
        try {
            profileManager = new ProfileManager();
        } 
        catch (SQLException ex) {
            Logger.getLogger(TvProgRecommender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
