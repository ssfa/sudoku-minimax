package com.ssfa.sudoku.minimax.ui.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.ssfa.sudoku.minimax.ui.theme.AppThemeMode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import com.ssfa.sudoku.minimax.engine.GameState
import kotlin.random.Random

@Composable
fun GameScreen(state: GameState, onCellClick: (Int, Int) -> Unit, onNumberInput: (Int) -> Unit, onErase: () -> Unit, onHint: () -> Unit, onNote: () -> Unit, onMenu: () -> Unit, themeMode: AppThemeMode, onThemeChange: (AppThemeMode) -> Unit) {
    var showCompleted by remember { mutableStateOf(false) }

    LaunchedEffect(state.isCompleted) {
        if (state.isCompleted) showCompleted = true
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.fillMaxSize().padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        // Header: title + menu
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("스도쿠 (MiniMax M2.7)", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            IconButton(onClick = onMenu) { Icon(Icons.Filled.Close, contentDescription = "종료", tint = MaterialTheme.colorScheme.onBackground) }
        }

        // Time + remaining
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            val remaining = state.puzzle.indices.sumOf { r ->
                state.puzzle[r].indices.count { c -> state.puzzle[r][c] == 0 && state.userBoard[r][c] == 0 }
            }
            Text("${remaining}칸 남음", fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
            Text(formatTime(state.elapsedSeconds), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Theme chips
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            ThemeChipInline("라이트", themeMode == AppThemeMode.LIGHT) { onThemeChange(AppThemeMode.LIGHT) }
            ThemeChipInline("다크", themeMode == AppThemeMode.DARK) { onThemeChange(AppThemeMode.DARK) }
            ThemeChipInline("자동", themeMode == AppThemeMode.SYSTEM) { onThemeChange(AppThemeMode.SYSTEM) }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Board
        BoxWithConstraints(modifier = Modifier.fillMaxWidth().aspectRatio(1f).padding(4.dp)) {
            val size = minOf(maxWidth, maxHeight)
            SudokuBoard(state, size, onCellClick)
        }

        // Number pad
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            for (n in 1..9) {
                NumberButton(n, state, onNumberInput)
            }
        }

        // Action buttons: icon + label
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            ActionButtonIcon(Icons.Filled.Clear, "지우기", onErase, MaterialTheme.colorScheme.error)
            ActionButtonIcon(Icons.Filled.Lightbulb, "힌트", onHint, MaterialTheme.colorScheme.tertiary)
            ActionButtonIcon(Icons.Filled.Edit, "메모", onNote, MaterialTheme.colorScheme.secondary)
        }

        Spacer(modifier = Modifier.weight(1f))

        // Version footer
        Text("v0.2.5", fontSize = 11.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.35f), modifier = Modifier.padding(bottom = 4.dp))

        // Confetti
        ConfettiOverlay(showCompleted)
        // Completion dialog
        if (showCompleted) CompletedDialog(time = state.elapsedSeconds, onNewGame = onMenu) }
    }
}

@Composable
private fun SudokuBoard(state: GameState, boardSize: androidx.compose.ui.unit.Dp, onCellClick: (Int, Int) -> Unit) {
    Canvas(modifier = Modifier.size(boardSize)) {
        val cs = size.width / 9
        val thick = cs * 0.045f
        val thin = cs * 0.018f

        // Thin lines
        for (i in 0..9) {
            val pos = i * cs
            drawLine(Color.Gray, Offset(pos, 0f), Offset(pos, size.height), thin)
            drawLine(Color.Gray, Offset(0f, pos), Offset(size.width, pos), thin)
        }
        // Thick 3x3 lines
        for (i in listOf(3, 6)) {
            val pos = i * cs
            drawLine(Color.Black, Offset(pos, 0f), Offset(pos, size.height), thick, StrokeCap.Round)
            drawLine(Color.Black, Offset(0f, pos), Offset(size.width, pos), thick, StrokeCap.Round)
        }
    }

    Column(modifier = Modifier.size(boardSize)) {
        for (row in 0..8) {
            Row(modifier = Modifier.weight(1f)) {
                for (col in 0..8) {
                    val isGiven = state.puzzle[row][col] != 0
                    val isSelected = row == state.selectedRow && col == state.selectedCol
                    val sameRow = row == state.selectedRow
                    val sameCol = col == state.selectedCol
                    val sameBlock = (row / 3) == (state.selectedRow / 3) && (col / 3) == (state.selectedCol / 3)
                    val value = if (state.userBoard[row][col] != 0) state.userBoard[row][col] else null
                    val notes = if (!isGiven && state.notes[row][col].isNotEmpty()) state.notes[row][col] else null

                    Box(modifier = Modifier.weight(1f).aspectRatio(1f)
                        .background(when {
                            isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
                            sameRow || sameCol || sameBlock -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            else -> Color.Transparent
                        })
                        .border(0.3.dp, if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray, RoundedCornerShape(0.dp))
                        .clickable(enabled = !isGiven && !state.isCompleted) { onCellClick(row, col) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (value != null) {
                            Text("$value", fontSize = 18.sp, fontWeight = if (isGiven) FontWeight.Bold else FontWeight.Normal,
                                color = if (isGiven) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.primary)
                        } else if (notes != null) {
                            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize().padding(0.dp)) {
                                for (r in 0..2) {
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        for (c in 0..2) {
                                            val n = r * 3 + c + 1
                                            Text(if (notes.contains(n)) "$n" else " ", fontSize = 7.sp,
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), modifier = Modifier.weight(1f), textAlign = TextAlign.Center, lineHeight = 8.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable private fun NumberButton(n: Int, state: GameState, onNumberInput: (Int) -> Unit) {
    val isErased = state.userBoard.all { row -> row.none { it == n } }
    Button(onClick = { onNumberInput(n) }, modifier = Modifier.size(36.dp), shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = if (isErased) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.secondary),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)) {
        Text("$n", fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable private fun ActionButtonIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit, color: Color) {
    val contentColor = MaterialTheme.colorScheme.onPrimary
    Button(onClick = onClick, modifier = Modifier.height(56.dp).width(90.dp), shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Icon(icon, contentDescription = label, tint = contentColor, modifier = Modifier.size(22.dp))
            Text(label, fontSize = 10.sp, color = contentColor)
        }
    }
}

@Composable private fun ConfettiOverlay(visible: Boolean) {
    if (!visible) return
    val colors = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Magenta, Color.Cyan)
    val particles = remember { List(60) { ConfettiParticle(Random.nextFloat(), Random.nextFloat(), colors.random(), Random.nextFloat() * 0.5f + 0.3f) } }
    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    val anim by infiniteTransition.animateFloat(0f, 1f, infiniteRepeatable(tween(2000, easing = LinearEasing), RepeatMode.Restart), label = "confetti")
    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { p ->
            val x = p.x * size.width
            val y = ((p.y + anim * p.speed) % 1f) * size.height
            drawCircle(p.color, radius = 5f, center = Offset(x, y))
        }
    }
}

private data class ConfettiParticle(val x: Float, val y: Float, val color: Color, val speed: Float)

@Composable private fun CompletedDialog(time: Int, onNewGame: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text("★ 完成!", fontWeight = FontWeight.Bold) },
        text = { Text("스도쿠를 완성했습니다!\n소요 시간: ${formatTime(time)}", style = MaterialTheme.typography.bodyLarge) },
        confirmButton = { Button(onClick = onNewGame) { Text("새 게임") } }
    )
}

@Composable private fun ThemeChipInline(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label, fontSize = 11.sp) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
            selectedLabelColor = MaterialTheme.colorScheme.primary
        ),
        border = null,
        modifier = Modifier.height(28.dp)
    )
}

private fun formatTime(seconds: Int): String {
    val m = seconds / 60; val s = seconds % 60
    return "%02d:%02d".format(m, s)
}