package com.ssfa.sudoku.minimax.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssfa.sudoku.minimax.engine.Difficulty

@Composable
fun MenuScreen(onStart: (Difficulty) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "SUDOKU",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "MINIMAX",
            fontSize = 24.sp,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(64.dp))

        Text("난이도 선택", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(24.dp))

        DifficultyButton("하 (초급)", Difficulty.EASY, MaterialTheme.colorScheme.primary, onStart)
        Spacer(modifier = Modifier.height(12.dp))
        DifficultyButton("중 (중급)", Difficulty.MEDIUM, MaterialTheme.colorScheme.secondary, onStart)
        Spacer(modifier = Modifier.height(12.dp))
        DifficultyButton("상 (고급)", Difficulty.HARD, MaterialTheme.colorScheme.tertiary, onStart)
    }
}

@Composable
private fun DifficultyButton(label: String, diff: Difficulty, color: androidx.compose.ui.graphics.Color, onStart: (Difficulty) -> Unit) {
    Button(
        onClick = { onStart(diff) },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp)),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(label, fontSize = 18.sp, fontWeight = FontWeight.Medium)
    }
}