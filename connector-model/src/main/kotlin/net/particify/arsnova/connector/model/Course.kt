package net.particify.arsnova.connector.model

import java.time.Instant

data class Course(
    var id: String? = null,
    var fullname: String? = null,
    var shortname: String? = null,
    var membership: Membership? = null,
    var startdate: Instant? = null,
    var enddate: Instant? = null,
    var type: String? = null
)