package com.bookshopweb.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "SecurityFilter", value = "/*")
public class SecurityFilter implements Filter {
    @Override
    public void init(FilterConfig config) throws ServletException {
        // Khởi tạo các tài nguyên cần thiết (nếu có)
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        // Thiết lập Content-Security-Policy header
        httpResponse.setHeader("Content-Security-Policy", "default-src 'self' unsafe-eval' 'unsafe-inline'; script-src 'self'; style-src 'self';");

        // Thiết lập X-Content-Type-Options header
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");

        httpResponse.addHeader("Content-Security-Policy", "frame-ancestors 'none'");
        httpResponse.addHeader("X-Frame-Options", "DENY");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Giải phóng các tài nguyên (nếu có)
    }
}
