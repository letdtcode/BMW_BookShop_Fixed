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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet(name = "SigninServlet", value = "/signin")
public class SigninServlet extends HttpServlet {
    private final UserService userService = new UserService();

    private static final String LOGIN_ATTEMPTS_COOKIE_NAME = "loginAttempts";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.addHeader("Content-Security-Policy", "frame-ancestors 'none'");
        response.addHeader("X-Frame-Options", "DENY");
        request.getRequestDispatcher("/WEB-INF/views/signinView.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> values = new HashMap<>();
        values.put("username", request.getParameter("username"));
        values.put("password", StringEscapeUtils.escapeHtml4(request.getParameter("password")));

        // Kiểm tra số lần đăng nhập trước đó cho username
        int loginAttempts = getLoginAttempts(request, values.get("username"));

        Map<String, List<String>> violations = new HashMap<>();
               Optional<User> userFromServer = Protector.of(() -> userService.getByUsername(values.get("username")))
                .get(Optional::empty);

        if (loginAttempts <= 5) {
            // Tăng số lần đăng nhập
            loginAttempts++;
            // Cập nhật cookie loginAttempts
            updateLoginAttemptsCookie(response, values.get("username"), loginAttempts);
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
                    .isVerifyerTo(userFromServer.map(User::getPassword).orElse(""), "Mật khẩu")
                    .toList());

            int sumOfViolations = violations.values().stream().mapToInt(List::size).sum();

            if (sumOfViolations == 0 && userFromServer.isPresent()) {
                // Xóa cookie loginAttempts khi đăng nhập thành công
                clearLoginAttemptsCookie(response, values.get("username"));

                request.getSession().setAttribute("currentUser", userFromServer.get());
                request.getSession().setMaxInactiveInterval(60);
                response.sendRedirect(request.getContextPath() + "/");
            } else {
                // Kiểm tra số lần đăng nhập sai và cập nhật cookie
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
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ( cookie.getName().equals(username)) {
                    try {
                        return Integer.parseInt(cookie.getValue());
                    } catch (NumberFormatException e) {
                        // Xử lý ngoại lệ khi giá trị cookie không hợp lệ
                    }
                }
            }
        }
        return 0;
    }


    private void updateLoginAttemptsCookie(HttpServletResponse response, String username, int loginAttempts) {
        Cookie loginAttemptsCookie = new Cookie(username, username);
        loginAttemptsCookie.setMaxAge(10 *60);
        loginAttemptsCookie.setPath("/");
        loginAttemptsCookie.setValue(String.valueOf(loginAttempts));
        response.addCookie(loginAttemptsCookie);
    }

    private void clearLoginAttemptsCookie(HttpServletResponse response, String username) {
        Cookie resetLoginAttemptsCookie = new Cookie(username, username);
        resetLoginAttemptsCookie.setMaxAge(0); // Set max age to 0 to delete the cookie
        resetLoginAttemptsCookie.setPath("/");
        response.addCookie(resetLoginAttemptsCookie);
    }

    private void handleExceededLoginAttempts(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Xử lý khi vượt quá số lần đăng nhập sai cho phép
        // Ví dụ: Chuyển hướng đến trang thông báo hoặc chặn tài khoản
        response.sendRedirect("https://utex.hcmute.edu.vn/login/index.php");
    }
}

