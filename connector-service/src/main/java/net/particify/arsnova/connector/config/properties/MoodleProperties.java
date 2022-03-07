package net.particify.arsnova.connector.config.properties;

import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(MoodleProperties.PREFIX)
public class MoodleProperties {
	public static final String PREFIX = "lms.moodle";

	private Set<Integer> teacherRoleIds;
	private Set<Integer> studentRoleIds;

	public Set<Integer> getTeacherRoleIds() {
		return teacherRoleIds;
	}

	public void setTeacherRoleIds(final Set<Integer> teacherRoleIds) {
		this.teacherRoleIds = teacherRoleIds;
	}

	public Set<Integer> getStudentRoleIds() {
		return studentRoleIds;
	}

	public void setStudentRoleIds(final Set<Integer> studentRoleIds) {
		this.studentRoleIds = studentRoleIds;
	}
}
