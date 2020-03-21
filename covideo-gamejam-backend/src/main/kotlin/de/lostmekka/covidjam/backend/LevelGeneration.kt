package de.lostmekka.covidjam.backend

import de.lostmekka.covidjam.backend.levelgen.FloorGenerator
import de.lostmekka.covidjam.backend.levelgen.MutableLevel
import de.lostmekka.covidjam.backend.levelgen.Rect
import de.lostmekka.covidjam.backend.levelgen.RoomGenerator

data class LevelGenerationInput(
    val levelSize: Dimension
)

data class LevelGenerationOutput(
    val tiles: List<Tile>,
    val entities: List<Entity>,
    val entrancePositions: List<Point>
)

data class Dimension(val w: Int, val h: Int)
data class Point(val x: Int, val y: Int)

data class Tile(
    val pos: Point,
    val type: Type
) {
    enum class Type { Floor, Wall, Door }
}

data class Entity(
    val pos: Point,
    val type: Type
) {
    enum class Type {
        ShelveSmall1, ShelveSmall2, ShelveSmall3,
        ShelveTall1, ShelveTall2, ShelveTall3,
        PaperRollScrappy, PaperRollBad, PaperRollGood, PaperRollPrint, PaperRollScented
    }
}

object LevelGeneration : BackendEndpoint<LevelGenerationInput, LevelGenerationOutput> {
    override val path = "level/generate"

    override fun handleRequest(input: LevelGenerationInput): LevelGenerationOutput {
        val rect = Rect(0, 0, input.levelSize.w, input.levelSize.h)
        val generator = createGenerator()
        val level = MutableLevel()
        generator.generate(rect, level)
        return LevelGenerationOutput(
            tiles = level.tiles,
            entities = level.entities,
            entrancePositions = listOf()
        )
    }

    private fun createGenerator() =
        RoomGenerator(
            inner = FloorGenerator()
        )
}
