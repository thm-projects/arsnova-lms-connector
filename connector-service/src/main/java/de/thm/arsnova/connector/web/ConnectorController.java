package de.thm.arsnova.connector.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.thm.arsnova.connector.model.Courses;
import de.thm.arsnova.connector.model.Membership;
import de.thm.arsnova.connector.services.ConnectorService;

@Controller
@RequestMapping(value = "/{username}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
public class ConnectorController {
	
	@Autowired
	ConnectorService connectorService;

	@RequestMapping("/membership/{courseid}")
	@ResponseBody
	public Membership getMembership(@PathVariable String username, @PathVariable String courseid) {
		return connectorService.getMembership(username, courseid);
	}
	
	@RequestMapping("/courses")
	@ResponseBody
	public Courses getCourses(@PathVariable String username) {
		return connectorService.getCourses(username);
	}
}
