package de.thm.arsnova.connector.web;

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

import de.thm.arsnova.connector.core.IliasCategoryNode;
import de.thm.arsnova.connector.core.IliasQuestion;
import de.thm.arsnova.connector.services.UniRepService;

@Controller
@RequestMapping(value = "/ilias", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
public class UniRepController {

	@Autowired
	UniRepService service;

	@RequestMapping("/{refId}")
	@ResponseBody
	public HttpEntity<List<IliasCategoryNode>> getIliasTreeObjects(@PathVariable int refId) throws Exception {
		List<IliasCategoryNode> nodes = service.getTreeObjects(refId);
		return new ResponseEntity<List<IliasCategoryNode>>(nodes, HttpStatus.OK);
	}

	@RequestMapping("/question/{refId}")
	@ResponseBody
	public List<IliasQuestion> getIliasQuestion(@PathVariable int refId) throws Exception {
		return service.getQuestions(refId);
	}
}
