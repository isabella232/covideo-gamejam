package de.lostmekka.covidjam.backend.levelgen

import de.lostmekka.covidjam.backend.Entity
import de.lostmekka.covidjam.backend.Point
import de.lostmekka.covidjam.backend.Tile
import de.lostmekka.covidjam.backend.Tile.Type.*

private fun String.coloredFg(color: Int) = "\u001B[${30 + color % 8}m$this\u001B[0m"
private fun String.coloredBg(color: Int) = "\u001B[${40 + color % 8}m$this\u001B[0m"


private fun Tile.Type.toDebugString() = when (this) {
    in TypeGroups.floor.all -> "..".coloredBg(TypeGroups.floor.all.indexOf(this))
    in TypeGroups.wall.all -> "WW".coloredFg(TypeGroups.wall.all.indexOf(this))
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
