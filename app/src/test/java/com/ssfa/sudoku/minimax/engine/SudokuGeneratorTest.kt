package com.ssfa.sudoku.minimax.engine

import org.junit.Assert.*
import org.junit.Test

class SudokuGeneratorTest {
    @Test fun generateEasy_hasSolution() {
        val board = SudokuGenerator.generate(Difficulty.EASY)
        assertEquals(9, board.size)
        assertEquals(9, board[0].size)
        // Should have empty cells
        val empties = board.sumOf { row -> row.count { it == 0 } }
        assertTrue("Expected ~35 holes, got $empties", empties in 30..45)
    }

    @Test fun generateMedium_hasSolution() {
        val board = SudokuGenerator.generate(Difficulty.MEDIUM)
        val empties = board.sumOf { row -> row.count { it == 0 } }
        assertTrue("Expected ~45 holes, got $empties", empties in 40..50)
    }

    @Test fun generateHard_hasSolution() {
        val board = SudokuGenerator.generate(Difficulty.HARD)
        val empties = board.sumOf { row -> row.count { it == 0 } }
        assertTrue("Expected ~55 holes, got $empties", empties in 50..60)
    }

    @Test fun isValidPlacement_rejectsDuplicateInRow() {
        val board = Array(9) { IntArray(9) }
        board[0][0] = 5
        assertFalse(SudokuGenerator.isValidPlacement(board, 0, 1, 5))
    }

    @Test fun isValidPlacement_rejectsDuplicateInCol() {
        val board = Array(9) { IntArray(9) }
        board[0][0] = 3
        assertFalse(SudokuGenerator.isValidPlacement(board, 1, 0, 3))
    }

    @Test fun isValidPlacement_rejectsDuplicateInBlock() {
        val board = Array(9) { IntArray(9) }
        board[0][0] = 7
        assertFalse(SudokuGenerator.isValidPlacement(board, 1, 1, 7))
    }

    @Test fun isValidPlacement_acceptsValidNumber() {
        val board = Array(9) { IntArray(9) }
        assertTrue(SudokuGenerator.isValidPlacement(board, 0, 0, 5))
    }

    @Test fun isComplete_detectsFullValidBoard() {
        // solved board (every row/col/block valid)
        val solved = Array(9) { r ->
            IntArray(9) { c ->
                ((r * 3 + r / 3 + c) % 9) + 1
            }
        }
        assertTrue(SudokuGenerator.isComplete(solved))
    }

    @Test fun isComplete_detectsIncompleteBoard() {
        val board = Array(9) { IntArray(9) }
        assertFalse(SudokuGenerator.isComplete(board))
    }

    @Test fun getSolution_returnsSolvedBoard() {
        val board = Array(9) { IntArray(9) { 0 } }
        val solution = SudokuGenerator.getSolution(board)
        assertTrue(SudokuGenerator.isComplete(solution))
    }
}