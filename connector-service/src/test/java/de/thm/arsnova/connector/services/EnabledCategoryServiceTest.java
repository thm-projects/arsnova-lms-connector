package de.thm.arsnova.connector.services;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.thm.arsnova.connector.config.RepositoryTestConfig;
import de.thm.arsnova.connector.persistence.domain.EnabledCategory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( classes = {RepositoryTestConfig.class, RepositoryTestConfig.class} )
public class EnabledCategoryServiceTest {

	@Autowired
	private EnabledCategoryService enabledCategoryService;

	@Autowired
	private DataSource dataSource;

	@Test
	public void testShouldEnableOneCategory() {
		enabledCategoryService.enableCategory(123);
		List<EnabledCategory> actual = enabledCategoryService.getEnabledCategories();

		assertEquals(1, actual.size());
		assertEquals(123, actual.get(0).getRefId());
	}
}