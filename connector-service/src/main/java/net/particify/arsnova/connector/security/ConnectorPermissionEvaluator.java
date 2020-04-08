package net.particify.arsnova.connector.security;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

public class ConnectorPermissionEvaluator implements PermissionEvaluator {
	@Override
	public boolean hasPermission(
			final Authentication authentication,
			final Object targetDomainObject,
			final Object permission
			) {
		return false;
	}

	/**
	 * Checks permission
	 *
	 */
	@Override
	public boolean hasPermission(
			final Authentication authentication,
			final Serializable targetId,
			final String targetType,
			final Object permission
			) {
		if (authentication == null || targetId == null || targetType == null || !(permission instanceof String)) {
			return false;
		}

		if (isAdmin(authentication)) {
			return true;
		}

		switch (targetType) {
		case "membership":
		case "courses":
			if ("read".equals(permission) && authentication.getName().equals(targetId)) {
				return true;
			}
			break;
		}

		return false;
	}

	private boolean isAdmin(final Authentication authentication) {
		return authentication.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equals("ADMIN"));
	}
}
