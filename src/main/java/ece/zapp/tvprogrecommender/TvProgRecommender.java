package ece.zapp.tvprogrecommender;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

public class TvProgRecommender {

    public static void main(String[] args) {
        try {
            ProfileManager profileManager = new ProfileManager("jdbc:mysql://localhost:3306/Profile", "root", "");
            Map<Integer, List<RecommendedItem>> usersArtistRecommendations = profileManager.findArtistPreferences(2);
            for(Map.Entry<Integer, List<RecommendedItem>> entry : usersArtistRecommendations.entrySet()) {
                for(RecommendedItem recommendation : entry.getValue()) {
                    System.out.println("recommendation " + entry.getKey() + " = " + recommendation);
                }
                System.out.println("");
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(TvProgRecommender.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
