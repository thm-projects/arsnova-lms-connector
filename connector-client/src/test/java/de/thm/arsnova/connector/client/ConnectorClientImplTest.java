package de.thm.arsnova.connector.client;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.ResourceAccessException;

import de.thm.arsnova.connector.client.ConnectorClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/connector-client-test.xml" })
public class ConnectorClientImplTest {

	@Autowired(required=false)
	ConnectorClient connectorClient;

	@Test
	public void testShouldInitializeClientBean() {
		assertNotNull(connectorClient);
	}

	@Test(expected=ResourceAccessException.class)
	public void testShouldResultInException() {
		connectorClient.getCourses("ptsr00");
	}
}
