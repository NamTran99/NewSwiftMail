package app.k9mail.feature.account.setup.domain.usecase

import app.k9mail.autodiscovery.api.AuthenticationType
import app.k9mail.autodiscovery.api.AutoDiscoveryResult
import app.k9mail.autodiscovery.api.ConnectionSecurity
import app.k9mail.autodiscovery.api.ImapServerSettings
import app.k9mail.autodiscovery.api.SmtpServerSettings
import app.k9mail.core.common.net.Hostname
import app.k9mail.core.common.net.Port
import app.k9mail.feature.account.setup.domain.DomainContract
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract.ConfigStep

internal class GetManualDiscoveryResult(
) : DomainContract.UseCase.GetManualDiscoveryResult {
    override fun execute(configStep: ConfigStep): AutoDiscoveryResult.Settings? {
        return when (configStep) {
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
                    connectionSecurity = ConnectionSecurity.StartTLS,
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
    }
}
