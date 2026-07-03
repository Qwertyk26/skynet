package ru.spb.skynet.lk.components.password.change

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.spb.skynet.lk.R
import ru.spb.skynet.lk.ui.theme.SkynetGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePassword(navController: NavController) {

    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var oldPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var repeatPasswordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                Column {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                        title = { Text(
                            stringResource(R.string.change_password),
                            fontWeight = FontWeight.Bold
                        ) },
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
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().background(Color.White).padding(20.dp)
                ) {
                    OutlinedTextField(
                        value = oldPassword,
                        onValueChange = {
                            oldPassword = it
                        },
                        modifier = Modifier.border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)).fillMaxWidth(),
                        placeholder = { Text(stringResource(R.string.old_password)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        visualTransformation = if (oldPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        trailingIcon = {
                            val image = if (oldPasswordVisible)
                                Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff

                            IconButton(onClick = { oldPasswordVisible =! oldPasswordVisible }) {
                                Icon(imageVector = image, contentDescription = "Toggle password visibility")
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedPlaceholderColor = Color.LightGray,
                            unfocusedPlaceholderColor = Color.LightGray,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            errorBorderColor = Color.Red,
                            cursorColor = SkynetGreen,
                        ),
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = {
                            newPassword = it
                        },
                        modifier = Modifier.border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)).fillMaxWidth(),
                        placeholder = { Text(stringResource(R.string.new_password)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        trailingIcon = {
                            val image = if (newPasswordVisible)
                                Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff

                            IconButton(onClick = { newPasswordVisible =! newPasswordVisible }) {
                                Icon(imageVector = image, contentDescription = "Toggle password visibility")
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedPlaceholderColor = Color.LightGray,
                            unfocusedPlaceholderColor = Color.LightGray,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            errorBorderColor = Color.Red,
                            cursorColor = SkynetGreen,
                        ),
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedTextField(
                        value = repeatPassword,
                        onValueChange = {
                            repeatPassword = it
                        },
                        modifier = Modifier.border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)).fillMaxWidth(),
                        placeholder = { Text(stringResource(R.string.repeat_password)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        visualTransformation = if (repeatPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (repeatPasswordVisible)
                                Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff

                            IconButton(onClick = { repeatPasswordVisible = ! repeatPasswordVisible }) {
                                Icon(imageVector = image, contentDescription = "Toggle password visibility")
                            }
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedPlaceholderColor = Color.LightGray,
                            unfocusedPlaceholderColor = Color.LightGray,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            errorBorderColor = Color.Red,
                            cursorColor = SkynetGreen,
                        ),
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(modifier = Modifier.height(56.dp).fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                        onClick = {
                            if (newPassword != repeatPassword) {
                                Toast.makeText(context, context.getString(R.string.password_not_same), Toast.LENGTH_SHORT).show()
                            }
                        }, colors = ButtonDefaults.buttonColors(
                            containerColor = SkynetGreen,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.save)
                        )
                    }
                }
            }
        }
    }
}