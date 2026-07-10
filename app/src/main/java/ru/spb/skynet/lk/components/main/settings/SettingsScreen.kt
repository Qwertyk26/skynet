package ru.spb.skynet.lk.components.main.settings

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ru.spb.skynet.lk.R
import ru.spb.skynet.lk.components.dialogs.logout.LogOutDialog
import ru.spb.skynet.lk.components.progress_bar.ProgressBar
import ru.spb.skynet.lk.data.models.components.bar.NavRoutes
import ru.spb.skynet.lk.data.models.settings.SettingsClickableRow
import ru.spb.skynet.lk.data.models.settings.SettingsHeader
import ru.spb.skynet.lk.data.models.settings.SettingsSwitchRow
import ru.spb.skynet.lk.viewModels.settings.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, viewModel: SettingsViewModel) {

    var showDialog by remember { mutableStateOf(false) }
    var showProgressBar by remember { mutableStateOf(false) }
    var changePinCode by remember { mutableStateOf(false) }
    var changePassword by remember { mutableStateOf(false) }
    val settings by viewModel.settingsData.collectAsStateWithLifecycle()
    val useBiometric by viewModel.useBiometric.collectAsStateWithLifecycle()

    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.White.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    LaunchedEffect(Unit) {
        viewModel.settings()
    }

    LaunchedEffect(useBiometric) {
        viewModel.getBiometric()
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                    title = { Text(
                        stringResource(R.string.settings),
                        fontWeight = FontWeight.Bold
                    )
                    }
                )
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = Color.LightGray.copy(alpha = 0.5f)
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item { SettingsHeader(title = stringResource(R.string.safety)) }
            item {
                SettingsClickableRow(
                    title = stringResource(R.string.change_pin_code),
                    subtitle = stringResource(R.string.change_pin_code_subtitle),
                    icon = null,
                    onClick = {
                        changePinCode = true
                    }
                )
            }
            item {
                SettingsClickableRow(
                    title = stringResource(R.string.change_password),
                    subtitle = stringResource(R.string.change_pin_code_subtitle),
                    icon = null,
                    onClick = {
                        changePassword = true
                    }
                )
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(R.string.biometry),
                    subtitle = stringResource(R.string.biometry_subtitle),
                    icon = null,
                    checked = useBiometric ?: false,
                    onCheckedChange = { checked ->
                        viewModel.toggleBiometric(checked)
                    }
                )
            }
            item { HorizontalDivider(
                thickness = 0.5.dp,
                modifier = Modifier.padding(vertical = 8.dp))
            }
            item {
                SettingsClickableRow(
                    title = stringResource(R.string.active_sessions),
                    subtitle = null,
                    icon = null,
                    onClick = {
                        navController.navigate(NavRoutes.Sessions.route)
                    }
                )
            }
            item { HorizontalDivider(
                thickness = 0.5.dp,
                modifier = Modifier.padding(vertical = 8.dp))
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(R.string.advance_payment),
                    subtitle = stringResource(R.string.advance_payment_subtitle),
                    icon = null,
                    checked = settings?.data?.blUSAvans?.contains("yes") ?: false,
                    onCheckedChange = { checked ->

                    }
                )
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(R.string.pools),
                    subtitle = null,
                    icon = null,
                    checked = settings?.data?.blUSPoll == "yes",
                    onCheckedChange = { checked ->
                        viewModel.togglePools(checked)
                    }
                )
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(R.string.channels_18),
                    subtitle = null,
                    icon = null,
                    checked = false,
                    onCheckedChange = { checked ->

                    }
                )
            }
            item { HorizontalDivider(
                thickness = 0.5.dp,
                modifier = Modifier.padding(vertical = 8.dp))
            }
            item {
                Button(
                    modifier = Modifier.fillMaxWidth().height(48.dp).padding(horizontal = 20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ),
                    shape = RoundedCornerShape(12.dp),
                    onClick = {
                        showDialog = true
                    }
                ) {
                    Text(
                        text = stringResource(R.string.logout),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
    if (showDialog) {
        LogOutDialog(onDismiss = {
            showDialog = false
        }, onConfirm = {
            showDialog = false
            showProgressBar = true
        })
    }
    if (showProgressBar) {
        ProgressBar()
    }
    if (changePinCode) {
        navController.navigate(NavRoutes.ChangePinCode.route)
    }
    if (changePassword) {
        navController.navigate(NavRoutes.ChangePassword.route)
    }
}