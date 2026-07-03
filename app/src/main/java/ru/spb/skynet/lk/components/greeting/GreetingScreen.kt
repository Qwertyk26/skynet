package ru.spb.skynet.lk.components.greeting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import ru.spb.skynet.lk.R
import ru.spb.skynet.lk.components.widgets.BlurLoadingIndicator
import ru.spb.skynet.lk.data.models.day.TimeOfDay
import ru.spb.skynet.lk.data.models.day.getTimeOfDay

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GreetingScreen(navController: NavController) {
    val timeOfDay = remember { getTimeOfDay() }

    val (greetingText, icon, iconTint, backgroundColor, progressColor, trackColor) = when (timeOfDay) {
        TimeOfDay.MORNING -> six(
            stringResource(R.string.good_morning),
            Icons.Default.WbSunny,
            Color(0xFFFFB74D),
            Color(0xFFFFF8E1),
            Color(0xFFFFB74D),
            Color(0xFFFFB74D).copy(alpha = 0.7f)
        )
        TimeOfDay.DAY -> six(
            stringResource(R.string.good_day),
            Icons.Default.WbSunny,
            Color(0xFFFFB300),
            Color(0xFFE3F2FD),
            Color(0xFFFFB300),
            Color(0xFFFFB300).copy(alpha = 0.7f)
        )
        TimeOfDay.EVENING -> six(
            stringResource(R.string.good_evening),
            Icons.Default.WbTwilight,
            Color(0xFFFF7043),
            Color(0xFFFFF3E0),
            Color(0xFFFF7043),
            Color(0xFFFF7043).copy(alpha = 0.7f)
        )
        TimeOfDay.NIGHT -> six(
            stringResource(R.string.good_night),
            Icons.Default.NightsStay,
            Color(0xFF90CAF9),
            Color(0xFF1A237E),
            Color(0xFF90CAF9),
            Color(0xFF90CAF9).copy(alpha = 0.7f)
        )
    }

    val textColor = if (timeOfDay == TimeOfDay.NIGHT) Color.White else Color.Black
    val subTextColor = if (timeOfDay == TimeOfDay.NIGHT) Color.White.copy(alpha = 0.7f) else Color.Gray

    var iconVisible by remember { mutableStateOf(false) }
    var textVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        iconVisible = true
        delay(300)
        textVisible = true
    }

    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate("main")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {

        AnimatedVisibility(
            visible = iconVisible,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> -fullHeight },
                animationSpec = tween(durationMillis = 800)
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .padding(bottom = 0.dp),
                    tint = iconTint
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = textVisible,
                enter = fadeIn(animationSpec = tween(600))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = greetingText,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        modifier = Modifier.padding(top = 24.dp)
                    )
                    Text(
                        text = stringResource(R.string.have_a_nice_day),
                        fontSize = 18.sp,
                        color = subTextColor,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    BlurLoadingIndicator(
                        modifier = Modifier.size(56.dp),
                        color = progressColor
                    )
                }
            }
        }
    }
}

private data class Six<A, B, C, D, E, F>(
    val a: A, val b: B, val c: C, val d: D, val e: E, val f: F
)

private fun <A, B, C, D, E, F> six(a: A, b: B, c: C, d: D, e: E, f: F) = Six(a, b, c, d, e, f)
