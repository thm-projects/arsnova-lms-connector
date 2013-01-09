package de.thm.arsnova.connector.moodle.client;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.ResourceAccessException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/moodle-connector-client-test.xml" })
public class MoodleClientImplTest {

	@Autowired(required=false)
	MoodleClient moodleClient;

	@Test
	public void testShouldInitializeClientBean() {
		assertNotNull(moodleClient);
	}

	@Test(expected=ResourceAccessException.class)
	public void testShouldResultInException() {
		moodleClient.getCourses("ptsr00");
	}

}
