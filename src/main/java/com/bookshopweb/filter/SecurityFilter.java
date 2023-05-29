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
        httpResponse.setHeader("Content-Security-Policy", "default-src 'self' " +
                "'unsafe-eval' 'unsafe-inline'; script-src 'self';" +
                "frame-ancestors 'none';" +
                "style-src 'self' 'unsafe-inline' http://www.w3.org https://github.com/twbs/bootstrap;");
        // Thiết lập X-Content-Type-Options header
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");
        httpResponse.addHeader("X-Frame-Options", "SAMEORIGIN");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Giải phóng các tài nguyên (nếu có)
    }
}
