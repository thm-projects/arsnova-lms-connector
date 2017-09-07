# ARSnova LMS Connector

LMS Connector acts as a proxy to Learning Management Systems and provides course membership data under a unified API.
This API can be consumed by ARSnova Backend to allow the creation of sessions linked to LMS courses.
The LMS membership data is retrieved either directly from the database or via an API provided by the LMS.

## Status

LMS Connector is still in beta phase but has been used on production systems for several years.
Since it only needs limited read access, it is very unlikely to have a negative impact on your LMS.
We currently provide implementations to access Moodle, Ilias and Stud.IP:

| LMS     | Retrieval method   | Compatible versions |
|---------|--------------------|---------------------|
| Moodle  | DBMS               | *                   |
| Ilias   | DBMS               | *                   |
| Stud.IP | DBMS or REST API   | *                   |

\* The database structures and/or APIs for the limited data accessed by LMS Connector usually do not change with new LMS versions and we do not have the resources to test against every new release. If you notice any incompatibilities, please create an issue.

## Installation

LMS Connector is available as a web archive (`.war` file) which can be deployed to a Java servlet container.
Based on your needs it can either be deployed on the same system running ARSnova or independently.
Create a configuration file at `/etc/arsnova/connector.properties` based on
[`connector.properties.example`](connector-service/src/main/resources/connector.properties.example).
You have to select the implementation for your LMS and setup the access method.
Credentials you set via `admin.username` and `admin.password` have to be set accordingly in the configuration file of ARSnova Backend.
