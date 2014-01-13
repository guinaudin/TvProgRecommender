package ece.zapp.tvprogrecommender;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

public class TvProgRecommender {

    public static void main(String[] args) {
        try {
            ProfileManager profileManager = new ProfileManager("jdbc:mysql://localhost:3306/Profile", "root", "");
            List<RecommendedItem> recommendations = profileManager.findActorPreferences();
            for(RecommendedItem recommendation : recommendations) {
                System.out.println("recommendation =" + recommendation);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TvProgRecommender.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
