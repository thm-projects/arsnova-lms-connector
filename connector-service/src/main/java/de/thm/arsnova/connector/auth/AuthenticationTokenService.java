package de.thm.arsnova.connector.auth;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Service;

import de.thm.arsnova.connector.persistence.domain.User;

@Service
public class AuthenticationTokenService {
	private Map<String, User> tokenToUserMappings = new HashMap<>();
	
	public final String HEADER_SECURITY_TOKEN = "X-Auth-Token"; 
	
	/** Authenticates an user and writes the authentication token to
	 *  the response body.
	 *
	 * @param Authentication auth
	 * @param UserDetails ud
	 */
	@PostAuthorize("isAuthenticated()")
	public void authenticateUser(Authentication auth, HttpServletResponse response) throws IOException {
		UserDetails ud = (UserDetails) auth.getPrincipal();
		
		JSONObject obj = new JSONObject();
		obj.put(HEADER_SECURITY_TOKEN, createAndStoreToken(ud));
		
		response.getWriter().write(obj.toString());
	}
	
	
	/** Gets HEADER_SECURITY_TOKEN header from requests and tries to find
	 *  user associated with it.
	 * 
	 * @param HttpServletRequest request
	 * @return User user
	 */
	public User findUserByToken(HttpServletRequest request) {	
		String token = request.getHeader(HEADER_SECURITY_TOKEN);
		User user = null;
		
		if(token != null) {
			user = tokenToUserMappings.get(token);
		}
		
		return user;
	}

	
	/** Generates an authentication token.
	 *
	 * @param ud UserDetails
	 * @return The authentication token string.
	 */
	private String generateToken(UserDetails ud) {
		Date now = new Date();
		MessageDigestPasswordEncoder encoder = new MessageDigestPasswordEncoder("MD5");
		
		return encoder.encode(ud.getUsername() + now.toString() + Math.random());
	}
	
	
	/** Creates an authentication token from given UserDetails and
	 *  saves it in userRep.
	 *
	 * @param ud UserDetails
	 * @return The authentication token string.
	*/
	private String createAndStoreToken(UserDetails ud) {
		String token = generateToken(ud);

		User user = new User();
		user.setUserId(ud.getUsername());
		
		user.setAuthToken(token);
		tokenToUserMappings.put(token, user);
		
		return token;
	}
}
