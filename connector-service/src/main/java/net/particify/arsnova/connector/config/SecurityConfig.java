package net.particify.arsnova.connector.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import net.particify.arsnova.connector.auth.AuthenticationFilter;
import net.particify.arsnova.connector.auth.AuthenticationHandler;
import net.particify.arsnova.connector.auth.AuthenticationTokenService;
import net.particify.arsnova.connector.config.properties.AuthenticationProperties;
import net.particify.arsnova.connector.core.RepoPermissionEvaluator;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Value("${authentication.username}") private String username;
	@Value("${authentication.password}") private String password;
	
	@Autowired
	private AuthenticationProperties authenticationProperties;
		
	@Autowired
	public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser(authenticationProperties.getUsername())
				.password("{noop}" + authenticationProperties.getPassword()).authorities("ADMIN");
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public AuthenticationHandler authHandler() {
		return new AuthenticationHandler();
	}
	
	@Bean
	public AuthenticationTokenService authTokenService() {
		return new AuthenticationTokenService();
	}
		
	@Bean
	public AuthenticationFilter authFilter() {
		AuthenticationFilter authFilter = new AuthenticationFilter("/**");
		authFilter.setAuthenticationFailureHandler(authHandler().authFailureHandler());
		authFilter.setAuthenticationSuccessHandler(authHandler().tokenAuthSuccessHandler());
		return authFilter;
	}
	
	@Bean
	public PermissionEvaluator permissionEvaluator() {
		return new RepoPermissionEvaluator();
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.httpBasic();
	}
}
