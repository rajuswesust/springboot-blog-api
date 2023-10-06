package com.spring.boot.blog.app.controller;

import com.spring.boot.blog.app.exception.ResourceNotFoundException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Enumeration;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<?> handleError(HttpServletRequest request) throws Exception {

         getRequestInfo(request);

        // Get the error status code
        int statusCode = (int) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode == 404) {
            // Log or handle the specific error, for example:
            System.out.println("Resource not found - Requested URL: " + request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));
            throw new ResourceNotFoundException("Resource not found");
        } else {
            // Log or handle other errors
            System.out.println("Other error - Status code: " + statusCode);
            // You can throw a different exception or handle it accordingly
            throw new Exception((String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
        }

    }

    public void getRequestInfo(HttpServletRequest request) {
        // Log or print request details
        System.out.println("Request Method: " + request.getMethod());
        System.out.println("Request URI: " + request.getRequestURI());

        // Log request headers
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            System.out.println("Request Headers:");
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                System.out.println(headerName + ": " + headerValue);
            }
        }

        // Log request parameters
        System.out.println("Request Parameters:");
        request.getParameterMap().forEach((param, value) -> {
            System.out.println(param + ": " + String.join(", ", value));
        });
    }
}
