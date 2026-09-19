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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssfa.sudoku.minimax.engine.GameState

@Composable
fun GameScreen(state: GameState, onCellClick: (Int, Int) -> Unit, onNumberInput: (Int) -> Unit, onErase: () -> Unit, onHint: () -> Unit, onNote: () -> Unit, onPause: () -> Unit, onMenu: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        // Timer bar
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onMenu) { Text("☰", fontSize = 20.sp) }
            Text(formatTime(state.elapsedSeconds), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            IconButton(onClick = onPause) { Text(if (state.isTimerPaused) "▶" else "⏸", fontSize = 18.sp) }
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

        // Action buttons
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            ActionButton("⌫", onErase, MaterialTheme.colorScheme.error)
            ActionButton("💡", onHint, MaterialTheme.colorScheme.tertiary)
            ActionButton("📝", onNote, MaterialTheme.colorScheme.secondary)
        }

        Spacer(modifier = Modifier.weight(1f))

        // Version footer
        Text("v0.1.0", fontSize = 11.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.35f), modifier = Modifier.padding(bottom = 4.dp))
    }
}

@Composable
private fun SudokuBoard(state: GameState, boardSize: androidx.compose.ui.unit.Dp, onCellClick: (Int, Int) -> Unit) {
    val cellSize = boardSize / 9

    Canvas(modifier = Modifier.size(boardSize)) {
        val cs = boardSize.toPx() / 9
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
                            Text("$value", fontSize = (cellSize.toPx() * 0.5f / density).sp, fontWeight = if (isGiven) FontWeight.Bold else FontWeight.Normal,
                                color = if (isGiven) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.primary)
                        } else if (notes != null) {
                            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(1.dp)) {
                                for (r in 0..2) {
                                    Row {
                                        for (c in 0..2) {
                                            val n = r * 3 + c + 1
                                            Text(if (notes.contains(n)) "$n" else " ", fontSize = (cellSize.toPx() * 0.17f / density).sp,
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
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

@Composable private fun ActionButton(emoji: String, onClick: () -> Unit, color: Color) {
    Button(onClick = onClick, modifier = Modifier.height(44.dp).width(80.dp), shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)) {
        Text(emoji, fontSize = 18.sp)
    }
}

private fun formatTime(seconds: Int): String {
    val m = seconds / 60; val s = seconds % 60
    return "%02d:%02d".format(m, s)
}