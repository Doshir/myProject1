package org.example.myproject1.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
public class RequestFilter implements Filter {


    private static final Logger log = LoggerFactory.getLogger(RequestFilter.class);
    @Value("${request.throttle.maxRequestsPerMinute}")
    private int maxRequestsPerMinute;

    @Value("${request.throttle.timeFrameInMillis}")
    private long timeFrameInMillis;

    private AtomicInteger requestCount = new AtomicInteger(0);
    private long startTime = System.currentTimeMillis();

    public RequestFilter(int maxRequestsPerMinute, long timeFrameInMillis) {
        this.maxRequestsPerMinute = maxRequestsPerMinute;
        this.timeFrameInMillis = timeFrameInMillis;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long currentTime = System.currentTimeMillis();




        if (currentTime - startTime >= timeFrameInMillis) {
            requestCount.set(0);
            startTime = currentTime;
        }

        if (requestCount.incrementAndGet() <= maxRequestsPerMinute) {

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
            httpServletResponse.setStatus(429);
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