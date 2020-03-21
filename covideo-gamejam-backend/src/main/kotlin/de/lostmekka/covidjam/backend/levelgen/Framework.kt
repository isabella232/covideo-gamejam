package de.lostmekka.covidjam.backend.levelgen

import de.lostmekka.covidjam.backend.Entity
import de.lostmekka.covidjam.backend.Point
import de.lostmekka.covidjam.backend.Tile
import kotlin.math.abs
import kotlin.math.min

class MutableLevel(
    val bounds: Rect,
    tiles: List<Tile> = listOf(),
    entities: List<Entity> = listOf()
) {
    private val tilesInternal: MutableMap<Point, Tile> = tiles.associateBy { it.pos }.toMutableMap()
    private val entitiesInternal: MutableList<Entity> = entities.toMutableList()

    val tiles: List<Tile> get() = tilesInternal.values.toList()
    val entities: List<Entity> = entitiesInternal

    operator fun plus(other: MutableLevel) = MutableLevel(
        bounds = bounds,
        tiles = tiles + other.tiles,
        entities = entities + other.entities
    )

    fun addTiles(tiles: Iterable<Tile>) {
        tiles.associateByTo(tilesInternal) { it.pos }
    }

    operator fun plusAssign(tile: Tile) {
        tilesInternal[tile.pos] = tile
    }

    fun addEntities(entities: Iterable<Entity>) {
        entitiesInternal += entities
    }

    operator fun plusAssign(entity: Entity) {
        entitiesInternal += entity
    }
}

abstract class Area : Iterable<Point> {
    abstract val bounds: Rect
    abstract operator fun contains(point: Point): Boolean
    operator fun plus(other: Area) = PointCloud(filter { it !in other }.toSet())
    operator fun minus(other: Area) = PointCloud(toSet() - other.toSet())
}

class PointCloud(val points: Set<Point>) : Area() {
    // TODO: hide Area implementations behind interfaces and provide factory functions.
    // (then we can pass inferred bounds via constructor, making things faster)
    override val bounds by lazy {
        val xValues = points.map { it.x }
        val yValues = points.map { it.y }
        val xMin = xValues.min() ?: 0
        val xMax = xValues.max() ?: 0
        val yMin = yValues.min() ?: 0
        val yMax = yValues.max() ?: 0
        Rect(xMin, yMin, xMax - xMin, yMax - yMin)
    }

    override fun iterator() = points.iterator()
    override fun contains(point: Point) = point in points
}

open class Rect(
    val x: Int,
    val y: Int,
    val w: Int,
    val h: Int
) : Area() {
    override val bounds get() = this

    override fun iterator() = iterator {
        for (xx in x until x + w) {
            for (yy in y until y + h) {
                yield(Point(xx, yy))
            }
        }
    }

    override fun contains(point: Point) = point.x in xRange && point.y in yRange

    val xRange = x until x + w
    val yRange = y until y + h
}

operator fun Point.rangeTo(other: Point) = Rect(
    x = min(x, other.x),
    y = min(y, other.y),
    w = abs(x - other.x),
    h = abs(y - other.y)
)

interface Generator {
    fun generate(area: Area, level: MutableLevel)
}
