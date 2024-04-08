package com.ucaldas.mssecurity.Interceptors;

import com.ucaldas.mssecurity.Services.ValidatorsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class SecurityInterceptor implements HandlerInterceptor {
    @Autowired
    private ValidatorsService validatorService;

    // Lógica a ejecutar antes de que se maneje la solicitud por el controlador
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler)
            throws Exception {
        boolean success=this.validatorService.validationRolePermission(request,request.getRequestURI(),request.getMethod());
        return success;
    }

    // Lógica a ejecutar durante el manejo de la solicitud por el controlador
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // Lógica a ejecutar después de que se haya manejado la solicitud por el controlador
    }

    // Lógica a ejecutar después de completar la solicitud, incluso después de la renderización de la vista
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {
        // Lógica a ejecutar después de completar la solicitud, incluso después de la renderización de la vista
    }
}
