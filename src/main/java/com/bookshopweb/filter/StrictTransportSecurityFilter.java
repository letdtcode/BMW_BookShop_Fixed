package com.bookshopweb.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "StrictTransportSecurityFilter", value = "/*")
public class StrictTransportSecurityFilter implements Filter {
    @Override
    public void init(FilterConfig config) throws ServletException {
        // Khởi tạo các tài nguyên cần thiết (nếu có)
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Giải phóng các tài nguyên (nếu có)
    }
}
