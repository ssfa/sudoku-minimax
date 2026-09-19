package com.ssfa.sudoku.minimax.engine

import kotlin.random.Random

enum class Difficulty { EASY, MEDIUM, HARD }

object SudokuGenerator {
    private val EMPTY = 0

    fun generate(difficulty: Difficulty): Array<IntArray> {
        val solution = generateSolution()
        val puzzle = copyBoard(solution)
        val holes = when (difficulty) {
            Difficulty.EASY -> 35
            Difficulty.MEDIUM -> 45
            Difficulty.HARD -> 55
        }
        carveHoles(puzzle, holes)
        return puzzle
    }

    fun getSolution(board: Array<IntArray>): Array<IntArray> {
        val solution = copyBoard(board)
        solve(solution)
        return solution
    }

    fun isComplete(board: Array<IntArray>): Boolean {
        for (r in 0..8) for (c in 0..8) {
            if (board[r][c] == EMPTY) return false
        }
        return isValidBoard(board)
    }

    fun isValidPlacement(board: Array<IntArray>, row: Int, col: Int, num: Int): Boolean {
        if (board[row][col] != EMPTY) return false
        for (i in 0..8) {
            if (board[row][i] == num) return false
            if (board[i][col] == num) return false
        }
        val br = (row / 3) * 3
        val bc = (col / 3) * 3
        for (r in br..br + 2) for (c in bc..bc + 2) {
            if (board[r][c] == num) return false
        }
        return true
    }

    // --- private ---

    private fun generateSolution(): Array<IntArray> {
        val board = Array(9) { IntArray(9) }
        fillBoard(board)
        return board
    }

    private fun fillBoard(board: Array<IntArray>): Boolean {
        for (r in 0..8) {
            for (c in 0..8) {
                if (board[r][c] == EMPTY) {
                    val nums = (1..9).shuffled()
                    for (n in nums) {
                        if (isValid(board, r, c, n)) {
                            board[r][c] = n
                            if (fillBoard(board)) return true
                            board[r][c] = EMPTY
                        }
                    }
                    return false
                }
            }
        }
        return true
    }

    private fun carveHoles(board: Array<IntArray>, count: Int) {
        val positions = mutableListOf<Pair<Int, Int>>()
        for (r in 0..8) for (c in 0..8) positions.add(r to c)
        positions.shuffle()
        var removed = 0
        for ((r, c) in positions) {
            if (removed >= count) break
            board[r][c] = EMPTY
            removed++
        }
    }

    private fun solve(board: Array<IntArray>): Boolean {
        for (r in 0..8) {
            for (c in 0..8) {
                if (board[r][c] == EMPTY) {
                    for (n in 1..9) {
                        if (isValid(board, r, c, n)) {
                            board[r][c] = n
                            if (solve(board)) return true
                            board[r][c] = EMPTY
                        }
                    }
                    return false
                }
            }
        }
        return true
    }

    private fun isValid(board: Array<IntArray>, row: Int, col: Int, num: Int): Boolean {
        for (i in 0..8) {
            if (board[row][i] == num) return false
            if (board[i][col] == num) return false
        }
        val br = (row / 3) * 3
        val bc = (col / 3) * 3
        for (r in br..br + 2) for (c in bc..bc + 2) {
            if (board[r][c] == num) return false
        }
        return true
    }

    private fun isValidBoard(board: Array<IntArray>): Boolean {
        for (r in 0..8) {
            val seen = BooleanArray(10)
            for (c in 0..8) { val n = board[r][c]; if (n != EMPTY) { if (seen[n]) return false; seen[n] = true } }
        }
        for (c in 0..8) {
            val seen = BooleanArray(10)
            for (r in 0..8) { val n = board[r][c]; if (n != EMPTY) { if (seen[n]) return false; seen[n] = true } }
        }
        for (br in 0..2) for (bc in 0..2) {
            val seen = BooleanArray(10)
            for (r in br * 3..br * 3 + 2) for (c in bc * 3..bc * 3 + 2) {
                val n = board[r][c]; if (n != EMPTY) { if (seen[n]) return false; seen[n] = true }
            }
        }
        return true
    }

    private fun copyBoard(board: Array<IntArray>) = Array(9) { board[it].copyOf() }
}