package com.bookshopweb.servlet.client;

import com.bookshopweb.beans.User;
import com.bookshopweb.service.UserService;
import com.bookshopweb.utils.HashingUtils;
import at.favre.lib.crypto.bcrypt.BCrypt;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@WebServlet(name = "ChangePassword", value = "/changePassword")
public class ChangePasswordServlet extends HomeServlet {
    private final UserService userService = new UserService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String csrfToken = UUID.randomUUID().toString();
        request.getSession().setAttribute("csrfToken", csrfToken);


        response.addHeader("Content-Security-Policy", "frame-ancestors 'none'");
        response.addHeader("X-Frame-Options", "DENY");

        request.getRequestDispatcher("WEB-INF/views/changePasswordView.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String csrfToken = request.getParameter("csrfToken");
        String storedToken = (String) request.getSession().getAttribute("csrfToken");

        if (csrfToken == null || !csrfToken.equals(storedToken)) {
//             Mã thông báo CSRF không hợp lệ, xử lý từ chối yêu cầu
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // Trả về mã lỗi 403 Forbidden
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/error.jsp");
            dispatcher.forward(request, response);
        } else {
            Map<String, String> values = new HashMap<>();
            values.put("currentPassword", request.getParameter("currentPassword"));
            values.put("newPassword", request.getParameter("newPassword"));
            values.put("newPasswordAgain", request.getParameter("newPasswordAgain"));

            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("currentUser");

            //boolean currentPasswordEqualsUserPassword = HashingUtils.hash(values.get("currentPassword")).equals(user.getPassword());
            boolean currentPasswordEqualsUserPassword = BCrypt.verifyer().verify(values.get("currentPassword").toCharArray(), user.getPassword()).verified;
            boolean newPasswordEqualsNewPasswordAgain = values.get("newPassword").equals(values.get("newPasswordAgain"));
            System.out.println(user.getPassword());
            if (currentPasswordEqualsUserPassword && newPasswordEqualsNewPasswordAgain) {
                String newPassword = HashingUtils.hash(values.get("newPassword"));
                userService.changePassword(user.getId(), newPassword);
                String successMessage = "Đổi mật khẩu thành công!";
                request.setAttribute("successMessage", successMessage);
            } else {
                String errorMessage = "Đổi mật khẩu thất bại!";
                request.setAttribute("errorMessage", errorMessage);
            }

            request.getRequestDispatcher("/WEB-INF/views/changePasswordView.jsp").forward(request, response);
        }
    }
}
