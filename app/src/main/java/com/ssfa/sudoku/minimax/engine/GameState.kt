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
) {
    fun copy(
        puzzle: Array<IntArray> = this.puzzle,
        solution: Array<IntArray> = this.solution,
        userBoard: Array<IntArray> = this.userBoard,
        selectedRow: Int = this.selectedRow,
        selectedCol: Int = this.selectedCol,
        difficulty: Difficulty = this.difficulty,
        isCompleted: Boolean = this.isCompleted,
        notes: Array<Array<Set<Int>>> = this.notes,
        elapsedSeconds: Int = this.elapsedSeconds,
        isTimerPaused: Boolean = this.isTimerPaused
    ) = GameState(puzzle, solution, userBoard, selectedRow, selectedCol, difficulty, isCompleted, notes, elapsedSeconds, isTimerPaused)

    companion object {
        fun new(difficulty: Difficulty) = GameState(
            puzzle = SudokuGenerator.generate(difficulty),
            solution = SudokuGenerator.getSolution(SudokuGenerator.generate(difficulty)),
            userBoard = Array(9) { IntArray(9) },
            difficulty = difficulty
        )
    }
}