package ece.zapp.tvprogrecommender;

import java.util.List;
import java.util.Map;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

public class TvProgRecommender {

    public static void main(String[] args) {
        ProfileManager profileManager = new ProfileManager("jdbc:mysql://localhost:3306/Profile", "root", "");
        Map<Integer, List<RecommendedItem>> usersArtistRecommendations = profileManager.findArtistPreferences(2);
        for(Map.Entry<Integer, List<RecommendedItem>> entry : usersArtistRecommendations.entrySet()) {
            for(RecommendedItem recommendation : entry.getValue()) {
                System.out.println("recommendation " + entry.getKey() + " = " + recommendation);
            }
            System.out.println("");
        }
        profileManager.actorWeightCalculation();
    }
}
