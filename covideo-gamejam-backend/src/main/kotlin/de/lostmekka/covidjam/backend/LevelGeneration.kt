package de.lostmekka.covidjam.backend

import de.lostmekka.covidjam.backend.levelgen.MutableLevel
import de.lostmekka.covidjam.backend.levelgen.Rect
import de.lostmekka.covidjam.backend.levelgen.generator.borderGenerator
import de.lostmekka.covidjam.backend.levelgen.generator.floorGenerator
import de.lostmekka.covidjam.backend.levelgen.generator.roomGenerator
import de.lostmekka.covidjam.backend.levelgen.generator.shelveAreaGenerator
import de.lostmekka.covidjam.backend.levelgen.printDebug
import kotlin.random.Random

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

    private fun createGenerator() =
        roomGenerator(
            inner = borderGenerator(
                innerGenerator = shelveAreaGenerator(
                    horizontal = Random.nextBoolean(),
                    corridorWidth = (1..3).random(),
                    doubleShelveColumn = Random.nextBoolean(),
                    useTallShelves = Random.nextBoolean()
                ),
                borderGenerator = floorGenerator()
            )
        )

    override fun handleRequest(input: LevelGenerationInput): LevelGenerationOutput {
        val bounds = Rect(0, 0, input.levelSize.w, input.levelSize.h)
        val generator = createGenerator()
        val level = MutableLevel(bounds)
        generator.generate(bounds, level)
        if (IS_RUNNING_IN_TEST) level.printDebug()
        return LevelGenerationOutput(
            tiles = level.tiles,
            entities = level.entities,
            entrancePositions = listOf()
        )
    }
}
