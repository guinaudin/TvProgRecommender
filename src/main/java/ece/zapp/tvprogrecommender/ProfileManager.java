package ece.zapp.tvprogrecommender;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private Connection myCon;
    private Statement stateListArtistPreferences;
    private Statement stateListNumberUsers;
    private ResultSet result;
    private int numberUsers;

    public ProfileManager(String url, String user, String password) throws SQLException {
        //Etablissement de la connection à la BDD
        System.setProperty("jdbc.drivers", "com.mysql.jdbc.Driver");
        myCon = DriverManager.getConnection(url, user, password);
        //Pas d'auto commit
        myCon.setAutoCommit(false);
        
        numberUsers = this.getNumberUsers();
    }

    public void weightCalculation() throws SQLException {

        Statement stmt = myCon.createStatement();

        //récupère tous les id utilisateurs
        ResultSet rs = stmt.executeQuery(
                "SELECT USERID FROM USERS");

        while (rs.next()) {
            //récupère tous les id des programmes vu pour un utilisateur
            ResultSet rs2 = stmt.executeQuery(
                    "SELECT PROGID"
                    + "WHERE USERID LIKE 'rs.getLong(0)' FROM USERSHISTORIC");
        }

        //ex de data à rentrer après calcul
        stmt.executeUpdate("INSERT INTO ARTISTPREFERENCES VALUES (1, 1, 0.99)");
        stmt.executeUpdate("INSERT INTO ARTISTPREFERENCES VALUES (2, 1, 0.19)");
    }
    
    private int getNumberUsers() throws SQLException {
        String query = "SELECT userId FROM Users";

        stateListNumberUsers = myCon.createStatement();
        result = stateListNumberUsers.executeQuery(query);
        myCon.commit();
        result.last();

        return result.getRow();
    }

    public Map<Integer, List<RecommendedItem>> findArtistPreferences(int recommendationNumber) throws SQLException {
        Map<Integer, List<RecommendedItem>> usersArtistRecommendations;
        usersArtistRecommendations = new HashMap<Integer, List<RecommendedItem>>();
        List<RecommendedItem> artistRecommendations = null;
        boolean nextResult = false;
        String query = "SELECT userId, artistId, artistWeight FROM ArtistPreferences";

        //On créé la statement
        stateListArtistPreferences = myCon.createStatement();
        //On execute la quiery
        result = stateListArtistPreferences.executeQuery(query);
        //On commit pour mettre à jour la BDD
        myCon.commit();

        nextResult = result.next();
        if (nextResult) {
            try {
                String filePath = System.getProperty("user.dir") + "/ArtistPreferences.csv";
                FileWriter fileWriter = new FileWriter(filePath, false);
                BufferedWriter output = new BufferedWriter(fileWriter);

                while (nextResult) {
                    output.write(result.getInt(1) + "," + result.getInt(2) + "," + result.getFloat(3) + "\n");
                    output.flush();
                    nextResult = result.next();
                }

                output.close();
            } catch (IOException ex) {
                Logger.getLogger(ProfileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                //faire test si fichier vide
                DataModel model = new FileDataModel(new File("ArtistPreferences.csv"));
                UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
                //how many neighbours ??????? (2)
                UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);
                Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
                for(int i = 1; i <= numberUsers; i++) {
                    artistRecommendations = recommender.recommend(i, recommendationNumber);
                    usersArtistRecommendations.put(i, artistRecommendations);
                }
            } catch (IOException ex) {
                Logger.getLogger(ProfileManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TasteException ex) {
                Logger.getLogger(ProfileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return usersArtistRecommendations;
    }
}
