<%@page import="com.entity.viewlist"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%!
    private String escHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#x27;");
    }
%>
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
<%@ include file="navbar.jsp" %>

<div style="background-color: #ebe9eb">
	<br>
	<h1>Search Results</h1>
	<% String query = (String) request.getAttribute("query"); %>
	<% if (query != null && !query.trim().isEmpty()) { %>
	<h4>Results for: "<%= escHtml(query) %>"</h4>
	<% } %>
	<br>
</div>
<br>

<%
	@SuppressWarnings("unchecked")
	List<viewlist> listv = (List<viewlist>) request.getAttribute("searchResults");
%>

<% if (listv == null || listv.isEmpty()) { %>
	<div class="container">
		<div class="w3-panel w3-pale-yellow w3-border" style="padding:20px; margin-top:20px;">
			<h3>No products found.</h3>
			<p>Try a different search term or browse by <a href="category.jsp">category</a>.</p>
		</div>
	</div>
<% } else { %>
	<div class="container">
	<div class="row">
	<% for (viewlist v : listv) { %>
		<div class="col-xxl-3 col-xl-3 col-lg-3 col-md-4 col-sm-6 col-xs-12 border">
		<div class="container" style="background-color: white">
		<center>
			<table>
				<tr><th>
					<a href='selecteditem.jsp?Pn=<%= escHtml(v.getPimage()) %>'><img src=' images/<%= escHtml(v.getPimage()) %>' height=150px width=150px></a>
				</th></tr><br>
				<tr style='background-color: #ebe9eb'><th style='text-align: center'>
					<a href='selecteditem.jsp?Pn=<%= escHtml(v.getPimage()) %>'><%= escHtml(v.getBname()) %> <%= escHtml(v.getPname()) %></a>
				</th></tr>
				<tr><th style='text-align: center'>Price: &#8377;<%= v.getPprice() %></th></tr>
			</table>
		</center>
		</div>
		</div>
	<% } %>
	</div>
	</div>
<% } %>

<br>
<footer text-align: center; padding: 3px; background-color: DarkSalmon; color: white;>
<%@ include file="footer.jsp" %>
</footer>
</body>
</html>
