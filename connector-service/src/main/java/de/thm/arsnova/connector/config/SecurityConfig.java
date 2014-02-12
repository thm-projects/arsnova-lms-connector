package de.thm.arsnova.connector.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;

@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private @Value("${service.username}") String username;
	private @Value("${service.password}") String password;
	
	// LDAP
	private @Value("${ldap.serverUrl}") String ldapServerUrl;
	private @Value("${ldap.userSearchBase}") String ldapUserSearchBase;
	private @Value("${ldap.userSearchFilter}") String ldapUserSearchFilter;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser(username)
			.password(password).roles("ADMIN");
	
		auth.ldapAuthentication().contextSource(ldapContextSource())
			.userSearchBase(ldapUserSearchBase)
			.userSearchFilter(ldapUserSearchFilter);
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {	
		return super.authenticationManagerBean();
	}
	
	@Bean
	public BaseLdapPathContextSource ldapContextSource() {
		return new DefaultSpringSecurityContextSource(ldapServerUrl);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.httpBasic();
	}
}
