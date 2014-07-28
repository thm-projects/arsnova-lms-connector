package de.thm.arsnova.connector.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import de.thm.arsnova.connector.web.CorsFilter;

public class SecurityWebAppInitializer extends AbstractSecurityWebApplicationInitializer {
	
	@Override
	protected void beforeSpringSecurityFilterChain(javax.servlet.ServletContext servletContext) {
		insertFilters(servletContext, new CorsFilter());
	}
}
