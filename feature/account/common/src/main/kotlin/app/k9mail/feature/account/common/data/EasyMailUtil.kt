package app.k9mail.feature.account.common.data

import android.util.Log
import com.fsck.k9.helper.EmailHelper
import com.hungbang.email2018.data.entity.Account
import com.hungbang.email2018.data.entity.SignInConfigs
import io.paperdb.Paper

object EasyMailUtil {
    /**
     * used to get account email & password
     */
    fun getSavedAccountFromEasyMail(): Account? {
        val fakeAcc = Account().apply {
            accountEmail = "easyai.group@mailo.com"
            password = "Matkhausieumanh1"
        }
//        return Paper.book().read<Account>("CURRENT_ACCOUNT", null)
        return fakeAcc
    }

    /**
     * used to get host, port...
     */
    fun getSavedSignInConfigFromEasyMail(mailDomain: String): SignInConfigs? {
        val fakeConfigs = SignInConfigs(
            mailDomain,
            "mail.mailo.com",
            "993",
            "1",
            "mail.mailo.com",
            "465",
            "1"
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
