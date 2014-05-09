package co.dwdteam9.dao;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.AbstractDataModel;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.knn.ConjugateGradientOptimizer;
import org.apache.mahout.cf.taste.impl.recommender.knn.KnnItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.knn.Optimizer;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;

public class RecommenderMySQL {
	
	public static void main(String[] args){
		
		//check running time start
		long startTime = System.currentTimeMillis();
		
		//check the number of args
		if(args.length != 2){
			System.out.println("The number of arguments should be 2 ");
			System.out.println("Arguments: Filname(including file path) + UserID");
			System.exit(1);
		}
		
		// Data model created to accept the input file
		String filename = args[0];

		// specifying the user id to which the recommendations have to be generated for
		String userIDstr = args[1];
		int userId = Integer.parseInt(userIDstr);

		//specifying the number of recommendations to be generated
		int noOfRecommendations=10;
		
		
	    try {
	    	/**************** MySQL SET UP *******************/
	    	String url = "jdbc:mysql://bigdata.stern.nyu.edu:3306/dealingS149";
	        String user = "dealingS149";
	        String password = "dealingS149!!";
	        
	    	/* MySQL connection */		
	    	Connection con = DriverManager.getConnection(url, user, password);
			Statement stmt = con.createStatement();
			
			//change the database 
			//stmt.execute ("use dealingS149"); 
			
		    /**************** MySQL SET UP END *******************/
		    
		    /* run recommendation using the User ID parameter  */
			RandomUtils.useTestSeed();
			// Data model created to accept the input file
			FileDataModel dataModel = new FileDataModel(new File(filename));
			

			/*Specifies the Similarity algorithm*/
			UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(dataModel);
			
			/* specified the neighborhood */
			UserNeighborhood neighborhood =
				      new NearestNUserNeighborhood(3, userSimilarity, dataModel);

			/*Initalizing the recommender */
			Recommender recommender = new GenericUserBasedRecommender(dataModel, neighborhood, userSimilarity);
			
			//generate recommendations
			List<RecommendedItem> recommendations = recommender.recommend(userId, noOfRecommendations);

			//to avoid redundant map/reduce of Hive, make item id in one string
			String concatRecItemID = "";
			float[] valueArray = new float[recommendations.size()];
			//initialize i for loop
			int i = 0 ;
			//print recommendation 
			for (RecommendedItem recommendedItem : recommendations){
				System.out.println("### Recommendation Item ID :: " + recommendedItem.getItemID() + " ITEM VALUE ::: " + recommendedItem.getValue() );
				if(i==0){
					concatRecItemID = "'"+ recommendedItem.getItemID()+"'";
				}else{
					concatRecItemID = concatRecItemID + ", '" + recommendedItem.getItemID() + "'";
				}
				valueArray[i] = recommendedItem.getValue();
				i++;
			}
			
			String sql = " select * from " 
					+ " movies "
					+ " where movieid in (" + concatRecItemID + ") ";
		    System.out.println("Running: " + sql);
		    ResultSet rs = stmt.executeQuery(sql);
		    i=0; //reset i
		    while (rs.next()) {
		      System.out.println(String.valueOf(rs.getInt(1)) + "\t" + rs.getString(2) + "\t" + valueArray[i]  );
		      i++;
		    }
			
			//check running time end
			long endTime = System.currentTimeMillis();
			long duration = endTime - startTime;
			NumberFormat formatter = new DecimalFormat("#0.00000");
			System.out.println("!!!!!!!!! RUNNING TIME ::::::::" + formatter.format((duration) / 1000d));


		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

