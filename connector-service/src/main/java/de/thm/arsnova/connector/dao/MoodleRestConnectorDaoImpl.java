package de.thm.arsnova.connector.dao;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import de.thm.arsnova.connector.dao.model.moodle_rest.MoodleCourse;
import de.thm.arsnova.connector.dao.model.moodle_rest.MoodleUser;
import de.thm.arsnova.connector.model.Course;
import de.thm.arsnova.connector.model.Membership;
import de.thm.arsnova.connector.model.UserRole;

public class MoodleRestConnectorDaoImpl implements ConnectorDao{

	private static final String TYPE = "moodle";
	private static final int MOODLE_COURSE_EDITINGTEACHER = 3;
	private static final int MOODLE_COURSE_TEACHER = 4;
	private static final int MOODLE_COURSE_MEMBER = 5;

	@Value("${lms.http.token}") private String token ;
	@Value("${lms.http.serverUrl}") private String domainName;

	private static String enrolledUserInCourseURL;
	private static String userInfoByFieldURL;
	private static String usersCoursesURL;

	private final RestTemplate restTemplate = new RestTemplate();

	static{
		//TODO: Only for testing
		disableSslVerification();
	}
	
	@PostConstruct
	public void initialize() {
		enrolledUserInCourseURL = domainName + "/webservice/rest/server.php?wstoken=" + token + "&wsfunction=core_enrol_get_enrolled_users&moodlewsrestformat=json";
		userInfoByFieldURL = domainName + "/webservice/rest/server.php?wstoken=" + token + "&wsfunction=core_user_get_users_by_field&moodlewsrestformat=json";
		usersCoursesURL = domainName + "/webservice/rest/server.php?wstoken=" + token + "&wsfunction=core_enrol_get_users_courses&moodlewsrestformat=json";
	}

	@Override
	public List<String> getCourseUsers(String courseid) {
		final List<String> result = new ArrayList<String>();
		for(MoodleUser mu : getUsersInCourse(courseid))
		{
			result.add(mu.getUsername());
		}
		return result;
	}

	//Seems to need capability "moodle/course:view", which is not stated in moodle
	@Override
	public List<Course> getMembersCourses(String username) {
		final List<Course> result = new ArrayList<Course>();
		int userId=getIdByUsername(username);
		if(userId==-1)
			return result;	
		try {
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("userid", URLEncoder.encode(""+userId, "UTF-8"));
			ResponseEntity<MoodleCourse[]> userWrapper=restTemplate.postForEntity(usersCoursesURL, map, MoodleCourse[].class);
			for(MoodleCourse mc : userWrapper.getBody())
			{
				result.add(buildCourse(mc, getMembership(username, mc.getId())));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	//TODO: Maybe find a better way to do this as this might produce quite some traffic in courses with many users
	@Override
	public Membership getMembership(String username, String courseid) {
		for(MoodleUser mu:getUsersInCourse(courseid))
		{
			if(mu.getUsername().equals(username))
			{
				Membership ms=new Membership();
				ms.setMember(true);
				ms.setUserrole(getMembershipRole(mu.getFirstMoodleRoleId()));
				return ms;
			}
		}
		return new Membership();
	}

	//Seems to need capability "moodle/user:viewalldetails", which is not stated in moodle
	private int getIdByUsername(String username)
	{
		try {
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("field", URLEncoder.encode("username", "UTF-8"));
			map.add("values[0]", URLEncoder.encode(username, "UTF-8"));
			ResponseEntity<MoodleUser[]> userWrapper=restTemplate.postForEntity(userInfoByFieldURL, map, MoodleUser[].class);
			for(MoodleUser mu : userWrapper.getBody())
			{
				if(mu.getUsername().equals(username))
				{
					return mu.getId();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	private MoodleUser[] getUsersInCourse(String courseId)
	{
		try {
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("courseid", URLEncoder.encode(courseId, "UTF-8"));
			ResponseEntity<MoodleUser[]> userWrapper=restTemplate.postForEntity(enrolledUserInCourseURL, map, MoodleUser[].class);
			return userWrapper.getBody();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new MoodleUser[0];
	}

	//Copied from MoodleConnectorDao
	private UserRole getMembershipRole(final int moodleRoleId) {
		if (moodleRoleId == MOODLE_COURSE_EDITINGTEACHER || moodleRoleId == MOODLE_COURSE_TEACHER) {
			return UserRole.TEACHER;
		} else if (moodleRoleId == MOODLE_COURSE_MEMBER) {
			return UserRole.MEMBER;
		}

		// User is course guest
		return UserRole.OTHER;
	}

	private Course buildCourse(MoodleCourse mCourse, Membership membership) {
		Course course = new Course();
		course.setId(mCourse.getId());
		course.setFullname(mCourse.getFullname());
		course.setShortname(mCourse.getShortname());
		course.setType(TYPE);
		course.setMembership(membership);
		return course;
	}
	
	//http://stackoverflow.com/questions/875467/java-client-certificates-over-https-ssl
	private static void disableSslVerification() {
		try
		{
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}
				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			}
			};

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
	}
}

/*
//Kept for debugging
HttpURLConnection connection=null;
OutputStreamWriter writer =null;
try {
	String body="userid="+userId;
	URL url = new URL( usersCoursesURL );
	connection = (HttpURLConnection) url.openConnection();
	connection.setRequestMethod( "POST" );
	connection.setDoInput( true );
	connection.setDoOutput( true );
	connection.setUseCaches( false );
	connection.setRequestProperty( "Content-Type",
			"application/x-www-form-urlencoded" );
	connection.setRequestProperty( "Content-Length", String.valueOf(body.length()) );
	connection.connect();
	writer = new OutputStreamWriter( connection.getOutputStream(), "UTF-8" );
	writer.write( body );
	writer.flush();
	System.out.println(body);
	System.out.println(connection.getResponseCode()+" - "+connection.getResponseMessage());
	if(connection.getResponseCode()==HttpURLConnection.HTTP_OK)
	{
		Map<String, List<String>> header=connection.getHeaderFields();
		for(String hName:header.keySet())
		{
			System.out.println("*********** "+hName);
			for(String s:header.get(hName))
				System.out.println(s);
		}
	}
	BufferedReader in = new BufferedReader(new InputStreamReader(
			connection.getInputStream()));
	String inputLine;
	while ((inputLine = in.readLine()) != null) 
		System.out.println(inputLine);
	in.close();
	writer.close();
}
catch(IOException ex)
{
	ex.printStackTrace();
}
*/