package de.lostmekka.covidjam.backend.levelgen.generator

import de.lostmekka.covidjam.backend.Tile
import de.lostmekka.covidjam.backend.levelgen.Area
import de.lostmekka.covidjam.backend.levelgen.Generator
import de.lostmekka.covidjam.backend.levelgen.MutableLevel
import de.lostmekka.covidjam.backend.levelgen.Rect
import de.lostmekka.covidjam.backend.levelgen.addTilesToLevel
import de.lostmekka.covidjam.backend.levelgen.toTile

fun fillGenerator(
    tileTypes: List<Tile.Type>
) = object : Generator<Area> {
    override fun generate(area: Area, level: MutableLevel) {
        area.map { it.toTile(tileTypes.random()) }
            .addTilesToLevel(level)
    }
}

fun fillGenerator(
    tileType: Tile.Type
) = object : Generator<Area> {
    override fun generate(area: Area, level: MutableLevel) {
        area.map { it.toTile(tileType) }
            .addTilesToLevel(level)
    }
}

fun borderGenerator(
    innerGenerator: Generator<Rect>,
    borderGenerator: Generator<Area>,
    rightBorder: Int = 1,
    topBorder: Int = 1,
    leftBorder: Int = 1,
    bottomBorder: Int = 1
) = object : Generator<Rect> {
    override fun generate(area: Rect, level: MutableLevel) {
        val innerRect = Rect(
            x = area.x + leftBorder,
            y = area.y + topBorder,
            w = area.w - leftBorder - rightBorder,
            h = area.h - topBorder - bottomBorder
        )
        innerGenerator.generate(innerRect, level)
        borderGenerator.generate(area - innerRect, level)
    }
}
