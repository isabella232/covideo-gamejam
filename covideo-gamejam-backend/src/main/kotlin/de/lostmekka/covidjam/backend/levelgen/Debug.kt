package de.lostmekka.covidjam.backend.levelgen

import de.lostmekka.covidjam.backend.Entity
import de.lostmekka.covidjam.backend.Point
import de.lostmekka.covidjam.backend.Tile

private fun Tile.Type.toDebugString() = when (this) {
    Tile.Type.Floor -> ".."
    Tile.Type.Wall -> "WW"
    Tile.Type.Door -> "DD"
}

private fun Entity.Type.toDebugString() = when (this) {
    Entity.Type.ShelveSmall1,
    Entity.Type.ShelveSmall2,
    Entity.Type.ShelveSmall3
    -> "ss"
    Entity.Type.ShelveTall1,
    Entity.Type.ShelveTall2,
    Entity.Type.ShelveTall3
    -> "SS"
    Entity.Type.PaperRollScrappy,
    Entity.Type.PaperRollBad,
    Entity.Type.PaperRollGood,
    Entity.Type.PaperRollPrint,
    Entity.Type.PaperRollScented
    -> "tp"
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
