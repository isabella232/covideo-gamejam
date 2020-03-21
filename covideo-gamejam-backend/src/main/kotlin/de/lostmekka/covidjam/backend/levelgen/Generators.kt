package de.lostmekka.covidjam.backend.levelgen

import de.lostmekka.covidjam.backend.Tile

class FloorGenerator : Generator<Area> {
    override fun generate(area: Area, level: MutableLevel) {
        area.map { it.toTile(Tile.Type.Floor) }
            .addTilesToLevel(level)
    }
}

class RoomGenerator(
    val inner: Generator<Rect>
) : Generator<Rect> {
    override fun generate(area: Rect, level: MutableLevel) {
        val innerRect = Rect(area.x + 1, area.y + 1, area.w - 2, area.h - 2)
        (area - innerRect)
            .map { it.toTile(Tile.Type.Wall) }
            .addTilesToLevel(level)
        inner.generate(innerRect, level)
    }
}

object ShelveAreaGenerator: Generator<Rect> {
    override fun generate(area: Rect, level: MutableLevel) {
        FloorGenerator().generate(area, level)
        // TODO: place entities
    }
}
