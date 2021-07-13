package net.particify.arsnova.connector.model

data class Membership(
    var member: Boolean = false,
    var userrole: UserRole? = null
) {
    fun isMember() = member
}