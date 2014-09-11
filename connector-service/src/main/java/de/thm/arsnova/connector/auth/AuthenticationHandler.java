package de.thm.arsnova.connector.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.bind.annotation.RequestMethod;

public class AuthenticationHandler {
	
	@Autowired
	private AuthenticationTokenService tokenService;
    
	@Bean
	public AuthenticationSuccessHandler authSuccessHandler() {
		return new AuthenticationSuccessHandler() {					
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request,
					HttpServletResponse response, Authentication authentication) 
					throws IOException, ServletException {
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
				tokenService.authenticateUser(authentication, response);
				response.setStatus(HttpServletResponse.SC_OK);
			}
		};
	}
	
	@Bean
	public SimpleUrlAuthenticationSuccessHandler tokenAuthSuccessHandler() {
		return new SimpleUrlAuthenticationSuccessHandler() {
			@Override
		    protected String determineTargetUrl(HttpServletRequest request,
		            HttpServletResponse response) {
		        String context = request.getContextPath();
		        String fullURL = request.getRequestURI();
		        String url = fullURL.substring(fullURL.indexOf(context)+context.length());
		        return url;
		    }
		 
		    @Override
		    public void onAuthenticationSuccess(HttpServletRequest request,
		            HttpServletResponse response, Authentication authentication)
		            throws IOException, ServletException {
		    	
		        String url = determineTargetUrl(request, response);
		        request.getRequestDispatcher(url).forward(request, response);
		    }
		};
	}
	
	@Bean
	public AuthenticationFailureHandler authFailureHandler() {
		return new AuthenticationFailureHandler() {
			@Override
			public void onAuthenticationFailure(HttpServletRequest request,
					HttpServletResponse response, AuthenticationException exception)
					throws IOException, ServletException {
				
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
		};
	}
	
    @Bean
    public AuthenticationEntryPoint tokenAuthenticationEntryPoint() {
    	return new AuthenticationEntryPoint() {
			
			@Override
			public void commence(HttpServletRequest request, 
					HttpServletResponse response, AuthenticationException authException) 
					throws IOException, ServletException {
				
	        	String method = request.getMethod();
	        	
	        	if(!RequestMethod.OPTIONS.toString().equals(method)) {
	        		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	        	}
			}
		};
    }
}
