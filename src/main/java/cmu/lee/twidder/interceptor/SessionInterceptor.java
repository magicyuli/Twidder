package cmu.lee.twidder.interceptor;

import cmu.lee.twidder.entity.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by lee on 4/5/16.
 */

/**
 * Interceptor to associate current user object with the current http session
 * so that the controllers have access to the user object easily.
 */
public class SessionInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        if (session.getAttribute("me") == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                User user = (User) authentication.getPrincipal();
                session.setAttribute("me", user);
            }
        }

        return true;
    }
}
