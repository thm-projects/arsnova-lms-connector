package de.thm.arsnova.connector.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.thm.arsnova.connector.core.ServiceUnavailableException;
import de.thm.arsnova.connector.model.IliasCategoryNode;
import de.thm.arsnova.connector.model.IliasQuestion;
import de.thm.arsnova.connector.services.UniRepService;

@Controller
@RequestMapping(value = "/ilias", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
public class UniRepController {

	@Autowired(required = false)
	private UniRepService service;

	@RequestMapping("/{refId}")
	@ResponseBody
	public HttpEntity<IliasCategoryNode> getIliasTreeObjects(@PathVariable int refId) {
		try {
			IliasCategoryNode node = service.getTreeObjects(refId);
			return new ResponseEntity<IliasCategoryNode>(node, HttpStatus.OK);
		} catch ( ServiceUnavailableException e ) {
			return new ResponseEntity<IliasCategoryNode>(new IliasCategoryNode(), HttpStatus.SERVICE_UNAVAILABLE);
		}
	}

	@RequestMapping("/question/{refId}")
	@ResponseBody
	public HttpEntity<List<IliasQuestion>> getIliasQuestion(@PathVariable int refId) {
		try {
			return new ResponseEntity<List<IliasQuestion>>(
					service.getQuestions(refId),
					HttpStatus.OK
					);
		} catch (ServiceUnavailableException e) {
			return new ResponseEntity<List<IliasQuestion>>(new ArrayList<IliasQuestion>(), HttpStatus.SERVICE_UNAVAILABLE);
		}
	}
}
