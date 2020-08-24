<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<!-- 页面meta -->
<meta charset="utf-8">
<title>highcharts测试</title>
	<script>
		var contextPath = "${pageContext.request.contextPath}";
		var data = [
			parseInt("${mapList[0]['NUM']}"),
			parseInt("${mapList[1]['NUM']}"),
			parseInt("${mapList[2]['NUM']}"),
			parseInt("${mapList[3]['NUM']}"),
			parseInt("${mapList[4]['NUM']}")
		];
	</script>
	<!-- 引入jQuery -->
	<script type="text/javascript" src="../js/jquery-1.8.0.js"></script>
	<!-- 引入highcharts.js -->
	<script type="text/javascript" src="../js/highcharts.js"></script>
	<!-- 引入自定义js -->
	<script type="text/javascript" src="../js/report.js"></script>

</head>

<body>
	<div>
		<!-- 柱状图容器 -->
		<div id="container" style="width: 500px;height:400px;float:left"></div>
		<!-- 饼状图容器 -->
		<div id="container1" style="width: 500px;height:400px;float:left"></div>
	</div>
	<div id="detail" style="clear: left">

	</div>

</body>

</html>