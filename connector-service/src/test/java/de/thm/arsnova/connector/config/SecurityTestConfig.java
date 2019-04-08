package de.thm.arsnova.connector.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import de.thm.arsnova.connector.core.RepoPermissionEvaluator;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityTestConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
				.withUser("admin").password("{noop}secret").authorities("ADMIN")
				.and().withUser("user").password("{noop}secret").authorities("USER");
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PermissionEvaluator permissionEvaluator() throws Exception {
		return new RepoPermissionEvaluator();
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.httpBasic();
	}
}
