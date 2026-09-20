package com.ssfa.sudoku.minimax.ui.game

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ssfa.sudoku.minimax.data.GameStorage
import com.ssfa.sudoku.minimax.engine.Difficulty
import com.ssfa.sudoku.minimax.engine.GameState
import com.ssfa.sudoku.minimax.engine.SudokuGenerator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs: SharedPreferences by lazy { getApplication<Application>().getSharedPreferences("sudoku", Context.MODE_PRIVATE) }

    private val _state = MutableStateFlow<GameState?>(null)
    val state: StateFlow<GameState?> = _state

    private val _showMenu = MutableStateFlow(true)
    val showMenu: StateFlow<Boolean> = _showMenu

    private var timerJob: Job? = null

    init { loadGame() }

    fun newGame(difficulty: Difficulty) {
        timerJob?.cancel()
        val puzzle = SudokuGenerator.generate(difficulty)
        val solution = SudokuGenerator.getSolution(puzzle)
        val state = GameState(
            puzzle = puzzle,
            solution = solution,
            userBoard = Array(9) { puzzle[it].copyOf() },
            difficulty = difficulty
        )
        _state.value = state
        _showMenu.value = false
        saveGame()
        startTimer()
    }

    fun selectCell(row: Int, col: Int) {
        val s = _state.value ?: return
        if (s.isCompleted) return
        if (s.puzzle[row][col] != 0) return
        _state.value = s.copy(selectedRow = row, selectedCol = col)
        saveGame()
    }

    fun inputNumber(num: Int) {
        val s = _state.value ?: return
        if (s.selectedRow < 0 || s.isCompleted) return
        if (s.puzzle[s.selectedRow][s.selectedCol] != 0) return
        val newBoard = s.userBoard.map { it.copyOf() }.toTypedArray()
        newBoard[s.selectedRow][s.selectedCol] = num
        val completed = SudokuGenerator.isComplete(newBoard)
        _state.value = s.copy(userBoard = newBoard, isCompleted = completed)
        saveGame()
        if (completed) timerJob?.cancel()
    }

    fun eraseCell() {
        val s = _state.value ?: return
        if (s.selectedRow < 0 || s.isCompleted) return
        if (s.puzzle[s.selectedRow][s.selectedCol] != 0) return
        val newBoard = s.userBoard.map { it.copyOf() }.toTypedArray()
        newBoard[s.selectedRow][s.selectedCol] = 0
        _state.value = s.copy(userBoard = newBoard)
        saveGame()
    }

    fun toggleNote() {
        val s = _state.value ?: return
        if (s.selectedRow < 0 || s.isCompleted) return
        if (s.puzzle[s.selectedRow][s.selectedCol] != 0) return
        // Cycle through numbers 1-9 for note
        val current = s.userBoard[s.selectedRow][s.selectedCol]
        val newNotes = Array(9) { r -> Array(9) { c -> s.notes[r][c].toMutableSet() } }
        val set = newNotes[s.selectedRow][s.selectedCol]
        val next = if (set.isEmpty()) 1 else {
            val max = set.maxOrNull() ?: 1
            if (max >= 9) { set.clear(); 1 } else max + 1
        }
        set.clear(); set.add(next)
        val notesArray = Array(9) { r -> Array(9) { c -> newNotes[r][c].toSet() } }
        _state.value = s.copy(notes = notesArray)
        saveGame()
    }

    fun giveHint() {
        val s = _state.value ?: return
        if (s.selectedRow < 0 || s.isCompleted) return
        if (s.puzzle[s.selectedRow][s.selectedCol] != 0) return
        val newBoard = s.userBoard.map { it.copyOf() }.toTypedArray()
        newBoard[s.selectedRow][s.selectedCol] = s.solution[s.selectedRow][s.selectedCol]
        val completed = SudokuGenerator.isComplete(newBoard)
        _state.value = s.copy(userBoard = newBoard, isCompleted = completed)
        saveGame()
        if (completed) timerJob?.cancel()
    }

    fun toggleTimer() {
        val s = _state.value ?: return
        _state.value = s.copy(isTimerPaused = !s.isTimerPaused)
        saveGame()
    }

    fun goToMenu() {
        timerJob?.cancel()
        _state.value = null
        _showMenu.value = true
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val s = _state.value ?: break
                if (!s.isTimerPaused && !s.isCompleted) {
                    _state.value = s.copy(elapsedSeconds = s.elapsedSeconds + 1)
                    if (_state.value!!.elapsedSeconds % 5 == 0) saveGame()
                }
            }
        }
    }

    private fun loadGame() {
        val saved = GameStorage.load(prefs)
        if (saved != null && !saved.isCompleted) {
            _state.value = saved
            _showMenu.value = false
            startTimer()
        }
    }

    private fun saveGame() {
        val s = _state.value ?: return
        GameStorage.save(prefs, s)
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        saveGame()
    }
}