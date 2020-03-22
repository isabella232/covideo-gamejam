package de.lostmekka.covidjam.backend.levelgen.generator

import de.lostmekka.covidjam.backend.Entity
import de.lostmekka.covidjam.backend.Point
import de.lostmekka.covidjam.backend.Tile
import de.lostmekka.covidjam.backend.levelgen.Generator
import de.lostmekka.covidjam.backend.levelgen.TypeGroups

fun floorGenerator() = fillGenerator(Tile.Type.FloorTiles1)

fun roomGenerator(
    inner: Generator
) = borderGenerator(
    innerGenerator = inner,
    borderGenerator = fillGenerator(Tile.Type.Wall1)
)

fun shelveAreaGenerator(
    horizontal: Boolean,
    doubleShelveColumn: Boolean,
    useTallShelves: Boolean,
    corridorWidth: Int = 2
) = customGenerator {
    floorGenerator().generate()

    val entityTypes = if (useTallShelves) TypeGroups.shelve.tall else TypeGroups.shelve.small
    val bounds = area.bounds
    val columnWidth = if (doubleShelveColumn) 2 else 1
    val totalCorridorWidth = corridorWidth + columnWidth
    val areaWidth = if (horizontal) bounds.h else bounds.w
    val areaLength = if (horizontal) bounds.w else bounds.h
    val areaVirtualWidth = areaWidth + corridorWidth
    val startOffset = areaVirtualWidth % totalCorridorWidth / 2 // what if this is odd?
    val columnCount = areaVirtualWidth / totalCorridorWidth

    for (columnIndex in 0 until columnCount) {
        for (columnX in 0 until columnWidth) {
            for (localY in 0 until areaLength) {
                val localX = startOffset + columnIndex * totalCorridorWidth + columnX
                val pos = if (horizontal) {
                    Point(bounds.x + localY, bounds.y + localX)
                } else {
                    Point(bounds.x + localX, bounds.y + localY)
                }
                if (pos in area) level += Entity(pos, entityTypes.random())
            }
        }
    }
}
