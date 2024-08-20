package app.k9mail.feature.account.common.ui

data class WizardNavigationBarState(
    val isNextEnabled: Boolean = true,
    val showNext: Boolean = true,
    val isBackEnabled: Boolean = false,
    val showBack: Boolean = true,
)
