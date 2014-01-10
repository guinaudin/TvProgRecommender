package ece.zapp.tvprogrecommender;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TvProgRecommender {

    public static void main(String[] args) {
        try {
            ProfileManager profileManager = new ProfileManager("jdbc:mysql://localhost:3306/profile", "root", "");
        } catch (SQLException ex) {
            Logger.getLogger(TvProgRecommender.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
        /*DataModel model = new FileDataModel(new File("intro.csv"));
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
        UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);
        Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
        List<RecommendedItem> recommendations = recommender.recommend(1, 1);
        for (RecommendedItem recommendation : recommendations) {
            System.out.println(recommendation);*/
    }
}
