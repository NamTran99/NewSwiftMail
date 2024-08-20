package app.k9mail.feature.account.setup.ui.autodiscovery

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.k9mail.autodiscovery.api.AuthenticationType
import app.k9mail.autodiscovery.api.AutoDiscoveryResult
import app.k9mail.autodiscovery.api.ConnectionSecurity
import app.k9mail.autodiscovery.api.ImapServerSettings
import app.k9mail.autodiscovery.api.IncomingServerSettings
import app.k9mail.autodiscovery.api.SmtpServerSettings
import app.k9mail.autodiscovery.demo.DemoServerSettings
import app.k9mail.core.common.domain.usecase.validation.ValidationResult
import app.k9mail.core.common.net.Hostname
import app.k9mail.core.common.net.Port
import app.k9mail.core.ui.compose.common.mvi.BaseViewModel
import app.k9mail.feature.account.common.domain.AccountDomainContract
import app.k9mail.feature.account.common.domain.entity.IncomingProtocolType
import app.k9mail.feature.account.common.domain.input.StringInputField
import app.k9mail.feature.account.oauth.domain.entity.OAuthResult
import app.k9mail.feature.account.oauth.ui.AccountOAuthContract
import app.k9mail.feature.account.setup.domain.DomainContract.UseCase
import app.k9mail.feature.account.setup.domain.entity.AutoDiscoveryAuthenticationType
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.AutoDiscoveryUiResult
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.ConfigStep
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.Effect
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.Error
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.Event
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.State
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.Validator
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions")
internal class AccountAutoDiscoveryViewModel(
    initialState: State = State(),
    private val validator: Validator,
    private val getAutoDiscovery: UseCase.GetAutoDiscovery,
    private val accountStateRepository: AccountDomainContract.AccountStateRepository,
    override val oAuthViewModel: AccountOAuthContract.ViewModel,
) : BaseViewModel<State, Event, Effect>(initialState), AccountAutoDiscoveryContract.ViewModel {
    override fun initState(state: State) {
        updateState {
            state.copy()
        }
    }

    override fun event(event: Event) {
        when (event) {
            is Event.EmailAddressChanged -> changeEmailAddress(event.emailAddress)
            is Event.PasswordChanged -> changePassword(event.password)
            is Event.OnOAuthResult -> onOAuthResult(event.result)
            is Event.OnSelectServer -> {
                updateState {
                    it.copy(
                        configStep = event.state,
                        autoDiscoverySettings = if (event.state == ConfigStep.OTHER) null else it.autoDiscoverySettings,
                        isShowToolbar = event.state == ConfigStep.OTHER,
                        isNextButtonVisible = event.state == ConfigStep.OTHER,
                    )
                }
                setUpMailServerConfig(event.state)
            }

            Event.OnNextClicked -> onNext()
            Event.OnBackClicked -> onBack()
            Event.OnRetryClicked -> onRetry()
            Event.OnSignInPasswordClicked -> {
                submitEmail()
                submitPassword()
            }

            Event.OnManualConfigurationClicked -> {
                navigateNext(isAutomaticConfig = false)
            }
        }
    }

    private fun setUpMailServerConfig(mailState: ConfigStep) {
        val result = when (mailState) {
            ConfigStep.GMAIL -> {
                AutoDiscoveryResult.Settings(
                    incomingServerSettings = ImapServerSettings(
                        hostname = Hostname("imap.gmail.com"),
                        port = Port(993),
                        connectionSecurity = ConnectionSecurity.TLS,
                        authenticationTypes = listOf(AuthenticationType.OAuth2, AuthenticationType.PasswordCleartext),
                        username = "",
                    ),
                    outgoingServerSettings = SmtpServerSettings(
                        hostname = Hostname("smtp.gmail.com"),
                        port = Port(465),
                        connectionSecurity = ConnectionSecurity.TLS,
                        authenticationTypes = listOf(AuthenticationType.OAuth2, AuthenticationType.PasswordCleartext),
                        username = "",
                    ),
                    source = "",
                )
            }

            ConfigStep.OUTLOOK -> AutoDiscoveryResult.Settings(
                incomingServerSettings = ImapServerSettings(
                    hostname = Hostname("outlook.office365.com"),
                    port = Port(993),
                    connectionSecurity = ConnectionSecurity.TLS,
                    authenticationTypes = listOf(AuthenticationType.OAuth2, AuthenticationType.PasswordCleartext),
                    username = "",
                ),
                outgoingServerSettings = SmtpServerSettings(
                    hostname = Hostname("smtp.office365.com"),
                    port = Port(587),
                    connectionSecurity = ConnectionSecurity.TLS,
                    authenticationTypes = listOf(AuthenticationType.OAuth2, AuthenticationType.PasswordCleartext),
                    username = "",
                ),
                source = "",
            )

            ConfigStep.YANDEX -> AutoDiscoveryResult.Settings(
                incomingServerSettings = ImapServerSettings(
                    hostname = Hostname("imap.yandex.com"),
                    port = Port(993),
                    connectionSecurity = ConnectionSecurity.TLS,
                    authenticationTypes = listOf(AuthenticationType.PasswordCleartext),
                    username = "",
                ),
                outgoingServerSettings = SmtpServerSettings(
                    hostname = Hostname("smtp.yandex.com"),
                    port = Port(465),
                    connectionSecurity = ConnectionSecurity.TLS,
                    authenticationTypes = listOf(AuthenticationType.PasswordCleartext),
                    username = "",
                ),
                source = "",
            )

            else -> null
        }
        updateAutoDiscoverySettings(result ?: return)

    }

    private fun changeEmailAddress(emailAddress: String) {
        accountStateRepository.clear()
        updateState {
            it.copy(
                emailAddress = StringInputField(value = emailAddress),
            )
        }
    }

    private fun changePassword(password: String) {
        updateState {
            it.copy(
                password = it.password.updateValue(password),
            )
        }
    }

    private fun onNext() {
        Log.d("TAG", "onNext: NamTD8 func này có gọi")
        when (state.value.configStep) {
            ConfigStep.LIST_MAIL_SERVER ->
                if (state.value.error != null) {
                    updateState {
                        it.copy(
                            error = null,
                            configStep = ConfigStep.PASSWORD,
                        )
                    }
                } else {
                    submitEmail()
                }

            ConfigStep.PASSWORD -> submitPassword()
            ConfigStep.MANUAL_SETUP -> navigateNext(isAutomaticConfig = false)
            ConfigStep.OTHER -> {
                submitEmail()
            }

            else -> Unit
        }
    }

    private fun onRetry() {
        updateState {
            it.copy(error = null)
        }
        loadAutoDiscovery()
    }

    private fun submitEmail() {
        with(state.value) {
            val emailValidationResult = validator.validateEmailAddress(emailAddress.value)
            val hasError = emailValidationResult is ValidationResult.Failure

            updateState {
                it.copy(
                    emailAddress = it.emailAddress.updateFromValidationResult(emailValidationResult),
                )
            }

            if (!hasError) {
                loadAutoDiscovery()
            }
        }
    }

    private fun loadAutoDiscovery() {
        viewModelScope.launch {
            updateState {
                it.copy(
                    isLoading = true,
                )
            }

            when (val result = getAutoDiscovery.execute(state.value.emailAddress.value)) {
                AutoDiscoveryResult.NoUsableSettingsFound -> updateNoSettingsFound()
                is AutoDiscoveryResult.Settings -> updateAutoDiscoverySettings(result)
                is AutoDiscoveryResult.NetworkError -> updateError(Error.NetworkError)
                is AutoDiscoveryResult.UnexpectedException -> updateError(Error.UnknownError)
            }
        }
    }


    private fun updateNoSettingsFound() {
        updateState {
            it.copy(
                isLoading = false,
                autoDiscoverySettings = null,
                configStep = ConfigStep.MANUAL_SETUP,
            )
        }
    }

    private fun updateAutoDiscoverySettings(settings: AutoDiscoveryResult.Settings) {
        val imapServerSettings = settings.incomingServerSettings as ImapServerSettings
        val isOAuth = imapServerSettings.authenticationTypes.first() == AutoDiscoveryAuthenticationType.OAuth2

        if (isOAuth) {
            oAuthViewModel.initState(
                AccountOAuthContract.State(
                    hostname = imapServerSettings.hostname.value,
                    emailAddress = state.value.emailAddress.value,
                ),
            )
        }

        updateState {
            it.copy(
                isLoading = false,
                autoDiscoverySettings = settings,
//                configStep = if (isOAuth) ConfigStep.OAUTH else ConfigStep.PASSWORD,
                isNextButtonVisible = !isOAuth,
            )
        }
    }

    private fun updateError(error: Error) {
        updateState {
            it.copy(
                isLoading = false,
                error = error,
            )
        }
    }

    private fun submitPassword() {
        with(state.value) {
            val emailValidationResult = validator.validateEmailAddress(emailAddress.value)
            val passwordValidationResult = validator.validatePassword(password.value)
//            val configurationApprovalValidationResult = validator.validateConfigurationApproval(
//                isApproved = configurationApproved.value,
//                isAutoDiscoveryTrusted = autoDiscoverySettings?.isTrusted,
//            )
            val hasError = listOf(
                emailValidationResult,
                passwordValidationResult,
//                configurationApprovalValidationResult,
            ).any { it is ValidationResult.Failure }

            updateState {
                it.copy(
                    emailAddress = it.emailAddress.updateFromValidationResult(emailValidationResult),
                    password = it.password.updateFromValidationResult(passwordValidationResult),
                )
            }

            if (!hasError) {
                navigateNext(state.value.autoDiscoverySettings != null)
            }
        }
    }

    private fun onBack() {
        Log.d("TAG", "onBack: NamTD8 ${state.value.configStep}")
        when (state.value.configStep) {
            ConfigStep.LIST_MAIL_SERVER -> {
                if (state.value.error != null) {
                    updateState {
                        it.copy(error = null)
                    }
                } else {
                    navigateBack()
                }
            }

            ConfigStep.OAUTH,
            ConfigStep.PASSWORD,
            ConfigStep.MANUAL_SETUP,
            -> updateState {
                it.copy(
                    configStep = ConfigStep.LIST_MAIL_SERVER,
                    password = StringInputField(),
                    isNextButtonVisible = true,
                    isShowToolbar = false,
                )
            }

            ConfigStep.YANDEX, ConfigStep.GMAIL, ConfigStep.OUTLOOK, ConfigStep.OTHER -> {
                updateState {
                    it.copy(
                        configStep = ConfigStep.LIST_MAIL_SERVER,
                        isShowToolbar = false,
                    )
                }
            }
        }
    }

    private fun onOAuthResult(result: OAuthResult) {
        if (result is OAuthResult.Success) {
            updateState {
                it.copy(authorizationState = result.authorizationState)
            }
        } else {
            updateState {
                it.copy(authorizationState = null)
            }
        }
    }

    private fun navigateBack() = emitEffect(Effect.NavigateBack)

    private fun navigateNext(isAutomaticConfig: Boolean) {
        val addressOauth = accountStateRepository.getState().emailAddress ?: state.value.emailAddress.value
        updateState {
            it.copy(
                autoDiscoverySettings = it.autoDiscoverySettings?.changeAddress(addressOauth),
                emailAddress = StringInputField(addressOauth),
            )
        }
        accountStateRepository.setState(state.value.toAccountState(addressOauth))

        emitEffect(
            Effect.NavigateNext(
                result = mapToAutoDiscoveryResult(
                    isAutomaticConfig = isAutomaticConfig,
                    incomingServerSettings = state.value.autoDiscoverySettings?.incomingServerSettings,
                ),
            ),
        )
    }

    private fun mapToAutoDiscoveryResult(
        isAutomaticConfig: Boolean,
        incomingServerSettings: IncomingServerSettings?,
    ): AutoDiscoveryUiResult {
        val incomingProtocolType = if (incomingServerSettings is ImapServerSettings) {
            IncomingProtocolType.IMAP
        } else {
            null
        }

        return AutoDiscoveryUiResult(
            isAutomaticConfig = isAutomaticConfig,
            incomingProtocolType = incomingProtocolType,
        )
    }
}
