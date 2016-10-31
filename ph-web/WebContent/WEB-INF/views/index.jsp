<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.text.*,java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="shortcut icon" href="static/assets/favicon.ico" />
		<title>Sure Thing Pricing Hub</title>
	</head>
	<body>
		<h2>Pricing Hub</h2>
		<hr/>
		
		<h3>REST Endpoints</h3>
		<ul>
			<c:forEach items="${ data.restPaths }" var="entry">
				<li>
		        	${ entry.key } - <a href="${ entry.value }"> ${ entry.value } </a><br>
		        </li>
			</c:forEach>
   		</ul>
		<hr/>
		
		<h3>SOAP Endpoints</h3>
		<ul>
			<c:forEach items="${ data.soapPaths }" var="entry">
				<li>
		        	${ entry.key } - <a href="${ entry.value }"> ${ entry.value } </a><br>
		        </li>
			</c:forEach>
   		</ul>
		
		
		${ data.metrics } 
	</body>
</html>
