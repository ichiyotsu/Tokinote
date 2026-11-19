package com.tokinote.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tokinote.app.R
import com.tokinote.app.ui.Dest
import com.tokinote.app.ui.pressScale
import com.tokinote.app.ui.theme.TokiChromeStroke

private data class NavEntry(
    val dest: Dest,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val label: Int,
)

private val navEntries = listOf(
    NavEntry(Dest.Notes, Icons.Outlined.StickyNote2, Icons.Filled.StickyNote2, R.string.nav_notes),
    NavEntry(Dest.Album, Icons.Outlined.Photo, Icons.Filled.Photo, R.string.nav_album),
    NavEntry(Dest.Focus, Icons.Outlined.Timer, Icons.Filled.Timer, R.string.nav_focus),
    NavEntry(Dest.Settings, Icons.Outlined.Settings, Icons.Filled.Settings, R.string.nav_settings),
)

/**
 * 悬浮导航栏（对照 TokiStudio 0.1.10 的 TokiFloatingNavBar）：
 * 单层毛玻璃胶囊 + 条目 56dp + 选中实色 pill 标签弹簧展开。
 *
 * - 颜色：全部取当前 MD3E ColorScheme 的 roles —— 选中 pill = primary /
 *   onPrimary，未选中图标 = onSurfaceVariant，涟漪 = primary 28%；
 * - 弹簧：不写死数值，全部映射到 MaterialTheme.motionScheme 的槽位 ——
 *   胶囊尺寸变化 → defaultSpatialSpec；标签展开 → fastSpatialSpec；
 *   标签收起 → defaultSpatialSpec；淡入淡出 → defaultEffectsSpec；
 * - 形状：胶囊 = MD3E 的 full 角（50% 圆角）；条目 pill = CircleShape；
 * - 玻璃：只挂一层调用方给的 [glass]（内容源采样的 chrome，见
 *   `tokiContentChrome`——胶囊宽度随标签动，平台窗口背景采样在尺寸
 *   动画里边缘会发黑）。磨砂打开时胶囊不加粗描边；[solidStroke] 为真
 *   （玻璃关闭或采样不可用）时，外沿加一道与滚动标题色块同粗的强调色描边，
 *   选中 pill 和描边之间留出 [TokiChromeStroke.barGap]。选中 pill 是实色块，
 *   可读性不依赖模糊；
 * - 高度：条目 56dp + 胶囊内边距。磨砂时上下各 8dp，总高 72dp；实心描边时
 *   内边距含描边厚度和间隙。悬浮间距由 App 层挂载。
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FloatingNavBar(
    current: Dest,
    onSelect: (Dest) -> Unit,
    modifier: Modifier = Modifier,
    glass: Modifier = Modifier,
    showFocus: Boolean = true,
    showAlbum: Boolean = true,
    solidStroke: Boolean = false,
    action: (@Composable () -> Unit)? = null,
) {
    val shape = RoundedCornerShape(percent = 50)
    val inset = if (solidStroke) TokiChromeStroke.width + TokiChromeStroke.barGap else 8.dp
    val entries = navEntries.filter { (it.dest != Dest.Focus || showFocus) && (it.dest != Dest.Album || showAlbum) }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // 尺寸动画与裁剪分离：胶囊的玻璃跟动画尺寸走（裁成胶囊形），
            // 条目行是撑起胶囊的唯一普通子项——matchParentSize 的子项不参与
            // Box 尺寸测量，两个子项都挂它会把胶囊压成 0（0.1.7 底栏消失的
            // 原因）。标签展开用快弹簧，会超调，切换时新旧两个标签同时在场，
            // 内容瞬时宽度会超过胶囊的动画宽度。裁剪护栏挂在条目行上：以行宽
            // 为界，超调只允许伸进与侧钮之间的 8dp 缝隙，再往外切掉——否则
            // 内容滑到侧钮的玻璃底下，右边看起来缺一块。护栏不挂在外层 Box
            // 上：玻璃盒子不能被动画中的裁剪矩形切到，否则胶囊边缘在动画里会发黑。
            Box(
                modifier = Modifier
                    .animateContentSize(animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    Modifier
                        .matchParentSize()
                        .clip(shape)
                        .then(glass)
                        .then(
                            if (solidStroke) {
                                Modifier.border(TokiChromeStroke.width, MaterialTheme.colorScheme.primary, shape)
                            } else {
                                Modifier
                            },
                        ),
                )
                Row(
                    modifier = Modifier
                        .padding(horizontal = inset, vertical = inset)
                        .drawWithContent {
                            val slack = 8.dp.toPx()
                            clipRect(left = -slack, top = 0f, right = size.width + slack, bottom = size.height) {
                                this@drawWithContent.drawContent()
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    entries.forEach { entry ->
                        NavItem(
                            entry = entry,
                            selected = current == entry.dest,
                            onSelect = onSelect,
                        )
                    }
                }
            }
            action?.invoke()
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun NavItem(
    entry: NavEntry,
    selected: Boolean,
    onSelect: (Dest) -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val haptic = LocalHapticFeedback.current
    TokiLongPressLabel(stringResource(entry.label)) {
        Row(
            modifier = Modifier
                .height(56.dp)
                .clip(CircleShape)
                .background(
                    if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
                )
                .clickable(
                    interactionSource = interaction,
                    indication = ripple(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.28f)),
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onSelect(entry.dest)
                    },
                )
                .pressScale(interaction, pressedScale = 0.92f)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = if (selected) entry.selectedIcon else entry.icon,
                contentDescription = stringResource(entry.label),
                tint = if (selected) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.size(22.dp),
            )
            AnimatedVisibility(
                visible = selected,
                enter = expandHorizontally(
                    animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
                ) + fadeIn(animationSpec = MaterialTheme.motionScheme.defaultEffectsSpec()),
                exit = shrinkHorizontally(
                    animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec(),
                ) + fadeOut(animationSpec = MaterialTheme.motionScheme.defaultEffectsSpec()),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = stringResource(entry.label),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

/**
 * 底栏旁的独立圆形玻璃按钮（对照 TokiStudio 0.1.10 的 TokiSideActionButton）：
 * 胶囊外右侧、与胶囊间隔 8dp 的一颗独立按钮，与胶囊条目同高 56dp，
 * 玻璃与胶囊同一层 [glass]。之前是 64dp 主色方钮，不再使用。
 * 玻璃关闭时（莫奈悬浮底栏）：不画玻璃也不描边，整个圆填当前底栏边框色
 * （primary），图标改 onPrimary。调用方用 [solidFill] 与底栏的 solidStroke
 * 取同一个状态。支持长按：有长按菜单时菜单自己占用长按，名称提示不再一起弹出。
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NavActionButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    glass: Modifier = Modifier,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null,
    solidFill: Boolean = false,
) {
    val interaction = remember { MutableInteractionSource() }
    val haptic = LocalHapticFeedback.current
    val tooltipState = rememberTooltipState()
    // 布局和命中都锁在 56dp 圆里。模糊和按压缩放不能画出这个圆，
    // 否则动画过程中会盖住旁边的胶囊。可见间距仍由外层 8dp 决定。
    Box(modifier.requiredSize(56.dp).clipToBounds()) {
        TooltipBox(
            positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
            tooltip = { PlainTooltip { Text(contentDescription) } },
            state = tooltipState,
            // 已有长按菜单时，菜单自己占用长按，名称提示不再一起弹出——
            // 提示浮在按钮上方，会被随后弹出的菜单盖住。
            enableUserInput = onLongClick == null,
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .then(if (solidFill) Modifier.background(MaterialTheme.colorScheme.primary) else glass)
                    .combinedClickable(
                        interactionSource = interaction,
                        indication = ripple(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.28f)),
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onClick()
                        },
                        onLongClick = onLongClick?.let { action ->
                            {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                action()
                            }
                        },
                    )
                    .pressScale(interaction),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    icon,
                    contentDescription = contentDescription,
                    tint = if (solidFill) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}
