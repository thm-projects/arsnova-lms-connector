package de.thm.arsnova.connector.moodle.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;


/**
 * The persistent class for the mdl_course database table.
 * 
 */
@Entity
@Table(name="mdl_course")
@NamedQueries({
	@NamedQuery(name="getCourse", query="SELECT c FROM MdlCourse c WHERE c.id=:courseid")
})
public class MdlCourse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private BigInteger category;

	private byte completionnotify;

	private byte completionstartonenrol;

	private BigInteger defaultgroupingid;

	private byte enablecompletion;

	private String format;

	private String fullname;

	private short groupmode;

	private short groupmodeforce;

	private String idnumber;

	private String lang;

	private short legacyfiles;

	private BigInteger marker;

	private BigInteger maxbytes;

	@Lob
	private String modinfo;

	private int newsitems;

	private byte requested;

	@Lob
	private String sectioncache;

	private String shortname;

	private byte showgrades;

	private short showreports;

	private BigInteger sortorder;

	private BigInteger startdate;

	@Lob
	private String summary;

	private byte summaryformat;

	private String theme;

	private BigInteger timecreated;

	private BigInteger timemodified;

	private byte visible;

	private byte visibleold;

	//bi-directional many-to-many association to MdlUser
	@ManyToMany
	@JoinTable(
		name="mdl_user_enrolments"
		, joinColumns={
			@JoinColumn(name="enrolid")
			}
		, inverseJoinColumns={
			@JoinColumn(name="userid")
			}
		)
	private List<MdlUser> mdlUsers;

	public MdlCourse() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigInteger getCategory() {
		return this.category;
	}

	public void setCategory(BigInteger category) {
		this.category = category;
	}

	public byte getCompletionnotify() {
		return this.completionnotify;
	}

	public void setCompletionnotify(byte completionnotify) {
		this.completionnotify = completionnotify;
	}

	public byte getCompletionstartonenrol() {
		return this.completionstartonenrol;
	}

	public void setCompletionstartonenrol(byte completionstartonenrol) {
		this.completionstartonenrol = completionstartonenrol;
	}

	public BigInteger getDefaultgroupingid() {
		return this.defaultgroupingid;
	}

	public void setDefaultgroupingid(BigInteger defaultgroupingid) {
		this.defaultgroupingid = defaultgroupingid;
	}

	public byte getEnablecompletion() {
		return this.enablecompletion;
	}

	public void setEnablecompletion(byte enablecompletion) {
		this.enablecompletion = enablecompletion;
	}

	public String getFormat() {
		return this.format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getFullname() {
		return this.fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public short getGroupmode() {
		return this.groupmode;
	}

	public void setGroupmode(short groupmode) {
		this.groupmode = groupmode;
	}

	public short getGroupmodeforce() {
		return this.groupmodeforce;
	}

	public void setGroupmodeforce(short groupmodeforce) {
		this.groupmodeforce = groupmodeforce;
	}

	public String getIdnumber() {
		return this.idnumber;
	}

	public void setIdnumber(String idnumber) {
		this.idnumber = idnumber;
	}

	public String getLang() {
		return this.lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public short getLegacyfiles() {
		return this.legacyfiles;
	}

	public void setLegacyfiles(short legacyfiles) {
		this.legacyfiles = legacyfiles;
	}

	public BigInteger getMarker() {
		return this.marker;
	}

	public void setMarker(BigInteger marker) {
		this.marker = marker;
	}

	public BigInteger getMaxbytes() {
		return this.maxbytes;
	}

	public void setMaxbytes(BigInteger maxbytes) {
		this.maxbytes = maxbytes;
	}

	public String getModinfo() {
		return this.modinfo;
	}

	public void setModinfo(String modinfo) {
		this.modinfo = modinfo;
	}

	public int getNewsitems() {
		return this.newsitems;
	}

	public void setNewsitems(int newsitems) {
		this.newsitems = newsitems;
	}

	public byte getRequested() {
		return this.requested;
	}

	public void setRequested(byte requested) {
		this.requested = requested;
	}

	public String getSectioncache() {
		return this.sectioncache;
	}

	public void setSectioncache(String sectioncache) {
		this.sectioncache = sectioncache;
	}

	public String getShortname() {
		return this.shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public byte getShowgrades() {
		return this.showgrades;
	}

	public void setShowgrades(byte showgrades) {
		this.showgrades = showgrades;
	}

	public short getShowreports() {
		return this.showreports;
	}

	public void setShowreports(short showreports) {
		this.showreports = showreports;
	}

	public BigInteger getSortorder() {
		return this.sortorder;
	}

	public void setSortorder(BigInteger sortorder) {
		this.sortorder = sortorder;
	}

	public BigInteger getStartdate() {
		return this.startdate;
	}

	public void setStartdate(BigInteger startdate) {
		this.startdate = startdate;
	}

	public String getSummary() {
		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public byte getSummaryformat() {
		return this.summaryformat;
	}

	public void setSummaryformat(byte summaryformat) {
		this.summaryformat = summaryformat;
	}

	public String getTheme() {
		return this.theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public BigInteger getTimecreated() {
		return this.timecreated;
	}

	public void setTimecreated(BigInteger timecreated) {
		this.timecreated = timecreated;
	}

	public BigInteger getTimemodified() {
		return this.timemodified;
	}

	public void setTimemodified(BigInteger timemodified) {
		this.timemodified = timemodified;
	}

	public byte getVisible() {
		return this.visible;
	}

	public void setVisible(byte visible) {
		this.visible = visible;
	}

	public byte getVisibleold() {
		return this.visibleold;
	}

	public void setVisibleold(byte visibleold) {
		this.visibleold = visibleold;
	}

	public List<MdlUser> getMdlUsers() {
		return this.mdlUsers;
	}

	public void setMdlUsers(List<MdlUser> mdlUsers) {
		this.mdlUsers = mdlUsers;
	}

}