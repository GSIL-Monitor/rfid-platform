<%--
  Created by IntelliJ IDEA.
  User: lly
  Date: 2018/8/15
  Time: 15:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="css/mynumkb.css">
    <style>*{padding:0;margin:0;}</style>
</head>
<body>
    <input type="text" id="input1"/>

    <script src="js/jquery.min.js"></script>
    <script src="js/mynumkb.js"></script>
    <script>
        $("#input1").mynumkb();
    </script>
</body>
</html>
