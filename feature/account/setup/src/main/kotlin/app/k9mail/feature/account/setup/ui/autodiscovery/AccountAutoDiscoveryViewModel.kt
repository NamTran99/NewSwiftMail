package app.k9mail.feature.account.setup.ui.autodiscovery

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.k9mail.autodiscovery.api.AuthenticationType
import app.k9mail.autodiscovery.api.AutoDiscoveryResult
import app.k9mail.autodiscovery.api.ConnectionSecurity
import app.k9mail.autodiscovery.api.ImapServerSettings
import app.k9mail.autodiscovery.api.IncomingServerSettings
import app.k9mail.autodiscovery.api.SmtpServerSettings
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
import app.k9mail.feature.account.setup.domain.oldMail.EasyMailUtil
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.AutoDiscoveryUiResult
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.ConfigStep
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.ConfigStep.GMAIL
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.Effect
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.Error
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.Event
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.State
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.Validator
import com.hungbang.email2018.data.entity.OldMailAccountType
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions")
internal class AccountAutoDiscoveryViewModel(
    initialState: State = State(),
    private val validator: Validator,
    private val getAutoDiscovery: UseCase.GetAutoDiscovery,
    private val getManualDiscovery: UseCase.GetManualDiscoveryResult,
    private val accountStateRepository: AccountDomainContract.AccountStateRepository,
    override val oAuthViewModel: AccountOAuthContract.ViewModel,
) : BaseViewModel<State, Event, Effect>(initialState), AccountAutoDiscoveryContract.ViewModel {

    init {
        convertLocalConfig()
    }

    override fun event(event: Event) {
        when (event) {
            is Event.EmailAddressChanged -> changeEmailAddress(event.emailAddress)
            is Event.PasswordChanged -> changePassword(event.password)
            is Event.OnOAuthResult -> onOAuthResult(event.result)
            is Event.OnSelectServer -> {
                if (event.state in listOf(ConfigStep.OTHER, ConfigStep.YANDEX)) {
                    accountStateRepository.clear()
                }
                updateState {
                    it.copy(
                        emailAddress = StringInputField(),
                        password = StringInputField(),
                        configStep = event.state,
                        autoDiscoverySettings = if (event.state == ConfigStep.OTHER) null else it.autoDiscoverySettings,
                        isShowToolbar = event.state == ConfigStep.OTHER,
                        isNextButtonVisible = event.state == ConfigStep.OTHER,
                    )
                }
                if (event.state in listOf(GMAIL, ConfigStep.OUTLOOK)) {
                    oAuthViewModel.event(AccountOAuthContract.Event.SignInClicked)
                }
                setUpMailServerConfig(event.state)
            }

            Event.OnNextClicked -> onNext()
            Event.OnBackClicked -> onBack()
            Event.OnRetryClicked -> onRetry()
            Event.OnSignInPasswordClicked -> {
                submitEmail()
            }

            Event.OnManualConfigurationClicked -> {
                navigateNext(isAutomaticConfig = false)
            }

            Event.OnScreenShown -> {
                with(state.value) {
                    if (configStep == ConfigStep.LIST_MAIL_SERVER && isReLogin) {
                        updateState {
                            copy(isReLogin = false)
                        }
                        submitPassword()
                    }
                }
            }
        }
    }

    private fun OldMailAccountType.conVertAccountTypeToConfigStep(): ConfigStep {
        return when (this) {
            OldMailAccountType.GOOGLE -> GMAIL
            OldMailAccountType.OUTLOOK -> ConfigStep.OUTLOOK
            OldMailAccountType.YANDEX -> ConfigStep.YANDEX
        }
    }

    private fun convertLocalConfig() {
        val savedAccount = EasyMailUtil.getSavedAccountFromEasyMail()
        val savedMailSigning = EasyMailUtil.getSavedSignInConfigFromEasyMail(savedAccount?.accountEmail)

        if (savedAccount != null) {
            if (savedMailSigning != null) {
                accountStateRepository.clear()
                val result = AutoDiscoveryResult.Settings(
                    incomingServerSettings = ImapServerSettings(
                        hostname = Hostname(savedMailSigning.imap_host),
                        port = Port(savedMailSigning.imap_port.toInt()),
                        connectionSecurity = ConnectionSecurity.TLS,
                        authenticationTypes = listOf(AuthenticationType.PasswordCleartext),
                        username = savedAccount.accountEmail,
                    ),
                    outgoingServerSettings = SmtpServerSettings(
                        hostname = Hostname(savedMailSigning.smtp_host),
                        port = Port(savedMailSigning.smtp_port.toInt()),
                        connectionSecurity = if (savedMailSigning.isSmtpStartTLS()) ConnectionSecurity.StartTLS else ConnectionSecurity.TLS,
                        authenticationTypes = listOf(AuthenticationType.PasswordCleartext),
                        username = savedAccount.accountEmail,
                    ),
                    source = "",
                )


                updateAutoDiscoverySettings(result)
            }else{
                setUpMailServerConfig(savedAccount.getAccountTypeFromInt().conVertAccountTypeToConfigStep())
            }

            updateState {
                it.copy(
                    isReLogin = true,
                    configStep = ConfigStep.LIST_MAIL_SERVER,
                    emailAddress = StringInputField(savedAccount.accountEmail),
                    password = StringInputField(savedAccount.password ?: ""),
                )
            }
        }

    }

    private fun setUpMailServerConfig(mailState: ConfigStep) {
        val result = getManualDiscovery.execute(mailState, false)
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
                if (state.value.configStep == ConfigStep.YANDEX) {
                    submitPassword()
                } else
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

            if (state.value.configStep == ConfigStep.OTHER) {
                submitPassword()
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

            ConfigStep.YANDEX, GMAIL, ConfigStep.OUTLOOK, ConfigStep.OTHER -> {
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
            navigateNext(isAutomaticConfig = true)
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
