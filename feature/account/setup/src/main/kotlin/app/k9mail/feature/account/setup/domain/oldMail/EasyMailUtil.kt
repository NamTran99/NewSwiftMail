package app.k9mail.feature.account.setup.domain.oldMail

import android.util.Log
import com.fsck.k9.helper.EmailHelper
import app.k9mail.feature.account.setup.domain.entity.oldmail.OldAccount
import app.k9mail.feature.account.setup.domain.entity.oldmail.SignInConfigs

object EasyMailUtil {
    /**
     * used to get account email & password
     */
    fun getSavedAccountFromEasyMail(): OldAccount? {
        val fakeAcc = OldAccount().apply {
            accountEmail = "easyai.group@mailo.com"
            password = "Matkhausieumanh1"
        }
//        return Paper.book().read<Account>("CURRENT_ACCOUNT", null)
        return fakeAcc
    }

    /**
     * used to get host, port...
     */
    fun getSavedSignInConfigFromEasyMail(mailDomain: String?): SignInConfigs? {
        val fakeConfigs = SignInConfigs(
            mailDomain?:"",
            "mail.mailo.com",
            "993",
            "1",
            "mail.mailo.com",
            "465",
            "0"
        )
        return fakeConfigs
//        return Paper.book().read<SignInConfigs>("KEY_CONFIG_SIGNIN$mailDomain", null)
    }

    fun testGetSavedDataFromEasyMail(){
        val acc = getSavedAccountFromEasyMail()
        Log.d("hungnd", "testGetSavedDataFromEasyMail: saved acc: $acc")
        if(acc != null){
            val domain = EmailHelper.getDomainFromEmailAddress(acc.accountEmail)
            val configs = domain?.let { getSavedSignInConfigFromEasyMail(it) }
            Log.d("hungnd", "testGetSavedDataFromEasyMail: configs: $configs")
        } else {
            Log.d("hungnd", "testGetSavedDataFromEasyMail: no saved acc")
        }
    }
}
