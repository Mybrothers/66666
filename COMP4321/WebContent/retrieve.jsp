<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"  import="searchEngine.Retrive"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Retrieve Documents</title>
</head>
<body>
<%
if(request.getParameter("userquery")!=null)
{
	Retrive re = new Retrive();
	//re.retrive())
	out.println("Hahahahahaha");
}
else
{
	out.println("You input nothing");
	request.getRequestDispatcher("ErrorPage.html").forward(request, response);
}

%>
</body>
</html>