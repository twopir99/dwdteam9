<%@ page import="java.sql.*" %>

<%

	String callBack = request.getParameter("callback");
	//System.out.println("callback : " + callBack);
	request.setCharacterEncoding("UTF-8");
	response.setCharacterEncoding("UTF-8");
	
   // Returns all data as json.
   response.setContentType("text/html");
   response.setHeader("Content-Disposition", "inline");
%>
<%
Class.forName("com.mysql.jdbc.Driver");
Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dealingS149", "twopir" , "twopir");
//Connection conn = DriverManager.getConnection("jdbc:mysql://bigdata.stern.nyu.edu:3306/dealingS149", "dealingS149" , "dealingS149!!");
Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
Statement stmt1 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
Statement stmt2 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
Statement stmt3 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

%>