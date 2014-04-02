package de.thm.arsnova.connector.web;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

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
	public HttpEntity<List<IliasCategoryNode>> getIliasTreeObjects(@PathVariable int refId) {
		try {
			List<IliasCategoryNode> nodeList = service.getTreeObjects(refId);

			for (IliasCategoryNode node : nodeList) {
				node.add(linkTo(UniRepController.class).slash(String.valueOf(node.getChild())).withSelfRel());
				if ("qpl".equals(node.getType())) {
					node.add(linkTo(UniRepController.class).slash("question").slash(String.valueOf(node.getChild()))
							.withRel("questions"));
				}
			}
			return new ResponseEntity<List<IliasCategoryNode>>(nodeList, HttpStatus.OK);
		} catch ( ServiceUnavailableException e ) {
			return new ResponseEntity<List<IliasCategoryNode>>(new ArrayList<IliasCategoryNode>(), HttpStatus.SERVICE_UNAVAILABLE);
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
