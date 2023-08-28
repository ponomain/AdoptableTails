package ru.otus.otuskotlin.adoptabletails.app.ktor.configs

data class KtorAuthConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm: String,
    val clientId: String,
    val certUrl: String? = null,
) {
    companion object {
        const val ID_CLAIM = "sub"
        const val GROUPS_CLAIM = "groups"
        const val F_NAME_CLAIM = "fname"
        const val M_NAME_CLAIM = "mname"
        const val L_NAME_CLAIM = "lname"

        val TEST = KtorAuthConfig(
            audience = "testAudience",
            realm = "testRealm",
            secret = "testSecret",
            certUrl = "testCertPath",
            clientId = "testId",
            issuer = "testIssuer"
        )

        val NONE = KtorAuthConfig(
            secret = "",
            issuer = "",
            audience = "",
            realm = "",
            clientId = "",
        )
    }
}