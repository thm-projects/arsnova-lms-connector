# ARSnova LMS Connector

LMS Connector acts as a proxy to Learning Management Systems and provides course membership data under a unified API.
This API can be consumed by ARSnova Backend to allow the creation of sessions linked to LMS courses.
The LMS membership data is retrieved either directly from the database provided by the LMS.
Only limited read access to the LMS's data is required.

## Compatibility

We currently provide implementations to access Moodle and Stud.IP:

| LMS     | Retrieval Method   | Compatible Versions |
|---------|--------------------|---------------------|
| Moodle  | DBMS               | >= 3.2              |
| Stud.IP | DBMS               | >= 1.0              |

## Installation

LMS Connector is available as a web archive (`.war` file) which can be deployed to a Java servlet container.
Based on your needs it can either be deployed on the same system running ARSnova or independently.
Create a configuration directory with a `lms-connector.yml` file based on
[`defaults.yml`](connector-service/src/main/resources/config/defaults.yml).
The location of the directory needs to be set as a Java system property.
This is done by passing the command line argument `-Dconnector.config-dir=/path/to/config` to Java at startup.
For Tomcat, Java command line arguments can be added via the `JAVA_OPTS` environment variable.
On Debian-based systems, `JAVA_OPTS` can be set in `/etc/default/tomcat9`.
You have to select the implementation for your LMS and setup the access method.
Credentials you set via `admin.username` and `admin.password` have to be set accordingly in the configuration file of ARSnova Backend.
