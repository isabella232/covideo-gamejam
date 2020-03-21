package de.lostmekka.covidjam.backend.levelgen

import de.lostmekka.covidjam.backend.Tile
import de.lostmekka.covidjam.backend.TileType

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
