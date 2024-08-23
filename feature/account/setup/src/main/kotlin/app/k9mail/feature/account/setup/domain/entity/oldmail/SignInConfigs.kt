package app.k9mail.feature.account.setup.domain.entity.oldmail


/**
 * Created by Hungnd on 7/15/2017.
 */
data class SignInConfigs (
    val mail_domain: String,
    val imap_host: String ,
    val imap_port: String ,
    val imap_ssl: String ,
    val smtp_host: String ,
    val smtp_port: String ,
    val smtp_start_tls: String ,
){
    fun isSmtpStartTLS()  = smtp_start_tls == "1"
}
