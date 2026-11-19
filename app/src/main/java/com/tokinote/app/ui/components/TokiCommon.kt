package com.tokinote.app.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * 只显示图标的控件，长按弹出功能名称（对照 TokiStudio 0.1.10）。
 * Material 3 的 [PlainTooltip]：短按仍走原来的点击，长按才出现名称。
 * 名称同时是无障碍朗读文本。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TokiLongPressLabel(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
        tooltip = { PlainTooltip { Text(label) } },
        state = rememberTooltipState(),
        modifier = modifier,
    ) {
        content()
    }
}
