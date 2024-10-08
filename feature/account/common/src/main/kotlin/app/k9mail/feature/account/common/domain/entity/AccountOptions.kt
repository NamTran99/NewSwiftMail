package app.k9mail.feature.account.common.domain.entity

data class AccountOptions(
    val displayName: String,
    val emailSignature: String?,
    val messageDisplayCount: Int,
    val showNotification: Boolean,
)
