package app.k9mail.feature.account.setup.ui.options.display

import app.k9mail.feature.account.common.domain.entity.AccountDisplayOptions
import app.k9mail.feature.account.common.domain.entity.AccountState
import app.k9mail.feature.account.common.domain.input.StringInputField
import app.k9mail.feature.account.setup.ui.options.display.DisplayOptionsContract.State

internal fun AccountState.toDisplayOptionsState(): State {
    val options = displayOptions

    val displayNameFormat = emailAddress?.substringBefore("@")?.split("[._]".toRegex())?.flatMap {
        val regex = Regex("[a-zA-Z]+|[0-9]+")
        regex.findAll(it).map {
            it.value.replaceFirstChar {
                it.uppercase()
            }
        }
    }?.joinToString(" ")

    return if (options == null) {
        State(
            displayName = StringInputField(displayNameFormat?:""),
            // TODO: get display name from: preferences.defaultAccount?.senderName ?: ""
        )
    } else {
        State(
            displayName = StringInputField(options.displayName),
            emailSignature = StringInputField(options.emailSignature ?: ""),
        )
    }
}

internal fun State.toAccountDisplayOptions(): AccountDisplayOptions {
    return AccountDisplayOptions(
        displayName = displayName.value,
        emailSignature = emailSignature.value.takeIf { it.isNotEmpty() },
    )
}
