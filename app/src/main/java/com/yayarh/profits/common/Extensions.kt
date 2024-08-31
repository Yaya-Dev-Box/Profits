package com.yayarh.profits.common

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun String?.nullIfBlank() = if (this.isNullOrBlank()) null else this

fun Int?.zeroIfNull() = this ?: 0

fun LocalDate.toString(format: String) = this.format(DateTimeFormatter.ofPattern(format))