package de.lostmekka.covidjam.backend.levelgen

import de.lostmekka.covidjam.backend.Entity
import de.lostmekka.covidjam.backend.Point
import de.lostmekka.covidjam.backend.Tile
import kotlin.math.abs
import kotlin.math.min
import kotlin.random.Random

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
    val xEnd = x + w
    val yEnd = y + h
    val area = w * h

    infix fun overlapsWithX(other: Rect) = x in other.x until other.xEnd || other.x in x until xEnd
    infix fun overlapsWithY(other: Rect) = y in other.y until other.yEnd || other.y in y until yEnd
    infix fun touchesY(other: Rect) = x == other.xEnd || other.x == xEnd
    infix fun touchesX(other: Rect) = y == other.yEnd || other.y == yEnd

    infix fun overlapsWith(other: Rect) = this overlapsWithX other && this overlapsWithY other
    infix fun touches(other: Rect) =
        (this overlapsWithX other && this touchesX other) || (this overlapsWithY other && this touchesY other)

    fun split(
        horizontally: Boolean,
        at: Int,
        minSideLength: Int = 1
    ): Pair<Rect, Rect>? {
        val limitingSize = if (horizontally) h else w
        return when {
            at < minSideLength || at > limitingSize - minSideLength -> null
            horizontally -> Pair(
                Rect(x, y, w, at),
                Rect(x, y + at, w, h - at)
            )
            else -> Pair(
                Rect(x, y, at, h),
                Rect(x + at, y, w - at, h)
            )
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun split(
        horizontally: Boolean,
        lengths: List<Int>
    ): List<Rect> {
        val limitingSize = if (horizontally) h else w
        val lengthSum = lengths.sum()
        if (lengthSum > limitingSize) {
            throw IllegalArgumentException("cannot split rects with new sizes that sum up to more than the original size")
        }
        val rectCoords = lengths
            .toMutableList()
            .also {
                it.add(0, 0)
                if (lengthSum < limitingSize) it.add(limitingSize - lengthSum)
            }
            .scanReduce { a, b -> a + b }
            .zipWithNext()
        return if (horizontally) {
            rectCoords.map { (y1, y2) -> Rect(x, y + y1, w, y2 - y1) }
        } else {
            rectCoords.map { (x1, x2) -> Rect(x + x1, y, x2 - x1, h) }
        }
    }

    fun splitRandomly(
        horizontally: Boolean,
        minSize: Int,
        maxSize: Int,
        borderWidth: Int
    ): List<Rect> {
        if (borderWidth < 0) throw IllegalArgumentException("border width must not be negative")
        val relevantLength = if (horizontally) h else w

        val minChunkCount = (relevantLength - borderWidth) / (minSize + borderWidth)
        val maxChunkCount = (relevantLength - borderWidth) / (maxSize + borderWidth)
        val chunkCount = when {
            minChunkCount >= maxChunkCount -> minChunkCount
            else -> Random.nextInt(minChunkCount, maxChunkCount + 1)
        }
        if (chunkCount <= 1) return listOf(this)
        val lengths = IntArray(chunkCount) { Random.nextInt(minSize, maxSize + 1) }

        val currentTotalLength = lengths.sum() + (chunkCount - 1) * borderWidth
        val lengthDiff = relevantLength - currentTotalLength
        if (lengthDiff > 0) {
            repeat(lengthDiff) {
                val index = lengths
                    .mapIndexed { index, length -> if (length < maxSize) index else null }
                    .filterNotNull()
                    .random()
                lengths[index]++
            }
        }
        if (lengthDiff < 0) {
            repeat(-lengthDiff) {
                val index = lengths
                    .mapIndexed { index, length -> if (length > minSize) index else null }
                    .filterNotNull()
                    .random()
                lengths[index]--
            }
        }

        val finalLengths = lengths
            .flatMap { listOf(it, borderWidth) }
            .dropLast(1)
        return split(horizontally, finalLengths)
    }
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
