package de.thm.arsnova.connector.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.thm.arsnova.connector.dao.UniRepDao;
import de.thm.arsnova.connector.model.IliasCategoryNode;

public class UniRepServiceTest {

	@InjectMocks
	private final UniRepService service = new UniRepServiceImpl();

	@Mock
	private UniRepDao dao;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		when(dao.getTreeObjects(anyInt())).thenReturn(new ArrayList<IliasCategoryNode>());
	}

	@Test
	public void testShouldReturnTreeObjects() {
		assertNotNull(dao.getTreeObjects(42));
		assertEquals(0, dao.getTreeObjects(42).size());
	}

	@Test
	public void testShouldReturnQuestions() {
		assertNotNull(dao.getQuestion(42));
		assertEquals(0, dao.getQuestion(42).size());
	}
}
