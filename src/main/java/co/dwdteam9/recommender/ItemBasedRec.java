package co.dwdteam9.recommender;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.knn.ConjugateGradientOptimizer;
import org.apache.mahout.cf.taste.impl.recommender.knn.KnnItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.knn.Optimizer;
import org.apache.mahout.cf.taste.impl.similarity.GenericItemSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.common.RandomUtils;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

public class ItemBasedRec {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
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

		try
		{
			 
			// Data model created to accept the input file
			//FileDataModel dataModel = new FileDataModel(new File("/bigtemp/dealingS14-Team9/ratings3.dat"));
			FileDataModel dataModel = new FileDataModel(new File(filename));

			/*Specifies the Similarity algorithm*/ 
			ItemSimilarity itemSimilarity = new PearsonCorrelationSimilarity(dataModel);

			/*Initalizing the recommender */
			ItemBasedRecommender recommender = new GenericItemBasedRecommender(dataModel, itemSimilarity);

			//calling the recommend method to generate recommendations
			List<RecommendedItem> recommendations = recommender.recommend(userId, noOfRecommendations);

			//print recommend item id and value
			for (RecommendedItem recommendedItem : recommendations){
				System.out.println("### ItemBasedRecommender Item ID :: " + recommendedItem.getItemID() + " :: Item Value :: " + recommendedItem.getValue() );
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
