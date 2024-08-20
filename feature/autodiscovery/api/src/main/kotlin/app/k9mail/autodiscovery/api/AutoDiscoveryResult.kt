package app.k9mail.autodiscovery.api

import java.io.IOException

/**
 * Results of a mail server settings lookup.
 */
sealed interface AutoDiscoveryResult {
    /**
     * Mail server settings found during the lookup.
     */
    data class Settings(
        val incomingServerSettings: IncomingServerSettings,
        val outgoingServerSettings: OutgoingServerSettings,

        /**
         * String describing the source of the server settings. Use a URI if possible.
         */
        val source: String,
    ) : AutoDiscoveryResult{
        fun changeAddress(address: String): Settings {
            val mIncomingServerSettings =( incomingServerSettings as? ImapServerSettings)?.copy(username = address)
            val mOutgoingServerSettings = (outgoingServerSettings as? SmtpServerSettings)?.copy(username = address)

            return if(mIncomingServerSettings == null || mOutgoingServerSettings == null) this
            else this.copy(incomingServerSettings = mIncomingServerSettings, outgoingServerSettings = mOutgoingServerSettings)
        }
    }

    /**
     * No usable mail server settings were found.
     */
    object NoUsableSettingsFound : AutoDiscoveryResult

    /**
     * A network error occurred while looking for mail server settings.
     */
    data class NetworkError(val exception: IOException) : AutoDiscoveryResult

    /**
     * Encountered an unexpected exception when looking up mail server settings.
     */
    data class UnexpectedException(val exception: Exception) : AutoDiscoveryResult
}

/**
 * Incoming mail server settings.
 *
 * Implementations contain protocol-specific properties.
 */
interface IncomingServerSettings

/**
 * Outgoing mail server settings.
 *
 * Implementations contain protocol-specific properties.
 */
interface OutgoingServerSettings
