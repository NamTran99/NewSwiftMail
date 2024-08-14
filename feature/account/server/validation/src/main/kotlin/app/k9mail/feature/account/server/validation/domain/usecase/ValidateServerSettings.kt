package app.k9mail.feature.account.server.validation.domain.usecase

import app.k9mail.feature.account.common.domain.AccountDomainContract
import app.k9mail.feature.account.server.validation.domain.ServerValidationDomainContract
import com.fsck.k9.mail.ServerSettings
import com.fsck.k9.mail.oauth.AuthStateStorage
import com.fsck.k9.mail.server.ServerSettingsValidationResult
import com.fsck.k9.mail.server.ServerSettingsValidator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ValidateServerSettings(
    private val authStateStorage: AuthStateStorage,
    private val imapValidator: ServerSettingsValidator,
    private val pop3Validator: ServerSettingsValidator,
    private val smtpValidator: ServerSettingsValidator,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val accRepo: AccountDomainContract.AccountStateRepository,
) : ServerValidationDomainContract.UseCase.ValidateServerSettings {
    override suspend fun execute(settings: ServerSettings): ServerSettingsValidationResult {
        return withContext(coroutineDispatcher) {
            var newSetting = settings
            accRepo.getState().emailAddress?.let {
                newSetting = settings.copy(username = it)
            }
            when (settings.type) {
                "imap" -> imapValidator.checkServerSettings(newSetting, authStateStorage)
                "pop3" -> pop3Validator.checkServerSettings(newSetting, authStateStorage)
                "smtp" -> smtpValidator.checkServerSettings(newSetting, authStateStorage)
                "demo" -> ServerSettingsValidationResult.Success
                else -> {
                    throw IllegalArgumentException("Unsupported server type: ${newSetting.type}")
                }
            }
        }
    }
}
