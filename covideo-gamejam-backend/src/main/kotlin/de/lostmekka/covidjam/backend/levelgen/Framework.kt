package de.lostmekka.covidjam.backend.levelgen

import de.lostmekka.covidjam.backend.Point
import de.lostmekka.covidjam.backend.Tile
import de.lostmekka.covidjam.backend.TileType
import kotlin.math.abs
import kotlin.math.min

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
