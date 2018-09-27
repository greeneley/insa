<%--
  jsp avec jstl et EL
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Ma Réponse</title>
</head>
<body>
<c:if test='${resp }'>
    D'accord
</c:if>
<c:if test='${not resp }'>
    Hors de question
</c:if>

</body>
</html>
