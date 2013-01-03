package de.thm.arsnova.connector.moodle.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class CourseDaoImpl implements CourseDao {
	
	@Autowired
	private DataSource dataSource;
	
	public List<String> getCourseUsers(String courseid) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query(
						"SELECT username FROM mdl_user " 
						+ "JOIN mdl_user_enrolments ON (mdl_user.id = mdl_user_enrolments.userid) " 
						+ "JOIN mdl_enrol ON (mdl_user_enrolments.enrolid = mdl_enrol.id) "
						+ "WHERE mdl_enrol.courseid = ?;",
						new String[] {courseid},
						new RowMapper<String>() {
							public String mapRow(ResultSet resultSet, int row) throws SQLException {
								return resultSet.getString("username");
							}
						}
				);
	}
}
