package org.example.myproject1.prometheus;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

@Component
public class ResponseMetricInterceptor implements HandlerInterceptor {
    private final MeterRegistry meterRegistry;
    private ThreadLocal<StopWatch> stopWatchThreadLocal = new ThreadLocal<>();

    public ResponseMetricInterceptor(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatchThreadLocal.set(stopWatch);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        StopWatch stopWatch = stopWatchThreadLocal.get();
        if (stopWatch != null) {
            stopWatch.stop();
            meterRegistry.timer("http.request.duration", "status", String.valueOf(response.getStatus()), "path", request.getRequestURI())
                    .record(stopWatch.getTotalTimeMillis(), TimeUnit.MILLISECONDS);
        }
        meterRegistry.counter("responses", "status", String.valueOf(response.getStatus())).increment();
        stopWatchThreadLocal.remove();
    }


}