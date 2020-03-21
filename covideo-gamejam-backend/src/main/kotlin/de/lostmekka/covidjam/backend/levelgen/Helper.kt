package de.lostmekka.covidjam.backend.levelgen

import de.lostmekka.covidjam.backend.Entity
import de.lostmekka.covidjam.backend.Point
import de.lostmekka.covidjam.backend.Tile

fun Point.toTile(type: Tile.Type) = Tile(this, type)
fun List<Tile>.addTilesToLevel(level: MutableLevel) {
    level.addTiles(this)
}
fun List<Entity>.addEntitiesToLevel(level: MutableLevel) {
    level.addEntities(this)
}
