package com.example.tasktracker.service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InFlightRequestsFilter extends OncePerRequestFilter {

    private final AtomicInteger inFlight = new AtomicInteger();

    public InFlightRequestsFilter(MeterRegistry registry) {
        Gauge.builder("app_inflight_requests", inFlight, AtomicInteger::get)
                .description("Number of HTTP requests currently being processed")
                .register(registry);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // опционально: не считать actuator
        if (request.getRequestURI().startsWith("/actuator")) {
            filterChain.doFilter(request, response);
            return;
        }

        inFlight.incrementAndGet();
        try {
            filterChain.doFilter(request, response);
        } finally {
            inFlight.decrementAndGet();
        }
    }
}