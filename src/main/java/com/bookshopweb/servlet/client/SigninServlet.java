package com.bookshopweb.servlet.client;

import com.bookshopweb.beans.User;
import com.bookshopweb.service.UserService;
import com.bookshopweb.utils.Protector;
import com.bookshopweb.utils.Validator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet(name = "SigninServlet", value = "/signin")
public class SigninServlet extends HttpServlet {
    private final UserService userService = new UserService();

    private static final String LOGIN_ATTEMPTS_SESSION_NAME = "loginAttempts";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.addHeader("Content-Security-Policy", "frame-ancestors 'none'");
        response.addHeader("X-Frame-Options", "DENY");
        request.getRequestDispatcher("/WEB-INF/views/signinView.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> values = new HashMap<>();
        values.put("username", StringEscapeUtils.escapeHtml4(request.getParameter("username")));
        values.put("password", StringEscapeUtils.escapeHtml4(request.getParameter("password")));

        // Kiểm tra số lần đăng nhập trước đó cho username
        int loginAttempts = getLoginAttempts(request, values.get("username"));
        Map<String, List<String>> violations = new HashMap<>();
        Optional<User> userFromServer = Protector.of(() -> userService.getByUsername(values.get("username")))
                .get(Optional::empty);

        if (loginAttempts < 5) {
            // Tăng số lần đăng nhập
            loginAttempts++;
            // Cập nhật session loginAttempts
            updateLoginAttempts(request, values.get("username"), loginAttempts);
            violations.put("usernameViolations", Validator.of(values.get("username"))
                    .isNotNullAndEmpty()
                    .isNotBlankAtBothEnds()
                    .isAtMostOfLength(25)
                    .isExistent(userFromServer.isPresent(), "Tên đăng nhập")
                    .toList());
            violations.put("passwordViolations", Validator.of(values.get("password"))
                    .isNotNullAndEmpty()
                    .isNotBlankAtBothEnds()
                    .isAtMostOfLength(32)
                    .isVerifyerTo(userFromServer.map(User::getPassword).orElse(""),  "Password")
                    .toList());
            int sumOfViolations = violations.values().stream().mapToInt(List::size).sum();

            if (sumOfViolations == 0 && userFromServer.isPresent()) {
                // Xóa sessison loginAttempts khi đăng nhập thành công
                clearLoginAttempts(request, values.get("username"));
                request.getSession().setAttribute("currentUser", userFromServer.get());
                request.getSession().setMaxInactiveInterval(60);
                response.sendRedirect(request.getContextPath() + "/");
            } else {
                request.setAttribute("values", values);
                request.setAttribute("violations", violations);
                request.getRequestDispatcher("/WEB-INF/views/signinView.jsp").forward(request, response);
            }
        }
        else {
            violations.put("usernameViolations", Validator.of(values.get("username"))
                    .isNotNullAndEmpty()
                    .isNotBlankAtBothEnds()
                    .isAtMostOfLength(25)
                    .isSpam(false, values.get("username"))
                    .toList());
            request.setAttribute("values",values);
            request.setAttribute("violations", violations);
            request.getRequestDispatcher("/WEB-INF/views/signinView.jsp").forward(request, response);
        }
    }
    private int getLoginAttempts(HttpServletRequest request, String username) {
        HttpSession session = request.getSession();
        Integer loginAttempts = (Integer) session.getAttribute(LOGIN_ATTEMPTS_SESSION_NAME + "_" + username);
        return (loginAttempts != null) ? loginAttempts : 0;
    }
    private void updateLoginAttempts(HttpServletRequest request, String username, int loginAttempts) {
        HttpSession session = request.getSession();
        session.setAttribute(LOGIN_ATTEMPTS_SESSION_NAME + "_" + username, loginAttempts);
        request.getSession().setMaxInactiveInterval(10*60);
    }

    private void clearLoginAttempts(HttpServletRequest request, String username) {
        HttpSession session = request.getSession();
        session.removeAttribute(LOGIN_ATTEMPTS_SESSION_NAME + "_" + username);
    }

}

