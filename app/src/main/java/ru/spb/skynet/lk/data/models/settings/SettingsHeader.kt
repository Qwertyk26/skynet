package ru.spb.skynet.lk.data.models.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.spb.skynet.lk.ui.theme.SkynetGreen

@Composable
fun SettingsHeader(title: String) {
    Text(
        text = title,
        color = Color.Black,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

// 2. Элемент настройки с переключателем (Switch)
@Composable
fun SettingsSwitchRow(
    title: String,
    subtitle: String?,
    icon: @Composable (() -> Unit)?,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = subtitle?.let { { Text(it) } },
        leadingContent = icon,
        colors = ListItemDefaults.colors(
            headlineColor = Color.Black,
            containerColor = Color.White,
            supportingColor = Color.Gray
        ),
        trailingContent = {
            Switch(
                colors = SwitchDefaults.colors(
                    checkedBorderColor = SkynetGreen,
                    checkedThumbColor = SkynetGreen,
                    checkedTrackColor = Color.White,
                ),
                checked = checked,
                onCheckedChange = null
            )
        },
        modifier = Modifier.clickable(
            interactionSource = interactionSource,
            indication = null
        ) {
            onCheckedChange(!checked)
        }
    )
}

@Composable
fun SettingsClickableRow(
    title: String,
    subtitle: String?,
    icon: @Composable (() -> Unit)?,
    onClick: () -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }

    ListItem(
        headlineContent = { Text(title) },
        supportingContent = subtitle?.let { { Text(it) } },
        leadingContent = icon,
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.LightGray
            )
        },
        colors = ListItemDefaults.colors(
            headlineColor = Color.Black,
            containerColor = Color.White,
            supportingColor = Color.Gray
        ),
        modifier = Modifier.clickable(
            interactionSource = interactionSource,
            indication = null
        ) {
            onClick()
        }
    )
}