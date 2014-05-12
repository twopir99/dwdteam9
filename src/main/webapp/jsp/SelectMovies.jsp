<%@ page language="java" contentType="text/html; chars=utf-8" import="java.sql.*" %>

<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="javax.servlet.http.*"%>

<%@ include file="./ConnectionHeader.jsp"%>

<%

String query = "SELECT * FROM movies order by title asc ";

ResultSet rs = null;
rs = stmt.executeQuery(query);

String movieid="";
String title="";
String genres="";

Random rand = new Random();
int num1 = (rand.nextInt(10) + 1) * 10000 + (rand.nextInt(10) * 1000) + (rand.nextInt(10) * 100)+ (rand.nextInt(10) * 10)+ (rand.nextInt(10) * 1);
int userid =  num1;
 
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; chars=UTF-8">
<title>Insert title here</title>
</head>
<body>
<FORM METHOD=POST ACTION="ProcMovieSelection.jsp">
<table>
	<TR>
		<input type='hidden' value=<%=userid %> id='userid' name='userid'>
		<TD>&nbsp;</TD>
		<TD width='80' >Movie ID</TD>
		<TD>Title</TD>
		<TD>ETC</TD>
		<TD>Ratings</TD>
		<td><input type="submit" value="Submit"></td>
	</TR>
	<%
	while(rs.next()) {
		movieid = rs.getString("MovieID");
		title = rs.getString("Title");
		genres = rs.getString("Genres");
	%>
		<TR>
			<td><input type='checkbox' value='<%=movieid%>' id='mvidchk' name='mvidchk'></td>
			<TD><%=movieid %></TD>
			<TD><%=title %></TD>
			<TD><%=genres %></TD>
			<TD>
			<select name="ratings<%=movieid%>" id="ratings<%=movieid%>">
			    <option value="1">1</option>
			    <option value="2">2</option>
			    <option value="3">3</option>
			    <option value="4">4</option>
			    <option value="5">5</option>
			</select>
			</TD>
			<TD>&nbsp;</TD>
		</TR>
	<%		
		} 
	%> 
</table>
</FORM>
</body>
</html>

<%

rs.close();
stmt.close();
conn.close();

%>
