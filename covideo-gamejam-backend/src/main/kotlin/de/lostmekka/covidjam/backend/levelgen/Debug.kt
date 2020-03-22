package de.lostmekka.covidjam.backend.levelgen

import de.lostmekka.covidjam.backend.Entity
import de.lostmekka.covidjam.backend.Point
import de.lostmekka.covidjam.backend.Tile
import de.lostmekka.covidjam.backend.Tile.Type.*

private fun Tile.Type.toDebugString() = when (this) {
    in TypeGroups.floor.all -> ".."
    in TypeGroups.wall.all -> "WW"
    DoorHorizontal -> "--"
    DoorVertical -> "|."
    else -> null
}

private fun Entity.Type.toDebugString() = when (this) {
    in TypeGroups.shelve.small -> "ss"
    in TypeGroups.shelve.tall -> "SS"
    in TypeGroups.toiletPaper.all -> "tp"
    else -> null
}

fun MutableLevel.printDebug() {
    val entities = entities.groupBy { it.pos }
    val tiles = tiles.associateBy { it.pos }
    for (y in bounds.yRange) {
        val line = bounds.xRange.joinToString("") { x ->
            val pos = Point(x, y)
            entities[pos]?.firstOrNull()?.type?.toDebugString()
                ?: tiles[pos]?.type?.toDebugString()
                ?: "??"
        }
        println(line)
    }
}
