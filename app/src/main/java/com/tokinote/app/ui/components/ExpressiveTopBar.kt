package com.tokinote.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tokinote.app.ui.pressScale
import com.tokinote.app.ui.theme.LocalBackdropBlur
import com.tokinote.app.ui.theme.LocalHazeState
import com.tokinote.app.ui.theme.TokiScrollChipBackdrop
import com.tokinote.app.ui.theme.TokiSpacing
import dev.chrisbanes.haze.HazeState

/**
 * 全 App 统一的顶栏（对照 TokiStudio 0.1.10 的 OverlayTitleHeader）：
 * 标题只用一个文字角色（headlineMedium、粗体），返回键是 56dp 圆 chip。
 * 返回键与标题默认就带同一块磨砂（或实色退路），不随滚动出现或消失；
 * 两者间距固定 12dp。能采样时色块只做与底栏相同的磨砂，标题直接放在
 * 磨砂上，不再叠实色。不能采样时是不透明表面加与实心底栏同粗的描边。
 * 字始终用 onSurface。
 *
 * 之前各页的顶栏是透明 TopAppBar + headlineLarge 32sp，各页自己的返回按钮
 * 还是 44dp 的 GlassIconButton，字号、字重、返回键尺寸都不统一，现在收敛到这一处。
 * subtitle 是 Tokinote 自己的扩展（归档页的副标题），没有时只有标题一行。
 */
@Composable
fun ExpressiveTopBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
) {
    val titleShape = RoundedCornerShape(percent = 50)
    Row(
        modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp)
            .padding(horizontal = TokiSpacing.pageMargin),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onBack != null) {
            TokiScrollBackChip(onClick = onBack)
            Spacer(Modifier.width(TokiSpacing.md))
        }
        Box(
            Modifier
                .weight(1f, fill = false)
                .heightIn(min = 56.dp)
                .clip(titleShape),
            contentAlignment = Alignment.CenterStart,
        ) {
            TokiScrollChipBackdrop(
                shape = titleShape,
                modifier = Modifier.matchParentSize(),
            )
            Column(
                modifier = Modifier.padding(horizontal = TokiSpacing.titleGlyphInset),
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (!subtitle.isNullOrBlank()) {
                    Text(
                        subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

/** 返回键自己的大圆角色块，高度和标题胶囊对齐。默认就带磨砂（与标题同一套），不随滚动变化。 */
@Composable
fun TokiScrollBackChip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backdropBlur: Boolean = LocalBackdropBlur.current,
    hazeState: HazeState? = LocalHazeState.current,
) {
    val interaction = remember { MutableInteractionSource() }
    val content = MaterialTheme.colorScheme.onSurface
    TokiLongPressLabel("返回", modifier) {
        Box(
            Modifier
                .size(56.dp)
                .clip(CircleShape)
                .clickable(interactionSource = interaction, indication = ripple()) { onClick() }
                .pressScale(interaction),
            contentAlignment = Alignment.Center,
        ) {
            TokiScrollChipBackdrop(
                shape = CircleShape,
                backdropBlur = backdropBlur,
                hazeState = hazeState,
                modifier = Modifier.matchParentSize(),
            )
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "返回",
                tint = content,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}
