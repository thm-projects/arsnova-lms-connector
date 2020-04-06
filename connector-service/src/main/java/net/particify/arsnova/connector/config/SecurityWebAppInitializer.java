package net.particify.arsnova.connector.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import net.particify.arsnova.connector.web.CorsFilter;

public class SecurityWebAppInitializer extends AbstractSecurityWebApplicationInitializer {
	
	@Override
	protected void beforeSpringSecurityFilterChain(javax.servlet.ServletContext servletContext) {
		insertFilters(servletContext, new CorsFilter());
	}
}
