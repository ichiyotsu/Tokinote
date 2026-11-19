package com.tokinote.app.ui.notes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tokinote.app.R
import com.tokinote.app.data.Note
import com.tokinote.app.ui.components.ExpressiveTopBar

/**
 * 相册组件的独立页面：按一般相册的形式直接展示全部照片的三列网格，
 * 左上角是「相册」大标题，点照片打开原来的图片查看器。
 *
 * 按 TokiUI 语义标准实现：用 Material 3 Expressive 的原生 TopAppBar
 * 与主题的颜色、字阶、形状角色，不自造颜色与尺寸；照片数据与
 * 抽屉里的「全部照片」同一来源（未归档、未删除笔记的全部图片）。
 */
@Composable
fun AlbumScreen(
    notes: List<Note>,
    contentPadding: PaddingValues,
    onOpenMedia: (String, Int) -> Unit,
) {
    val photos = remember(notes) {
        notes.filter { !it.archived && it.deletedAt == null }
            .sortedByDescending { it.updatedAt }
            .flatMap { note ->
                note.media.mapIndexed { index, path ->
                    AlbumPhoto(
                        noteId = note.id,
                        path = path,
                        index = index,
                        date = note.journalDate,
                        noteTitle = note.title,
                    )
                }
            }
            .sortedByDescending { it.date }
    }
    val layoutDirection = LocalLayoutDirection.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = contentPadding.calculateStartPadding(layoutDirection),
                top = contentPadding.calculateTopPadding(),
                end = contentPadding.calculateEndPadding(layoutDirection),
            ),
    ) {
        ExpressiveTopBar(
            title = stringResource(R.string.nav_album),
        )
        if (photos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.album_page_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 4.dp,
                    bottom = contentPadding.calculateBottomPadding() + 80.dp,
                ),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                items(photos, key = { it.path }) { photo ->
                    val image = rememberImage(photo.path)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                            .clickable { onOpenMedia(photo.noteId, photo.index) },
                    ) {
                        if (image != null) {
                            Image(
                                bitmap = image,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                            )
                        }
                    }
                }
            }
        }
    }
}
