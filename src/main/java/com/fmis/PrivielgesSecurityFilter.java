package com.fmis;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fmis.sec.UserInfo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


// TODO Qadeer ::: Find way to Move it to security
@Component
public class PrivielgesSecurityFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	
    	System.out.println("Incoming Headers: " + request.getHeaderNames());

    	
        String authoritiesHeader = request.getHeader("X-Authorities");
        String userInfoHeader = request.getHeader("X-User-Details");

        if (authoritiesHeader != null && userInfoHeader != null) {
        	ObjectMapper objectMapper = new ObjectMapper();
            try {
            	
                // Deserialize user details from JSON
            	UserInfo userInfo = objectMapper.readValue(userInfoHeader, UserInfo.class);
            	
                // Deserialize authorities from JSON
                List<String> authoritiesList = objectMapper.readValue(authoritiesHeader, List.class);

                // Convert to GrantedAuthority
                List<SimpleGrantedAuthority> authorities = authoritiesList.stream()
                        .map(auth -> new SimpleGrantedAuthority(auth))
                        .toList();

                // Create Authentication object
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userInfo.getUsername(), null, authorities);
                authentication.setDetails(userInfo);
                
                // Set the Authentication in SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse headers", e);
            }
        }

        filterChain.doFilter(request, response);
    }
}
