package com.yayarh.profits.ui.utils

import android.content.Context
import androidx.annotation.StringRes
import com.yayarh.profits.common.nullIfBlank

sealed interface UiText {
    class StringResource(@StringRes val res: Int) : UiText
    class StringValue(val value: String) : UiText

    companion object {
        fun fromOrDefault(value: String?, @StringRes default: Int): UiText = value.nullIfBlank()?.let { StringValue(it) } ?: StringResource(default)

        fun from(@StringRes res: Int): UiText = StringResource(res)

        fun UiText.getText(context: Context): CharSequence = when (this) {
            is StringResource -> context.getText(res)
            is StringValue -> value
        }


    }

}