package org.example.myproject1.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
@WebFilter
public class RequestThrottleFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RequestThrottleFilter.class);
    private final int MAX_REQUESTS_PER_MINUTE = 5;
    private final long ONE_MINUTE_IN_MILLIS = 100 * 100;

    private AtomicInteger requestCount = new AtomicInteger(0);
    private long startTime = System.currentTimeMillis();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long currentTime = System.currentTimeMillis();




        if (currentTime - startTime >= ONE_MINUTE_IN_MILLIS) {
            requestCount.set(0);
            startTime = currentTime;
        }

        if (requestCount.incrementAndGet() <= MAX_REQUESTS_PER_MINUTE) {

            filterChain.doFilter(servletRequest, servletResponse);
        }
        else {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

            String rquidHeader = httpServletRequest.getHeader("x-platform-rquid");
            String rqtmHeader = httpServletRequest.getHeader("x-platform-rqtm");
            String scnameHeader = httpServletRequest.getHeader("x-platform-scname");

            Status status = new Status();
            status.setCode("2");
            status.setName("Rate limit exceeded");
            status.setDescription("Too many requests");

            log.error("Too many requests " + rquidHeader + " " + rqtmHeader+ " " + scnameHeader + "Exception " + status );
            httpServletResponse.setHeader("x-platform-rquid", rquidHeader);
            httpServletResponse.setHeader("x-platform-rqtm", rqtmHeader);
            ObjectMapper objectMapper = new ObjectMapper();
            String statusJson = objectMapper.writeValueAsString(status);
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.getWriter().write(statusJson);
        }


    }

    @Override
    public void destroy() {
    }
}