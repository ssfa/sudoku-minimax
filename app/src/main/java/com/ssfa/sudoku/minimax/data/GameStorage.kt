package com.ssfa.sudoku.minimax.data

import android.content.Context
import android.content.SharedPreferences
import com.ssfa.sudoku.minimax.engine.Difficulty
import com.ssfa.sudoku.minimax.engine.GameState
import org.json.JSONArray
import org.json.JSONObject

object GameStorage {
    private const val KEY = "sudoku_game"

    fun save(prefs: SharedPreferences, state: GameState) {
        val json = JSONObject().apply {
            put("difficulty", state.difficulty.name)
            put("isCompleted", state.isCompleted)
            put("elapsedSeconds", state.elapsedSeconds)
            put("isTimerPaused", state.isTimerPaused)
            put("selectedRow", state.selectedRow)
            put("selectedCol", state.selectedCol)
            put("puzzle", intArrayToJson(state.puzzle))
            put("solution", intArrayToJson(state.solution))
            put("userBoard", intArrayToJson(state.userBoard))
            put("notes", notesToJson(state.notes))
        }
        prefs.edit().putString(KEY, json.toString()).apply()
    }

    fun load(prefs: SharedPreferences): GameState? {
        val json = prefs.getString(KEY, null) ?: return null
        return try {
            val obj = JSONObject(json)
            GameState(
                puzzle = jsonToIntArray(obj.getJSONArray("puzzle")),
                solution = jsonToIntArray(obj.getJSONArray("solution")),
                userBoard = jsonToIntArray(obj.getJSONArray("userBoard")),
                selectedRow = obj.optInt("selectedRow", -1),
                selectedCol = obj.optInt("selectedCol", -1),
                difficulty = Difficulty.valueOf(obj.getString("difficulty")),
                isCompleted = obj.optBoolean("isCompleted", false),
                notes = jsonToNotes(obj.getJSONArray("notes")),
                elapsedSeconds = obj.optInt("elapsedSeconds", 0),
                isTimerPaused = obj.optBoolean("isTimerPaused", false)
            )
        } catch (e: Exception) { null }
    }

    fun clear(prefs: SharedPreferences) = prefs.edit().remove(KEY).apply()

    private fun intArrayToJson(arr: Array<IntArray>) = JSONArray().apply {
        for (row in arr) put(JSONArray(row.toList()))
    }

    private fun jsonToIntArray(json: JSONArray): Array<IntArray> = Array(9) { r ->
        val row = json.getJSONArray(r)
        IntArray(9) { c -> row.getInt(c) }
    }

    private fun notesToJson(notes: Array<Array<Set<Int>>>) = JSONArray().apply {
        for (r in 0..8) put(JSONArray().apply {
            for (c in 0..8) put(JSONArray(notes[r][c].toList()))
        })
    }

    private fun jsonToNotes(json: JSONArray): Array<Array<Set<Int>>> = Array(9) { r ->
        val row = json.getJSONArray(r)
        Array(9) { c ->
            val arr = row.getJSONArray(c)
            (0 until arr.length()).mapTo(mutableSetOf()) { arr.getInt(it) }
        }
    }
}