<%@ page language="java" contentType="text/html; chars=utf-8" import="java.sql.*" %>

<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="co.dwdteam9.recommender.RatingsVO"%>
<%@ page import="co.dwdteam9.recommender.UserBasedRec"%>
<%@ page import="org.apache.mahout.cf.taste.recommender.RecommendedItem" %>

<%@ include file="./ConnectionHeader.jsp"%>

<%

String userid = request.getParameter("userid");
String mvidarray[]= request.getParameterValues("mvidchk");

ArrayList<RatingsVO> ratingArray = new ArrayList<RatingsVO>();

if(mvidarray != null){
	System.out.println(" LENGTH :::: "+mvidarray.length);
	for(int i=0; i < mvidarray.length; i++){
		//insert into rating text file
		RatingsVO vo = new RatingsVO();
		vo.setUserid( Integer.parseInt(userid) );
		vo.setMovieid( Integer.parseInt(mvidarray[i]) );
		vo.setRating( Integer.parseInt(request.getParameter("ratings"+mvidarray[i])) );
		ratingArray.add(vo);
		System.out.println("SEE DATA ::: " + userid + " ::: " + mvidarray[i]+ " :::: " + request.getParameter("ratings"+mvidarray[i]) );
	}
	
}

UserBasedRec ubr = new UserBasedRec();

List<RecommendedItem> listitem = ubr.appendMovieItemToRatings(ratingArray);
 
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; chars=UTF-8">
<title>Movie Recommendation result </title>
</head>
<body>
<table>
	<TR>
		<TD>&nbsp;</TD>
		<TD width='80' >Movie ID</TD>
		<TD>Title</TD>
		<TD>ETC</TD>
		<td>ItemValue</td>
	</TR>
	<%
	

	int recsize = listitem.size();
	for(int j=0; j<recsize; j++){
		RecommendedItem item = listitem.get(j);
		long movieid = item.getItemID();
		float value = item.getValue();

		String query = "SELECT * FROM movies where movieid = '"+movieid+"'";
		
		ResultSet rs = null;
		rs = stmt.executeQuery(query);
		
		String title="";
		String genres="";
	
		while(rs.next()) {
			title = rs.getString("Title");
			genres = rs.getString("Genres");
	%>
		<TR>
			<td></td>
			<TD><%=movieid %></TD>
			<TD><%=title %></TD>
			<TD><%=genres %></TD>
			<TD><%=value %></TD>
		</TR>
	<%		
		}
		
	}
	%> 
</table>
</body>
</html>

<%

stmt.close();
conn.close();

%>
