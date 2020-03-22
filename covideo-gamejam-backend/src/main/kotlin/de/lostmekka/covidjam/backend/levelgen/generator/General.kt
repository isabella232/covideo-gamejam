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
) = object : Generator {
    override fun generate(area: Area, level: MutableLevel) {
        area.map { it.toTile(tileTypes.random()) }
            .addTilesToLevel(level)
    }
}

fun fillGenerator(
    tileType: Tile.Type
) = object : Generator {
    override fun generate(area: Area, level: MutableLevel) {
        area.map { it.toTile(tileType) }
            .addTilesToLevel(level)
    }
}

fun borderGenerator(
    innerGenerator: Generator,
    borderGenerator: Generator,
    rightBorder: Int = 1,
    topBorder: Int = 1,
    leftBorder: Int = 1,
    bottomBorder: Int = 1
) = object : Generator {
    override fun generate(area: Area, level: MutableLevel) {
        val innerRect = Rect(
            x = area.bounds.x + leftBorder,
            y = area.bounds.y + topBorder,
            w = area.bounds.w - leftBorder - rightBorder,
            h = area.bounds.h - topBorder - bottomBorder
        )
        innerGenerator.generate(innerRect, level)
        borderGenerator.generate(area - innerRect, level)
    }
}

fun customGenerator(
    generator: CustomGeneratorDslBody.() -> Unit
) = object : Generator {
    override fun generate(area: Area, level: MutableLevel) {
        CustomGeneratorDslBody(area, level).generator()
    }
}

class CustomGeneratorDslBody(
    val area: Area,
    val level: MutableLevel
) {
    fun Generator.generate() = generate(area, level)
    fun Generator.generate(area: Area) = generate(area, level)
    fun fill(tileType: Tile.Type) = fillGenerator(tileType).generate(area, level)
    fun Area.fill(tileType: Tile.Type) = fillGenerator(tileType).generate(this, level)
}
