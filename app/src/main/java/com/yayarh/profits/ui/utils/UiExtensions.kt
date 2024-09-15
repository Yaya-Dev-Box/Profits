package com.yayarh.profits.ui.utils

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.yayarh.profits.ui.theme.DarkGreen
import com.yayarh.profits.ui.utils.UiText.Companion.getText
import java.time.LocalDate

@Composable
fun ShowToast(uiText: UiText) = Toast.makeText(LocalContext.current, uiText.getText(LocalContext.current), Toast.LENGTH_SHORT).show()

@Composable
fun ShowToast(message: String) = Toast.makeText(LocalContext.current, message, Toast.LENGTH_SHORT).show()

@Composable
fun ShowToast(@StringRes messageRes: Int) = Toast.makeText(LocalContext.current, LocalContext.current.getString(messageRes), Toast.LENGTH_SHORT).show()

/** Get a color based on a value, green if positive, red if negative */
fun greenOrRed(value: Int): Color = if (value >= 0) DarkGreen else Color.Red

/** Get a color based on the date, green if the same as today, red if not */
fun greenOrRed(localDate: LocalDate): Color = if (localDate == LocalDate.now()) DarkGreen else Color.Red
