package de.thm.arsnova.connector.core;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.HttpClientErrorException;

import de.thm.arsnova.connector.dao.ConnectorDao;

public class RepoPermissionEvaluator implements PermissionEvaluator {

	@Value("${service.studipclient.url}") private String url;
	@Value("${service.studipclient.username}") private String username;
	@Value("${service.studipclient.password}") private String password;

	@Autowired
	ConnectorDao dao;

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		return false;
	}

	/** Checks permission
	 *
	 */
	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
		if (authentication.getPrincipal() instanceof String) return false;

		UserDetails ud = (UserDetails)authentication.getPrincipal();
		System.out.println(ud.getUsername());
		System.out.println(targetId);
		System.out.println(targetType);
		System.out.println(permission);
		System.out.println(ud.getAuthorities());

		try {
			switch (targetType) {
			case "membership":
			case "courses":
				if ("read".equals(permission) && ud.getUsername().equals(targetId) ) {
					return true;
				}
				break;

			case "uniRepQuestion":
			case "uniRepTree":
				if ("read".equals(permission) && ud.getUsername().equals(targetId) ) {
					return true;
				}
				break;
			}
		} catch (HttpClientErrorException e) {
			return false;
		}
		return false;
	}
}
