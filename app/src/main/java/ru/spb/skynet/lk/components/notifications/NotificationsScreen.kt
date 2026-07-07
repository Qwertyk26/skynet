package ru.spb.skynet.lk.components.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ru.spb.skynet.lk.R
import ru.spb.skynet.lk.components.widgets.BlurLoadingIndicator
import ru.spb.skynet.lk.data.network.NetworkState
import ru.spb.skynet.lk.extensions.shimmer
import ru.spb.skynet.lk.extensions.toFormattedDate
import ru.spb.skynet.lk.ui.theme.SkynetGreen
import ru.spb.skynet.lk.viewModels.notifications.NotificationsViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NotificationsScreen(navController: NavController, viewModel: NotificationsViewModel) {

    val itemsList by viewModel.notificationsList.collectAsStateWithLifecycle()
    val networkState by viewModel.networkState.collectAsStateWithLifecycle()

    val pullToRefreshState = rememberPullToRefreshState()
    val isRefreshing = networkState is NetworkState.Loading

    LaunchedEffect(Unit) {
        viewModel.notifications()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                Column {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                        title = {
                            Text(
                                text = stringResource(R.string.notifications),
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
                    .background(Color.White)
            ) {
                if (isRefreshing && itemsList.isEmpty()) {
                    BlurLoadingIndicator(
                        modifier = Modifier
                            .size(56.dp)
                            .padding(innerPadding)
                            .align(Alignment.Center),
                        color = SkynetGreen
                    )
                }
                else if (networkState is NetworkState.Error && itemsList.isEmpty()) {
                    Text(
                        text = stringResource(R.string.empty),
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Gray
                    )
                }
                // СЦЕНАРИЙ 3: ВЫВОД СПИСКА ДАННЫХ
                else {
                    if (itemsList.isEmpty()) {
                        Text(
                            text = stringResource(R.string.empty),
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Gray
                        )
                    } else {
                        PullToRefreshBox(
                            isRefreshing = isRefreshing,
                            onRefresh = { viewModel.refreshNotifications() },
                            state = pullToRefreshState,
                            modifier = Modifier.fillMaxSize(),
                            indicator = {
                                PullToRefreshDefaults.LoadingIndicator(
                                    state = pullToRefreshState,
                                    isRefreshing = isRefreshing,
                                    modifier = Modifier.align(Alignment.TopCenter),
                                    color = SkynetGreen,
                                    containerColor = SkynetGreen.copy(alpha = 0.4f)
                                )
                            }
                        ) {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = innerPadding
                            ) {
                                itemsIndexed(
                                    items = itemsList,
                                    key = { _, item -> item.id ?: item.hashCode() }
                                ) { index, notificationItem ->

                                    LaunchedEffect(index) {
                                        if (index >= itemsList.size - 3) {
                                            viewModel.notifications()
                                        }
                                    }

                                    if (notificationItem != null) {
                                        ListItem(
                                            headlineContent = {
                                                Column() {
                                                    Text(
                                                        modifier = Modifier.shimmer(12.dp)
                                                            .padding(),
                                                        text = notificationItem.message ?: "",
                                                        color = Color.Black
                                                    )
                                                    Spacer(modifier = Modifier.height(5.dp))
                                                    Text(
                                                        modifier = Modifier.shimmer(12.dp),
                                                        text = notificationItem.timestampDelivery?.toFormattedDate() ?: "",
                                                        color = Color.Gray
                                                    )
                                                }
                                            },
                                            supportingContent = {

                                            },
                                            colors = ListItemDefaults.colors(
                                                containerColor = Color.White,
                                                headlineColor = Color.Black,
                                                supportingColor = Color.Gray
                                            )
                                        )
                                        HorizontalDivider(
                                            modifier = Modifier.padding(horizontal = 16.dp),
                                            thickness = 0.5.dp,
                                            color = Color.LightGray.copy(alpha = 0.3f)
                                        )
                                    }
                                }

                                if (isRefreshing && itemsList.isNotEmpty()) {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            BlurLoadingIndicator(
                                                modifier = Modifier.size(32.dp),
                                                color = SkynetGreen
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
