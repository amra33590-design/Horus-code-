package com.example.horuscode.data

import com.example.horuscode.engine.model.BlockType
import com.example.horuscode.engine.model.HorusBlock

enum class Direction(val angle: Float, val dx: Int, val dy: Int) {
    NORTH(0f, 0, -1),
    EAST(90f, 1, 0),
    SOUTH(180f, 0, 1),
    WEST(270f, -1, 0);

    fun turnLeft(): Direction = when (this) {
        NORTH -> WEST
        WEST -> SOUTH
        SOUTH -> EAST
        EAST -> NORTH
    }

    fun turnRight(): Direction = when (this) {
        NORTH -> EAST
        EAST -> SOUTH
        SOUTH -> WEST
        WEST -> NORTH
    }
}

enum class TileType {
    EMPTY,
    WALL,
    START,
    GOAL,
    KEY
}

data class MazeState(
    val playerX: Int,
    val playerY: Int,
    val direction: Direction,
    val collectedKeys: Set<Pair<Int, Int>> = emptySet(),
    val isWin: Boolean = false,
    val isFailed: Boolean = false,
    val failReasonEn: String? = null,
    val failReasonAr: String? = null
)

data class MazeMap(
    val id: Int,
    val nameEn: String,
    val nameAr: String,
    val subtitleEn: String,
    val subtitleAr: String,
    val width: Int,
    val height: Int,
    val startX: Int,
    val startY: Int,
    val startDir: Direction,
    val goalX: Int,
    val goalY: Int,
    val walls: Set<Pair<Int, Int>>,
    val keys: Set<Pair<Int, Int>> = emptySet(),
    val starterBlocks: List<HorusBlock>,
    val solutionBlocks: List<HorusBlock>,
    val descriptionEn: String,
    val descriptionAr: String
)

object TempleMazeRepository {

    val maps: List<MazeMap> = listOf(
        // Map 1: The Gateway to Abydos (5x5)
        MazeMap(
            id = 1,
            nameEn = "The Gateway to Abydos",
            nameAr = "بوابة أبيدوس",
            subtitleEn = "Sequential Navigation",
            subtitleAr = "التنقل التسلسلي",
            width = 5,
            height = 5,
            startX = 0,
            startY = 2,
            startDir = Direction.EAST,
            goalX = 4,
            goalY = 0,
            walls = setOf(
                0 to 0, 0 to 1, 0 to 3, 0 to 4,
                1 to 0, 1 to 1, 1 to 3, 1 to 4,
                2 to 3, 2 to 4,
                3 to 3, 3 to 4,
                4 to 3, 4 to 4
            ),
            starterBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "MOVE_FORWARD")),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "MOVE_FORWARD")),
                HorusBlock(type = BlockType.STOP)
            ),
            solutionBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "MOVE_FORWARD")),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "MOVE_FORWARD")),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "TURN_LEFT")),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "MOVE_FORWARD")),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "MOVE_FORWARD")),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "TURN_RIGHT")),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "MOVE_FORWARD")),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "MOVE_FORWARD")),
                HorusBlock(type = BlockType.STOP)
            ),
            descriptionEn = "Move forward along the stone causeway, turn left into the courtyard, and reach the Golden Shrine.",
            descriptionAr = "تقدم عبر الممر الحجري، استدر لليسار نحو الفناء، ثم توجه للوصول إلى المحراب الذهبي."
        ),

        // Map 2: The Hall of Columns (6x6)
        MazeMap(
            id = 2,
            nameEn = "The Hall of Columns",
            nameAr = "بهو الأعمدة",
            subtitleEn = "Repetition & Loops",
            subtitleAr = "التكرار والحلقات",
            width = 6,
            height = 6,
            startX = 0,
            startY = 5,
            startDir = Direction.NORTH,
            goalX = 5,
            goalY = 0,
            walls = setOf(
                1 to 1, 1 to 2, 1 to 3, 1 to 4,
                3 to 1, 3 to 2, 3 to 3, 3 to 4
            ),
            starterBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.REPEAT, params = mapOf("times" to "5")),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "MOVE_FORWARD")),
                HorusBlock(type = BlockType.END_REPEAT),
                HorusBlock(type = BlockType.STOP)
            ),
            solutionBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.REPEAT, params = mapOf("times" to "5")),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "MOVE_FORWARD")),
                HorusBlock(type = BlockType.END_REPEAT),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "TURN_RIGHT")),
                HorusBlock(type = BlockType.REPEAT, params = mapOf("times" to "5")),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "MOVE_FORWARD")),
                HorusBlock(type = BlockType.END_REPEAT),
                HorusBlock(type = BlockType.STOP)
            ),
            descriptionEn = "Use the Scarab 𓆣 loop to stride across the vast Hypostyle hall without writing duplicate commands.",
            descriptionAr = "استخدم حلقة الجعران 𓆣 للتقدم عبر بهو الأعمدة الشاسع دون كتابة أوامر مكررة يدوياً."
        ),

        // Map 3: The Labyrinth of Karnak (7x7)
        MazeMap(
            id = 3,
            nameEn = "The Labyrinth of Karnak",
            nameAr = "متاهة الكرنك",
            subtitleEn = "Key Collection & Mastery",
            subtitleAr = "جمع المفتاح والبراعة التامة",
            width = 7,
            height = 7,
            startX = 0,
            startY = 6,
            startDir = Direction.NORTH,
            goalX = 6,
            goalY = 0,
            walls = setOf(
                1 to 1, 1 to 2, 1 to 3, 1 to 5, 1 to 6,
                2 to 3,
                3 to 0, 3 to 1, 3 to 3, 3 to 5, 3 to 6,
                4 to 3,
                5 to 1, 5 to 2, 5 to 3, 5 to 4, 5 to 5
            ),
            keys = setOf(2 to 0),
            starterBlocks = listOf(
                HorusBlock(type = BlockType.START),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "MOVE_FORWARD")),
                HorusBlock(type = BlockType.STOP)
            ),
            solutionBlocks = listOf(
                HorusBlock(type = BlockType.START),
                // Walk up along corridor
                HorusBlock(type = BlockType.REPEAT, params = mapOf("times" to "6")),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "MOVE_FORWARD")),
                HorusBlock(type = BlockType.END_REPEAT),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "TURN_RIGHT")),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "MOVE_FORWARD")),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "MOVE_FORWARD")),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "COLLECT_KEY")),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "TURN_RIGHT")),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "MOVE_FORWARD")),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "MOVE_FORWARD")),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "TURN_LEFT")),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "MOVE_FORWARD")),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "MOVE_FORWARD")),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "TURN_LEFT")),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "MOVE_FORWARD")),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "MOVE_FORWARD")),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "TURN_RIGHT")),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "MOVE_FORWARD")),
                HorusBlock(type = BlockType.ACTION, params = mapOf("action" to "MOVE_FORWARD")),
                HorusBlock(type = BlockType.STOP)
            ),
            descriptionEn = "Collect the sacred Golden Ankh Key at (2,0) before unlocking the Inner Sanctuary of Karnak at (6,0)!",
            descriptionAr = "التقط مفتاح العنخ الذهبي المقدس في الموقع (2,0) لفتح أبواب قدس أقداس الكرنك في (6,0)!"
        )
    )

    fun simulateStep(current: MazeState, action: String, map: MazeMap): MazeState {
        if (current.isWin || current.isFailed) return current

        var x = current.playerX
        var y = current.playerY
        var dir = current.direction
        val keys = current.collectedKeys.toMutableSet()

        when (action) {
            "MOVE_FORWARD" -> {
                val nextX = x + dir.dx
                val nextY = y + dir.dy

                // Boundary check
                if (nextX !in 0 until map.width || nextY !in 0 until map.height) {
                    return current.copy(
                        isFailed = true,
                        failReasonEn = "Explorer hit the perimeter wall of the temple!",
                        failReasonAr = "اصطدم المستكشف بجدار المعبد الخارجي!"
                    )
                }

                // Wall obstacle check
                if (map.walls.contains(nextX to nextY)) {
                    return current.copy(
                        isFailed = true,
                        failReasonEn = "Explorer collided with a sandstone column at ($nextX, $nextY)!",
                        failReasonAr = "اصطدم المستكشف بعمود حجري عند الإحداثيات ($nextX, $nextY)!"
                    )
                }

                x = nextX
                y = nextY
            }

            "TURN_LEFT" -> {
                dir = dir.turnLeft()
            }

            "TURN_RIGHT" -> {
                dir = dir.turnRight()
            }

            "COLLECT_KEY" -> {
                if (map.keys.contains(x to y)) {
                    keys.add(x to y)
                }
            }
        }

        // Auto collect key if stepping on it
        if (map.keys.contains(x to y)) {
            keys.add(x to y)
        }

        // Goal reached check
        val reachedGoal = (x == map.goalX && y == map.goalY)
        val hasAllKeys = (map.keys.isEmpty() || keys.containsAll(map.keys))

        if (reachedGoal) {
            if (hasAllKeys) {
                return MazeState(x, y, dir, keys, isWin = true)
            } else {
                return MazeState(
                    x, y, dir, keys,
                    isFailed = true,
                    failReasonEn = "You reached the sanctuary, but the gates are sealed without the Golden Key!",
                    failReasonAr = "وصلت إلى قدس الأقداس لكن البوابة مغلقة لعدم حيازة المفتاح الذهبي!"
                )
            }
        }

        return MazeState(x, y, dir, keys)
    }
}
