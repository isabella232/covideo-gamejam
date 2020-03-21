package de.lostmekka.covidjam.backend

import kotlin.math.abs
import kotlin.math.min

data class LevelGenerationInput(
    val levelSize: Dimension
)

class LevelGenerationOutput(
    val tiles: List<Tile>
)

data class Dimension(val w: Int, val h: Int)
data class Point(val x: Int, val y: Int)

data class Tile(
    val pos: Point,
    val type: TileType,
    val isEntrance: Boolean
)

enum class TileType { Floor, Wall, Shelve, Door }

object LevelGeneration : BackendEndpoint<LevelGenerationInput, LevelGenerationOutput> {
    override val path = "level/generate"

    override fun handleRequest(input: LevelGenerationInput): LevelGenerationOutput {
        val rect = Rect(0, 0, input.levelSize.w, input.levelSize.h)

        return LevelGenerationOutput(
            listOf(
                Tile(Point(0, 0), TileType.Floor, false),
                Tile(Point(0, 1), TileType.Floor, false),
                Tile(Point(1, 0), TileType.Floor, false),
                Tile(Point(1, 1), TileType.Floor, true)
            )
        )
    }
}

private typealias Area = MutableSet<Tile>

private data class Rect(val x: Int, val y: Int, val w: Int, val h: Int): Iterable<Point> {
    constructor(p1: Point, p2: Point) : this(min(p1.x, p2.x), min(p1.y, p2.y), abs(p1.x - p2.x), abs(p1.y - p2.y))

    override fun iterator() = iterator {
        for (xx in x until x + w) {
            for (yy in y until y + h) {
                yield(Point(xx, yy))
            }
        }
    }
}
