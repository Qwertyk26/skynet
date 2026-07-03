import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.spb.skynet.lk.ui.theme.SkynetGreen

@Composable
fun PinCodeDisplayFields(
    pinCode: String,
    length: Int = 4
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(length) { index ->
            val isFilled = index < pinCode.length

            Surface(
                modifier = Modifier.size(20.dp),
                shape = CircleShape,
                color = if (isFilled) SkynetGreen else Color.LightGray.copy(alpha = 0.5f)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (isFilled) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(SkynetGreen, CircleShape)
                        )
                    }
                }
            }
        }
    }
}
