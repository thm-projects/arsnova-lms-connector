package de.thm.arsnova.connector.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;

import de.thm.arsnova.connector.auth.AuthenticationFilter;
import de.thm.arsnova.connector.auth.AuthenticationHandler;
import de.thm.arsnova.connector.auth.AuthenticationTokenService;
import de.thm.arsnova.connector.core.RepoPermissionEvaluator;

@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@PropertySource("file:///etc/arsnova/connector.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Value("${admin.username}") private String username;
	@Value("${admin.password}") private String password;

	// LDAP
	@Value("${ldap.serverUrl}") private String ldapServerUrl;
	@Value("${ldap.managerDn:}") private String ldapManagerDn;
	@Value("${ldap.managerPassword:}") private String ldapManagerPassword;
	@Value("${ldap.userSearchBase}") private String ldapUserSearchBase;
	@Value("${ldap.userSearchFilter}") private String ldapUserSearchFilter;
	
	@Autowired
	private Environment env;
		
	@Autowired
	public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser(username)
		.password(password).authorities("ADMIN");
	
		if (!"".equals(ldapServerUrl)) {
			auth.ldapAuthentication().contextSource(ldapContextSource())
			.userSearchBase(ldapUserSearchBase)
			.userSearchFilter(ldapUserSearchFilter);
		}
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

	@Bean
	public BaseLdapPathContextSource ldapContextSource() {
		if (!"".equals(ldapServerUrl)) {
			DefaultSpringSecurityContextSource contextSource = 
					new DefaultSpringSecurityContextSource(ldapServerUrl);
			
			if(!"".equals(ldapManagerDn)) {
				contextSource.setUserDn(ldapManagerDn);
			}
			
			if(!"".equals(ldapManagerPassword)) {
				contextSource.setPassword(ldapManagerPassword);
			}
			
			return contextSource;
		}

		return null;
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.httpBasic();
	}
}
