<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="searchEngine.Retrive" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="database.Documents" %>
<%@ page import="database.Pair" %>
<%@ page import="jdbm.RecordManagerFactory" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/result.css" media="screen" />
<title>Retrieve Documents</title>
</head>
<body>
<%
if(request.getParameter("userquery")==null || request.getParameter("userquery")=="")
{%>
	<div style="text-align:center;font-size:300%;" >
		<p style="text-align:center;">Please Input Keywords </p>
		<a href="index.html" > Return </a>
	</div>
	
<%}
else{
	Retrive re = new Retrive();
	String query = request.getParameter("userquery");
	ArrayList<String> processedQuery = new ArrayList<String>();	
   	for (String str: re.process(query)) {
   		processedQuery.add(str);
   	}
	ArrayList<Documents> docs = re.retrive(processedQuery);
	if(docs.isEmpty()){%>
		<div style="text-align:center;font-size:300%;" >
			<p style="text-align:center;">Sorry, we don't find any results </p>
			<a href="index.html" > Return </a>
		</div>
	<% }
	for(int i = 0; i < docs.size(); i++){
		Documents doc = docs.get(i);%>
		<div class="container-fluid">
		<p><%="Ranking Score: " + doc.getScore() %></p>
		<a href=<%=doc.getURL() %>> <%=doc.getTitle() %></a>
		<p> </p>
		<a href=<%=doc.getURL() %>>Click Me !</a>
		<p><%= "Last Modified at " + doc.getDate() + " File size: " + doc.getSize() %></p>
<% 		ArrayList<Pair> keywords = doc.getKeywords();
		for(int j = 0; j< 5; j++){
			Pair p = keywords.get(j);
			String word = p.getL();
			int freq = p.getR();%>
			<p><%= word + " " + freq + " "%></p>
		<%}
		ArrayList<String> ParentURLs = doc.getParentURLs();
		for(int j = 0; j < ParentURLs.size(); j++){
			String url = ParentURLs.get(j);%>
			<p><%="Parent Link: " + url %></p>
		<%}
		ArrayList<String> ChildURLs = doc.getChildURLs();
		for(int j = 0; j < ChildURLs.size(); j++){
			String url = ChildURLs.get(j);%>
			<p><%="Child Link: " + url %></p>
		<%}%>
		</div>
		<hr>
	<% }%>	
<%}%>

	
</body>
</html>