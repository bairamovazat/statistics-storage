package ru.ivmiit.web.security.filter;

import com.netflix.discovery.EurekaClient;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import ru.ivmiit.web.client.AuthClient;
import ru.ivmiit.web.security.TokenAuthentication;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TokenFilter extends AbstractAuthenticationProcessingFilter {

    private final EurekaClient eurekaClient;
    private final AuthClient authClient;
    private AuthenticationManager authenticationManager;


    public TokenFilter(AuthClient authClient, EurekaClient eurekaClient, AuthenticationManager authenticationManagerBean) {
        super("/**");
        this.authClient = authClient;
        this.eurekaClient = eurekaClient;
        this.authenticationManager = authenticationManagerBean;
        setAuthenticationSuccessHandler((request, response, authentication) ->
        {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String url = request.getServletPath()  + (request.getPathInfo() == null ? "" : request.getPathInfo());
            request.getRequestDispatcher(url).forward(request, response);
        });
        setAuthenticationFailureHandler((request, response, authenticationException) -> {
            String redirectUrl = eurekaClient.getApplication("authorization-service").getInstances().get(0).getHomePageUrl();
            redirectUrl += "?redirect=http://localhost:8081";
            response.sendRedirect(redirectUrl);
        });
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        String token = null;
        String tokenFromCookie = getAuthCookie(request);
        String tokenFromCode = checkCode(request);
        if(tokenFromCookie != null) {
            token = tokenFromCookie;
        } else if (tokenFromCode != null){
            response.addCookie(new Cookie("Authorization", tokenFromCode));
//            response.sendRedirect("/");
        }
        if (token == null) {
            throw  new AuthenticationServiceException("");
//            TokenAuthentication authentication = new TokenAuthentication(null, null);
//            authentication.setAuthenticated(false);
//            return authentication;
        }
        TokenAuthentication tokenAuthentication = new TokenAuthentication(token, null);
        return authenticationManager.authenticate(tokenAuthentication);
    }

    private String checkCode(HttpServletRequest request) {
        String code = request.getParameter("code");
        if (code == null || code.isEmpty()) {
            return null;
        }
        String token;
        try {
            token = authClient.getToken(code);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return token;
    }

    public String getAuthCookie(HttpServletRequest httpServletRequest) {
        Map<String, Cookie> cookieMap = new HashMap<>();
        Cookie[] cookies = httpServletRequest.getCookies();
        if(cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            cookieMap.put(cookie.getName(), cookie);
        }

        Cookie cookie = cookieMap.get("Authorization");

        return cookie == null ? null : cookie.getValue();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        super.doFilter(req, res, chain);
    }
}
