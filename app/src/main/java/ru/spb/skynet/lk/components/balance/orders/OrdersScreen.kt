package ru.spb.skynet.lk.components.balance.orders

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.valentinilk.shimmer.shimmer
import ru.spb.skynet.lk.R
import ru.spb.skynet.lk.data.models.response.orders.Order
import ru.spb.skynet.lk.data.network.NetworkState
import ru.spb.skynet.lk.extensions.shimmer
import ru.spb.skynet.lk.extensions.toFormattedDate
import ru.spb.skynet.lk.viewModels.balance.BalanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperationsScreen(navController: NavController, viewModel: BalanceViewModel) {

    val ordersList by viewModel.ordersData.collectAsStateWithLifecycle()
    val networkState by viewModel.networkState.collectAsStateWithLifecycle()

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    LaunchedEffect(Unit) {
        if (ordersList == null) {
            viewModel.orders()
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
                            stringResource(R.string.card_operations),
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
                .padding(top = innerPadding.calculateTopPadding())
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
                        ordersList?.orders ?: emptyList()
                    }

                    if (!isLoading && itemsList.isEmpty()) {
                        Text(
                            text = stringResource(R.string.empty),
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Gray
                        )
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(0.dp)) {
                            items(
                                items = itemsList
                            ) { order ->
                                ListItem(
                                    colors = ListItemDefaults.colors(containerColor = Color.White),
                                    trailingContent = {
                                        Icon(
                                            modifier = Modifier.shimmer(12.dp, isLoading = isLoading),
                                            imageVector = Icons.Default.Refresh,
                                            tint = Color.LightGray,
                                            contentDescription = null
                                        )

                                    },
                                    headlineContent = {
                                        Column(modifier = Modifier.fillMaxWidth()) {
                                            Row(modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = "Карта",
                                                    color = Color.Black,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.shimmer(12.dp, isLoading = isLoading)
                                                )

                                                Spacer(modifier = Modifier.weight(1f))

                                                Text(
                                                    text = if (isLoading) "0000 ₽" else "${
                                                        order?.money ?: 0} ₽",
                                                    color = if (isLoading) Color.Transparent else Color.Black,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.shimmer(12.dp, isLoading = isLoading)
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(4.dp))

                                            Text(
                                                text = if (isLoading) "00.00.0000 00:00" else order?.timestampUpdate?.toFormattedDate() ?: "",
                                                color = if (isLoading) Color.Transparent else Color.Gray,
                                                fontSize = 14.sp,
                                                modifier = Modifier.shimmer(12.dp, isLoading = isLoading)
                                            )

                                            Spacer(modifier = Modifier.height(4.dp))

                                            val statusText = if (isLoading) {
                                                "Длинный статус для корректного шиммера"
                                            } else {
                                                order?.getStatusText(ordersList?.statuses ?: emptyMap()) ?: ""
                                            }
                                            Text(
                                                text = statusText,
                                                fontSize = 14.sp,
                                                color = if (isLoading) {
                                                    Color.Transparent
                                                } else {
                                                    when(order?.queueStatus) {
                                                        3, 5, 10 -> Color(0xFF4CAF50)
                                                        4, 6, 7, 8, 9 -> Color.Red
                                                        else -> Color.Gray
                                                    }
                                                },
                                                modifier = Modifier.shimmer(12.dp, isLoading = isLoading)
                                            )
                                        }
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

fun Order.getStatusText(statuses: Map<String, String>): String { return statuses[queueStatus.toString()] ?: "Статус $queueStatus" }
