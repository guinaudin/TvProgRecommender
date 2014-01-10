package ece.zapp.tvprogrecommender;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class ProfileManager {
    //Déclaration des variables
    private Connection myCon;
    
    //Statement
    private Statement stateListArtistPreferences;
    //Prepare statement
    //private PreparedStatement pStateListSeats;
    //Resultat
    private ResultSet result;
    //List
    //private List<Journey> journeyList;
    
    public ProfileManager(String url, String user, String password) throws SQLException {
        //Etablissement de la connection à la BDD
        System.setProperty("jdbc.drivers", "com.mysql.jdbc.Driver");
        myCon = DriverManager.getConnection(url, user, password);
        //Pas d'auto commit
        myCon.setAutoCommit(false);
    }
    
    public List<RecommendedItem> findActorPreferences() throws SQLException {
        List<RecommendedItem> recommendations = null;
        String query = "SELECT userId, artistId, artistWeight FROM ArtistPreferences";

        //On créé la statement
        stateListArtistPreferences = myCon.createStatement();
        //On execute la quiery
        result = stateListArtistPreferences.executeQuery(query);
        //On commit pour mettre à jour la BDD
        myCon.commit();
        try {
            String filePath = System.getProperty("user.dir") + "/actorPreferences.csv";
            System.out.println(filePath);
            FileWriter fileWriter = new FileWriter(filePath, false);
            BufferedWriter output = new BufferedWriter(fileWriter);

            while(result.next()) {
                output.write(result.getInt(1) + "," + result.getInt(2) + "," + result.getFloat(3));
                output.flush();
            }
            
            output.close();
        } catch (IOException ex) {
            Logger.getLogger(ProfileManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            DataModel model = new FileDataModel(new File("actorPreferences.csv"));
            UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);
            Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
            recommendations = recommender.recommend(1, 1);
        } catch (IOException ex) {
            Logger.getLogger(ProfileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TasteException ex) {
            Logger.getLogger(ProfileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return recommendations;
    }
}
