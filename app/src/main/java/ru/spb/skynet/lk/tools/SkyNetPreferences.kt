package ru.spb.skynet.lk.tools

import android.content.Context
import android.os.Build
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.spb.skynet.lk.BuildConfig

class SkyNetPreferences(private val context: Context) {
    companion object {
        private val Context.dataStore by preferencesDataStore(name = BuildConfig.APPLICATION_ID)
        private val GENESIS_SESSION_REFRESH = stringPreferencesKey("GenesisSessionRefresh")
    }

    
    val sessionFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[GENESIS_SESSION_REFRESH] }

    suspend fun saveSession(session: String) {
        context.dataStore.edit { preferences ->
            preferences[GENESIS_SESSION_REFRESH] = session
        }
    }

    suspend fun cleaSeession() {
        context.dataStore.edit { preferences ->
            preferences.remove(GENESIS_SESSION_REFRESH)
        }
    }
}