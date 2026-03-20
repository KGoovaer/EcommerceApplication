<%@page import="com.entity.viewlist"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Search Results</title>
<link rel="stylesheet" href="images/bootstrap.css">
<link rel="stylesheet" href="Css/w3.css">
<link rel="stylesheet" href="Css/font.css">
<style>
.w3-sidebar a {font-family: "Roboto", sans-serif}
body,h1,h2,h3,h4,h5,h6,.w3-wide {font-family: "Montserrat", sans-serif;}
</style>
</head>

<body>
<form action="" method="post">
<%@ include file="admin_navbar.jsp" %>

<div style="background-color: #ebe9eb">
	<br>
	<h1>Search Results for "<%= request.getAttribute("q") %>"</h1>
	<br>
</div>
<br>

<%
	List<viewlist> results = (List<viewlist>) request.getAttribute("results");
	if (results == null || results.isEmpty()) {
%>
	<div class="container">
		<p style="font-size:1.2em; color:#888;">No products found for "<%= request.getAttribute("q") %>". Try a different keyword.</p>
	</div>
<%
	} else {
%>
	<div class="container">
	<div class="row">
	<% for (viewlist v : results) { %>
		<div class="col-xxl-3 col-xl-3 col-lg-3 col-md-4 col-sm-6 col-xs-12 border">
		<div class="container" style="background-color: white">
		<center>
			<table>
				<tr><th>
					<a href='selecteditema.jsp?Pn=<%= v.getPimage() %>'><img src=' images/<%= v.getPimage() %>' height=150px width=150px></a>
				</th></tr><br>
				<tr style='background-color: #ebe9eb'><th style='text-align: center'>
					<a href='selecteditema.jsp?Pn=<%= v.getPimage() %>'><%= v.getBname() %> <%= v.getPname() %></a>
				</th></tr>
			</table>
		</center>
		</div>
		</div>
	<% } %>
	</div>
	</div>
<% } %>

<br>
<footer>
	<%@ include file="footer.jsp" %>
</footer>

</form>
</body>
</html>
