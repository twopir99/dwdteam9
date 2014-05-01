package co.dwdteam9.recommender;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.knn.ConjugateGradientOptimizer;
import org.apache.mahout.cf.taste.impl.recommender.knn.KnnItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.knn.Optimizer;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

public class RecommenderMain {

	/**
	 * @param args
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		// specifying the user id to which the recommendations have to be generated for
		int userId=510;

		//specifying the number of recommendations to be generated
		int noOfRecommendations=10;

		try
		{
			// Data model created to accept the input file
			FileDataModel dataModel = new FileDataModel(new File("u2.csv"));

			/*Specifies the Similarity algorithm*/
			ItemSimilarity itemSimilarity = new LogLikelihoodSimilarity(dataModel);

			/*Initalizing the recommender */
			ItemBasedRecommender recommender = new GenericItemBasedRecommender(dataModel, itemSimilarity);

			//calling the recommend method to generate recommendations
			List<RecommendedItem> recommendations =recommender.recommend(userId, noOfRecommendations);

			//
			for (RecommendedItem recommendedItem : recommendations){
				System.out.println("### ItemBasedRecommender :: " + recommendedItem.getItemID()+", value="+recommendedItem.getValue());
			}

			//--------------KNN recommender BEGIN------------------ 
			ItemSimilarity itemSimilarity_knn = new PearsonCorrelationSimilarity(dataModel);
			Optimizer optimizer_knn = new ConjugateGradientOptimizer();
			KnnItemBasedRecommender recommender_knn = new KnnItemBasedRecommender(dataModel, itemSimilarity_knn, optimizer_knn, noOfRecommendations);
			
			List<RecommendedItem> recommendations_knn =recommender_knn.recommend(userId, noOfRecommendations);
			
			for (RecommendedItem recommendedItem_knn : recommendations_knn){
				System.out.println("### KNN :: " + recommendedItem_knn.getItemID()+", value="+recommendedItem_knn.getValue());
			}
			
			//--------------KNN recommender END------------------

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
