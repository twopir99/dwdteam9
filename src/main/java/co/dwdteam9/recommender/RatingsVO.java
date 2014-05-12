package co.dwdteam9.recommender;

public class RatingsVO {
	private int userid;
	private int movieid;
	private int rating;
	private long timestamp;
	public RatingsVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RatingsVO(int userid, int movieid, int rating, long timestamp) {
		super();
		this.userid = userid;
		this.movieid = movieid;
		this.rating = rating;
		this.timestamp = timestamp;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getMovieid() {
		return movieid;
	}
	public void setMovieid(int movieid) {
		this.movieid = movieid;
	}
	
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	

}
