package com.ssfa.sudoku.minimax

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ssfa.sudoku.minimax.ui.game.GameScreen
import com.ssfa.sudoku.minimax.ui.game.GameViewModel
import com.ssfa.sudoku.minimax.ui.game.MenuScreen
import com.ssfa.sudoku.minimax.ui.theme.AppThemeMode
import com.ssfa.sudoku.minimax.ui.theme.SudokuMinimaxTheme
import com.ssfa.sudoku.minimax.ui.theme.isDarkTheme
import com.ssfa.sudoku.minimax.ui.theme.rememberThemeMode

class MainActivity : ComponentActivity() {
    private val themePrefs by lazy { getSharedPreferences("theme", MODE_PRIVATE) }
    private var gameVm: GameViewModel? = null

    private val timerLifecycleObserver = object : DefaultLifecycleObserver {
        override fun onPause(owner: LifecycleOwner) {
            gameVm?.pauseTimer()
        }
        override fun onResume(owner: LifecycleOwner) {
            gameVm?.resumeTimer()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        lifecycle.addObserver(timerLifecycleObserver)
        setContent {
            val themeMode by rememberThemeMode(themePrefs)
            val isDark = isDarkTheme(themeMode)

            SudokuMinimaxTheme(darkTheme = isDark) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Surface(
                        modifier = Modifier.fillMaxSize().statusBarsPadding(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                    val vm: GameViewModel = viewModel()
                    gameVm = vm
                    val state by vm.state.collectAsState()
                    val showMenu by vm.showMenu.collectAsState()

                    if (showMenu || state == null) {
                        MenuScreen(
                            onStart = { vm.newGame(it) },
                            themeMode = themeMode,
                            onThemeChange = { mode ->
                                themePrefs.edit().putString("theme_mode", mode.name).apply()
                            }
                        )
                    } else {
                        state?.let { s ->
                            GameScreen(
                                state = s,
                                onCellClick = { r, c -> vm.selectCell(r, c) },
                                onNumberInput = { n -> vm.inputNumber(n) },
                                onErase = { vm.eraseCell() },
                                onHint = { vm.giveHint() },
                                onNote = { vm.toggleNote() },
                                onMenu = { vm.goToMenu() },
                                themeMode = themeMode,
                                onThemeChange = { mode ->
                                    themePrefs.edit().putString("theme_mode", mode.name).apply()
                                }
                            )
                        }
                    }
                }
                }
            }
        }
    }
}