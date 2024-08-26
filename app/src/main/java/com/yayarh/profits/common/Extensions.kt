package com.yayarh.profits.common

fun String?.nullIfBlank() = if (this.isNullOrBlank()) null else this