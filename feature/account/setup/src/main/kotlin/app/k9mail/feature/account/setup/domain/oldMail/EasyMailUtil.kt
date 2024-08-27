package app.k9mail.feature.account.setup.domain.oldMail

import com.hungbang.email2018.data.entity.Account
import com.hungbang.email2018.data.entity.SignInConfigs
import io.paperdb.Paper

object EasyMailUtil {
    /**
     * used to get account email & password
     */
    fun getSavedAccountFromEasyMail(): Account? {
        val fakeAcc = Account().apply {
            accountType = 3
            accountEmail = "trandinhnam1199@yandex.com"
            password = "sbfksbfprvricpvk"
        }
//        return Paper.book().read<Account>("CURRENT_ACCOUNT", null)
        return fakeAcc
    }

    /**
     * used to get host, port...
     */
    fun getSavedSignInConfigFromEasyMail(mailDomain: String?): SignInConfigs? {
        val fakeConfigs = SignInConfigs(
            mailDomain ?: "",
            "mail.mailo.com",
            "993",
            "1",
            "mail.mailo.com",
            "465",
            "0",
        )
//        return fakeConfigs
        return null
    }

//    fun testGetSavedDataFromEasyMail(){
//        val acc = getSavedAccountFromEasyMail()
//        Log.d("hungnd", "testGetSavedDataFromEasyMail: saved acc: $acc")
//        if(acc != null){
//            val domain = EmailHelper.getDomainFromEmailAddress(acc.accountEmail)
//            val configs = domain?.let { getSavedSignInConfigFromEasyMail(it) }
//            Log.d("hungnd", "testGetSavedDataFromEasyMail: configs: $configs")
//        } else {
//            Log.d("hungnd", "testGetSavedDataFromEasyMail: no saved acc")
//        }
//    }
}
