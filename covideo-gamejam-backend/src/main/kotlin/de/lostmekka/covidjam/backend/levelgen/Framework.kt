package de.lostmekka.covidjam.backend.levelgen

import de.lostmekka.covidjam.backend.Entity
import de.lostmekka.covidjam.backend.Point
import de.lostmekka.covidjam.backend.Tile
import kotlin.math.abs
import kotlin.math.min

class MutableLevel(
    tiles: List<Tile> = listOf(),
    entities: List<Entity> = listOf()
) {
    private val tilesInternal: MutableMap<Point, Tile> = tiles.associateBy { it.pos }.toMutableMap()
    private val entitiesInternal: MutableList<Entity> = entities.toMutableList()

    val tiles: List<Tile> get() = tilesInternal.values.toList()
    val entities: List<Entity> = entitiesInternal

    operator fun plus(other: MutableLevel) = MutableLevel(
        tiles = tiles + other.tiles,
        entities = entities + other.entities
    )

    fun addTiles(tiles: Iterable<Tile>) {
        tiles.associateByTo(tilesInternal) { it.pos }
    }

    fun addEntities(entities: Iterable<Entity>) {
        entitiesInternal += entities
    }
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
    fun generate(area: T, level: MutableLevel)
}
