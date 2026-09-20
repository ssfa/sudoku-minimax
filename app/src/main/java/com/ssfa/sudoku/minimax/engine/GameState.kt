package com.ssfa.sudoku.minimax.engine

data class GameState(
    val puzzle: Array<IntArray>,
    val solution: Array<IntArray>,
    val userBoard: Array<IntArray>,
    val selectedRow: Int = -1,
    val selectedCol: Int = -1,
    val difficulty: Difficulty = Difficulty.EASY,
    val isCompleted: Boolean = false,
    val notes: Array<Array<Set<Int>>> = Array(9) { Array(9) { emptySet() } },
    val elapsedSeconds: Int = 0,
    val isTimerPaused: Boolean = false
)