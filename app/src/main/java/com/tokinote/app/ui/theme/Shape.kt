@file:OptIn(androidx.compose.material3.ExperimentalMaterial3ExpressiveApi::class)

package com.tokinote.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/** Expressive corner scale: 8 stops, rounder than the baseline M3 scale. */
val TokiShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(26.dp),
    extraLarge = RoundedCornerShape(36.dp),
    largeIncreased = RoundedCornerShape(32.dp),
    extraLargeIncreased = RoundedCornerShape(44.dp),
    extraExtraLarge = RoundedCornerShape(56.dp),
)

/** 笔记卡片：四角不对称，避免通用圆角的“模板感”。 */
val ExpressiveCardShape = RoundedCornerShape(
    topStart = 34.dp,
    topEnd = 22.dp,
    bottomEnd = 34.dp,
    bottomStart = 22.dp,
)

/** 分组胶囊：一端更圆、一端收紧，像一片被压过的叶子。 */
val ExpressiveChipShape = RoundedCornerShape(
    topStart = 20.dp,
    topEnd = 20.dp,
    bottomEnd = 20.dp,
    bottomStart = 7.dp,
)

/** Expressive FAB：略带动感的不对称容器。 */
val ExpressiveFabShape = RoundedCornerShape(
    topStart = 26.dp,
    topEnd = 26.dp,
    bottomEnd = 26.dp,
    bottomStart = 10.dp,
)

/** 编辑页悬浮工具岛 / 信息面板。 */
val ExpressiveIslandShape = RoundedCornerShape(34.dp)
val ExpressiveSheetShape = RoundedCornerShape(topStart = 34.dp, topEnd = 34.dp)

/**
 * 间距：M3 的 4dp 基线网格阶梯（对照 TokiStudio 0.1.10）；紧凑窗口的页面
 * 水平边距为 24dp。标题、底栏等统一读这里，不再各处手写数值。
 */
object TokiSpacing {
    val xs: androidx.compose.ui.unit.Dp = 4.dp
    val sm: androidx.compose.ui.unit.Dp = 8.dp
    val md: androidx.compose.ui.unit.Dp = 12.dp
    val lg: androidx.compose.ui.unit.Dp = 16.dp
    val xl: androidx.compose.ui.unit.Dp = 24.dp
    val xxl: androidx.compose.ui.unit.Dp = 32.dp

    /** 页面水平边距（紧凑窗口 margin）：24dp。 */
    val pageMargin: androidx.compose.ui.unit.Dp = xl

    /**
     * 大标题字形相对页面边距再往里的距离。滚动标题色块的文字内边距用它，
     * 这样标题和下面的正文左缘是同一条线。
     */
    val titleGlyphInset: androidx.compose.ui.unit.Dp = lg

    /** 悬浮容器与屏幕边缘的间距量级。 */
    val floatingBarMargin: androidx.compose.ui.unit.Dp = 16.dp

    /** 悬浮导航下方留给内容的安全间距（在实测底栏高度之外追加）。 */
    val contentGap: androidx.compose.ui.unit.Dp = 16.dp

    /** 窗口宽度达到此值时，主导航从悬浮胶囊换成 NavigationRail。 */
    val expandedWindow: androidx.compose.ui.unit.Dp = 840.dp
}

