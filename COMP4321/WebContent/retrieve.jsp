<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="searchEngine.Retrive" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="database.Documents" %>
<%@ page import="jdbm.RecordManagerFactory" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Retrieve Documents</title>
</head>
<body>
<%
if(request.getParameter("userquery")==null)
{
	out.println("You input nothing");
	request.getRequestDispatcher("ErrorPage.html").forward(request, response);
	
}
else{
	Retrive re = new Retrive();
	String query = request.getParameter("userquery");
	ArrayList<String> processedQuery = new ArrayList<String>();	
   	for (String str: re.process(query)) {
   		processedQuery.add(str);
   	}
	ArrayList<Documents> docs = re.retrive(processedQuery);
	for(int i = 0; i < docs.size(); i++){
		Documents doc = docs.get(i);%>
		
		<p><%= doc.getScore() %></p>
		<a href=<%=doc.getURL() %>> <%=doc.getTitle() %>></a>
		<a href=<%=doc.getURL() %>>Click Me !</a>
		<p><%= doc.getDate() + " " + doc.getSize() %></p>
<% 		ArrayList<String> ChildURLs = doc.getChildURLs();
		for(int j = 0; j < ChildURLs.size(); j++){
			String url = ChildURLs.get(j);%>
			<p><%= url %></p>
		<%}%>
	<% }%>	
<%}%>
</body>
</html>