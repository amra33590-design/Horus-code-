package com.example.horuscode.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Achievement(
    val id: String,
    val titleEn: String,
    val titleAr: String,
    val descEn: String,
    val descAr: String,
    val iconGlyph: String,
    val xpValue: Int,
    val isUnlocked: Boolean = false
)

data class UserProgress(
    val totalXp: Int = 0,
    val completedLevels: Set<Int> = emptySet(),
    val completedLessons: Set<Int> = emptySet(),
    val solvedDebugging: Set<Int> = emptySet(),
    val solvedMazes: Set<Int> = emptySet(),
    val programsExecutedCount: Int = 0,
    val preTestScore: Int? = null,
    val preTestTotal: Int? = null,
    val postTestScore: Int? = null,
    val postTestTotal: Int? = null,
    val currentStreakDays: Int = 1,
    val unlockedAchievements: Set<String> = emptySet(),
    val isArabic: Boolean = true,
    val isDarkTheme: Boolean = true,
    val hasSeenIntro: Boolean = false,
    val showIntroOnLaunch: Boolean = true
)

class UserProgressManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("horus_code_progress", Context.MODE_PRIVATE)

    private val _progressFlow = MutableStateFlow(loadProgress())
    val progressFlow: StateFlow<UserProgress> = _progressFlow.asStateFlow()

    private fun loadProgress(): UserProgress {
        val totalXp = prefs.getInt("total_xp", 0)
        val completedLevels = prefs.getStringSet("completed_levels", emptySet())?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
        val completedLessons = prefs.getStringSet("completed_lessons", emptySet())?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
        val solvedDebugging = prefs.getStringSet("solved_debugging", emptySet())?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
        val solvedMazes = prefs.getStringSet("solved_mazes", emptySet())?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
        val programsCount = prefs.getInt("programs_count", 0)
        val preScore = if (prefs.contains("pre_score")) prefs.getInt("pre_score", 0) else null
        val preTotal = if (prefs.contains("pre_total")) prefs.getInt("pre_total", 5) else null
        val postScore = if (prefs.contains("post_score")) prefs.getInt("post_score", 0) else null
        val postTotal = if (prefs.contains("post_total")) prefs.getInt("post_total", 5) else null
        val unlockedAch = prefs.getStringSet("unlocked_achievements", emptySet()) ?: emptySet()
        val isArabic = prefs.getBoolean("is_arabic", true)
        val isDarkTheme = prefs.getBoolean("is_dark_theme", true)
        val hasSeenIntro = prefs.getBoolean("has_seen_intro", false)
        val showIntroOnLaunch = prefs.getBoolean("show_intro_on_launch", true)

        return UserProgress(
            totalXp = totalXp,
            completedLevels = completedLevels,
            completedLessons = completedLessons,
            solvedDebugging = solvedDebugging,
            solvedMazes = solvedMazes,
            programsExecutedCount = programsCount,
            preTestScore = preScore,
            preTestTotal = preTotal,
            postTestScore = postScore,
            postTestTotal = postTotal,
            unlockedAchievements = unlockedAch,
            isArabic = isArabic,
            isDarkTheme = isDarkTheme,
            hasSeenIntro = hasSeenIntro,
            showIntroOnLaunch = showIntroOnLaunch
        )
    }

    private fun saveProgress(progress: UserProgress) {
        prefs.edit().apply {
            putInt("total_xp", progress.totalXp)
            putStringSet("completed_levels", progress.completedLevels.map { it.toString() }.toSet())
            putStringSet("completed_lessons", progress.completedLessons.map { it.toString() }.toSet())
            putStringSet("solved_debugging", progress.solvedDebugging.map { it.toString() }.toSet())
            putStringSet("solved_mazes", progress.solvedMazes.map { it.toString() }.toSet())
            putInt("programs_count", progress.programsExecutedCount)
            progress.preTestScore?.let { putInt("pre_score", it) } ?: remove("pre_score")
            progress.preTestTotal?.let { putInt("pre_total", it) } ?: remove("pre_total")
            progress.postTestScore?.let { putInt("post_score", it) } ?: remove("post_score")
            progress.postTestTotal?.let { putInt("post_total", it) } ?: remove("post_total")
            putStringSet("unlocked_achievements", progress.unlockedAchievements)
            putBoolean("is_arabic", progress.isArabic)
            putBoolean("is_dark_theme", progress.isDarkTheme)
            putBoolean("has_seen_intro", progress.hasSeenIntro)
            putBoolean("show_intro_on_launch", progress.showIntroOnLaunch)
            apply()
        }
        _progressFlow.value = progress
    }

    fun markProgramExecuted() {
        val current = _progressFlow.value
        val newCount = current.programsExecutedCount + 1
        val newAch = current.unlockedAchievements.toMutableSet()
        if (newCount >= 1) newAch.add("FIRST_PROGRAM")
        val updated = current.copy(
            programsExecutedCount = newCount,
            totalXp = current.totalXp + 10,
            unlockedAchievements = newAch
        )
        saveProgress(updated)
    }

    fun markLevelCompleted(levelId: Int, xp: Int) {
        val current = _progressFlow.value
        val newCompleted = current.completedLevels + levelId
        val newAch = current.unlockedAchievements.toMutableSet()
        if (levelId >= 3) newAch.add("VARIABLE_MASTER")
        if (levelId >= 4) newAch.add("LOGIC_EXPLORER")
        if (levelId >= 5) newAch.add("LOOP_ARCHITECT")
        if (newCompleted.size >= 8) newAch.add("HORUS_CHAMPION")

        val newXp = if (levelId !in current.completedLevels) current.totalXp + xp else current.totalXp
        val updated = current.copy(
            completedLevels = newCompleted,
            totalXp = newXp,
            unlockedAchievements = newAch
        )
        saveProgress(updated)
    }

    fun markLessonCompleted(lessonId: Int, xp: Int) {
        val current = _progressFlow.value
        val newCompleted = current.completedLessons + lessonId
        val newXp = if (lessonId !in current.completedLessons) current.totalXp + xp else current.totalXp
        val updated = current.copy(
            completedLessons = newCompleted,
            totalXp = newXp
        )
        saveProgress(updated)
    }

    fun markDebuggingSolved(challengeId: Int, xp: Int) {
        val current = _progressFlow.value
        val newCompleted = current.solvedDebugging + challengeId
        val newAch = current.unlockedAchievements.toMutableSet()
        if (newCompleted.size >= 3) newAch.add("DEBUGGING_DETECTIVE")
        val newXp = if (challengeId !in current.solvedDebugging) current.totalXp + xp else current.totalXp
        val updated = current.copy(
            solvedDebugging = newCompleted,
            totalXp = newXp,
            unlockedAchievements = newAch
        )
        saveProgress(updated)
    }

    fun markMazeSolved(mapId: Int, xp: Int) {
        val current = _progressFlow.value
        val newSolved = current.solvedMazes + mapId
        val newAch = current.unlockedAchievements.toMutableSet()
        if (newSolved.size >= 3) newAch.add("TEMPLE_CONQUEROR")
        val newXp = if (mapId !in current.solvedMazes) current.totalXp + xp else current.totalXp
        val updated = current.copy(
            solvedMazes = newSolved,
            totalXp = newXp,
            unlockedAchievements = newAch
        )
        saveProgress(updated)
    }

    fun recordPreTest(score: Int, total: Int) {
        val current = _progressFlow.value
        val updated = current.copy(
            preTestScore = score,
            preTestTotal = total,
            totalXp = current.totalXp + (score * 20)
        )
        saveProgress(updated)
    }

    fun recordPostTest(score: Int, total: Int) {
        val current = _progressFlow.value
        val updated = current.copy(
            postTestScore = score,
            postTestTotal = total,
            totalXp = current.totalXp + (score * 25)
        )
        saveProgress(updated)
    }

    fun setLanguage(isArabic: Boolean) {
        val current = _progressFlow.value
        saveProgress(current.copy(isArabic = isArabic))
    }

    fun setTheme(isDark: Boolean) {
        val current = _progressFlow.value
        saveProgress(current.copy(isDarkTheme = isDark))
    }

    fun markIntroSeen() {
        val current = _progressFlow.value
        saveProgress(current.copy(hasSeenIntro = true))
    }

    fun resetIntroSeen() {
        val current = _progressFlow.value
        saveProgress(current.copy(hasSeenIntro = false))
    }

    fun setShowIntroOnLaunch(show: Boolean) {
        val current = _progressFlow.value
        saveProgress(current.copy(showIntroOnLaunch = show))
    }

    fun resetProgress() {
        prefs.edit().clear().apply()
        _progressFlow.value = UserProgress()
    }

    companion object {
        val allAchievements = listOf(
            Achievement("FIRST_PROGRAM", "First Inscription", "النقش الأول", "Execute your very first program in the studio", "تنفيذ أول برنامج لك في استوديو شفرة حورس", "☥", 50),
            Achievement("VARIABLE_MASTER", "Djed Guardian", "حارس عمود جد", "Master memory allocation and variable mutations", "إتقان تخزين المتغيرات وتعديل قيمها بالذاكرة", "𓋹", 100),
            Achievement("LOGIC_EXPLORER", "Eye of Insight", "عين البصيرة", "Direct programs through conditional branches", "توجيه مسار البرامج باستخدام الشروط المنطقية", "𓂀", 100),
            Achievement("LOOP_ARCHITECT", "Scarab Cycle", "دورة الجعران", "Automate repetitions using loop structures", "أتمتة العمليات التكرارية بواسطة الحلقات البرمجية", "𓆣", 100),
            Achievement("DEBUGGING_DETECTIVE", "Truth Restorer", "مسترد الحقيقة", "Diagnose and fix 3 bugs in the Debugging Lab", "اكتشاف وتصحيح 3 أخطاء في مختبر تصحيح الأخطاء", "𓋖", 150),
            Achievement("TEMPLE_CONQUEROR", "Sanctuary Master", "فاتح المعابد", "Solve all 3 navigation trials in the Temple of Logic", "اجتياز جميع خرائط التنقل الثلاث في معبد المنطق", "𓊗", 150),
            Achievement("HORUS_CHAMPION", "Horus Grand Master", "سيد شفرة حورس", "Complete all 8 core levels of the learning journey", "إتمام جميع المستويات الثمانية في رحلة التعلّم", "👑", 250)
        )
    }
}
