package app.k9mail.feature.account.setup.ui.options.display

import app.k9mail.core.common.domain.usecase.validation.ValidationResult
import app.k9mail.core.ui.compose.common.mvi.UnidirectionalViewModel
import app.k9mail.feature.account.common.domain.input.StringInputField

interface DisplayOptionsContract {

    interface ViewModel : UnidirectionalViewModel<State, Event, Effect>

    data class State(
        val displayName: StringInputField = StringInputField(),
        val emailSignature: StringInputField = StringInputField(),
    )

    sealed interface Event {
        data class OnDisplayNameChanged(val displayName: String) : Event
        data class OnEmailSignatureChanged(val emailSignature: String) : Event

        data object LoadAccountState : Event

        data object OnNextClicked : Event
        data object OnBackClicked : Event
    }

    sealed interface Effect {
        data object NavigateNext : Effect
        data object NavigateBack : Effect
    }

    interface Validator {
        fun validateAccountName(accountName: String): ValidationResult
        fun validateDisplayName(displayName: String): ValidationResult
        fun validateEmailSignature(emailSignature: String): ValidationResult
    }
}
