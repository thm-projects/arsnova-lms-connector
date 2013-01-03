package de.thm.arsnova.connector.moodle.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;


/**
 * The persistent class for the mdl_user database table.
 * 
 */
@Entity
@Table(name="mdl_user")
public class MdlUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String address;

	private String aim;

	private String auth;

	private byte autosubscribe;

	private String city;

	private byte confirmed;

	private String country;

	private BigInteger currentlogin;

	private byte deleted;

	private String department;

	@Lob
	private String description;

	private byte descriptionformat;

	private String email;

	private byte emailstop;

	private BigInteger firstaccess;

	private String firstname;

	private byte htmleditor;

	private String icq;

	private String idnumber;

	private String imagealt;

	private String institution;

	private String lang;

	private BigInteger lastaccess;

	private String lastip;

	private BigInteger lastlogin;

	private String lastname;

	private byte maildigest;

	private byte maildisplay;

	private byte mailformat;

	private BigInteger mnethostid;

	private String msn;

	private String password;

	private String phone1;

	private String phone2;

	private BigInteger picture;

	private byte policyagreed;

	private String secret;

	private String skype;

	private byte suspended;

	private String theme;

	private BigInteger timecreated;

	private BigInteger timemodified;

	private String timezone;

	private byte trackforums;

	private BigInteger trustbitmask;

	private String url;

	private String username;

	private String yahoo;

	//bi-directional many-to-many association to MdlCourse
	@ManyToMany(mappedBy="mdlUsers")
	private List<MdlCourse> mdlCourses;

	public MdlUser() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAim() {
		return this.aim;
	}

	public void setAim(String aim) {
		this.aim = aim;
	}

	public String getAuth() {
		return this.auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public byte getAutosubscribe() {
		return this.autosubscribe;
	}

	public void setAutosubscribe(byte autosubscribe) {
		this.autosubscribe = autosubscribe;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public byte getConfirmed() {
		return this.confirmed;
	}

	public void setConfirmed(byte confirmed) {
		this.confirmed = confirmed;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public BigInteger getCurrentlogin() {
		return this.currentlogin;
	}

	public void setCurrentlogin(BigInteger currentlogin) {
		this.currentlogin = currentlogin;
	}

	public byte getDeleted() {
		return this.deleted;
	}

	public void setDeleted(byte deleted) {
		this.deleted = deleted;
	}

	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte getDescriptionformat() {
		return this.descriptionformat;
	}

	public void setDescriptionformat(byte descriptionformat) {
		this.descriptionformat = descriptionformat;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte getEmailstop() {
		return this.emailstop;
	}

	public void setEmailstop(byte emailstop) {
		this.emailstop = emailstop;
	}

	public BigInteger getFirstaccess() {
		return this.firstaccess;
	}

	public void setFirstaccess(BigInteger firstaccess) {
		this.firstaccess = firstaccess;
	}

	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public byte getHtmleditor() {
		return this.htmleditor;
	}

	public void setHtmleditor(byte htmleditor) {
		this.htmleditor = htmleditor;
	}

	public String getIcq() {
		return this.icq;
	}

	public void setIcq(String icq) {
		this.icq = icq;
	}

	public String getIdnumber() {
		return this.idnumber;
	}

	public void setIdnumber(String idnumber) {
		this.idnumber = idnumber;
	}

	public String getImagealt() {
		return this.imagealt;
	}

	public void setImagealt(String imagealt) {
		this.imagealt = imagealt;
	}

	public String getInstitution() {
		return this.institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getLang() {
		return this.lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public BigInteger getLastaccess() {
		return this.lastaccess;
	}

	public void setLastaccess(BigInteger lastaccess) {
		this.lastaccess = lastaccess;
	}

	public String getLastip() {
		return this.lastip;
	}

	public void setLastip(String lastip) {
		this.lastip = lastip;
	}

	public BigInteger getLastlogin() {
		return this.lastlogin;
	}

	public void setLastlogin(BigInteger lastlogin) {
		this.lastlogin = lastlogin;
	}

	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public byte getMaildigest() {
		return this.maildigest;
	}

	public void setMaildigest(byte maildigest) {
		this.maildigest = maildigest;
	}

	public byte getMaildisplay() {
		return this.maildisplay;
	}

	public void setMaildisplay(byte maildisplay) {
		this.maildisplay = maildisplay;
	}

	public byte getMailformat() {
		return this.mailformat;
	}

	public void setMailformat(byte mailformat) {
		this.mailformat = mailformat;
	}

	public BigInteger getMnethostid() {
		return this.mnethostid;
	}

	public void setMnethostid(BigInteger mnethostid) {
		this.mnethostid = mnethostid;
	}

	public String getMsn() {
		return this.msn;
	}

	public void setMsn(String msn) {
		this.msn = msn;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone1() {
		return this.phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return this.phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public BigInteger getPicture() {
		return this.picture;
	}

	public void setPicture(BigInteger picture) {
		this.picture = picture;
	}

	public byte getPolicyagreed() {
		return this.policyagreed;
	}

	public void setPolicyagreed(byte policyagreed) {
		this.policyagreed = policyagreed;
	}

	public String getSecret() {
		return this.secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getSkype() {
		return this.skype;
	}

	public void setSkype(String skype) {
		this.skype = skype;
	}

	public byte getSuspended() {
		return this.suspended;
	}

	public void setSuspended(byte suspended) {
		this.suspended = suspended;
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

	public String getTimezone() {
		return this.timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public byte getTrackforums() {
		return this.trackforums;
	}

	public void setTrackforums(byte trackforums) {
		this.trackforums = trackforums;
	}

	public BigInteger getTrustbitmask() {
		return this.trustbitmask;
	}

	public void setTrustbitmask(BigInteger trustbitmask) {
		this.trustbitmask = trustbitmask;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getYahoo() {
		return this.yahoo;
	}

	public void setYahoo(String yahoo) {
		this.yahoo = yahoo;
	}

	public List<MdlCourse> getMdlCourses() {
		return this.mdlCourses;
	}

	public void setMdlCourses(List<MdlCourse> mdlCourses) {
		this.mdlCourses = mdlCourses;
	}

}