package de.thm.arsnova.connector.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;

import de.thm.arsnova.connector.core.RepoPermissionEvaluator;

@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Value("${admin.username}") private String username;
	@Value("${admin.password}") private String password;

	// LDAP
	@Value("${ldap.serverUrl}") private String ldapServerUrl;
	@Value("${ldap.userSearchBase}") private String ldapUserSearchBase;
	@Value("${ldap.userSearchFilter}") private String ldapUserSearchFilter;

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
	public PermissionEvaluator permissionEvaluator() {
		return new RepoPermissionEvaluator();
	}

	@Bean
	public BaseLdapPathContextSource ldapContextSource() {
		if (!"".equals(ldapServerUrl)) {
			return new DefaultSpringSecurityContextSource(ldapServerUrl);
		}

		return null;
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.httpBasic();
	}
}
