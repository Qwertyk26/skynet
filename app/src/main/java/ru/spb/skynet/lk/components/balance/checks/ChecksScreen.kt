package ru.spb.skynet.lk.components.balance.checks

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.remember
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
import ru.spb.skynet.lk.extensions.formatServerDate
import ru.spb.skynet.lk.extensions.parseCustomTimestamp
import ru.spb.skynet.lk.extensions.shimmer
import ru.spb.skynet.lk.ui.theme.SkynetGreen
import ru.spb.skynet.lk.viewModels.balance.BalanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecksScreen(navController: NavController, viewModel: BalanceViewModel) {

    val checkList by viewModel.checksData.collectAsStateWithLifecycle()
    val networkState by viewModel.networkState.collectAsStateWithLifecycle()

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    LaunchedEffect(Unit) {
        if (checkList == null) {
            viewModel.checks()
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
                            stringResource(R.string.check),
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
                        List(5) { null }
                    } else {
                        checkList?.data ?: emptyList()
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
                            ) { check ->
                                ListItem(
                                    modifier = Modifier.clickable(indication = null,
                                        interactionSource = remember { MutableInteractionSource() },
                                        enabled = !isLoading) {
                                        navController.navigate("cash_receipt/${check?.id}")
                                    },
                                    colors = ListItemDefaults.colors(containerColor = Color.White),
                                    headlineContent = {
                                        Column(modifier = Modifier.fillMaxWidth()) {
                                            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = if (isLoading) "00.00.0000 00:00" else check?.time?.formatServerDate()
                                                        ?: "",
                                                    color = if (isLoading) Color.Transparent else Color.LightGray,
                                                    modifier = Modifier.shimmer(
                                                        12.dp,
                                                        isLoading = isLoading
                                                    )
                                                )
                                                Spacer(modifier = Modifier.weight(1f))

                                                val money = check?.money ?: ""
                                                Text(
                                                    text = if (isLoading) "0000 ₽ " else "$money₽",
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.Black,
                                                    modifier = Modifier.shimmer(
                                                        12.dp,
                                                        isLoading = isLoading
                                                    )
                                                )
                                            }
                                        }
                                    },
                                    trailingContent = {
                                        Icon(
                                            modifier = Modifier.size(12.dp).shimmer(12.dp, isLoading = isLoading),
                                            imageVector = Icons.Default.ArrowForwardIos,
                                            tint = Color.LightGray,
                                            contentDescription = null
                                        )
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