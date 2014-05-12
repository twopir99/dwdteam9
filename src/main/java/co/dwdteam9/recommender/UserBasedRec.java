package co.dwdteam9.recommender;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
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


public class UserBasedRec {
	
	private String filepath = "/bigtemp/dealingS14-Team9/ratings3.dat";
	//private String filepath = "u.txt";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UserBasedRec ubr = new UserBasedRec();
		ubr.recommendUserBased(args);
	}
	
	public List<RecommendedItem> recommendUserBased(String[] args){
		//check running time start
		long startTime = System.currentTimeMillis();
		
		List<RecommendedItem> recommendations = null;
		
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
			Recommender cachingRecommender = new CachingRecommender(recommender);

			System.out.println("USER ID ::: " + userId + " NO OF REC ::: " + noOfRecommendations);
			//calling the recommend method to generate recommendations
			recommendations = cachingRecommender.recommend(userId, noOfRecommendations);

			System.out.println("RECOMMENDATIONS ::: " + recommendations.size());
			for (RecommendedItem recommendedItem : recommendations){
				System.out.println("### UserBasedRecommender Item ID :: " + recommendedItem.getItemID() + " :: Item Value :: " + recommendedItem.getValue() );
			}
			
			//check runnning time end
			long endTime = System.currentTimeMillis();
			long duration = endTime - startTime;
			NumberFormat formatter = new DecimalFormat("#0.00000");
			System.out.println("### RUNNING TIME :: " + formatter.format((duration) / 1000d));
			
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return recommendations;
	}
	
	public List<RecommendedItem> appendMovieItemToRatings(ArrayList ratingsArray){
		List<RecommendedItem> rub = null;
		try{
			
			File file =new File(filepath);
			 
    		//if file doesn't exists, then create it
    		if(!file.exists()){
    			file.createNewFile();
    		}
    		
    		int userid=0;
    		
			//making data
			int size = ratingsArray.size();
			for(int i=0; i<size; i++){
				RatingsVO vo = (RatingsVO) ratingsArray.get(i);
				Date time = new Date();
				long timestamp = time.getTime();
				String timestamp_str = Long.toString(timestamp);
				String data = vo.getUserid()+"\t"+vo.getMovieid()+"\t"+vo.getRating()+"\t"+timestamp_str.substring(4,13)+"\r\n";
				
	    		//true = append file
	    		FileWriter fileWritter = new FileWriter(file.getName(),true);
    	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
    	        bufferWritter.write(data);
    	        bufferWritter.close();
    	        
    	        userid = vo.getUserid();
			}
    		
	        System.out.println("Writing Done");
	        
	        String[] args = {filepath, Integer.toString(userid) };
	        rub = recommendUserBased(args);
    	}catch(IOException e){
    		e.printStackTrace();
    	}
		
		return rub;
	}

}
