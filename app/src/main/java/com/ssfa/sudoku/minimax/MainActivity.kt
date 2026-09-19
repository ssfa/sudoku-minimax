package com.ssfa.sudoku.minimax

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ssfa.sudoku.minimax.ui.game.GameScreen
import com.ssfa.sudoku.minimax.ui.game.GameViewModel
import com.ssfa.sudoku.minimax.ui.game.MenuScreen
import com.ssfa.sudoku.minimax.ui.theme.SudokuMinimaxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SudokuMinimaxTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val vm: GameViewModel = viewModel()
                    val state by vm.state.collectAsState()
                    val showMenu by vm.showMenu.collectAsState()

                    if (showMenu || state == null) {
                        MenuScreen { vm.newGame(it) }
                    } else {
                        state?.let { s ->
                            GameScreen(
                                state = s,
                                onCellClick = { r, c -> vm.selectCell(r, c) },
                                onNumberInput = { n -> vm.inputNumber(n) },
                                onErase = { vm.eraseCell() },
                                onHint = { vm.giveHint() },
                                onNote = { vm.toggleNote() },
                                onPause = { vm.toggleTimer() },
                                onMenu = { vm.goToMenu() }
                            )
                        }
                    }
                }
            }
        }
    }
}