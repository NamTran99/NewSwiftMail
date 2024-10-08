enum class SigningType(
    val app: String,
    val type: String,
    val id: String = "$app.$type",
) {
    K9_RELEASE(app = "k9", type = "release"),
    K9_DEBUG(app = "k9", type = "debug"),
    TB_RELEASE(app = "tb", type = "release"),
    TB_BETA(app = "tb", type = "beta"),
    TB_DAILY(app = "tb", type = "daily"),
}
