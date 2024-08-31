package com.yayarh.profits.ui.utils

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.yayarh.profits.ui.utils.UiText.Companion.getText

@Composable
fun ShowToast(uiText: UiText) = Toast.makeText(LocalContext.current, uiText.getText(LocalContext.current), Toast.LENGTH_SHORT).show()

@Composable
fun ShowToast(message: String) = Toast.makeText(LocalContext.current, message, Toast.LENGTH_SHORT).show()

@Composable
fun ShowToast(@StringRes messageRes: Int) = Toast.makeText(LocalContext.current, LocalContext.current.getString(messageRes), Toast.LENGTH_SHORT).show()