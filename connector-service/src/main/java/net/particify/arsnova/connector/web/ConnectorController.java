package net.particify.arsnova.connector.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.particify.arsnova.connector.model.Courses;
import net.particify.arsnova.connector.model.Membership;
import net.particify.arsnova.connector.services.ConnectorService;

@RestController
@RequestMapping(value = "/{username}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
public class ConnectorController {

	@Autowired
	private ConnectorService connectorService;

	@RequestMapping("/membership/{courseid}")
	public Membership getMembership(@PathVariable final String username, @PathVariable final String courseid) {
		return connectorService.getMembership(username, courseid);
	}

	@RequestMapping("/courses")
	public Courses getCourses(@PathVariable final String username) {
		return connectorService.getCourses(username);
	}
}
