package app.k9mail.core.android.permissions

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val PERMISSION_PREF_NAME = "permission_pref"
private val Context.dataStore by preferencesDataStore(PERMISSION_PREF_NAME)

class AppPermissionDataStore(
     context: Context,
) {
    private val perStore = context.dataStore

    suspend fun increaseDeclineTime(permission: String) {
        val key = intPreferencesKey(permission)
        Log.d("TAG", "NamTD8 increaseDeclineTime: ${getDeclineTime(permission) + 1} ${permission}")
        perStore.edit {
            it[key] = getDeclineTime(permission) + 1
        }
    }

    private suspend fun getDeclineTime(permission: String): Int {
        val key = intPreferencesKey(permission)
        return perStore.data.map {
            it[key]
        }.first() ?: 0
    }

    suspend fun isPermissionBlock(permission: String): Boolean {
        return getDeclineTime(permission) >= 2
    }

    companion object {
        private val KEY_LOGIN_STATE = stringPreferencesKey("keyLoginState")
        private val KEY_IS_USER_PURCHASED = booleanPreferencesKey("key_is_user_purchased")
        private val KEY_LAST_MESSAGE_READ = stringPreferencesKey("keyLastMessageRead")
    }
}
