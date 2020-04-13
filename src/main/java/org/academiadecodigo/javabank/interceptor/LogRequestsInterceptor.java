package org.academiadecodigo.javabank.interceptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * A {@link HandlerInterceptorAdapter} responsible for the logging of http requests
 */
@Component
public class LogRequestsInterceptor extends HandlerInterceptorAdapter {

    /**
     * @see HandlerInterceptorAdapter#preHandle(HttpServletRequest, HttpServletResponse, Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        Logger logger = LogManager.getLogger(method.getDeclaringClass());
        logger.info(request.getMethod() + " " + request.getServletPath());
        return true;
    }
}
