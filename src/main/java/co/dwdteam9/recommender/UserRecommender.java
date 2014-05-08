package co.dwdteam9.recommender;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

public class UserRecommender {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//check running time start
		long startTime = System.currentTimeMillis();
		
		// specifying the user id to which the recommendations have to be generated for
		int userId=510;

		//specifying the number of recommendations to be generated
		int noOfRecommendations=10;

		try
		{
			RandomUtils.useTestSeed();
			
			// Data model created to accept the input file
			FileDataModel dataModel = new FileDataModel(new File("/bigtemp/dealingS14-Team9/ratings3.dat"));
			//FileDataModel dataModel = new FileDataModel(new File("ratings3.dat"));

			/*Specifies the Similarity algorithm*/
			UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(dataModel);

			
			/* specified the neighborhood */
			UserNeighborhood neighborhood =
				      new NearestNUserNeighborhood(3, userSimilarity, dataModel);
				      
			/*Initalizing the recommender */
			Recommender recommender = new GenericUserBasedRecommender(dataModel, neighborhood, userSimilarity);
			Recommender cachingRecommender = new CachingRecommender(recommender);

			//calling the recommend method to generate recommendations
			List<RecommendedItem> recommendations = cachingRecommender.recommend(userId, noOfRecommendations);

			//
			for (RecommendedItem recommendedItem : recommendations){
				System.out.println("### UserBasedRecommender :: " + recommendedItem.getItemID() + " ITEM VALUE ::: " + recommendedItem.getValue() );
			}
			
			//check runnning time end
			long endTime = System.currentTimeMillis();
			long duration = endTime - startTime;
			NumberFormat formatter = new DecimalFormat("#0.00000");
			System.out.println("!!!!!!!!! RUNNING TIME ::::::::" + formatter.format((duration) / 1000d));
			
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
