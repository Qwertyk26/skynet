package ru.spb.skynet.lk.components.sessions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ru.spb.skynet.lk.R
import ru.spb.skynet.lk.components.widgets.BlurLoadingIndicator
import ru.spb.skynet.lk.data.network.NetworkState
import ru.spb.skynet.lk.extensions.shimmer
import ru.spb.skynet.lk.extensions.toFormattedDate
import ru.spb.skynet.lk.ui.theme.SkynetGreen
import ru.spb.skynet.lk.viewModels.sessions.SessionsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionsScreen(navController: NavController, viewModel: SessionsViewModel) {

    val sessionsList by viewModel.sessionsData.collectAsStateWithLifecycle()
    val sessionsState by viewModel.sessionsState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sessions()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                Column {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                        title = {
                            Text(
                                stringResource(R.string.active_sessions),
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
                when (sessionsState) {

                    is NetworkState.Loading -> {
                        BlurLoadingIndicator(
                            color = SkynetGreen,
                            modifier = Modifier
                        )
                    }


                    is NetworkState.Error -> {
                        Text(
                            text = stringResource(R.string.empty),
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Gray
                        )
                    }

                    is NetworkState.Success -> {
                        if (sessionsList?.data.isNullOrEmpty()) {
                            Text(
                                text = stringResource(R.string.empty),
                                modifier = Modifier.align(Alignment.Center),
                                color = Color.Gray
                            )
                        } else {
                            LazyColumn(modifier = Modifier.fillMaxSize(),
                                contentPadding = innerPadding) {
                                items(sessionsList?.data ?: emptyList(),  key = { session -> session?.id ?: session.hashCode() } ) { session ->
                                    ListItem(
                                        headlineContent = {
                                            Text(session?.useragent ?: "", modifier = Modifier.shimmer(12.dp, isLoading = sessionsState is NetworkState.Loading))
                                        },
                                        supportingContent = {
                                            Text(session?.updatedAt?.toFormattedDate() ?: "", modifier = Modifier.shimmer(12.dp, isLoading = sessionsState is NetworkState.Loading))
                                        },
                                        trailingContent = {
                                            IconButton(onClick = {

                                            }, modifier = Modifier.shimmer(12.dp, isLoading = sessionsState is NetworkState.Loading)) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = null,
                                                    tint = Color.Red.copy(alpha = 0.7f)
                                                )
                                            }
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
                        }
                    }
                    else -> Unit
                }
            }
        }
    }
}
