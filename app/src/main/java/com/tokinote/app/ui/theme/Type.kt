@file:OptIn(ExperimentalTextApi::class)

package com.tokinote.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.tokinote.app.R

/**
 * 左侧抽屉标题使用的展示字体（Google Fonts / Micro 5 Charted）。
 * 正文和界面排版不用它，走 [GoogleSansFlex]。
 */
val Micro5Charted = FontFamily(
    Font(
        resId = R.font.micro5_charted,
        weight = FontWeight.Normal,
        style = FontStyle.Normal,
    ),
)

/**
 * 字重只用两档（对照 TokiStudio 0.1.10 / TokiUI 0.2.0-b1）：
 * 标题、标签用 FontWeight.Bold（wght 700）；正文用 FontWeight.Normal（wght 400）。
 * 层级靠字号和行距，不再在 regular / medium / semiBold 之间混档。
 * 15 个 emphasized 样式一并补齐，之前只有 15 个基础样式。
 */
fun Typography.withFont(family: FontFamily): Typography {
    fun TextStyle.bold() = copy(fontFamily = family, fontWeight = FontWeight.Bold)
    fun TextStyle.regular() = copy(fontFamily = family, fontWeight = FontWeight.Normal)
    return Typography(
        displayLarge = displayLarge.bold(), displayMedium = displayMedium.bold(), displaySmall = displaySmall.bold(),
        headlineLarge = headlineLarge.bold(), headlineMedium = headlineMedium.bold(), headlineSmall = headlineSmall.bold(),
        titleLarge = titleLarge.bold(), titleMedium = titleMedium.bold(), titleSmall = titleSmall.bold(),
        bodyLarge = bodyLarge.regular(), bodyMedium = bodyMedium.regular(), bodySmall = bodySmall.regular(),
        labelLarge = labelLarge.bold(), labelMedium = labelMedium.bold(), labelSmall = labelSmall.bold(),
        displayLargeEmphasized = displayLargeEmphasized.bold(), displayMediumEmphasized = displayMediumEmphasized.bold(), displaySmallEmphasized = displaySmallEmphasized.bold(),
        headlineLargeEmphasized = headlineLargeEmphasized.bold(), headlineMediumEmphasized = headlineMediumEmphasized.bold(), headlineSmallEmphasized = headlineSmallEmphasized.bold(),
        titleLargeEmphasized = titleLargeEmphasized.bold(), titleMediumEmphasized = titleMediumEmphasized.bold(), titleSmallEmphasized = titleSmallEmphasized.bold(),
        bodyLargeEmphasized = bodyLargeEmphasized.bold(), bodyMediumEmphasized = bodyMediumEmphasized.bold(), bodySmallEmphasized = bodySmallEmphasized.bold(),
        labelLargeEmphasized = labelLargeEmphasized.bold(), labelMediumEmphasized = labelMediumEmphasized.bold(), labelSmallEmphasized = labelSmallEmphasized.bold(),
    )
}

val TokiTypography: Typography = Typography()
    .withFont(GoogleSansFlex)
    .copy(
        displayLarge = Typography().displayLarge.copy(
            fontFamily = GoogleSansFlex,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-1.5).sp,
        ),
        headlineLarge = Typography().headlineLarge.copy(
            fontFamily = GoogleSansFlex,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 38.sp,
            letterSpacing = (-0.4).sp,
        ),
        headlineMedium = Typography().headlineMedium.copy(
            fontFamily = GoogleSansFlex,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            lineHeight = 34.sp,
            letterSpacing = (-0.3).sp,
        ),
        titleLarge = Typography().titleLarge.copy(
            fontFamily = GoogleSansFlex,
            fontWeight = FontWeight.Bold,
        ),
        titleMedium = Typography().titleMedium.copy(
            fontFamily = GoogleSansFlex,
            fontWeight = FontWeight.Bold,
        ),
        titleSmall = Typography().titleSmall.copy(
            fontFamily = GoogleSansFlex,
            fontWeight = FontWeight.Bold,
        ),
        bodyLarge = Typography().bodyLarge.copy(
            fontFamily = GoogleSansFlex,
            fontWeight = FontWeight.Normal,
            lineHeight = 26.sp,
        ),
        labelLarge = Typography().labelLarge.copy(
            fontFamily = GoogleSansFlex,
            fontWeight = FontWeight.Bold,
        ),
        labelMedium = Typography().labelMedium.copy(
            fontFamily = GoogleSansFlex,
            fontWeight = FontWeight.Bold,
        ),
    )
