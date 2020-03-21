package de.lostmekka.covidjam.backend.levelgen

import de.lostmekka.covidjam.backend.TileType

class FloorGenerator : Generator<Area> {
    override fun generate(area: Area) =
        area.map { it.toTile(TileType.Floor) }
            .toGeneratedArea()
}

class RoomGenerator(
    val inner: Generator<Rect>
) : Generator<Rect> {
    override fun generate(area: Rect): GeneratedArea {
        val innerRect = Rect(area.x + 1, area.y + 1, area.w - 2, area.h - 2)
        val walls = (area - innerRect)
            .map { it.toTile(TileType.Wall) }
            .toGeneratedArea()
        return walls + inner.generate(innerRect)
    }
}
