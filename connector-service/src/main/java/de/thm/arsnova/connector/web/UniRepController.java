package de.thm.arsnova.connector.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.thm.arsnova.connector.core.NotFoundException;
import de.thm.arsnova.connector.core.ServiceUnavailableException;
import de.thm.arsnova.connector.model.IliasCategoryNode;
import de.thm.arsnova.connector.model.IliasQuestion;
import de.thm.arsnova.connector.services.UniRepService;

@RestController
@RequestMapping(value = "/ilias", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
public class UniRepController {

	@Autowired(required = false)
	private UniRepService service;

	public enum QuestionSource {
		ALL,
		RANDOM_TEST,
		QUESTION_POOL
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String check() {
		return "OK";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public void login() {}
	
	@RequestMapping(value = "/checklogin", method = RequestMethod.GET)
	public String checkLogin(HttpServletRequest request) {
		return "OK";
	}
	
	@RequestMapping(value = "/{refId}", method = RequestMethod.GET)
	public HttpEntity<IliasCategoryNode> getIliasTreeObjects(@PathVariable int refId) {
		if (service == null) {
			return new ResponseEntity<IliasCategoryNode>(new IliasCategoryNode(), HttpStatus.SERVICE_UNAVAILABLE);
		}

		try {
			IliasCategoryNode node = service.getTreeObjects(refId);
			return new ResponseEntity<IliasCategoryNode>(node, HttpStatus.OK);
		} catch ( NotFoundException e ) {
			return new ResponseEntity<IliasCategoryNode>(new IliasCategoryNode(), HttpStatus.NOT_FOUND);
		} catch ( ServiceUnavailableException e ) {
			return new ResponseEntity<IliasCategoryNode>(new IliasCategoryNode(), HttpStatus.SERVICE_UNAVAILABLE);
		}
	}

	@RequestMapping(value = "/question/{refId}", method = RequestMethod.GET)
	public HttpEntity<List<IliasQuestion>> getIliasQuestions(
			@PathVariable int refId,
			@RequestParam(value = "source", required = false) QuestionSource source) 
					throws ServiceUnavailableException {
		if (service == null) {
			return new ResponseEntity<List<IliasQuestion>>(new ArrayList<IliasQuestion>(), HttpStatus.SERVICE_UNAVAILABLE);
		}
		
		boolean noRandomQuestions = false;
		
		if(QuestionSource.ALL.equals(source)) {
			noRandomQuestions = true;
		}

		return new ResponseEntity<List<IliasQuestion>>(
				service.getQuestions(refId, noRandomQuestions),
				HttpStatus.OK
				);
	}
}
