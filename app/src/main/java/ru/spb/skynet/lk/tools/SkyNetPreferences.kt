package ru.spb.skynet.lk.tools

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.spb.skynet.lk.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SkyNetPreferences@Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private val Context.dataStore by preferencesDataStore(name = BuildConfig.APPLICATION_ID)
        private val GENESIS_SESSION_REFRESH = stringPreferencesKey("GenesisSessionRefresh")
        private val USE_BIOMETRIC = booleanPreferencesKey("UseBiometric")
    }

    val sessionFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[GENESIS_SESSION_REFRESH] }

    val biometricFlow: Flow<Boolean?> = context.dataStore.data
        .map { preferences -> preferences[USE_BIOMETRIC] }

    suspend fun saveBiometric(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[USE_BIOMETRIC] = enabled
        }
    }

    suspend fun saveSession(session: String) {
        context.dataStore.edit { preferences ->
            preferences[GENESIS_SESSION_REFRESH] = session
        }
    }

    suspend fun cleaSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(GENESIS_SESSION_REFRESH)
        }
    }
}