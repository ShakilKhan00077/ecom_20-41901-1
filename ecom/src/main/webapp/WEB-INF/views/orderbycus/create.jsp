<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Create Order</title>
</head>
<body>

<h1>Create Order</h1>

<form:form action="store_order" method="post" modelAttribute="orderDto">
    <label for="product-select">Select a product:</label>
    <select name="productId" id="product-select">
        <c:forEach items="${products}" var="product">
            <option value="${product.id}">${product.name}</option>
        </c:forEach>
    </select>

    <br><br>

    <label for="customer-select">Select a customer:</label>
    <select name="customerId" id="customer-select">
        <c:forEach items="${customers}" var="customer">
            <option value="${customer.id}">${customer.user.fullName}</option>
        </c:forEach>
    </select>

    <br><br>

    <input type="submit" value="Submit">
</form:form>

</body>
</html>
