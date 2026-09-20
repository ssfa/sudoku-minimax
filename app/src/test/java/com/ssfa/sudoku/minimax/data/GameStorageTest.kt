package com.ssfa.sudoku.minimax.data

import android.content.SharedPreferences
import com.ssfa.sudoku.minimax.engine.Difficulty
import com.ssfa.sudoku.minimax.engine.GameState
import org.junit.Assert.*
import org.junit.Test

class FakeSharedPreferences : SharedPreferences {
    private val map = mutableMapOf<String, String>()
    private val editors = mutableListOf<SharedPreferences.Editor>()
    override fun getAll(): Map<String, *> = map.toMap() as Map<String, *>
    override fun getString(k: String, def: String?) = map[k] ?: def
    override fun getStringSet(k: String, def: MutableSet<String>?) = def
    override fun getInt(k: String, def: Int) = map[k]?.toIntOrNull() ?: def
    override fun getLong(k: String, def: Long) = map[k]?.toLongOrNull() ?: def
    override fun getFloat(k: String, def: Float) = map[k]?.toFloatOrNull() ?: def
    override fun getBoolean(k: String, def: Boolean) = map[k]?.toBooleanStrictOrNull() ?: def
    override fun contains(k: String) = map.containsKey(k)
    override fun edit(): SharedPreferences.Editor = FakeEditor().also { editors.add(it) }
    override fun registerOnSharedPreferenceChangeListener(l: SharedPreferences.OnSharedPreferenceChangeListener?) {}
    override fun unregisterOnSharedPreferenceChangeListener(l: SharedPreferences.OnSharedPreferenceChangeListener?) {}

    private inner class FakeEditor : SharedPreferences.Editor {
        private val puts = mutableMapOf<String, String>()
        private val removes = mutableSetOf<String>()
        override fun putString(k: String, v: String?) = apply { puts[k] = v ?: ""; removes.remove(k) }
        override fun putStringSet(k: String, v: MutableSet<String>?) = apply {}
        override fun putInt(k: String, v: Int) = apply { puts[k] = v.toString(); removes.remove(k) }
        override fun putLong(k: String, v: Long) = apply { puts[k] = v.toString(); removes.remove(k) }
        override fun putFloat(k: String, v: Float) = apply { puts[k] = v.toString(); removes.remove(k) }
        override fun putBoolean(k: String, v: Boolean) = apply { puts[k] = v.toString(); removes.remove(k) }
        override fun remove(k: String) = apply { removes.add(k) }
        override fun clear() = apply { puts.clear(); removes.clear() }
        override fun commit() = run { removes.forEach { map.remove(it) }; map.putAll(puts); true }
        override fun apply() { removes.forEach { map.remove(it) }; map.putAll(puts) }
    }
}

class GameStorageTest {
    private fun makeState() = GameState(
        puzzle = Array(9) { r -> IntArray(9) { c -> if (r == 0 && c == 0) 5 else 0 } },
        solution = Array(9) { r -> IntArray(9) { c -> ((r * 3 + r / 3 + c) % 9) + 1 } },
        userBoard = Array(9) { r -> IntArray(9) { c -> if (r == 0 && c == 0) 5 else 0 } },
        difficulty = Difficulty.EASY,
        elapsedSeconds = 42,
        isTimerPaused = false
    )

    @Test
    fun saveAndLoad_roundTrips() {
        val prefs = FakeSharedPreferences()
        val state = makeState()
        GameStorage.save(prefs, state)
        val loaded = GameStorage.load(prefs)
        assertNotNull(loaded)
        assertEquals(42, loaded!!.elapsedSeconds)
        assertEquals(Difficulty.EASY, loaded.difficulty)
        assertFalse(loaded.isCompleted)
        assertEquals(5, loaded.userBoard[0][0])
    }

    @Test
    fun load_returnsNullWhenEmpty() {
        val prefs = FakeSharedPreferences()
        assertNull(GameStorage.load(prefs))
    }

    @Test
    fun clear_removesData() {
        val prefs = FakeSharedPreferences()
        GameStorage.save(prefs, makeState())
        GameStorage.clear(prefs)
        assertNull(GameStorage.load(prefs))
    }
}