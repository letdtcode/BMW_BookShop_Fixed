<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">

<head>
  <jsp:include page="_meta.jsp"/>
  <title>Đăng nhập</title>

</head>

<body>
<jsp:include page="_header.jsp"/>

<section class="section-content" style="margin: 100px 0;">
  <div class="card mx-auto" style="max-width: 380px">
    <div class="card-body">
      <h4 class="card-title mb-4">Đăng nhập</h4>
      <form id="form-signin" action="${pageContext.request.contextPath}/signin" method="post">
        <div class="mb-3">
          <input name="username"
                 class="form-control ${not empty requestScope.violations.usernameViolations
                   ? 'is-invalid' : (not empty requestScope.values.username ? 'is-valid' : '')}"
                 placeholder="Tên đăng nhập"
                 required type="text" maxlength="25"
                 autocomplete="off"
                 value="${requestScope.values.username}">
          <c:if test="${not empty requestScope.violations.usernameViolations}">
            <div class="invalid-feedback">
              <ul class="list-unstyled">
                <c:forEach var="violation" items="${requestScope.violations.usernameViolations}">
                  <li>${violation}</li>
                </c:forEach>
              </ul>
            </div>
          </c:if>
        </div>
        <div class="mb-3">
          <input id="password" name="password"
                 class="form-control ${not empty requestScope.violations.passwordViolations
                   ? 'is-invalid' : (not empty requestScope.values.password ? 'is-valid' : '')}"
                 placeholder="Mật khẩu"
                 required type="password" maxlength="25"
                 autocomplete="off"
                 value="${requestScope.values.password}">
          <p id="validatePassword"></p>
          <c:if test="${not empty requestScope.violations.passwordViolations}">
            <div class="invalid-feedback">
              <ul class="list-unstyled">
                <c:forEach var="violation" items="${requestScope.violations.passwordViolations}">
                  <li>${violation}</li>
                </c:forEach>
              </ul>
            </div>
          </c:if>
        </div>
        <button type="submit" class="btn btn-primary w-100">Đăng nhập</button>
      </form>
    </div> <!-- card-body.// -->
  </div> <!-- card .// -->
  <p class="text-center mt-4">Không có tài khoản? <a href="${pageContext.request.contextPath}/signup">Đăng ký</a></p>
</section> <!-- section-content.// -->
<script src="<%=request.getContextPath()%>/assets/jquery/jquery-3.5.1.min.js" type="text/javascript" ></script>
<script src="<%=request.getContextPath()%>/assets/validate.js" type="text/javascript" ></script>
<%--<script>--%>
<%--  $('#form-signin').submit(function (e){--%>
<%--    validateForm(e)--%>
<%--  })--%>
<%--  function validateForm(e) {--%>
<%--    // let noError=true;--%>
<%--    e.preventDefault();--%>
<%--    let password = $('#password').val();--%>
<%--    let validatePasswordShow = $('#validatePassword');--%>
<%--    let passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,15}$/;--%>
<%--    if (!passwordRegex.test(password)) {--%>
<%--      validatePasswordShow.html("Mật khẩu không đáp ứng yêu cầu: ít nhất 8 kí tự, có chữ hoa, chữ thường, kí tự đặc biệt và tối đa 15 kí tự.").css('color','red');--%>
<%--      return false;--%>
<%--    } else {--%>
<%--      $('#form-signin').unbind('submit').submit();--%>
<%--    }--%>
<%--  }--%>
<%--</script>--%>
<jsp:include page="_footer.jsp"/>
</body>
</html>
