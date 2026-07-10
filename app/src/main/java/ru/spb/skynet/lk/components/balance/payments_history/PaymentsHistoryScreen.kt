package ru.spb.skynet.lk.components.balance.payments_history

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ru.spb.skynet.lk.R
import ru.spb.skynet.lk.data.network.NetworkState
import ru.spb.skynet.lk.extensions.parseCustomTimestamp
import ru.spb.skynet.lk.extensions.shimmer
import ru.spb.skynet.lk.ui.theme.SkynetGreen
import ru.spb.skynet.lk.viewModels.balance.BalanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentsHistoryScreen(navController: NavController, viewModel: BalanceViewModel) {

    val historyList by viewModel.historyData.collectAsStateWithLifecycle()
    val networkState by viewModel.networkState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (historyList?.payments.isNullOrEmpty()) {
            viewModel.payments()
        }
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                    title = {
                        Text(
                            stringResource(R.string.payment_history),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                )

                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = Color.LightGray.copy(alpha = 0.5f)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            when (networkState) {
                is NetworkState.Error -> {
                    Text(
                        text = stringResource(R.string.empty),
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Gray
                    )
                }

                is NetworkState.Loading, is NetworkState.Success -> {
                    val isLoading = networkState is NetworkState.Loading

                    val itemsList = if (isLoading) {
                        List(7) { null }
                    } else {
                        historyList?.payments ?: emptyList()
                    }

                    if (!isLoading && itemsList.isEmpty()) {
                        Text(
                            text = stringResource(R.string.empty),
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Gray
                        )
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(
                                items = itemsList
                            ) { history ->
                                ListItem(
                                    colors = ListItemDefaults.colors(containerColor = Color.White),
                                    headlineContent = {

                                            Column(modifier = Modifier.fillMaxWidth()) {
                                                Row(modifier = Modifier.fillMaxWidth()) {
                                                    Text(
                                                        text = if (isLoading) "00.00.0000 00:00" else history?.dateTime?.parseCustomTimestamp()
                                                            ?: "",
                                                        color = if (isLoading) Color.Transparent else Color.LightGray,
                                                        modifier = Modifier.shimmer(
                                                            12.dp,
                                                            isLoading = isLoading
                                                        )
                                                    )
                                                    Spacer(modifier = Modifier.weight(1f))

                                                    val cash = history?.cashFlow ?: 0
                                                    Text(
                                                        text = if (isLoading) " -0000 ₽ " else if (cash > 0) "+$cash ₽" else "$cash ₽",
                                                        fontWeight = FontWeight.Bold,
                                                        color = when {
                                                            isLoading -> Color.Transparent
                                                            cash < 0 -> Color.Red
                                                            else -> SkynetGreen
                                                        },
                                                        modifier = Modifier.shimmer(
                                                            12.dp,
                                                            isLoading = isLoading
                                                        )
                                                    )
                                                }
                                                Text(
                                                    text = if (isLoading) "Длинное описание платежа для корректного шиммера" else history?.details ?: "",
                                                    color = if (isLoading) Color.Transparent else Color.Unspecified,
                                                    modifier = Modifier.shimmer(12.dp, isLoading = isLoading).padding(top = if (isLoading) { 20.dp } else { 0.dp })
                                                )
                                        }
                                    },
                                    supportingContent = {

                                    }
                                )
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    thickness = 0.5.dp,
                                    color = Color.LightGray.copy(alpha = 0.3f)
                                )
                            }
                        }
                    }
                }
                else -> Unit
            }
        }
    }
}
