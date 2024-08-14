package app.k9mail.feature.account.setup.ui.autodiscovery

import androidx.lifecycle.viewModelScope
import app.k9mail.autodiscovery.api.AutoDiscoveryResult
import app.k9mail.autodiscovery.api.ImapServerSettings
import app.k9mail.autodiscovery.api.IncomingServerSettings
import app.k9mail.autodiscovery.demo.DemoServerSettings
import app.k9mail.core.common.domain.usecase.validation.ValidationResult
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
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.MailState
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions")
internal class AccountAutoDiscoveryViewModel(
    initialState: State = State(),
    private val validator: Validator,
    private val getAutoDiscovery: UseCase.GetAutoDiscovery,
    private val accountStateRepository: AccountDomainContract.AccountStateRepository,
    override val oAuthViewModel: AccountOAuthContract.ViewModel,
) : BaseViewModel<State, Event, Effect>(initialState), AccountAutoDiscoveryContract.ViewModel {

    companion object{
        const val MAIL_SERVER  = "imap.gmail.com"
        const val OUTLOOK_SERVER  = "outlook.office365.com"
    }

    override fun initState(state: State) {
        updateState {
            state.copy()
        }
    }

    override fun event(event: Event) {
        when (event) {
            is Event.EmailAddressChanged -> changeEmailAddress(event.emailAddress)
            is Event.PasswordChanged -> changePassword(event.password)
            is Event.ResultApprovalChanged -> changeConfigurationApproval(event.confirmed)
            is Event.OnOAuthResult -> onOAuthResult(event.result)

            Event.OnNextClicked -> onNext()
            Event.OnBackClicked -> onBack()
            Event.OnRetryClicked -> onRetry()
            Event.OnEditConfigurationClicked -> {
                navigateNext(isAutomaticConfig = false)
            }

            is Event.OnSelectServer -> {
                val configStep = when(event.state){
                    MailState.GMAIL -> ConfigStep.GMAIL
                    MailState.OUTLOOK -> ConfigStep.OUTLOOK
                    MailState.YANDEX -> ConfigStep.YANDEX
                    MailState.OTHER -> ConfigStep.PASSWORD
                }
//                setServerOauth(event.state)
                val emailTest = when(event.state){
                    MailState.GMAIL -> "test@gmail.com"
                    MailState.OUTLOOK -> "test@outlook.com"
                    else -> null
                }
                updateState {
                    it.copy(configStep = configStep, currentMailState = event.state, emailAddress =StringInputField(emailTest?:"")  )
                }
                submitEmail()

            }
        }
    }


    private fun setServerOauth(mailState: MailState){
       val hostName = when(mailState){
           MailState.GMAIL -> MAIL_SERVER
           MailState.OUTLOOK -> OUTLOOK_SERVER
           else -> null
        }

        hostName?.let{
            oAuthViewModel.initState(
                AccountOAuthContract.State(
                    hostname = it,
                ),
            )
        }
    }


    private fun changeEmailAddress(emailAddress: String) {
        accountStateRepository.clear()
        updateState {
            State(
                emailAddress = StringInputField(value = emailAddress),
                isNextButtonVisible = false,
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

    private fun changeConfigurationApproval(approved: Boolean) {
        updateState {
            it.copy(
                configurationApproved = it.configurationApproved.updateValue(approved),
            )
        }
    }

    private fun onNext() {
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
            ConfigStep.OAUTH -> Unit
            ConfigStep.MANUAL_SETUP -> navigateNext(isAutomaticConfig = false)
            ConfigStep.YANDEX -> Unit
            ConfigStep.GMAIL -> Unit
            ConfigStep.OUTLOOK -> Unit
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
        if (settings.incomingServerSettings is DemoServerSettings) {
            updateState {
                it.copy(
                    isLoading = false,
                    autoDiscoverySettings = settings,
                    configStep = ConfigStep.PASSWORD,
                    isNextButtonVisible = false,
                )
            }
            return
        }

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
            val configurationApprovalValidationResult = validator.validateConfigurationApproval(
                isApproved = configurationApproved.value,
                isAutoDiscoveryTrusted = autoDiscoverySettings?.isTrusted,
            )
            val hasError = listOf(
                emailValidationResult,
                passwordValidationResult,
                configurationApprovalValidationResult,
            ).any { it is ValidationResult.Failure }

            updateState {
                it.copy(
                    emailAddress = it.emailAddress.updateFromValidationResult(emailValidationResult),
                    password = it.password.updateFromValidationResult(passwordValidationResult),
                    configurationApproved = it.configurationApproved.updateFromValidationResult(
                        configurationApprovalValidationResult,
                    ),
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
                )
            }

            ConfigStep.YANDEX,ConfigStep.GMAIL, ConfigStep.OUTLOOK -> {
                updateState {
                    it.copy(
                        configStep = ConfigStep.LIST_MAIL_SERVER,
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
        val addressOauth = accountStateRepository.getState().emailAddress?:""
        updateState {
            it.copy(autoDiscoverySettings = it.autoDiscoverySettings?.changeAddress(addressOauth), emailAddress = StringInputField(addressOauth))
        }
        accountStateRepository.setState(state.value.toAccountState(addressOauth?:""))

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
