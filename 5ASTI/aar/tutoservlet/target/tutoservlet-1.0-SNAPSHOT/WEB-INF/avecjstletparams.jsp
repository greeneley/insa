<%--
  jsp avec jstl et EL et paramètres
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Ma Réponse</title>
</head>
<body>
Vos paramètres :
<ul>
    <c:forEach items="${paramValues}" var="par">
    <li> ${par.key} =>
        <c:forEach items="${par.value}" var="val">
            ${val} ;
        </c:forEach>
        </c:forEach>
</ul>
<br/>
La question :
${param.question}
<br/>
<c:if test='${resp }'>
    D'accord
</c:if>
<c:if test='${not resp }'>
    Hors de question
</c:if>

</body>
</html>
