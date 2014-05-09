package co.dwdteam9.evaluator;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.knn.ConjugateGradientOptimizer;
import org.apache.mahout.cf.taste.impl.recommender.knn.KnnItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.knn.Optimizer;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.common.RandomUtils;

public class RecommenderEvaluatorItemBased {

	public static void main(String[] args){
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
			String filename = args[0];
			FileDataModel dataModel = new FileDataModel(new File(filename));
			
			//define evaluator
			RecommenderIRStatsEvaluator evaluator = new GenericRecommenderIRStatsEvaluator();

			RecommenderBuilder rBuilder = new RecommenderBuilder(){
				@Override
				public Recommender buildRecommender(DataModel dataModel) throws TasteException{
					/*Specifies the Similarity algorithm*/
					//ItemSimilarity itemSimilarity = new LogLikelihoodSimilarity(dataModel);
					ItemSimilarity itemSimilarity = new PearsonCorrelationSimilarity(dataModel);					

					/*Initalizing the recommender */
					ItemBasedRecommender recommender = new GenericItemBasedRecommender(dataModel, itemSimilarity);
					
					return recommender;
				}
			};
			
			//calling the recommend method to generate recommendations
			ItemBasedRecommender recommender = (ItemBasedRecommender) rBuilder.buildRecommender(dataModel);
			List<RecommendedItem> recommendations = recommender.recommend(userId, noOfRecommendations);

			IRStatistics stats = evaluator.evaluate(rBuilder, null, dataModel, null, 5, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1);
			
			System.out.println("#####################");
			System.out.println(" Precision ::: " + stats.getPrecision());
			System.out.println(" Recall ::: " + stats.getRecall());
			System.out.println(" F1 measure ::: " + stats.getF1Measure());
			System.out.println("#####################");
			
			//print recommendation 
			for (RecommendedItem recommendedItem : recommendations){
				System.out.println("### ItemBasedRecommender :: " + recommendedItem.getItemID() + " VALUE ::: " + recommendedItem.getValue());
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

