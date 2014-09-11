package de.thm.arsnova.connector.auth;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import de.thm.arsnova.connector.persistence.domain.User;

public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	@Autowired
	private AuthenticationTokenService tokenService;

	public AuthenticationFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}

    @Override
    public void afterPropertiesSet() {
		super.setAuthenticationManager(new AuthenticationManager() {
			@Override
			public Authentication authenticate(Authentication authentication)
					throws AuthenticationException {
				return authentication;
			}
		});
		super.afterPropertiesSet();
    }

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) 
			throws AuthenticationException {
		
		Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ADMIN");
		User user = tokenService.findUserByToken(request);
		Authentication auth = null;
		
		if(user != null) {
			auth = new UsernamePasswordAuthenticationToken(
					user.getAuthToken(), user.getAuthToken(), authorities);
		} else {
			throw new AuthenticationException("TOKEN_ERROR") {
				private static final long serialVersionUID = 1L;
			};
		}
		
		return auth;
	}
	
	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        Authentication auth;

        if(tokenService.findUserByToken(httpRequest) != null) {
	        try {
	            auth = attemptAuthentication(httpRequest, httpResponse);
	            if(auth != null) successfulAuthentication(httpRequest, httpResponse, chain, auth);
	            return;
	        } 
	        catch (AuthenticationException failed) {
	            unsuccessfulAuthentication(httpRequest, httpResponse, failed);
	            return;
	        }
		}
		
        chain.doFilter(httpRequest, httpResponse);
    }
}
