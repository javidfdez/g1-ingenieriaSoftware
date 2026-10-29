package es.fuengiuma;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component @Order(1)
public class CsrfFilter extends OncePerRequestFilter {
    public static String token(HttpSession s) {
        String token=(String)s.getAttribute("csrf");
        if(token==null) { byte[] bytes=new byte[32]; new SecureRandom().nextBytes(bytes); token=Base64.getUrlEncoder().withoutPadding().encodeToString(bytes); s.setAttribute("csrf",token); }
        return token;
    }
    @Override protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain) throws ServletException,IOException {
        response.setHeader("X-Content-Type-Options","nosniff"); response.setHeader("Referrer-Policy","same-origin");
        response.setHeader("Content-Security-Policy","default-src 'self'; script-src 'self'; style-src 'self'; img-src 'self' data:; connect-src 'self'; object-src 'none'; frame-ancestors 'none'; base-uri 'self'; form-action 'self'");
        if(request.getRequestURI().startsWith("/api/")) {
            response.setHeader("Cache-Control","no-store");
            if(!Set.of("GET","HEAD","OPTIONS").contains(request.getMethod())) {
                HttpSession s=request.getSession(false); String given=request.getHeader("X-CSRF-Token"); String expected=s==null?null:(String)s.getAttribute("csrf");
                if(expected==null||given==null||!MessageDigest.isEqual(expected.getBytes(StandardCharsets.UTF_8),given.getBytes(StandardCharsets.UTF_8))) {
                    response.setStatus(403); response.setContentType("application/json;charset=UTF-8"); response.getWriter().write("{\"message\":\"Actualiza la página y vuelve a intentarlo.\"}"); return;
                }
            }
        }
        chain.doFilter(request,response);
    }
}
