package de.lostmekka.covidjam.backend

import kotlin.math.abs
import kotlin.math.min

data class LevelGenerationInput(
    val levelSize: Dimension
)

data class LevelGenerationOutput(
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
        val generator = RoomGenerator(
            inner = FloorGenerator()
        )

        val rect = Rect(0, 0, input.levelSize.w, input.levelSize.h)
        val generated = generator.generate(rect)
        return LevelGenerationOutput(
            tiles = generated.tiles
        )
    }
}

class GeneratedArea(
    val tiles: List<Tile>
) {
    operator fun plus(other: GeneratedArea) = GeneratedArea(
        tiles = (tiles + other.tiles).distinctBy { it.pos }
    )
}

abstract class Area : Iterable<Point> {
    abstract operator fun contains(point: Point): Boolean
    operator fun plus(other: Area) = PointCloud(filter { it !in other }.toSet())
    operator fun minus(other: Area) = PointCloud(toSet() + other.toSet())
}

class PointCloud(val points: Set<Point>) : Area() {
    override fun iterator() = points.iterator()
    override fun contains(point: Point) = point in points
}

data class Rect(val x: Int, val y: Int, val w: Int, val h: Int) : Area() {
    constructor(p1: Point, p2: Point) : this(min(p1.x, p2.x), min(p1.y, p2.y), abs(p1.x - p2.x), abs(p1.y - p2.y))

    override fun iterator() = iterator {
        for (xx in x until x + w) {
            for (yy in y until y + h) {
                yield(Point(xx, yy))
            }
        }
    }

    override fun contains(point: Point) = point.x in x until x + w && point.y in y until y + h
}

interface Generator<in T : Area> {
    fun generate(area: T): GeneratedArea
}

class FloorGenerator : Generator<Area> {
    override fun generate(area: Area) =
        GeneratedArea(area.map { Tile(it, TileType.Floor, false) })
}

class RoomGenerator(
    val inner: Generator<Rect>
) : Generator<Rect> {
    override fun generate(area: Rect): GeneratedArea {
        val innerRect = Rect(area.x + 1, area.y + 1, area.w - 2, area.h - 2)
        val wallTiles = (area - innerRect).map { Tile(it, TileType.Wall, false) }
        return GeneratedArea(wallTiles) + inner.generate(innerRect)
    }
}
