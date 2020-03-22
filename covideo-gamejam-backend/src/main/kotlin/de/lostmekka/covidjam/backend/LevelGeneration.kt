package de.lostmekka.covidjam.backend

import de.lostmekka.covidjam.backend.levelgen.MutableLevel
import de.lostmekka.covidjam.backend.levelgen.Rect
import de.lostmekka.covidjam.backend.levelgen.combine
import de.lostmekka.covidjam.backend.levelgen.fromRange
import de.lostmekka.covidjam.backend.levelgen.generator.customGenerator
import de.lostmekka.covidjam.backend.levelgen.generator.shelveAreaGenerator
import de.lostmekka.covidjam.backend.levelgen.printDebug
import kotlin.math.roundToInt
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
    enum class Type {
        FloorTiles1, FloorTiles2, FloorTiles3,
        FloorStone1, FloorStone2,
        Wall1, Wall2, Wall3, Wall4, Wall5, Wall6, Wall7, Wall8, Wall9,
        DoorHorizontal, DoorVertical
    }
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
        customGenerator {
            val levelBounds = area.bounds
            val levelInsideRect = levelBounds.withAlteredSize(-1)
            val levelBorderArea = levelBounds - levelInsideRect

            val entranceHeight = 4
            val entranceWidth = 6
            val minEntranceStart = 1
            val maxEntranceStart = level.bounds.w - 2 - entranceWidth
            val entranceStart =
                if (maxEntranceStart <= minEntranceStart) minEntranceStart
                else Random.fromRange(minEntranceStart, maxEntranceStart)
            val entranceRect = Rect(
                x = entranceStart,
                y = levelBounds.yEnd - entranceHeight,
                w = entranceWidth,
                h = entranceHeight
            )
            val entranceRectCenter = entranceRect.withAlteredSize(left = -2, right = -2)
            val normalWallArea = levelBorderArea - entranceRectCenter
            val entranceDoorArea = levelBorderArea intersect entranceRectCenter
            val entranceFloorArea = entranceRect.minus(entranceRectCenter, levelBorderArea)
            val entranceCorridorArea = entranceRectCenter - levelBorderArea

            val openAreaWidth = Random.fromRange(5, levelBounds.w / 2)
            val openAreaHeight = Random.fromRange(4, (levelBounds.w * 0.4).roundToInt())
            val minOpenAreaStart = 1
            val maxOpenAreaStart = level.bounds.w - 2 - entranceWidth
            val openAreaStart =
                if (maxOpenAreaStart <= minOpenAreaStart) minOpenAreaStart
                else Random.fromRange(minOpenAreaStart, maxOpenAreaStart)
            val openAreaRect = Rect(
                x = openAreaStart,
                y = levelBounds.yEnd - 1 - openAreaHeight,
                w = openAreaWidth,
                h = openAreaHeight
            )
            val openArea = (openAreaRect intersect levelInsideRect) - entranceRect

            val shelveAreas = levelInsideRect
                .withAlteredSize(-1)
                .splitRandomly(
                    horizontally = false,
                    borderWidth = 1,
                    minSize = 6,
                    maxSize = 15,
                    includeBorderRects = false
                )
                .flatMap {
                    val minSize = Random.fromRange(5, 10)
                    val maxSize = minSize + Random.fromRange(5, 10)
                    it.splitRandomly(
                        horizontally = true,
                        borderWidth = 1,
                        minSize = minSize,
                        maxSize = maxSize,
                        includeBorderRects = false
                    )
                }
            val combinedShelveArea = shelveAreas.combine()
            val shelveCorridorArea = levelInsideRect.minus(entranceRect, openAreaRect, combinedShelveArea)

            for (shelveArea in shelveAreas) {
                shelveAreaGenerator(
                    horizontal = Random.nextBoolean(),
                    useTallShelves = Random.nextBoolean(),
                    doubleShelveColumn = Random.nextBoolean(),
                    corridorWidth = 2
                ).generate(shelveArea.minus(entranceRect, openAreaRect))
            }
            shelveCorridorArea.fill(Tile.Type.FloorTiles1)
            openArea.fill(Tile.Type.FloorTiles2)
            normalWallArea.fill(Tile.Type.Wall1)
            entranceFloorArea.fill(Tile.Type.FloorTiles3)
            entranceCorridorArea.fill(Tile.Type.FloorTiles1)
            entranceDoorArea.fill(Tile.Type.DoorHorizontal)
            level.entrancePoints += entranceDoorArea
        }

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
